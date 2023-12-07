package UI;


import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;

public class SCFactory
{
    public static BaseSC CreateInstance(Class<? extends BaseSC> clazz)
    {
        FxmlPathAnnotation annotation = clazz.getAnnotation(FxmlPathAnnotation.class);
        String fxmlPath = annotation.value();
        FXMLLoader loader = new FXMLLoader(clazz.getResource(fxmlPath));
        Parent root;
        try {
            root = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        BaseSC baseSC = loader.getController();
        baseSC.setRoot(root);
        return baseSC;
    }



}
