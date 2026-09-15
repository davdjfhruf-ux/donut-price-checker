package com.donutsmp;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DonutPriceCheckerMod implements ModInitializer {
	public static final String MOD_ID = "donutpricechecker";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("Donut Price Checker mod initialized!");
	}
}