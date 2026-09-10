package net.ent.entcopperrails;

import net.ent.entcopperrails.registry.ModBlocks;
import net.ent.entcopperrails.registry.ModItems;
import net.ent.entcopperrails.registry.RegistryObject;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.fabricmc.fabric.api.registry.OxidizableBlocksRegistry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;

public class EntCopperRails implements ModInitializer {

    private static final ResourceKey<CreativeModeTab> REDSTONE_TAB = ResourceKey.create(
            Registries.CREATIVE_MODE_TAB, Identifier.fromNamespaceAndPath("minecraft", "redstone_blocks"));
    private static final ResourceKey<CreativeModeTab> TOOLS_TAB = ResourceKey.create(
            Registries.CREATIVE_MODE_TAB, Identifier.fromNamespaceAndPath("minecraft", "tools_and_utilities"));

    @Override
    public void onInitialize() {
        CommonClass.init();
        registerCopperBehavior();
        registerCreativeTabContents();
        Constants.LOG.info("[{}] Fabric init complete", Constants.MOD_NAME);
    }

    private static void registerCopperBehavior() {
        OxidizableBlocksRegistry.registerNextStage(
                ModBlocks.COPPER_RAIL.get(), ModBlocks.EXPOSED_COPPER_RAIL.get());
        OxidizableBlocksRegistry.registerNextStage(
                ModBlocks.EXPOSED_COPPER_RAIL.get(), ModBlocks.WEATHERED_COPPER_RAIL.get());
        OxidizableBlocksRegistry.registerNextStage(
                ModBlocks.WEATHERED_COPPER_RAIL.get(), ModBlocks.OXIDIZED_COPPER_RAIL.get());

        OxidizableBlocksRegistry.registerWaxable(
                ModBlocks.COPPER_RAIL.get(), ModBlocks.WAXED_COPPER_RAIL.get());
        OxidizableBlocksRegistry.registerWaxable(
                ModBlocks.EXPOSED_COPPER_RAIL.get(), ModBlocks.WAXED_EXPOSED_COPPER_RAIL.get());
        OxidizableBlocksRegistry.registerWaxable(
                ModBlocks.WEATHERED_COPPER_RAIL.get(), ModBlocks.WAXED_WEATHERED_COPPER_RAIL.get());
        OxidizableBlocksRegistry.registerWaxable(
                ModBlocks.OXIDIZED_COPPER_RAIL.get(), ModBlocks.WAXED_OXIDIZED_COPPER_RAIL.get());
    }
    
    private static void registerCreativeTabContents() {
        CreativeModeTabEvents.ModifyOutput adder = output -> {
            if (output.getDisplayStacks().stream().anyMatch(stack -> stack.is(Items.ACTIVATOR_RAIL))) {
                ItemLike[] rails = ModItems.ALL.stream().map(RegistryObject::get).toArray(ItemLike[]::new);
                output.insertAfter(Items.ACTIVATOR_RAIL, rails);
            }
        };
        CreativeModeTabEvents.modifyOutputEvent(REDSTONE_TAB).register(adder);
        CreativeModeTabEvents.modifyOutputEvent(TOOLS_TAB).register(adder);
    }
}
