package io.github.tehstoneman.betterstorage.item.recipe;

import java.util.List;
import java.util.logging.Logger;

import io.github.tehstoneman.betterstorage.ModInfo;
import io.github.tehstoneman.betterstorage.api.crafting.ICraftingSource;
import io.github.tehstoneman.betterstorage.api.crafting.IRecipeInput;
import io.github.tehstoneman.betterstorage.api.crafting.RecipeInputBase;
import io.github.tehstoneman.betterstorage.api.crafting.StationCrafting;
import io.github.tehstoneman.betterstorage.config.GlobalConfig;
import io.github.tehstoneman.betterstorage.utils.StackUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.Level;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.ItemStackHandler;

public class VanillaStationCrafting extends StationCrafting
{
	public VanillaStationCrafting( Level world, IRecipe recipe, ItemStack[] craftingIn, ItemStack output )
	{
		super( new ItemStack[] { null, null, null, null, output }, createRecipeInput( world, recipe, craftingIn ) );

	}

	private static IRecipeInput[] createRecipeInput( Level world, IRecipe recipe, ItemStack[] craftingIn )
	{
		final IRecipeInput[] requiredInput = new IRecipeInput[9];
		for( int i = 0; i < craftingIn.length; i++ )
			if( craftingIn[ i ] != null )
			{
				requiredInput[i] = new VanillaRecipeInput( world, recipe, craftingIn, i );
			}
		return requiredInput;
	}

	@Override
	public boolean canCraft( ICraftingSource source )
	{
		return source.getPlayer() != null || !GlobalConfig.enableStationAutoCraftingSetting.getValue();
	}

	private static class VanillaRecipeInput extends RecipeInputBase
	{

		private final Level				world;
		private final IRecipe			recipe;
		private final int				slot;

		private final InventoryCrafting	crafting;
		private final ItemStack			expectedOutput;

		public VanillaRecipeInput( Level world, IRecipe recipe, ItemStack[] craftingIn, int slot )
		{
			this.world = world;
			this.recipe = recipe;
			this.slot = slot;

			crafting = new InventoryCrafting( new FakeContainer(), 3, 3 );
			for( int i = 0; i < craftingIn.length; i++ )
			{
				crafting.setInventorySlotContents( i, ItemStack.copyItemStack( craftingIn[ i ] ) );
			}

			final ItemStack output = recipe.assemble( crafting );
			if( output == null )
				throw new IllegalArgumentException( recipe.getClass() + " returned null for assemble." );
			expectedOutput = output.copy();
		}

		@Override
		public int getAmount()
		{
			return 1;
		}

		@Override
		public boolean matches( ItemStack stack )
		{
			final ItemStack stackBefore = crafting.getItem( slot );
			crafting.setInventorySlotContents( slot, stack );
			final boolean matches = recipe.matches( crafting, world ) && StackUtils.matches( expectedOutput, recipe.assemble( crafting ) );
			crafting.setInventorySlotContents( slot, stackBefore );
			return matches;
		}

		@Override
		@SideOnly( Side.CLIENT )
		public List< ItemStack > getBlockPossibleMatches()
		{
			return null;
		}

	}

	public static VanillaStationCrafting findVanillaRecipe( ItemStack[] craftingIn,  Level world )
	{
		// final Level world = inv.entity != null ? inv.entity.getLevel() : WorldUtils.getLocalWorld();
		//final Level world = Minecraft.getMinecraft().theWorld;

		final InventoryCrafting crafting = new InventoryCrafting( new FakeContainer(), 3, 3 );
		for( int i = 0; i < craftingIn.length; i++ )
			crafting.setInventorySlotContents( i, ItemStack.copyItemStack( craftingIn[ i ] ) );

		final IRecipe recipe = findRecipe( crafting, world );
		return recipe == null ? null : new VanillaStationCrafting( world, recipe, craftingIn, recipe.assemble( crafting ) );
	}

	private static IRecipe findRecipe( InventoryCrafting crafting, Level world )
	{
		for( final IRecipe recipe : CraftingManager.getInstance().getRecipeList() )
			if( recipe.matches( crafting, world ) )
				return recipe;
		return null;
	}

	private static class FakeContainer extends Container
	{
		@Override
		public boolean stillValid( EntityPlayer player )
		{
			return false;
		}
	}
}
