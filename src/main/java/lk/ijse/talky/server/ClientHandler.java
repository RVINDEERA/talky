package lk.ijse.talky.server;

import lk.ijse.talky.controller.ChatFormController;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ClientHandler implements  Runnable{
    public static ChatFormController chatFormController;
    private final Socket socket;
    private final DataInputStream dataInputStream;
    private final DataOutputStream dataOutputStream;
    // private final String clientName;
    private static ArrayList<DataOutputStream> clientOutputStreams;


    public ClientHandler(Socket socket, ArrayList<DataOutputStream> clientOutputStreams) throws IOException {
        this.socket = socket;
        this.clientOutputStreams = clientOutputStreams;
        dataInputStream = new DataInputStream(socket.getInputStream());
        dataOutputStream = new DataOutputStream(socket.getOutputStream());
        clientOutputStreams.add(dataOutputStream);
    }

    @Override
    public void run() {
        try {
            while (true) {
                String msg = dataInputStream.readUTF();
                if (msg.equals("TEXT")) {
                    String allMsg = dataInputStream.readUTF();
                    if (allMsg != null) {
                        broadcastMessage(allMsg); // Broadcast the message to all clients
                    }
                } else if (msg.equals("IMAGE")) {
                    String img = dataInputStream.readUTF();
                    int fileSize = dataInputStream.readInt();
                    byte[] fileData = new byte[fileSize];
                    dataInputStream.readFully(fileData);
                    if (fileData != null) {
                        broadcastImage(fileData,img);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
                dataInputStream.close();
                dataOutputStream.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void broadcastImage(byte[] fileData, String img) {
        for (DataOutputStream clientOutputStream : clientOutputStreams) {
            try {
                clientOutputStream.writeUTF("IMAGE");
                clientOutputStream.writeUTF(img);
                clientOutputStream.writeInt(fileData.length);
                clientOutputStream.write(fileData);
                clientOutputStream.flush();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void broadcastMessage(String message) throws IOException {
        for (DataOutputStream outputStream : clientOutputStreams) {
            try {
                outputStream.writeUTF("TEXT");
                outputStream.writeUTF(message);
                outputStream.flush();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
