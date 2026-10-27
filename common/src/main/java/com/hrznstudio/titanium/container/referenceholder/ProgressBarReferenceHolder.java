// Titanium Lite Progress Bar Reference Holder

package com.hrznstudio.titanium.container.referenceholder;

import com.hrznstudio.titanium.component.progress.ProgressBarComponent;
import net.minecraft.world.inventory.ContainerData;

public class ProgressBarReferenceHolder implements ContainerData {
    private final ProgressBarComponent<?> progressBarComponent;

    public ProgressBarReferenceHolder(ProgressBarComponent<?> progressBarComponent) {
        this.progressBarComponent = progressBarComponent;
    }

    @Override
    public int get(int index) {
        if (index == 0) {
            return progressBarComponent.getProgress();
        } else if (index == 1) {
            return progressBarComponent.getMaxProgress();
        } else {
            return progressBarComponent.getProgressIncrease();
        }
    }

    @Override
    public void set(int index, int value) {
        if (index == 0) {
            progressBarComponent.setProgress(value);
        } else if (index == 1) {
            progressBarComponent.setMaxProgress(value);
        } else {
            progressBarComponent.setProgressIncrease(value);
        }
    }

    @Override
    public int getCount() {
        return 3;
    }
}
