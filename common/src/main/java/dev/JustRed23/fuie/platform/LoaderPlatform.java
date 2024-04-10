package dev.JustRed23.fuie.platform;

public interface LoaderPlatform {
    LoaderPlatform INSTANCE = PlatformImpl.loadService();

    String platform();

    boolean modLoaded(String modid);

    boolean devEnvironment();
}
