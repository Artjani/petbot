package petbotgroupid.petbot;

import java.io.Serial;
import java.io.Serializable;

public class Datensatz implements Serializable{

    @Serial
    private static final long serialVersionUID = 1L;
    private String marke;
    private double preis;

    public Datensatz(String marke, double preis) {
        this.marke = marke;
        this.preis = preis;
    }

    public String getMarke() {
        return marke;
    }

    public void setMarke(String marke) {
        this.marke = marke;
    }

    public double getPreis() {
        return preis;
    }

    public void setPreis(double preis) {
        this.preis = preis;
    }
}

