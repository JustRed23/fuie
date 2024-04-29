package dev.JustRed23.fuie;

import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLLoader;

@SuppressWarnings("unused")
@Mod(FUIEConstants.MODID)
public class FUIEForge {

    public FUIEForge() {
        if (FMLLoader.getDist().isDedicatedServer())
            return;

        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        bus.addListener(this::onKeyRegister);
        bus.addListener(this::onClientSetup);

        MinecraftForge.EVENT_BUS.register(this);
    }

    public void onKeyRegister(RegisterKeyMappingsEvent event) {
        FUIEKeys.getKeys().forEach((keycode, key) -> event.register(key));
    }

    public void onClientSetup(FMLClientSetupEvent event) {
        FUIECommon.initialize();
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END)
            FUIECommon.onClientTick();
    }
}
