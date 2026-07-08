package com.example.ai.recycle.guide.service.dto;

import com.example.ai.recycle.guide.domain.RecyclingAnalysis;

public class GeminiAnalysisResponseDto {

	private String itemName;
	private String category;
	private String guide;
	private String caution;

	public GeminiAnalysisResponseDto() {
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getGuide() {
		return guide;
	}

	public void setGuide(String guide) {
		this.guide = guide;
	}

	public String getCaution() {
		return caution;
	}

	public void setCaution(String caution) {
		this.caution = caution;
	}

	public RecyclingAnalysis toDomain() {
		return new RecyclingAnalysis(itemName, category, guide, caution);
	}
}
