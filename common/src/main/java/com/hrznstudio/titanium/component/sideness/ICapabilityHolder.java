// Titanium Lite I Capability Holder

package com.hrznstudio.titanium.component.sideness;

import com.hrznstudio.titanium.util.FacingUtil;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

public interface ICapabilityHolder<T> {

    @Nonnull
    Optional<T> getCapabilityForSide(@Nullable FacingUtil.Sideness sideness);

    boolean handleFacingChange(String handlerName, FacingUtil.Sideness facing, int mode);
}
