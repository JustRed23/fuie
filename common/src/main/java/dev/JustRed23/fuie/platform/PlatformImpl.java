package dev.JustRed23.fuie.platform;

import java.util.ServiceLoader;

class PlatformImpl {

    static LoaderPlatform loadService() {
        return ServiceLoader.load(LoaderPlatform.class)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No LoaderPlatform implementation found"));
    }
}
