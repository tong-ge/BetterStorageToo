package io.github.tehstoneman.betterstorage.client.model.xobj;

import java.io.IOException;
import java.util.Map;

import javax.annotation.Nullable;

import com.google.common.collect.Maps;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;

import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.model.IModelLoader;
import net.minecraftforge.client.model.obj.LineReader;
import net.minecraftforge.client.model.obj.MaterialLibrary;

@OnlyIn( Dist.CLIENT )
public class XOBJLoader// implements IModelLoader< XOBJModel >
{
	/*
	 * public static XOBJLoader INSTANCE = new XOBJLoader();
	 * 
	 * private final Map< XOBJModel.ModelSettings, XOBJModel > modelCache = Maps.newHashMap();
	 * private final Map< ResourceLocation, MaterialLibrary > materialCache = Maps.newHashMap();
	 * 
	 * private IResourceManager manager = Minecraft.getInstance().getResourceManager();
	 * 
	 * @Override
	 * public void onResourceManagerReload( IResourceManager resourceManager )
	 * {
	 * modelCache.clear();
	 * materialCache.clear();
	 * manager = resourceManager;
	 * }
	 * 
	 * @Override
	 * public XOBJModel read( JsonDeserializationContext deserializationContext, JsonObject modelContents )
	 * {
	 * if( !modelContents.has( "model" ) )
	 * throw new RuntimeException( "XOBJ Loader requires a 'model' key that points to a valid .OBJ model." );
	 * 
	 * final String modelLocation = modelContents.get( "model" ).getAsString();
	 * 
	 * final boolean detectCullableFaces = GsonHelper.getAsBoolean( modelContents, "detectCullableFaces", true );
	 * final boolean diffuseLighting = GsonHelper.getAsBoolean( modelContents, "diffuseLighting", false );
	 * final boolean flipV = GsonHelper.getAsBoolean( modelContents, "flip-v", false );
	 * final boolean ambientToFullbright = GsonHelper.getAsBoolean( modelContents, "ambientToFullbright", true );
	 * 
	 * @Nullable
	 * final String materialLibraryOverrideLocation = modelContents.has( "materialLibraryOverride" )
	 * ? GsonHelper.getAsString( modelContents, "materialLibraryOverride" )
	 * : null;
	 * 
	 * return loadModel( new XOBJModel.ModelSettings( new ResourceLocation( modelLocation ), detectCullableFaces, diffuseLighting, flipV,
	 * ambientToFullbright, materialLibraryOverrideLocation ) );
	 * }
	 * 
	 * public XOBJModel loadModel( XOBJModel.ModelSettings settings )
	 * {
	 * return modelCache.computeIfAbsent( settings, ( data ) ->
	 * {
	 * IResource resource;
	 * try
	 * {
	 * resource = manager.getResource( settings.modelLocation );
	 * }
	 * catch( final IOException e )
	 * {
	 * throw new RuntimeException( "Could not find OBJ model", e );
	 * }
	 * 
	 * try( LineReader reader = new LineReader( resource ) )
	 * {
	 * return new XOBJModel( reader, settings );
	 * }
	 * catch( final Exception e )
	 * {
	 * throw new RuntimeException( "Could not read OBJ model", e );
	 * }
	 * } );
	 * }
	 */
}
