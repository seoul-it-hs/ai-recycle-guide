# AI Recycle Guide

이 저장소는 Java와 Spring Boot를 처음 배우는 학생이

- Spring MVC 기반 REST API 구조를 이해하고
- 이미지 업로드 API를 직접 구현하고
- Google Gemini SDK로 AI 기능을 연결하고
- HTML/CSS/JavaScript로 간단한 웹 화면을 만드는 것을 목표로 한 학습형 프로젝트입니다.

## 문서 안내

아래 파일을 순서대로 읽는 것을 권장합니다.

1. `README.md` (프로젝트 목적과 실행 방법)
2. `.env.example` (로컬 실행 설정 예시)
3. `src/main/java/com/example/ai/recycle/guide/controller/RecyclingController.java` (HTTP 요청 처리)
4. `src/main/java/com/example/ai/recycle/guide/service/GeminiRecyclingAnalysisService.java` (AI 분석 기능)
5. `src/main/resources/static/index.html` (웹 화면)

## 빠른 실행

환경 설정:

```powershell
copy .env.example .env
```

`.env` 파일을 열고 `gemini.api-key`에 본인의 Gemini API Key를 입력합니다.

백엔드 실행:

```powershell
gradlew.bat bootRun
```

브라우저 접속:

```text
http://localhost:8080
```

## 한 줄 요약

이미지 업로드 기능을 만들면서 Spring Boot와 Gemini AI 연동을 함께 배우는 프로젝트입니다.
