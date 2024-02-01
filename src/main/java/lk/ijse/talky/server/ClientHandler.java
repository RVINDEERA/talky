package lk.ijse.talky.server;

import lk.ijse.talky.controller.ChatFormController;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ClientHandler implements  Runnable{
    public static final List<ClientHandler> clientHandlersList = new ArrayList<>();
    public static ChatFormController chatFormController;
    private final Socket socket;
    private final DataInputStream dataInputStream;
    private final DataOutputStream dataOutputStream;
    private final String clientName;

    public ClientHandler(Socket socket) throws IOException {
        this.socket = socket;
        dataInputStream= new DataInputStream(socket.getInputStream());
        dataOutputStream = new DataOutputStream(socket.getOutputStream());
        clientName = dataInputStream.readUTF();
        clientHandlersList.add(this);
    }

    @Override
    public void run() {
        while(socket.isConnected()){
            try{
                String message =dataInputStream.readUTF();
                if(message.equals("*image*")){
                    receiveImage();
                }else{
                    for(ClientHandler clientHandler:clientHandlersList){
                        if(!clientHandler.clientName.equals(clientName)){
                            clientHandler.sendMessage(clientName,message);
                        }
                    }
                }
            }catch (IOException e){
                clientHandlersList.remove(this);
                System.out.println(clientName+" removed");
                System.out.println(clientHandlersList.size());
                break;
            }
        }

    }

    private void receiveImage() throws IOException {
        int size = dataInputStream.readInt();
        byte[] bytes= new byte[size];
        dataInputStream.readFully(bytes);
        for (ClientHandler clientHandler : clientHandlersList) {
            if (!clientHandler.clientName.equals(clientName)) {
                clientHandler.sendImage(clientName, bytes);
                System.out.println(clientName+" - image sent ");
            }
        }
    }

    private void sendImage(String clientName, byte[] bytes) throws IOException {
        dataOutputStream.writeUTF("*image*");
        dataOutputStream.writeUTF(clientName);
        dataOutputStream.writeInt(bytes.length);
        dataOutputStream.write(bytes);
        dataOutputStream.flush();
        System.out.println("Image Sent");

    }

    private void sendMessage(String clientName, String message) throws IOException {
        dataOutputStream.writeUTF(clientName+" : "+message);
        dataOutputStream.flush();
    }
}
