package dev.JustRed23.fuie;

import com.google.common.collect.ImmutableMap;
import dev.JustRed23.fuie.platform.LoaderPlatform;
import net.minecraft.client.KeyMapping;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unused")
public final class FUIEKeys {

    public static int keyOpenGui = GLFW.GLFW_KEY_O;
    public static int keyDebug = GLFW.GLFW_KEY_F4;

    private static final Map<Integer, KeyMapping> keys = new HashMap<>();
    private static final Map<Integer, KeyCallback> keyCallbacks = new HashMap<>();

    public static void registerKeyCallback(int keyCode, KeyCallback callback) {
        keyCallbacks.put(keyCode, callback);
    }

    public static @Nullable KeyMapping getKey(int keyCode) {
        return keys.get(keyCode);
    }

    public static Map<Integer, KeyMapping> getKeys() {
        if (keys.isEmpty()) init();
        return ImmutableMap.copyOf(keys);
    }

    private static void init() {
        String category = "key.categories." + FUIEConstants.MODID;

        for (Field field : FUIEKeys.class.getDeclaredFields()) {
            if (field.getType() == int.class) {
                try {
                    int keyCode = field.getInt(null);

                    if (!LoaderPlatform.INSTANCE.devEnvironment() && field.getName().equals("keyDebug"))
                        continue;

                    KeyMapping key = new KeyMapping("key." + FUIEConstants.MODID + "." + field.getName(), keyCode, category);
                    keys.put(keyCode, key);
                    FUIEConstants.LOGGER.debug("Registered key: {}", key.getName());
                } catch (IllegalAccessException e) {
                    FUIEConstants.LOGGER.error("Failed to register key: {}", field.getName(), e);
                }
            }
        }
    }

    static void onClientTick() {
        keys.forEach((keyCode, key) -> {
            while (key.consumeClick()) {
                KeyCallback callback = keyCallbacks.get(keyCode);
                if (callback != null) callback.run();
            }
        });
    }
}
