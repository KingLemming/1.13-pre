package cofh.core;

import cofh.core.key.KeyHandler;
import cofh.core.key.PacketKey;
import cofh.core.network.PacketHandler;
import cofh.core.network.packet.client.PacketIndexedChat;
import cofh.core.network.packet.client.PacketTileControl;
import cofh.core.network.packet.client.PacketTileGui;
import cofh.core.network.packet.client.PacketTileState;
import cofh.core.network.packet.server.*;
import cofh.core.proxy.ProxyCommon;
import cofh.core.util.CoreUtils;
import cofh.core.util.oredict.OreDictionaryArbiter;
import cofh.lib.capabilities.CapabilityEnchantable;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLInterModComms.IMCEvent;
import net.minecraftforge.fml.common.event.FMLModIdMappingEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static cofh.lib.util.Constants.*;

@Mod (modid = CoFHCore.MOD_ID, name = CoFHCore.MOD_NAME, version = CoFHCore.VERSION, dependencies = CoFHCore.DEPENDENCIES)
public class CoFHCore {

	public static final String MOD_ID = ID_COFH_CORE;
	public static final String MOD_NAME = "CoFH Core";
	public static final String VERSION = "0.1.0";
	public static final String DEPENDENCIES = "required-after:forge@[14.0.0.0,15.0.0.0);";

	@Instance (MOD_ID)
	public static CoFHCore instance;

	@SidedProxy (clientSide = "cofh.core.proxy.ProxyClient", serverSide = "cofh.core.proxy.ProxyCommon")
	public static ProxyCommon proxy;

	public static Logger log = LogManager.getLogger(MOD_ID);
	public static PacketHandler packetHandler;

	static {
		FluidRegistry.enableUniversalBucket();
	}

	// region INITIALIZATION
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {

		CoreUtils.configDir = event.getModConfigurationDirectory();

		registerOreDictionaryEntries();
		registerHandlers();
		CapabilityEnchantable.register();

		proxy.preInit(event);
	}

	@EventHandler
	public void initialize(FMLInitializationEvent event) {

		proxy.initialize(event);
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {

		proxy.postInit(event);
	}

	@EventHandler
	public void handleIdMappingEvent(FMLModIdMappingEvent event) {

		OreDictionaryArbiter.initialize();
	}

	@EventHandler
	public void handleIMC(IMCEvent event) {

		OreDictionaryArbiter.initialize();
	}
	// endregion

	// region HELPERS
	private void registerOreDictionaryEntries() {

		OreDictionary.registerOre("blockGlowstone", new ItemStack(Blocks.GLOWSTONE));
		OreDictionary.registerOre("blockWool", new ItemStack(Blocks.WOOL, 1, OreDictionary.WILDCARD_VALUE));
		OreDictionary.registerOre("coal", new ItemStack(Items.COAL));
		OreDictionary.registerOre("charcoal", new ItemStack(Items.COAL, 1, 1));
		OreDictionary.registerOre("dustBlaze", new ItemStack(Items.BLAZE_POWDER));
		OreDictionary.registerOre("rodBlaze", new ItemStack(Items.BLAZE_ROD));

		OreDictionary.registerOre("cropBeetroot", Items.BEETROOT);
		OreDictionary.registerOre("cropMelon", Blocks.MELON_BLOCK);
		OreDictionary.registerOre("cropPumpkin", Blocks.PUMPKIN);
		OreDictionary.registerOre("cropWheat", Items.WHEAT);

		OreDictionary.registerOre("seedBeetroot", Items.BEETROOT_SEEDS);
		OreDictionary.registerOre("seedMelon", Items.MELON_SEEDS);
		OreDictionary.registerOre("seedPumpkin", Items.PUMPKIN_SEEDS);
		OreDictionary.registerOre("seedWheat", Items.WHEAT_SEEDS);

		// OreDictionary.registerOre("cofh:potion", Items.POTIONITEM);
		// OreDictionary.registerOre("cofh:potion", Items.SPLASH_POTION);
		// OreDictionary.registerOre("cofh:potion", Items.LINGERING_POTION);
	}

	private void registerHandlers() {

		packetHandler = new PacketHandler(ID_COFH);

		packetHandler.registerPacket(PACKET_CONTROL, PacketTileControl::new);
		packetHandler.registerPacket(PACKET_GUI, PacketTileGui::new);
		packetHandler.registerPacket(PACKET_STATE, PacketTileState::new);
		packetHandler.registerPacket(PACKET_CHAT, PacketIndexedChat::new);
		packetHandler.registerPacket(PACKET_SECURITY, PacketSecurity::new);
		packetHandler.registerPacket(PACKET_FILTER, PacketFilter::new);
		packetHandler.registerPacket(PACKET_SECURITY_CONTROL, PacketSecurityControl::new);
		packetHandler.registerPacket(PACKET_REDSTONE_CONTROL, PacketRedstoneControl::new);
		packetHandler.registerPacket(PACKET_TRANSFER_CONTROL, PacketTransferControl::new);
		packetHandler.registerPacket(PACKET_SIDE_CONFIG, PacketSideConfig::new);
		packetHandler.registerPacket(PACKET_KEY, PacketKey::new);

		MinecraftForge.EVENT_BUS.register(KeyHandler.INSTANCE);
	}
	// endregion
}
