package net.ent.entcopperrails.registry;

import java.util.List;

import net.ent.entcopperrails.Constants;
import net.ent.entcopperrails.block.CopperRailBlock;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;

public final class ModItems {

    public static final RegistrationProvider<Item> ITEMS = RegistrationProvider.get(Registries.ITEM, Constants.MOD_ID);

    public static final RegistryObject<Item> COPPER_RAIL = blockItem(ModBlocks.COPPER_RAIL);
    public static final RegistryObject<Item> EXPOSED_COPPER_RAIL = blockItem(ModBlocks.EXPOSED_COPPER_RAIL);
    public static final RegistryObject<Item> WEATHERED_COPPER_RAIL = blockItem(ModBlocks.WEATHERED_COPPER_RAIL);
    public static final RegistryObject<Item> OXIDIZED_COPPER_RAIL = blockItem(ModBlocks.OXIDIZED_COPPER_RAIL);
    public static final RegistryObject<Item> WAXED_COPPER_RAIL = blockItem(ModBlocks.WAXED_COPPER_RAIL);
    public static final RegistryObject<Item> WAXED_EXPOSED_COPPER_RAIL = blockItem(ModBlocks.WAXED_EXPOSED_COPPER_RAIL);
    public static final RegistryObject<Item> WAXED_WEATHERED_COPPER_RAIL = blockItem(ModBlocks.WAXED_WEATHERED_COPPER_RAIL);
    public static final RegistryObject<Item> WAXED_OXIDIZED_COPPER_RAIL = blockItem(ModBlocks.WAXED_OXIDIZED_COPPER_RAIL);

    public static final List<RegistryObject<Item>> ALL = List.of(
        COPPER_RAIL, EXPOSED_COPPER_RAIL, WEATHERED_COPPER_RAIL, OXIDIZED_COPPER_RAIL,
        WAXED_COPPER_RAIL, WAXED_EXPOSED_COPPER_RAIL, WAXED_WEATHERED_COPPER_RAIL, WAXED_OXIDIZED_COPPER_RAIL
    );

    private static RegistryObject<Item> blockItem(RegistryObject<CopperRailBlock> block) {
        String name = block.getId().getPath();
        return ITEMS.register(name, () -> {
            ResourceKey<Item> key = ResourceKey.create(Registries.ITEM,
                    Identifier.fromNamespaceAndPath(Constants.MOD_ID, name));
            return new BlockItem(block.get(), new Item.Properties().setId(key));
        });
    }

    public static void init() {}

    private ModItems() {}
}
