package dev.JustRed23.fuie;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;

public class FUIEFabric implements ClientModInitializer {

    public void onInitializeClient() {
        //Key registering
        FUIEKeys.getKeys().forEach((keyCode, key) -> KeyBindingHelper.registerKeyBinding(key));

        //Init common
        FUIECommon.initialize();

        //Client tick event
        ClientTickEvents.END_CLIENT_TICK.register($ -> FUIECommon.onClientTick());
    }
}
