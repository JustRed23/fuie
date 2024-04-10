package dev.JustRed23.fuie;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;

public class FUIEFabric implements ClientModInitializer {

    public void onInitializeClient() {
        FUIECommon.initialize();
        FUIEKeys.getKeys().forEach((keyCode, key) -> KeyBindingHelper.registerKeyBinding(key));
        ClientTickEvents.END_CLIENT_TICK.register($ -> FUIECommon.onClientTick());
    }
}
