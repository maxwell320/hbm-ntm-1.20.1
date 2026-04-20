package com.hbm.ntm.client.debug;

import com.hbm.ntm.HbmNtmMod;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

@SuppressWarnings({"null", "deprecation"})
public final class HbmDebugModelInspector {

    private HbmDebugModelInspector() {
    }

    public static Path dumpLookedAtBlock() throws IOException {
        final Minecraft mc = Minecraft.getInstance();
        final LocalPlayer player = mc.player;
        final ClientLevel level = mc.level;
        if (player == null || level == null) {
            return HbmDebugWriter.write("model-look-none", "No player or level available.\n");
        }
        final HitResult hit = mc.hitResult;
        if (!(hit instanceof BlockHitResult bhr) || bhr.getType() != HitResult.Type.BLOCK) {
            return HbmDebugWriter.write("model-look-none", "Player is not looking at a block.\n");
        }
        final BlockPos pos = bhr.getBlockPos();
        final BlockState state = level.getBlockState(pos);
        return HbmDebugWriter.write("model-look", describeBlockModel(pos, state));
    }

    public static Path scanAllModels() throws IOException {
        final Minecraft mc = Minecraft.getInstance();
        final StringBuilder sb = new StringBuilder(16_384);
        sb.append("=== HBM model scan ===\n");
        sb.append("timestamp: ").append(HbmDebugWriter.stamp()).append('\n');
        sb.append("modId: ").append(HbmNtmMod.MOD_ID).append('\n');

        int total = 0;
        int suspicious = 0;
        final Map<String, Integer> bakedModelClasses = new TreeMap<>();
        final List<String> zeroQuadLines = new ArrayList<>();

        for (final ResourceLocation id : ForgeRegistries.BLOCKS.getKeys()) {
            if (!HbmNtmMod.MOD_ID.equals(id.getNamespace())) {
                continue;
            }
            final Block block = ForgeRegistries.BLOCKS.getValue(id);
            if (block == null) {
                continue;
            }
            total++;
            final BlockState state = block.defaultBlockState();
            final BakedModel model = mc.getBlockRenderer().getBlockModel(state);
            bakedModelClasses.merge(model.getClass().getSimpleName(), 1, Integer::sum);
            final int[] quadCounts = new int[7];
            final RandomSource rand = RandomSource.create(42L);
            for (int i = 0; i < 6; i++) {
                quadCounts[i] = model.getQuads(state, Direction.values()[i], rand).size();
            }
            quadCounts[6] = model.getQuads(state, null, rand).size();
            final int totalQuads = quadCounts[0] + quadCounts[1] + quadCounts[2] + quadCounts[3]
                + quadCounts[4] + quadCounts[5] + quadCounts[6];
            if (totalQuads == 0 || "SimpleBakedModel".equals(model.getClass().getSimpleName())
                && totalQuads < 6) {
                suspicious++;
                zeroQuadLines.add(String.format("  %s -> %s quads=%d (d/u/n/s/w/e/any: %d/%d/%d/%d/%d/%d/%d)",
                    id, model.getClass().getSimpleName(), totalQuads,
                    quadCounts[0], quadCounts[1], quadCounts[2], quadCounts[3],
                    quadCounts[4], quadCounts[5], quadCounts[6]));
            }
        }

        sb.append("total hbmntm blocks scanned: ").append(total).append('\n');
        sb.append("suspicious (0 quads or missing faces): ").append(suspicious).append('\n');

        sb.append("\n-- baked model class distribution --\n");
        for (final Map.Entry<String, Integer> e : bakedModelClasses.entrySet()) {
            sb.append("  ").append(e.getKey()).append(" x").append(e.getValue()).append('\n');
        }

        sb.append("\n-- suspicious blocks --\n");
        if (zeroQuadLines.isEmpty()) {
            sb.append("  (none)\n");
        } else {
            for (final String line : zeroQuadLines) {
                sb.append(line).append('\n');
            }
        }

        return HbmDebugWriter.write("model-scan", sb.toString());
    }

