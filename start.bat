@echo off
mvn compile || exit /b
echo === starting player2 ===
start /B java -cp target\classes com.example.player.player.PlayerServer 5000
timeout /t 2 /nobreak > nul
echo === starting player1 ===
java -cp target\classes com.example.player.player.PlayerClient localhost 5000
echo === finished ===
