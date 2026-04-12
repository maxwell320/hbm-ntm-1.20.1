package com.hbm.ntm.common.block.entity;

import com.hbm.ntm.common.block.IcfControllerBlock;
import com.hbm.ntm.common.block.IcfLaserComponentBlock;
import com.hbm.ntm.common.block.IcfLaserComponentPart;
import com.hbm.ntm.common.config.IcfControllerMachineConfig;
import com.hbm.ntm.common.energy.EnergyConnectionMode;
import com.hbm.ntm.common.energy.HbmEnergyStorage;
import com.hbm.ntm.common.registration.HbmBlockEntityTypes;
import com.hbm.ntm.common.registration.HbmBlocks;
import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Deque;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.energy.IEnergyStorage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("null")
public class IcfControllerBlockEntity extends MachineBlockEntity {
    private static final int MAX_ASSEMBLY_SIZE = 1024;
    private static final float BREAK_BEAM_HARDNESS = 50.0F;

    private boolean assembled;
    private int cellCount;
    private int emitterCount;
    private int capacitorCount;
    private int turbochargerCount;
    private int beamLength;
    private long beamPower;
    private long maxBeamPower;
    private Set<BlockPos> componentPositions = new LinkedHashSet<>();
    private Set<BlockPos> portPositions = new LinkedHashSet<>();

    public IcfControllerBlockEntity(final BlockPos pos, final BlockState state) {
        super(HbmBlockEntityTypes.MACHINE_ICF_CONTROLLER.get(), pos, state, 0);
    }

    @Override
    protected @Nullable HbmEnergyStorage createEnergyStorage() {
        final int capacity = Math.max(1, IcfControllerMachineConfig.INSTANCE.energyBuffer());
        return this.createSimpleEnergyStorage(capacity, capacity, 0);
    }

    @Override
    protected EnergyConnectionMode getEnergyConnectionMode(final @Nullable Direction side) {
        return EnergyConnectionMode.RECEIVE;
    }

