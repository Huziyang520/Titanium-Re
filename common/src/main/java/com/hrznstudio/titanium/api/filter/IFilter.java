// Titanium Lite I Filter

package com.hrznstudio.titanium.api.filter;

import com.hrznstudio.titanium.api.client.IScreenAddonProvider;
import com.hrznstudio.titanium.nbthandler.INBTSerializable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

import java.util.function.Predicate;

public interface IFilter<T> extends INBTSerializable<CompoundTag>, IScreenAddonProvider {
    String getName();

    boolean acceptsAsFilter(ItemStack filter);

    void setFilter(int slot, ItemStack stack);

    void setFilter(int slot, FilterSlot<T> filterSlot);

    FilterSlot<T>[] getFilterSlots();

    Type getType();

    FilterAction<T> getAction();

    void toggleFilterMode();

    void selectNextFilter();

    default boolean matches(T object) {
        return getType().getFilter().test(getAction().getFilterCheck().test(this, object));
    }

    enum Type {
        WHITELIST(filter -> filter),
        BLACKLIST(filter -> !filter);

        private final Predicate<Boolean> filter;

        Type(Predicate<Boolean> filter) {
            this.filter = filter;
        }

        public Predicate<Boolean> getFilter() {
            return filter;
        }
    }

}
