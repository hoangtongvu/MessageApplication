package UI.Conversation;

import UI.BaseSC;
import UI.FxmlPathAnnotation;
import UI.SCFactory;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;
import animatefx.animation.*;

@FxmlPathAnnotation("/fxml/Conversation/ConversationScene.fxml")
public class ConversationSC extends BaseSC implements Initializable
{


    @FXML private VBox conversationVbox;
    @FXML private TextArea userTextArea;
    @FXML private ScrollPane messageScrollPane;
    @FXML private ImageView confirmButtonImageView;
    @FXML private HBox userMessagePlaceHolder;


    private final List<MessageBlock> messageBlocks;


    public ConversationSC()
    {
        this.messageBlocks = new ArrayList<>();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        this.AddTextAreaKeyCombination();
        this.AddUserMessageProfile();
    }

    private void AddTextAreaKeyCombination()
    {
        this.userTextArea.addEventFilter(KeyEvent.KEY_PRESSED, keyEvent ->
        {
            KeyCode keyCode = keyEvent.getCode();
            if(keyEvent.isShiftDown() && keyCode == KeyCode.ENTER)
            {
                userTextArea.appendText("\n");
                return;
            }
            if(keyCode == KeyCode.ENTER)
            {
                OnUserConfirmInstruction();
                keyEvent.consume();
            }

        });
    }

    private void AddUserMessageProfile()
    {
        UserMessageProfile userMessageProfile = (UserMessageProfile) SCFactory.CreateInstance(UserMessageProfile.class);
        this.userMessagePlaceHolder.getChildren().add(userMessageProfile.getRoot());
    }


    private MessageBlock CreateMessageBlock()
    {
        MessageBlock messageBlock = (MessageBlock) SCFactory.CreateInstance(MessageBlock.class);
        this.conversationVbox.getChildren().add(messageBlock.getRoot());
        this.messageBlocks.add(messageBlock);
        new FadeInUp(messageBlock.getRoot()).play();
        return messageBlock;
    }

    @FXML
    private void OnUserConfirmInstruction()
    {

        String input = this.userTextArea.getText();
        if (input.isEmpty()) return;

        //play animation for send button.
        this.PlayButtonAnimation();

        this.userTextArea.clear();


        //create user message block
        MessageBlock userMessageBlock = this.CreateMessageBlock();
        userMessageBlock.setText(input);
        userMessageBlock.SetUserRole();
        userMessageBlock.PlayOnAppearAnimation();

        this.ScrollToLatestMessage();


        //get task.
        Task<String> task = this.GetResponseProcessingTask();

        MessageBlock botMessageBlock = this.CreateMessageBlock();
        botMessageBlock.setText("...");
        botMessageBlock.PlayOnAppearAnimation();

        task.messageProperty().addListener((observableValue, s, t1) -> botMessageBlock.setText(t1));
        task.messageProperty().addListener((observableValue, s, t1) -> this.ScrollToLatestMessage());

        Thread processingResponseThread = new Thread(task);

        processingResponseThread.setDaemon(true);
        processingResponseThread.start();

    }

    private Task<String> GetResponseProcessingTask()
    {

        return new Task<>() {

            @Override
            protected String call() throws InterruptedException {

                //play waiting animation.
                Timeline waitingTimeline = this.GetWaitingAnimationTimeline();
                waitingTimeline.play();

                //Waiting.
                this.Waiting();

                //stop animation on get response.
                waitingTimeline.stop();

                this.UpdateResponseWithFakeEffect(this.GetRandomResponse());

                return "response";
            }

            private void Waiting()
            {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

            private String GetRandomResponse()
            {
                String base = "<This is random response>";
                Random rand = new Random();
                String response = "";

                for (int i = 0; i < rand.nextInt(1, 9); i++)
                {
                    response += base;
                }
                return response;
            }

            private Timeline GetWaitingAnimationTimeline()
            {
                double delaySecond = 0.2;
                char dotChar = '●';
                KeyFrame oneDotFrame = new KeyFrame(Duration.seconds(1 * delaySecond), e -> this.updateMessage(new String(new char[1]).replace('\0', dotChar)));

                KeyFrame twoDotFrame = new KeyFrame(Duration.seconds(2 * delaySecond), e -> this.updateMessage(new String(new char[2]).replace('\0', dotChar)));

                KeyFrame threeDotFrame = new KeyFrame(Duration.seconds(3 * delaySecond), e -> this.updateMessage(new String(new char[3]).replace('\0', dotChar)));
                Timeline timeline = new Timeline(oneDotFrame, twoDotFrame, threeDotFrame);
                timeline.setCycleCount(Animation.INDEFINITE);
                return timeline;
            }

            private void UpdateResponseWithFakeEffect(String response) throws InterruptedException
            {
                for (int i = 1; i < response.length(); i = i + 2)
                {
                    this.updateMessage(response.substring(0, i));
                    Thread.sleep(50);
                }
                this.updateMessage(response);
            }

        };

    }

    private void ScrollToLatestMessage()
    {
        this.messageScrollPane.setVvalue(1);
    }

    private void PlayButtonAnimation()
    {
        FadeOutRight fadeOutRight = new FadeOutRight(this.confirmButtonImageView);
        fadeOutRight.setSpeed(2);
        fadeOutRight.playOnFinished(new FadeInLeft(this.confirmButtonImageView));
        fadeOutRight.play();
    }


}
