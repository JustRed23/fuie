package dev.JustRed23.fuie.api.config.components;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Checkbox extends ConfigComponent<Boolean> {

    private int lastCbSize = 10, lastTextOffset = 4;
    public int cbSize = 10;
    public int textOffset = 4;

    public Checkbox(@NotNull String name, @Nullable String description, boolean defaultValue) {
        super(name, description, defaultValue);
        setWidth(cbSize + Minecraft.getInstance().font.width(name) + textOffset + getPadding().leftRight() + (getBorderSize() * 2));
        setHeight(cbSize + getPadding().topBottom() + (getBorderSize() * 2));
    }

    public void updateComponent() {
        if (lastCbSize != cbSize || lastTextOffset != textOffset) {
            setWidth(cbSize + Minecraft.getInstance().font.width(getName()) + textOffset + getPadding().leftRight() + (getBorderSize() * 2));
            setHeight(cbSize + getPadding().topBottom() + (getBorderSize() * 2));
            lastCbSize = cbSize;
            lastTextOffset = textOffset;
        }
    }

    public void onMouseClick(double mouseX, double mouseY) {
        if (mouseX >= getComponentX() && mouseX <= getComponentX() + cbSize && mouseY >= getComponentY() && mouseY <= getComponentY() + cbSize)
            setValue(!Boolean.TRUE.equals(getValue()));
    }

    public void renderComponent(GuiGraphics g) {
        int color = getForegroundColor();
        g.hLine(getComponentX(), getComponentX() + cbSize - 1, getComponentY(), color);
        g.hLine(getComponentX(), getComponentX() + cbSize - 1, getComponentY() + cbSize - 1, color);
        g.vLine(getComponentX(), getComponentY(), getComponentY() + cbSize, color);
        g.vLine(getComponentX() + cbSize - 1, getComponentY(), getComponentY() + cbSize - 1, color);

        int checkOffset = 2;
        if (Boolean.TRUE.equals(getValue())) {
            int cbX = getComponentX() + checkOffset;
            int cbY = getComponentY() + checkOffset;
            int cbW = cbSize - (2 * checkOffset);
            int cbH = cbSize - (2 * checkOffset);

            g.fill(cbX, cbY, cbX + cbW, cbY + cbH, getForegroundColor());
        }

        g.drawString(Minecraft.getInstance().font, getName(), getComponentX() + cbSize + textOffset, getComponentY() + checkOffset, getTextColor());
    }
}
