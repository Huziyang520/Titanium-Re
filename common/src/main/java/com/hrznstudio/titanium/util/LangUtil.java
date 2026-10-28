// Titanium Lite Lang Util

package com.hrznstudio.titanium.util;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public class LangUtil {

    public static MutableComponent get(String string, Object... args) {
        return Component.translatable(string, args);
    }

    public static String getString(String string, Object... args) {
        return Component.translatable(string, args).getString();
    }

}
