package dev.JustRed23.fuie.api.config.components;

import net.minecraft.client.gui.GuiGraphics;

public abstract class ComponentEvents extends ComponentGraphics {

    //Peripheral events
    public void onMouseEnter() {}
    public void onMouseLeave() {}
    public void onMouseClick(double mouseX, double mouseY) {}
    public void onMouseRelease(double mouseX, double mouseY) {}
    public void onMouseDrag(double mouseX, double mouseY) {}
    public void onMouseScroll(double mouseX, double mouseY, double scroll) {}

    public void onKeyPress(int keyCode, int scanCode, int modifiers) {}
    public void onKeyRelease(int keyCode, int scanCode, int modifiers) {}
    public void onKeyType(char typedChar, int keyCode) {}

    //UI events
    public void onComponentUpdate() {}
    public void onComponentRender(GuiGraphics g) {}
}
