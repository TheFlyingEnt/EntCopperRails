package net.ent.entcopperrails.mixin;

import net.minecraft.world.entity.vehicle.minecart.AbstractMinecart;
import net.minecraft.world.entity.vehicle.minecart.MinecartBehavior;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(MinecartBehavior.class)
public interface MinecartBehaviorAccessor {

    @Accessor("minecart")
    AbstractMinecart entcopperrails$getMinecart();
}
