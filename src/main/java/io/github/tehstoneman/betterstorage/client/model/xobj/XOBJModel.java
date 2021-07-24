package io.github.tehstoneman.betterstorage.client.model.xobj;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import joptsimple.internal.Strings;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.model.IModelTransform;
import net.minecraft.client.renderer.model.IUnbakedModel;
import net.minecraft.client.renderer.model.ModelBakery;
import net.minecraft.client.renderer.model.RenderMaterial;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormatElement;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.TransformationMatrix;
import net.minecraft.util.math.vector.Vector2f;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.math.vector.Vector4f;
import net.minecraftforge.client.model.IModelBuilder;
import net.minecraftforge.client.model.IModelConfiguration;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.client.model.geometry.IModelGeometryPart;
import net.minecraftforge.client.model.geometry.IMultipartModelGeometry;
import net.minecraftforge.client.model.obj.LineReader;
import net.minecraftforge.client.model.obj.MaterialLibrary;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.client.model.obj.OBJModel;
import net.minecraftforge.client.model.pipeline.BakedQuadBuilder;
import net.minecraftforge.client.model.pipeline.IVertexConsumer;

public class XOBJModel implements IMultipartModelGeometry< XOBJModel >
{
	private static Vector4f					COLOR_WHITE		= new Vector4f( 1, 1, 1, 1 );
	private static Vector2f[]				DEFAULT_COORDS	= { new Vector2f( 0, 0 ), new Vector2f( 0, 1 ), new Vector2f( 1, 1 ),
			new Vector2f( 1, 0 ), };

	private final Map< String, ModelGroup >	parts			= Maps.newHashMap();

	private final List< Vector3f >			positions		= Lists.newArrayList();
	private final List< Vector2f >			texCoords		= Lists.newArrayList();
	private final List< Vector3f >			normals			= Lists.newArrayList();
	private final List< Vector4f >			colors			= Lists.newArrayList();

	public final boolean					detectCullableFaces;
	public final boolean					diffuseLighting;
	public final boolean					flipV;
	public final boolean					ambientToFullbright;

	public final ResourceLocation			modelLocation;

	@Nullable
	public final String						materialLibraryOverrideLocation;

