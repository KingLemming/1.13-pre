package cofh.thermal.core.init;

import cofh.core.util.CoreUtils;
import cofh.lib.util.oredict.OreDictHelper;
import cofh.thermal.core.ThermalSeries;
import cofh.thermal.core.util.parsers.IContentParser;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.item.ItemStack;
import net.minecraft.util.JsonUtils;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.ModContainer;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;

import java.io.BufferedReader;
import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import static cofh.lib.util.Constants.ID_THERMAL_SERIES;
import static cofh.lib.util.helpers.ItemHelper.cloneStack;
import static cofh.thermal.core.util.parsers.AbstractContentParser.TYPE;

public class ParsersTSeries {

	private ParsersTSeries() {

	}

	// region PARSERS
	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
	private static final Map<String, IContentParser> PARSERS = new Object2ObjectOpenHashMap<>();
	private static File contentDir;

	public static void registerParser(String name, IContentParser parser) {

		if (!PARSERS.containsKey(name)) {
			PARSERS.put(name, parser);
		}
	}

	public static void preProcess() {

		contentDir = new File(CoreUtils.configDir, "/cofh/" + ID_THERMAL_SERIES + "/content/");
		if (!contentDir.exists()) {
			try {
				contentDir.mkdir();
			} catch (Throwable t) {
				// pokemon!
			}
		}

		for (IContentParser parser : PARSERS.values()) {
			parser.preProcess();
		}
	}

	public static void initializeParsers() {

		preProcess();

		ModContainer mod = FMLCommonHandler.instance().findContainerFor(ID_THERMAL_SERIES);

		parseCustomConstants();
		parseConstants(mod);

		parseCustomContent();
		parseContent(mod);
	}

	public static void postProcess() {

		for (IContentParser parser : PARSERS.values()) {
			parser.postProcess();
		}
	}
	// endregion

	// region FILES
	private static void addConstantFiles(ArrayList<File> list, File folder) {

		File[] fList = folder.listFiles((file, name) -> name != null && ((name.toLowerCase(Locale.US).endsWith(".json") && name.startsWith("_")) || new File(file, name).isDirectory()));
		if (fList == null || fList.length <= 0) {
			return;
		}
		list.addAll(Arrays.asList(fList));
	}

	private static void addContentFiles(ArrayList<File> list, File folder) {

		File[] fList = folder.listFiles((file, name) -> name != null && ((name.toLowerCase(Locale.US).endsWith(".json") && !name.startsWith("_")) || new File(file, name).isDirectory()));
		if (fList == null || fList.length <= 0) {
			return;
		}
		list.addAll(Arrays.asList(fList));
	}

	private static void parseCustomConstants() {

		ArrayList<File> fList = new ArrayList<>();
		addConstantFiles(fList, contentDir);

		for (int i = 0; i < fList.size(); ++i) {
			File file = fList.get(i);
			if (file.isDirectory()) {
				addConstantFiles(fList, file);
				continue;
			}
			String fileName = file.getName();
			BufferedReader reader = null;
			try {
				reader = Files.newBufferedReader(file.toPath());
				JsonObject json = JsonUtils.fromJson(GSON, reader, JsonObject.class);
				if (json == null) {
					continue;
				}
				for (Entry<String, JsonElement> contentEntry : json.entrySet()) {
					if (parseEntry(contentEntry.getKey(), contentEntry.getValue())) {
						ThermalSeries.log.debug("Content entry added from file " + fileName + ": \"" + contentEntry.getKey() + "\"");
					} else {
						ThermalSeries.log.error("Error parsing content from file " + fileName + ": \"" + contentEntry.getKey() + "\"");
					}
				}
			} catch (Exception e) {
				ThermalSeries.log.error("Error parsing content file " + fileName + "!", e);
			} finally {
				IOUtils.closeQuietly(reader);
			}
		}
	}

