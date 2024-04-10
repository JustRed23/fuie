package dev.JustRed23.fuie;

import dev.JustRed23.fuie.platform.LoaderPlatform;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLLoader;

public class ForgePlatform implements LoaderPlatform {

    public String platform() {
        return "MinecraftForge";
    }

    public boolean modLoaded(String modid) {
        return ModList.get().isLoaded(modid);
    }

    public boolean devEnvironment() {
        return !FMLLoader.isProduction();
    }
}
