package com.hbm.ntm.common.anvil;

import com.hbm.ntm.common.block.NtmAnvilBlock;
import com.hbm.ntm.common.registration.HbmMenuTypes;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("null")
public class NtmAnvilMenu extends AbstractContainerMenu {
    private static final int INPUT_SLOT_LEFT = 0;
    private static final int INPUT_SLOT_RIGHT = 1;
    private static final int RESULT_SLOT = 2;
    private static final int PLAYER_INV_START = 3;
    private static final int PLAYER_INV_END = 39;
    private static final int CRAFT_ALL_OFFSET = 1000;

    private final ContainerLevelAccess access;
    private final BlockPos pos;
    private final int tier;
    private final Inventory playerInventory;
    private final SimpleContainer input = new SimpleContainer(2) {
        @Override
        public void setChanged() {
            super.setChanged();
            NtmAnvilMenu.this.slotsChanged(this);
        }
    };
    private final ResultContainer result = new ResultContainer();
    private final List<HbmAnvilRecipes.ConstructionRecipe> constructionRecipes;
    private HbmAnvilRecipes.SmithingRecipe selectedSmithingRecipe;

    public NtmAnvilMenu(final int containerId, final Inventory inventory, final FriendlyByteBuf buffer) {
        this(containerId, inventory, readMenuPos(buffer));
    }

    private NtmAnvilMenu(final int containerId, final Inventory inventory, final BlockPos pos) {
        this(containerId, inventory, ContainerLevelAccess.create(inventory.player.level(), pos), pos, resolveTier(inventory.player.level(), pos));
    }

