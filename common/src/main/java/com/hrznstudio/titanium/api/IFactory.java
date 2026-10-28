// Titanium Lite I Factory

package com.hrznstudio.titanium.api;

import javax.annotation.Nonnull;

public interface IFactory<T> {
    @Nonnull
    T create();
}
