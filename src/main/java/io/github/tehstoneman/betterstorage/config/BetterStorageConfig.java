package io.github.tehstoneman.betterstorage.config;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;

public class BetterStorageConfig
{
	private static final ForgeConfigSpec.Builder	BUILDER	= new ForgeConfigSpec.Builder();
	public static General							GENERAL	= new General( BUILDER );
	private static ForgeConfigSpec					commonSpec;

	public static class General
	{
		public ForgeConfigSpec.ConfigValue< Integer >	reinforcedColumns;
		public ForgeConfigSpec.ConfigValue< Boolean >	enableCrateInventoryInterface;

		public ForgeConfigSpec.IntValue					cardboardBoxRows;
		public ForgeConfigSpec.IntValue					cardboardBoxUses;
		public ForgeConfigSpec.BooleanValue				cardboardBoxShowContents;

		public ForgeConfigSpec.BooleanValue				lockBreakable;

		public ForgeConfigSpec.BooleanValue				enableHelpTooltips;
		public ForgeConfigSpec.BooleanValue				enableWarningMessages;

		public General( ForgeConfigSpec.Builder builder )
		{
			builder.push( "General" );
			//@formatter:off
			reinforcedColumns = builder
					.comment( "Number of columns in reinforced chests and lockers. Valid values are 9, 11 and 13." )
					.translation( "config.betterstorage.general.reinforcedColumns" )
					.define( "reinforcedColumns", 13 );
			enableCrateInventoryInterface = builder
					.comment( "If enabled, exposes a special block view of crates, so items can be moved in and out by automated systems." )
					.translation( "config.betterstorage.general.enableCrateInventoryInterface" )
					.define( "enableCrateInventoryInterface", true );
			//@formatter:on

		}
	}

	static
	{
		final Pair< General, ForgeConfigSpec > specPair = new ForgeConfigSpec.Builder().configure( General::new );
		commonSpec = specPair.getRight();
		GENERAL = specPair.getLeft();
	}

	public static void register( ModLoadingContext context )
	{
		context.registerConfig( ModConfig.Type.COMMON, commonSpec );
	}
}