    public NtmAnvilMenu(final int containerId, final Inventory inventory, final ContainerLevelAccess access, final BlockPos pos, final int tier) {
        super(HbmMenuTypes.NTM_ANVIL.get(), containerId);
        this.access = access;
        this.pos = pos.immutable();
        this.tier = tier;
        this.playerInventory = inventory;
        this.constructionRecipes = HbmAnvilRecipes.constructionRecipesForTier(tier);

        addSlot(new Slot(this.input, INPUT_SLOT_LEFT, 17, 27));
        addSlot(new Slot(this.input, INPUT_SLOT_RIGHT, 53, 27));
        addSlot(new Slot(this.result, RESULT_SLOT, 89, 27) {
            @Override
            public boolean mayPlace(final @NotNull ItemStack stack) {
                return false;
            }

            @Override
            public void onTake(final @NotNull Player player, final @NotNull ItemStack stack) {
                NtmAnvilMenu.this.consumeSmithingInputs();
                super.onTake(player, stack);
            }
        });

        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 9; column++) {
                addSlot(new Slot(inventory, column + row * 9 + 9, 8 + column * 18, 140 + row * 18));
            }
        }

        for (int hotbar = 0; hotbar < 9; hotbar++) {
            addSlot(new Slot(inventory, hotbar, 8 + hotbar * 18, 198));
        }

        updateSmithingResult();
    }

    public int tier() {
        return this.tier;
    }

    public List<HbmAnvilRecipes.ConstructionRecipe> constructionRecipes() {
        return this.constructionRecipes;
    }

    public int countPlayerItem(final ItemStack target) {
        int count = 0;
        for (final ItemStack stack : this.playerInventory.items) {
            if (ItemStack.isSameItemSameTags(stack, target)) {
                count += stack.getCount();
            }
        }
        return count;
    }

    @Override
    public void slotsChanged(final @NotNull Container container) {
        super.slotsChanged(container);
        updateSmithingResult();
    }

    @Override
    public boolean clickMenuButton(final @NotNull Player player, final int buttonId) {
        final boolean craftAll = buttonId >= CRAFT_ALL_OFFSET;
        final int recipeIndex = buttonId >= CRAFT_ALL_OFFSET ? buttonId - CRAFT_ALL_OFFSET : buttonId;
        if (recipeIndex < 0 || recipeIndex >= this.constructionRecipes.size()) {
            return false;
        }
        final HbmAnvilRecipes.ConstructionRecipe recipe = this.constructionRecipes.get(recipeIndex);
        final ItemStack outputStack = recipe.outputStack();
        final int maxCrafts = craftAll ? Math.max(1, outputStack.getMaxStackSize() / Math.max(1, outputStack.getCount())) : 1;
        int crafted = 0;

        while (crafted < maxCrafts && consumePlayerItem(player, recipe.inputStack())) {
            player.getInventory().placeItemBackInInventory(outputStack.copy());
            crafted++;
        }

        if (crafted <= 0) {
            return false;
        }
        broadcastChanges();
        return true;
    }

    @Override
    public @NotNull ItemStack quickMoveStack(final @NotNull Player player, final int index) {
        ItemStack copied = ItemStack.EMPTY;
        final Slot slot = this.slots.get(index);
        if (!slot.hasItem()) {
            return ItemStack.EMPTY;
        }
        final ItemStack stack = slot.getItem();
        copied = stack.copy();

        if (index == RESULT_SLOT) {
            if (!moveItemStackTo(stack, PLAYER_INV_START, PLAYER_INV_END + 1, true)) {
                return ItemStack.EMPTY;
            }
            slot.onQuickCraft(stack, copied);
        } else if (index == INPUT_SLOT_LEFT || index == INPUT_SLOT_RIGHT) {
            if (!moveItemStackTo(stack, PLAYER_INV_START, PLAYER_INV_END + 1, false)) {
                return ItemStack.EMPTY;
            }
        } else if (!moveItemStackTo(stack, INPUT_SLOT_LEFT, INPUT_SLOT_RIGHT + 1, false)) {
            return ItemStack.EMPTY;
        }

        if (stack.isEmpty()) {
            slot.set(ItemStack.EMPTY);
        } else {
            slot.setChanged();
        }

        if (stack.getCount() == copied.getCount()) {
            return ItemStack.EMPTY;
        }

        slot.onTake(player, stack);
        return copied;
    }

    @Override
    public void removed(final @NotNull Player player) {
        super.removed(player);
        this.access.execute((level, blockPos) -> clearContainer(player, this.input));
    }

    @Override
    public boolean stillValid(final @NotNull Player player) {
        return this.access.evaluate((level, blockPos) -> level.getBlockState(blockPos).getBlock() instanceof NtmAnvilBlock
            && player.distanceToSqr(blockPos.getX() + 0.5D, blockPos.getY() + 0.5D, blockPos.getZ() + 0.5D) <= 64.0D, true);
    }

    private void updateSmithingResult() {
        this.selectedSmithingRecipe = null;
        this.result.setItem(0, ItemStack.EMPTY);
        final ItemStack left = this.input.getItem(INPUT_SLOT_LEFT);
        final ItemStack right = this.input.getItem(INPUT_SLOT_RIGHT);
        if (left.isEmpty() || right.isEmpty()) {
            return;
        }
        for (final HbmAnvilRecipes.SmithingRecipe recipe : HbmAnvilRecipes.smithingRecipesForTier(this.tier)) {
            if (recipe.matches(left, right)) {
                this.selectedSmithingRecipe = recipe;
                this.result.setItem(0, recipe.outputStack());
                break;
            }
        }
        broadcastChanges();
    }

    private void consumeSmithingInputs() {
        if (this.selectedSmithingRecipe == null) {
            return;
        }
        this.input.removeItem(INPUT_SLOT_LEFT, this.selectedSmithingRecipe.leftCount());
        this.input.removeItem(INPUT_SLOT_RIGHT, this.selectedSmithingRecipe.rightCount());
        updateSmithingResult();
    }

    private boolean consumePlayerItem(final Player player, final ItemStack target) {
        if (countPlayerItem(target) < target.getCount()) {
            return false;
        }
        int remaining = target.getCount();
        for (final ItemStack stack : player.getInventory().items) {
            if (!ItemStack.isSameItemSameTags(stack, target)) {
                continue;
            }
            final int removed = Math.min(remaining, stack.getCount());
            stack.shrink(removed);
            remaining -= removed;
            if (remaining <= 0) {
                return true;
            }
        }
        return true;
    }

    public BlockPos pos() {
        return this.pos;
    }

    private static BlockPos readMenuPos(final FriendlyByteBuf buffer) {
        return buffer.readBlockPos();
    }

    private static int resolveTier(final Level level, final BlockPos pos) {
        return level.getBlockState(pos).getBlock() instanceof final NtmAnvilBlock block ? block.tier() : NtmAnvilBlock.TIER_IRON;
    }
}
