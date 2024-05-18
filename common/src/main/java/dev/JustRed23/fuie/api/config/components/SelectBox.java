package dev.JustRed23.fuie.api.config.components;

import net.minecraft.client.gui.GuiGraphics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

public class SelectBox extends ConfigComponent<String> {

    public final int minBoxHeight = 13;

    public int textIndent = 2;
    public int choiceHeight = font.lineHeight + 2;

    private final int boxBorder = 1;
    private final List<String> choices;

    private boolean expanded = false;
    private String selectedChoice;

    public SelectBox(@NotNull String name, @Nullable String description, @NotNull List<String> choices, @NotNull String defaultValue) {
        super(name, description, defaultValue);

        if (choices.isEmpty()) throw new IllegalArgumentException("Choices list cannot be empty");
        if (!choices.contains(defaultValue)) throw new IllegalArgumentException("Default value is not in the list of choices");

        this.choices = choices;
        this.selectedChoice = defaultValue;

        setHeight(minBoxHeight + font.lineHeight + 2 + (availableChoices().size() * choiceHeight) + (boxBorder * 4));
    }

    public SelectBox(@NotNull String name, @Nullable String description, @NotNull Enum<?>[] choices, @NotNull Enum<?> defaultValue) {
        this(name, description, Arrays.stream(choices).map(Enum::toString).toList(), defaultValue.toString());
    }

    public SelectBox(@NotNull String name, @Nullable String description, @NotNull List<String> choices) {
        this(name, description, choices, choices.get(0));
    }

    protected void setHeight(int height) {
        int choicesSize = choices != null ? (availableChoices().size() * choiceHeight) : 0;
        int defaultHeight = minBoxHeight + font.lineHeight + 2 + choicesSize + (boxBorder * 4);
        if (height < defaultHeight) height = defaultHeight;
        super.setHeight(height);
    }

    protected void updateComponent() {}

    protected void renderComponent(GuiGraphics g) {
        g.drawString(font, getName(), getComponentX(), getComponentY(), getTextColor());

        int y = getComponentY() + font.lineHeight + 1;
        int height = Math.min(minBoxHeight, getComponentHeight() - font.lineHeight - 1);
        drawHollowRect(g, getComponentX() - boxBorder, y - boxBorder, getComponentWidth() + (boxBorder * 2), height + (boxBorder * 2), getBorderColor());
        drawRect(g, getComponentX(), y, getComponentWidth(), height, getBackgroundColor());

        int centeredTextY = y + (height / 2) - (font.lineHeight / 2);
        int arrowWidth = drawArrow(g, centeredTextY);

        String text = calculateDisplayString(getValue(), arrowWidth);
        g.drawString(font, text, getComponentX() + textIndent, centeredTextY, getTextColor());

        drawChoices(g, y + height);
    }

    private int drawArrow(GuiGraphics g, int y) {
        String currentArrow = expanded ? "▲" : "▼";
        int arrowWidth = font.width(currentArrow);
        g.drawString(font, currentArrow, getComponentX() + getComponentWidth() - arrowWidth - textIndent, y, getTextColor());
        return arrowWidth;
    }

    private String calculateDisplayString(String input, int arrowWidth) {
        int inputLength = input.length();
        int availableWidth = getComponentWidth() - arrowWidth - textIndent;

        if (font.width(input) <= availableWidth) return input;

        String subStr = "";
        for (int i = 0; i < inputLength; i++) {
            subStr = input.substring(0, i);
            if (font.width(subStr + "...") > availableWidth) break;
        }

        return subStr + "...";
    }

    private void drawChoices(GuiGraphics g, int y) {
        if (!expanded) return;

        int height = y + (availableChoices().size() * choiceHeight);

        drawHollowRect(g, getComponentX() - boxBorder, y, getComponentWidth() + (boxBorder * 2), height - y + (boxBorder * 2), getBorderColor());

        int choiceY = y + boxBorder;
        for (String choice : availableChoices()) {
            int backgroundColor = choice.equals(selectedChoice) ? getForegroundColor() : getBackgroundColor();

            drawRect(g, getComponentX(), choiceY, getComponentWidth(), choiceHeight, backgroundColor);
            g.drawString(font, choice, getComponentX() + textIndent, choiceY + 1, getTextColor());

            choiceY += choiceHeight;
        }
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

        if(mouseX >= x && mouseX <= width && mouseY >= y) {
            if(mouseY <= height)
                expanded = !expanded;

            if (expanded && mouseY > height) {
                int choiceY = height;
                for (String choice : availableChoices()) {
                    choiceY += choiceHeight;
                    if (mouseY < choiceY) {
                        setValue(choice);
                        expanded = false;
                        break;
                    }
                }
            }
        }
    }

    public void onMouseMove(double mouseX, double mouseY) {
        if (!expanded) return;

        int x = getComponentX();
        int y = getComponentY() + font.lineHeight + 1;
        int width = x + getComponentWidth();
        int height = y + Math.min(minBoxHeight, getComponentHeight() - font.lineHeight - 1);

        //count border
        x -= boxBorder;
        y -= boxBorder;
        width += boxBorder;
        height += boxBorder;

        //update y and height
        y += height - y + boxBorder;
        height = y + (availableChoices().size() * choiceHeight);

        if (mouseX >= x && mouseX <= width && mouseY >= y && mouseY <= height) {
            int choiceY = y;
            for (String choice : availableChoices()) {
                choiceY += choiceHeight;
                if (mouseY < choiceY) {
                    selectedChoice = choice;
                    break;
                }
            }
        } else selectedChoice = "";
    }

    public void onKeyPress(int keyCode, int scanCode, int modifiers) {
        System.out.println("Key Pressed: " + keyCode);
        //TODO: arrow key navigation
    }

    public void onMouseScroll(double mouseX, double mouseY, double scroll) {
        //TODO: scroll through choices
    }

    private List<String> availableChoices() {
        return choices.stream()
                .filter(choice -> !choice.equals(getValue()))
                .toList();
    }
}
