package net.ent.entcopperrails.platform;

import java.util.ServiceLoader;

import net.ent.entcopperrails.Constants;
import net.ent.entcopperrails.platform.services.IPlatformHelper;

public class Services {
    public static final IPlatformHelper PLATFORM = load(IPlatformHelper.class);
    public static <T> T load(Class<T> clazz) {

        final T loadedService = ServiceLoader.load(clazz, Services.class.getClassLoader())
                .findFirst()
                .orElseThrow(() -> new NullPointerException("Failed to load service for " + clazz.getName()));
        Constants.LOG.debug("Loaded {} for service {}", loadedService, clazz);
        return loadedService;
    }
}