	public XOBJModel( LineReader reader, ModelSettings settings ) throws IOException
	{
		modelLocation = settings.modelLocation;
		detectCullableFaces = settings.detectCullableFaces;
		diffuseLighting = settings.diffuseLighting;
		flipV = settings.flipV;
		ambientToFullbright = settings.ambientToFullbright;
		materialLibraryOverrideLocation = settings.materialLibraryOverrideLocation;

		// for relative references to material libraries
		final String modelDomain = modelLocation.getNamespace();
		String modelPath = modelLocation.getPath();
		final int lastSlash = modelPath.lastIndexOf( '/' );
		if( lastSlash >= 0 )
			modelPath = modelPath.substring( 0, lastSlash + 1 ); // include the '/'
		else
			modelPath = "";

		MaterialLibrary mtllib = MaterialLibrary.EMPTY;
		MaterialLibrary.Material currentMat = null;
		String currentSmoothingGroup = null;
		ModelGroup currentGroup = null;
		ModelObject currentObject = null;
		ModelMesh currentMesh = null;

		boolean objAboveGroup = false;

		if( materialLibraryOverrideLocation != null )
		{
			final String lib = materialLibraryOverrideLocation;
			if( lib.contains( ":" ) )
				mtllib = OBJLoader.INSTANCE.loadMaterialLibrary( new ResourceLocation( lib ) );
			else
				mtllib = OBJLoader.INSTANCE.loadMaterialLibrary( new ResourceLocation( modelDomain, modelPath + lib ) );
		}

		String[] line;
		while( ( line = reader.readAndSplitLine( true ) ) != null )
			switch( line[0] )
			{
			case "mtllib": // Loads material library
			{
				if( materialLibraryOverrideLocation != null )
					break;

				final String lib = line[1];
				if( lib.contains( ":" ) )
					mtllib = OBJLoader.INSTANCE.loadMaterialLibrary( new ResourceLocation( lib ) );
				else
					mtllib = OBJLoader.INSTANCE.loadMaterialLibrary( new ResourceLocation( modelDomain, modelPath + lib ) );
				break;
			}

			case "usemtl": // Sets the current material (starts new mesh)
			{
				final String mat = Strings.join( Arrays.copyOfRange( line, 1, line.length ), " " );
				final MaterialLibrary.Material newMat = mtllib.getMaterial( mat );
				if( !Objects.equals( newMat, currentMat ) )
				{
					currentMat = newMat;
					if( currentMesh != null && currentMesh.mat == null && currentMesh.faces.size() == 0 )
						currentMesh.mat = currentMat;
					else
						// Start new mesh
						currentMesh = null;
				}
				break;
			}

			case "v": // Vertex
				positions.add( OBJModel.parseVector4To3( line ) );
				break;
			case "vt": // Vertex texcoord
				texCoords.add( OBJModel.parseVector2( line ) );
				break;
			case "vn": // Vertex normal
				normals.add( OBJModel.parseVector3( line ) );
				break;
			case "vc": // Vertex color (non-standard)
				colors.add( OBJModel.parseVector4( line ) );
				break;

			case "f": // Face
			{
				if( currentMesh == null )
				{
					currentMesh = new ModelMesh( currentMat, currentSmoothingGroup );
					if( currentObject != null )
						currentObject.meshes.add( currentMesh );
					else
					{
						if( currentGroup == null )
						{
							currentGroup = new ModelGroup( "" );
							parts.put( "", currentGroup );
						}
						currentGroup.meshes.add( currentMesh );
					}
				}

				final int[][] vertices = new int[line.length - 1][];
				for( int i = 0; i < vertices.length; i++ )
				{
					final String vertexData = line[i + 1];
					final String[] vertexParts = vertexData.split( "/" );
					final int[] vertex = Arrays.stream( vertexParts ).mapToInt( num -> Strings.isNullOrEmpty( num ) ? 0 : Integer.parseInt( num ) )
							.toArray();
					if( vertex[0] < 0 )
						vertex[0] = positions.size() + vertex[0];
					else
						vertex[0]--;
					if( vertex.length > 1 )
					{
						if( vertex[1] < 0 )
							vertex[1] = texCoords.size() + vertex[1];
						else
							vertex[1]--;
						if( vertex.length > 2 )
						{
							if( vertex[2] < 0 )
								vertex[2] = normals.size() + vertex[2];
							else
								vertex[2]--;
							if( vertex.length > 3 )
								if( vertex[3] < 0 )
									vertex[3] = colors.size() + vertex[3];
								else
									vertex[3]--;
						}
					}
					vertices[i] = vertex;
				}

				currentMesh.faces.add( vertices );

				break;
			}

			case "s": // Smoothing group (starts new mesh)
			{
				final String smoothingGroup = "off".equals( line[1] ) ? null : line[1];
				if( !Objects.equals( currentSmoothingGroup, smoothingGroup ) )
				{
					currentSmoothingGroup = smoothingGroup;
					if( currentMesh != null && currentMesh.smoothingGroup == null && currentMesh.faces.size() == 0 )
						currentMesh.smoothingGroup = currentSmoothingGroup;
					else
						// Start new mesh
						currentMesh = null;
				}
				break;
			}

			case "g":
			{
				final String name = line[1];
				if( objAboveGroup )
				{
					currentObject = new ModelObject( currentGroup.name() + "/" + name );
					currentGroup.parts.put( name, currentObject );
				}
				else
				{
					currentGroup = new ModelGroup( name );
					parts.put( name, currentGroup );
					currentObject = null;
				}
				// Start new mesh
				currentMesh = null;
				break;
			}

			case "o":
			{
				final String name = line[1];
				if( objAboveGroup || currentGroup == null )
				{
					objAboveGroup = true;

					currentGroup = new ModelGroup( name );
					parts.put( name, currentGroup );
					currentObject = null;
				}
				else
				{
					currentObject = new ModelObject( currentGroup.name() + "/" + name );
					currentGroup.parts.put( name, currentObject );
				}
				// Start new mesh
				currentMesh = null;
				break;
			}
			}
	}

	@Override
	public Collection< ? extends IModelGeometryPart > getParts()
	{
		return parts.values();
	}

