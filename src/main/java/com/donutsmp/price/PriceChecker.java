package com.donutsmp.price;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.donutsmp.DonutPriceCheckerMod;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PriceChecker {
	private static final String DEFAULT_MARKET_URL = "https://donutsmp-mc.com/market";
	private static final Map<String, String> ITEM_MAPPING = loadMapping();

	private static final List<Conversion> DEFAULT_CONVERSIONS = Arrays.asList(
		new Conversion("bone", 64, "bone_meal", 192),
		new Conversion("coal_block", 1, "coal", 9),
		new Conversion("diamond_block", 1, "diamond", 9),
		new Conversion("iron_block", 1, "iron_ingot", 9),
		new Conversion("gold_block", 1, "gold_ingot", 9)
	);

	public static class FetchResult {
		public final List<Conversion.ConversionResult> results;
		public final Map<String, PriceItem> items;
		public final String status;
		public final boolean success;

		public FetchResult(List<Conversion.ConversionResult> results, Map<String, PriceItem> items, String status, boolean success) {
			this.results = results;
			this.items = items;
			this.status = status;
			this.success = success;
		}
	}

	public static FetchResult fetchAndCalculate() {
		return fetchAndCalculate(DEFAULT_MARKET_URL);
	}

	public static FetchResult fetchAndCalculate(String marketUrl) {
		try {
			DonutPriceCheckerMod.LOGGER.info("Fetching market prices from: {}", marketUrl);
			String html = fetchFromUrl(marketUrl);
			if (html == null || html.isEmpty()) {
				return new FetchResult(new ArrayList<>(), new HashMap<>(), "Failed to fetch market page", false);
			}

			Map<String, PriceItem> items = parseMarketHtml(html);
			if (items.isEmpty()) {
				return new FetchResult(new ArrayList<>(), new HashMap<>(), "No prices found in market page", false);
			}

			List<Conversion.ConversionResult> results = new ArrayList<>();
			for (Conversion conversion : DEFAULT_CONVERSIONS) {
				PriceItem fromItem = items.get(conversion.from);
				PriceItem toItem = items.get(conversion.to);
				results.add(conversion.calculate(fromItem, toItem));
			}

			return new FetchResult(results, items, "Fetch successful", true);
		} catch (Exception e) {
			DonutPriceCheckerMod.LOGGER.error("Error fetching prices", e);
			return new FetchResult(new ArrayList<>(), new HashMap<>(), "Error: " + e.getMessage(), false);
		}
	}

	private static String fetchFromUrl(String urlString) throws Exception {
		URL url = new URL(urlString);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		conn.setConnectTimeout(5000);
		conn.setReadTimeout(5000);
		conn.setRequestProperty("User-Agent", "DonutPriceChecker/1.0");

		if (conn.getResponseCode() != 200) {
			return null;
		}

		BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
		StringBuilder response = new StringBuilder();
		String line;
		while ((line = in.readLine()) != null) {
			response.append(line).append("\n");
		}
		in.close();
		return response.toString();
	}

	private static Map<String, PriceItem> parseMarketHtml(String html) {
		Map<String, PriceItem> items = new HashMap<>();

		// Try JSON first
		try {
			JsonElement element = JsonParser.parseString(html);
			if (element.isJsonObject()) {
				JsonObject obj = element.getAsJsonObject();
				if (obj.has("items")) {
					obj.getAsJsonObject("items").entrySet().forEach(entry -> {
						try {
							JsonObject itemObj = entry.getValue().getAsJsonObject();
							long price = itemObj.has("price") ? itemObj.get("price").getAsLong() : 0;
							int qty = itemObj.has("qty") ? itemObj.get("qty").getAsInt() : 1;
							String key = ITEM_MAPPING.getOrDefault(normalizeName(entry.getKey()), normalizeName(entry.getKey()));
							items.put(key, new PriceItem(key, price, qty, entry.getKey()));
						} catch (Exception e) {
							DonutPriceCheckerMod.LOGGER.debug("Error parsing item", e);
						}
					});
				}
			}
			if (!items.isEmpty()) return items;
		} catch (Exception e) {
			DonutPriceCheckerMod.LOGGER.debug("Not JSON, trying HTML parse");
		}

		// Heuristic HTML parsing
		Pattern numberPattern = Pattern.compile("([0-9,]{2,8})");
		String[] lines = html.split("\n");
		for (int i = 0; i < lines.length; i++) {
			String line = lines[i];
			Matcher matcher = numberPattern.matcher(line);
			if (matcher.find()) {
				String number = matcher.group(1).replace(",", "");
				try {
					long price = Long.parseLong(number);
					// Look back for item name
					String name = null;
					for (int j = Math.max(0, i - 5); j < i; j++) {
						String prevLine = lines[j];
						if (prevLine.matches(".*[A-Za-z]{3,}.*")) {
							name = prevLine.replaceAll("<[^>]*>", "").trim();
							if (!name.isEmpty() && name.length() > 2 && name.length() < 60) {
								break;
							}
						}
					}
					if (name != null && !name.isEmpty()) {
						String key = ITEM_MAPPING.getOrDefault(normalizeName(name), normalizeName(name));
						if (!items.containsKey(key)) {
							items.put(key, new PriceItem(key, price, 1, name));
						}
					}
				} catch (NumberFormatException e) {
					// Skip
				}
			}
		}

		return items;
	}

	private static String normalizeName(String s) {
		return s.toLowerCase().replaceAll("[^a-z0-9]+", " ").trim();
	}

	private static Map<String, String> loadMapping() {
		Map<String, String> mapping = new HashMap<>();
		mapping.put("bone", "bone");
		mapping.put("bone meal", "bone_meal");
		mapping.put("bone_meal", "bone_meal");
		mapping.put("coal block", "coal_block");
		mapping.put("coal", "coal");
		mapping.put("diamond block", "diamond_block");
		mapping.put("diamond", "diamond");
		mapping.put("iron block", "iron_block");
		mapping.put("iron ingot", "iron_ingot");
		mapping.put("gold block", "gold_block");
		mapping.put("gold ingot", "gold_ingot");
		return mapping;
	}
}