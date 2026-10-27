// Service locator for the loader store provider

package com.hrznstudio.titanium.transfer;

public final class Stores {

    private static volatile IStoreProvider provider;

    private Stores() {
    }

    public static void install(IStoreProvider storeProvider) {
        if (provider != null) {
            throw new IllegalStateException("store provider already installed");
        }
        provider = storeProvider;
    }

    public static IStoreProvider get() {
        IStoreProvider current = provider;
        if (current == null) {
            throw new IllegalStateException("no store provider installed");
        }
        return current;
    }
}
