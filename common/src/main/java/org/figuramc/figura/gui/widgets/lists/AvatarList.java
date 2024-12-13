package org.figuramc.figura.gui.widgets.lists;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.util.Mth;
import org.figuramc.figura.cosmetics.CosmeticManager;
import org.figuramc.figura.cosmetics.CosmeticWidget;
import org.figuramc.figura.gui.screens.AbstractPanelScreen;
import org.figuramc.figura.gui.widgets.FiguraWidget;
import org.figuramc.figura.utils.ui.UIHelper;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class AvatarList extends AbstractList {

    // -- Variables -- // 
    private final ArrayList<CosmeticWidget> avatarList = new ArrayList<>();

    private int totalHeight = 0;

    public static Path selectedEntry;

    // -- Constructors -- // 

    public AvatarList(int x, int y, int width, int height, AbstractPanelScreen parentScreen) {
        super(x, y, width, height);

        for (CosmeticManager.WardrobeEntry entry : CosmeticManager.getWardrobe()) {
            CosmeticWidget widget = new CosmeticWidget(x, y, entry);
            avatarList.add(widget);
            children.add(widget);
        }

        // scrollbar
        this.scrollBar.setY(y + 48);
        this.scrollBar.setHeight(height - 52);

        // scissors
        this.updateScissors(1, 49, -2, -50);

        // initial load
    }

    // -- Functions -- // 
    @Override
    public void tick() {
        // update list
        super.tick();
    }

    @Override
    public void render(GuiGraphics gui, int mouseX, int mouseY, float delta) {
        int x = getX();
        int y = getY();
        int width = getWidth();
        int height = getHeight();

        // background and scissors
        UIHelper.blitSliced(gui, x, y, width, height, UIHelper.OUTLINE_FILL);
        enableScissors(gui);

        // scrollbar
        totalHeight = 2;
        for (FiguraWidget widget : avatarList)
            totalHeight += widget.getHeight() + 2;
        int entryHeight = avatarList.isEmpty() ? 0 : totalHeight / avatarList.size();

        scrollBar.setVisible(totalHeight > height - 49);
        scrollBar.setScrollRatio(entryHeight, totalHeight - (height - 49));

        // render list
        int xOffset = scrollBar.isVisible() ? 4 : 11;
        int yOffset = scrollBar.isVisible() ? (int) -(Mth.lerp(scrollBar.getScrollProgress(), -49, totalHeight - height)) : 49;

        for (CosmeticWidget avatar : avatarList) {
            avatar.render(gui, mouseX, mouseY, delta);

            avatar.setX(x + xOffset);
            avatar.setY(y + yOffset);

            yOffset += avatar.getHeight() + 2;
//
//            if (avatar.getY() + avatar.getHeight() > y + scissorsY)
//                avatar.render(gui, mouseX, mouseY, delta);
//
//            yOffset += avatar.getHeight() + 2;
//            if (yOffset > height)
//                hidden = true;
        }

        // reset scissor
        gui.disableScissor();

        if (avatarList.isEmpty()) {
            gui.drawCenteredString(Minecraft.getInstance().font, "You have no cosmetics available", width / 2, 130, 0xAAAAAA);
            gui.drawCenteredString(Minecraft.getInstance().font, "It's time to fix that ;)", width / 2, 140, 0xAAAAAA);
        }

        // render children
        super.render(gui, mouseX, mouseY, delta);
    }

    public void updateScroll() {
        // store old scroll pos
        double pastScroll = (totalHeight - getHeight()) * scrollBar.getScrollProgress();

        // get new height
        totalHeight = 2;
        for (FiguraWidget avatar : avatarList)
            totalHeight += avatar.getHeight() + 2;

        // set new scroll percentage
        scrollBar.setScrollProgress(pastScroll / (totalHeight - getHeight()));
    }

    @Override
    public List<? extends GuiEventListener> contents() {
        return avatarList;
    }
}
