package com.hbm.ntm.common.command;

import com.hbm.ntm.HbmNtmMod;
import com.hbm.ntm.common.block.entity.MachineBlockEntity;
import com.hbm.ntm.common.pollution.PollutionSavedData;
import com.hbm.ntm.common.pollution.PollutionType;
import com.hbm.ntm.common.radiation.ChunkRadiationManager;
import com.hbm.ntm.common.saveddata.TomImpactSavedData;
import com.hbm.ntm.common.transfer.TransferGraphManager;
import com.hbm.ntm.common.transfer.TransferNetwork;
import com.hbm.ntm.common.transfer.TransferNetworkKind;
import com.hbm.ntm.common.transfer.TransferNodeProvider;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = HbmNtmMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
@SuppressWarnings("null")
public final class HbmRootCommand {
    private static final SuggestionProvider<CommandSourceStack> POLLUTION_TYPE_SUGGESTIONS =
        (context, builder) -> SharedSuggestionProvider.suggest(new String[]{"soot", "heavy_metal", "poison"}, builder);
    private static final DynamicCommandExceptionType INVALID_POLLUTION_TYPE =
        new DynamicCommandExceptionType(type -> Component.literal("Unknown pollution type: " + type));

    private HbmRootCommand() {
    }

    @SubscribeEvent
    public static void onRegisterCommands(final RegisterCommandsEvent event) {
        register(event.getDispatcher());
    }

