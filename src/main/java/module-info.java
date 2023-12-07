module main.messageapplication {
    requires javafx.controls;
    requires javafx.fxml;
    requires AnimateFX;


    exports Main;
    opens Main to javafx.fxml;

    exports UI.Conversation;
    opens UI.Conversation to javafx.fxml;
}