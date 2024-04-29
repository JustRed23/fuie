package dev.JustRed23.fuie;

import net.minecraft.client.Minecraft;

public class KeyCallback {

    public enum ActivationRequirement {
        IN_GAME, SCREEN, ALWAYS
    }

    private final ActivationRequirement activationRequirement;
    private final Runnable callback;

    public KeyCallback(ActivationRequirement activationRequirement, Runnable callback) {
        this.activationRequirement = activationRequirement;
        this.callback = callback;
    }

    public KeyCallback(Runnable callback) {
        this(ActivationRequirement.ALWAYS, callback);
    }

    void run() {
        switch (activationRequirement) {
            case IN_GAME:
                if (Minecraft.getInstance().screen == null)
                    callback.run();
                break;
            case SCREEN:
                if (Minecraft.getInstance().screen != null)
                    callback.run();
                break;
            case ALWAYS:
                callback.run();
                break;
        }
    }
}
