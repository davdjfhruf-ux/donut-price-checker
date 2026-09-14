# Donut Price Checker

This repository contains a browser prototype and a Fabric mod scaffold (initial) for checking Donut SMP market prices and showing profitable item conversions.

What is included
- `donut-price-checker.html` — a browser prototype that can fetch the market page and heuristically parse prices (may require a CORS proxy). It computes profits for common conversions (bone → bone meal, block → items, etc.).
- `config/pricechecker.json` — sample config (edit in-game for the mod).
- `src/` — initial Fabric mod scaffold (placeholder). Use it as a starting point for a Fabric mod for Minecraft 1.21.1.

Live market fetching
- The prototype can fetch `https://donutsmp-mc.com/market`. If your browser blocks the request due to CORS, you can use a CORS proxy for testing (for example: `https://cors-anywhere.herokuapp.com/` — note: CORS proxies are third-party services; use at your own risk).

Next steps
- I added a working live-fetch prototype. If you want the Fabric mod built into a `.jar` automatically, I can add a GitHub Actions workflow to build the mod on push and upload the artifact. Tell me if you want that and I will set it up.

How you can use the browser prototype now
1. Download `donut-price-checker.html` from this repo and open it in your browser.
2. Click "Fetch Market" to load the page HTML (use a CORS proxy if needed) or paste the page HTML/JSON into the text box.
3. Click "Refresh Calculations" to parse prices and compute profits.

Notes about accuracy and mapping
- The site uses human-readable item names. The prototype uses simple normalization and a small mapping table. If some items are not recognized, add entries to the mapping file or the prototype mapping.

Security and etiquette
- The mod/prototype will only fetch public data from the market page. Do not provide login cookies or private credentials here. If the site disallows scraping, stop automated requests and use a manual paste workflow.

If you want, I will now:
- Add Fabric mod source that performs the same live fetch in-game, and a GUI with a Refresh button; and
- Add a GitHub Actions workflow that builds the mod and uploads the compiled `.jar` as an artifact. 

Reply `build` to have me set up the build workflow and finish the mod source, or `instructions` if you'd prefer to build locally and I should provide step-by-step build commands.
