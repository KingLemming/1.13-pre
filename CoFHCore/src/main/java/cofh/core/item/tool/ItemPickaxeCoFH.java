package cofh.core.item.tool;

import net.minecraft.block.material.Material;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import static cofh.lib.util.Constants.TOOL_PICKAXE;

public class ItemPickaxeCoFH extends ItemToolCoFH {

	public ItemPickaxeCoFH(ToolMaterial toolMaterial) {

		super(1.0F, -2.8F, toolMaterial);

		setHarvestLevel(TOOL_PICKAXE, toolMaterial.getHarvestLevel());

		effectiveMaterials.add(Material.IRON);
		effectiveMaterials.add(Material.ANVIL);
		effectiveMaterials.add(Material.ROCK);
		effectiveMaterials.add(Material.ICE);
		effectiveMaterials.add(Material.PACKED_ICE);
		effectiveMaterials.add(Material.GLASS);
		effectiveMaterials.add(Material.REDSTONE_LIGHT);
	}

	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {

		return super.canApplyAtEnchantingTable(stack, enchantment) || enchantment.canApply(new ItemStack(Items.IRON_PICKAXE));
	}

}
