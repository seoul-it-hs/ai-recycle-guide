package com.example.ai.recycle.guide.service;

// Gemini에게 어떤 역할을 맡길지, 어떤 JSON 형식으로 답해야 할지 알려주는 프롬프트다.
public class GeminiRecyclingPrompt {

	private GeminiRecyclingPrompt() {
	}

	public static String text() {
		// AI 응답은 자유로운 문장보다 정해진 JSON 형식이 서버에서 처리하기 쉽다.
		return """
				너는 한국의 분리배출 안내 도우미다.
				이미지를 보고 물건이 무엇인지 추정하고,
				분리배출 종류와 배출 방법을 학생이 이해하기 쉽게 한국어로 설명해라.
				확실하지 않으면 확실하지 않다고 말하고 일반적인 안내를 제공해라.

				응답은 아래 JSON 형식만 사용해라.
				{
				  "itemName": "물건 이름",
				  "category": "분리배출 종류",
				  "guide": "배출 방법",
				  "caution": "주의 사항"
				}
				JSON 앞뒤에 설명 문장을 붙이지 마라.
				""";
	}
}
