package com.example.ai.recycle.guide.domain;

public class RecyclingAnalysis {

	private final String itemName;
	private final String category;
	private final String guide;
	private final String caution;

	public RecyclingAnalysis(String itemName, String category, String guide, String caution) {
		this.itemName = itemName;
		this.category = category;
		this.guide = guide;
		this.caution = caution;
	}

	public String getItemName() {
		return itemName;
	}

	public String getCategory() {
		return category;
	}

	public String getGuide() {
		return guide;
	}

	public String getCaution() {
		return caution;
	}
}
