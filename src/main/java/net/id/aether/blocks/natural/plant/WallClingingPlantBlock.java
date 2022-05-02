package net.id.aether.blocks.natural.plant;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.PlantBlock;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.tag.Tag;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;

public class WallClingingPlantBlock extends PlantBlock {

    public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;
    private final @Nullable Tag<Block> clingableBlocks;

    public WallClingingPlantBlock(Settings settings, @Nullable Tag<Block> clingableBlocks) {
        super(settings);
        this.clingableBlocks = clingableBlocks;
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        var wall = pos.offset(state.get(FACING));
        return canClingTo(world.getBlockState(wall));
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        var side = ctx.getSide().getOpposite();
        if(side.getHorizontal() >= 0) {
            return getDefaultState().with(FACING, ctx.getSide().getOpposite());
        }
        return null;
    }

    @Override
    protected boolean canPlantOnTop(BlockState floor, BlockView world, BlockPos pos) {
        return true;
    }

    public boolean canClingTo(BlockState state) {
        return clingableBlocks == null || state.isIn(clingableBlocks);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(FACING);
    }
}
