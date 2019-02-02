package cofh.orbulation.util;

import cofh.lib.util.Utils;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static cofh.lib.util.helpers.ItemHelper.cloneStack;
import static cofh.orbulation.Orbulation.*;

public class MorbUtils {

	private MorbUtils() {

	}

	public static final String GENERIC = "Generic";
	public static final ArrayList<ItemStack> MORB_LIST = new ArrayList<>();
	public static final Set<String> VALID_MOBS = new ObjectOpenHashSet<>();

	public static boolean enable = true;
	public static List<String> blacklist = new ArrayList<>();
	public static int reuseChance = 25;

	public static ItemStack setTag(ItemStack container, String entityId, boolean genericTag) {

		if (entityId != null && !entityId.isEmpty()) {
			container.setTagCompound(new NBTTagCompound());
			container.getTagCompound().setString("id", entityId);
			if (genericTag) {
				container.getTagCompound().setBoolean(GENERIC, true);
			}
		}
		return container;
	}

	public static void config() {

		String category = "Morbs";
		String comment = "If TRUE, the recipes for Morbs are enabled. Setting this to FALSE means that you actively dislike fun things and/or Pokemon tributes.";
		enable = config.getBoolean("EnableRecipe", category, enable, comment);

		category = "Morbs.Blacklist";
		comment = "List of entities that are not allowed to be placed in Morbs. Mobs without spawn eggs are automatically disallowed.";
		blacklist = Arrays.asList(config.getStringList("Blacklist", category, new String[0], comment));

		addDefaultMorbs();
	}

	// region HELPERS
	public static void addDefaultMorbs() {

		for (ResourceLocation name : EntityList.getEntityNameList()) {
			Class<? extends Entity> clazz = EntityList.getClass(name);
			if (clazz == null || !EntityLiving.class.isAssignableFrom(clazz)) {
				continue;
			}
			if (blacklist.contains(name.toString()) || !EntityList.ENTITY_EGGS.containsKey(name)) {
				continue;
			}
			addMorb(name.toString());
		}
	}

	public static void addMorb(String entityId) {

		ItemStack morb = cloneStack(itemMorb);
		setTag(morb, entityId, true);
		MORB_LIST.add(morb);
		VALID_MOBS.add(entityId);
	}

	public static void dropMorb(int type, NBTTagCompound nbt, World world, BlockPos pos) {

		ItemStack stack = type == 0 ? cloneStack(itemMorb) : cloneStack(itemMorbReusable);

		if (nbt != null && VALID_MOBS.contains(nbt.getString("id"))) {
			if (nbt.hasKey(GENERIC)) {
				stack = setTag(stack, nbt.getString("id"), true);
			} else {
				nbt.removeTag("Pos");
				nbt.removeTag("Motion");
				nbt.removeTag("Rotation");
				nbt.removeTag("FallDistance");
				nbt.removeTag("Fire");
				nbt.removeTag("Air");
				nbt.removeTag("OnGround");
				nbt.removeTag("Dimension");
				// nbt.removeTag("Invulnerable");
				nbt.removeTag("PortalCooldown");
				nbt.removeTag("UUIDMost");
				nbt.removeTag("UUIDLeast");

				nbt.removeTag("Leashed");
				nbt.removeTag("Leash");

				stack.setTagCompound(nbt);
			}
		}
		Utils.dropItemStackIntoWorldWithVelocity(stack, world, pos);
	}

	@Nonnull
	public static ItemStack getMorb(int type, NBTTagCompound nbt) {

		ItemStack stack = type == 0 ? cloneStack(itemMorb) : cloneStack(itemMorbReusable);

		if (nbt != null && VALID_MOBS.contains(nbt.getString("id"))) {
			if (nbt.hasKey(GENERIC)) {
				stack = setTag(stack, nbt.getString("id"), true);
			} else {
				nbt.removeTag("Pos");
				nbt.removeTag("Motion");
				nbt.removeTag("Rotation");
				nbt.removeTag("FallDistance");
				nbt.removeTag("Fire");
				nbt.removeTag("Air");
				nbt.removeTag("OnGround");
				nbt.removeTag("Dimension");
				// nbt.removeTag("Invulnerable");
				nbt.removeTag("PortalCooldown");
				nbt.removeTag("UUIDMost");
				nbt.removeTag("UUIDLeast");

				nbt.removeTag("Leashed");
				nbt.removeTag("Leash");

				stack.setTagCompound(nbt);
			}
		}
		return stack;
	}
	// endregion
}
