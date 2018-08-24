package fr.romax.medievalcom.common.crafting;


import java.util.function.Predicate;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.IRecipeFactory;
import net.minecraftforge.common.crafting.JsonContext;
import net.minecraftforge.oredict.ShapelessOreRecipe;

public class ShapelessCopyTagsRecipe extends ShapelessOreRecipe
{
	protected final Predicate<ItemStack> copyInput;

	protected ShapelessCopyTagsRecipe(ResourceLocation group, Ingredient copyInput, NonNullList<Ingredient> input, ItemStack result)
	{
		super(group, input, result);
		this.copyInput = copyInput;
	}
	
	@Override
	public ItemStack getCraftingResult(InventoryCrafting inv)
	{
		ItemStack result = super.getCraftingResult(inv);
		
		if (!result.isEmpty())
		{
			for (int i = 0; i < inv.getSizeInventory(); ++i)
	        {
	            ItemStack itemstack = inv.getStackInSlot(i);
	            if (!itemstack.isEmpty())
	            {
	            	if (this.copyInput.test(itemstack))
	            	{
	            		result.setTagCompound(itemstack.getTagCompound().copy());
	            	}
	            }
	        }
		}
		
		return result;
	}

	public static class Factory implements IRecipeFactory
	{
		@Override
		public IRecipe parse(JsonContext context, JsonObject json)
		{
			final String group = JsonUtils.getString(json, "group", "");

			NonNullList<Ingredient> ingredients = NonNullList.create();
			for (JsonElement ele : JsonUtils.getJsonArray(json, "ingredients", new JsonArray()))
			{
				ingredients.add(CraftingHelper.getIngredient(ele, context));
			}
			
			Ingredient copyIngredients = CraftingHelper.getIngredient(json.get("copy_ingredient"), context);
			ingredients.add(copyIngredients);
			
			final ItemStack result = CraftingHelper.getItemStack(JsonUtils.getJsonObject(json, "result"), context);
			
			return new ShapelessCopyTagsRecipe(group.isEmpty() ? null : new ResourceLocation(group), copyIngredients, ingredients, result);
		}
	}
	
}
