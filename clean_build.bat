@echo off
echo Dang dung Gradle daemon...
call gradlew --stop

echo Dang xoa thu muc build...
timeout /t 2 /nobreak > nul

if exist app\build (
    rmdir /s /q app\build
    echo Da xoa app\build
)

if exist build (
    rmdir /s /q build
    echo Da xoa build
)

if exist .gradle (
    rmdir /s /q .gradle
    echo Da xoa .gradle
)

echo Dang clean project...
call gradlew clean

echo Hoan thanh! Ban co the build lai project.
pause
