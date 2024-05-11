package dev.JustRed23.fuie;

import net.minecraft.SharedConstants;
import net.minecraft.client.Minecraft;

public final class FUIECommon {

    public static void initialize() {
        FUIEConstants.LOGGER.info("{} version {} initializing...", FUIEConstants.NAME, SharedConstants.getCurrentVersion().getName() + "-" + FUIEConstants.BUILD);
        FUIEKeys.registerKeyCallback(FUIEKeys.keyOpenGui, new KeyCallback(KeyCallback.ActivationRequirement.IN_GAME,
                () -> Minecraft.getInstance().setScreen(new TestScreen())));
    }

    public static void onClientTick() {
        FUIEKeys.onClientTick();
    }
}
