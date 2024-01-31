package lk.ijse.talky.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import lk.ijse.talky.dto.UserDto;

public class ChatFormController {
    @FXML
    private AnchorPane emogiPane;
    @FXML
    private Button btnSend;

    @FXML
    private AnchorPane chatPain;

    @FXML
    private Label lblName;

    @FXML
    private VBox vBox;
    @FXML
    private ScrollPane scrollPane;

    @FXML
    private TextField txtTypemsg;
    private UserDto userDto;
    private LoginFormController loginFormController;

    public void initialize(){
        lblName.setText(LoginFormController.name);
    }
    @FXML
    void btnSendEmojiOnActioni(ActionEvent event) {

    }

    @FXML
    void btnSendImageOnAction(ActionEvent event) {

    }

    @FXML
    void btnSendOnAction(ActionEvent event) {

    }

    @FXML
    void txtTypingOnAction(ActionEvent event) {

    }

}
