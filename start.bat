@echo off
mvn compile || exit /b
echo === Starting player2 ===
start /B java -cp target\classes com.example.player.player.PlayerServer 5000

REM Wait for server to be ready (timeout since nc is not standard on Windows)
timeout /t 2 /nobreak > nul

echo === Starting player1 ===
java -cp target\classes com.example.player.player.PlayerClient localhost 5000

REM Server will exit automatically after shutdown signal
timeout /t 1 /nobreak > nul
echo === All done ===
```