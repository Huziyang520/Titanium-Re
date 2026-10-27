// Titanium Lite Tile Field Network Message

package com.hrznstudio.titanium.network.messages;

import com.hrznstudio.titanium.block.tile.BasicTile;
import com.hrznstudio.titanium.network.IMessageContext;
import com.hrznstudio.titanium.network.Message;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;

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
    protected void handleServerMessage(IMessageContext context) {

    }

    @Override
    public void handleClientMessage(IMessageContext context) {
        BlockEntity entity = context.player().level().getBlockEntity(pos);
        if (entity instanceof BasicTile) {
            ((BasicTile<?>) entity).handleSyncObject(data);
        }
    }
}
