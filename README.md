# Message-Player

Pure-Java demonstration of two players exchanging messages:

1. **Same JVM** – two threads connected via blocking queues
2. **Separate JVMs** – two processes connected via TCP sockets

## Build
```bash
mvn compile

Run – single process
mvn exec:java "-Dexec.mainClass=com.example.player.Main" "-Dexec.args=--mode=inprocess"

Run – separate processes
**Windows:**
start.bat

Linux/macOS:
./start.sh
```

**Class**               **responsibilities**
Message –               immutable payload carrier
Messenger –             transport abstraction
InProcessMessenger –    queue-based, same-JVM implementation
SocketMessenger –       TCP-based, cross-JVM implementation
Player –                generic responder logic
InitiatorPlayer –       drives exactly 10 round-trips
Main –                  launches single-JVM demo
PlayerServer –          TCP responder program
PlayerClient –          TCP initiator program
ShutdownSignal –        coordinates graceful stop
