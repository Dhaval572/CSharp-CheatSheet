@echo off
REM Build script for C# Interpreter
echo Building C# Interpreter using C++17...

cd /d "%~dp0"
cd ../../..

g++ -std=c++17 -O2 -o CSharpInterpreter.exe src/main/cpp/CSharpInterpreter.cpp

if %ERRORLEVEL% EQU 0 (
    echo ✅ Build successful! CSharpInterpreter.exe created.
    echo Testing interpreter...
    echo.
    .\CSharpInterpreter.exe
) else (
    echo ❌ Build failed!
)