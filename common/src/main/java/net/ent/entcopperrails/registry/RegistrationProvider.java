package net.ent.entcopperrails.registry;

import java.util.ServiceLoader;
import java.util.function.Supplier;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public interface RegistrationProvider<T> {

    Factory FACTORY = ServiceLoader.load(Factory.class).findFirst().orElseThrow(() -> new IllegalStateException("No RegistrationProvider.Factory service was found"));

    static <T> RegistrationProvider<T> get(ResourceKey<? extends Registry<T>> registryKey, String modId) {
        return FACTORY.create(registryKey, modId);
    }

    <R extends T> RegistryObject<R> register(String name, Supplier<R> supplier);

    interface Factory {
        <T> RegistrationProvider<T> create(ResourceKey<? extends Registry<T>> registryKey, String modId);
    }
}
