package Main;

import UI.Conversation.ConversationSC;
import UI.SCFactory;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application
{

    @Override
    public void start(Stage stage) throws IOException
    {
        Parent root = SCFactory.CreateInstance(ConversationSC.class).getRoot();
        Scene scene = new Scene(root, 500, 800);
        stage.setTitle("MessageApp!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}