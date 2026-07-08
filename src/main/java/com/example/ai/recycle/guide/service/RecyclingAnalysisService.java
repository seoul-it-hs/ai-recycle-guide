package com.example.ai.recycle.guide.service;

import com.example.ai.recycle.guide.domain.RecyclingAnalysis;
import org.springframework.web.multipart.MultipartFile;

public interface RecyclingAnalysisService {
	RecyclingAnalysis analyze(MultipartFile file);
}
