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

1. Java 설치 확인

명령 프롬프트(cmd)에서 아래 명령을 실행합니다.

```cmd
echo %JAVA_HOME%
java -version
javac -version
```

PowerShell을 사용한다면 아래 명령을 실행합니다.

```powershell
$env:JAVA_HOME
java -version
javac -version
```

`java -version`과 `javac -version`이 정상 출력되면 다음 단계로 이동합니다.

2. 환경 설정 파일 만들기

명령 프롬프트(cmd) 또는 PowerShell에서 아래 명령을 실행합니다.

```powershell
copy .env.example .env
```

`.env` 파일을 열고 `gemini.api-key`에 본인의 Gemini API Key를 입력합니다.

3. 빌드하기

명령 프롬프트(cmd)에서 아래 명령을 실행합니다.

```cmd
gradlew.bat build
```

PowerShell을 사용한다면 아래 명령을 실행합니다.

```powershell
.\gradlew.bat build
```

빌드가 성공하면 다음 단계로 이동합니다.

4. 실행하기

명령 프롬프트(cmd)에서 아래 명령을 실행합니다.

```cmd
gradlew.bat bootRun
```

PowerShell을 사용한다면 아래 명령을 실행합니다.

```powershell
.\gradlew.bat bootRun
```

5. 브라우저에서 접속하기

```text
http://localhost:8080
```

## VS Code 터미널에서 Java를 찾지 못할 때

VS Code 터미널에서 아래 명령이 실패할 수 있습니다.

```powershell
java -version
javac -version
```

오류 예시:

```text
java : 'java' 용어가 cmdlet, 함수, 스크립트 파일 또는 실행할 수 있는 프로그램 이름으로 인식되지 않습니다.
```

이 오류는 Spring Boot 코드 문제가 아닙니다. 현재 VS Code 터미널이 JDK 실행 파일을 찾지 못한다는 뜻입니다.

Eclipse Adoptium JDK 21을 설치 파일로 설치했다면, 먼저 일반 명령 프롬프트(cmd)를 새로 열고 프로젝트 폴더로 이동합니다.

```cmd
cd C:\Users\사용자이름\Documents\GitHub\ai-recycle-guide
```

그 다음 아래 스크립트를 실행해 볼 수 있습니다.

```cmd
scripts\setup-java-env.bat
```

이 스크립트는 아래 작업을 합니다.

- 설치 과정에서 설정된 `JAVA_HOME` 값을 읽습니다.
- `JAVA_HOME\bin\java.exe`와 `JAVA_HOME\bin\javac.exe`가 있는지 확인합니다.
- 사용자 `Path`에 실제 JDK `bin` 경로를 추가합니다.

스크립트 실행 후에는 VS Code 안에서 터미널만 새로 여는 것으로는 부족할 수 있습니다.

VS Code 창을 모두 완전히 종료한 뒤 다시 실행합니다. 그 다음 VS Code에서 새 터미널을 열고 아래 명령을 확인합니다.

```powershell
java -version
javac -version
```

그래도 해결되지 않으면 아래 내용을 순서대로 확인합니다.

먼저 VS Code 터미널에서 아래 명령을 실행합니다.

```powershell
$env:JAVA_HOME
java -version
javac -version
```

Java가 PC에 설치되어 있는지 확인하려면 아래 명령을 실행합니다.

```powershell
[Environment]::GetEnvironmentVariable("JAVA_HOME", "User")
[Environment]::GetEnvironmentVariable("JAVA_HOME", "Machine")
```

둘 중 하나에서 JDK 경로가 나오면 Java 설치 경로는 Windows 환경 변수에 등록되어 있는 것입니다.

예시:

```text
C:\Program Files\Eclipse Adoptium\jdk-21.0.11.10-hotspot\
```

PATH에 JDK `bin` 폴더가 있는지도 확인합니다.

```powershell
[Environment]::GetEnvironmentVariable("Path", "User")
[Environment]::GetEnvironmentVariable("Path", "Machine")
```

출력된 값 안에 아래 경로가 있어야 합니다.

```text
C:\Program Files\Eclipse Adoptium\jdk-21.0.11.10-hotspot\bin
```

또는 아래처럼 적혀 있어도 됩니다.

```text
%JAVA_HOME%\bin
```

수업 중 바로 빌드만 확인해야 한다면, VS Code 터미널에서 아래 명령을 실행할 수 있습니다.

```powershell
$env:JAVA_HOME = [Environment]::GetEnvironmentVariable("JAVA_HOME", "Machine")
$env:Path = "$env:JAVA_HOME\bin;$env:Path"
```

그 다음 다시 확인합니다.

```powershell
java -version
javac -version
```

정상 출력되면 빌드를 실행합니다.

```powershell
.\gradlew.bat build
```

이 임시 해결 방법은 현재 열려 있는 VS Code 터미널에서만 적용됩니다. VS Code를 다시 열면 다시 설정해야 할 수 있습니다.

영구적으로 해결하려면 Windows 환경 변수에 아래 값이 들어가 있어야 합니다.

```text
JAVA_HOME = C:\Program Files\Eclipse Adoptium\jdk-21.0.11.10-hotspot\
Path      = %JAVA_HOME%\bin
```

환경 변수를 수정한 뒤에는 VS Code를 완전히 종료하고 다시 실행합니다.

