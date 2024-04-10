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
    public static int keyDebug = GLFW.GLFW_KEY_F3;

    private static final Map<Integer, KeyMapping> keys = new HashMap<>();

    public static @Nullable KeyMapping getKey(int keyCode) {
        return keys.get(keyCode);
    }

    public static Map<Integer, KeyMapping> getKeys() {
        return ImmutableMap.copyOf(keys);
    }

    static void init() {
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
        keys.forEach(($, key) -> {
            while (key.consumeClick()) {
                FUIEConstants.LOGGER.debug("Key {} was pressed", key.getName());
            }
        });
    }
}