    public static void serverTick(final Level level, final BlockPos pos, final BlockState state, final IcfControllerBlockEntity controller) {
        boolean dirty = false;

        if (controller.assembled && level.getGameTime() % 20L == 0L && !controller.isAssemblyStillValid()) {
            controller.disassemble();
            dirty = true;
        }

        final long currentMaxPower = controller.getCurrentMaxBeamPower();
        final long availablePower = Math.min(controller.getStoredEnergy(), currentMaxPower);
        int newBeamLength = 0;

        if (controller.assembled && availablePower > 0L && currentMaxPower > 0L) {
            newBeamLength = controller.emitBeam(availablePower);
            final IEnergyStorage storage = controller.getEnergyStorage(null);
            if (storage != null) {
                storage.extractEnergy((int) Math.min(Integer.MAX_VALUE, availablePower), false);
            }
        }

        if (controller.updateBeamState(newBeamLength, availablePower, currentMaxPower)) {
            dirty = true;
        }

        if (dirty) {
            controller.markChangedAndSync();
        }
        controller.tickMachineStateSync();
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.translatable(HbmBlocks.MACHINE_ICF_CONTROLLER.get().getDescriptionId());
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(final int containerId, final @NotNull Inventory inventory, final @NotNull Player player) {
        return null;
    }

    public boolean tryAssemble(final @Nullable Player player) {
        if (this.level == null || this.level.isClientSide()) {
            return false;
        }

        this.disassemble();
        final AssemblyScanResult scanResult = this.scanAssembly();
        if (!scanResult.success()) {
            if (player != null) {
                player.displayClientMessage(Component.literal(scanResult.error()), true);
            }
            return false;
        }

        this.assembled = true;
        this.cellCount = scanResult.cellCount();
        this.emitterCount = scanResult.emitterCount();
        this.capacitorCount = scanResult.capacitorCount();
        this.turbochargerCount = scanResult.turbochargerCount();
        this.componentPositions = new LinkedHashSet<>(scanResult.components());
        this.portPositions = new LinkedHashSet<>(scanResult.ports());
        this.setComponentControllerPointers(this.worldPosition);
        this.markChangedAndSync();
        return true;
    }

    public void onComponentBroken(final BlockPos componentPos) {
        if (this.assembled && this.componentPositions.contains(componentPos)) {
            this.disassemble();
        }
    }

    public boolean isAssembled() {
        return this.assembled;
    }

    public int receivePortEnergy(final int amount, final BlockPos portPos, final boolean simulate) {
        if (amount <= 0 || !this.assembled || !this.portPositions.contains(portPos)) {
            return 0;
        }
        final IEnergyStorage storage = this.getEnergyStorage(null);
        return storage == null ? 0 : storage.receiveEnergy(amount, simulate);
    }

    public int getPortEnergyStored() {
        return this.getStoredEnergy();
    }

    public int getPortEnergyCapacity() {
        return this.getMaxStoredEnergy();
    }

    public int getBeamLength() {
        return this.beamLength;
    }

    public long getBeamPower() {
        return this.beamPower;
    }

    public long getMaxBeamPower() {
        return this.maxBeamPower;
    }

    public int getCellCount() {
        return this.cellCount;
    }

    public int getEmitterCount() {
        return this.emitterCount;
    }

    public int getCapacitorCount() {
        return this.capacitorCount;
    }

    public int getTurbochargerCount() {
        return this.turbochargerCount;
    }

    @Override
    public Map<com.hbm.ntm.common.item.MachineUpgradeItem.UpgradeType, Integer> getValidUpgrades() {
        return Map.of();
    }

    private AssemblyScanResult scanAssembly() {
        if (this.level == null) {
            return AssemblyScanResult.error("Controller world is unavailable.");
        }

        final Direction facing = this.getBlockState().getValue(IcfControllerBlock.FACING);
        final BlockPos seed = this.worldPosition.relative(facing.getOpposite());
        final Set<BlockPos> visited = new LinkedHashSet<>();
        final Set<BlockPos> components = new LinkedHashSet<>();
        final Set<BlockPos> ports = new LinkedHashSet<>();
        final Set<BlockPos> cells = new LinkedHashSet<>();
        final Set<BlockPos> emitters = new LinkedHashSet<>();
        final Set<BlockPos> capacitors = new LinkedHashSet<>();
        final Set<BlockPos> turbos = new LinkedHashSet<>();
        final Deque<BlockPos> frontier = new ArrayDeque<>();

        visited.add(this.worldPosition);
        frontier.add(seed);

        while (!frontier.isEmpty()) {
            final BlockPos current = frontier.removeFirst();
            if (!visited.add(current)) {
                continue;
            }
            if (visited.size() > MAX_ASSEMBLY_SIZE) {
                return AssemblyScanResult.error("ICF assembly exceeds max size " + MAX_ASSEMBLY_SIZE + ".");
            }

            final BlockState state = this.level.getBlockState(current);
            if (!state.is(HbmBlocks.MACHINE_ICF_LASER_COMPONENT.get())) {
                return AssemblyScanResult.error("Assembly blocked by non-component at " + current.toShortString() + ".");
            }

            final IcfLaserComponentPart part = state.getValue(IcfLaserComponentBlock.PART);
            components.add(current.immutable());
            switch (part) {
                case PORT -> ports.add(current.immutable());
                case CELL -> cells.add(current.immutable());
                case EMITTER -> emitters.add(current.immutable());
                case CAPACITOR -> capacitors.add(current.immutable());
                case TURBO -> turbos.add(current.immutable());
                case CASING -> {
                }
            }

            if (part.propagatesAssembly()) {
                for (final Direction direction : Direction.values()) {
                    frontier.add(current.relative(direction));
                }
            }
        }

        final Set<BlockPos> validCells = new LinkedHashSet<>();
        int cellCount = 0;
        for (int i = 1; i <= MAX_ASSEMBLY_SIZE; i++) {
            final BlockPos check = this.worldPosition.relative(facing.getOpposite(), i);
            if (!cells.contains(check)) {
                break;
            }
            validCells.add(check);
            cellCount++;
        }

        final Set<BlockPos> validEmitters = this.findAdjacents(emitters, validCells);
        final Set<BlockPos> validCapacitors = this.findAdjacents(capacitors, validEmitters);
        final Set<BlockPos> validTurbos = this.findAdjacents(turbos, validCapacitors);

        return AssemblyScanResult.success(
            components,
            ports,
            cellCount,
            validEmitters.size(),
            validCapacitors.size(),
            validTurbos.size()
        );
    }

    private Set<BlockPos> findAdjacents(final Set<BlockPos> source, final Set<BlockPos> adjacentTo) {
        final Set<BlockPos> valid = new LinkedHashSet<>();
        if (adjacentTo.isEmpty()) {
            return valid;
        }
        for (final BlockPos candidate : source) {
            for (final Direction direction : Direction.values()) {
                if (adjacentTo.contains(candidate.relative(direction))) {
                    valid.add(candidate.immutable());
                    break;
                }
            }
        }
        return valid;
    }

    private long getCurrentMaxBeamPower() {
        final double capacitorTerm = Math.sqrt(Math.max(0, this.capacitorCount)) * Math.max(1, IcfControllerMachineConfig.INSTANCE.capacitorPower());
        final double turboTerm = Math.sqrt(Math.max(0, Math.min(this.turbochargerCount, this.capacitorCount))) * Math.max(1, IcfControllerMachineConfig.INSTANCE.turboPower());
        return Math.max(0L, Math.round(capacitorTerm + turboTerm));
    }

    private int emitBeam(final long power) {
        if (this.level == null || power <= 0L) {
            return 0;
        }
        final Direction facing = this.getBlockState().getValue(IcfControllerBlock.FACING);
        final int maxLength = Math.max(1, IcfControllerMachineConfig.INSTANCE.maxBeamLength());

        for (int step = 1; step <= maxLength; step++) {
            final BlockPos scanPos = this.worldPosition.relative(facing, step);
            final BlockState scanState = this.level.getBlockState(scanPos);

            if (scanState.is(HbmBlocks.MACHINE_ICF.get())) {
                final BlockPos reactorPos = scanPos.relative(facing, 8).below(3);
                if (this.level.getBlockEntity(reactorPos) instanceof final IcfBlockEntity reactor) {
                    reactor.injectExternalLaser(power, this.getCurrentMaxBeamPower());
                }
                this.damageBeamEntities(facing, step);
                return step;
            }

            if (!scanState.isAir()) {
                final float hardness = scanState.getDestroySpeed(this.level, scanPos);
                if (hardness >= 0.0F && hardness < BREAK_BEAM_HARDNESS) {
                    this.level.destroyBlock(scanPos, false);
                }
                this.damageBeamEntities(facing, step);
                return step;
            }
        }

        this.damageBeamEntities(facing, maxLength);
        return maxLength;
    }

    private void damageBeamEntities(final Direction facing, final int length) {
        if (this.level == null || length <= 0) {
            return;
        }

        final double sx = this.worldPosition.getX() + 0.5D;
        final double sy = this.worldPosition.getY() + 0.5D;
        final double sz = this.worldPosition.getZ() + 0.5D;
        final double ex = sx + facing.getStepX() * length;
        final double ey = sy + facing.getStepY() * length;
        final double ez = sz + facing.getStepZ() * length;
        final AABB beamBox = new AABB(
            Math.min(sx, ex) - 0.3D,
            Math.min(sy, ey) - 0.3D,
            Math.min(sz, ez) - 0.3D,
            Math.max(sx, ex) + 0.3D,
            Math.max(sy, ey) + 0.3D,
            Math.max(sz, ez) + 0.3D
        );

        final List<Entity> entities = this.level.getEntitiesOfClass(Entity.class, beamBox);
        for (final Entity entity : entities) {
            entity.hurt(this.level.damageSources().inFire(), 50.0F);
            entity.setSecondsOnFire(5);
        }
    }

    private boolean isAssemblyStillValid() {
        if (this.level == null || !this.assembled) {
            return false;
        }

        for (final BlockPos componentPos : this.componentPositions) {
            final BlockState state = this.level.getBlockState(componentPos);
            if (!state.is(HbmBlocks.MACHINE_ICF_LASER_COMPONENT.get())) {
                return false;
            }
            if (this.portPositions.contains(componentPos) && state.getValue(IcfLaserComponentBlock.PART) != IcfLaserComponentPart.PORT) {
                return false;
            }
            if (this.level.getBlockEntity(componentPos) instanceof final IcfLaserComponentBlockEntity component) {
                final BlockPos controllerPos = component.getControllerPos();
                if (controllerPos == null || !controllerPos.equals(this.worldPosition)) {
                    return false;
                }
            }
        }

        return true;
    }

    private boolean updateBeamState(final int newLength, final long newPower, final long newMaxPower) {
        if (this.beamLength == newLength && this.beamPower == newPower && this.maxBeamPower == newMaxPower) {
            return false;
        }
        this.beamLength = Math.max(0, newLength);
        this.beamPower = Math.max(0L, newPower);
        this.maxBeamPower = Math.max(0L, newMaxPower);
        return true;
    }

    private void disassemble() {
        this.assembled = false;
        this.cellCount = 0;
        this.emitterCount = 0;
        this.capacitorCount = 0;
        this.turbochargerCount = 0;
        this.updateBeamState(0, 0L, 0L);
        this.setComponentControllerPointers(null);
        this.componentPositions.clear();
        this.portPositions.clear();
    }

    private void setComponentControllerPointers(final @Nullable BlockPos controllerPos) {
        if (this.level == null) {
            return;
        }
        for (final BlockPos componentPos : this.componentPositions) {
            if (this.level.getBlockEntity(componentPos) instanceof final IcfLaserComponentBlockEntity component) {
                if (controllerPos == null) {
                    component.clearControllerPos();
                } else {
                    component.setControllerPos(controllerPos);
                }
                component.setChanged();
            }
        }
    }

    @Override
    protected void saveMachineData(final @NotNull CompoundTag tag) {
        tag.putBoolean("assembled", this.assembled);
        tag.putInt("cellCount", this.cellCount);
        tag.putInt("emitterCount", this.emitterCount);
        tag.putInt("capacitorCount", this.capacitorCount);
        tag.putInt("turbochargerCount", this.turbochargerCount);
        tag.putInt("beamLength", this.beamLength);
        tag.putLong("beamPower", this.beamPower);
        tag.putLong("maxBeamPower", this.maxBeamPower);
        tag.put("components", writePositionList(this.componentPositions));
        tag.put("ports", writePositionList(this.portPositions));
    }

    @Override
    protected void loadMachineData(final @NotNull CompoundTag tag) {
        this.assembled = tag.getBoolean("assembled");
        this.cellCount = Math.max(0, tag.getInt("cellCount"));
        this.emitterCount = Math.max(0, tag.getInt("emitterCount"));
        this.capacitorCount = Math.max(0, tag.getInt("capacitorCount"));
        this.turbochargerCount = Math.max(0, tag.getInt("turbochargerCount"));
        this.beamLength = Math.max(0, tag.getInt("beamLength"));
        this.beamPower = Math.max(0L, tag.getLong("beamPower"));
        this.maxBeamPower = Math.max(0L, tag.getLong("maxBeamPower"));
        this.componentPositions = readPositionList(tag.getList("components", Tag.TAG_COMPOUND));
        this.portPositions = readPositionList(tag.getList("ports", Tag.TAG_COMPOUND));
    }

    @Override
    protected void writeAdditionalMachineStateSync(final CompoundTag tag) {
        tag.putBoolean("assembled", this.assembled);
        tag.putInt("cellCount", this.cellCount);
        tag.putInt("emitterCount", this.emitterCount);
        tag.putInt("capacitorCount", this.capacitorCount);
        tag.putInt("turbochargerCount", this.turbochargerCount);
        tag.putInt("beamLength", this.beamLength);
        tag.putLong("beamPower", this.beamPower);
        tag.putLong("maxBeamPower", this.maxBeamPower);
    }

    @Override
    protected void readMachineStateSync(final CompoundTag tag) {
        this.assembled = tag.getBoolean("assembled");
        this.cellCount = Math.max(0, tag.getInt("cellCount"));
        this.emitterCount = Math.max(0, tag.getInt("emitterCount"));
        this.capacitorCount = Math.max(0, tag.getInt("capacitorCount"));
        this.turbochargerCount = Math.max(0, tag.getInt("turbochargerCount"));
        this.beamLength = Math.max(0, tag.getInt("beamLength"));
        this.beamPower = Math.max(0L, tag.getLong("beamPower"));
        this.maxBeamPower = Math.max(0L, tag.getLong("maxBeamPower"));
    }

    private static ListTag writePositionList(final Collection<BlockPos> positions) {
        final ListTag list = new ListTag();
        for (final BlockPos pos : positions) {
            final CompoundTag entry = new CompoundTag();
            entry.putInt("x", pos.getX());
            entry.putInt("y", pos.getY());
            entry.putInt("z", pos.getZ());
            list.add(entry);
        }
        return list;
    }

    private static Set<BlockPos> readPositionList(final ListTag list) {
        final Set<BlockPos> positions = new LinkedHashSet<>();
        for (int i = 0; i < list.size(); i++) {
            final CompoundTag entry = list.getCompound(i);
            positions.add(new BlockPos(entry.getInt("x"), entry.getInt("y"), entry.getInt("z")));
        }
        return positions;
    }

    private record AssemblyScanResult(boolean success,
                                      String error,
                                      Set<BlockPos> components,
                                      Set<BlockPos> ports,
                                      int cellCount,
                                      int emitterCount,
                                      int capacitorCount,
                                      int turboCount) {
        private static AssemblyScanResult success(final Set<BlockPos> components,
                                                  final Set<BlockPos> ports,
                                                  final int cellCount,
                                                  final int emitterCount,
                                                  final int capacitorCount,
                                                  final int turboCount) {
            return new AssemblyScanResult(true, "", new LinkedHashSet<>(components), new LinkedHashSet<>(ports), cellCount, emitterCount, capacitorCount, turboCount);
        }

        private static AssemblyScanResult error(final String error) {
            return new AssemblyScanResult(false, error, Set.of(), Set.of(), 0, 0, 0, 0);
        }

        private int turbochargerCount() {
            return this.turboCount;
        }
    }
}