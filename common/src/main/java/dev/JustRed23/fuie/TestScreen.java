package dev.JustRed23.fuie;

import dev.JustRed23.fuie.api.config.components.*;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.Arrays;
import java.util.List;

public class TestScreen extends Screen {

    ConfigComponent<?>[] component;

    enum TestEnum {
        OPTION_1,
        OPTION_2,
        OPTION_3;

        public String toString() {
            return name().replace("_", " ");
        }
    }

    protected TestScreen() {
        super(Component.literal("Testing"));

        Checkbox checkbox1 = new Checkbox("Test 1", "This is a checkbox", false);

        Checkbox checkbox2 = new Checkbox("Test 2", "This is a checkbox", true);
        checkbox2.setX(checkbox1.getX() + checkbox1.getWidth() + 10);

        Slider slider1 = new Slider("Slider 1", "This is a slider", 0, 100, 0);
        slider1.setY(checkbox1.getY() + checkbox1.getHeight() + 10);

        Slider slider2 = new Slider("Slider 2", "This is a slider", 0, 100, 50);
        slider2.setY(slider1.getY() + slider1.getHeight() + 10);

        Slider slider3 = new Slider("Slider 3", "This is a slider", 0, 100, 100);
        slider3.setY(slider2.getY() + slider2.getHeight() + 10);

        InputBox inputBox = new InputBox("Bingus Box", "This is an input box", "bingus", 20);
        inputBox.setY(slider3.getY() + slider3.getHeight() + 10);

        List<String> list = Arrays.asList("Option 1", "Option 2", "Option 3", "Option 4", "Option 5");

        SelectBox selectBox1 = new SelectBox("String Select Box", "This is a string select box", list);
        selectBox1.setY(inputBox.getY() + inputBox.getHeight() + 10);

        SelectBox selectBox2 = new SelectBox("String Select Box", "This is an string select box", list, "Option 3");
        selectBox2.setX(selectBox1.getX() + selectBox1.getWidth() + 10);
        selectBox2.setY(selectBox1.getY());

        SelectBox enumBox = new SelectBox("Enum Select Box", "This is an enum select box", TestEnum.values(), TestEnum.OPTION_1);
        enumBox.setX(selectBox2.getX() + selectBox2.getWidth() + 10);
        enumBox.setY(selectBox2.getY());

        component = new ConfigComponent[] {
                checkbox1,
                checkbox2,
                slider1,
                slider2,
                slider3,
                inputBox,
                selectBox1,
                selectBox2,
                enumBox
        };
    }

    public void render(GuiGraphics $$0, int $$1, int $$2, float $$3) {
        renderBackground($$0);
        Arrays.stream(component).forEach(c -> c.onComponentRender($$0));
    }

    public void tick() {
        Arrays.stream(component).forEach(ConfigComponent::onComponentUpdate);
    }

    public boolean mouseClicked(double $$0, double $$1, int $$2) {
        Arrays.stream(component).forEach(c -> c.onMouseClick($$0, $$1));
        return super.mouseClicked($$0, $$1, $$2);
    }

    public boolean mouseReleased(double $$0, double $$1, int $$2) {
        Arrays.stream(component).forEach(c -> c.onMouseRelease($$0, $$1));
        return super.mouseReleased($$0, $$1, $$2);
    }

    public void mouseMoved(double $$0, double $$1) {
        Arrays.stream(component).forEach(c -> c.onMouseMove($$0, $$1));
        super.mouseMoved($$0, $$1);
    }

    public boolean mouseDragged(double $$0, double $$1, int $$2, double $$3, double $$4) {
        Arrays.stream(component).forEach(c -> c.onMouseDrag($$0, $$1, $$3, $$4));
        return super.mouseDragged($$0, $$1, $$2, $$3, $$4);
    }

    public boolean keyPressed(int $$0, int $$1, int $$2) {
        Arrays.stream(component).forEach(c -> c.onKeyPress($$0, $$1, $$2));
        return super.keyPressed($$0, $$1, $$2);
    }

    public boolean keyReleased(int $$0, int $$1, int $$2) {
        Arrays.stream(component).forEach(c -> c.onKeyRelease($$0, $$1, $$2));
        return super.keyReleased($$0, $$1, $$2);
    }

    public boolean charTyped(char $$0, int $$1) {
        Arrays.stream(component).forEach(c -> c.onKeyType($$0, $$1));
        return super.charTyped($$0, $$1);
    }
}
