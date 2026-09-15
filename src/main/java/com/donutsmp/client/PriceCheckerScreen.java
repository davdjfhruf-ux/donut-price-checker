package com.donutsmp.client;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import com.donutsmp.DonutPriceCheckerMod;
import com.donutsmp.price.Conversion;
import com.donutsmp.price.PriceChecker;
import java.util.List;

public class PriceCheckerScreen extends Screen {
	private static final int PADDING = 10;
	private static final int HEADER_HEIGHT = 40;
	private static final int ROW_HEIGHT = 20;
	private static final int COLOR_GOOD = 0xFF0f8f0f;
	private static final int COLOR_BAD = 0xFFff3333;
	private static final int COLOR_BORDER_GOOD = 0xFF00ff00;
	private static final int COLOR_BORDER_BAD = 0xFFff0000;

	private PriceChecker.FetchResult result;
	private boolean isFetching = false;
	private long lastFetchTime = 0;
	private ButtonWidget refreshBtn;
	private ButtonWidget closeBtn;

	public PriceCheckerScreen() {
		super(Text.literal("Donut Price Checker"));
		this.result = new PriceChecker.FetchResult(List.of(), java.util.Map.of(), "Click Refresh to fetch prices", false);
	}

	@Override
	protected void init() {
		// Refresh button
		this.refreshBtn = this.addDrawableChild(
			ButtonWidget.builder(Text.literal("Refresh Prices"), button -> this.fetchPrices())
				.dimensions(this.width / 2 - 100, PADDING, 90, 20)
				.build()
		);

		// Close button
		this.closeBtn = this.addDrawableChild(
			ButtonWidget.builder(Text.literal("Close"), button -> this.close())
				.dimensions(this.width / 2 + 10, PADDING, 90, 20)
				.build()
		);
	}

	private void fetchPrices() {
		if (isFetching) return;
		isFetching = true;
		refreshBtn.active = false;

		// Fetch in background thread to avoid freezing
		new Thread(() -> {
			try {
				result = PriceChecker.fetchAndCalculate();
				lastFetchTime = System.currentTimeMillis();
			} catch (Exception e) {
				DonutPriceCheckerMod.LOGGER.error("Error fetching prices", e);
			} finally {
				isFetching = false;
			}
		}).start();
	}

	@Override
	public void render(DrawContext context, int mouseX, int mouseY, float delta) {
		this.renderBackground(context, mouseX, mouseY, delta);
		super.render(context, mouseX, mouseY, delta);

		int y = PADDING + HEADER_HEIGHT;
		int contentWidth = this.width - 2 * PADDING;

		// Title
		context.drawCenteredTextWithShadow(this.textRenderer, Text.literal("Donut Price Checker"), this.width / 2, PADDING + 5, 0xFFFFFF);

		// Status / Fetch indicator
		String statusText = isFetching ? "Fetching prices..." : result.status;
		int statusColor = result.success ? 0xFF00FF00 : 0xFFFF6666;
		context.drawTextWithShadow(this.textRenderer, Text.literal(statusText), PADDING, y, statusColor);
		y += ROW_HEIGHT + 5;

		// Table header
		context.fill(PADDING, y, this.width - PADDING, y + ROW_HEIGHT, 0xFF333333);
		context.drawTextWithShadow(this.textRenderer, Text.literal("Conversion"), PADDING + 5, y + 5, 0xFFFFFF);
		context.drawTextWithShadow(this.textRenderer, Text.literal("Cost"), PADDING + 150, y + 5, 0xFFFFFF);
		context.drawTextWithShadow(this.textRenderer, Text.literal("Revenue"), PADDING + 250, y + 5, 0xFFFFFF);
		context.drawTextWithShadow(this.textRenderer, Text.literal("Profit"), PADDING + 350, y + 5, 0xFFFFFF);
		context.drawTextWithShadow(this.textRenderer, Text.literal("Profit %"), PADDING + 430, y + 5, 0xFFFFFF);
		y += ROW_HEIGHT + 2;

		// Conversion results
		int maxRows = (this.height - y - PADDING) / ROW_HEIGHT;
		int rowCount = 0;

		for (Conversion.ConversionResult res : result.results) {
			if (rowCount >= maxRows) break;

			int bgColor = res.isProfitable ? COLOR_GOOD : COLOR_BAD;
			int borderColor = res.isProfitable ? COLOR_BORDER_GOOD : COLOR_BORDER_BAD;

			// Background
			context.fill(PADDING, y, this.width - PADDING, y + ROW_HEIGHT, bgColor);
			// Left border
			context.fill(PADDING, y, PADDING + 3, y + ROW_HEIGHT, borderColor);

			// Draw text
			context.drawTextWithShadow(this.textRenderer, Text.literal(res.label), PADDING + 5, y + 5, 0xFFFFFFFF);

			if (res.cost != null) {
				context.drawTextWithShadow(this.textRenderer, Text.literal(String.format("%,d", res.cost)), PADDING + 150, y + 5, 0xFFFFFFFF);
			}
			if (res.revenue != null) {
				context.drawTextWithShadow(this.textRenderer, Text.literal(String.format("%,d", res.revenue)), PADDING + 250, y + 5, 0xFFFFFFFF);
			}
			if (res.profit != null) {
				context.drawTextWithShadow(this.textRenderer, Text.literal(String.format("%,d", res.profit)), PADDING + 350, y + 5, 0xFFFFFFFF);
			}
			if (res.percent != null) {
				context.drawTextWithShadow(this.textRenderer, Text.literal(String.format("%.1f%%", res.percent)), PADDING + 430, y + 5, 0xFFFFFFFF);
			}

			y += ROW_HEIGHT;
			rowCount++;
		}

		if (result.results.isEmpty() && result.success) {
			context.drawCenteredTextWithShadow(this.textRenderer, Text.literal("No conversions to display"), this.width / 2, y + 20, 0xFFAAAAAA);
		}

		refreshBtn.active = !isFetching;
	}

	@Override
	public boolean shouldCloseOnEsc() {
		return true;
	}

	@Override
	public void close() {
		this.client.setScreen(null);
	}
}