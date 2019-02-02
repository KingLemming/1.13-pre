package cofh.thermal.core.util.managers;

import cofh.core.util.CoreUtils;
import cofh.core.util.oredict.OreDictionaryArbiter;
import cofh.lib.util.comparison.ComparableItemStack;
import cofh.lib.util.comparison.ItemWrapper;
import cofh.lib.util.helpers.ItemHelper;
import cofh.lib.util.oredict.OreDictHelper;
import cofh.thermal.core.ThermalSeries;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.oredict.OreDictionary;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.*;

import static cofh.lib.util.Constants.FEATURE_DEBUG;
import static cofh.lib.util.helpers.ItemHelper.cloneStack;
import static cofh.lib.util.helpers.ItemHelper.getItemDamage;

public class LexiconManager {

	private LexiconManager() {

	}

	public static final String LEXICON_DATA = "thermal.lexicon_data";

	public static void config() {

		String category = "Lexicon";
		String comment;

		comment = "If TRUE, a WHITELIST is used, if FALSE, a BLACKLIST is used.";
		isWhitelist = ThermalSeries.config.getBoolean("Use Whitelist", category, isWhitelist, comment);

		comment = "If TRUE, a default list will be generated depending on your list setting. This will ONLY generate if no list file already exists OR the Always Generate option is enabled.";
		writeDefaultFile = ThermalSeries.config.getBoolean("Generate Default List", category, writeDefaultFile, comment);

		comment = "If TRUE, a default list will generate EVERY time. Enable this if you are satisfied with the default filtering and are adding/removing mods.";
		alwaysWriteFile = ThermalSeries.config.getBoolean("Always Generate List", category, alwaysWriteFile, comment);

		comment = "If TRUE, all entries will be echoed to the system LOG.";
		logEntries = ThermalSeries.config.getBoolean("Log Entries", category, logEntries, comment);
	}

	public static void initialize() {

		generateList();
		addAllListedOres();
		sortOreNames();
	}

	private static void generateList() {

		filterList = isWhitelist ? new File(CoreUtils.configDir, "/cofh/thermal/lexicon-whitelist.cfg") : new File(CoreUtils.configDir, "/cofh/thermal/lexicon-blacklist.cfg");

		boolean writingDefaultFile = false;
		BufferedWriter out = null;
		ArrayList<String> defaultList = new ArrayList<>();

		if (writeDefaultFile && alwaysWriteFile && filterList.exists()) {
			filterList.delete();
		}
		if (writeDefaultFile && !filterList.exists()) {
			try {
				writingDefaultFile = true;
				filterList.createNewFile();
				out = new BufferedWriter(new FileWriter(filterList));
			} catch (Throwable t) {
				ThermalSeries.log.warn("There is an error in the " + filterList.getName() + " file!");
				t.printStackTrace();
			}
		}
		if (writingDefaultFile) {
			String[] registeredOreNames = OreDictionary.getOreNames();
			for (String oreName : registeredOreNames) {
				if (isWhitelist && ComparableItemStack.DEFAULT_VALIDATOR.validate(oreName)) {
					if (oreName.contains("blockWool") || oreName.contains("blockGlass")) {
						// ignore Wool and Glass
					} else {
						listNames.add(oreName);
						defaultList.add(oreName);
					}
				} else if (!isWhitelist && !ComparableItemStack.DEFAULT_VALIDATOR.validate(oreName) || oreName.contains("blockWool") || oreName.contains("blockGlass")) {
					listNames.add(oreName);
					defaultList.add(oreName);
				}
			}
			Collections.sort(defaultList);
			if (isWhitelist) {
				ThermalSeries.log.info("[Whitelist] Generating Default Whitelist.");
			} else {
				ThermalSeries.log.info("[Blacklist] Generating Default Blacklist.");
			}
			try {
				for (String listEntry : defaultList) {
					out.write(listEntry + "\n");
				}
				out.close();
				defaultList.clear();
			} catch (Throwable t) {
				t.printStackTrace();
			}
		}
	}

	private static void addAllListedOres() {

		try {
			if (!filterList.exists()) {
				return;
			}
			if (isWhitelist) {
				ThermalSeries.log.info("[Whitelist] Reading established Whitelist from file.");
			} else {
				ThermalSeries.log.info("[Blacklist] Reading established Blacklist from file.");
			}
			Scanner scan = new Scanner(filterList);
			String[] line;
			String[] tokens;
			while (scan.hasNext()) {
				line = scan.next().split("\\n");
				tokens = line[0].split(":");

				if (tokens.length == 1) {
					listNames.add(line[0]);
					if (logEntries) {
						if (isWhitelist) {
							ThermalSeries.log.info("[Whitelist] The Forge Lexicon will allow conversions for ALL items of type '" + line[0] + "'.");
						} else {
							ThermalSeries.log.info("[Blacklist] The Forge Lexicon will disable conversions for ALL items of type '" + line[0] + "'.");
						}
					}
				}
			}
			scan.close();
		} catch (Throwable t) {
			ThermalSeries.log.warn("There is an error in the " + filterList.getName() + " file!");
			t.printStackTrace();
		}
	}

