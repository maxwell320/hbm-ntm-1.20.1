package com.hbm.ntm.client.screen;

import com.hbm.ntm.HbmNtmMod;
import com.hbm.ntm.common.menu.AshpitMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

@SuppressWarnings("null")
public class AshpitScreen extends MachineScreenBase<AshpitMenu> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(HbmNtmMod.MOD_ID, "textures/gui/machine/gui_ashpit.png");

    public AshpitScreen(final AshpitMenu menu, final Inventory inventory, final Component title) {
        super(menu, inventory, title, 176, 168);
    }

    @Override
    protected ResourceLocation texture() {
        return TEXTURE;
    }
}