    public static void register(final CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("hbm")
            .requires(source -> source.hasPermission(2))
            .then(Commands.literal("help").executes(HbmRootCommand::runHelp))
            .then(radiationCommand())
            .then(pollutionCommand())
            .then(tomCommand())
            .then(debugCommand())
            .then(Commands.literal("status").executes(HbmRootCommand::runStatus))
            .executes(HbmRootCommand::runHelp));
    }

    private static LiteralArgumentBuilder<CommandSourceStack> radiationCommand() {
        return Commands.literal("radiation")
            .then(Commands.literal("get")
                .executes(context -> radiationGet(context, sourcePos(context)))
                .then(Commands.argument("pos", BlockPosArgument.blockPos())
                    .executes(context -> radiationGet(context, BlockPosArgument.getLoadedBlockPos(context, "pos")))))
            .then(Commands.literal("set")
                .then(Commands.argument("amount", FloatArgumentType.floatArg(0.0F))
                    .executes(context -> radiationSet(context, sourcePos(context), FloatArgumentType.getFloat(context, "amount")))
                    .then(Commands.argument("pos", BlockPosArgument.blockPos())
                        .executes(context -> radiationSet(context,
                            BlockPosArgument.getLoadedBlockPos(context, "pos"),
                            FloatArgumentType.getFloat(context, "amount"))))))
            .then(Commands.literal("add")
                .then(Commands.argument("delta", FloatArgumentType.floatArg())
                    .executes(context -> radiationAdd(context, sourcePos(context), FloatArgumentType.getFloat(context, "delta")))
                    .then(Commands.argument("pos", BlockPosArgument.blockPos())
                        .executes(context -> radiationAdd(context,
                            BlockPosArgument.getLoadedBlockPos(context, "pos"),
                            FloatArgumentType.getFloat(context, "delta"))))))
            .then(Commands.literal("clear")
                .executes(context -> radiationSet(context, sourcePos(context), 0.0F))
                .then(Commands.argument("pos", BlockPosArgument.blockPos())
                    .executes(context -> radiationSet(context, BlockPosArgument.getLoadedBlockPos(context, "pos"), 0.0F))));
    }

    private static LiteralArgumentBuilder<CommandSourceStack> pollutionCommand() {
        return Commands.literal("pollution")
            .then(Commands.literal("get")
                .then(Commands.argument("type", StringArgumentType.word())
                    .suggests(POLLUTION_TYPE_SUGGESTIONS)
                    .executes(context -> pollutionGet(context, sourcePos(context)))
                    .then(Commands.argument("pos", BlockPosArgument.blockPos())
                        .executes(context -> pollutionGet(context, BlockPosArgument.getLoadedBlockPos(context, "pos"))))))
            .then(Commands.literal("add")
                .then(Commands.argument("type", StringArgumentType.word())
                    .suggests(POLLUTION_TYPE_SUGGESTIONS)
                    .then(Commands.argument("amount", FloatArgumentType.floatArg())
                        .executes(context -> pollutionAdd(
                            context,
                            sourcePos(context),
                            FloatArgumentType.getFloat(context, "amount"))
                        )
                        .then(Commands.argument("pos", BlockPosArgument.blockPos())
                            .executes(context -> pollutionAdd(
                                context,
                                BlockPosArgument.getLoadedBlockPos(context, "pos"),
                                FloatArgumentType.getFloat(context, "amount"))
                            )))))
            .then(Commands.literal("set")
                .then(Commands.argument("type", StringArgumentType.word())
                    .suggests(POLLUTION_TYPE_SUGGESTIONS)
                    .then(Commands.argument("amount", FloatArgumentType.floatArg(0.0F))
                        .executes(context -> pollutionSet(
                            context,
                            sourcePos(context),
                            FloatArgumentType.getFloat(context, "amount"))
                        )
                        .then(Commands.argument("pos", BlockPosArgument.blockPos())
                            .executes(context -> pollutionSet(
                                context,
                                BlockPosArgument.getLoadedBlockPos(context, "pos"),
                                FloatArgumentType.getFloat(context, "amount"))
                            )))));
    }

    private static LiteralArgumentBuilder<CommandSourceStack> tomCommand() {
        return Commands.literal("tom")
            .then(Commands.literal("get").executes(HbmRootCommand::tomGet))
            .then(Commands.literal("set")
                .then(Commands.literal("dust")
                    .then(Commands.argument("value", FloatArgumentType.floatArg(0.0F))
                        .executes(context -> tomSetDust(context, FloatArgumentType.getFloat(context, "value")))))
                .then(Commands.literal("fire")
                    .then(Commands.argument("value", FloatArgumentType.floatArg(0.0F))
                        .executes(context -> tomSetFire(context, FloatArgumentType.getFloat(context, "value")))))
                .then(Commands.literal("impact")
                    .then(Commands.argument("value", BoolArgumentType.bool())
                        .executes(context -> tomSetImpact(context, BoolArgumentType.getBool(context, "value"))))));
    }

    private static LiteralArgumentBuilder<CommandSourceStack> debugCommand() {
        return Commands.literal("debug")
            .then(Commands.literal("machine")
                .executes(context -> debugMachine(context, sourcePos(context)))
                .then(Commands.argument("pos", BlockPosArgument.blockPos())
                    .executes(context -> debugMachine(context, BlockPosArgument.getLoadedBlockPos(context, "pos")))))
            .then(Commands.literal("transfer")
                .executes(context -> debugTransfer(context, sourcePos(context)))
                .then(Commands.argument("pos", BlockPosArgument.blockPos())
                    .executes(context -> debugTransfer(context, BlockPosArgument.getLoadedBlockPos(context, "pos"))))
                .then(Commands.literal("rebuild")
                    .executes(context -> debugTransferRebuild(context, sourcePos(context)))
                    .then(Commands.argument("pos", BlockPosArgument.blockPos())
                        .executes(context -> debugTransferRebuild(context, BlockPosArgument.getLoadedBlockPos(context, "pos"))))))
            .then(Commands.literal("world")
                .executes(context -> debugWorld(context, sourcePos(context)))
                .then(Commands.argument("pos", BlockPosArgument.blockPos())
                    .executes(context -> debugWorld(context, BlockPosArgument.getLoadedBlockPos(context, "pos")))))
            .then(Commands.literal("worldgen")
                .executes(context -> debugWorldgen(context, sourcePos(context)))
                .then(Commands.argument("pos", BlockPosArgument.blockPos())
                    .executes(context -> debugWorldgen(context, BlockPosArgument.getLoadedBlockPos(context, "pos")))))
            .then(Commands.literal("entity")
                .executes(context -> debugEntities(context, sourcePos(context), 24.0F))
                .then(Commands.argument("radius", FloatArgumentType.floatArg(1.0F, 256.0F))
                    .executes(context -> debugEntities(context,
                        sourcePos(context),
                        FloatArgumentType.getFloat(context, "radius"))))
                .then(Commands.argument("pos", BlockPosArgument.blockPos())
                    .executes(context -> debugEntities(context,
                        BlockPosArgument.getLoadedBlockPos(context, "pos"),
                        24.0F))
                    .then(Commands.argument("radius", FloatArgumentType.floatArg(1.0F, 256.0F))
                        .executes(context -> debugEntities(context,
                            BlockPosArgument.getLoadedBlockPos(context, "pos"),
                            FloatArgumentType.getFloat(context, "radius"))))));
    }

    private static int runHelp(final CommandContext<CommandSourceStack> context) {
        final CommandSourceStack source = context.getSource();
        source.sendSuccess(() -> Component.literal("/hbm help"), false);
        source.sendSuccess(() -> Component.literal("/hbm status"), false);
        source.sendSuccess(() -> Component.literal("/hbm radiation get|set|add|clear [amount] [pos]"), false);
        source.sendSuccess(() -> Component.literal("/hbm pollution get|add|set <type> [amount] [pos]"), false);
        source.sendSuccess(() -> Component.literal("/hbm tom get|set dust|fire|impact <value>"), false);
        source.sendSuccess(() -> Component.literal("/hbm debug machine|transfer|world|worldgen [pos]"), false);
        source.sendSuccess(() -> Component.literal("/hbm debug transfer rebuild [pos]"), false);
        source.sendSuccess(() -> Component.literal("/hbm debug entity [radius] OR [pos] [radius]"), false);
        return 1;
    }

    private static int runStatus(final CommandContext<CommandSourceStack> context) {
        final CommandSourceStack source = context.getSource();
        final ServerLevel level = source.getLevel();
        final BlockPos pos = sourcePos(context);

        final float radiation = ChunkRadiationManager.getRadiation(level, pos.getX(), pos.getY(), pos.getZ());
        final float soot = PollutionSavedData.getPollution(level, pos.getX(), pos.getY(), pos.getZ(), PollutionType.SOOT);
        final float heavy = PollutionSavedData.getPollution(level, pos.getX(), pos.getY(), pos.getZ(), PollutionType.HEAVY_METAL);
        final float poison = PollutionSavedData.getPollution(level, pos.getX(), pos.getY(), pos.getZ(), PollutionType.POISON);
        final TomImpactSavedData tom = TomImpactSavedData.get(level);

        source.sendSuccess(() -> Component.literal("Status @ " + formatPos(pos) + " in " + level.dimension().location()), false);
        source.sendSuccess(() -> Component.literal("Radiation: " + formatFloat(radiation)), false);
        source.sendSuccess(() -> Component.literal("Pollution soot/heavy/poison: "
            + formatFloat(soot) + " / " + formatFloat(heavy) + " / " + formatFloat(poison)), false);
        source.sendSuccess(() -> Component.literal("Tom dust/fire/impact: "
            + formatFloat(tom.dust()) + " / " + formatFloat(tom.fire()) + " / " + tom.impact()), false);
        return 1;
    }

    private static int radiationGet(final CommandContext<CommandSourceStack> context, final BlockPos pos) {
        final CommandSourceStack source = context.getSource();
        final ServerLevel level = source.getLevel();
        final float radiation = ChunkRadiationManager.getRadiation(level, pos.getX(), pos.getY(), pos.getZ());
        source.sendSuccess(() -> Component.literal("Radiation @ " + formatPos(pos) + " = " + formatFloat(radiation)), false);
        return (int) radiation;
    }

    private static int radiationSet(final CommandContext<CommandSourceStack> context, final BlockPos pos, final float amount) {
        final CommandSourceStack source = context.getSource();
        final ServerLevel level = source.getLevel();
        final float clamped = Math.max(0.0F, amount);
        ChunkRadiationManager.setRadiation(level, pos.getX(), pos.getY(), pos.getZ(), clamped);
        source.sendSuccess(() -> Component.literal("Set radiation @ " + formatPos(pos) + " to " + formatFloat(clamped)), true);
        return (int) clamped;
    }

    private static int radiationAdd(final CommandContext<CommandSourceStack> context, final BlockPos pos, final float delta) {
        final CommandSourceStack source = context.getSource();
        final ServerLevel level = source.getLevel();
        final float current = ChunkRadiationManager.getRadiation(level, pos.getX(), pos.getY(), pos.getZ());
        final float next = Math.max(0.0F, current + delta);
        ChunkRadiationManager.setRadiation(level, pos.getX(), pos.getY(), pos.getZ(), next);
        source.sendSuccess(() -> Component.literal("Changed radiation @ " + formatPos(pos)
            + " from " + formatFloat(current)
            + " to " + formatFloat(next)), true);
        return (int) next;
    }

    private static int pollutionGet(final CommandContext<CommandSourceStack> context, final BlockPos pos) throws CommandSyntaxException {
        final CommandSourceStack source = context.getSource();
        final ServerLevel level = source.getLevel();
        final PollutionType type = pollutionType(context);
        final float value = PollutionSavedData.getPollution(level, pos.getX(), pos.getY(), pos.getZ(), type);
        source.sendSuccess(() -> Component.literal("Pollution " + type.name().toLowerCase(Locale.ROOT)
            + " @ " + formatPos(pos) + " = " + formatFloat(value)), false);
        return (int) value;
    }

    private static int pollutionAdd(final CommandContext<CommandSourceStack> context,
                                    final BlockPos pos,
                                    final float amount) throws CommandSyntaxException {
        final CommandSourceStack source = context.getSource();
        final ServerLevel level = source.getLevel();
        final PollutionType type = pollutionType(context);
        final float before = PollutionSavedData.getPollution(level, pos.getX(), pos.getY(), pos.getZ(), type);
        PollutionSavedData.incrementPollution(level, pos.getX(), pos.getY(), pos.getZ(), type, amount);
        final float after = PollutionSavedData.getPollution(level, pos.getX(), pos.getY(), pos.getZ(), type);
        source.sendSuccess(() -> Component.literal("Changed " + type.name().toLowerCase(Locale.ROOT)
            + " pollution @ " + formatPos(pos)
            + " from " + formatFloat(before)
            + " to " + formatFloat(after)), true);
        return (int) after;
    }

    private static int pollutionSet(final CommandContext<CommandSourceStack> context,
                                    final BlockPos pos,
                                    final float amount) throws CommandSyntaxException {
        final CommandSourceStack source = context.getSource();
        final ServerLevel level = source.getLevel();
        final PollutionType type = pollutionType(context);
        final float target = Math.max(0.0F, amount);
        final float before = PollutionSavedData.getPollution(level, pos.getX(), pos.getY(), pos.getZ(), type);
        PollutionSavedData.incrementPollution(level, pos.getX(), pos.getY(), pos.getZ(), type, target - before);
        source.sendSuccess(() -> Component.literal("Set " + type.name().toLowerCase(Locale.ROOT)
            + " pollution @ " + formatPos(pos)
            + " to " + formatFloat(target)), true);
        return (int) target;
    }

    private static int tomGet(final CommandContext<CommandSourceStack> context) {
        final CommandSourceStack source = context.getSource();
        final TomImpactSavedData data = TomImpactSavedData.get(source.getLevel());
        source.sendSuccess(() -> Component.literal("Tom state dust/fire/impact: "
            + formatFloat(data.dust()) + " / " + formatFloat(data.fire()) + " / " + data.impact()), false);
        return 1;
    }

    private static int tomSetDust(final CommandContext<CommandSourceStack> context, final float value) {
        final CommandSourceStack source = context.getSource();
        final TomImpactSavedData data = TomImpactSavedData.get(source.getLevel());
        data.setDust(value);
        source.sendSuccess(() -> Component.literal("Set Tom dust to " + formatFloat(value)), true);
        return 1;
    }

    private static int tomSetFire(final CommandContext<CommandSourceStack> context, final float value) {
        final CommandSourceStack source = context.getSource();
        final TomImpactSavedData data = TomImpactSavedData.get(source.getLevel());
        data.setFire(value);
        source.sendSuccess(() -> Component.literal("Set Tom fire to " + formatFloat(value)), true);
        return 1;
    }

    private static int tomSetImpact(final CommandContext<CommandSourceStack> context, final boolean value) {
        final CommandSourceStack source = context.getSource();
        final TomImpactSavedData data = TomImpactSavedData.get(source.getLevel());
        data.setImpact(value);
        source.sendSuccess(() -> Component.literal("Set Tom impact to " + value), true);
        return 1;
    }

    private static int debugMachine(final CommandContext<CommandSourceStack> context, final BlockPos pos) {
        final CommandSourceStack source = context.getSource();
        final ServerLevel level = source.getLevel();
        final BlockState state = level.getBlockState(pos);
        final String blockId = String.valueOf(BuiltInRegistries.BLOCK.getKey(state.getBlock()));

        source.sendSuccess(() -> Component.literal("Machine debug @ " + formatPos(pos) + " in " + level.dimension().location()), false);
        source.sendSuccess(() -> Component.literal("Block: " + blockId), false);

        final BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity == null) {
            source.sendSuccess(() -> Component.literal("BlockEntity: none"), false);
            return 0;
        }

        final String beTypeId = String.valueOf(BuiltInRegistries.BLOCK_ENTITY_TYPE.getKey(blockEntity.getType()));
        source.sendSuccess(() -> Component.literal("BlockEntity: " + blockEntity.getClass().getSimpleName() + " (" + beTypeId + ")"), false);

        if (!(blockEntity instanceof final MachineBlockEntity machine)) {
            source.sendSuccess(() -> Component.literal("Machine: no"), false);
            return 1;
        }

        source.sendSuccess(() -> Component.literal("Machine: yes"), false);
        if (machine.hasEnergyStorage()) {
            source.sendSuccess(() -> Component.literal("Energy: " + machine.getStoredEnergy() + " / " + machine.getMaxStoredEnergy() + " HE"), false);
        } else {
            source.sendSuccess(() -> Component.literal("Energy: none"), false);
        }

        source.sendSuccess(() -> Component.literal("Maintenance: level=" + machine.getMaintenanceLevel()
            + ", blocked=" + machine.isMaintenanceBlocked()
            + ", muffled=" + machine.isMuffled()
            + ", loaded=" + machine.isLoaded()), false);

        final String upgradeInfo = machine.getUpgradeLevels().entrySet().stream()
            .map(entry -> entry.getKey().name().toLowerCase(Locale.ROOT) + "=" + entry.getValue())
            .collect(Collectors.joining(", "));
        source.sendSuccess(() -> Component.literal("Upgrades: " + (upgradeInfo.isBlank() ? "none" : upgradeInfo)), false);

        final int tankCount = machine.getTankCount();
        source.sendSuccess(() -> Component.literal("Tanks: " + tankCount), false);
        for (int i = 0; i < tankCount; i++) {
            final int tankIndex = i;
            final var tank = machine.getFluidTank(tankIndex);
            if (tank == null) {
                source.sendSuccess(() -> Component.literal("  [" + tankIndex + "] missing"), false);
                continue;
            }

            final int amount = tank.getFluidAmount();
            final int capacity = tank.getCapacity();
            final String fluidName = tank.isEmpty() ? "empty" : tank.getFluid().getDisplayName().getString();
            source.sendSuccess(() -> Component.literal("  [" + tankIndex + "] " + amount + " / " + capacity + " mB " + fluidName), false);
        }

        final var inventory = machine.getInternalItemHandler();
        final int slotCount = inventory.getSlots();
        int occupiedSlots = 0;
        int totalItems = 0;
        final Map<String, Integer> itemsById = new java.util.HashMap<>();

        for (int slot = 0; slot < slotCount; slot++) {
            final ItemStack stack = inventory.getStackInSlot(slot);
            if (stack.isEmpty()) {
                continue;
            }

            occupiedSlots++;
            totalItems += stack.getCount();
            final String itemId = String.valueOf(BuiltInRegistries.ITEM.getKey(stack.getItem()));
            itemsById.merge(itemId, stack.getCount(), Integer::sum);
        }

        final int occupiedSlotsFinal = occupiedSlots;
        final int totalItemsFinal = totalItems;
        source.sendSuccess(() -> Component.literal("Inventory slots occupied/total: " + occupiedSlotsFinal + " / " + slotCount), false);
        source.sendSuccess(() -> Component.literal("Inventory total items: " + totalItemsFinal), false);

        final String topItems = itemsById.entrySet().stream()
            .sorted((left, right) -> Integer.compare(right.getValue(), left.getValue()))
            .limit(8)
            .map(entry -> entry.getKey() + "=" + entry.getValue())
            .collect(Collectors.joining(", "));

        if (!topItems.isBlank()) {
            source.sendSuccess(() -> Component.literal("Inventory top items: " + topItems), false);
        }

        return 1;
    }

    private static int debugTransfer(final CommandContext<CommandSourceStack> context, final BlockPos pos) {
        final CommandSourceStack source = context.getSource();
        final ServerLevel level = source.getLevel();
        final BlockEntity blockEntity = level.getBlockEntity(pos);

        source.sendSuccess(() -> Component.literal("Transfer debug @ " + formatPos(pos) + " in " + level.dimension().location()), false);

        if (!(blockEntity instanceof final TransferNodeProvider node)) {
            source.sendSuccess(() -> Component.literal("Transfer node: no"), false);
            return 0;
        }

        final TransferNetworkKind kind = node.getTransferNetworkKind();
        final TransferNetwork rebuilt = TransferGraphManager.rebuildIfDirty(level, pos, kind);
        final TransferNetwork network = rebuilt.size() > 0 ? rebuilt : TransferGraphManager.getNetwork(level, pos, kind);

        source.sendSuccess(() -> Component.literal("Transfer node: yes"), false);
        source.sendSuccess(() -> Component.literal("Kind: " + kind.name().toLowerCase(Locale.ROOT)), false);
        source.sendSuccess(() -> Component.literal("Connections: "
            + node.getConnectionDirections().stream().map(d -> d.getName().toLowerCase(Locale.ROOT)).collect(Collectors.joining(", "))), false);
        source.sendSuccess(() -> Component.literal("Network size: " + network.size()), false);
        source.sendSuccess(() -> Component.literal("Transfer this tick: " + TransferGraphManager.getTransferThisTick(level, pos, kind)), false);

        final String sampleNodes = network.nodes().stream()
            .limit(8)
            .map(HbmRootCommand::formatPos)
            .collect(Collectors.joining(", "));
        if (!sampleNodes.isBlank()) {
            source.sendSuccess(() -> Component.literal("Sample nodes: " + sampleNodes), false);
        }

        return network.size();
    }

    private static int debugTransferRebuild(final CommandContext<CommandSourceStack> context, final BlockPos pos) {
        final CommandSourceStack source = context.getSource();
        final ServerLevel level = source.getLevel();
        final BlockEntity blockEntity = level.getBlockEntity(pos);

        source.sendSuccess(() -> Component.literal("Transfer rebuild @ " + formatPos(pos) + " in " + level.dimension().location()), false);

        if (!(blockEntity instanceof final TransferNodeProvider node)) {
            source.sendSuccess(() -> Component.literal("Transfer node: no"), false);
            return 0;
        }

        final TransferNetworkKind kind = node.getTransferNetworkKind();
        final TransferNetwork previous = TransferGraphManager.getNetwork(level, pos, kind);
        final int previousSize = previous.size();
        final TransferNetwork rebuilt = TransferGraphManager.rebuildNetwork(level, pos, kind);

        source.sendSuccess(() -> Component.literal("Kind: " + kind.name().toLowerCase(Locale.ROOT)), false);
        source.sendSuccess(() -> Component.literal("Network size (before -> after): " + previousSize + " -> " + rebuilt.size()), true);
        return rebuilt.size();
    }

    private static int debugWorld(final CommandContext<CommandSourceStack> context, final BlockPos pos) {
        final CommandSourceStack source = context.getSource();
        final ServerLevel level = source.getLevel();
        final ChunkPos chunkPos = new ChunkPos(pos);

        final String biomeId = level.getBiome(pos)
            .unwrapKey()
            .map(key -> key.location().toString())
            .orElse("unknown");
        final int surfaceY = level.getHeight(Heightmap.Types.WORLD_SURFACE, pos.getX(), pos.getZ());
        final int oceanFloorY = level.getHeight(Heightmap.Types.OCEAN_FLOOR, pos.getX(), pos.getZ());
        final int skylight = level.getBrightness(LightLayer.SKY, pos);
        final int blockLight = level.getBrightness(LightLayer.BLOCK, pos);
        final boolean loaded = level.isLoaded(pos);

        source.sendSuccess(() -> Component.literal("World debug @ " + formatPos(pos) + " in " + level.dimension().location()), false);
        source.sendSuccess(() -> Component.literal("Chunk: " + chunkPos.x + ", " + chunkPos.z + " (loaded=" + loaded + ")"), false);
        source.sendSuccess(() -> Component.literal("Biome: " + biomeId), false);
        source.sendSuccess(() -> Component.literal("Weather: rain=" + level.isRaining() + ", thunder=" + level.isThundering()), false);
        source.sendSuccess(() -> Component.literal("Height surface/oceanFloor: " + surfaceY + " / " + oceanFloorY), false);
        source.sendSuccess(() -> Component.literal("Light sky/block: " + skylight + " / " + blockLight), false);
        return 1;
    }

    private static int debugWorldgen(final CommandContext<CommandSourceStack> context, final BlockPos pos) {
        final CommandSourceStack source = context.getSource();
        final ServerLevel level = source.getLevel();
        final ChunkPos chunkPos = new ChunkPos(pos);
        final ChunkGenerator generator = level.getChunkSource().getGenerator();

        source.sendSuccess(() -> Component.literal("Worldgen debug @ " + formatPos(pos) + " in " + level.dimension().location()), false);
        source.sendSuccess(() -> Component.literal("Chunk: " + chunkPos.x + ", " + chunkPos.z), false);
        source.sendSuccess(() -> Component.literal("World seed: " + level.getSeed()), false);
        source.sendSuccess(() -> Component.literal("Generator: " + generator.getClass().getSimpleName()), false);
        source.sendSuccess(() -> Component.literal("Biome source: " + generator.getBiomeSource().getClass().getSimpleName()), false);
        source.sendSuccess(() -> Component.literal("Sea level: " + generator.getSeaLevel()), false);
        source.sendSuccess(() -> Component.literal("Generation Y range: min=" + generator.getMinY() + ", depth=" + generator.getGenDepth()), false);
        return 1;
    }

    private static int debugEntities(final CommandContext<CommandSourceStack> context,
                                     final BlockPos pos,
                                     final float radius) {
        final CommandSourceStack source = context.getSource();
        final ServerLevel level = source.getLevel();
        final float clampedRadius = Math.max(1.0F, Math.min(256.0F, radius));
        final AABB bounds = new AABB(pos).inflate(clampedRadius);
        final var entities = level.getEntities((Entity) null, bounds, entity -> true);

        final long living = entities.stream().filter(LivingEntity.class::isInstance).count();
        final long hostile = entities.stream().filter(Enemy.class::isInstance).count();
        final long items = entities.stream().filter(ItemEntity.class::isInstance).count();
        final long players = entities.stream().filter(entity -> entity instanceof net.minecraft.server.level.ServerPlayer).count();

        source.sendSuccess(() -> Component.literal("Entity debug @ " + formatPos(pos)
            + " radius=" + String.format(Locale.ROOT, "%.1f", clampedRadius)), false);
        source.sendSuccess(() -> Component.literal("Counts total/living/hostile/items/players: "
            + entities.size() + " / " + living + " / " + hostile + " / " + items + " / " + players), false);

        final Map<String, Long> byType = entities.stream()
            .collect(Collectors.groupingBy(
                entity -> String.valueOf(BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType())),
                Collectors.counting()));

        final String topTypes = byType.entrySet().stream()
            .sorted((left, right) -> Long.compare(right.getValue(), left.getValue()))
            .limit(8)
            .map(entry -> entry.getKey() + "=" + entry.getValue())
            .collect(Collectors.joining(", "));

        if (!topTypes.isBlank()) {
            source.sendSuccess(() -> Component.literal("Top entity types: " + topTypes), false);
        }

        return entities.size();
    }

    private static PollutionType pollutionType(final CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        final String raw = StringArgumentType.getString(context, "type");
        final String normalized = raw.toUpperCase(Locale.ROOT);
        try {
            return PollutionType.valueOf(normalized);
        } catch (final IllegalArgumentException ignored) {
            throw INVALID_POLLUTION_TYPE.create(raw);
        }
    }

    private static BlockPos sourcePos(final CommandContext<CommandSourceStack> context) {
        return BlockPos.containing(context.getSource().getPosition());
    }

    private static String formatPos(final BlockPos pos) {
        return pos.getX() + " " + pos.getY() + " " + pos.getZ();
    }

    private static String formatFloat(final float value) {
        return String.format(Locale.ROOT, "%.3f", value);
    }
}
