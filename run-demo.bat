@echo off
setlocal
cd /d "%~dp0"

echo [1/3] Building FastKeylogger...
call mvn clean install -DskipTests -q
if %ERRORLEVEL% NEQ 0 (
    echo [ERROR] FastKeylogger build failed.
    pause
    exit /b %ERRORLEVEL%
)

powershell -NoProfile -Command "Unblock-File -Path '%USERPROFILE%\.fastcore\native\fastkeyboard\*', '%~dp0release\*' -ErrorAction SilentlyContinue" >nul 2>&1

echo [2/3] Compiling Demo...
cd examples\Demo
call mvn compile dependency:build-classpath "-Dmdep.outputFile=cp.txt" "-DincludeScope=runtime" -q
if %ERRORLEVEL% NEQ 0 (
    echo [ERROR] Demo compilation failed.
    pause
    exit /b %ERRORLEVEL%
)

echo [3/3] Running Demo...
set /p CP=<cp.txt
java --enable-native-access=ALL-UNNAMED -cp "target\classes;%CP%" fastkeylogger.demo.Demo

cd ..\..
pause
