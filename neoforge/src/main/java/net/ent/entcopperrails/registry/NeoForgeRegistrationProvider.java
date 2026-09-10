package net.ent.entcopperrails.registry;

import java.util.function.Supplier;

import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class NeoForgeRegistrationProvider<T> implements RegistrationProvider<T> {

    public static IEventBus modEventBus;

    private final DeferredRegister<T> deferredRegister;

    private NeoForgeRegistrationProvider(DeferredRegister<T> deferredRegister) {
        this.deferredRegister = deferredRegister;
    }

    @Override
    public <R extends T> RegistryObject<R> register(String name, Supplier<R> supplier) {
        DeferredHolder<T, R> holder = this.deferredRegister.register(name, supplier);
        return new NeoForgeRegistryObject<>(holder);
    }

    public static final class Factory implements RegistrationProvider.Factory {
        @Override
        public <T> RegistrationProvider<T> create(ResourceKey<? extends Registry<T>> registryKey, String modId) {
            if (modEventBus == null) {
                throw new IllegalStateException("NeoForge mod event bus was not captured before registration");
            }
            DeferredRegister<T> register = DeferredRegister.create(registryKey, modId);
            register.register(modEventBus);
            return new NeoForgeRegistrationProvider<>(register);
        }
    }

    private record NeoForgeRegistryObject<T, R extends T>(DeferredHolder<T, R> holder) implements RegistryObject<R> {
        @Override
        @SuppressWarnings("unchecked")
        public ResourceKey<R> getResourceKey() {
            return (ResourceKey<R>) this.holder.getKey();
        }

        @Override
        public Identifier getId() {
            return this.holder.getId();
        }

        @Override
        public R get() {
            return this.holder.get();
        }
    }
}
