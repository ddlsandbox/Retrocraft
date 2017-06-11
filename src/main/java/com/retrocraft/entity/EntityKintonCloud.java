package com.retrocraft.entity;

import net.minecraft.entity.item.EntityBoat;
import net.minecraft.world.World;

public class EntityKintonCloud extends EntityBoat {

	public EntityKintonCloud(World world) {
		super(world);
	}

	public EntityKintonCloud(World world, double x, double y, double z) {
        super(world, x, y, z);
        //this.inventory = new ItemStack[INVENTORY_SIZE];
        this.setSize(1.375F, 0.8625F);
}
	
	}