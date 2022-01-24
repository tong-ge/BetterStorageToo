package io.github.tehstoneman.betterstorage.client.renderer.tileentity;


//@OnlyIn( Dist.CLIENT )
public class TileEntityLockerRenderer// extends BlockEntityRenderer< TileEntityLocker >
{
/*	private final ModelLocker				simpleLocker	= new ModelLocker();
	private final ModelLocker				largeLocker		= new ModelLargeLocker();
	private HexKeyConfig					config;

	private ItemRenderer					itemRenderer;
	private static BlockRendererDispatcher	blockRenderer;
	private static ModelManager				modelManager;

	public TileEntityLockerRenderer( BlockEntityRenderDispatcher rendererDispatcher )
	{
		super( rendererDispatcher );
	}

	@Override
	public void render( TileEntityLocker tileEntity, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int combinedLight,
			int combinedOverlay )
	{
		final BlockState blockState = tileEntity.hasLevel() ? tileEntity.getBlockState()
				: BetterStorageBlocks.LOCKER.get().defaultBlockState().setValue( BlockLocker.FACING, Direction.SOUTH );
		// blockState.hasProperty() <- blockState.has()
		final ConnectedType lockerType = blockState.hasProperty( BlockConnectableContainer.TYPE )
				? blockState.getValue( BlockConnectableContainer.TYPE )
				: ConnectedType.SINGLE;
		final DoorHingeSide hingeSide = blockState.hasProperty( BlockStateProperties.DOOR_HINGE )
				? blockState.getValue( BlockStateProperties.DOOR_HINGE )
				: DoorHingeSide.LEFT;
		final Direction facing = blockState.getValue( BlockReinforcedChest.FACING );

		if( lockerType != ConnectedType.SLAVE )
		{
			if( tileEntity instanceof TileEntityReinforcedLocker )
				config = ( (TileEntityReinforcedLocker)tileEntity ).getConfig();
			final boolean flag = lockerType != ConnectedType.SINGLE;
			if( blockRenderer == null )
				blockRenderer = Minecraft.getInstance().getBlockRenderer();
			if( modelManager == null )
				modelManager = Minecraft.getInstance().getModelManager();

			matrixStack.pushPose();

			final float f = facing.toYRot();
			matrixStack.translate( 0.5, 0.5, 0.5 );
			matrixStack.mulPose( Vector3f.YP.rotationDegrees( -f ) );
			matrixStack.translate( -0.5, -0.5, -0.5 );

			if( tileEntity instanceof TileEntityReinforcedLocker && BetterStorageConfig.CLIENT.useObjModels.get() )
			{
				final IBakedModel modelBase = modelManager
						.getModel( lockerType == ConnectedType.SINGLE ? Resources.MODEL_REINFORCED_LOCKER : Resources.MODEL_REINFORCED_LOCKER_LARGE );
				final IBakedModel modelDoor = modelManager.getModel( lockerType == ConnectedType.SINGLE
						? hingeSide == DoorHingeSide.LEFT ? Resources.MODEL_REINFORCED_LOCKER_DOOR_L : Resources.MODEL_REINFORCED_LOCKER_DOOR_R
						: hingeSide == DoorHingeSide.LEFT ? Resources.MODEL_REINFORCED_LOCKER_DOOR_LARGE_L
								: Resources.MODEL_REINFORCED_LOCKER_DOOR_LARGE_R );
				final IBakedModel modelFrame = modelManager.getModel( lockerType == ConnectedType.SINGLE ? Resources.MODEL_REINFORCED_LOCKER_FRAME
						: Resources.MODEL_REINFORCED_LOCKER_LARGE_FRAME );
				final IBakedModel modelDoorFrame = modelManager.getModel( lockerType == ConnectedType.SINGLE
						? hingeSide == DoorHingeSide.LEFT ? Resources.MODEL_REINFORCED_LOCKER_DOOR_FRAME_L
								: Resources.MODEL_REINFORCED_LOCKER_DOOR_FRAME_R
						: hingeSide == DoorHingeSide.LEFT ? Resources.MODEL_REINFORCED_LOCKER_DOOR_LARGE_FRAME_L
								: Resources.MODEL_REINFORCED_LOCKER_DOOR_LARGE_FRAME_R );

				final Level world = tileEntity.getLevel();
				final BlockPos pos = tileEntity.getBlockPos();
				// final ChunkRenderCache lightReader = MinecraftForgeClient.getRegionRenderCache( world, pos );
				// final long random = blockState.getBlockPositionRandom( pos );
				final VertexConsumer renderBufferLocker = buffer.getBuffer( Atlases.solidBlockSheet() );

				Material material = new Material( InventoryMenu.BLOCK_ATLAS, Resources.TEXTURE_REINFORCED_FRAME );
				final ItemStack itemStack = config.getStackInSlot( HexKeyConfig.SLOT_APPEARANCE );
				if( !itemStack.isEmpty() )
				{
					final Item item = itemStack.getItem();
					if( item instanceof BlockItem )
					{
						final BlockState state = ( (BlockItem)item ).getBlock().defaultBlockState();
						final TextureAtlasSprite texture = Minecraft.getInstance().getBlockRenderer().getBlockModelShaper().getTexture( state, world,
								pos );
						material = new Material( InventoryMenu.BLOCK_ATLAS, texture.getName() );
					}
				}
				final VertexConsumer renderBufferFrame = material.buffer( buffer, RenderType::entitySolid );
				final IModelData data = modelBase.getModelData( world, pos, blockState, ModelDataManager.getModelData( world, pos ) );

				final PoseStack.Entry currentMatrix = matrixStack.last();

				blockRenderer.getModelRenderer().renderModel( currentMatrix, renderBufferLocker, null, modelBase, 1.0f, 1.0f, 1.0f, combinedLight,
						combinedOverlay, data );
				blockRenderer.getModelRenderer().renderModel( currentMatrix, renderBufferFrame, null, modelFrame, 1.0f, 1.0f, 1.0f, combinedLight,
						combinedOverlay, data );

				rotateDoor( tileEntity, partialTicks, matrixStack, hingeSide );

				blockRenderer.getModelRenderer().renderModel( currentMatrix, renderBufferLocker, null, modelDoor, 1.0f, 1.0f, 1.0f, combinedLight,
						combinedOverlay, data );
				blockRenderer.getModelRenderer().renderModel( currentMatrix, renderBufferFrame, null, modelDoorFrame, 1.0f, 1.0f, 1.0f, combinedLight,
						combinedOverlay, data );
				matrixStack.translate( hingeSide == DoorHingeSide.LEFT ? 0.0 : -1.0, 0.0, -0.8125 );
			}
			else
			{
				final ModelLocker modelLocker = getLockerModel( tileEntity, flag );
				final Material material = getMaterial( tileEntity, flag );
				final VertexConsumer vertexBuilder = material.buffer( buffer, RenderType::entityCutout );

				rotateDoor( tileEntity, partialTicks, modelLocker, hingeSide );
				modelLocker.renderToBuffer( matrixStack, vertexBuilder, combinedLight, combinedOverlay, 1.0F, 1.0F, 1.0F, 1.0F,
						hingeSide == DoorHingeSide.LEFT );
			}
			if( tileEntity instanceof TileEntityReinforcedLocker )
				renderItem( (TileEntityReinforcedLocker)tileEntity, partialTicks, matrixStack, buffer, combinedLight, blockState );

			matrixStack.popPose();
		}
	}

	private ModelLocker getLockerModel( TileEntityLocker tileEntityLocker, boolean flag )
	{
		return flag ? largeLocker : simpleLocker;
	}

	protected Material getMaterial( TileEntityLocker tileEntity, boolean flag )
	{
		final ResourceLocation resourcelocation;
		if( tileEntity instanceof TileEntityReinforcedLocker )
			resourcelocation = flag ? Resources.TEXTURE_LOCKER_REINFORCED_DOUBLE : Resources.TEXTURE_LOCKER_REINFORCED;
		else
			resourcelocation = flag ? Resources.TEXTURE_LOCKER_NORMAL_DOUBLE : Resources.TEXTURE_LOCKER_NORMAL;
		return new Material( InventoryMenu.BLOCK_ATLAS, resourcelocation );
		// return null;
	}

	private void rotateDoor( TileEntityLocker tileEntityLocker, float partialTicks, ModelLocker modelLocker, DoorHingeSide hingeSide )
	{
		float angle = ( (LidBlockEntity)tileEntityLocker ).getOpenNess( partialTicks );
		angle = 1.0F - angle;
		angle = 1.0F - angle * angle * angle;
		modelLocker.rotateDoor( angle, hingeSide == DoorHingeSide.LEFT );
	}

	private void rotateDoor( TileEntityLocker tileEntityLocker, float partialTicks, PoseStack matrixStack, DoorHingeSide hingeSide )
	{
		float angle = ( (LidBlockEntity)tileEntityLocker ).getOpenNess( partialTicks );
		angle = 1.0F - angle;
		angle = 1.0F - angle * angle * angle;
		matrixStack.translate( hingeSide == DoorHingeSide.LEFT ? 0.0 : 1.0, 0.0, 0.8125 );
		matrixStack.mulPose( Vector3f.YP.rotation( hingeSide == DoorHingeSide.LEFT ? -angle : angle ) );
	}*/

