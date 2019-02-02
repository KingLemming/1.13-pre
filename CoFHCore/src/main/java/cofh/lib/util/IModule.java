package cofh.lib.util;

import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionType;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.VillagerRegistry.VillagerProfession;

public interface IModule {

	// region REGISTRATION
	default void registerBlocks(RegistryEvent.Register<Block> event) {

	}

	default void registerItems(RegistryEvent.Register<Item> event) {

	}

	default void registerBiomes(RegistryEvent.Register<Biome> event) {

	}

	default void registerEnchantments(RegistryEvent.Register<Enchantment> event) {

	}

	default void registerEntities(RegistryEvent.Register<EntityEntry> event) {

	}

	// TODO: Placeholder for 1.13.
	default void registerFluids() {

	}

	default void registerPotions(RegistryEvent.Register<Potion> event) {

	}

	default void registerPotionTypes(RegistryEvent.Register<PotionType> event) {

	}

	default void registerRecipes(RegistryEvent.Register<IRecipe> event) {

	}

	default void registerSoundEvents(RegistryEvent.Register<SoundEvent> event) {

	}

	default void registerVillagerProfessions(RegistryEvent.Register<VillagerProfession> event) {

	}
	// endregion
}
