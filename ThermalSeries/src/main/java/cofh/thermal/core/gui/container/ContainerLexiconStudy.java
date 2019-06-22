package cofh.thermal.core.gui.container;

import cofh.core.gui.container.ContainerInventoryItem;
import cofh.core.util.oredict.OreDictionaryArbiter;
import cofh.lib.inventory.container.slot.SlotLocked;
import cofh.thermal.core.network.PacketLexiconStudy;
import cofh.thermal.core.util.managers.LexiconManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;

import static cofh.lib.util.helpers.ItemHelper.itemsIdentical;
import static cofh.thermal.core.network.PacketLexiconStudy.*;

public class ContainerLexiconStudy extends ContainerInventoryItem {

	ArrayList<ItemStack> oreList;
	int oreSelection = -1;
	boolean hasPreferredStack = false;
	boolean syncClient = false;

	EntityPlayer player;

	public InventoryLexiconStudy lexiconInv = new InventoryLexiconStudy();

	public ContainerLexiconStudy(ItemStack stack, InventoryPlayer inventory) {

		super(stack, inventory);

		player = inventory.player;
		addSlotToContainer(new SlotLocked(lexiconInv, 0, 95, 33));
	}

	public boolean canSetPreferred() {

		ItemStack input = lexiconInv.getStackInSlot(0);

		if (input.isEmpty()) {
			return false;
		}
		return LexiconManager.validOre(input);
	}

	public boolean doSetPreferred() {

		if (!canSetPreferred()) {
			return false;
		}
		LexiconManager.setPreferredStack(player, lexiconInv.getStackInSlot(0));
		hasPreferredStack = true;
		syncClient = true;

		return true;
	}

	public boolean doClearPreferred() {

		if (!hasPreferredOre()) {
			return false;
		}
		LexiconManager.clearPreferredStack(player, lexiconInv.getStackInSlot(0));
		hasPreferredStack = false;
		syncClient = true;

		return true;
	}

	public boolean hasMultipleOres() {

		return oreList != null && oreList.size() > 1;
	}

	public boolean hasPreferredOre() {

		return hasPreferredStack;
	}

	public void prevOre() {

		oreSelection += oreList.size() - 1;
		oreSelection %= oreList.size();
		lexiconInv.setInventorySlotContents(0, oreList.get(oreSelection));
	}

	public void nextOre() {

		oreSelection++;
		oreSelection %= oreList.size();
		lexiconInv.setInventorySlotContents(0, oreList.get(oreSelection));
	}

	public void onSelectionChanged(String oreName) {

		oreList = OreDictionaryArbiter.getOres(oreName);
		if (LexiconManager.hasPreferredStack(player, oreName)) {
			ItemStack ore = LexiconManager.getPreferredStack(player, oreName);
			lexiconInv.setInventorySlotContents(0, ore);
			for (int i = 0; i < oreList.size(); i++) {
				if (itemsIdentical(oreList.get(i), ore)) {
					oreSelection = i;
					break;
				}
			}
		}
		PacketLexiconStudy.sendToServer(oreName);
	}

	public void handlePacket(PacketLexiconStudy packet) {

		switch (packet.command) {
			case ORE_PREV:
				prevOre();
				return;
			case ORE_NEXT:
				nextOre();
				return;
			case SET_PREFERRED:
				doSetPreferred();
				return;
			case CLEAR_PREFERRED:
				doClearPreferred();
				return;
			case SELECT_ORE:
				String oreName = packet.oreName;
				oreList = OreDictionaryArbiter.getOres(oreName);
				if (LexiconManager.hasPreferredStack(player, oreName)) {
					ItemStack ore = LexiconManager.getPreferredStack(player, oreName);
					lexiconInv.setInventorySlotContents(0, ore);
					for (int i = 0; i < oreList.size(); i++) {
						if (itemsIdentical(oreList.get(i), ore)) {
							oreSelection = i;
							break;
						}
					}
					hasPreferredStack = true;
				} else {
					lexiconInv.setInventorySlotContents(0, OreDictionaryArbiter.getOres(oreName).get(0));
					oreSelection = 0;
					hasPreferredStack = false;
				}
				syncClient = true;
			default:

		}
	}

	@Override
	public void detectAndSendChanges() {

		super.detectAndSendChanges();

		for (IContainerListener listener : this.listeners) {
			if (syncClient) {
				listener.sendWindowProperty(this, 0, hasPreferredStack ? 1 : 0);
				syncClient = false;
			}
		}
	}

	@Override
	public void updateProgressBar(int i, int j) {

		hasPreferredStack = j == 1;
	}

	@Override
	public boolean canInteractWith(EntityPlayer player) {

		return true;
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int slotIndex) {

		return ItemStack.EMPTY;
	}

	@Override
	protected int getPlayerInventoryVerticalOffset() {

		return 84;
	}

}
