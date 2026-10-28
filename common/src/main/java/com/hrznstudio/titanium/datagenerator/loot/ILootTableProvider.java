// Loot table builder provider

package com.hrznstudio.titanium.datagenerator.loot;

import net.minecraft.world.level.storage.loot.LootTable;

public interface ILootTableProvider<T> {

    LootTable.Builder getLootTable(T helper);
}
