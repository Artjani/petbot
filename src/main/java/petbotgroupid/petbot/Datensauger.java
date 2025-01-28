package petbotgroupid.petbot;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.time.Duration;
import java.time.LocalTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Datensauger {

    public void datenHolen() {
        try {
            String url = "https://www.kleinanzeigen.de/s-videorekorder/k0";
            Document doc = Jsoup.connect(url).get();
            Elements vhsListe = doc.select(".aditem");
            String[] titelPreis;

            for (Element vhs:vhsListe) {
                String titel = vhs.select("h2 > .ellipsis").text();
                String preis = vhs.select(".aditem-main--middle--price-shipping--price").text();
                String zeit = vhs.select(".aditem-main--top--right").text();
                formatiereZeit(zeit);
//				System.out.println(titel + " -- " + preis + " -- " + zeit);
//				System.out.println(formatiereZeit(zeit));
            }
//			System.out.println("Daten geholt");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String formatiereZeit(String zeit) {
        String formatierteZeit = "";
        LocalTime jetzt = LocalTime.now();
        Pattern muster = Pattern.compile("^Heute,\\s*(\\d{2}:\\d{2})$");
        Matcher vergleicher = muster.matcher(zeit);

        if(vergleicher.find()) {
            formatierteZeit = vergleicher.group(1);
            LocalTime zeit2 = LocalTime.parse(formatierteZeit);

            Duration zeitspanne = Duration.between(zeit2, jetzt);

            if( zeitspanne.toMinutes() <= 2 && !zeitspanne.isNegative()) { //Spanne wie weit er zurückschaut
                System.out.println(zeit2);

            }

        }

        return formatierteZeit;
    }
}

