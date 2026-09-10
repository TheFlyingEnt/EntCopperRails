package net.ent.entcopperrails.registry;

import java.util.function.Supplier;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;

public final class FabricRegistrationProvider<T> implements RegistrationProvider<T> {

    private final ResourceKey<? extends Registry<T>> registryKey;
    private final Registry<T> registry;
    private final String modId;

    private FabricRegistrationProvider(ResourceKey<? extends Registry<T>> registryKey, Registry<T> registry, String modId) {
        this.registryKey = registryKey;
        this.registry = registry;
        this.modId = modId;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <R extends T> RegistryObject<R> register(String name, Supplier<R> supplier) {
        Identifier id = Identifier.fromNamespaceAndPath(this.modId, name);
        ResourceKey<R> key = (ResourceKey<R>) ResourceKey.create(this.registryKey, id);
        R value = Registry.register(this.registry, id, supplier.get());
        return new FabricRegistryObject<>(key, id, value);
    }

    public static final class Factory implements RegistrationProvider.Factory {
        @Override
        @SuppressWarnings("unchecked")
        public <T> RegistrationProvider<T> create(ResourceKey<? extends Registry<T>> registryKey, String modId) {
            Registry<T> registry = (Registry<T>) BuiltInRegistries.REGISTRY.getValue(registryKey.identifier());
            if (registry == null) {
                throw new IllegalStateException("No registry found for " + registryKey.identifier());
            }
            return new FabricRegistrationProvider<>(registryKey, registry, modId);
        }
    }

    private record FabricRegistryObject<T>(ResourceKey<T> getResourceKey, Identifier getId, T value)
            implements RegistryObject<T> {
        @Override
        public T get() {
            return this.value;
        }
    }
}
