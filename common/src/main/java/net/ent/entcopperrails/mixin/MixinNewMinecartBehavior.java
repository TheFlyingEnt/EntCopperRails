package net.ent.entcopperrails.mixin;

import net.ent.entcopperrails.entity.MaxSpeedHolder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.vehicle.minecart.AbstractMinecart;
import net.minecraft.world.entity.vehicle.minecart.NewMinecartBehavior;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(NewMinecartBehavior.class)
public abstract class MixinNewMinecartBehavior {

    @Inject(method = "getMaxSpeed", at = @At("HEAD"), cancellable = true)
    private void entcopperrails$perCartMaxSpeed(ServerLevel level, CallbackInfoReturnable<Double> cir) {
        AbstractMinecart cart = ((MinecartBehaviorAccessor) (Object) this).entcopperrails$getMinecart();
        double speed = ((MaxSpeedHolder) cart).entcopperrails$getMaxSpeedBps() / 20.0D;
        if (cart.isInWater()) {
            speed *= 0.5D; // match vanillas in water halving
        }
        cir.setReturnValue(speed);
    }
}