	/**
	 * Renders attached lock on chest. Adapted from vanilla item frame
	 *
	 * @param locker
	 * @param partialTicks
	 * @param matrixStack
	 * @param buffer
	 * @param packedLight
	 * @param state
	 */
	/*private void renderItem( TileEntityReinforcedLocker locker, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer,
			int packedLight, BlockState state )
	{
		final ItemStack itemstack = locker.getLock();

		if( !itemstack.isEmpty() )
		{
			if( itemRenderer == null )
				itemRenderer = Minecraft.getInstance().getItemRenderer();

			float openAngle = ( (LidBlockEntity)locker ).getOpenNess( partialTicks );
			openAngle = 1.0F - openAngle;
			openAngle = 1.0F - openAngle * openAngle * openAngle;

			final boolean left = state.getValue( DoorBlock.HINGE ) == DoorHingeSide.LEFT;

			matrixStack.translate( 0.0, 0.0, 0.8125 );

			// matrixStack.translate( left ? 0.0 : 1.0, 0.0, 0.0 );
			// matrixStack.rotate( Vector3f.YP.rotationDegrees( left ? -openAngle * 90 : openAngle * 90 ) );
			// matrixStack.translate( left ? -0.0 : -1.0, 0.0, 0.0 );

			matrixStack.translate( left ? 0.8125 : 0.1875, locker.isConnected() ? 0.875 : 0.375, 0.125 );
			matrixStack.scale( 0.5F, 0.5F, 0.5F );
			itemRenderer.renderStatic( itemstack, ItemCameraTransforms.TransformType.FIXED, packedLight, OverlayTexture.NO_OVERLAY, matrixStack,
					buffer );
		}
	}*/
}
