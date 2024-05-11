package dev.JustRed23.fuie.api.config.components;

public record ComponentPadding(int top, int right, int bottom, int left) {
    public static final ComponentPadding DEFAULT_PADDING = new ComponentPadding(1);

    public ComponentPadding(int top, int right, int bottom, int left) {
        this.top = validatePadding(top, "Top padding");
        this.right = validatePadding(right, "Right padding");
        this.bottom = validatePadding(bottom, "Bottom padding");
        this.left = validatePadding(left, "Left padding");
    }

    private int validatePadding(int padding, String name) {
        if (padding < 0)
            throw new IllegalArgumentException(name + " cannot be negative");
        return padding;
    }

    public ComponentPadding(int top, int right, int bottom) {
        this(top, right, bottom, right);
    }

    public ComponentPadding(int horizontal, int vertical) {
        this(vertical, horizontal, vertical, horizontal);
    }

    public ComponentPadding(int padding) {
        this(padding, padding, padding, padding);
    }

    public int leftRight() {
        return left + right;
    }

    public int topBottom() {
        return top + bottom;
    }

    public boolean paddingExists() {
        return top != 0 || right != 0 || bottom != 0 || left != 0;
    }
}
