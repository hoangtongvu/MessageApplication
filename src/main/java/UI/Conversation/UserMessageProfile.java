package UI.Conversation;

import UI.BaseSC;
import UI.FxmlPathAnnotation;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.shape.Circle;

@FxmlPathAnnotation("/fxml/Conversation/UserMessageProfile.fxml")
public class UserMessageProfile extends BaseSC
{
    @FXML private Circle avatarCircle;
    @FXML private Label nameLabel;
    @FXML private Label onlineStatusLabel;


    public Circle getAvatarCircle() {
        return avatarCircle;
    }
}
