package net.ent.entcopperrails.registry;

import net.ent.entcopperrails.Constants;
import net.ent.entcopperrails.block.CopperRailBlock;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.WeatheringCopper.WeatherState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;

public final class ModBlocks {

    public static final RegistrationProvider<Block> BLOCKS = RegistrationProvider.get(Registries.BLOCK, Constants.MOD_ID);

    public static final RegistryObject<CopperRailBlock> COPPER_RAIL =
            rail("copper_rail", WeatherState.UNAFFECTED, true);
    public static final RegistryObject<CopperRailBlock> EXPOSED_COPPER_RAIL =
            rail("exposed_copper_rail", WeatherState.EXPOSED, true);
    public static final RegistryObject<CopperRailBlock> WEATHERED_COPPER_RAIL =
            rail("weathered_copper_rail", WeatherState.WEATHERED, true);
    public static final RegistryObject<CopperRailBlock> OXIDIZED_COPPER_RAIL =
            rail("oxidized_copper_rail", WeatherState.OXIDIZED, true);

    public static final RegistryObject<CopperRailBlock> WAXED_COPPER_RAIL =
            rail("waxed_copper_rail", WeatherState.UNAFFECTED, false);
    public static final RegistryObject<CopperRailBlock> WAXED_EXPOSED_COPPER_RAIL =
            rail("waxed_exposed_copper_rail", WeatherState.EXPOSED, false);
    public static final RegistryObject<CopperRailBlock> WAXED_WEATHERED_COPPER_RAIL =
            rail("waxed_weathered_copper_rail", WeatherState.WEATHERED, false);
    public static final RegistryObject<CopperRailBlock> WAXED_OXIDIZED_COPPER_RAIL =
            rail("waxed_oxidized_copper_rail", WeatherState.OXIDIZED, false);

    private static RegistryObject<CopperRailBlock> rail(String name, WeatherState weather, boolean weathers) {
        return BLOCKS.register(name, () -> {
            ResourceKey<Block> key = ResourceKey.create(Registries.BLOCK,
                    Identifier.fromNamespaceAndPath(Constants.MOD_ID, name));
            BlockBehaviour.Properties props = BlockBehaviour.Properties.of()
                    .mapColor(mapColor(weather))
                    .noCollision()
                    .strength(0.7F)
                    .sound(SoundType.COPPER)
                    .setId(key);
            if (weathers) {
                props = props.randomTicks();
            }
            return new CopperRailBlock(weather, props);
        });
    }

    private static MapColor mapColor(WeatherState weather) {
        return switch (weather) {
            case UNAFFECTED -> MapColor.COLOR_ORANGE;
            case EXPOSED -> MapColor.TERRACOTTA_LIGHT_GRAY;
            case WEATHERED -> MapColor.WARPED_STEM;
            case OXIDIZED -> MapColor.WARPED_NYLIUM;
        };
    }

    public static void init() {}

    private ModBlocks() {}
}
