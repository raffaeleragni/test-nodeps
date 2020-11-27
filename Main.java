import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import static java.nio.charset.StandardCharsets.UTF_8;
import java.time.Instant;
import java.util.Map;
import java.util.function.Supplier;

public class Main {
  public static void main(String[] args) throws IOException {
    new Server(8080, Map.of(
      "/", () -> "Hello world",
      "/test", () -> "Test response",
      "/time", () -> Instant.now().toString()
    )).start();
  }
}

class Server {
  final HttpServer server;
  
  Server(int port, Map<String, Supplier<String>> endpoints) {
    try {
      server = HttpServer.create(new InetSocketAddress(port), 0);
      endpoints.entrySet().stream().forEach(e -> {
        server.createContext(e.getKey(), handlerOf(e.getValue()));
      });
    } catch (IOException e) {
      throw new RuntimeException(e.getMessage(), e);
    }
  }
  
  void start() {
    server.start();
  }
  
  void stop() {
    server.stop(0);
  }

  HttpHandler handlerOf(Supplier<String> function) {
    return ctx -> {
      try {
        writeBody(ctx, 200, function.get());
      } catch (RuntimeException e) {
        writeBody(ctx, 500, e.getMessage());
      }
    };
  }

  void writeBody(HttpExchange ctx, int status, String body) {
    try (var out = new BufferedWriter(new OutputStreamWriter(ctx.getResponseBody(), UTF_8))) {
      ctx.sendResponseHeaders(status, body.length());
      out.write(body);
    } catch (IOException e) {
      throw new RuntimeException(e.getMessage(), e);
    }
  }
}
