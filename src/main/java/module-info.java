module petbotgroupid.petbot {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.jsoup;
    requires jakarta.mail;
    requires java.desktop;
    requires htmlunit;
    requires org.seleniumhq.selenium.chrome_driver;
    requires io.github.bonigarcia.webdrivermanager;

    opens petbotgroupid.petbot to javafx.fxml;
    exports petbotgroupid.petbot;
}