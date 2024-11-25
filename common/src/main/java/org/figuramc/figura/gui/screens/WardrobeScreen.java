package org.figuramc.figura.gui.screens;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import org.figuramc.figura.FiguraMod;
import org.figuramc.figura.avatar.local.LocalAvatarFetcher;
import org.figuramc.figura.gui.FiguraToast;
import org.figuramc.figura.gui.widgets.EntityPreview;
import org.figuramc.figura.gui.widgets.lists.AvatarList;
import org.figuramc.figura.utils.FiguraText;
import org.figuramc.figura.utils.IOUtils;

import java.nio.file.Path;
import java.util.List;

public class WardrobeScreen extends AbstractPanelScreen {
    public WardrobeScreen(Screen parentScreen) {
        super(parentScreen, FiguraText.of("gui.panels.title.wardrobe"));
    }

    @Override
    protected void init() {
        super.init();

        // screen
        Minecraft minecraft = Minecraft.getInstance();
        int middle = width / 2;
        int panels = getPanels();

        int modelBgSize = Math.min(width - panels - 16, height - 96);
        panels = Math.max((width - modelBgSize) / 2 - 8, panels);

        // -- left -- //

        AvatarList avatarList = new AvatarList(4, 20, panels, height - 32, this);
        addRenderableWidget(avatarList);

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
        gui.drawString(Minecraft.getInstance().font, "Wardrobe", 20, 40, 0xFFFFFF);
        super.render(gui, mouseX, mouseY, delta);
    }

    @Override
    public void removed() {
        super.removed();
        LocalAvatarFetcher.save();
    }

    @Override
    public void onFilesDrop(List<Path> paths) {
        super.onFilesDrop(paths);

        StringBuilder packs = new StringBuilder();
        for (int i = 0; i < paths.size(); i++) {
            if (i > 0)
                packs.append("\n");
            packs.append(IOUtils.getFileNameOrEmpty(paths.get(i)));
        }

        this.minecraft.setScreen(new FiguraConfirmScreen(confirmed -> {
            if (confirmed) {
                try {
                    LocalAvatarFetcher.loadExternal(paths);
                    FiguraToast.sendToast(FiguraText.of("toast.wardrobe_copy.success", paths.size()));
                } catch (Exception e) {
                    FiguraToast.sendToast(FiguraText.of("toast.wardrobe_copy.error"), FiguraToast.ToastType.ERROR);
                    FiguraMod.LOGGER.error("Failed to copy files", e);
                }
            }
            this.minecraft.setScreen(this);
        }, FiguraText.of("gui.wardrobe.drop_files"), packs.toString(), this));
    }
}
