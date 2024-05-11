package dev.JustRed23.fuie;

import dev.JustRed23.fuie.api.config.components.Checkbox;
import dev.JustRed23.fuie.api.config.components.ConfigComponent;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class TestScreen extends Screen {

    ConfigComponent component = new Checkbox("Test", "A test description, used to fool the world", false);

    protected TestScreen() {
        super(Component.literal("Testing"));
    }

    public void render(GuiGraphics $$0, int $$1, int $$2, float $$3) {
        renderBackground($$0);
        component.onComponentRender($$0);
    }

    public void tick() {
        component.onComponentUpdate();
    }

    public boolean mouseClicked(double $$0, double $$1, int $$2) {
        component.onMouseClick($$0, $$1);
        return super.mouseClicked($$0, $$1, $$2);
    }

    public boolean mouseReleased(double $$0, double $$1, int $$2) {
        component.onMouseRelease($$0, $$1);
        return super.mouseReleased($$0, $$1, $$2);
    }
}
