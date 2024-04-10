package dev.JustRed23.fuie;

import dev.JustRed23.fuie.platform.LoaderPlatform;
import net.fabricmc.loader.api.FabricLoader;

public class FabricPlatform implements LoaderPlatform {

    public String platform() {
        return "Fabric";
    }

    public boolean modLoaded(String modid) {
        return FabricLoader.getInstance().isModLoaded(modid);
    }

    public boolean devEnvironment() {
        return FabricLoader.getInstance().isDevelopmentEnvironment();
    }
}
