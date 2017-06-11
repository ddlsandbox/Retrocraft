package com.retrocraft.entity;

import com.retrocraft.RetroCraft;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.registry.EntityRegistry;

public class ModEntities {

	private static final int KINTON_CLOUD = 1;
	
	public static void init() {
		register(EntityKintonCloud.class, "entity_cloud");
	}

	private static void register(Class <? extends Entity> entityClass, String entityName) {
		
		EntityRegistry.registerModEntity(
				new ModelResourceLocation(RetroCraft.modId + ":", entityName), 
				entityClass, 
				entityName, 
				KINTON_CLOUD, 
				RetroCraft.instance, 
				64, // trackingRange
				1, // updateFrequency
				true, // sendsVelocityUpdates
				0xffffff, 0x00ff00); //eggs
	}
}