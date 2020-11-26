package uhk.fim.server;

import java.io.IOException;

public class WebServer {
    private final int port;

    public WebServer(int port) {
        this.port = port;

    }

    private void start() throws IOException {
        // Create new thread for the server to run on
        ServerThread thread = new ServerThread(port);
        thread.start();

        System.out.println("Server launched on http://localhost:" + port);

    }

    public static void main(String[] args) {
        WebServer server = new WebServer(8080);
        try {
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error occured during launch: " + e.getMessage());
        }

    }
}
