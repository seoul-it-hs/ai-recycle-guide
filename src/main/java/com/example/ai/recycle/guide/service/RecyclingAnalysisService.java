package com.example.ai.recycle.guide.service;

import com.example.ai.recycle.guide.domain.RecyclingAnalysis;
import org.springframework.web.multipart.MultipartFile;

// 컨트롤러가 구체적인 AI 서비스 구현체를 직접 알지 않도록 약속을 정의하는 인터페이스다.
public interface RecyclingAnalysisService {
	RecyclingAnalysis analyze(MultipartFile file);
}