	@Override
	public Optional< ? extends IModelGeometryPart > getPart( String name )
	{
		return Optional.ofNullable( parts.get( name ) );
	}

	/*
	 * @Override
	 * public IBakedModel bake( IModelConfiguration owner, ModelBakery bakery, Function< Material, TextureAtlasSprite > spriteGetter,
	 * IModelTransform modelTransform, ItemOverrideList overrides, ResourceLocation modelLocation )
	 * {
	 * final TextureAtlasSprite particle = spriteGetter.apply( owner.resolveTexture( "particle" ) );
	 *
	 * final IModelBuilder< ? > builder = IModelBuilder.of( owner, overrides, particle );
	 *
	 * addQuads( owner, builder, bakery, spriteGetter, modelTransform, modelLocation );
	 *
	 * return builder.build();
	 * }
	 */

	private Pair< BakedQuad, Direction > makeQuad( int[][] indices, int tintIndex, Vector4f colorTint, Vector4f ambientColor,
			TextureAtlasSprite texture, TransformationMatrix transform )
	{
		boolean needsNormalRecalculation = false;
		for( final int[] ints : indices )
			needsNormalRecalculation |= ints.length < 3;
		Vector3f faceNormal = new Vector3f( 0, 0, 0 );
		if( needsNormalRecalculation )
		{
			final Vector3f a = positions.get( indices[0][0] );
			final Vector3f ab = positions.get( indices[1][0] );
			final Vector3f ac = positions.get( indices[2][0] );
			final Vector3f abs = ab.copy();
			abs.sub( a );
			final Vector3f acs = ac.copy();
			acs.sub( a );
			abs.cross( acs );
			abs.normalize();
			faceNormal = abs;
		}

		final Vector4f[] pos = new Vector4f[4];
		final Vector3f[] norm = new Vector3f[4];

		final BakedQuadBuilder builder = new BakedQuadBuilder( texture );

		builder.setQuadTint( tintIndex );

		Vector2f uv2 = new Vector2f( 0, 0 );
		if( ambientToFullbright )
		{
			final int fakeLight = (int)( ( ambientColor.x() + ambientColor.y() + ambientColor.z() ) * 15 / 3.0f );
			uv2 = new Vector2f( ( fakeLight << 4 ) / 32767.0f, ( fakeLight << 4 ) / 32767.0f );
			builder.setApplyDiffuseLighting( fakeLight == 0 );
		}

		final boolean hasTransform = !transform.isIdentity();
		// The incoming transform is referenced on the center of the block, but our coords are referenced on the corner
		final TransformationMatrix transformation = hasTransform ? transform.blockCenterToCorner() : transform;

		for( int i = 0; i < 4; i++ )
		{
			final int[] index = indices[Math.min( i, indices.length - 1 )];
			final Vector3f pos0 = positions.get( index[0] );
			final Vector4f position = new Vector4f( pos0 );
			final Vector2f texCoord = index.length >= 2 && texCoords.size() > 0 ? texCoords.get( index[1] ) : DEFAULT_COORDS[i];
			final Vector3f norm0 = !needsNormalRecalculation && index.length >= 3 && normals.size() > 0 ? normals.get( index[2] ) : faceNormal;
			Vector3f normal = norm0;
			final Vector4f color = index.length >= 4 && colors.size() > 0 ? colors.get( index[3] ) : COLOR_WHITE;
			if( hasTransform )
			{
				normal = norm0.copy();
				transformation.transformPosition( position );
				transformation.transformNormal( normal );
			} ;
			final Vector4f tintedColor = new Vector4f( color.x() * colorTint.x(), color.y() * colorTint.y(),
					color.z() * colorTint.z(), color.w() * colorTint.w() );
			putVertexData( builder, position, texCoord, normal, tintedColor, uv2, texture );
			pos[i] = position;
			norm[i] = normal;
		}

		builder.setQuadOrientation( Direction.getNearest( norm[0].x(), norm[0].y(), norm[0].z() ) );

		Direction cull = null;
		if( detectCullableFaces )
			if( MathHelper.equal( pos[0].x(), 0 ) && // vertex.position.x
					MathHelper.equal( pos[1].x(), 0 ) && MathHelper.equal( pos[2].x(), 0 )
					&& MathHelper.equal( pos[3].x(), 0 ) && norm[0].x() < 0 )
				cull = Direction.WEST;
			else if( MathHelper.equal( pos[0].x(), 1 ) && // vertex.position.x
					MathHelper.equal( pos[1].x(), 1 ) && MathHelper.equal( pos[2].x(), 1 )
					&& MathHelper.equal( pos[3].x(), 1 ) && norm[0].x() > 0 )
				cull = Direction.EAST;
			else if( MathHelper.equal( pos[0].z(), 0 ) && // vertex.position.z
					MathHelper.equal( pos[1].z(), 0 ) && MathHelper.equal( pos[2].z(), 0 )
					&& MathHelper.equal( pos[3].z(), 0 ) && norm[0].z() < 0 )
				cull = Direction.NORTH; // can never remember
			else if( MathHelper.equal( pos[0].z(), 1 ) && // vertex.position.z
					MathHelper.equal( pos[1].z(), 1 ) && MathHelper.equal( pos[2].z(), 1 )
					&& MathHelper.equal( pos[3].z(), 1 ) && norm[0].z() > 0 )
				cull = Direction.SOUTH;
			else if( MathHelper.equal( pos[0].y(), 0 ) && // vertex.position.y
					MathHelper.equal( pos[1].y(), 0 ) && MathHelper.equal( pos[2].y(), 0 )
					&& MathHelper.equal( pos[3].y(), 0 ) && norm[0].y() < 0 )
				cull = Direction.DOWN; // can never remember
			else if( MathHelper.equal( pos[0].y(), 1 ) && // vertex.position.y
					MathHelper.equal( pos[1].y(), 1 ) && MathHelper.equal( pos[2].y(), 1 )
					&& MathHelper.equal( pos[3].y(), 1 ) && norm[0].y() > 0 )
				cull = Direction.UP;

		return Pair.of( builder.build(), cull );
	}

