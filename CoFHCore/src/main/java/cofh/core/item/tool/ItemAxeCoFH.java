package cofh.core.item.tool;

import net.minecraft.block.material.Material;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import static cofh.lib.util.Constants.TOOL_AXE;

public class ItemAxeCoFH extends ItemToolCoFH {

	public ItemAxeCoFH(ToolMaterial toolMaterial) {

		super(3.0F, -3.2F, toolMaterial);

		setHarvestLevel(TOOL_AXE, toolMaterial.getHarvestLevel());

		effectiveMaterials.add(Material.WOOD);
		effectiveMaterials.add(Material.PLANTS);
		effectiveMaterials.add(Material.VINE);
		effectiveMaterials.add(Material.CACTUS);
		effectiveMaterials.add(Material.GOURD);

		if (harvestLevel > 0) {
			attackDamage = 8.0F;
			attackSpeed = -3.3F + (0.1F * harvestLevel);
		} else {
			attackDamage = 6.0F;
			attackSpeed = -3.2F + (0.1F * (int) (efficiency / 5));
		}
	}

	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {

		return super.canApplyAtEnchantingTable(stack, enchantment) || enchantment.canApply(new ItemStack(Items.IRON_AXE));
	}

	@Override
	public boolean canDisableShield(ItemStack stack, ItemStack shield, EntityLivingBase entity, EntityLivingBase attacker) {

		return true;
	}

}
