package com.donut.pricechecker;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;

/**
 * Simple Fabric mod initializer (placeholder).
 * This class is intentionally minimal — it provides an HTTP fetch utility and will be
 * extended to register a keybind and GUI that shows market prices and profit calculations.
 */
public class PriceCheckerMod {
    public static final String MODID = "donut-price-checker";
    private static final Logger LOGGER = Logger.getLogger(MODID);

    // Called on mod initialization by Fabric (you'll need to wire this as an entrypoint in fabric.mod.json)
    public static void onInitialize() {
        LOGGER.info("Donut Price Checker initializing (placeholder).\nConfigure the mod and use the in-game GUI to fetch market prices.");
    }

    // Simple HTTP GET utility — synchronous. Use on a background thread in-game.
    public static String httpGet(String urlStr) throws Exception {
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("User-Agent", "DonutPriceChecker/0.1 (+https://github.com/davdjfhruf-ux/donut-price-checker)");
        conn.setConnectTimeout(10000);
        conn.setReadTimeout(10000);
        int status = conn.getResponseCode();
        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
        StringBuilder content = new StringBuilder();
        String line;
        while ((line = in.readLine()) != null) content.append(line).append('\n');
        in.close();
        conn.disconnect();
        if (status >= 400) throw new RuntimeException("HTTP error: " + status + "\n" + content.toString());
        return content.toString();
    }
}
