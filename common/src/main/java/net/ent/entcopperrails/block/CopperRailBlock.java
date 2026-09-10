package net.ent.entcopperrails.block;

import org.jetbrains.annotations.Nullable;

import java.util.Optional;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.PoweredRailBlock;
import net.minecraft.world.level.block.BaseRailBlock;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.minecraft.world.level.redstone.Orientation;

public class CopperRailBlock extends BaseRailBlock implements WeatheringCopper {

    public static final EnumProperty<RailShape> SHAPE = BlockStateProperties.RAIL_SHAPE_STRAIGHT;
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    public static final MapCodec<CopperRailBlock> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    WeatheringCopper.WeatherState.CODEC.fieldOf("weathering_state").forGetter(CopperRailBlock::getAge),
                    propertiesCodec()
            ).apply(instance, CopperRailBlock::new));

    private final WeatheringCopper.WeatherState weatherState;

    public CopperRailBlock(WeatheringCopper.WeatherState weatherState, Properties properties) {
        super(true, properties);
        this.weatherState = weatherState;
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(SHAPE, RailShape.NORTH_SOUTH)
                .setValue(POWERED, Boolean.FALSE)
                .setValue(WATERLOGGED, Boolean.FALSE));
    }

    public int getSpeedDelta() {
        return switch (this.weatherState) {
            case UNAFFECTED -> 8;
            case EXPOSED -> 4;
            case WEATHERED -> 2;
            case OXIDIZED -> 1;
        };
    }

    @Override
    public Property<RailShape> getShapeProperty() {
        return SHAPE;
    }

    @Override
    public WeatheringCopper.WeatherState getAge() {
        return this.weatherState;
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        Optional<Block> next = WeatheringCopper.getNext(state.getBlock());
        if (next.isEmpty()) {
            return;
        }
        float modifier = this.weatherState == WeatheringCopper.WeatherState.UNAFFECTED ? 0.75F : 1.0F;
        if (random.nextFloat() < 0.05688889F * modifier) {
            level.setBlockAndUpdate(pos, next.get().withPropertiesOf(state));
        }
    }

    @Override
    protected boolean isRandomlyTicking(BlockState state) {
        return WeatheringCopper.getNext(state.getBlock()).isPresent();
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState placed = super.getStateForPlacement(context);
        if (placed == null) {
            return null;
        }
        return placed.setValue(POWERED, this.isConnectedToPower(context.getLevel(), context.getClickedPos(), placed));
    }

    @Override
    protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighborBlock,
                                   @Nullable Orientation orientation, boolean movedByPiston) {
        super.neighborChanged(state, level, pos, neighborBlock, orientation, movedByPiston);
        BlockState current = level.getBlockState(pos);
        if (current.getBlock() instanceof CopperRailBlock || current.getBlock() instanceof PoweredRailBlock) { //Test for Powered Rail Plez
            boolean want = this.isConnectedToPower(level, pos, current);
            if (want != current.getValue(POWERED)) {
                level.setBlock(pos, current.setValue(POWERED, want), Block.UPDATE_ALL);
            }
        }
    }

    private boolean isConnectedToPower(Level level, BlockPos pos, BlockState state) {
        return level.hasNeighborSignal(pos)
                || this.findPoweredRailConnected(level, pos, state, true, 0)
                || this.findPoweredRailConnected(level, pos, state, false, 0);
    }

    private boolean findPoweredRailConnected(Level level, BlockPos pos, BlockState state, boolean forward, int depth) {
        if (depth >= 8) {
            return false;
        }
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();
        boolean checkBelow = true;
        RailShape shape = state.getValue(SHAPE);
        switch (shape) {
            case NORTH_SOUTH -> z += forward ? 1 : -1;
            case EAST_WEST -> x += forward ? -1 : 1;
            case ASCENDING_EAST -> {
                if (forward) {
                    x--;
                } else {
                    x++;
                    y++;
                    checkBelow = false;
                }
                shape = RailShape.EAST_WEST;
            }
            case ASCENDING_WEST -> {
                if (forward) {
                    x--;
                    y++;
                    checkBelow = false;
                } else {
                    x++;
                }
                shape = RailShape.EAST_WEST;
            }
            case ASCENDING_NORTH -> {
                if (forward) {
                    z++;
                } else {
                    z--;
                    y++;
                    checkBelow = false;
                }
                shape = RailShape.NORTH_SOUTH;
            }
            case ASCENDING_SOUTH -> {
                if (forward) {
                    z++;
                    y++;
                    checkBelow = false;
                } else {
                    z--;
                }
                shape = RailShape.NORTH_SOUTH;
            }
            default -> {
                return false;
            }
        }
        if (this.isSameRailWithPower(level, new BlockPos(x, y, z), forward, depth, shape)) {
            return true;
        }
        return checkBelow && this.isSameRailWithPower(level, new BlockPos(x, y - 1, z), forward, depth, shape);
    }

    private boolean isSameRailWithPower(Level level, BlockPos pos, boolean forward, int depth, RailShape expected) {
        BlockState state = level.getBlockState(pos);
        if (!(state.getBlock() instanceof CopperRailBlock)) {
            return false;
        }
        RailShape shape = state.getValue(SHAPE);
        if (expected == RailShape.EAST_WEST
                && (shape == RailShape.NORTH_SOUTH || shape == RailShape.ASCENDING_NORTH || shape == RailShape.ASCENDING_SOUTH)) {
            return false;
        }
        if (expected == RailShape.NORTH_SOUTH
                && (shape == RailShape.EAST_WEST || shape == RailShape.ASCENDING_EAST || shape == RailShape.ASCENDING_WEST)) {
            return false;
        }
        if (level.hasNeighborSignal(pos)) {
            return true;
        }
        return this.findPoweredRailConnected(level, pos, state, forward, depth + 1);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(SHAPE, POWERED, WATERLOGGED);
    }

    @Override
    protected MapCodec<? extends BaseRailBlock> codec() {
        return CODEC;
    }
}
