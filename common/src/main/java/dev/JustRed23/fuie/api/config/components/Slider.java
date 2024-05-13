package dev.JustRed23.fuie.api.config.components;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Slider extends ConfigComponent<Double> {

    private int lastSliderBaseHeight, lastSliderIndent, lastSliderButtonWidth, lastSliderButtonHeight;

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

        setHeight(sliderBaseHeight + sliderButtonHeight + Minecraft.getInstance().font.lineHeight + 4);
    }

    public Slider(@NotNull String name, @Nullable String description, double defaultValue) {
        this(name, description, 0, 1, defaultValue);
    }

    public void updateComponent() {
        if (lastSliderBaseHeight != sliderBaseHeight || lastSliderIndent != sliderIndent || lastSliderButtonWidth != sliderButtonWidth || lastSliderButtonHeight != sliderButtonHeight) {
            setHeight(sliderBaseHeight + sliderButtonHeight + Minecraft.getInstance().font.lineHeight + 4);
            lastSliderBaseHeight = sliderBaseHeight;
            lastSliderIndent = sliderIndent;
            lastSliderButtonWidth = sliderButtonWidth;
            lastSliderButtonHeight = sliderButtonHeight;
        }
    }

    public void renderComponent(GuiGraphics g) {
        //Slider base
        final int sbX = getComponentX() + sliderIndent;
        final int sbY = getComponentY() + (getComponentHeight() - (sliderBaseHeight / 2) - (sliderButtonHeight / 2));
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

        //Slider text
        final String text = getName() + ": " + String.format("%.2f", value);
        final Font font = Minecraft.getInstance().font;
        g.drawString(font, text, getComponentX() + sliderIndent, sbY - (sliderButtonHeight / 2) - font.lineHeight, getTextColor());
    }

    public void onMouseClick(double mouseX, double mouseY) {
        final int sbX = getComponentX() + sliderIndent;
        final int sbY = getComponentY() + (getComponentHeight() - (sliderBaseHeight / 2) - (sliderButtonHeight / 2));
        final int sbW = getComponentWidth() - (sliderIndent * 2);

        final int rightX = sbX + sbW;
        final int topY = sbY - (sliderButtonHeight / 2);
        final int bottomY = topY + sliderBaseHeight + sliderButtonHeight;

        canDrag = mouseX >= sbX && mouseX <= rightX && mouseY >= topY && mouseY <= bottomY;

        if (canDrag) {
            final double valueRange = maxValue - minValue;
            final double valuePercentage = (mouseX - sbX) / sbW;

            double newValue = minValue + (valueRange * valuePercentage);
            newValue = Math.max(minValue, Math.min(maxValue, newValue));

            setValue(newValue);
        }
    }

    public void onMouseRelease(double mouseX, double mouseY) {
        canDrag = false;
    }

    public void onMouseDrag(double mouseX, double mouseY, double deltaX, double deltaY) {
        if (canDrag) {
            final int sbX = getComponentX() + sliderIndent;
            final int sbW = getComponentWidth() - (sliderIndent * 2);

            final double valueRange = maxValue - minValue;
            final double valuePercentage = (mouseX - sbX) / sbW;

            double newValue = minValue + (valueRange * valuePercentage);
            newValue = Math.max(minValue, Math.min(maxValue, newValue));

            setValue(newValue);
        }
    }
}
