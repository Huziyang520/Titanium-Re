// Titanium Lite Titanium Config

package com.hrznstudio.titanium._impl.test;

import com.hrznstudio.titanium.annotation.config.ConfigFile;
import com.hrznstudio.titanium.annotation.config.ConfigVal;
import net.neoforged.fml.config.ModConfig;

@ConfigFile(type = ModConfig.Type.STARTUP)
public class TitaniumConfig {

    @ConfigVal(comment = "A Boolean that is true by default")
    public static boolean thisIsABoolean = true;
    @ConfigVal(comment = "A Boolean that is false by default", value = "NOT_BOOLEAN")
    public static boolean thisIsNotABoolean = false;
    @ConfigVal
    public static int intAngery = 7;
    @ConfigVal
    public static Dabber dabber = new Dabber();

    public static class Dabber {

        @ConfigVal
        public static String dabby = "lil dab";

    }
}