VS Code 설정에 아래 항목이 있으면 터미널 환경 변수를 덮어쓸 수 있습니다.

```json
"terminal.integrated.env.windows": {
  "Path": "..."
}
```

이 설정이 있으면 JDK `bin` 경로가 빠져 있지 않은지 확인해야 합니다.

## 빌드 검증 기록

이 내용은 수업 준비 과정에서 실제 빌드 명령을 실행하며 확인한 기록입니다.

나중에 별도 문서로 분리할 수 있지만, 지금은 학생들이 겪을 수 있는 문제를 한곳에서 볼 수 있도록 README에 함께 기록합니다.

### 1. 학생이 실행할 빌드 명령

VS Code 터미널이 PowerShell이면 아래 명령을 실행합니다.

```powershell
.\gradlew.bat build
```

명령 프롬프트(cmd)라면 아래 명령을 실행합니다.

```cmd
gradlew.bat build
```

빌드 전에 아래 명령이 정상 동작해야 합니다.

```powershell
java -version
javac -version
```

확인된 Java 버전 예시는 다음과 같습니다.

```text
openjdk version "21.0.11" 2026-04-21 LTS
OpenJDK Runtime Environment Temurin-21.0.11+10 (build 21.0.11+10-LTS)
OpenJDK 64-Bit Server VM Temurin-21.0.11+10 (build 21.0.11+10-LTS, mixed mode, sharing)
javac 21.0.11
```

### 2. Gradle 다운로드 단계에서 실패한 경우

처음 `.\gradlew.bat build`를 실행하면 Gradle Wrapper가 인터넷에서 Gradle을 내려받을 수 있습니다.

이때 아래 오류가 발생할 수 있습니다.

```text
Fetching distribution.
Downloading https://services.gradle.org/distributions/gradle-9.5.1-bin.zip

Attempt 1/1 failed. Reason: Permission denied: getsockopt
Exception in thread "main" java.net.SocketException: Permission denied: getsockopt
```

이 오류는 Spring Boot 코드 오류가 아닙니다.

Gradle 배포 파일을 다운로드하는 네트워크 접근이 막혔다는 뜻입니다.

학생 PC에서는 아래 조건이 필요합니다.

- 인터넷 연결 가능
- `https://services.gradle.org/distributions/gradle-9.5.1-bin.zip` 다운로드 가능
- 학교 네트워크나 보안 프로그램이 Gradle 다운로드를 차단하지 않음

### 3. 테스트 단계에서 ObjectMapper Bean 오류가 발생한 경우

Gradle 다운로드가 성공한 뒤 실제 빌드가 진행되면, 테스트 단계에서 아래 오류가 발생할 수 있습니다.

```text
AiRecycleGuideApplicationTests > contextLoads() FAILED
    java.lang.IllegalStateException
        Caused by: org.springframework.beans.factory.UnsatisfiedDependencyException
            Caused by: org.springframework.beans.factory.NoSuchBeanDefinitionException
```

테스트 리포트에서 확인한 핵심 오류는 다음과 같습니다.

```text
Parameter 0 of constructor in com.example.ai.recycle.guide.service.GeminiRecyclingAnalysisService required a bean of type 'com.fasterxml.jackson.databind.ObjectMapper' that could not be found.
```

원인은 `ObjectMapper` 타입이 Spring Boot 4의 Jackson 버전과 맞지 않았기 때문입니다.

Jackson은 Java에서 JSON 문자열을 객체로 바꾸거나, 객체를 JSON 문자열로 바꾸는 대표적인 라이브러리입니다.

이 프로젝트에서는 Gemini가 반환한 JSON 응답을 `GeminiAnalysisResponseDto` 객체로 바꾸기 위해 `ObjectMapper`를 사용합니다.

처음 코드는 Jackson 2 계열 패키지를 사용하고 있었습니다.

```java
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
```

하지만 현재 프로젝트는 Spring Boot `4.1.0`을 사용하고 있고, Spring Boot 4는 Jackson 3 계열을 사용합니다.

그래서 아래처럼 Spring Boot 4에 맞는 Jackson 3 패키지로 수정했습니다.

```java
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;
```

예외 처리도 함께 수정했습니다.

```java
} catch (JacksonException e) {
    throw new IllegalStateException("Gemini 응답을 JSON으로 해석할 수 없습니다.", e);
}
```

이 수정의 의미는 단순히 import 이름만 바꾼 것이 아닙니다.

Spring Boot가 관리하는 `ObjectMapper` Bean의 타입과 서비스 코드가 요구하는 `ObjectMapper` 타입을 맞춘 것입니다.

### 4. 최종 빌드 성공 확인

수정 후 학생들이 실행할 빌드 명령과 같은 명령을 다시 실행했습니다.

```powershell
.\gradlew.bat build
```

결과는 다음과 같습니다.

```text
> Task :build

BUILD SUCCESSFUL in 17s
7 actionable tasks: 5 executed, 2 up-to-date
```

따라서 현재 기준으로는 학생들이 Java 환경을 정상 설정하고 Gradle 다운로드가 가능한 네트워크에서 실행하면, 프로젝트 빌드는 성공합니다.

## 한 줄 요약

이미지 업로드 기능을 만들면서 Spring Boot와 Gemini AI 연동을 함께 배우는 프로젝트입니다.
