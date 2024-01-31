package lk.ijse.talky.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server implements Runnable {
    private static Server server;
    private final ServerSocket serverSocket;
    private Server() throws IOException {
        serverSocket = new ServerSocket(1200);
        System.out.println("Server Started Successfully");
    }
    public static Server getServerSocket() throws IOException {
        return server == null ? server = new Server() : server;
    }
    @Override
    public void run() {
        while (!serverSocket.isClosed()){
            try {
                Socket socket = serverSocket.accept();
                ClientHandler clientHandler = new ClientHandler(socket);
                Thread thread = new Thread(clientHandler);
                thread.start();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
