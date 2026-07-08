package com.example.ai.recycle.guide.controller.dto;

import com.example.ai.recycle.guide.domain.RecyclingAnalysis;

public class RecyclingAnalysisResponseDto {

	private String itemName;
	private String category;
	private String guide;
	private String caution;

	public RecyclingAnalysisResponseDto() {
	}

	public RecyclingAnalysisResponseDto(String itemName, String category, String guide, String caution) {
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

	public static RecyclingAnalysisResponseDto from(RecyclingAnalysis analysis) {
		return new RecyclingAnalysisResponseDto(
				analysis.getItemName(),
				analysis.getCategory(),
				analysis.getGuide(),
				analysis.getCaution()
		);
	}
}
