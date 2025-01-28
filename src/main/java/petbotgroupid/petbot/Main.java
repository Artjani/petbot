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
            URL location = GuiController.class.getResource("GuiFenster.fxml");
            FXMLLoader loader = new FXMLLoader(location);
            Parent root = loader.load() ;
//            Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("/petbotgroupid/petbot/GuiFenster.fxml"));

            Scene scene = new Scene(root,600,400);
            scene.getStylesheets().add(GuiController.class.getResource("style.css").toExternalForm());
//			System.out.println(GuiController.class.getResource("AJP-Pixellogo.PNG"));
			Image logo = new Image(GuiController.class.getResource("AJP-Pixellogo.PNG").toExternalForm());
            primaryStage.setTitle("Για τον ελληνικό μου αδελφό!"); // "Für meinen griechischen Bruder!"
//			primaryStage.getIcons().add(logo);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        //TODO: .jar muss noch geteste werden, dazu pfade ändern!
        // auch hier "/application/GuiFenster.fxml", "/application/application.css", "/application/AJP-Pixellogo.PNG"


        launch(args);
    }
}
