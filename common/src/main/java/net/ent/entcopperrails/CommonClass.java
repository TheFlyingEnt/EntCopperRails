package net.ent.entcopperrails;

import net.ent.entcopperrails.platform.Services;
import net.ent.entcopperrails.registry.ModBlocks;
import net.ent.entcopperrails.registry.ModItems;

public class CommonClass {

    public static void init() {
        Constants.LOG.info("[{}] Common init on {} ({} environment)",
                Constants.MOD_NAME, Services.PLATFORM.getPlatformName(), Services.PLATFORM.getEnvironmentName());

        ModBlocks.init();
        ModItems.init();

    }

    private CommonClass() {}
}
