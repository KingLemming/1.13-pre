package cofh.thermal.expansion.plugins.jei.dynamo;

import cofh.lib.util.oredict.OreDictHelper;
import cofh.thermal.core.plugins.jei.BaseRecipeWrapper;
import cofh.thermal.core.util.recipes.IDynamoFuel;
import cofh.thermal.expansion.util.managers.dynamo.StirlingFuelManager;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.List;

import static cofh.lib.util.helpers.ItemHelper.cloneStack;
import static java.util.Collections.singletonList;

public class TemplateFuelWrapper extends BaseRecipeWrapper {

	protected List<List<ItemStack>> input;

	public TemplateFuelWrapper(IGuiHelper guiHelper, IDynamoFuel fuel) {

		List<ItemStack> fuelInput = new ArrayList<>();
		int oreID = StirlingFuelManager.instance().convertInput(fuel.getInputItems().get(0)).oreID;
		if (oreID != -1) {
			int count = fuel.getInputItems().get(0).getCount();
			for (ItemStack ore : OreDictionary.getOres(OreDictHelper.getOreName(oreID), false)) {
				fuelInput.add(cloneStack(ore, count));
			}
		} else {
			fuelInput.addAll(fuel.getInputItems());
		}
		input = singletonList(fuelInput);
	}

	@Override
	public void getIngredients(IIngredients ingredients) {

		ingredients.setInputLists(VanillaTypes.ITEM, input);
	}

	@Override
	public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {

	}

}
