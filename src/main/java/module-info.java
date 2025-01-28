module petbotgroupid.petbot {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.jsoup;
    requires java.desktop;

    opens petbotgroupid.petbot to javafx.fxml;
    exports petbotgroupid.petbot;
}