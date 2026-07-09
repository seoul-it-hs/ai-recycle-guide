@echo off
setlocal EnableExtensions

echo [1/4] Reading JAVA_HOME...

set "FOUND_JDK=%JAVA_HOME%"

if not defined FOUND_JDK (
    for /f "tokens=2,*" %%A in ('reg query "HKCU\Environment" /v JAVA_HOME 2^>nul') do set "FOUND_JDK=%%B"
)

if not defined FOUND_JDK (
    for /f "tokens=2,*" %%A in ('reg query "HKLM\SYSTEM\CurrentControlSet\Control\Session Manager\Environment" /v JAVA_HOME 2^>nul') do set "FOUND_JDK=%%B"
)

if not defined FOUND_JDK (
    echo ERROR: JAVA_HOME was not found.
    echo Please install JDK 21 and select "Set or override JAVA_HOME variable".
    exit /b 1
)

:trimTrailingSlash
if "%FOUND_JDK:~-1%"=="\" (
    set "FOUND_JDK=%FOUND_JDK:~0,-1%"
    goto trimTrailingSlash
)

if not exist "%FOUND_JDK%\bin\java.exe" (
    echo ERROR: java.exe was not found in JAVA_HOME.
    echo JAVA_HOME=%FOUND_JDK%
    echo Expected: %FOUND_JDK%\bin\java.exe
    exit /b 1
)

if not exist "%FOUND_JDK%\bin\javac.exe" (
    echo ERROR: javac.exe was not found in JAVA_HOME.
    echo JAVA_HOME=%FOUND_JDK%
    echo Expected: %FOUND_JDK%\bin\javac.exe
    exit /b 1
)

set "JAVA_BIN=%FOUND_JDK%\bin"

echo [2/4] JAVA_HOME is valid:
echo %FOUND_JDK%

echo [3/4] Updating user environment variables...

reg add "HKCU\Environment" /v JAVA_HOME /t REG_SZ /d "%FOUND_JDK%" /f >nul

if errorlevel 1 (
    echo ERROR: Failed to update user JAVA_HOME.
    exit /b 1
)

set "USER_PATH="
for /f "tokens=2,*" %%A in ('reg query "HKCU\Environment" /v Path 2^>nul') do set "USER_PATH=%%B"

echo ;%USER_PATH%; | find /I ";%JAVA_BIN%;" >nul

if errorlevel 1 (
    if defined USER_PATH (
        set "NEW_USER_PATH=%USER_PATH%;%JAVA_BIN%"
    ) else (
        set "NEW_USER_PATH=%JAVA_BIN%"
    )

    reg add "HKCU\Environment" /v Path /t REG_EXPAND_SZ /d "%NEW_USER_PATH%" /f >nul

    if errorlevel 1 (
        echo ERROR: Failed to update user Path.
        exit /b 1
    )
)

set "UPDATED_USER_PATH="
for /f "tokens=2,*" %%A in ('reg query "HKCU\Environment" /v Path 2^>nul') do set "UPDATED_USER_PATH=%%B"

echo ;%UPDATED_USER_PATH%; | find /I ";%JAVA_BIN%;" >nul

if errorlevel 1 (
    echo User JAVA_HOME= %FOUND_JDK%
    echo User Path contains JDK bin= False
    echo ERROR: Failed to verify user Path.
    exit /b 1
)

echo User JAVA_HOME= %FOUND_JDK%
echo User Path contains JDK bin= True

set "JAVA_HOME=%FOUND_JDK%"
set "PATH=%JAVA_BIN%;%PATH%"

echo [4/4] Checking Java in this terminal...
java -version
javac -version

echo.
echo Done.
echo Close all VS Code windows, open VS Code again, and run:
echo java -version
echo javac -version
echo .\gradlew.bat build
