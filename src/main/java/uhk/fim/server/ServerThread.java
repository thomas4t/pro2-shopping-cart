package uhk.fim.server;

import com.google.gson.Gson;
import uhk.fim.model.ShoppingCart;
import uhk.fim.model.ShoppingCartItem;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerThread extends Thread {

    private final ServerSocket serverSocket;

    public ServerThread(int port) throws IOException {
        this.serverSocket = new ServerSocket(port);
    }

    @Override
    public void run() {
        super.run();
        while(true){
            //listen to request
            try {
                Socket socket = serverSocket.accept(); //code waits here until we receive a request
                //server can receive shit ton of requests => make new thread for it
                new Thread(() -> {
                    processRequest(socket);
                    //processRequestJSON(socket);
                }).start();

                System.out.println("Im listening");
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private void processRequest(Socket s){
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));

            String responseContent;

            String requestHeader = in.readLine();
            if(requestHeader.contains("about")) {
                responseContent = "<html><body><h1>About page</h1><p>hahahahahaah</p></body></html>";
            }else {
                responseContent = "<html><body><h1>Home page</h1><p>hahahahahaah</p></body></html>";
            }

            PrintWriter out = new PrintWriter(s.getOutputStream());

            out.println("HTTP/1.1 200 OK");
            out.println("Content-type: text/html");
            out.println("Content-length: " + responseContent.length());
            out.println();
            out.println(responseContent);

            out.flush();
            s.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void processRequestJSON(Socket s){
        try {
            PrintWriter out = new PrintWriter(s.getOutputStream());

            ShoppingCart cart = new ShoppingCart();
            cart.addItem(new ShoppingCartItem("Telefon", 6990, 1));
            cart.addItem(new ShoppingCartItem("Notebook", 29990, 2));

            Gson gson = new Gson();
            String json = gson.toJson(cart);

            String responseContent = "<html><body><p>hahahahahaah</p></body></html>";

            out.println("HTTP/1.1 200 OK");
            out.println("Content-type: application/json");
            out.println("Content-length: " + json.length());
            out.println();
            out.println(json);

            out.flush();
            s.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
