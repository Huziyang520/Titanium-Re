// Titanium Lite NBT Manager

package com.hrznstudio.titanium.nbthandler;

import com.google.common.collect.Lists;
import com.hrznstudio.titanium.annotation.Save;
import com.hrznstudio.titanium.api.INBTHandler;
import com.hrznstudio.titanium.nbthandler.data.*;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;

import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NBTManager {

    private static NBTManager ourInstance = new NBTManager();
    private final List<INBTHandler> handlerList;
    private final HashMap<Class<? extends BlockEntity>, List<Field>> tileFieldList;

    private NBTManager() {
        handlerList = new ArrayList<>();
        tileFieldList = new HashMap<>();
        handlerList.add(new IntegerNBTHandler());
        handlerList.add(new ShortNBTHandler());
        handlerList.add(new LongNBTHandler());
        handlerList.add(new FloatNBTHandler());
        handlerList.add(new DoubleNBTHandler());
        handlerList.add(new BooleanNBTHandler());
        handlerList.add(new ItemStackNBTHandler());
        handlerList.add(new BlockPosNBTHandler());
        handlerList.add(new StringNBTHandler());
        handlerList.add(new EnumDyeColorNBTHandler());
        handlerList.add(new ItemStackHandlerNBTHandler());
        handlerList.add(new UUIDNBTHandler());
        handlerList.add(new NBTSerializableNBTHandler());
    }

    public static NBTManager getInstance() {
        return ourInstance;
    }

    private static List<Field> getAllDeclaredFields(Class<?> entity) {
        List<Field> currentClassFields = Lists.newArrayList(entity.getDeclaredFields());
        Class<?> parent = entity.getSuperclass();
        if (BlockEntity.class.isAssignableFrom(parent))
            currentClassFields.addAll(getAllDeclaredFields(entity.getSuperclass()));
        return currentClassFields;
    }

    public void scanTileClassForAnnotations(Class<? extends BlockEntity> entity) {
        List<Field> fields = new ArrayList<>();
        for (Field field : getAllDeclaredFields(entity)) {
            if (field.isAnnotationPresent(Save.class) && checkForHandler(field)) {
                field.setAccessible(true);
                fields.add(field);
            }
        }
        for (Field field : entity.getFields()) {
            if (field.isAnnotationPresent(Save.class) && checkForHandler(field)) {
                fields.add(field);
            }
        }
        if (!fields.isEmpty()) tileFieldList.put(entity, fields);
    }

    private boolean checkForHandler(Field field) {
        for (INBTHandler<?> handler : handlerList) {
            if (handler.isClassValid(field.getType())) {
                return true;
            }
        }
        throw new RuntimeException("Missing NBT Field processor for " + field.getType());
    }

    public CompoundTag writeTileEntity(BlockEntity entity, CompoundTag compound) {
        if (tileFieldList.containsKey(entity.getClass())) {
            for (Field field : tileFieldList.get(entity.getClass())) {
                Save save = field.getAnnotation(Save.class);
                try {
                    Object obj = field.get(entity);
                    if (obj == null) continue;
                    compound = handleNBTWrite(entity.getLevel().registryAccess(), compound, save.value().isEmpty() ? field.getName() : save.value(), obj, field);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return compound;
    }

    public void readTileEntity(BlockEntity entity, HolderLookup.Provider provider, CompoundTag compound) {
        if (tileFieldList.containsKey(entity.getClass())) {
            for (Field field : tileFieldList.get(entity.getClass())) {
                Save save = field.getAnnotation(Save.class);
                var name = save.value().isEmpty() ? field.getName() : save.value();
                if (compound.contains(name)){
                    try {
                        Object value = handleNBTRead(provider, compound, name, field.get(entity), field);
                        field.set(entity, value);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public CompoundTag writeTileEntityObject(BlockEntity entity, Object object, CompoundTag compound) {
        if (tileFieldList.containsKey(entity.getClass())) {
            for (Field field : tileFieldList.get(entity.getClass())) {
                try {
                    if (object.equals(field.get(entity))) {
                        Save save = field.getAnnotation(Save.class);
                        Object obj = field.get(entity);
                        if (obj == null) continue;
                        compound = handleNBTWrite(entity.getLevel().registryAccess(), compound, save.value().isEmpty() ? field.getName() : save.value(), obj, field);
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return compound;
    }

    private CompoundTag handleNBTWrite(HolderLookup.Provider provider, CompoundTag compound, String name, Object value, Field field) {
        for (INBTHandler handler : handlerList) {
            if (handler.isClassValid(value == null ? field.getType() : value.getClass())) {
                if (handler.storeToNBT(provider, compound, name, value)) return compound;
            }
        }
        return compound;
    }

    private Object handleNBTRead(HolderLookup.Provider provider, CompoundTag compound, String name, @Nullable Object value, Field field) {
        for (INBTHandler handler : handlerList) {
            if (handler.isClassValid(value == null ? field.getType() : value.getClass())) {
                if (!compound.contains(name)) continue;
                Object readValue = handler.readFromNBT(provider, compound, name, value);
                if (readValue != null) {
                    return readValue;
                }
            }
        }
        return value;
    }

    public List<INBTHandler> getHandlerList() {
        return handlerList;
    }

    public HashMap<Class<? extends BlockEntity>, List<Field>> getTileFieldList() {
        return tileFieldList;
    }
}
