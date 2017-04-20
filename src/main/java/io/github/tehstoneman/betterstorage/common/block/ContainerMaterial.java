package io.github.tehstoneman.betterstorage.common.block;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.github.tehstoneman.betterstorage.client.BetterStorageResource;
import io.github.tehstoneman.betterstorage.utils.StackUtils;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class ContainerMaterial
{

	public static final String							TAG_NAME		= "Material";

	private static Map< String, ContainerMaterial >		materialMap		= new HashMap<>();
	private static Map< Integer, ContainerMaterial >	materialMapOld	= new HashMap<>();
	private static List< ContainerMaterial >			materials		= new ArrayList<>();

	// Vanilla materials
	public static ContainerMaterial						iron			= new ContainerMaterial( 0, "iron", Items.IRON_INGOT, Blocks.IRON_BLOCK );
	public static ContainerMaterial						gold			= new ContainerMaterial( 1, "gold", Items.GOLD_INGOT, Blocks.GOLD_BLOCK );
	public static ContainerMaterial						diamond			= new ContainerMaterial( 2, "diamond", Items.DIAMOND, Blocks.DIAMOND_BLOCK );
	public static ContainerMaterial						emerald			= new ContainerMaterial( 3, "emerald", Items.EMERALD, Blocks.EMERALD_BLOCK );

	// Mod materials
	public static ContainerMaterial						copper			= new ContainerMaterial( 5, "copper", "ingotCopper", "blockCopper" );
	public static ContainerMaterial						tin				= new ContainerMaterial( 6, "tin", "ingotTin", "blockTin" );
	public static ContainerMaterial						silver			= new ContainerMaterial( 7, "silver", "ingotSilver", "blockSilver" );
	public static ContainerMaterial						zinc			= new ContainerMaterial( 8, "zinc", "ingotZinc", "blockZinc" );
	public static ContainerMaterial						steel			= new ContainerMaterial( "steel", "ingotSteel", "blockSteel" );

	public static List< ContainerMaterial > getMaterials()
	{
		return materials;
	}

	public static ContainerMaterial get( String name )
	{
		return materialMap.get( name );
	}

	public static ContainerMaterial get( int id )
	{
		return materialMapOld.getOrDefault( id, iron );
	}

	/**
	 * Gets the material of the stack, either using the new method, the
	 * old ID lookup or if everything fails, it'll return the default.
	 */
	public static ContainerMaterial getMaterial( ItemStack stack, ContainerMaterial _default )
	{
		final String name = StackUtils.get( stack, (String)null, TAG_NAME );
		final ContainerMaterial material = name != null ? get( name ) : get( stack.getItemDamage() );
		return material != null ? material : _default;
	}

	public final String		name;

	private final Object	ingot;
	private final Object	block;

	private ContainerMaterial( String name, Object ingot, Object block )
	{
		this.name = name;
		this.ingot = ingot;
		this.block = block;
		materialMap.put( name, this );
		materials.add( this );
	}

	private ContainerMaterial( String name )
	{
		this( name, null, null );
	}

	private ContainerMaterial( int id, String name, Object ingot, Object block )
	{
		this( name, ingot, block );
		materialMapOld.put( id, this );
	}

	public ShapedOreRecipe getReinforcedRecipe( Block middle, Block result )
	{
		if( ingot == null || block == null )
			return null;
		//@formatter:off
		return new ShapedOreRecipe( setMaterial( new ItemStack( result ) ),
								"o#o",
								"#C#",
								"oOo",	'C', middle,
										'#', "logWood",
										'o', ingot,
										'O', block );
		//@formatter:on
	}

	public ResourceLocation getChestResource( boolean large )
	{
		return new BetterStorageResource( "textures/models/chest" + ( large ? "_large/" : "/" ) + name + ".png" );
	}

	public ResourceLocation getLockerResource( boolean large )
	{
		return new BetterStorageResource( "textures/models/locker" + ( large ? "_large/" : "/" ) + name + ".png" );
	}

	public ItemStack setMaterial( ItemStack stack )
	{
		StackUtils.set( stack, name, TAG_NAME );
		return stack;
	}

}
