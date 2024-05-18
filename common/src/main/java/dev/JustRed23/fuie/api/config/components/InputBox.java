package dev.JustRed23.fuie.api.config.components;

import net.minecraft.SharedConstants;
import net.minecraft.Util;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class InputBox extends ConfigComponent<String> {

    public final int minBoxHeight = 12;

    public int boxBorder = 1;
    public int textIndent = 2;
    public int cursorDelay = 10;

    private final int maxTextLength;

    private boolean hasFocus = false;

    private int cursorCounter = 0;
    private boolean cursorVisible = false;

    private int displayPosition;
    private int cursorPosition;

    public InputBox(@NotNull String name, @Nullable String description, @Nullable String defaultValue, int maxTextLength) {
        super(name, description, defaultValue);

        if (maxTextLength < 1) throw new IllegalArgumentException("Max text length must be at least 1");
        if (defaultValue != null && defaultValue.length() > maxTextLength) throw new IllegalArgumentException("Default value is longer than max text length");

        this.maxTextLength = maxTextLength;
        this.cursorPosition = defaultValue == null ? 0 : defaultValue.length();

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
            cursorCounter++;
            if (cursorCounter >= cursorDelay) {
                cursorCounter = 0;
                cursorVisible = !cursorVisible;
            }
        }
    }

    protected void renderComponent(GuiGraphics g) {
        g.drawString(font, getName(), getComponentX(), getComponentY(), getTextColor());

        int y = getComponentY() + font.lineHeight + 1;
        int height = Math.min(minBoxHeight, getComponentHeight() - font.lineHeight - 1);
        drawHollowRect(g, getComponentX() - boxBorder, y - boxBorder, getComponentWidth() + (boxBorder * 2), height + (boxBorder * 2), hasFocus ? getForegroundColor() : getBorderColor());
        drawRect(g, getComponentX(), y, getComponentWidth(), height, getBackgroundColor());

        String text = getValue() == null ? "" : getValue();
        text = calculateDisplayText(text);

        drawCursor(g, text, y, height);

        g.drawString(font, text, getComponentX() + textIndent, y + (height / 2) - (font.lineHeight / 2), getTextColor());
    }

    private String calculateDisplayText(String input) {
        int inputLength = input.length();
        int availableWidth = getComponentWidth() - textIndent - 1; //1 for the cursor

        if (font.width(input) <= availableWidth) {
            displayPosition = 0;
            return input;
        }

        String maxChars = font.plainSubstrByWidth(input, availableWidth, true);
        int displayableChars = maxChars.length();

        if (cursorPosition < displayPosition || cursorPosition >= displayPosition + displayableChars)
            displayPosition = Math.max(0, cursorPosition - displayableChars);

        return input.substring(displayPosition, Math.min(inputLength, displayPosition + displayableChars));
    }

    private void drawCursor(GuiGraphics g, String text, int y, int height) {
        if (!hasFocus || !cursorVisible) return;

        int position = Mth.clamp(cursorPosition - displayPosition, 0, text.length());
        int cursorX = getComponentX() + textIndent + font.width(text.substring(0, position));
        int cursorY = y + (height / 2) - (font.lineHeight / 2);
        drawRect(g, cursorX, cursorY, 1, font.lineHeight, getTextColor());
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

        switch (keyCode) {
            case 259 -> { //backspace
                String value = getValue();
                if (value == null || value.isEmpty()) return;
                if (cursorPosition == 0) return;
                setValue(value.substring(0, cursorPosition - 1) + value.substring(cursorPosition));
                this.moveCursor(-1);
            }
            case 261 -> { //delete
                String value = getValue();
                if (value == null || value.isEmpty()) return;
                if (cursorPosition == value.length()) return;
                setValue(value.substring(0, cursorPosition) + value.substring(cursorPosition + 1));
            }
            case 263 -> //left arrow
                    this.moveCursor(-1);
            case 262 -> //right arrow
                    this.moveCursor(1);
        }
    }

    private void moveCursor(int amount) {
        this.moveCursorTo(getCursorPos(amount));
    }

    private void moveCursorTo(int position) {
        int prevPosition = cursorPosition;
        this.cursorPosition = Mth.clamp(position, 0, getValue() == null ? 0 : getValue().length());
        if (prevPosition != cursorPosition) {
            cursorCounter = 0;
            cursorVisible = true;
        }
    }

    private int getCursorPos(int amount) {
        String value = getValue() == null ? "" : getValue();
        if (cursorPosition > value.length()) return value.length();

        return Util.offsetByCodepoints(value, this.cursorPosition, amount);
    }

    public void onKeyType(char typedChar, int keyCode) {
        if (!hasFocus) return;

        final String value = getValue();
        if (value != null && value.length() >= maxTextLength) return;

        String toAdd = SharedConstants.filterText(Character.toString(typedChar));
        if (toAdd.isEmpty()) return;
        setValue(value + toAdd);
        cursorPosition += toAdd.length();
    }
}
