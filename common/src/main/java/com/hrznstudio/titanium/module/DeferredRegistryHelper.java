// Titanium Lite Deferred Registry Helper

package com.hrznstudio.titanium.module;

import com.hrznstudio.titanium.block.BasicBlock;
import com.hrznstudio.titanium.block.BasicTileBlock;
import com.hrznstudio.titanium.block.tile.ITileFactory;
import com.hrznstudio.titanium.platform.IRegistryHelper;
import com.hrznstudio.titanium.tab.TitaniumTab;

import javax.annotation.Nullable;
import java.util.function.Function;
import java.util.function.Supplier;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;

public abstract class DeferredRegistryHelper implements IRegistryHelper {

    private static final ThreadLocal<ResourceKey<Block>> CURRENT_BLOCK_KEY = new ThreadLocal<>();
    private static final ThreadLocal<ResourceKey<Item>> CURRENT_ITEM_KEY = new ThreadLocal<>();

    private final String modId;

    public DeferredRegistryHelper(String modId) {
        this.modId = modId;
    }

    public static Block.Properties applyBlockRegistrationId(Block.Properties properties) {
        ResourceKey<Block> key = CURRENT_BLOCK_KEY.get();
        return key == null ? properties : properties.setId(key);
    }

    public static Item.Properties applyItemRegistrationId(Item.Properties properties) {
        ResourceKey<Item> key = CURRENT_ITEM_KEY.get();
        return key == null ? properties : properties.setId(key);
    }

    protected String modId() {
        return this.modId;
    }

    public <T> net.minecraft.core.Holder<T> registerHolder(ResourceKey<? extends Registry<T>> key, String name, Supplier<T> supplier) {
        return createHolder(key, name, supplier);
    }

    protected abstract <T> net.minecraft.core.Holder<T> createHolder(ResourceKey<? extends Registry<T>> key, String name, Supplier<T> supplier);

    protected abstract <T> Supplier<T> createRegistration(ResourceKey<? extends Registry<T>> key, String name, Supplier<T> supplier);

    protected <T> void onRegistered(ResourceKey<? extends Registry<T>> key, Supplier<T> registered) {
    }

    public void registerCapabilities(Supplier<BlockEntityType<?>> type) {
    }

    public String getModId() {
        return this.modId;
    }

    @Override
    public <T> Supplier<T> register(Registry<T> registry, String name, Supplier<T> supplier) {
        return registerFor(registry.key(), name, supplier);
    }

    @Override
    public <T> Supplier<T> registerFor(ResourceKey<? extends Registry<T>> key, String name, Supplier<T> supplier) {
        Identifier id = Identifier.fromNamespaceAndPath(this.modId, name);
        return createRegistration(key, name, () -> withRegistrationId(key, id, supplier));
    }

    public <T> Supplier<T> registerGeneric(ResourceKey<? extends Registry<T>> key, String name, Supplier<T> supplier) {
        Supplier<T> registered = registerFor(key, name, supplier);
        onRegistered(key, registered);
        return registered;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public <T, R extends T> Supplier<R> registerTyped(ResourceKey<? extends Registry<T>> key, String name, Supplier<R> supplier) {
        return (Supplier<R>) registerGeneric((ResourceKey) key, name, (Supplier) supplier);
    }

    public Supplier<BlockEntityType<?>> registerBlockEntityType(String name, Supplier<BlockEntityType<?>> supplier) {
        return registerFor(Registries.BLOCK_ENTITY_TYPE, name, supplier);
    }

    public Supplier<EntityType<?>> registerEntityType(String name, Supplier<EntityType<?>> supplier) {
        return registerFor(Registries.ENTITY_TYPE, name, supplier);
    }

    public Supplier<Block> registerBlockWithItem(String name, Supplier<? extends BasicBlock> blockSupplier, @Nullable TitaniumTab tab) {
        Supplier<Block> block = registerGeneric(Registries.BLOCK, name, blockSupplier::get);
        registerGeneric(Registries.ITEM, name, () -> {
            Item item = new BlockItem(block.get(), applyItemRegistrationId(new Item.Properties().useBlockDescriptionPrefix()));
            if (tab != null) tab.getTabList().add(item);
            return item;
        });
        return block;
    }

    public Supplier<Block> registerBlockWithItem(String name, Supplier<? extends Block> blockSupplier, Function<Supplier<Block>, Supplier<Item>> itemSupplier, @Nullable TitaniumTab tab) {
        Supplier<Block> block = registerGeneric(Registries.BLOCK, name, blockSupplier::get);
        registerGeneric(Registries.ITEM, name, () -> {
            Item item = itemSupplier.apply(block).get();
            if (tab != null) tab.getTabList().add(item);
            return item;
        });
        return block;
    }

    protected abstract BlockEntityType<?> createBlockEntityType(ITileFactory<?> factory, Block block);

    public BlockWithTile registerBlockWithTile(String name, Supplier<BasicTileBlock<?>> blockSupplier, @Nullable TitaniumTab tab) {
        Supplier<Block> block = registerBlockWithItem(name, blockSupplier, tab);
        Supplier<BlockEntityType<?>> type = registerBlockEntityType(name, () -> createBlockEntityType(((BasicTileBlock<?>) block.get()).getTileEntityFactory(), block.get()));
        registerCapabilities(type);
        return new BlockWithTile(block, type);
    }

    public BlockWithTile registerBlockWithTileItem(String name, Supplier<BasicTileBlock<?>> blockSupplier, Function<Supplier<Block>, Supplier<Item>> itemSupplier, @Nullable TitaniumTab tab) {
        Supplier<Block> block = registerBlockWithItem(name, blockSupplier, itemSupplier, tab);
        Supplier<BlockEntityType<?>> type = registerBlockEntityType(name, () -> createBlockEntityType(((BasicTileBlock<?>) block.get()).getTileEntityFactory(), block.get()));
        registerCapabilities(type);
        return new BlockWithTile(block, type);
    }

    private static <T> T withRegistrationId(ResourceKey<? extends Registry<T>> key, Identifier id, Supplier<T> supplier) {
        if (Registries.BLOCK.equals(key)) {
            CURRENT_BLOCK_KEY.set(ResourceKey.create(Registries.BLOCK, id));
        } else if (Registries.ITEM.equals(key)) {
            CURRENT_ITEM_KEY.set(ResourceKey.create(Registries.ITEM, id));
        }
        try {
            return supplier.get();
        } finally {
            CURRENT_BLOCK_KEY.remove();
            CURRENT_ITEM_KEY.remove();
        }
    }
}
