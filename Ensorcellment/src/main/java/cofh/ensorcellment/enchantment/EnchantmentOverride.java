package cofh.ensorcellment.enchantment;

import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraftforge.fml.common.FMLLog;

import static cofh.lib.util.Constants.ID_ENSORCELLMENT;

public abstract class EnchantmentOverride extends EnchantmentBase {

	protected EnchantmentOverride(String id, Rarity rarityIn, EnumEnchantmentType typeIn, EntityEquipmentSlot[] slots) {

		super(id, rarityIn, typeIn, slots);
		FMLLog.log.warn("[{}] - This is an INTENTIONAL override. The mod is NOT broken - the Forge warning cannot be removed or prevented. This override can be disabled in the mod configuration.", ID_ENSORCELLMENT);
	}

	@Override
	public String getName() {

		return "enchantment." + this.name;
	}

}
