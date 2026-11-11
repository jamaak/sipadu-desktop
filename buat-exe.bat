@echo off
REM Membuat SIPADU.exe (folder aplikasi mandiri). Butuh JDK 17+ (javac, jar, jpackage ada di PATH).
setlocal
cd /d "%~dp0"

if exist build rmdir /s /q build
if exist dist rmdir /s /q dist
mkdir build\classes
mkdir build\input

javac -encoding UTF-8 -sourcepath src\main\java -d build\classes src\main\java\com\sipadu\gui\DesktopMain.java
if errorlevel 1 goto error

jar --create --file build\input\sipadu.jar --main-class com.sipadu.gui.DesktopMain -C build\classes .
if errorlevel 1 goto error

jpackage --type app-image --name SIPADU --input build\input --main-jar sipadu.jar --main-class com.sipadu.gui.DesktopMain --dest dist
if errorlevel 1 goto error

echo.
echo Selesai. Jalankan: dist\SIPADU\SIPADU.exe
pause
exit /b 0

:error
echo.
echo Gagal. Pastikan JDK 17 atau lebih baru terpasang dan javac/jar/jpackage ada di PATH.
pause
exit /b 1
