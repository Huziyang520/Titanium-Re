// Titanium Lite active tile with components exposed to NeoForge capabilities

package com.hrznstudio.titanium.block.tile;

import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.api.client.IScreenAddonProvider;
import com.hrznstudio.titanium.api.filter.IFilter;
import com.hrznstudio.titanium.block.BasicTileBlock;
import com.hrznstudio.titanium.block.RotatableBlock;
import com.hrznstudio.titanium.client.screen.asset.IAssetProvider;
import com.hrznstudio.titanium.client.screen.asset.IHasAssetProvider;
import com.hrznstudio.titanium.component.IComponentBundle;
import com.hrznstudio.titanium.component.button.ButtonComponent;
import com.hrznstudio.titanium.component.button.MultiButtonComponent;
import com.hrznstudio.titanium.component.filter.MultiFilterComponent;
import com.hrznstudio.titanium.component.inventory.InventoryComponent;
import com.hrznstudio.titanium.component.inventory.MultiInventoryComponent;
import com.hrznstudio.titanium.component.progress.MultiProgressBarHandler;
import com.hrznstudio.titanium.component.progress.ProgressBarComponent;
import com.hrznstudio.titanium.component.sideness.IFacingComponent;
import com.hrznstudio.titanium.component.sideness.IFacingComponentHarness;
import com.hrznstudio.titanium.container.BasicAddonContainer;
import com.hrznstudio.titanium.container.addon.IContainerAddon;
import com.hrznstudio.titanium.container.addon.IContainerAddonProvider;
import com.hrznstudio.titanium.network.IButtonHandler;
import com.hrznstudio.titanium.network.locator.LocatorFactory;
import com.hrznstudio.titanium.network.locator.instance.TileEntityLocatorInstance;
import com.hrznstudio.titanium.util.FacingUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class ActiveTile<T extends ActiveTile<T>> extends BasicTile<T> implements IScreenAddonProvider,
    ITickableBlockEntity<T>, MenuProvider, IButtonHandler, IFacingComponentHarness, IContainerAddonProvider,
    IHasAssetProvider, IInventoryProvider {

    private MultiInventoryComponent<T> multiInventoryComponent;
    private MultiProgressBarHandler<T> multiProgressBarHandler;
    private MultiButtonComponent multiButtonComponent;
    private MultiFilterComponent multiFilterComponent;

    private final List<IFactory<?>> guiAddons;
    private final List<IFactory<? extends IContainerAddon>> containerAddons;
    private final List<IComponentBundle> bundles;

    public ActiveTile(BasicTileBlock<T> base, BlockEntityType<?> blockEntityType, BlockPos pos, BlockState state) {
        super(base, blockEntityType, pos, state);
        this.guiAddons = new ArrayList<>();
        this.containerAddons = new ArrayList<>();
        this.bundles = new ArrayList<>();
    }

    @Override
    @ParametersAreNonnullByDefault
    public InteractionResult onActivated(Player player, InteractionHand hand, Direction facing, double hitX, double hitY, double hitZ) {
        return InteractionResult.FAIL;
    }

    @Override
    public void onNeighborChanged(Block blockIn, BlockPos fromPos) {

    }

    public void openGui(Player player) {
        if (player instanceof ServerPlayer serverPlayer) {
            serverPlayer.openMenu(this, buffer ->
                LocatorFactory.writePacketBuffer(buffer, new TileEntityLocatorInstance(this.worldPosition)));
        }
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int menu, Inventory inventoryPlayer, Player entityPlayer) {
        return new BasicAddonContainer(this, new TileEntityLocatorInstance(this.worldPosition), this.getWorldPosCallable(),
            inventoryPlayer, menu);
    }

    @Override
    @Nonnull
    public Component getDisplayName() {
        return Component.translatable(this.getBasicTileBlock().getDescriptionId()).setStyle(Style.EMPTY.withColor(ChatFormatting.DARK_GRAY));
    }

    public void addInventory(InventoryComponent<T> handler) {
        if (this.multiInventoryComponent == null) {
            this.multiInventoryComponent = new MultiInventoryComponent<>();
        }
        this.multiInventoryComponent.add(handler.setComponentHarness(this.getSelf()));
    }

    public void addProgressBar(ProgressBarComponent<T> progressBarComponent) {
        if (this.multiProgressBarHandler == null) {
            this.multiProgressBarHandler = new MultiProgressBarHandler<>();
        }
        this.multiProgressBarHandler.add(progressBarComponent.setComponentHarness(this.getSelf()));
    }

    public void addButton(ButtonComponent button) {
        if (this.multiButtonComponent == null) {
            this.multiButtonComponent = new MultiButtonComponent();
        }
        this.multiButtonComponent.add(button);
    }

    public void addFilter(IFilter<?> filter) {
        if (this.multiFilterComponent == null) {
            this.multiFilterComponent = new MultiFilterComponent();
        }
        this.multiFilterComponent.add(filter);
    }

    public void addBundle(IComponentBundle bundle) {
        if (this.multiInventoryComponent == null) {
            this.multiInventoryComponent = new MultiInventoryComponent<>();
        }
        if (this.multiProgressBarHandler == null) {
            this.multiProgressBarHandler = new MultiProgressBarHandler<>();
        }
        if (this.multiButtonComponent == null) {
            this.multiButtonComponent = new MultiButtonComponent();
        }
        if (this.multiFilterComponent == null) {
            this.multiFilterComponent = new MultiFilterComponent();
        }
        bundle.accept(this.multiInventoryComponent, this.multiProgressBarHandler, null, this.multiButtonComponent, this.multiFilterComponent);
        bundle.getContainerAddons().forEach(this::addContainerAddonFactory);
        this.bundles.add(bundle);
    }

    @Override
    public void initClient() {
        super.initClient();
        this.bundles.forEach(bundle -> bundle.getScreenAddons().forEach(this::addGuiAddonFactory));
    }

    public MultiInventoryComponent<T> getMultiInventoryComponent() {
        return this.multiInventoryComponent;
    }

    @Override
    public Collection<InventoryComponent<?>> getInventories() {
        return this.multiInventoryComponent == null ? List.of() : new ArrayList<>(this.multiInventoryComponent.getInventoryHandlers());
    }

    public void addGuiAddonFactory(IFactory<?> factory) {
        this.guiAddons.add(factory);
    }

    public void addContainerAddonFactory(IFactory<? extends IContainerAddon> factory) {
        this.containerAddons.add(factory);
    }

    @Override
    public List<IFactory<?>> getScreenAddons() {
        List<IFactory<?>> addons = new ArrayList<>(this.guiAddons);
        if (this.multiInventoryComponent != null) {
            addons.addAll(this.multiInventoryComponent.getScreenAddons());
        }
        if (this.multiProgressBarHandler != null) {
            addons.addAll(this.multiProgressBarHandler.getScreenAddons());
        }
        if (this.multiButtonComponent != null) {
            addons.addAll(this.multiButtonComponent.getScreenAddons());
        }
        if (this.multiFilterComponent != null) {
            addons.addAll(this.multiFilterComponent.getScreenAddons());
        }
        return addons;
    }

    @Override
    public List<IFactory<? extends IContainerAddon>> getContainerAddons() {
        List<IFactory<? extends IContainerAddon>> addons = new ArrayList<>(this.containerAddons);
        if (this.multiInventoryComponent != null) {
            addons.addAll(this.multiInventoryComponent.getContainerAddons());
        }
        if (this.multiProgressBarHandler != null) {
            addons.addAll(this.multiProgressBarHandler.getContainerAddons());
        }
        return addons;
    }

    @Override
    public IAssetProvider getAssetProvider() {
        return IAssetProvider.DEFAULT_PROVIDER;
    }

    @Override
    public void serverTick(Level level, BlockPos pos, BlockState state, T blockEntity) {
        if (this.multiProgressBarHandler != null) {
            this.multiProgressBarHandler.update();
        }
        if (level.getGameTime() % this.getFacingHandlerWorkTime() == 0 && this.multiInventoryComponent != null) {
            for (InventoryComponent<T> inventoryHandler : this.multiInventoryComponent.getInventoryHandlers()) {
                if (inventoryHandler instanceof IFacingComponent component) {
                    component.work(this.level, this.worldPosition, this.getFacingDirection(), this.getFacingHandlerWorkAmount());
                }
            }
        }
    }

    public int getFacingHandlerWorkTime() {
        return 10;
    }

    public int getFacingHandlerWorkAmount() {
        return 4;
    }

    public MultiButtonComponent getMultiButtonComponent() {
        return this.multiButtonComponent;
    }

    public Direction getFacingDirection() {
        BlockState state = this.level.getBlockState(this.worldPosition);
        if (state.hasProperty(RotatableBlock.FACING_ALL)) {
            return state.getValue(RotatableBlock.FACING_ALL);
        }
        return state.hasProperty(RotatableBlock.FACING_HORIZONTAL) ? state.getValue(RotatableBlock.FACING_HORIZONTAL) : Direction.NORTH;
    }

    @Override
    public IFacingComponent getHandlerFromName(String string) {
        if (this.multiInventoryComponent != null) {
            for (InventoryComponent<T> handler : this.multiInventoryComponent.getInventoryHandlers()) {
                if (handler instanceof IFacingComponent facing && handler.getName().equalsIgnoreCase(string)) {
                    return facing;
                }
            }
        }
        return null;
    }

    @Override
    public void handleButtonMessage(int id, Player playerEntity, CompoundTag compound) {
        if (id == -2 && this.multiFilterComponent != null) {
            String name = compound.getStringOr("Name", "");
            for (IFilter<?> filter : this.multiFilterComponent.getFilters()) {
                if (filter.getName().equals(name)) {
                    int slot = compound.getIntOr("Slot", 0);
                    filter.setFilter(slot, com.hrznstudio.titanium.util.ItemStackSerialization.load(this.level.registryAccess(), compound.getCompoundOrEmpty("Filter")));
                    this.markComponentDirty();
                    break;
                }
            }
        }
        if (id == -1) {
            String name = compound.getStringOr("Name", "");
            FacingUtil.Sideness facing = FacingUtil.Sideness.valueOf(compound.getStringOr("Facing", ""));
            int faceMode = compound.getIntOr("Next", 0);
            if (this.multiInventoryComponent != null && this.multiInventoryComponent.handleFacingChange(name, facing, faceMode)) {
                this.markForUpdate();
            }
        } else if (this.multiButtonComponent != null) {
            this.multiButtonComponent.clickButton(id, playerEntity, compound);
        }
    }

    @Nonnull
    public abstract T getSelf();

    @Override
    public Level getComponentWorld() {
        return this.getSelf().getLevel();
    }

    @Override
    public void markComponentDirty() {
        super.setChanged();
    }

    @Override
    public void markComponentForUpdate(boolean referenced) {
        if (!referenced) {
            super.markForUpdate();
        } else {
            this.markComponentDirty();
        }
    }

    public ContainerLevelAccess getWorldPosCallable() {
        return this.getLevel() != null ? ContainerLevelAccess.create(this.getLevel(), this.getBlockPos()) : ContainerLevelAccess.NULL;
    }

    public boolean canInteract() {
        return this.level.getBlockEntity(this.worldPosition) == this;
    }

    public ResourceHandler<ItemResource> getItemHandler(@Nullable Direction direction) {
        return this.multiInventoryComponent == null ? null
            : this.multiInventoryComponent.getCapabilityForSide(FacingUtil.getFacingRelative(this.getFacingDirection(), direction)).orElse(null);
    }

    public ResourceHandler<net.neoforged.neoforge.transfer.fluid.FluidResource> getFluidHandler(@Nullable Direction direction) {
        return null;
    }

    public MultiFilterComponent getMultiFilterComponent() {
        return this.multiFilterComponent;
    }

    @Override
    protected void loadAdditional(net.minecraft.world.level.storage.ValueInput input) {
        super.loadAdditional(input);
        if (this.multiInventoryComponent != null) {
            this.multiInventoryComponent.rebuildCapability(FacingUtil.Sideness.values());
        }
    }
}
