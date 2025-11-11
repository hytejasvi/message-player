# Message-Player

Pure-Java demonstration of two players exchanging messages:

1. **Same JVM** – two threads connected via blocking queues
2. **Separate JVMs** – two processes connected via TCP sockets

## Build & Run

### Build
```bash
mvn compile
```

### Run – Single Process
```bash
mvn exec:java "-Dexec.mainClass=com.example.player.Main" "-Dexec.args=--mode=inprocess"
```

### Run – Separate Processes

**Windows:**
```cmd
start.bat
```

**Linux/macOS:**
```bash
chmod +x start.sh
./start.sh
```

## Architecture

| Class | Responsibility |
|-------|---------------|
| `Message` | Immutable payload carrier |
| `Messenger` | Transport abstraction |
| `InProcessMessenger` | Queue-based, same-JVM implementation |
| `SocketMessenger` | TCP-based, cross-JVM implementation |
| `Player` | Generic responder logic |
| `InitiatorPlayer` | Drives exactly 10 round-trips |
| `Main` | Launches single-JVM demo |
| `PlayerServer` | TCP responder program |
| `PlayerClient` | TCP initiator program |

## Requirements
- Java 17+
- Maven 3.6+
- `nc` (netcat) for start.sh port checking