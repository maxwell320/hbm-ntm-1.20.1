package com.hbm.ntm.common.command;

import com.hbm.ntm.HbmNtmMod;
import com.hbm.ntm.client.debug.HbmDebugBundle;
import com.hbm.ntm.client.debug.HbmDebugEventHandler;
import com.hbm.ntm.client.debug.HbmDebugFluidInspector;
import com.hbm.ntm.client.debug.HbmDebugLogTail;
import com.hbm.ntm.client.debug.HbmDebugMachineInspector;
import com.hbm.ntm.client.debug.HbmDebugModelInspector;
import com.hbm.ntm.client.debug.HbmDebugOverlay;
import com.hbm.ntm.client.debug.HbmDebugRecipeInspector;
import com.hbm.ntm.client.debug.HbmDebugScreenInspector;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import java.io.IOException;
import java.nio.file.Path;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterClientCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = HbmNtmMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
@SuppressWarnings("null")
public final class HbmDebugClientCommand {

    private static final SuggestionProvider<CommandSourceStack> OVERLAY_MODE_SUGGESTIONS =
        (ctx, builder) -> SharedSuggestionProvider.suggest(
            new String[]{"hitbox", "slots", "grid", "coords", "labels", "all", "off"}, builder);

    private HbmDebugClientCommand() {
    }

    @SubscribeEvent
    public static void onRegister(final RegisterClientCommandsEvent event) {
        register(event.getDispatcher());
    }

