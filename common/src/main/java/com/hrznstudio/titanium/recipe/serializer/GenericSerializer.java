// Titanium Lite Generic Serializer

package com.hrznstudio.titanium.recipe.serializer;

import com.hrznstudio.titanium.Titanium;
import com.hrznstudio.titanium.network.CompoundSerializableDataHandler;
import com.mojang.serialization.MapCodec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

import javax.annotation.ParametersAreNonnullByDefault;
import java.lang.reflect.Field;
import java.util.function.Supplier;

public final class GenericSerializer {
    private GenericSerializer() {
    }

    public static <T extends Recipe<?>> RecipeSerializer<T> create(Class<T> recipeClass, Supplier<RecipeType<?>> recipeTypeSupplier, MapCodec<T> codec) {
        StreamCodec<RegistryFriendlyByteBuf, T> streamCodec = StreamCodec.ofMember(
            (value, output) -> toNetwork(recipeClass, output, value),
            input -> fromNetwork(recipeClass, input));
        return new RecipeSerializer<>(codec, streamCodec);
    }

    @ParametersAreNonnullByDefault
    private static <T extends Recipe<?>> T fromNetwork(Class<T> recipeClass, RegistryFriendlyByteBuf buffer) {
        try {
            T recipe = recipeClass.getConstructor().newInstance();
            for (Field field : recipeClass.getFields()) {
                if (CompoundSerializableDataHandler.acceptField(field, field.getType())) {
                    CompoundSerializableDataHandler.readField(field, field.getType(), buffer, recipe);
                }
            }
            return recipe;
        } catch (Exception e) {
            Titanium.LOGGER.error("Failed to read recipe", e);
        }
        return null;
    }

    @ParametersAreNonnullByDefault
    private static <T extends Recipe<?>> void toNetwork(Class<T> recipeClass, RegistryFriendlyByteBuf buffer, T recipe) {
        try {
            for (Field field : recipeClass.getFields()) {
                if (CompoundSerializableDataHandler.acceptField(field, field.getType())) {
                    CompoundSerializableDataHandler.writeField(field, field.getType(), buffer, recipe);
                }
            }
        } catch (Exception e) {
            Titanium.LOGGER.error("Failed to read recipe", e);
        }
    }

}
