package dev.JustRed23.fuie.api.config.components;

import net.minecraft.SharedConstants;
import net.minecraft.client.gui.GuiGraphics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class InputBox extends ConfigComponent<String> {

    public final int minBoxHeight = 13;

    private int lastBoxBorder, lastTextIndent, lastCaretDelay;
    public int boxBorder = 1;
    public int textIndent = 2;
    public int caretDelay = 10;

    private final int maxTextLength;

    private boolean hasFocus = false;

    private int caretCounter = 0;
    private boolean caretVisible = false;

    public InputBox(@NotNull String name, @Nullable String description, @Nullable String defaultValue, int maxTextLength) {
        super(name, description, defaultValue);

        if (maxTextLength < 1) throw new IllegalArgumentException("Max text length must be at least 1");
        if (defaultValue != null && defaultValue.length() > maxTextLength) throw new IllegalArgumentException("Default value is longer than max text length");

        this.maxTextLength = maxTextLength;
        setHeight(minBoxHeight + font.lineHeight + 2);
    }

    public InputBox(@NotNull String name, @Nullable String description, @Nullable String defaultValue) {
        this(name, description, defaultValue, 100);
    }

    protected void setHeight(int height) {
        int defaultHeight = minBoxHeight + font.lineHeight + 2;
        if (height < defaultHeight) height = defaultHeight;
        super.setHeight(height);
    }

    protected void updateComponent() {
        if (hasFocus) {
            caretCounter++;
            if (caretCounter >= caretDelay) {
                caretCounter = 0;
                caretVisible = !caretVisible;
            }
        }

        if (lastBoxBorder != boxBorder || lastTextIndent != textIndent || lastCaretDelay != caretDelay) {
            lastBoxBorder = boxBorder;
            lastTextIndent = textIndent;
            lastCaretDelay = caretDelay;
        }
    }

    protected void renderComponent(GuiGraphics g) {
        g.drawString(font, getName(), getComponentX(), getComponentY(), getTextColor());

        int y = getComponentY() + font.lineHeight + 1;
        int height = Math.min(minBoxHeight, getComponentHeight() - font.lineHeight - 1);
        drawHollowRect(g, getComponentX() - boxBorder, y - boxBorder, getComponentWidth() + (boxBorder * 2), height + (boxBorder * 2), hasFocus ? getForegroundColor() : getBorderColor());
        drawRect(g, getComponentX(), y, getComponentWidth(), height, getBackgroundColor());

        String text = getValue() == null ? "" : getValue();

        //trim text if too long
        while (!text.isEmpty() && font.width(text + "_") > getComponentWidth() - textIndent)
            text = text.substring(1);

        if (hasFocus && caretVisible)
            text += "_";

        g.drawString(font, text, getComponentX() + textIndent, y + (height / 2) - (font.lineHeight / 2), getTextColor());
    }

    public void onMouseClick(double mouseX, double mouseY) {
        int x = getComponentX();
        int y = getComponentY() + font.lineHeight + 1;
        int width = x + getComponentWidth();
        int height = y + Math.min(minBoxHeight, getComponentHeight() - font.lineHeight - 1);

        //count border
        x -= boxBorder;
        y -= boxBorder;
        width += boxBorder;
        height += boxBorder;

        hasFocus = mouseX >= x && mouseX <= width && mouseY >= y && mouseY <= height;
    }

    public void onKeyPress(int keyCode, int scanCode, int modifiers) {
        if (!hasFocus) return;

        //TODO:event logic here
        System.out.println("Key pressed: " + keyCode);

        switch (keyCode) {
            case 259 -> { //backspace
                String value = getValue();
                if (value == null || value.isEmpty()) return;
                setValue(value.substring(0, value.length() - 1));
            }
        }
    }

    public void onKeyType(char typedChar, int keyCode) {
        if (!hasFocus) return;

        final String value = getValue();
        if (value != null && value.length() >= maxTextLength) return;

        String toAdd = SharedConstants.filterText(Character.toString(typedChar));
        if (toAdd.isEmpty()) return;
        setValue(value + toAdd);
    }
}
