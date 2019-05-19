package net.minecraft.entity;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import javax.annotation.Nullable;

/**
 * Such a dirty hack. :)
 */
public class CoFH_EntityLivingProxy extends EntityLiving {

	public CoFH_EntityLivingProxy(World worldIn) {

		super(worldIn);
	}

	@Nullable
	public static ResourceLocation getLootTable(EntityLiving entity) {

		return entity.getLootTable();
	}

}
