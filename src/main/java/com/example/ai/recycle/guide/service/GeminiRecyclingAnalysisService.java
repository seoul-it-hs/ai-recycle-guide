package com.example.ai.recycle.guide.service;

import com.example.ai.recycle.guide.domain.RecyclingAnalysis;
import com.example.ai.recycle.guide.service.dto.GeminiAnalysisResponseDto;
import com.google.genai.Client;
import com.google.genai.types.Content;
import com.google.genai.types.GenerateContentConfig;
import com.google.genai.types.GenerateContentResponse;
import com.google.genai.types.Part;
import java.io.IOException;
import java.util.Locale;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

// @Service는 이 클래스가 비즈니스 로직을 담당하는 Spring Bean임을 뜻한다.
@Service
public class GeminiRecyclingAnalysisService implements RecyclingAnalysisService {

	private final ObjectMapper objectMapper;
	private final String apiKey;
	private final String model;

	public GeminiRecyclingAnalysisService(
			ObjectMapper objectMapper,
			// @Value는 application.properties의 설정값을 생성자 매개변수로 주입한다.
			@Value("${gemini.api-key:}") String apiKey,
			@Value("${gemini.model:gemini-2.5-flash}") String model
	) {
		this.objectMapper = objectMapper;
		this.apiKey = apiKey;
		this.model = model;
	}

	@Override
	public RecyclingAnalysis analyze(MultipartFile file) {
		// 이미지 파일과 API Key를 먼저 확인한 뒤, Gemini에 보낼 요청 데이터를 만든다.
		validateFile(file);
		validateApiKey();

		String contentType = file.getContentType();
		byte[] imageBytes = readImageBytes(file);

		Content content = Content.fromParts(
				Part.fromText(GeminiRecyclingPrompt.text()),
				Part.fromBytes(imageBytes, contentType)
		);

		// Gemini가 설명 문장 대신 JSON만 반환하도록 응답 형식을 지정한다.
		GenerateContentConfig config = GenerateContentConfig.builder()
				.responseMimeType("application/json")
				.candidateCount(1)
				.build();

		Client client = Client.builder()
				.apiKey(apiKey)
				.build();

		GenerateContentResponse response = client.models.generateContent(model, content, config);
		return parseAnalysis(response.text());
	}

	private void validateFile(MultipartFile file) {
		if (file == null || file.isEmpty()) {
			throw new IllegalArgumentException("분석할 이미지 파일을 선택해주세요.");
		}

		String contentType = file.getContentType();
		if (contentType == null || !contentType.toLowerCase(Locale.ROOT).startsWith("image/")) {
			throw new IllegalArgumentException("이미지 파일만 업로드할 수 있습니다.");
		}
	}

	private void validateApiKey() {
		if (apiKey == null || apiKey.isBlank()) {
			throw new IllegalStateException("Gemini API Key가 설정되지 않았습니다.");
		}
	}

	private byte[] readImageBytes(MultipartFile file) {
		try {
			return file.getBytes();
		} catch (IOException e) {
			throw new IllegalStateException("이미지 파일을 읽는 중 오류가 발생했습니다.", e);
		}
	}

	private RecyclingAnalysis parseAnalysis(String responseText) {
		if (responseText == null || responseText.isBlank()) {
			throw new IllegalStateException("Gemini 응답이 비어 있습니다.");
		}

		try {
			// ObjectMapper는 JSON 문자열을 Java 객체로 바꿔주는 Jackson의 도구다.
			GeminiAnalysisResponseDto responseDto = objectMapper.readValue(responseText, GeminiAnalysisResponseDto.class);
			return responseDto.toDomain();
		} catch (JacksonException e) {
			throw new IllegalStateException("Gemini 응답을 JSON으로 해석할 수 없습니다.", e);
		}
	}
}
