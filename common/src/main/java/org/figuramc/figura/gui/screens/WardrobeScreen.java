package org.figuramc.figura.gui.screens;

import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.figuramc.figura.avatar.local.LocalAvatarFetcher;
import org.figuramc.figura.gui.widgets.Button;
import org.figuramc.figura.gui.widgets.EntityPreview;
import org.figuramc.figura.gui.widgets.lists.AvatarList;
import org.figuramc.figura.utils.FiguraText;

public class WardrobeScreen extends AbstractPanelScreen {
    public WardrobeScreen(Screen parentScreen) {
        super(parentScreen, FiguraText.of("gui.panels.title.wardrobe"));
    }

    @Override
    protected void init() {
        super.init();

        // screen
        Minecraft minecraft = Minecraft.getInstance();
        int panels = getPanels();

        int modelBgSize = Math.min(width - panels - 16, height - 96);
        panels = Math.max((width - modelBgSize) / 2 - 8, panels);

        // -- left -- //

        AvatarList avatarList = new AvatarList(4, 20, panels, height - 32, this);
        addRenderableWidget(avatarList);

        Button storeButton = new Button(20, 40, 130, 20, Component.literal("Open store").withStyle(ChatFormatting.DARK_PURPLE), null, button -> Util.getPlatform().openUri("https://kryeit.com/store"));
        addRenderableWidget(storeButton);

        // -- middle -- // 

        // model
        int entitySize = 11 * modelBgSize / 29;
        int entityY = this.height / 2 - modelBgSize / 2;

        EntityPreview entity = new EntityPreview(panels + 10, entityY, modelBgSize, modelBgSize, entitySize, -15f, 30f, minecraft.player, this);
        addRenderableWidget(entity);
    }

    private int getPanels() {
        return Math.min(width / 2, 256) - 8;
    }

    @Override
    public void tick() {
        super.tick();
    }

    @Override
    public void render(GuiGraphics gui, int mouseX, int mouseY, float delta) {
        gui.drawString(Minecraft.getInstance().font, "Wardrobe", 20, 30, 0xFFFFFF);

        super.render(gui, mouseX, mouseY, delta);
    }

    @Override
    public void removed() {
        super.removed();
        LocalAvatarFetcher.save();
    }
}
