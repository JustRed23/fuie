package dev.JustRed23.fuie;

import dev.JustRed23.fuie.api.config.components.Checkbox;
import dev.JustRed23.fuie.api.config.components.ConfigComponent;
import dev.JustRed23.fuie.api.config.components.Slider;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.Arrays;

public class TestScreen extends Screen {

    ConfigComponent<?>[] component;

    protected TestScreen() {
        super(Component.literal("Testing"));

        Checkbox checkbox1 = new Checkbox("Test 1", "This is a checkbox", false);

        Checkbox checkbox2 = new Checkbox("Test 2", "This is a checkbox", true);
        checkbox2.setX(checkbox1.getX() + checkbox1.getWidth() + 10);

        Slider slider1 = new Slider("Slider 1", "This is a slider", 0, 100, 50);
        slider1.setY(checkbox1.getY() + checkbox1.getHeight() + 10);

        component = new ConfigComponent[] {
                checkbox1,
                checkbox2,
                slider1
        };
    }

    public void render(GuiGraphics $$0, int $$1, int $$2, float $$3) {
        renderBackground($$0);
        Arrays.stream(component).forEach(c -> c.renderComponent($$0));
    }

    public void tick() {
        Arrays.stream(component).forEach(ConfigComponent::updateComponent);
    }

    public boolean mouseClicked(double $$0, double $$1, int $$2) {
        Arrays.stream(component).forEach(c -> c.onMouseClick($$0, $$1));
        return super.mouseClicked($$0, $$1, $$2);
    }

    public boolean mouseReleased(double $$0, double $$1, int $$2) {
        Arrays.stream(component).forEach(c -> c.onMouseRelease($$0, $$1));
        return super.mouseReleased($$0, $$1, $$2);
    }

    public boolean mouseDragged(double $$0, double $$1, int $$2, double $$3, double $$4) {
        Arrays.stream(component).forEach(c -> c.onMouseDrag($$0, $$1, $$3, $$4));
        return super.mouseDragged($$0, $$1, $$2, $$3, $$4);
    }
}
