package dev.JustRed23.fuie.api.config.components;

import net.minecraft.client.gui.GuiGraphics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Slider extends ConfigComponent<Double> {

    public int sliderBaseHeight = 2;
    public int sliderIndent = 6;

    public int sliderButtonWidth = 6;
    public int sliderButtonHeight = 12;

    private final double minValue, maxValue;

    private boolean canDrag = false;

    public Slider(@NotNull String name, @Nullable String description, double minValue, double maxValue, double defaultValue) {
        super(name, description, defaultValue);
        this.minValue = minValue;
        this.maxValue = maxValue;

        if (defaultValue < minValue || defaultValue > maxValue)
            throw new IllegalArgumentException("Default value must be between min and max value!");

        setBackgroundColor(0xFFBBBBBB);
        setBorderSize(1);
    }

    public Slider(@NotNull String name, @Nullable String description, double defaultValue) {
        this(name, description, 0, 1, defaultValue);
    }

    public void updateComponent() {}

    public void renderComponent(GuiGraphics g) {
        drawHollowRect(g, getComponentX(), getComponentY(), getComponentWidth(), getComponentHeight(), getBorderColor());

        final int halfHeight = getComponentHeight() / 2;
        final int halfSliderBaseHeight = sliderBaseHeight / 2;

        //Slider base
        final int sbX = getComponentX() + sliderIndent;
        final int sbY = getComponentY() + (halfHeight - halfSliderBaseHeight);
        final int sbW = getComponentWidth() - (sliderIndent * 2);
        final int sbH = sliderBaseHeight;

        drawRect(g, sbX, sbY, sbW, sbH, getBackgroundColor());

        //Slider button
        final double valueRange = maxValue - minValue;
        final double value = getValue() == null ? minValue : getValue();
        final double valuePercentage = (value - minValue) / valueRange;

        final int buttonX = (int) (sbX + (sbW * valuePercentage) - (sliderButtonWidth / 2));
        final int buttonY = sbY - ((sliderButtonHeight - sliderBaseHeight) / 2);

        drawRect(g, buttonX, buttonY, sliderButtonWidth, sliderButtonHeight, getForegroundColor());
    }

    public void onMouseClick(double mouseX, double mouseY) {
        final int halfHeight = getComponentHeight() / 2;
        final int halfSliderBaseHeight = sliderBaseHeight / 2;

        final int sbX = getComponentX() + sliderIndent;
        final int sbY = getComponentY() + (halfHeight - halfSliderBaseHeight);
        final int sbW = getComponentWidth() - (sliderIndent * 2);

        final int rightX = sbX + sbW;
        final int topY = sbY - (sliderButtonHeight / 2);
        final int bottomY = topY + sliderBaseHeight + sliderButtonHeight;

        canDrag = mouseX >= sbX && mouseX <= rightX && mouseY >= topY && mouseY <= bottomY;
    }

    public void onMouseRelease(double mouseX, double mouseY) {
        canDrag = false;
    }

    public void onMouseDrag(double mouseX, double mouseY, double deltaX, double deltaY) {
        //TODO: Implement slider dragging
    }
}
