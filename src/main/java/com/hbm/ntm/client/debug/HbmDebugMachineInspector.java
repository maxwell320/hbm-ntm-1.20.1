package com.hbm.ntm.client.debug;

import com.hbm.ntm.HbmNtmMod;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.file.Path;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.registries.ForgeRegistries;

@SuppressWarnings("null")
public final class HbmDebugMachineInspector {

    private HbmDebugMachineInspector() {
    }

    public static Path dumpLookedAtMachine() throws IOException {
        final Minecraft mc = Minecraft.getInstance();
        final LocalPlayer player = mc.player;
        final ClientLevel level = mc.level;
        if (player == null || level == null) {
            return HbmDebugWriter.write("machine-dump-none", "No player or level available.\n");
        }
        final HitResult hit = mc.hitResult;
        if (!(hit instanceof BlockHitResult bhr) || bhr.getType() != HitResult.Type.BLOCK) {
            return HbmDebugWriter.write("machine-dump-none", "Player is not looking at a block.\n");
        }
        return HbmDebugWriter.write("machine-dump", describeMachine(level, bhr.getBlockPos()));
    }

    public static String describeMachine(final ClientLevel level, final BlockPos pos) {
        final StringBuilder sb = new StringBuilder(4096);
        sb.append("=== HBM machine dump ===\n");
        sb.append("timestamp: ").append(HbmDebugWriter.stamp()).append('\n');
        sb.append("pos: ").append(pos.getX()).append(',').append(pos.getY()).append(',').append(pos.getZ()).append('\n');

        final BlockState state = level.getBlockState(pos);
        sb.append("block: ").append(ForgeRegistries.BLOCKS.getKey(state.getBlock())).append('\n');
        sb.append("blockState: ").append(state).append('\n');

        final BlockEntity be = level.getBlockEntity(pos);
        if (be == null) {
            sb.append("(no block entity)\n");
            return sb.toString();
        }

        sb.append("beClass: ").append(be.getClass().getName()).append('\n');
        sb.append("beType: ").append(ForgeRegistries.BLOCK_ENTITY_TYPES.getKey(be.getType())).append('\n');

        sb.append("\n-- NBT (saveWithoutMetadata) --\n");
        try {
            final CompoundTag nbt = be.saveWithoutMetadata();
            sb.append(nbt).append('\n');
        } catch (final Throwable t) {
            sb.append("NBT save error: ").append(t.getClass().getSimpleName()).append(": ").append(t.getMessage()).append('\n');
        }

        sb.append("\n-- capabilities --\n");
        for (final Direction dir : Direction.values()) {
            describeCap(sb, be, dir);
        }
        describeCap(sb, be, null);

        sb.append("\n-- reflected fields (non-static) --\n");
        dumpFields(sb, be);

        return sb.toString();
    }

    private static void describeCap(final StringBuilder sb, final BlockEntity be, final Direction dir) {
        final String tag = dir == null ? "<null>" : dir.getName();
        be.getCapability(ForgeCapabilities.ENERGY, dir).ifPresent((final IEnergyStorage e) -> {
            sb.append("  energy[").append(tag).append("]: ").append(e.getEnergyStored())
              .append(" / ").append(e.getMaxEnergyStored())
              .append(" FE canExtract=").append(e.canExtract())
              .append(" canReceive=").append(e.canReceive())
              .append('\n');
        });
        be.getCapability(ForgeCapabilities.FLUID_HANDLER, dir).ifPresent((final IFluidHandler h) -> {
            sb.append("  fluid[").append(tag).append("] tanks=").append(h.getTanks()).append('\n');
            for (int i = 0; i < h.getTanks(); i++) {
                sb.append("    tank ").append(i).append(": ")
                  .append(h.getFluidInTank(i)).append(" cap=")
                  .append(h.getTankCapacity(i)).append('\n');
            }
        });
        be.getCapability(ForgeCapabilities.ITEM_HANDLER, dir).ifPresent((final IItemHandler h) -> {
            sb.append("  items[").append(tag).append("] slots=").append(h.getSlots()).append('\n');
            for (int i = 0; i < h.getSlots(); i++) {
                if (!h.getStackInSlot(i).isEmpty()) {
                    sb.append("    slot ").append(i).append(": ").append(h.getStackInSlot(i)).append('\n');
                }
            }
        });
    }

    private static void dumpFields(final StringBuilder sb, final Object target) {
        Class<?> c = target.getClass();
        while (c != null && c != Object.class) {
            for (final Field f : c.getDeclaredFields()) {
                if (Modifier.isStatic(f.getModifiers())) {
                    continue;
                }
                try {
                    f.setAccessible(true);
                    final Object v = f.get(target);
                    sb.append("  ").append(c.getSimpleName()).append('.').append(f.getName())
                      .append(" = ").append(shortRender(v)).append('\n');
                } catch (final ReflectiveOperationException ex) {
                    sb.append("  ").append(c.getSimpleName()).append('.').append(f.getName())
                      .append(" = <error>\n");
                }
            }
            c = c.getSuperclass();
        }
    }

    private static String shortRender(final Object v) {
        if (v == null) {
            return "null";
        }
        final String raw = String.valueOf(v);
        if (raw.length() <= 160) {
            return raw;
        }
        return raw.substring(0, 160) + "... (truncated " + (raw.length() - 160) + " chars)";
    }

    public static void logToChat(final String text) {
        HbmNtmMod.LOGGER.info("[hbm-debug] {}", text);
    }
}