	private static void parseCustomContent() {

		ArrayList<File> contentList = new ArrayList<>();
		addContentFiles(contentList, contentDir);

		for (int i = 0; i < contentList.size(); ++i) {
			File file = contentList.get(i);
			if (file.isDirectory()) {
				addContentFiles(contentList, file);
				continue;
			}
			String fileName = file.getName();
			BufferedReader reader = null;
			try {
				reader = Files.newBufferedReader(file.toPath());
				JsonObject json = JsonUtils.fromJson(GSON, reader, JsonObject.class);
				if (json == null) {
					continue;
				}
				for (Entry<String, JsonElement> contentEntry : json.entrySet()) {
					if (parseEntry(contentEntry.getKey(), contentEntry.getValue())) {
						ThermalSeries.log.debug("Content entry added from file " + fileName + ": \"" + contentEntry.getKey() + "\"");
					} else {
						ThermalSeries.log.error("Error parsing content from file " + fileName + ": \"" + contentEntry.getKey() + "\"");
					}
				}
			} catch (Exception e) {
				ThermalSeries.log.error("Error parsing content file " + fileName + "!", e);
			} finally {
				IOUtils.closeQuietly(reader);
			}
		}
	}

	private static void parseConstants(ModContainer mod) {

		CraftingHelper.findFiles(mod, "assets/" + mod.getModId() + "/thermal_content/", null, (root, file) -> {

			String fileName = file.getFileName().toString();
			if (!"json".equals(FilenameUtils.getExtension(fileName)) || !fileName.startsWith("_")) {
				return true;
			}
			BufferedReader reader = null;
			try {
				reader = Files.newBufferedReader(file);
				JsonObject json = JsonUtils.fromJson(GSON, reader, JsonObject.class);
				for (Entry<String, JsonElement> contentEntry : json.entrySet()) {
					if (parseEntry(contentEntry.getKey(), contentEntry.getValue())) {
						ThermalSeries.log.debug("Content entry added from file " + fileName + ": \"" + contentEntry.getKey() + "\"");
					} else {
						ThermalSeries.log.error("Error parsing content from file " + fileName + ": \"" + contentEntry.getKey() + "\"");
					}
				}
			} catch (Exception e) {
				ThermalSeries.log.error("Error parsing content file " + fileName + "!", e);
			} finally {
				IOUtils.closeQuietly(reader);
			}
			return true;
		}, false, false);
	}

	private static void parseContent(ModContainer mod) {

		CraftingHelper.findFiles(mod, "assets/" + mod.getModId() + "/thermal_content/", null, (root, file) -> {

			String fileName = file.getFileName().toString();
			if (!"json".equals(FilenameUtils.getExtension(fileName)) || fileName.startsWith("_")) {
				return true;
			}
			BufferedReader reader = null;
			try {
				reader = Files.newBufferedReader(file);
				JsonObject json = JsonUtils.fromJson(GSON, reader, JsonObject.class);
				for (Entry<String, JsonElement> contentEntry : json.entrySet()) {
					if (parseEntry(contentEntry.getKey(), contentEntry.getValue())) {
						ThermalSeries.log.debug("Content entry added from file " + fileName + ": \"" + contentEntry.getKey() + "\"");
					} else {
						ThermalSeries.log.error("Error parsing content from file " + fileName + ": \"" + contentEntry.getKey() + "\"");
					}
				}
			} catch (Exception e) {
				ThermalSeries.log.error("Error parsing content file " + fileName + "!", e);
			} finally {
				IOUtils.closeQuietly(reader);
			}
			return true;
		}, false, false);
	}

	private static boolean parseEntry(String type, JsonElement element) {

		if (PARSERS.containsKey(type)) {                                // array
			return PARSERS.get(type).process(element);
		} else {                                                        // object
			JsonObject object = element.getAsJsonObject();
			if (object.has(TYPE)) {
				String parsedType = object.get(TYPE).getAsString();
				if (PARSERS.containsKey(parsedType)) {
					return PARSERS.get(parsedType).process(element);
				}
			}
		}
		return false;
	}
	// endregion

	// region HELPERS
	public static final Map<String, ItemStack> CONSTANTS = new Object2ObjectOpenHashMap<>();
	public static final Map<String, ItemStack> ORES = new Object2ObjectOpenHashMap<>();

	public static boolean hasPreferredOre(String oreName) {

		return ORES.containsKey(oreName);
	}

	public static ItemStack getPreferredOre(String oreName) {

		return getPreferredOre(oreName, 1);
	}

	public static ItemStack getPreferredOre(String oreName, int count) {

		if (!hasPreferredOre(oreName)) {
			return OreDictHelper.getOre(oreName, count);
		}
		return cloneStack(ORES.get(oreName), count);
	}
	// endregion
}
