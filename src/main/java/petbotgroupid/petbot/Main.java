package petbotgroupid.petbot;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.net.URL;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            Parent root = loadRoot();
            Scene scene = new Scene(root,600,400);
            scene.getStylesheets().add(GuiController.class.getResource("style.css").toExternalForm());
            setStage(primaryStage, scene);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    private Parent loadRoot() throws Exception {
        URL location = GuiController.class.getResource("GuiFenster.fxml");
        FXMLLoader loader = new FXMLLoader(location);
        return loader.load() ;
    }

    private void setStage(Stage primaryStage, Scene scene){
        Image logo = new Image(GuiController.class.getResource("AJP-Pixellogo.PNG").toExternalForm());
        primaryStage.setTitle("Για τον ελληνικό μου αδελφό!"); // "Für meinen griechischen Bruder!"
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {

        launch(args);
    }
}
