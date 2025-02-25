package petbotgroupid.petbot;

import org.htmlunit.BrowserVersion;
import org.htmlunit.WebClient;
import org.htmlunit.html.HtmlPage;
import org.htmlunit.html.HtmlSubmitInput;
import org.htmlunit.html.HtmlTextArea;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import petbotgroupid.petbot.GuiController;

public class Datensauger {

    List<String> gepruefteLinks = new ArrayList<>();

    // testlink zu Petros= https://www.kleinanzeigen.de/s-anzeige/suche-videorecorder-videorekorder-vhs-recorder-vhs-rekorder-vcr/2574915092-175-9306

    public void datenHolen() {
        try {
            String baseUrl = "https://www.kleinanzeigen.de/";
            String vhsUrl = baseUrl + "s-videorekorder/k0";
            Document doc = Jsoup.connect(vhsUrl).get();
            Elements vhsListe = doc.select(".aditem");

            for (Element vhs:vhsListe) {

                String zeitStr = vhs.select(".aditem-main--top--right").text();
                if (formatiereUndVergleicheZeit(zeitStr)){

                    String titel = vhs.select("a[class^=ellipsis], span[class^=ellipsis]").text();
                    String link = baseUrl + vhs.select("a[class^=ellipsis], span[class^=ellipsis]").attr("href");
                    // falls href leer ist, wird data-url getestet
                    if (link.equals(baseUrl)){
                        link = baseUrl + vhs.select("span[class^=ellipsis]").attr("data-url");
                    }



                    String preisStr = vhs.select(".aditem-main--middle--price-shipping--price").text();
                    double preis = formatierePreisString(preisStr);

                    for (Datensatz datensatz : GuiController.datenliste) {
                        if (titel.toLowerCase().contains(datensatz.getMarke().toLowerCase())) {
                            if (preis <= datensatz.getPreis()) {

                                if (gepruefteLinks.contains(link)) {
                                    System.out.println("doppelter link");
                                    continue;
                                }
                                gepruefteLinks.add(link);
                                System.out.println(zeitStr + ": " + titel + " -> Preis: " + preis);
                            }
                        }
                    }
                }
            }
            gepruefteLinks.forEach(System.out::println); // Methodenreferenz, == geprüfteLinks.forEach(link -> System.out.println(link));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void beschreibungChecken() {
        try {
            for (String link: gepruefteLinks) {
                Document document = Jsoup.connect(link).get();

                Element beschreibung = document.select("#viewad-description-text.text-force-linebreak").first();

                String beschreibungText = (beschreibung != null) ? beschreibung.text() : "";

                 if (!beschreibungText.toLowerCase().contains("kaputt") && !beschreibungText.toLowerCase().contains("defekt")) {
                     verschickeNachricht();
                 }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void verschickeNachricht() throws IOException {
        System.out.println("hier sollte nun eine Nachricht verschickt werden...");
        /*WebClient webClient = new WebClient(BrowserVersion.CHROME);
        HtmlPage seite = webClient.getPage("https://www.kleinanzeigen.de/s-anzeige/suche-videorecorder-videorekorder-vhs-recorder-vhs-rekorder-vcr/2574915092-175-9306");
        String nachricht = new String(Files.readAllBytes(Paths.get("nachricht.txt")));

        HtmlTextArea textArea = seite.getFirstByXPath("//textarea[@name='message']");
        if (textArea != null) {
            textArea.setText(nachricht);
            System.out.println("Nachricht eingefügt!");
        } else {
            System.out.println("Textfeld nicht gefunden!");
            return;
        }

        HtmlSubmitInput submitButton = seite.getFirstByXPath("//button[@class='button viewad-contact-submit taller']");
        if (submitButton != null) {
            HtmlPage resultPage = submitButton.click();
            System.out.println("Nachricht wurde gesendet!");
        } else {
            System.out.println("Senden-Button nicht gefunden!");
        }*/
    }

    public boolean formatiereUndVergleicheZeit(String zeit) {

        String regex = "^(Heute)?,?\\s*(\\d{2}:\\d{2})(?::\\d{2})?$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(zeit);
        if (matcher.find()) {
            String formatierteZeitStr = matcher.group(2); // uhrzeit ohne "heute/gestern"
            LocalTime uhrzeit = LocalTime.parse(formatierteZeitStr, DateTimeFormatter.ofPattern("HH:mm"));
            LocalTime jetzt = LocalTime.now();
            Duration zeitspanne = Duration.between(uhrzeit, jetzt);
            if (zeitspanne.toMinutes() <= 1800 && !zeitspanne.isNegative()) { //Spanne wie weit er zurückschaut
                return true;
            }
        }
        return false;
    }

    public double formatierePreisString(String preisStr){
        /*return (preisStr.isEmpty() || preisStr.contains("VB") || preisStr.contains(" ")) ? 0.0 :
                Double.parseDouble(preisStr.replaceAll("[^0-9,\\.]", "").replace(",", "."));*/
        if (preisStr.isEmpty() || preisStr.contains("VB") || preisStr.contains("verschenken")) {
            return 0.0;
        }
        String bereinigterPreisStr = preisStr.replaceAll("[^0-9,\\.]", "");
        bereinigterPreisStr = bereinigterPreisStr.replace(",", ".");
        try {
            return Double.parseDouble(bereinigterPreisStr);
        } catch (NumberFormatException e) {
            // Falls das Parsen fehlschlägt, gib 0.0 zurück
            return 0.0;
        }
    }

    public boolean vergleicheMarken(){
        return false;
    }
/*
    public void vergleicheDaten(){
        for (Datensatz kleinanzeige : ebayDaten) {
            for (Datensatz csvDatensatz : Guicontroller.datenliste) {
                if (kleinanzeige.getMarke().equalsIgnoreCase(csvDatensatz.getMarke())) {
                    // Preisprüfung
                    if (kleinanzeige.getPreis() <= csvDatensatz.getPreis()) {
                        System.out.println("Passender Datensatz gefunden: " + kleinanzeige);
                    }
                    // Wenn der Preis nicht passt, wird nichts gemacht
                }
            }
        }
    }

 */

}

