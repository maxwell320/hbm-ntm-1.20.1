package com.hbm.ntm.client.debug;

import com.hbm.ntm.HbmNtmMod;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.registries.ForgeRegistries;

@SuppressWarnings("null")
public final class HbmDebugRecipeInspector {

    private HbmDebugRecipeInspector() {
    }

    public static Path summarize() throws IOException {
        final Minecraft mc = Minecraft.getInstance();
        final StringBuilder sb = new StringBuilder(4096);
        sb.append("=== HBM recipe summary ===\n");
        sb.append("timestamp: ").append(HbmDebugWriter.stamp()).append('\n');

        if (mc.level == null) {
            sb.append("(no client level — open a world first)\n");
            return HbmDebugWriter.write("recipe-summary", sb.toString());
        }

        final RecipeManager recipes = mc.level.getRecipeManager();
        final Map<ResourceLocation, Integer> counts = new TreeMap<>();
        for (final ResourceLocation typeId : ForgeRegistries.RECIPE_TYPES.getKeys()) {
            final RecipeType<?> type = ForgeRegistries.RECIPE_TYPES.getValue(typeId);
            if (type == null) {
                continue;
            }
            @SuppressWarnings("unchecked")
            final RecipeType<Recipe<net.minecraft.world.Container>> casted =
                (RecipeType<Recipe<net.minecraft.world.Container>>) (RecipeType<?>) type;
            counts.put(typeId, recipes.getAllRecipesFor(casted).size());
        }

        int hbmTotal = 0;
        sb.append("\n-- all recipe types --\n");
        for (final Map.Entry<ResourceLocation, Integer> e : counts.entrySet()) {
            sb.append("  ").append(e.getKey()).append(" = ").append(e.getValue()).append('\n');
            if (HbmNtmMod.MOD_ID.equals(e.getKey().getNamespace())) {
                hbmTotal += e.getValue();
            }
        }
        sb.append("\nhbmntm recipe total: ").append(hbmTotal).append('\n');

        return HbmDebugWriter.write("recipe-summary", sb.toString());
    }

    public static Path listRecipeType(final String typeIdString) throws IOException {
        final Minecraft mc = Minecraft.getInstance();
        final StringBuilder sb = new StringBuilder(4096);
        sb.append("=== HBM recipe list ===\n");
        sb.append("timestamp: ").append(HbmDebugWriter.stamp()).append('\n');
        sb.append("type: ").append(typeIdString).append('\n');

        if (mc.level == null) {
            sb.append("(no client level — open a world first)\n");
            return HbmDebugWriter.write("recipe-list", sb.toString());
        }

        final ResourceLocation id = ResourceLocation.tryParse(typeIdString);
        if (id == null) {
            sb.append("ERROR: cannot parse resource location\n");
            return HbmDebugWriter.write("recipe-list", sb.toString());
        }
        final RecipeType<?> type = ForgeRegistries.RECIPE_TYPES.getValue(id);
        if (type == null) {
            sb.append("ERROR: recipe type not registered\n");
            return HbmDebugWriter.write("recipe-list", sb.toString());
        }
        @SuppressWarnings("unchecked")
        final List<Recipe<net.minecraft.world.Container>> list =
            (List<Recipe<net.minecraft.world.Container>>) (List<?>) mc.level.getRecipeManager()
                .getAllRecipesFor((RecipeType<Recipe<net.minecraft.world.Container>>) (RecipeType<?>) type);
        sb.append("count: ").append(list.size()).append('\n');
        sb.append('\n');
        int i = 0;
        for (final Recipe<?> recipe : list) {
            sb.append('[').append(i++).append("] ").append(recipe.getId())
              .append(" class=").append(recipe.getClass().getSimpleName())
              .append('\n');
            if (i >= 64) {
                sb.append("... (+").append(list.size() - 64).append(" more, truncated)\n");
                break;
            }
        }
        return HbmDebugWriter.write("recipe-list", sb.toString());
    }
}
