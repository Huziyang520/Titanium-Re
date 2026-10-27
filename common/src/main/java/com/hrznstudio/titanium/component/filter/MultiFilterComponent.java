// Titanium Lite Multi Filter Component

package com.hrznstudio.titanium.component.filter;

import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.api.client.IScreenAddonProvider;
import com.hrznstudio.titanium.api.filter.IFilter;
import com.hrznstudio.titanium.component.IComponentHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MultiFilterComponent implements IScreenAddonProvider, IComponentHandler {
    public final List<IFilter> filters;

    public MultiFilterComponent() {
        filters = new ArrayList<>();
    }

    public void add(Object... components) {
        Arrays.stream(components).filter(this::accepts).forEach(filter -> this.filters.add((IFilter) filter));
    }

    public List<IFilter> getFilters() {
        return filters;
    }

    @Override
    public List<IFactory<?>> getScreenAddons() {
        List<IFactory<?>> addons = new ArrayList<>();
        filters.forEach(filter -> addons.addAll(filter.getScreenAddons()));
        return addons;
    }

    private boolean accepts(Object component) {
        return component instanceof IFilter;
    }
}
