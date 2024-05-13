package dev.JustRed23.fuie.api.config.components;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class ConfigComponent<T> extends ComponentEvents {

    private final @NotNull String name;
    private final @Nullable String description;

    private @Nullable T value;

    protected final Font font = Minecraft.getInstance().font;

    public ConfigComponent(@NotNull String name, @Nullable String description, @Nullable T defaultValue) {
        this.name = name;
        this.description = description;
        this.value = defaultValue;

        // Set default values
        setX(0);
        setY(0);
        setWidth(100);
        setHeight(50);
        setBackgroundColor(0xFF000000);
        setBorderColor(0xFFAAAAAA);
        setForegroundColor(0xFF0098FF);
        setTextColor(0xFFFFFFFF);
    }

    //Enforce the implementation of these methods
    protected abstract void updateComponent();
    protected abstract void renderComponent(GuiGraphics g);

    //Helper methods
    protected void drawRect(GuiGraphics g, int x, int y, int width, int height, int color) {
        if (width <= 0 || height <= 0) return;
        g.fill(x, y, x + width, y + height, color);
    }

    protected void drawHollowRect(GuiGraphics g, int x, int y, int width, int height, int color) {
        if (width <= 0 || height <= 0) return;

        if (width > 1) width -= 1;
        if (height > 1) height -= 1;

        g.hLine(x, x + width, y, color);
        g.hLine(x, x + width, y + height, color);
        g.vLine(x, y, y + height, color);
        g.vLine(x + width, y, y + height, color);
    }

    //Getters and Setters
    public @NotNull String getName() {
        return name;
    }

    public @Nullable String getDescription() {
        return description;
    }

    protected void setValue(@Nullable T value) {
        this.value = value;
    }

    public @Nullable T getValue() {
        return value;
    }

    public int getComponentX() {
        return getX() + getPadding().left() + getBorderSize();
    }

    public int getComponentY() {
        return getY() + getPadding().top() + getBorderSize();
    }

    public int getComponentWidth() {
        return getWidth() - (getPadding().left() + getPadding().right() + (getBorderSize() * 2));
    }

    public int getComponentHeight() {
        return getHeight() - (getPadding().top() + getPadding().bottom() + (getBorderSize() * 2));
    }

    //Internal methods
    @ApiStatus.Internal
    public final void onComponentUpdate() {
        updateComponent();
    }

    @ApiStatus.Internal
    public final void onComponentRender(GuiGraphics g) {
        if (isDebug()) renderPadding(g);
        renderBorder(g);
        renderComponent(g);
    }

    private void renderPadding(GuiGraphics g) {
        int borderSize = getBorderSize();
        ComponentPadding padding = getPadding();

        if (!padding.paddingExists()) return;

        int colorLeft = 0xFFFF0000;
        int colorRight = 0xFF00FF00;
        int colorTop = 0xFF0000FF;
        int colorBottom = 0xFFFFFF00;

        int x = getX() + borderSize;
        int y = getY() + borderSize;
        int width = getWidth() - (borderSize * 2);
        int height = getHeight() - (borderSize * 2);

        //Left side
        g.fill(x,
                y + padding.top(),
                x + padding.left(),
                y + height - padding.bottom(),
                colorLeft
        );

        //Right side
        g.fill(x + width - padding.right(),
                y + padding.top(),
                x + width,
                y + height - padding.bottom(),
                colorRight
        );

        //Top side
        g.fill(x,
                y,
                x + width,
                y + padding.top(),
                colorTop
        );

        //Bottom side
        g.fill(x,
                y + height - padding.bottom(),
                x + width,
                y + height,
                colorBottom
        );
    }

    private void renderBorder(GuiGraphics g) {
        int borderColor = getBorderColor();
        int borderSize = getBorderSize();

        if (borderSize == 0) return;

        drawHollowRect(g, getX(), getY(), getWidth(), getHeight(), borderColor);
    }
}
