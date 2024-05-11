package dev.JustRed23.fuie.api.config.components;

import org.jetbrains.annotations.NotNull;

public abstract class ComponentGraphics {

    private int x, y, width, height, borderSize;
    private int backgroundColor, borderColor, foregroundColor, textColor;
    private ComponentPadding padding = ComponentPadding.DEFAULT_PADDING;
    private boolean debug;

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = notNegative(x, "X");
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = notNegative(y, "Y");
    }

    public int getWidth() {
        return width;
    }

    protected void setWidth(int width) {
        this.width = notNegative(width, "Width");
    }

    public int getHeight() {
        return height;
    }

    protected void setHeight(int height) {
        this.height = notNegative(height, "Height");
    }

    public ComponentPadding getPadding() {
        return padding;
    }

    protected void setPadding(@NotNull ComponentPadding padding) {
        this.padding = padding;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    protected void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public int getBorderColor() {
        return borderColor;
    }

    protected void setBorderColor(int borderColor) {
        this.borderColor = borderColor;
    }

    public int getBorderSize() {
        return borderSize;
    }

    protected void setBorderSize(int borderSize) {
        this.borderSize = notNegative(borderSize, "Border size");
    }

    public int getForegroundColor() {
        return foregroundColor;
    }

    protected void setForegroundColor(int foregroundColor) {
        this.foregroundColor = foregroundColor;
    }

    public int getTextColor() {
        return textColor;
    }

    protected void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public boolean isDebug() {
        return debug;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    private int notNegative(int value, String name) {
        if (value < 0) throw new IllegalArgumentException(name + " cannot be negative!");
        return value;
    }
}
