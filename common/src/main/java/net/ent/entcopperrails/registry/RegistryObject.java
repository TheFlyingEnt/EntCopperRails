package net.ent.entcopperrails.registry;

import java.util.function.Supplier;

import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;

public interface RegistryObject<T> extends Supplier<T> {

    ResourceKey<T> getResourceKey();

    Identifier getId();

    @Override
    T get();
}
