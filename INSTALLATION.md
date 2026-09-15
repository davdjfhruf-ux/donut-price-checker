# Donut Price Checker - Installation & Usage Guide

## Prerequisites

Before you can use the Donut Price Checker mod, make sure you have:

1. **Minecraft 1.21.1** (Java Edition)
2. **Fabric Loader** installed
3. **Fabric API** mod installed

### Step 1: Install Fabric Loader

1. Download the Fabric Loader installer from https://fabricmc.net/use/installer/
2. Run the installer
3. Select "Install client" and choose Minecraft 1.21.1
4. Click "Install"
5. Launch Minecraft with the "fabric-loader-1.21.1" profile

### Step 2: Install Fabric API

1. Download Fabric API for 1.21.1 from https://modrinth.com/mod/fabric-api
2. Find your Minecraft mods folder:
   - **Windows:** `%APPDATA%\.minecraft\mods`
   - **macOS:** `~/Library/Application Support/minecraft/mods`
   - **Linux:** `~/.minecraft/mods`
3. Drag and drop the Fabric API JAR file into the mods folder
4. Launch Minecraft to verify it loads correctly

---

## How to Download the Mod

### Option A: Download Pre-Built JAR (Easiest)

1. Go to the repository: https://github.com/davdjfhruf-ux/donut-price-checker
2. Click the **"Actions"** tab at the top
3. Click the latest successful workflow run (green checkmark)
4. Scroll down to **"Artifacts"** section
5. Download **"donut-price-checker-mod"** 
6. Extract the ZIP file
7. Copy the `.jar` file to your mods folder (see paths above)

### Option B: Build from Source

If you want to compile the mod yourself:

**Requirements:**
- Java 21 or higher (download from https://adoptopenjdk.net/)
- Git

**Build Instructions:**

```bash
# Clone the repository
git clone https://github.com/davdjfhruf-ux/donut-price-checker.git
cd donut-price-checker

# Build the mod
./gradlew build    # macOS/Linux
gradlew.bat build  # Windows

# The compiled JAR will be in: build/libs/donut-price-checker-1.0.0.jar
```

Then copy the JAR from `build/libs/` to your mods folder.

---

## How to Install the Mod

1. **Locate your mods folder:**
   - Windows: `%APPDATA%\.minecraft\mods`
   - macOS: `~/Library/Application Support/minecraft/mods`
   - Linux: `~/.minecraft/mods`

2. **Place the JAR file** (`donut-price-checker-1.0.0.jar`) into the mods folder

3. **Launch Minecraft** with the Fabric profile

4. **Verify installation:** You should see "Donut Price Checker" in the mods list (Mods button → search)

---

## How to Use the Mod In-Game

### Opening the Price Checker

1. Load into a world/server
2. Press **P** on your keyboard to open the Donut Price Checker screen
3. Click **"Refresh Prices"** to fetch live market data from Donut SMP

### Understanding the Display

The screen shows:
- **Conversion:** Item conversions (e.g., "bone → bone_meal")
- **Cost:** How much it costs to buy the input items
- **Revenue:** How much you'd get by selling the output items
- **Profit:** Net profit (revenue - cost)
- **Profit %:** Percentage return on investment

**Color Coding:**
- 🟢 **GREEN rows** = Profitable conversions (profit > 0)
- 🔴 **RED rows** = Unprofitable conversions (profit ≤ 0)

### Features

- **Live Fetching:** Pulls current market prices from `donutsmp-mc.com/market`
- **Auto-Calculation:** Instantly calculates profit for all item conversions
- **Background Loading:** Fetching prices doesn't freeze your game
- **Default Conversions:**
  - Bone (64) → Bone Meal (192)
  - Coal Block (1) → Coal (9)
  - Diamond Block (1) → Diamond (9)
  - Iron Block (1) → Iron Ingot (9)
  - Gold Block (1) → Gold Ingot (9)

---

## Keyboard Shortcuts

| Key | Action |
|-----|--------|
| **P** | Open/Close Donut Price Checker |
| **ESC** | Close the screen |

To change the keybind, go to:
- Main Menu → Options → Controls → search "Donut Price Checker"
- Click the key and assign a new one

---

## Troubleshooting

### "Mod won't load"
- Ensure you have **Fabric Loader** installed for 1.21.1
- Ensure you have **Fabric API** installed in mods folder
- Restart Minecraft

### "Can't fetch prices"
- Check your internet connection
- The Donut SMP market page may be down
- Check game logs: `%APPDATA%\.minecraft\logs\latest.log` for error messages

### "P key doesn't work"
- Try opening Controls and check if the key is bound
- Make sure you're in-game (not in menu)
- Try a different key in controls and bind it to Donut Price Checker

### Mod crashes
- Update Fabric Loader to the latest version
- Update Fabric API to the latest version
- Delete the mod and download again

---

## Support

If you encounter issues:
1. Check the **Issues** tab on GitHub
2. Check game logs in `.minecraft/logs/latest.log`
3. Report bugs with clear details on what happened

---

## What's Included in This Release

✅ Minecraft 1.21.1 Fabric mod
✅ Live market price fetching from Donut SMP
✅ In-game GUI with profit calculations
✅ Green/red highlighting for profitable/unprofitable conversions
✅ Automatic GitHub Actions build pipeline
✅ MIT License (free to use and modify)

Enjoy using Donut Price Checker! 🍩📊
