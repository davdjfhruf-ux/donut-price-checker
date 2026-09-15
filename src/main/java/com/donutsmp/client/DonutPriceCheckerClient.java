package com.donutsmp.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.Environment;
import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;
import com.donutsmp.DonutPriceCheckerMod;

@Environment(EnvType.CLIENT)
public class DonutPriceCheckerClient implements ClientModInitializer {
	private static KeyBinding openScreenKey;
	private static boolean screenOpen = false;

	@Override
	public void onInitializeClient() {
		openScreenKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
			"key.donutpricechecker.open",
			InputUtil.Type.KEYSYM,
			GLFW.GLFW_KEY_P,
			"category.donutpricechecker.main"
		));

		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			if (openScreenKey.wasPressed() && client.player != null) {
				screenOpen = !screenOpen;
				if (screenOpen) {
					client.setScreen(new PriceCheckerScreen());
				} else {
					client.setScreen(null);
				}
			}
		});

		DonutPriceCheckerMod.LOGGER.info("Donut Price Checker client initialized! Press P to open.");
	}
}