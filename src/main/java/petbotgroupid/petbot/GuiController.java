package petbotgroupid.petbot;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class GuiController {

    @FXML
    public Label marke;
    @FXML
    public Label preis;
    @FXML
    public Label botstatus;
    @FXML
    public Button speichern;
    @FXML
    public Button loeschen;
    @FXML
    public Button nachricht;
    @FXML
    public Button starten;
    @FXML
    public Button stoppen;
    @FXML
    public TableView<Datensatz> vergleichsdaten;
    @FXML
    public TableColumn<Datensatz, String> markeSpalte;
    @FXML
    public TableColumn<Datensatz, Double> preisSpalte;
    @FXML
    public TextField markeTextfeld;
    @FXML
    public TextField preisTextfeld;

    private Datensauger sauger;
    public static ObservableList<Datensatz> datenliste = FXCollections.observableArrayList();

    private String csvPfad = "vhs.csv";
    private String txtPfad = "nachricht.txt";
    private ScheduledExecutorService planer;
    public Timer timer;
    public TimerTask task;

    public void initialize() {
        markeSpalte.setCellValueFactory(new PropertyValueFactory<Datensatz, String>("marke"));
        preisSpalte.setCellValueFactory(new PropertyValueFactory<Datensatz, Double>("preis"));
        vergleichsdaten.setItems(datenliste);
        ladenAusDatei();
        speichern.setDisable(true);
        loeschen.setDisable(true);
        stoppen.setDisable(true);

        markeTextfeld.textProperty().addListener((observable, oldValue, newValue) -> {
            validiereEingaben();
        });
        preisTextfeld.textProperty().addListener((observable, oldValue, newValue) -> {
            validiereEingaben();
        });
        markeTextfeld.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                vergleichsdaten.getSelectionModel().clearSelection();
            }
        });
        preisTextfeld.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                vergleichsdaten.getSelectionModel().clearSelection();
            }
        });
        vergleichsdaten.getSelectionModel().selectedItemProperty().addListener((observable,oldValue, newValue) -> {
            loeschen.setDisable(newValue == null);
        });
    }
    @FXML
    public void speichern() {
        String marke = markeTextfeld.getText();
        double preis = Double.parseDouble(preisTextfeld.getText());

        if (!marke.isEmpty() && !preisTextfeld.getText().isEmpty()) {
            Datensatz neuerDatensatz = new Datensatz(marke, preis);

            datenliste.add(neuerDatensatz);
            vergleichsdaten.setItems(datenliste);
            markeTextfeld.clear();
            preisTextfeld.clear();

            speichernInDatei();
        }
    }
    @FXML
    public void loeschen() {
        Datensatz auswahl = vergleichsdaten.getSelectionModel().getSelectedItem();

        if (auswahl != null) {
            vergleichsdaten.getItems().remove(auswahl);
            speichernInDatei();
            vergleichsdaten.getSelectionModel().clearSelection();
        }
    }
    public void dateitesten() throws IOException {
        Path path = Path.of(csvPfad);
        String content = Files.readString(path);
        System.out.println(content);

    }
    @FXML
    public void nachrichtOeffnen() {

        File nachricht = new File(txtPfad);
        if(nachricht.exists()) {
            try {
                Desktop.getDesktop().edit(nachricht); // oeffnet mit Standard-Editor
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("nachricht.txt nicht vorhanden");
        }
    }
    @FXML
    public void starten() {
        sauger = new Datensauger();

        if(planer == null || planer.isShutdown()) {
            planer = Executors.newScheduledThreadPool(1);
            planer.scheduleAtFixedRate(() -> {

                sauger.datenHolen();
                sauger.beschreibungChecken();

                // wenn nicht soll die nachtricht in das nachrichtenfenster gesteckt und abgeschickt werden
                // verschickeNachricht()
            }, 0, 10, TimeUnit.SECONDS); // Wie oft werden Daten geholt, steht hier --------
        }
        botstatus.setText("PetBot läuft...");
        starten.setDisable(true);
        stoppen.setDisable(false);
    }

    @FXML
    public void stoppen() {
        if(planer != null || !planer.isShutdown()) {
            planer.shutdownNow();
        }
        botstatus.setText("PetBot gestoppt");
        starten.setDisable(false);
        stoppen.setDisable(true);
    }

    public void vergleicheAusgabe() {

    }
    public void speichernInDatei() {
        File datei = new File(csvPfad);

        try (PrintWriter writer = new PrintWriter(new FileWriter(datei))) {
            for (Datensatz datensatz : datenliste) {
                writer.printf("%s,%.0f%n", datensatz.getMarke(), datensatz.getPreis());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void ladenAusDatei() {
        File file = new File(csvPfad);
        if (!file.exists()) {
            System.err.println("Datei beim laden nicht gefunden: " + file.getAbsolutePath());
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(csvPfad))) {
            String datensatz = reader.readLine();

            while(datensatz != null) {
                String[] datenteile = datensatz.split(",");

                if (datenteile.length == 2) {
                    String marke = datenteile[0];
                    double preis = Double.parseDouble(datenteile[1]);

                    datenliste.add(new Datensatz(marke, preis));
                }
                datensatz = reader.readLine();
            }
            vergleichsdaten.setItems(datenliste);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void validiereEingaben() {
        String markeText = markeTextfeld.getText();
        String preisText = preisTextfeld.getText();

        boolean markePasst = !markeText.isEmpty();
        boolean preisPasst = false;

        try {
            Double.parseDouble(preisText);
            preisPasst = true;
        } catch (NumberFormatException e) {
            preisPasst = false;
        }
        speichern.setDisable(!(markePasst && preisPasst));
    }
}

