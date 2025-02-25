package petbotgroupid.petbot;

import java.io.Serial;
import java.io.Serializable;

public class Datensatz implements Serializable{

    @Serial
    private static final long serialVersionUID = 1L;
    private String marke;
    private double preis;
    private String zeitStr;
    private String link;

    public Datensatz(String marke, double preis, String zeitStr, String link) {
        this.marke = marke;
        this.preis = preis;
        this.zeitStr = zeitStr;
        this.link = link;
    }
    public Datensatz (String marke, double preis){
        this(marke, preis, "", "");

    }

    public String getMarke() {return marke;}
    public void setMarke(String marke) {this.marke = marke;}
    public double getPreis() {return preis;}
    public void setPreis(double preis) {this.preis = preis;}
    public String getLink() {return link;}
    public void setLink(String link) {this.link = link;}
    public void setZeitStr(String zeitStr) {this.zeitStr = zeitStr;}
    public String getZeitStr() {return zeitStr;}

    @Override
    public String toString() {
        return zeitStr + " - " + marke + " - " + preis + " - " + link;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Datensatz datensatz = (Datensatz) obj;
        return Double.compare(datensatz.preis, preis) == 0 && marke.equalsIgnoreCase(datensatz.marke);
    }
}

