package cofh.core.item.armor;

import cofh.core.item.ItemCoFH;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.passive.HorseArmorType;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemHorseArmorCoFH extends ItemCoFH {

	protected final HorseArmorType type;

	public ItemHorseArmorCoFH(HorseArmorType type) {

		setCreativeTab(CreativeTabs.MISC);

		this.type = type;
	}

	@Override
	public HorseArmorType getHorseArmorType(ItemStack stack) {

		return type;
	}

	@Override
	public String getHorseArmorTexture(EntityLiving wearer, ItemStack stack) {

		return type.getTextureName();
	}

	@Override
	public void onHorseArmorTick(World world, EntityLiving wearer, ItemStack itemStack) {

	}

}
