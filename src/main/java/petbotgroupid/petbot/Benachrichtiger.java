package petbotgroupid.petbot;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import io.github.bonigarcia.wdm.WebDriverManager;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Benachrichtiger {
    public static void main(String[] args) {
        // WebDriver für Chrome automatisch herunterladen
        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver();

        try {
            // URL der Anzeige
            String url = "HIER_DIE_ANZEIGEN_URL_EINFÜGEN";
            driver.get(url);

            // Nachricht aus Datei laden
            String nachricht = new String(Files.readAllBytes(Paths.get("nachricht.txt")));

            // Warte kurz, falls Elemente per JS geladen werden
            Thread.sleep(2000);

            // Nachricht in Textfeld eingeben
            WebElement textfeld = driver.findElement(By.name("message"));
            textfeld.sendKeys(nachricht);

            // Button suchen und klicken
            WebElement sendButton = driver.findElement(By.cssSelector("button.viewad-contact-submit"));
            sendButton.click();

            System.out.println("Nachricht erfolgreich gesendet!");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Browser schließen
            driver.quit();
        }
    }
}
