package dev.JustRed23.fuie.api.config.components;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ComponentList extends ConfigComponent<List<ConfigComponent<?>>> {

    public int scrollBarWidth = 6;
    public int componentMargin = 4;
    public int minimumScrollBarHeight = 10;
    public int scrollBarBackground = 0xFF444444;
    public int scrollBarForeground = 0xFF707070;

    private final int titleCardHeight = font.lineHeight + 2;

    private int lastComponentMargin = componentMargin;

    private boolean enableDragging;

    private boolean needsScroll;
    private int scroll;

    private int scrollBarY;
    private int scrollBarHeight;

    public ComponentList(String name, @NotNull List<ConfigComponent<?>> components) {
        super(name, null, components);
        if (components.isEmpty()) throw new IllegalArgumentException("Component list cannot be empty");

        setWidth(components.stream().mapToInt(ConfigComponent::getWidth).max().orElse(100) + scrollBarWidth + 2);
        setHeight(200);
    }

    private void updateComponentPositions() {
        if (!initialized) return;

        int currentY = getComponentY() + titleCardHeight;
        for (ConfigComponent<?> component : getComponents()) {
            component.setX(getComponentX());
            component.setY(currentY);
            component.setWidth(getComponentWidth() - scrollBarWidth - 2);
            currentY += component.getHeight() + componentMargin;
        }
    }

    protected void updateComponent() {
        getComponents().forEach(ConfigComponent::onComponentUpdate);
        needsScroll = getComponents().stream().mapToInt(ConfigComponent::getHeight).sum() > getComponentHeight();

        if (lastComponentMargin != componentMargin) {
            lastComponentMargin = componentMargin;
            updateComponentPositions();
        }
    }

    protected void renderComponent(GuiGraphics g) {
        drawRect(g, getComponentX(), getComponentY() + titleCardHeight, getComponentWidth(), getComponentHeight() - titleCardHeight, getBackgroundColor());

        drawRect(g, getComponentX(), getComponentY(), getComponentWidth(), titleCardHeight, getForegroundColor());
        g.drawString(font, calculateDisplayString(getName(), 2), getComponentX() + 2, getComponentY() + 1, getTextColor());

        g.enableScissor(getComponentX(), getComponentY() + titleCardHeight, getComponentX() + getComponentWidth() - scrollBarWidth - 2, getComponentY() + getComponentHeight());
        g.pose().pushPose();
        g.pose().translate(0, -scroll, 0);
        getComponents().forEach(component -> component.onComponentRender(g));
        g.pose().popPose();
        g.disableScissor();

        drawScrollBar(g);
    }

    private void drawScrollBar(GuiGraphics g) {
        if (!needsScroll) return;

        int x = getComponentX() + getComponentWidth() - scrollBarWidth - 1;
        int y = getComponentY() + titleCardHeight + 1;

        drawRect(g, x, y, scrollBarWidth, getComponentHeight() - titleCardHeight - 2, scrollBarBackground);

        scrollBarHeight = ((getComponentHeight() - titleCardHeight) * getComponentHeight()) / getComponentHeightTotal();
        scrollBarHeight = Math.max(scrollBarHeight, minimumScrollBarHeight);

        int sbY = Mth.clamp(((getComponentHeight() - titleCardHeight) * scroll) / getComponentHeightTotal(), 0, getComponentHeight() - titleCardHeight - scrollBarHeight - 2);
        scrollBarY = y + sbY;

        drawRect(g, x, scrollBarY, scrollBarWidth, scrollBarHeight, scrollBarForeground);
    }

    public void onMouseScroll(double mouseX, double mouseY, double scroll) {
        if (needsScroll && inScrollBarBounds(mouseX, mouseY)) {
            int scrollAmount = (int) scroll * 10;
            int maxScroll = getComponentHeightTotal() - getComponentHeight() + titleCardHeight;
            this.scroll = Mth.clamp(this.scroll - scrollAmount, 0, maxScroll);
            return;
        }

        getComponents().forEach(component -> component.onMouseScroll(mouseX, mouseY, scroll));
    }

    public void onMouseClick(double mouseX, double mouseY) {
        if (needsScroll && inScrollBarKnobBounds(mouseX, mouseY)) {
            enableDragging = true;
            return;
        }

        getComponents().forEach(component -> component.onMouseClick(mouseX, mouseY + scroll));
    }

    public void onMouseRelease(double mouseX, double mouseY) {
        enableDragging = false;
        getComponents().forEach(component -> component.onMouseRelease(mouseX, mouseY + scroll));
    }

    public void onMouseDrag(double mouseX, double mouseY, double deltaX, double deltaY) {
        if (enableDragging) {
            int componentY = getComponentY();
            int maxScroll = getComponentHeightTotal() - getComponentHeight();

            int relativeY = (int) (mouseY - componentY - (scrollBarHeight / 2.0));
            double scrollRatio = (double) relativeY / (getComponentHeight() - scrollBarHeight - 2);

            this.scroll = Mth.clamp((int) (scrollRatio * maxScroll), 0, maxScroll);
            return;
        }

        getComponents().forEach(component -> component.onMouseDrag(mouseX, mouseY + scroll, deltaX, deltaY));
    }

    private boolean inScrollBarBounds(double mouseX, double mouseY) {
        int x = getComponentX() + getComponentWidth() - scrollBarWidth - 1;
        int y = getComponentY() + titleCardHeight + 1;
        return mouseX >= x && mouseX <= x + scrollBarWidth && mouseY >= y && mouseY <= y + getComponentHeight() - titleCardHeight - 2;
    }

    private boolean inScrollBarKnobBounds(double mouseX, double mouseY) {
        return mouseX >= getComponentX() + getComponentWidth() - scrollBarWidth - 1 && mouseX <= getComponentX() + getComponentWidth() - 1 &&
                mouseY >= scrollBarY && mouseY <= scrollBarY + scrollBarHeight;
    }

    public @NotNull List<ConfigComponent<?>> getComponents() {
        assert getValue() != null;
        return getValue().stream()
                .filter(component -> component != this) //someone could be funny and add the list to itself, causing a stack overflow
                .toList();
    }

    private int getComponentHeightTotal() {
        return getComponents().stream().mapToInt(ConfigComponent::getHeight).sum() + ((getComponents().size() - 1) * componentMargin);
    }

    //events
    public void onMouseMove(double mouseX, double mouseY) {
        getComponents().forEach(component -> component.onMouseMove(mouseX, mouseY + scroll));
    }

    public void onKeyPress(int keyCode, int scanCode, int modifiers) {
        getComponents().forEach(component -> component.onKeyPress(keyCode, scanCode, modifiers));
    }
    public void onKeyRelease(int keyCode, int scanCode, int modifiers) {
        getComponents().forEach(component -> component.onKeyRelease(keyCode, scanCode, modifiers));
    }
    public void onKeyType(char typedChar, int keyCode) {
        getComponents().forEach(component -> component.onKeyType(typedChar, keyCode));
    }

    //update component positions
    public void setX(int x) {
        super.setX(x);
        updateComponentPositions();
    }
    public void setY(int y) {
        super.setY(y);
        updateComponentPositions();
    }
    protected void setWidth(int width) {
        super.setWidth(width);
        updateComponentPositions();
    }
    protected void setHeight(int height) {
        super.setHeight(height);
        updateComponentPositions();
    }
    protected void setPadding(@NotNull ComponentPadding padding) {
        super.setPadding(padding);
        updateComponentPositions();
    }
}
