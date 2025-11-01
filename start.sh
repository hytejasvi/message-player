#!/usr/bin/env bash
set -e
mvn -q compile
java -cp target/classes com.example.player.PlayerServer 5000 &
SERVER_PID=$!
sleep 1
java -cp target/classes com.example.player.PlayerClient localhost 5000
kill $SERVER_PID 2>/dev/null || true
wait $SERVER_PID 2>/dev/null || true
echo "All done."