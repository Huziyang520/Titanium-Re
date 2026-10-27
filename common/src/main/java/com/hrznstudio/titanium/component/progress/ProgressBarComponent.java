// Titanium Lite Progress Bar Component

package com.hrznstudio.titanium.component.progress;

import com.google.common.collect.Lists;
import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.api.client.AssetTypes;
import com.hrznstudio.titanium.api.client.IAsset;
import com.hrznstudio.titanium.api.client.IScreenAddonProvider;
import com.hrznstudio.titanium.client.screen.addon.ProgressBarScreenAddon;
import com.hrznstudio.titanium.client.screen.asset.IAssetProvider;
import com.hrznstudio.titanium.component.IComponentHarness;
import com.hrznstudio.titanium.container.addon.IContainerAddon;
import com.hrznstudio.titanium.container.addon.IContainerAddonProvider;
import com.hrznstudio.titanium.container.addon.IntArrayReferenceHolderAddon;
import com.hrznstudio.titanium.container.referenceholder.ProgressBarReferenceHolder;
import com.hrznstudio.titanium.nbthandler.INBTSerializable;
import com.hrznstudio.titanium.util.AssetUtil;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.DyeColor;

import java.awt.*;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

public class ProgressBarComponent<T extends IComponentHarness> implements INBTSerializable<CompoundTag>,
        IScreenAddonProvider, IContainerAddonProvider {
    private int posX;
    private int posY;
    private int progress;
    private int maxProgress;
    private int progressIncrease;
    private Predicate<T> canIncrease;
    private Predicate<T> canReset;
    private int tickingTime;
    private Runnable onFinishWork;
    private Runnable onTickWork;
    private Runnable onStart;
    private T componentHarness;
    private BarDirection barDirection;
    private DyeColor color;
    private boolean increaseType;

    public ProgressBarComponent(int posX, int posY, int maxProgress) {
        this.posX = posX;
        this.posY = posY;
        this.progress = 0;
        this.maxProgress = maxProgress;
        this.progressIncrease = 1;
        this.canIncrease = tileEntity -> false;
        this.canReset = tileEntity -> true;
        this.tickingTime = 1;
        this.onFinishWork = () -> {
        };
        this.onTickWork = () -> {
        };
        this.onStart = () -> {
        };
        this.barDirection = BarDirection.VERTICAL_UP;
        this.color = DyeColor.WHITE;
        this.increaseType = true;
    }

    public ProgressBarComponent(int posX, int posY, int progress, int maxProgress) {
        this(posX, posY, maxProgress);
        this.progress = progress;
    }

    public ProgressBarComponent<T> setOnFinishWork(Runnable runnable) {
        this.onFinishWork = runnable;
        return this;
    }

    public ProgressBarComponent<T> setOnTickWork(Runnable runnable) {
        this.onTickWork = runnable;
        return this;
    }

    public ProgressBarComponent<T> setOnStart(Runnable runnable) {
        this.onStart = runnable;
        return this;
    }

    public ProgressBarComponent<T> setComponentHarness(T componentHarness) {
        this.componentHarness = componentHarness;
        return this;
    }

    public T getComponentHarness() {
        return componentHarness;
    }

    public Predicate<T> getCanReset() {
        return canReset;
    }

    public ProgressBarComponent<T> setCanReset(Predicate<T> canReset) {
        this.canReset = canReset;
        return this;
    }

    public boolean getIncreaseType() {
        return increaseType;
    }

    public ProgressBarComponent<T> setIncreaseType(boolean increaseType) {
        this.increaseType = increaseType;
        return this;
    }

    public void tickBar() {
        if (componentHarness != null && componentHarness.getComponentWorld().getGameTime() % tickingTime == 0) {
            if (increaseType && progress < maxProgress) {
                setProgress(this.progress + progressIncrease);
                this.onTickWork.run();
            }
            if (!increaseType && progress > 0) {
                setProgress(this.progress - progressIncrease);
                this.onTickWork.run();
            }
        }
        if (increaseType && progress >= maxProgress && canReset.test(componentHarness)) {
            setProgress(0);
            this.onFinishWork.run();
        }
        if (!increaseType && progress <= 0 && canReset.test(componentHarness)) {
            setProgress(maxProgress);
            this.onFinishWork.run();
        }
    }

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }

    public Predicate<T> getCanIncrease() {
        return canIncrease;
    }

    public ProgressBarComponent<T> setCanIncrease(Predicate<T> canIncrease) {
        this.canIncrease = canIncrease;
        return this;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
        if (componentHarness != null) componentHarness.markComponentForUpdate(true);
    }

    public int getMaxProgress() {
        return maxProgress;
    }

    public ProgressBarComponent<T> setMaxProgress(int maxProgress) {
        this.maxProgress = maxProgress;
        return this;
    }

    public int getTickingTime() {
        return tickingTime;
    }

    public ProgressBarComponent<T> setTickingTime(int tickingTime) {
        this.tickingTime = tickingTime;
        return this;
    }

    public int getProgressIncrease() {
        return progressIncrease;
    }

    public ProgressBarComponent<T> setProgressIncrease(int progressIncrease) {
        this.progressIncrease = progressIncrease;
        return this;
    }

    public BarDirection getBarDirection() {
        return barDirection;
    }

    public ProgressBarComponent<T> setBarDirection(BarDirection direction) {
        this.barDirection = direction;
        return this;
    }

    public DyeColor getColor() {
        return color;
    }

    public ProgressBarComponent<T> setColor(DyeColor color) {
        this.color = color;
        return this;
    }

    @Override
    public List<IFactory<?>> getScreenAddons() {
        return Collections.singletonList(() -> new ProgressBarScreenAddon<>(posX, posY, this));
    }

    @Override
    public CompoundTag serializeNBT(HolderLookup.Provider provider) {
        CompoundTag compound = new CompoundTag();
        compound.putInt("Tick", progress);
        compound.putInt("MaxProgress", maxProgress);
        return compound;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag nbt) {
        progress = nbt.getIntOr("Tick", 0);
        maxProgress = nbt.getIntOr("MaxProgress", 0);
    }

    public void onStart() {
        onStart.run();
    }

    @Override
    public List<IFactory<? extends IContainerAddon>> getContainerAddons() {
        return Lists.newArrayList(
                () -> new IntArrayReferenceHolderAddon(new ProgressBarReferenceHolder(this))
        );
    }

    public enum BarDirection {
        VERTICAL_UP {
            @Override
            public <T extends IComponentHarness> void render(GuiGraphicsExtractor guiGraphics, Screen screen, int guiX, int guiY, IAssetProvider provider, ProgressBarScreenAddon<T> addon) {
                IAsset assetBorder = IAssetProvider.getAsset(provider, AssetTypes.PROGRESS_BAR_BORDER_VERTICAL);
                Point offset = assetBorder.getOffset();
                Rectangle area = assetBorder.getArea();
                com.hrznstudio.titanium.util.AssetUtil.blit(guiGraphics, assetBorder.getIdentifier(), guiX + addon.getPosX() + offset.x, guiY + addon.getPosY() + offset.y, area.x, area.y, area.width, area.height);
                var colors = getTextureDiffuseColors(addon.getProgressBar().getColor());
                int tint = AssetUtil.color(colors[0], colors[1], colors[2], 1);
                IAsset assetBar = IAssetProvider.getAsset(provider, AssetTypes.PROGRESS_BAR_BACKGROUND_VERTICAL);
                offset = assetBar.getOffset();
                area = assetBar.getArea();
                com.hrznstudio.titanium.util.AssetUtil.blit(guiGraphics, assetBar.getIdentifier(), guiX + addon.getPosX() + offset.x, guiY + addon.getPosY() + offset.y, area.x, area.y, area.width, area.height, tint);
                IAsset asset = IAssetProvider.getAsset(provider, AssetTypes.PROGRESS_BAR_VERTICAL);
                offset = asset.getOffset();
                area = asset.getArea();
                int progress = addon.getProgressBar().getProgress();
                int maxProgress = addon.getProgressBar().getMaxProgress();
                int progressOffset = progress * area.height / Math.max(maxProgress, 1);
                com.hrznstudio.titanium.util.AssetUtil.blit(guiGraphics, asset.getIdentifier(), addon.getPosX() + offset.x + guiX,
                    addon.getPosY() + offset.y + area.height - progressOffset + guiY,
                    area.x,
                    area.y + (area.height - progressOffset),
                    area.width,
                    progressOffset, tint);
            }

            @Override
            public int getXSize(IAssetProvider provider) {
                return (int) IAssetProvider.getAsset(provider, AssetTypes.PROGRESS_BAR_BORDER_VERTICAL).getArea().getWidth();
            }

            @Override
            public int getYSize(IAssetProvider provider) {
                return (int) IAssetProvider.getAsset(provider, AssetTypes.PROGRESS_BAR_BORDER_VERTICAL).getArea().getHeight();
            }
        },
        ARROW_RIGHT {
            @Override
            public <T extends IComponentHarness> void render(GuiGraphicsExtractor guiGraphics, Screen screen, int guiX, int guiY, IAssetProvider provider, ProgressBarScreenAddon<T> addon) {
                AssetUtil.drawAsset(guiGraphics, screen, IAssetProvider.getAsset(provider, AssetTypes.PROGRESS_BAR_BACKGROUND_ARROW_HORIZONTAL), addon.getPosX() + guiX, addon.getPosY() + guiY);
                IAsset asset = IAssetProvider.getAsset(provider, AssetTypes.PROGRESS_BAR_ARROW_HORIZONTAL);
                Point offset = asset.getOffset();
                Rectangle area = asset.getArea();
                int progress = addon.getProgressBar().getProgress();
                int maxProgress = addon.getProgressBar().getMaxProgress();
                int progressOffset = progress * area.width / Math.max(maxProgress, 1);
                var colors = getTextureDiffuseColors(addon.getProgressBar().getColor());
                int tint = AssetUtil.color(colors[0], colors[1], colors[2], 1);
                com.hrznstudio.titanium.util.AssetUtil.blit(guiGraphics, asset.getIdentifier(), addon.getPosX() + offset.x + guiX, addon.getPosY() + offset.y + guiY, area.x, area.y, progressOffset, area.height, tint);
            }

            @Override
            public int getXSize(IAssetProvider provider) {
                return (int) IAssetProvider.getAsset(provider, AssetTypes.PROGRESS_BAR_BACKGROUND_ARROW_HORIZONTAL).getArea().getWidth();
            }

            @Override
            public int getYSize(IAssetProvider provider) {
                return (int) IAssetProvider.getAsset(provider, AssetTypes.PROGRESS_BAR_BACKGROUND_ARROW_HORIZONTAL).getArea().getHeight();
            }
        },

        ARROW_DOWN {
            @Override
            public <T extends IComponentHarness> void render(GuiGraphicsExtractor guiGraphics, Screen screen, int guiX, int guiY, IAssetProvider provider, ProgressBarScreenAddon<T> addon) {
                AssetUtil.drawAsset(guiGraphics, screen, IAssetProvider.getAsset(provider, AssetTypes.PROGRESS_BAR_BACKGROUND_ARROW_DOWN), addon.getPosX() + guiX, addon.getPosY() + guiY);
                IAsset asset = IAssetProvider.getAsset(provider, AssetTypes.PROGRESS_BAR_ARROW_DOWN);
                Point offset = asset.getOffset();
                Rectangle area = asset.getArea();
                int progress = addon.getProgressBar().getProgress();
                int maxProgress = addon.getProgressBar().getMaxProgress();
                int progressOffset = progress * area.height / Math.max(maxProgress, 1);
                var colors = getTextureDiffuseColors(addon.getProgressBar().getColor());
                int tint = AssetUtil.color(colors[0], colors[1], colors[2], 1);
                com.hrznstudio.titanium.util.AssetUtil.blit(guiGraphics, asset.getIdentifier(), addon.getPosX() + offset.x + guiX, addon.getPosY() + offset.y + guiY, area.x, area.y, area.width, progressOffset, tint);
            }

            @Override
            public int getXSize(IAssetProvider provider) {
                return (int) IAssetProvider.getAsset(provider, AssetTypes.PROGRESS_BAR_BACKGROUND_ARROW_DOWN).getArea().getWidth();
            }

            @Override
            public int getYSize(IAssetProvider provider) {
                return (int) IAssetProvider.getAsset(provider, AssetTypes.PROGRESS_BAR_BACKGROUND_ARROW_DOWN).getArea().getHeight();
            }
        };

        public abstract <T extends IComponentHarness> void render(GuiGraphicsExtractor guiGraphics, Screen screen, int guiX, int guiY, IAssetProvider provider, ProgressBarScreenAddon<T> addon);

        public abstract int getXSize(IAssetProvider provider);

        public abstract int getYSize(IAssetProvider provider);
    }

    public static float[] getTextureDiffuseColors(DyeColor color) {
        int $$6 = (color.getTextureDiffuseColor() & 0xFF0000) >> 16;
        int $$7 = (color.getTextureDiffuseColor() & 0xFF00) >> 8;
        int $$8 = (color.getTextureDiffuseColor() & 0xFF);
        return new float[]{(float)$$6 / 255.0F, (float)$$7 / 255.0F, (float)$$8 / 255.0F};
    }
}
