package com.example.ai.recycle.guide.controller;

import com.example.ai.recycle.guide.controller.dto.RecyclingAnalysisResponseDto;
import com.example.ai.recycle.guide.domain.RecyclingAnalysis;
import com.example.ai.recycle.guide.service.RecyclingAnalysisService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

// @RestController는 이 클래스가 HTTP 요청을 받아 JSON 응답을 돌려주는 컨트롤러임을 뜻한다.
@RestController
@RequestMapping("/api")
public class RecyclingController {

	private final RecyclingAnalysisService recyclingAnalysisService;

	public RecyclingController(RecyclingAnalysisService recyclingAnalysisService) {
		this.recyclingAnalysisService = recyclingAnalysisService;
	}

	@PostMapping(path = "/analyze", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public RecyclingAnalysisResponseDto analyze(@RequestParam("file") MultipartFile file) {
		// MultipartFile은 브라우저가 업로드한 이미지 파일을 Spring이 담아주는 객체다.
		// 컨트롤러는 요청을 받고, 실제 분석 작업은 서비스에게 맡긴다.
		RecyclingAnalysis analysis = recyclingAnalysisService.analyze(file);
		return RecyclingAnalysisResponseDto.from(analysis);
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<String> handleBadRequest(IllegalArgumentException e) {
		return ResponseEntity.badRequest().body(e.getMessage());
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<String> handleServerError(Exception e) {
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("분석 중 오류가 발생했습니다.");
	}
}