    public static void register(final CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("hbm")
            .then(uiCommand())
            .then(modelCommand())
            .then(machineCommand())
            .then(fluidCommand())
            .then(recipeCommand())
            .then(logCommand())
            .then(Commands.literal("bundle").executes(HbmDebugClientCommand::runBundle))
            .then(Commands.literal("debughelp").executes(HbmDebugClientCommand::runHelp)));
    }

    private static LiteralArgumentBuilder<CommandSourceStack> uiCommand() {
        return Commands.literal("ui")
            .then(Commands.literal("dump").executes(ctx -> runWrite(ctx, HbmDebugScreenInspector::dumpCurrentScreen, "ui dump")))
            .then(Commands.literal("probe").executes(HbmDebugClientCommand::runProbe))
            .then(Commands.literal("shot").executes(ctx -> runWrite(ctx, HbmDebugBundle::screenshotOnly, "ui screenshot")))
            .then(Commands.literal("off").executes(ctx -> {
                HbmDebugOverlay.disableAll();
                reply(ctx, "All overlays disabled.");
                return 1;
            }))
            .then(Commands.literal("overlay")
                .then(Commands.argument("mode", StringArgumentType.word())
                    .suggests(OVERLAY_MODE_SUGGESTIONS)
                    .executes(ctx -> {
                        final String mode = StringArgumentType.getString(ctx, "mode").toLowerCase(java.util.Locale.ROOT);
                        return applyOverlay(ctx, mode);
                    })));
    }

    private static int applyOverlay(final CommandContext<CommandSourceStack> ctx, final String mode) {
        switch (mode) {
            case "off" -> {
                HbmDebugOverlay.disableAll();
                reply(ctx, "Overlays OFF.");
            }
            case "all" -> {
                HbmDebugOverlay.enableAll();
                reply(ctx, "All overlays ON: hitbox, slots, grid, coords, labels.");
            }
            default -> {
                final HbmDebugOverlay.OverlayMode parsed = HbmDebugOverlay.OverlayMode.fromKey(mode);
                if (parsed == null) {
                    reply(ctx, ChatColors.RED, "Unknown overlay mode '" + mode + "'. Try: hitbox, slots, grid, coords, labels, all, off.");
                    return 0;
                }
                if (HbmDebugOverlay.isEnabled(parsed)) {
                    HbmDebugOverlay.disable(parsed);
                    reply(ctx, "Overlay " + parsed.key() + " OFF.");
                } else {
                    HbmDebugOverlay.enable(parsed);
                    reply(ctx, "Overlay " + parsed.key() + " ON.");
                }
            }
        }
        return 1;
    }

    private static int runProbe(final CommandContext<CommandSourceStack> ctx) {
        if (Minecraft.getInstance().screen instanceof ChatScreen) {
            reply(ctx, ChatColors.RED, "Probe blocked: ChatScreen is open. Close chat, hover target UI, then run /hbm ui probe (or press F6).");
            return 0;
        }
        HbmDebugOverlay.requestProbe();
        reply(ctx, "Probe requested — move/hover and it will capture on next frame.");
        HbmDebugScreenInspector.captureProbe(HbmDebugEventHandler.lastMouseX(), HbmDebugEventHandler.lastMouseY());
        final HbmDebugScreenInspector.ProbeResult p = HbmDebugScreenInspector.lastProbe();
        final String text = HbmDebugScreenInspector.probeToString(p);
        for (final String line : text.split("\\n")) {
            reply(ctx, line);
        }
        try {
            final Path out = com.hbm.ntm.client.debug.HbmDebugWriter.write("ui-probe", text);
            reply(ctx, ChatColors.GRAY, "Saved -> " + out.toAbsolutePath());
        } catch (final IOException ex) {
            reply(ctx, ChatColors.RED, "probe save failed: " + ex);
        }
        return 1;
    }

    private static LiteralArgumentBuilder<CommandSourceStack> modelCommand() {
        return Commands.literal("model")
            .then(Commands.literal("look").executes(ctx -> runWrite(ctx, HbmDebugModelInspector::dumpLookedAtBlock, "model look")))
            .then(Commands.literal("scan").executes(ctx -> runWrite(ctx, HbmDebugModelInspector::scanAllModels, "model scan")))
            .then(Commands.literal("reload").executes(ctx -> {
                HbmDebugModelInspector.reloadModels();
                reply(ctx, "Reloading client resource packs (models will rebuild).");
                return 1;
            }));
    }

    private static LiteralArgumentBuilder<CommandSourceStack> machineCommand() {
        return Commands.literal("machine")
            .then(Commands.literal("dump").executes(ctx -> runWrite(ctx, HbmDebugMachineInspector::dumpLookedAtMachine, "machine dump")));
    }

    private static LiteralArgumentBuilder<CommandSourceStack> fluidCommand() {
        return Commands.literal("fluid")
            .then(Commands.literal("list").executes(ctx -> runWrite(ctx, HbmDebugFluidInspector::listFluids, "fluid list")))
            .then(Commands.literal("dump")
                .then(Commands.argument("id", StringArgumentType.greedyString())
                    .executes(ctx -> runWrite(ctx,
                        () -> HbmDebugFluidInspector.dumpFluid(StringArgumentType.getString(ctx, "id")),
                        "fluid dump"))));
    }

    private static LiteralArgumentBuilder<CommandSourceStack> recipeCommand() {
        return Commands.literal("recipe")
            .then(Commands.literal("summary").executes(ctx -> runWrite(ctx, HbmDebugRecipeInspector::summarize, "recipe summary")))
            .then(Commands.literal("list")
                .then(Commands.argument("type", StringArgumentType.greedyString())
                    .executes(ctx -> runWrite(ctx,
                        () -> HbmDebugRecipeInspector.listRecipeType(StringArgumentType.getString(ctx, "type")),
                        "recipe list"))));
    }

    private static LiteralArgumentBuilder<CommandSourceStack> logCommand() {
        return Commands.literal("log")
            .then(Commands.literal("tail")
                .executes(ctx -> runWrite(ctx, () -> HbmDebugLogTail.tail(500), "log tail"))
                .then(Commands.argument("lines", IntegerArgumentType.integer(1, 5000))
                    .executes(ctx -> runWrite(ctx,
                        () -> HbmDebugLogTail.tail(IntegerArgumentType.getInteger(ctx, "lines")),
                        "log tail"))))
            .then(Commands.literal("save").executes(ctx -> runWrite(ctx, HbmDebugLogTail::saveCopy, "log save")));
    }

    private static int runBundle(final CommandContext<CommandSourceStack> ctx) {
        return runWrite(ctx, HbmDebugBundle::runFull, "bundle (full)");
    }

    private static int runHelp(final CommandContext<CommandSourceStack> ctx) {
        reply(ctx, ChatColors.YELLOW, "/hbm ui dump | probe | shot | off | overlay <hitbox|slots|grid|coords|labels|all|off>");
        reply(ctx, ChatColors.YELLOW, "/hbm model look | scan | reload");
        reply(ctx, ChatColors.YELLOW, "/hbm machine dump");
        reply(ctx, ChatColors.YELLOW, "/hbm fluid list | dump <id>");
        reply(ctx, ChatColors.YELLOW, "/hbm recipe summary | list <type>");
        reply(ctx, ChatColors.YELLOW, "/hbm log tail [lines] | save");
        reply(ctx, ChatColors.YELLOW, "/hbm bundle (F8) | F9 screenshot");
        reply(ctx, ChatColors.GRAY, "Probe note: /hbm ui probe must be run with chat closed; ChatScreen probes are rejected to avoid false captures.");
        reply(ctx, ChatColors.GRAY, "Outputs land under run/hbm-debug/. Also see /hbm radiation|pollution|tom|debug|tools (server-side).");
        return 1;
    }

    private static int runWrite(final CommandContext<CommandSourceStack> ctx,
                                final IoPathSupplier action,
                                final String label) {
        try {
            final Path out = action.get();
            if (out == null) {
                reply(ctx, ChatColors.RED, label + " returned no output path.");
                return 0;
            }
            reply(ctx, ChatColors.GREEN, label + " -> " + out.toAbsolutePath());
            return 1;
        } catch (final IOException ex) {
            reply(ctx, ChatColors.RED, label + " failed: " + ex.getClass().getSimpleName() + ": " + ex.getMessage());
            HbmNtmMod.LOGGER.error("[hbm-debug] {} failed", label, ex);
            return 0;
        } catch (final RuntimeException ex) {
            reply(ctx, ChatColors.RED, label + " failed: " + ex.getClass().getSimpleName() + ": " + ex.getMessage());
            HbmNtmMod.LOGGER.error("[hbm-debug] {} failed", label, ex);
            return 0;
        }
    }

    @FunctionalInterface
    public interface IoPathSupplier {
        Path get() throws IOException;
    }

    private static void reply(final CommandContext<CommandSourceStack> ctx, final String message) {
        reply(ctx, ChatColors.WHITE, message);
    }

    private static void reply(final CommandContext<CommandSourceStack> ctx, final String colorPrefix, final String message) {
        final Component line = Component.literal(colorPrefix + message);
        ctx.getSource().sendSuccess(() -> line, false);
    }

    private static final class ChatColors {
        static final String WHITE = "";
        static final String GRAY = "\u00A77";
        static final String GREEN = "\u00A7a";
        static final String YELLOW = "\u00A7e";
        static final String RED = "\u00A7c";

        private ChatColors() {
        }
    }
}