    public static void reloadModels() {
        final Minecraft mc = Minecraft.getInstance();
        mc.reloadResourcePacks();
        HbmNtmMod.LOGGER.info("[hbm-debug] reloadResourcePacks() invoked");
    }

    public static String describeBlockModel(final BlockPos pos, final BlockState state) {
        final Minecraft mc = Minecraft.getInstance();
        final BlockRenderDispatcher dispatcher = mc.getBlockRenderer();
        final BakedModel model = dispatcher.getBlockModel(state);
        final StringBuilder sb = new StringBuilder(2048);
        sb.append("=== HBM model look ===\n");
        sb.append("timestamp: ").append(HbmDebugWriter.stamp()).append('\n');
        sb.append("pos: ").append(pos.getX()).append(',').append(pos.getY()).append(',').append(pos.getZ()).append('\n');
        sb.append("block: ").append(ForgeRegistries.BLOCKS.getKey(state.getBlock())).append('\n');
        sb.append("blockClass: ").append(state.getBlock().getClass().getName()).append('\n');
        sb.append("blockState: ").append(state).append('\n');
        sb.append("shape: ").append(state.getShape(mc.level, pos).bounds()).append('\n');
        sb.append("bakedModelClass: ").append(model.getClass().getName()).append('\n');
        sb.append("useAmbientOcclusion: ").append(model.useAmbientOcclusion()).append('\n');
        sb.append("isGui3d: ").append(model.isGui3d()).append('\n');
        sb.append("usesBlockLight: ").append(model.usesBlockLight()).append('\n');
        sb.append("isCustomRenderer: ").append(model.isCustomRenderer()).append('\n');
        sb.append("particleIcon: ")
            .append(model.getParticleIcon().contents().name()).append('\n');

        final RandomSource rand = RandomSource.create(42L);
        sb.append("\n-- quads per direction --\n");
        int total = 0;
        for (final Direction dir : Direction.values()) {
            final List<BakedQuad> quads = model.getQuads(state, dir, rand);
            sb.append("  ").append(dir.getName()).append(": ").append(quads.size()).append('\n');
            total += quads.size();
            appendQuadSprites(sb, quads, "    ");
        }
        final List<BakedQuad> generic = model.getQuads(state, null, rand);
        sb.append("  <null>: ").append(generic.size()).append('\n');
        appendQuadSprites(sb, generic, "    ");
        total += generic.size();
        sb.append("total quads: ").append(total).append('\n');

        if (total == 0) {
            sb.append("\n(!) zero-quad model — this block will render as a void.\n");
        }

        sb.append("\n-- registered render type --\n");
        try {
            sb.append("renderShape: ").append(state.getRenderShape()).append('\n');
            sb.append("lightEmission: ").append(state.getLightEmission(mc.level, pos)).append('\n');
        } catch (final Throwable t) {
            sb.append("renderShape lookup error: ").append(t.getClass().getSimpleName()).append(": ").append(t.getMessage()).append('\n');
        }

        return sb.toString();
    }

    private static void appendQuadSprites(final StringBuilder sb, final List<BakedQuad> quads, final String indent) {
        for (int i = 0; i < quads.size() && i < 8; i++) {
            final BakedQuad q = quads.get(i);
            sb.append(indent).append("quad#").append(i)
              .append(" direction=").append(q.getDirection() == null ? "null" : q.getDirection().getName())
              .append(" tintIndex=").append(q.getTintIndex())
              .append(" sprite=").append(q.getSprite() == null ? "null" : q.getSprite().contents().name())
              .append(" shade=").append(q.isShade())
              .append('\n');
        }
        if (quads.size() > 8) {
            sb.append(indent).append("... (+").append(quads.size() - 8).append(" more quads)\n");
        }
    }
}
