package com.hbm.ntm.common.blastfurnace;

import com.hbm.ntm.HbmNtmMod;
import com.hbm.ntm.common.item.CanisterItem;
import com.hbm.ntm.common.item.CokeItemType;
import com.hbm.ntm.common.material.HbmMaterialShape;
import com.hbm.ntm.common.material.HbmMaterials;
import com.hbm.ntm.common.registration.HbmItems;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Objects;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;

@SuppressWarnings("null")
public final class HbmBlastFurnaceRecipes {
    private static final List<BlastRecipe> RECIPES = new ArrayList<>();
    private static boolean initialized;

    private HbmBlastFurnaceRecipes() {
    }

    public static synchronized void ensureInitialized() {
        if (initialized) {
            return;
        }
        initialized = true;
        registerDefaults();
    }

    public static Optional<BlastRecipe> findRecipe(final ItemStack first, final ItemStack second) {
        if (first.isEmpty() || second.isEmpty()) {
            return Optional.empty();
        }
        ensureInitialized();
        return RECIPES.stream().filter(recipe -> recipe.matches(first, second)).findFirst();
    }

    private static void registerDefaults() {
        final Item steelIngot = HbmItems.getMaterialPart(HbmMaterials.STEEL, HbmMaterialShape.INGOT).get();
        final Item redCopperIngot = HbmItems.getMaterialPart(HbmMaterials.RED_COPPER, HbmMaterialShape.INGOT).get();
        final Item technetiumNugget = HbmItems.getMaterialPart(HbmMaterials.TECHNETIUM, HbmMaterialShape.NUGGET).get();
        final Item tcalloyIngot = HbmItems.getMaterialPart(HbmMaterials.TCALLOY, HbmMaterialShape.INGOT).get();
        final Item tungstenIngot = HbmItems.getMaterialPart(HbmMaterials.TUNGSTEN, HbmMaterialShape.INGOT).get();
        final Item cobaltNugget = HbmItems.getMaterialPart(HbmMaterials.COBALT, HbmMaterialShape.NUGGET).get();
        final Item magnetizedTungstenIngot = HbmItems.getMaterialPart(HbmMaterials.MAGNETIZED_TUNGSTEN, HbmMaterialShape.INGOT).get();
        final Item saturniteIngot = HbmItems.getMaterialPart(HbmMaterials.SATURNITE, HbmMaterialShape.INGOT).get();
        final Item meteoriteIngot = HbmItems.getMaterialPart(HbmMaterials.METEORITE, HbmMaterialShape.INGOT).get();
        final Item starmetalIngot = HbmItems.getMaterialPart(HbmMaterials.STARMETAL, HbmMaterialShape.INGOT).get();

        final Matcher cokeMatcher = any(
            item(HbmItems.getCoke(CokeItemType.COAL).get()),
            item(HbmItems.getCoke(CokeItemType.LIGNITE).get()),
            item(HbmItems.getCoke(CokeItemType.PETROLEUM).get()));
        final Matcher carbonFuel = any(tag(ItemTags.COALS), cokeMatcher);

        register(item(Items.IRON_INGOT), carbonFuel, new ItemStack(steelIngot));
        register(item(Items.IRON_BLOCK), carbonFuel, new ItemStack(steelIngot, 9));
        register(item(Items.COPPER_INGOT), item(Items.REDSTONE), new ItemStack(redCopperIngot, 2));
        register(item(steelIngot), item(technetiumNugget), new ItemStack(tcalloyIngot, 2));
        register(item(tungstenIngot), item(cobaltNugget), new ItemStack(magnetizedTungstenIngot));
        register(item(saturniteIngot), item(meteoriteIngot), new ItemStack(starmetalIngot, 2));
        register(item(HbmItems.METEORITE_SWORD_HARDENED.get()), carbonFuel, new ItemStack(HbmItems.METEORITE_SWORD_ALLOYED.get()));
        register(item(HbmItems.CANISTER_EMPTY.get()), carbonFuel, CanisterItem.withFluid(HbmItems.CANISTER_FULL.get(), ResourceLocation.fromNamespaceAndPath(HbmNtmMod.MOD_ID, "oil")));
        register(canisterFluid(ResourceLocation.fromNamespaceAndPath(HbmNtmMod.MOD_ID, "gasoline")), item(Items.SLIME_BALL), new ItemStack(HbmItems.CANISTER_NAPALM.get()));
    }

    private static void register(final Matcher first, final Matcher second, final ItemStack output) {
        if (output.isEmpty()) {
            return;
        }
        RECIPES.add(new BlastRecipe(first, second, output.copy()));
    }

    private static Matcher item(final ItemLike item) {
        return stack -> stack.is(item.asItem());
    }

    private static Matcher canisterFluid(final ResourceLocation fluidId) {
        return stack -> {
            if (!stack.is(HbmItems.CANISTER_FULL.get())) {
                return false;
            }
            return Objects.equals(CanisterItem.getFluidId(stack), fluidId);
        };
    }

    private static Matcher tag(final net.minecraft.tags.TagKey<Item> tag) {
        return stack -> stack.is(tag);
    }

    private static Matcher any(final Matcher... matchers) {
        return stack -> {
            for (final Matcher matcher : matchers) {
                if (matcher.test(stack)) {
                    return true;
                }
            }
            return false;
        };
    }

    @FunctionalInterface
    public interface Matcher {
        boolean test(ItemStack stack);
    }

    public record BlastRecipe(Matcher first, Matcher second, ItemStack output) {
        public boolean matches(final ItemStack left, final ItemStack right) {
            return (this.first.test(left) && this.second.test(right))
                || (this.first.test(right) && this.second.test(left));
        }

        public ItemStack result() {
            return this.output.copy();
        }
    }
}
