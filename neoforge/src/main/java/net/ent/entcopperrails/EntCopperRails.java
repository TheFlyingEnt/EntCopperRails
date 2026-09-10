package net.ent.entcopperrails;

import java.util.List;

import net.ent.entcopperrails.registry.ModItems;
import net.ent.entcopperrails.registry.NeoForgeRegistrationProvider;
import net.ent.entcopperrails.registry.RegistryObject;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.ent.entcopperrails.client.EntCopperRailsClient;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;

@Mod(Constants.MOD_ID)
public class EntCopperRails {

    private static final ResourceKey<CreativeModeTab> REDSTONE_TAB = ResourceKey.create(
            Registries.CREATIVE_MODE_TAB, Identifier.fromNamespaceAndPath("minecraft", "redstone_blocks"));
    private static final ResourceKey<CreativeModeTab> TOOLS_TAB = ResourceKey.create(
            Registries.CREATIVE_MODE_TAB, Identifier.fromNamespaceAndPath("minecraft", "tools_and_utilities"));

    public EntCopperRails(IEventBus modEventBus, Dist dist) {
        NeoForgeRegistrationProvider.modEventBus = modEventBus;

        CommonClass.init();

        modEventBus.addListener(EntCopperRails::onBuildCreativeTabs);

        if (dist == Dist.CLIENT) {
            EntCopperRailsClient.init(modEventBus);
        }

        Constants.LOG.info("[{}] NeoForge init complete", Constants.MOD_NAME);

        if (ModList.get().isLoaded("theagiterrant")) { // Rename to Agerthe
            //Hello Sister Mod!!!
            // ;) Coming Soon
        }

    }

    private static void onBuildCreativeTabs(BuildCreativeModeTabContentsEvent event) {
        ResourceKey<CreativeModeTab> key = event.getTabKey();
        if (!key.equals(REDSTONE_TAB) && !key.equals(TOOLS_TAB)) {
            return;
        }
        if (event.getParentEntries().stream().noneMatch(stack -> stack.is(Items.ACTIVATOR_RAIL))) {
            return;
        }
        ItemStack anchor = new ItemStack(Items.ACTIVATOR_RAIL);
        List<RegistryObject<Item>> all = ModItems.ALL;
        for (int i = all.size() - 1; i >= 0; i--) {
            event.insertAfter(anchor, new ItemStack(all.get(i).get()),
                    CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
        }
    }
}
