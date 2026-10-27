// Titanium Lite String Util

package com.hrznstudio.titanium.util;

import net.minecraft.util.Mth;

public class StringUtil {
    public static String typingAnimation(String message, int time, int maxTime) {
        float percent = ((float) time / (float) maxTime);
        int messageCount = message.length();
        return message.substring(0, Mth.clamp(Math.round(messageCount * percent), 0, messageCount));
    }
}
