package fr.romax.medievalcom.common.entities;

import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.world.World;

public class EntityVillagerMessager extends EntityAnimal{

	public EntityVillagerMessager(World worldIn) {
		super(worldIn);
	}

	@Override
	public EntityAgeable createChild(EntityAgeable ageable) {
		return new EntityVillagerMessager(ageable.world);
	}

}
