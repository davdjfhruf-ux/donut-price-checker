package com.donutsmp.price;

public class PriceItem {
	public final String key;
	public final long price;
	public final int qty;
	public final String siteName;

	public PriceItem(String key, long price, int qty, String siteName) {
		this.key = key;
		this.price = price;
		this.qty = qty;
		this.siteName = siteName;
	}

	public long getUnitPrice() {
		return price / qty;
	}
}