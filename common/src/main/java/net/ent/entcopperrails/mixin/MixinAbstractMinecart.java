package net.ent.entcopperrails.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;

import net.ent.entcopperrails.Constants;
import net.ent.entcopperrails.block.CopperRailBlock;
import net.ent.entcopperrails.entity.MaxSpeedHolder;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.vehicle.minecart.AbstractMinecart;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractMinecart.class)
public abstract class MixinAbstractMinecart implements MaxSpeedHolder {

    @Unique
    private int entcopperrails$maxSpeedBps = Constants.DEFAULT_SPEED_BPS;

    @Unique
    private BlockPos entcopperrails$lastRailPos = null;

    @Override
    public int entcopperrails$getMaxSpeedBps() {
        return this.entcopperrails$maxSpeedBps;
    }

    @Override
    public void entcopperrails$setMaxSpeedBps(int bps) {
        this.entcopperrails$maxSpeedBps = Mth.clamp(bps, Constants.MIN_SPEED_BPS, Constants.MAX_SPEED_BPS);
    }

    @ModifyReturnValue(method = "getMaxSpeed", at = @At("RETURN"))
    private double entcopperrails$capMaxSpeed(double original) {
        return this.entcopperrails$maxSpeedBps / 20.0D;
    }

    //I cant fricken figure out how to do this without enabling the useExperimentalMovement. Might make my own???
    @ModifyReturnValue(method = "useExperimentalMovement", at = @At("RETURN"))
    private static boolean entcopperrails$forceNewMovement(boolean original) {
        return true;
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void entcopperrails$applyCopperRail(CallbackInfo ci) {
        AbstractMinecart self = (AbstractMinecart) (Object) this;
        Level level = self.level();

        BlockPos pos = self.blockPosition();
        BlockState state = level.getBlockState(pos);
        if (!(state.getBlock() instanceof CopperRailBlock)) {
            BlockPos below = pos.below();
            BlockState belowState = level.getBlockState(below);
            if (belowState.getBlock() instanceof CopperRailBlock) {
                pos = below;
                state = belowState;
            } else {
                this.entcopperrails$lastRailPos = null;
                return;
            }
        }

        if (pos.equals(this.entcopperrails$lastRailPos)) {
            return;
        }
        this.entcopperrails$lastRailPos = pos.immutable();

        CopperRailBlock rail = (CopperRailBlock) state.getBlock();
        boolean powered = state.getValue(CopperRailBlock.POWERED);
        int delta = powered ? rail.getSpeedDelta() : -rail.getSpeedDelta();
        this.entcopperrails$setMaxSpeedBps(this.entcopperrails$maxSpeedBps + delta);
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void entcopperrails$save(ValueOutput output, CallbackInfo ci) {
        output.putInt("entcopperrails:MaxSpeedBps", this.entcopperrails$maxSpeedBps);
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void entcopperrails$load(ValueInput input, CallbackInfo ci) {
        this.entcopperrails$setMaxSpeedBps(
                input.getIntOr("entcopperrails:MaxSpeedBps", Constants.DEFAULT_SPEED_BPS));
    }
}
