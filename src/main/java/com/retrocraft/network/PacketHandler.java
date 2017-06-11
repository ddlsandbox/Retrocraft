package com.retrocraft.network;

import java.util.ArrayList;
import java.util.List;

import com.retrocraft.RetroCraft;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public final class PacketHandler {

    public static final List<IDataHandler> DATA_HANDLERS = new ArrayList<IDataHandler>();
    
    public static final IDataHandler TILE_ENTITY_HANDLER = new IDataHandler(){
        @Override
        @SideOnly(Side.CLIENT)
        public void handleData(NBTTagCompound compound, MessageContext context){
            World world = Minecraft.getMinecraft().world;
            if(world != null){
                TileEntity tile = world.getTileEntity(new BlockPos(compound.getInteger("X"), compound.getInteger("Y"), compound.getInteger("Z")));
                tile.readFromNBT(compound);
//                if(tile instanceof TileEntityBase){
//                    ((TileEntityBase)tile).readSyncableNBT(compound.getCompoundTag("Data"), TileEntityBase.NBTType.SYNC);
//                }
            }
        }
    };

    public static SimpleNetworkWrapper theNetwork;

    public static void init(){
        theNetwork = NetworkRegistry.INSTANCE.newSimpleChannel(RetroCraft.modId);
        theNetwork.registerMessage(PacketServerToClient.Handler.class, PacketServerToClient.class, 0, Side.CLIENT);
        theNetwork.registerMessage(PacketClientToServer.Handler.class, PacketClientToServer.class, 1, Side.SERVER);

        DATA_HANDLERS.add(TILE_ENTITY_HANDLER);
    }
}