package dev.JustRed23.fuie.api.config.components;

import net.minecraft.client.gui.GuiGraphics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

public class SelectBox extends ConfigComponent<String> {

    public final int minBoxHeight = 12;

    public int textIndent = 2;
    public int choiceHeight = font.lineHeight + 2;

    private final List<String> choices;

    private boolean expanded = false;
    private String selectedChoice;

    public SelectBox(@NotNull String name, @Nullable String description, @NotNull List<String> choices, @NotNull String defaultValue) {
        super(name, description, defaultValue);

        if (choices.isEmpty()) throw new IllegalArgumentException("Choices list cannot be empty");
        if (!choices.contains(defaultValue)) throw new IllegalArgumentException("Default value is not in the list of choices");

        this.choices = choices;
        this.selectedChoice = defaultValue;

        setHeight(minBoxHeight + font.lineHeight + 5 + (availableChoices().size() * choiceHeight));
    }

    public SelectBox(@NotNull String name, @Nullable String description, @NotNull Enum<?>[] choices, @NotNull Enum<?> defaultValue) {
        this(name, description, Arrays.stream(choices).map(Enum::toString).toList(), defaultValue.toString());
    }

    public SelectBox(@NotNull String name, @Nullable String description, @NotNull List<String> choices) {
        this(name, description, choices, choices.get(0));
    }

    protected void setHeight(int height) {
        int choicesSize = choices != null ? (availableChoices().size() * choiceHeight) : 0;
        int defaultHeight = minBoxHeight + font.lineHeight + 5 + choicesSize;
        if (height < defaultHeight) height = defaultHeight;
        super.setHeight(height);
    }

    protected void updateComponent() {}

    protected void renderComponent(GuiGraphics g) {
        g.drawString(font, getName(), getComponentX(), getComponentY(), getTextColor());

        int y = getComponentY() + font.lineHeight + 1;
        int height = Math.min(minBoxHeight, getComponentHeight() - font.lineHeight - 1);
        drawRect(g, getComponentX(), y, getComponentWidth(), height, getBackgroundColor());
        drawHollowRect(g, getComponentX(), y, getComponentWidth(), height, getBorderColor());

        int centeredTextY = y + (height / 2) - (font.lineHeight / 2);
        int arrowWidth = drawArrow(g, centeredTextY);

        g.drawString(font, calculateDisplayString(getValue(), arrowWidth), getComponentX() + textIndent, centeredTextY, getTextColor());

        drawChoices(g, y + height - 1);
    }

    private int drawArrow(GuiGraphics g, int y) {
        String currentArrow = expanded ? "▲" : "▼";
        int arrowWidth = font.width(currentArrow);
        g.drawString(font, currentArrow, getComponentX() + getComponentWidth() - arrowWidth - textIndent, y, getTextColor());
        return arrowWidth * 2;
    }

    private String calculateDisplayString(String input, int padding) {
        int inputLength = input.length();
        int availableWidth = getComponentWidth() - padding - textIndent;

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

        int choiceY = y + 1;
        for (String choice : availableChoices()) {
            int backgroundColor = choice.equals(selectedChoice) ? getForegroundColor() : getBackgroundColor();

            drawRect(g, getComponentX(), choiceY, getComponentWidth(), choiceHeight, backgroundColor);
            g.drawString(font, calculateDisplayString(choice, textIndent * 3), getComponentX() + textIndent, choiceY + 1, getTextColor());

            choiceY += choiceHeight;
        }

        int height = y + (availableChoices().size() * choiceHeight) + 2;
        drawHollowRect(g, getComponentX(), y, getComponentWidth(), height - y, getBorderColor());
    }

    public void onMouseClick(double mouseX, double mouseY) {
        int x = getComponentX();
        int y = getComponentY() + font.lineHeight + 1;
        int width = x + getComponentWidth();
        int height = y + Math.min(minBoxHeight, getComponentHeight() - font.lineHeight - 1);

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

        //update y and height
        y += height - y;
        height = y + (availableChoices().size() * choiceHeight);

        if (mouseX >= x && mouseX <= width && mouseY >= y && mouseY <= height) {
            int choiceY = y - 1;
            for (String choice : availableChoices()) {
                choiceY += choiceHeight;
                if (mouseY < choiceY) {
                    selectedChoice = choice;
                    break;
                }
            }
        } else selectedChoice = "";
    }

    private List<String> availableChoices() {
        return choices.stream()
                .filter(choice -> !choice.equals(getValue()))
                .toList();
    }
}
