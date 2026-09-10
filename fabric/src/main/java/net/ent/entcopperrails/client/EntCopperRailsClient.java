package net.ent.entcopperrails.client;

import net.ent.entcopperrails.Constants;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.ClientCommands;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

public class EntCopperRailsClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        HudElementRegistry.addLast(
                Identifier.fromNamespaceAndPath(Constants.MOD_ID, "speed_hud"),
                CopperRailsHud::render);

        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) ->
                dispatcher.register(ClientCommands.literal("copperspeed").executes(ctx -> {
                    boolean now = CopperRailsHud.toggle();
                    ctx.getSource().sendFeedback(
                            Component.literal("Copper Rails speed HUD: " + (now ? "ON" : "OFF")));
                    return 1;
                })));
    }
}
