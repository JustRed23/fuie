package dev.JustRed23.fuie;

import net.minecraft.SharedConstants;

public final class FUIECommon {

    public static void initialize() {
        FUIEConstants.LOGGER.info("{} version {} initializing...", FUIEConstants.NAME, SharedConstants.getCurrentVersion().getName() + "-" + FUIEConstants.BUILD);
    }
}