	private void putVertexData( IVertexConsumer consumer, Vector4f position0, Vector2f texCoord0, Vector3f normal0, Vector4f color0, Vector2f uv2,
			TextureAtlasSprite texture )
	{
		final ImmutableList< VertexFormatElement > elements = consumer.getVertexFormat().getElements();
		for( int j = 0; j < elements.size(); j++ )
		{
			final VertexFormatElement e = elements.get( j );
			switch( e.getUsage() )
			{
			case POSITION:
				consumer.put( j, position0.x(), position0.y(), position0.z(), position0.w() );
				break;
			case COLOR:
				consumer.put( j, color0.x(), color0.y(), color0.z(), color0.w() );
				break;
			case UV:
				switch( e.getIndex() )
				{
				case 0:
					consumer.put( j, texCoord0.x, flipV ? 1 - texCoord0.y : texCoord0.y );
					break;
				case 2:
					consumer.put( j, uv2.x, uv2.y );
					break;
				default:
					consumer.put( j );
					break;
				}
				break;
			case NORMAL:
				consumer.put( j, normal0.x(), normal0.y(), normal0.z() );
				break;
			default:
				consumer.put( j );
				break;
			}
		}
	}

	public class ModelObject implements IModelGeometryPart
	{
		public final String	name;

		List< ModelMesh >	meshes	= Lists.newArrayList();

		ModelObject( String name )
		{
			this.name = name;
		}

		@Override
		public String name()
		{
			return name;
		}

		@Override
		public void addQuads( IModelConfiguration owner, IModelBuilder< ? > modelBuilder, ModelBakery bakery,
				Function< RenderMaterial, TextureAtlasSprite > spriteGetter, IModelTransform modelTransform, ResourceLocation modelLocation )
		{
			for( final ModelMesh mesh : meshes )
			{
				final MaterialLibrary.Material mat = mesh.mat;
				if( mat == null )
					continue;
				final TextureAtlasSprite texture = spriteGetter.apply( ModelLoaderRegistry.resolveTexture( mat.diffuseColorMap, owner ) );
				final int tintIndex = mat.diffuseTintIndex;
				final Vector4f colorTint = mat.diffuseColor;

				for( final int[][] face : mesh.faces )
				{
					final Pair< BakedQuad, Direction > quad = makeQuad( face, tintIndex, colorTint, mat.ambientColor, texture,
							modelTransform.getRotation() );
					if( quad.getRight() == null )
						modelBuilder.addGeneralQuad( quad.getLeft() );
					else
						modelBuilder.addFaceQuad( quad.getRight(), quad.getLeft() );
				}
			}
		}

