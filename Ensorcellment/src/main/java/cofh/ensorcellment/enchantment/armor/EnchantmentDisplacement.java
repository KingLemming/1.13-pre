package cofh.ensorcellment.enchantment.armor;

import cofh.ensorcellment.Ensorcellment;
import cofh.lib.enchantment.EnchantmentCoFH;
import cofh.lib.util.Utils;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.HorseArmorType;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.FakePlayer;

import java.util.Random;

import static cofh.lib.util.Constants.MAX_ENCHANT_LEVEL;

public class EnchantmentDisplacement extends EnchantmentCoFH {

	public static int chance = 20;

	public EnchantmentDisplacement(String id) {

		super(id, Rarity.RARE, EnumEnchantmentType.ARMOR, EntityEquipmentSlot.values());
	}

	@Override
	protected void config() {

		String category = "Enchantment.Displacement";
		String comment = "If TRUE, the Displacement Enchantment is available for Armor, Shields, and Horse Armor.";
		enable = Ensorcellment.config.get("Enable", category, enable, comment);

		comment = "This option adjusts the maximum allowable level for the Enchantment.";
		maxLevel = Ensorcellment.config.get("Max Level", category, 3, 1, MAX_ENCHANT_LEVEL, comment);

		comment = "Adjust this value to set the chance per level of the Enchantment firing (in percentage).";
		chance = Ensorcellment.config.get("Effect Chance", category, chance, 1, 100, comment);
	}

	@Override
	public int getMinEnchantability(int enchantmentLevel) {

		return 5 + 10 * (enchantmentLevel - 1);
	}

	@Override
	public int getMaxEnchantability(int enchantmentLevel) {

		return super.getMinEnchantability(enchantmentLevel) + 50;
	}

	@Override
	public boolean canApply(ItemStack stack) {

		Item item = stack.getItem();
		return enable && (item instanceof ItemArmor || item.isShield(stack, null) || item.getHorseArmorType(stack) != HorseArmorType.NONE || capabilityCompatible(stack));
	}

	// region HELPERS
	@Override
	public void onUserHurt(EntityLivingBase user, Entity attacker, int level) {

		if (level <= 0 || !(attacker instanceof EntityLivingBase) || attacker instanceof FakePlayer) {
			return;
		}
		Random rand = user.getRNG();
		if (shouldHit(level, rand)) {
			teleportEntity(level, rand, attacker);
		}
	}

	public static boolean teleportEntity(int level, Random rand, Entity attacker) {

		if (!(attacker instanceof EntityLivingBase) || attacker instanceof FakePlayer) {
			return false;
		}
		int radius = 8 * (2 ^ level);
		int bound = radius * 2 + 1;
		BlockPos pos = new BlockPos(attacker.posX, attacker.posY, attacker.posZ);
		BlockPos randPos = pos.add(-radius + rand.nextInt(bound), rand.nextInt(8), -radius + rand.nextInt(bound));
		Utils.teleportEntityTo(attacker, randPos);
		return true;
	}

	public static boolean shouldHit(int level, Random rand) {

		return rand.nextInt(100) < chance * level;
	}
	// endregion
}
