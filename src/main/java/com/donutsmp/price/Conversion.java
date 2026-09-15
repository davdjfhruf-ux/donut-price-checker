package com.donutsmp.price;

public class Conversion {
	public final String from;
	public final int fromQty;
	public final String to;
	public final int toQty;

	public Conversion(String from, int fromQty, String to, int toQty) {
		this.from = from;
		this.fromQty = fromQty;
		this.to = to;
		this.toQty = toQty;
	}

	public ConversionResult calculate(PriceItem fromItem, PriceItem toItem) {
		if (fromItem == null || toItem == null) {
			return new ConversionResult(this, null, null, null, null, false, "Missing price data");
		}

		long cost = (long)(fromItem.price * (fromQty / (double)fromItem.qty));
		long revenue = (long)(toItem.price * (toQty / (double)toItem.qty));
		long profit = revenue - cost;
		double percent = cost == 0 ? 0 : (profit / (double)cost) * 100;

		return new ConversionResult(
			this,
			cost,
			revenue,
			profit,
			percent,
			profit > 0,
			from + " → " + to
		);
	}

	public static class ConversionResult {
		public final Conversion conversion;
		public final Long cost;
		public final Long revenue;
		public final Long profit;
		public final Double percent;
		public final boolean isProfitable;
		public final String label;

		public ConversionResult(Conversion conversion, Long cost, Long revenue, Long profit, Double percent, boolean isProfitable, String label) {
			this.conversion = conversion;
			this.cost = cost;
			this.revenue = revenue;
			this.profit = profit;
			this.percent = percent;
			this.isProfitable = isProfitable;
			this.label = label;
		}
	}
}