		@Override
		public Collection< RenderMaterial > getTextures( IModelConfiguration owner, Function< ResourceLocation, IUnbakedModel > modelGetter,
				Set< com.mojang.datafixers.util.Pair< String, String > > missingTextureErrors )
		{
			return meshes.stream().map( mesh -> ModelLoaderRegistry.resolveTexture( mesh.mat.diffuseColorMap, owner ) ).collect( Collectors.toSet() );
		}
	}

	public class ModelGroup extends ModelObject
	{
		final Map< String, ModelObject > parts = Maps.newHashMap();

		ModelGroup( String name )
		{
			super( name );
		}

		public Collection< ? extends IModelGeometryPart > getParts()
		{
			return parts.values();
		}

		@Override
		public void addQuads( IModelConfiguration owner, IModelBuilder< ? > modelBuilder, ModelBakery bakery,
				Function< RenderMaterial, TextureAtlasSprite > spriteGetter, IModelTransform modelTransform, ResourceLocation modelLocation )
		{
			super.addQuads( owner, modelBuilder, bakery, spriteGetter, modelTransform, modelLocation );

			getParts().stream().filter( part -> owner.getPartVisibility( part ) )
					.forEach( part -> part.addQuads( owner, modelBuilder, bakery, spriteGetter, modelTransform, modelLocation ) );
		}

		@Override
		public Collection< RenderMaterial > getTextures( IModelConfiguration owner, Function< ResourceLocation, IUnbakedModel > modelGetter,
				Set< com.mojang.datafixers.util.Pair< String, String > > missingTextureErrors )
		{
			return meshes.stream().map( mesh -> ModelLoaderRegistry.resolveTexture( mesh.mat.diffuseColorMap, owner ) ).collect( Collectors.toSet() );
		}
	}

	private class ModelMesh
	{
		@Nullable
		public MaterialLibrary.Material	mat;
		@Nullable
		public String					smoothingGroup;
		public final List< int[][] >	faces	= Lists.newArrayList();

		public ModelMesh( @Nullable MaterialLibrary.Material currentMat, @Nullable String currentSmoothingGroup )
		{
			mat = currentMat;
			smoothingGroup = currentSmoothingGroup;
		}
	}

	public static class ModelSettings
	{
		@Nonnull
		public final ResourceLocation	modelLocation;
		public final boolean			detectCullableFaces;
		public final boolean			diffuseLighting;
		public final boolean			flipV;
		public final boolean			ambientToFullbright;
		@Nullable
		public final String				materialLibraryOverrideLocation;

		public ModelSettings( @Nonnull ResourceLocation modelLocation, boolean detectCullableFaces, boolean diffuseLighting, boolean flipV,
				boolean ambientToFullbright, @Nullable String materialLibraryOverrideLocation )
		{
			this.modelLocation = modelLocation;
			this.detectCullableFaces = detectCullableFaces;
			this.diffuseLighting = diffuseLighting;
			this.flipV = flipV;
			this.ambientToFullbright = ambientToFullbright;
			this.materialLibraryOverrideLocation = materialLibraryOverrideLocation;
		}

		@Override
		public boolean equals( Object o )
		{
			if( this == o )
				return true;
			if( o == null || getClass() != o.getClass() )
				return false;
			final ModelSettings that = (ModelSettings)o;
			return equals( that );
		}

		public boolean equals( @Nonnull ModelSettings that )
		{
			return detectCullableFaces == that.detectCullableFaces && diffuseLighting == that.diffuseLighting && flipV == that.flipV
					&& ambientToFullbright == that.ambientToFullbright && modelLocation.equals( that.modelLocation )
					&& Objects.equals( materialLibraryOverrideLocation, that.materialLibraryOverrideLocation );
		}

		@Override
		public int hashCode()
		{
			return Objects.hash( modelLocation, detectCullableFaces, diffuseLighting, flipV, ambientToFullbright, materialLibraryOverrideLocation );
		}
	}
}
