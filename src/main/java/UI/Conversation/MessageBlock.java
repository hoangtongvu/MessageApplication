package UI.Conversation;

import UI.BaseSC;
import UI.FxmlPathAnnotation;
import javafx.animation.Interpolator;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

@FxmlPathAnnotation("/fxml/Conversation/MessageBlock.fxml")
public class MessageBlock extends BaseSC implements Initializable
{
    @FXML private HBox rootHbox;
    @FXML private Circle avatarCircle;
    @FXML private Text messageText;
    @FXML private HBox messageOnlyHBox;


    private final String radiusStyle = "-fx-background-radius: 15;";
    private final double maxWrappingWidth = 300;
    private boolean isUserMessage = false;

    public void setText(String content) {
        this.messageText.setWrappingWidth(0);
        this.messageText.setText(content);
        double width = this.messageText.getBoundsInLocal().getWidth();
        if (width >= this.maxWrappingWidth)
            this.messageText.setWrappingWidth(this.maxWrappingWidth);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
    }

    public void SetUserRole()
    {
        String backgroundColorStyle = "-fx-background-color: #3d5c95;";
        this.messageOnlyHBox.setStyle(backgroundColorStyle + this.radiusStyle);// #dfe3ee
        this.RemoveAvatarForUser();
        this.SwapMessagePosition();
        this.isUserMessage = true;
    }

    private void SwapMessagePosition()
    {
        this.rootHbox.setAlignment(Pos.TOP_RIGHT);
    }

    private void RemoveAvatarForUser()
    {
        this.rootHbox.getChildren().remove(this.avatarCircle);
    }

    public void PlayOnAppearAnimation()
    {
        this.PlayScaleAnimation();
        this.PlayTranslateAnimation();
    }

    private void PlayScaleAnimation()
    {
        ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(0.3), this.messageOnlyHBox);

        double startValue = 0.2;
        double topValue = 1.3;
        double finalValue = 1;

        scaleTransition.setInterpolator(Interpolator.EASE_OUT);
        scaleTransition.setFromX(startValue);
        scaleTransition.setFromY(startValue);
        scaleTransition.setToX(topValue);
        scaleTransition.setToY(topValue);


        ScaleTransition scaleTransition1 = new ScaleTransition(Duration.seconds(0.1), this.messageOnlyHBox);
        scaleTransition1.setInterpolator(Interpolator.EASE_BOTH);
        scaleTransition1.setFromX(topValue);
        scaleTransition1.setFromY(topValue);
        scaleTransition1.setToX(finalValue);
        scaleTransition1.setToY(finalValue);

        scaleTransition.setOnFinished(e -> scaleTransition1.play());


        scaleTransition.play();
    }

    private void PlayTranslateAnimation()
    {
        TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(0.3), this.messageOnlyHBox);

        double x = -50;
        if (this.isUserMessage) x *= -1;
        translateTransition.setInterpolator(Interpolator.EASE_OUT);
        translateTransition.setFromX(x);
        translateTransition.setToX(-x / 2);

        TranslateTransition translateTransition1 = new TranslateTransition(Duration.seconds(0.1), this.messageOnlyHBox);
        translateTransition1.setInterpolator(Interpolator.EASE_BOTH);
        translateTransition1.setFromX(-x / 2);
        translateTransition1.setToX(0);

        translateTransition.setOnFinished(e -> translateTransition1.play());

        translateTransition.play();

    }


}
