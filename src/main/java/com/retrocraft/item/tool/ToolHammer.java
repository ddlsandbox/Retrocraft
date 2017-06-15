package com.retrocraft.item.tool;

import com.retrocraft.RetroCraft;
import com.retrocraft.item.ItemModelProvider;

import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class ToolHammer extends ToolBase implements ItemModelProvider {

	private String name;

	private static Material[] materials = { Material.ANVIL, Material.GLASS, Material.ICE, Material.IRON,
			Material.PACKED_ICE, Material.PISTON, Material.ROCK };
	
	public ToolHammer(ToolMaterial material, String name) {
		super(material, materials);
		setRegistryName(name);
		setUnlocalizedName(name);
		this.name = name;
	}

	@Override
	public void registerItemModel(Item item) {
		RetroCraft.proxy.registerItemRenderer(this, 0, name);
	}
	  
//	public void mineBlock(World world, int x, int y, int z, int meta, EntityPlayer player, Block block)
//    {
//        // Workaround for dropping experience
//        boolean silktouch = EnchantmentHelper.getSilkTouchModifier(player);
//        int fortune = EnchantmentHelper.getFortuneModifier(player);
//        int exp = block.getExpDrop(world, meta, fortune);
//
//        block.onBlockHarvested(world, x, y, z, meta, player);
//        if (block.removedByPlayer(world, player, x, y, z, true))
//        {
//            block.onBlockDestroyedByPlayer(world, x, y, z, meta);
//            block.harvestBlock(world, player, x, y, z, meta);
//            // Workaround for dropping experience
//            if (!silktouch)
//                block.dropXpOnBlockBreak(world, x, y, z, exp);
//
//            if (world.isRemote)
//            {
//                INetHandler handler = FMLClientHandler.instance().getClientPlayHandler();
//                if (handler != null && handler instanceof NetHandlerPlayClient)
//                {
//                    NetHandlerPlayClient handlerClient = (NetHandlerPlayClient) handler;
//                    handlerClient.addToSendQueue(new C07PacketPlayerDigging(0, x, y, z, Minecraft.getMinecraft().objectMouseOver.sideHit));
//                    handlerClient.addToSendQueue(new C07PacketPlayerDigging(2, x, y, z, Minecraft.getMinecraft().objectMouseOver.sideHit));
//                }
//            }
//            else if (Config.noisyBlocks)
//            {
//                world.playAuxSFX(2001, x, y, z, Block.getIdFromBlock(block) + (meta << 12));
//            }
//        }
//}
	
	@Override
	public ToolHammer setCreativeTab(CreativeTabs tab) {
		super.setCreativeTab(tab);

		return this;
	}
}