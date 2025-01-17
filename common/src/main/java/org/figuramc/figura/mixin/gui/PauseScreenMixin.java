package org.figuramc.figura.mixin.gui;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.figuramc.figura.gui.screens.WardrobeScreen;
import org.figuramc.figura.gui.widgets.Button;
import org.figuramc.figura.utils.CosmetiguraIdentifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PauseScreen.class)
public class PauseScreenMixin extends Screen {
    @Unique
    private static final ResourceLocation KRYEIT_ICON = new CosmetiguraIdentifier("textures/gui/kryeit.png");
    protected PauseScreenMixin(Component title) {
        super(title);
    }

    @Inject(method = "createPauseMenu", at = @At("RETURN"))
    private void createTopButton(CallbackInfo ci) {
        int optionsButtonX = this.width / 2 - 100;
        int optionsButtonY = this.height / 4 + 80;

        int x = optionsButtonX - 25;
        int y = optionsButtonY;

        addRenderableWidget(new Button(x, y, 20, 20, 0, 0, 20, KRYEIT_ICON, 60, 20, null, btn -> this.minecraft.setScreen(new WardrobeScreen(this))) {
            @Override
            public void renderWidget(GuiGraphics gui, int mouseX, int mouseY, float delta) {
                renderVanillaBackground(gui, mouseX, mouseY, delta);
                super.renderWidget(gui, mouseX, mouseY, delta);
            }
        });
    }
}
