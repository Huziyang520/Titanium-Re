// Service locator installed by the loader entry point

package com.hrznstudio.titanium.platform;

public final class Platform {

    private static volatile IPlatform instance;

    private Platform() {
    }

    public static void install(IPlatform platform) {
        if (instance != null) {
            throw new IllegalStateException("platform already installed: " + instance.name());
        }
        instance = platform;
    }

    public static IPlatform get() {
        IPlatform platform = instance;
        if (platform == null) {
            throw new IllegalStateException("no platform installed");
        }
        return platform;
    }

    public static IRegistryHelper registries() {
        return get().registries();
    }

    public static IClassScanner scanner() {
        return get().scanner();
    }
}
