package org.figuramc.figura.cosmetics;

import dev.architectury.networking.NetworkManager;
import io.netty.buffer.Unpooled;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.figuramc.figura.CosmetiguraMod;
import org.figuramc.figura.cosmetics.network.SelectCosmeticsC2SPacket;
import org.figuramc.figura.gui.widgets.AbstractContainerElement;
import org.figuramc.figura.gui.widgets.Button;
import org.figuramc.figura.utils.ui.UIHelper;

public class CosmeticWidget extends AbstractContainerElement {
    private static final Component UNEQUIP_MESSAGE = Component.literal("Unequip");
    private static final Component EQUIP_MESSAGE = Component.literal("Equip");
    private final CosmeticManager.WardrobeEntry cosmetic;
    private final ResourceLocation thumbnail;
    private boolean equipped = false;
    private final Button equipButton;

    public CosmeticWidget(int x, int y, CosmeticManager.WardrobeEntry cosmetic) {
        super(x, y, 125, 60);
        this.cosmetic = cosmetic;
        this.thumbnail = new ResourceLocation("figura", "thumbnail_" + cosmetic.id());

        long[] cosmetics = CosmeticManager.getEquippedCosmetics(CosmetiguraMod.getLocalPlayerUUID());
        for (long id : cosmetics) {
            if (id == cosmetic.id()) {
                equipped = true;
                break;
            }
        }

        this.equipButton = new Button(getX() + 55, getY() + 30, 80, 20, equipped ? UNEQUIP_MESSAGE : EQUIP_MESSAGE, null, button -> {
            equipped = toggleEquipped();
            button.setMessage(equipped ? UNEQUIP_MESSAGE : EQUIP_MESSAGE);
        });
        children.add(equipButton);
    }

    @Override
    public void render(GuiGraphics gui, int mouseX, int mouseY, float delta) {
        super.render(gui, mouseX, mouseY, delta);

        UIHelper.blit(gui, getX(), getY(), 40, 60, thumbnail);

        gui.drawString(Minecraft.getInstance().font, cosmetic.name(), getX() + 45, getY() + 3, 0xFFFFFF);
        gui.drawString(Minecraft.getInstance().font, cosmetic.type().toString(), getX() + 45, getY() + 15, 0xAAAAAA);

        equipButton.setY(getY() + 30);

        equipButton.render(gui, mouseX, mouseY, delta);
    }

    private boolean toggleEquipped() {
        NetworkManager.sendToServer(SelectCosmeticsC2SPacket.ID, SelectCosmeticsC2SPacket.write(new FriendlyByteBuf(Unpooled.buffer()), cosmetic.id(), !equipped));
        return !equipped;
    }
}
