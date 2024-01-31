package lk.ijse.talky.client;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.talky.controller.ChatFormController;
import lk.ijse.talky.dto.UserDto;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.net.Socket;

public class Client implements Runnable , Serializable {
    private final String name;

    public String getName() {
        return name;
    }

    private final Socket socket;
    private final DataInputStream dataInputStream;
    private final DataOutputStream dataOutputStream;
    private ChatFormController chatFormController;

    public Client(String name) throws IOException {
        this.name=name;
//        if(pw==null){
//        }else{
//            this.pw=pw;
//        }
        this.socket = new Socket("localhost", 1200);
        this.dataInputStream = new DataInputStream(socket.getInputStream());
        this.dataOutputStream = new DataOutputStream(socket.getOutputStream());
        dataOutputStream.writeUTF(name);
        dataOutputStream.flush();

        try {
            loadScene();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    private void loadScene() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/chat_form.fxml"));
        AnchorPane anchorPane = loader.load();
        Scene scene = new Scene(anchorPane);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.setTitle(name+ "'s Talky");
        stage.setAlwaysOnTop(true);
        stage.setResizable(false);
        stage.show();

        stage.setOnCloseRequest(event -> {
            try{
                dataInputStream.close();
                dataOutputStream.close();
                socket.close();
            }catch (IOException e){
                System.out.println(e);
            }
        });
    }

    @Override
    public void run() {
        String message = "";
        while(!message.equals("exit")){
            try{
                message = dataInputStream.readUTF();
                if(message.equals("*image*")){
                    recieveImage();
                }else{
                    chatFormController.writeMessage(message);
                }
            }catch (IOException e){
                try {
                    socket.close();
                }catch (IOException ex){
                    System.out.println(ex);
                }
            }
        }
    }

    public void sendImage(byte[] bytes) throws IOException {
        dataOutputStream.writeUTF("*image*");
        dataOutputStream.writeInt(bytes.length);
        dataOutputStream.write(bytes);
        dataOutputStream.flush();
    }




    private void recieveImage() throws IOException {
        String message = dataInputStream.readUTF();
        int size = dataInputStream.readInt();
        byte[] bytes = new byte[size];
        dataInputStream.readFully(bytes);
        System.out.println(name+"- Image Recieved from "+ message);
        chatFormController.setImage(bytes,message);
    }


}