	private static void sortOreNames() {

		String[] ores = OreDictionary.getOreNames();

		for (String ore : ores) {
			if (validType(ore) && OreDictionaryArbiter.getOres(ore) != null) {
				sortedNames.add(ore);
			}
		}
		Collections.sort(sortedNames);
	}

	public static List<String> getSortedOreNames() {

		return sortedNames;
	}

	public static boolean validOre(ItemStack stack) {

		if (blacklistStacks.contains(new ItemWrapper(stack)) || ItemHelper.getItemDamage(stack) == OreDictionary.WILDCARD_VALUE) {
			return false;
		}
		return OreDictHelper.hasOreName(stack) && isWhitelist == listNames.contains(OreDictHelper.getOreName(stack));
	}

	private static boolean validType(String oreName) {

		return FEATURE_DEBUG || isWhitelist == listNames.contains(oreName);
	}

	// region PLAYER INTERACTION
	public static ItemStack getPreferredStack(EntityPlayer player, ItemStack stack) {

		NBTTagCompound tag = player.getEntityData();

		if (tag.hasKey(LEXICON_DATA)) {
			NBTTagCompound lexicon = tag.getCompoundTag(LEXICON_DATA);
			String oreName = OreDictHelper.getOreName(stack);

			if (lexicon.hasKey(oreName)) {
				ItemStack retStack = new ItemStack(lexicon.getCompoundTag(oreName));
				if (OreDictHelper.isOreNameEqual(retStack, oreName)) {
					return cloneStack(retStack, stack.getCount());
				}
			}
		}
		ItemStack defaultStack = OreDictionaryArbiter.getOres(stack).get(0);

		if (getItemDamage(defaultStack) == OreDictionary.WILDCARD_VALUE) {
			return stack;
		}
		return cloneStack(defaultStack, stack.getCount());
	}

	public static ItemStack getPreferredStack(EntityPlayer player, String oreName) {

		NBTTagCompound tag = player.getEntityData();

		if (tag.hasKey(LEXICON_DATA)) {
			NBTTagCompound lexicon = tag.getCompoundTag(LEXICON_DATA);

			if (lexicon.hasKey(oreName)) {
				ItemStack retStack = new ItemStack(lexicon.getCompoundTag(oreName));
				if (OreDictHelper.isOreNameEqual(retStack, oreName)) {
					return cloneStack(retStack, 1);
				}
			}
		}
		return cloneStack(OreDictionaryArbiter.getOres(oreName).get(0), 1);
	}

	public static void setPreferredStack(EntityPlayer player, ItemStack stack) {

		NBTTagCompound tag = player.getEntityData();

		NBTTagCompound lexicon = tag.getCompoundTag(LEXICON_DATA);
		String oreName = OreDictHelper.getOreName(stack);
		lexicon.setTag(oreName, stack.writeToNBT(new NBTTagCompound()));

		tag.setTag(LEXICON_DATA, lexicon);
	}

	public static void clearPreferredStack(EntityPlayer player, ItemStack stack) {

		NBTTagCompound tag = player.getEntityData();

		NBTTagCompound lexicon = tag.getCompoundTag(LEXICON_DATA);
		String oreName = OreDictHelper.getOreName(stack);
		lexicon.removeTag(oreName);

		tag.setTag(LEXICON_DATA, lexicon);
	}

	public static boolean hasPreferredStack(EntityPlayer player, String oreName) {

		NBTTagCompound tag = player.getEntityData();
		NBTTagCompound lexicon = tag.getCompoundTag(LEXICON_DATA);

		return lexicon.hasKey(oreName);
	}
	// endregion

	// region ENTRY MANAGEMENT
	public static boolean addBlacklistEntry(ItemStack stack) {

		return !stack.isEmpty() && blacklistStacks.add(new ItemWrapper(stack));
	}

	public static boolean removeBlacklistEntry(ItemStack stack) {

		return !stack.isEmpty() && blacklistStacks.remove(new ItemWrapper(stack));
	}
	// endregion

	private static HashSet<String> listNames = new HashSet<>();
	private static HashSet<ItemWrapper> blacklistStacks = new HashSet<>();
	private static List<String> sortedNames = new ArrayList<>();

	private static boolean isWhitelist = true;
	private static boolean logEntries = false;
	private static boolean writeDefaultFile = true;
	private static boolean alwaysWriteFile = false;

	private static File filterList;

}
