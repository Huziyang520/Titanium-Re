// Titanium Lite Tile Field Network Message

package com.hrznstudio.titanium.network.messages;

import com.hrznstudio.titanium.block.tile.BasicTile;
import com.hrznstudio.titanium.network.Message;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class TileFieldNetworkMessage extends Message {

    private BlockPos pos;
    private CompoundTag data;

    public TileFieldNetworkMessage(BlockPos pos, CompoundTag data) {
        this.pos = pos;
        this.data = data;
    }

    public TileFieldNetworkMessage() {
    }

    @Override
    protected void handleServerMessage(IPayloadContext context) {

    }

    @Override
    public void handleClientMessage(IPayloadContext context) {
        BlockEntity entity = context.player().level().getBlockEntity(pos);
        if (entity instanceof BasicTile){
            ((BasicTile<?>) entity).handleSyncObject(data);
        }
    }
}
