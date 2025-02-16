package com.maciej916.indreb.common.block.impl.machines.compressor;

import com.maciej916.indreb.IndReb;
import com.maciej916.indreb.common.screen.IndRebScreen;
import com.maciej916.indreb.common.screen.progress.GuiProgressCompressing;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class ScreenCompressor extends IndRebScreen<ContainerCompressor> {

    public ScreenCompressor(ContainerCompressor container, Inventory inv, Component name) {
        super(container, inv, name);
    }

    @Override
    public void init() {
        super.init();

        addRenderableOnly(new GuiProgressCompressing(this, 71, 35, ((BlockEntityCompressor) getBlockEntity()).progress));

    }

    @Override
    public ResourceLocation getGuiLocation() {
        return new ResourceLocation(IndReb.MODID, "textures/gui/container/standard_machine.png");
    }
}
