package net.ent.entcopperrails.client;

import net.ent.entcopperrails.Constants;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.RegisterClientCommandsEvent;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.common.NeoForge;

public final class EntCopperRailsClient {

    public static void init(IEventBus modEventBus) {
        modEventBus.addListener(EntCopperRailsClient::onRegisterGuiLayers);
        NeoForge.EVENT_BUS.addListener(EntCopperRailsClient::onRegisterClientCommands);
    }

    private static void onRegisterGuiLayers(RegisterGuiLayersEvent event) {
        event.registerAboveAll(
                Identifier.fromNamespaceAndPath(Constants.MOD_ID, "speed_hud"),
                CopperRailsHud::render);
    }

    private static void onRegisterClientCommands(RegisterClientCommandsEvent event) {
        event.getDispatcher().register(Commands.literal("copperspeed").executes(ctx -> {
            boolean now = CopperRailsHud.toggle();
            ctx.getSource().sendSuccess(
                    () -> Component.literal("Copper Rails speed HUD: " + (now ? "ON" : "OFF")), false);
            return 1;
        }));
    }

    private EntCopperRailsClient() {}
}
