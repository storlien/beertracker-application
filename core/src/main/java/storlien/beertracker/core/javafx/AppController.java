package storlien.beertracker.core.javafx;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.google.common.collect.Multimap;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.util.Duration;
import storlien.beertracker.core.*;

public class AppController {

    private TokenGetter tokenGetter;
    private Timer timer;
    private TimerTask task;

    private Paint green = new Color(0, 0.6, 0, 1.0);
    private Paint red = new Color(1.0, 0.2, 0.2, 1.0);

    @FXML
    private Pane container1, container2;
    @FXML
    private TextField firstName, lastName, cardNewFirst6, cardNewLast4, cardAddFirst6, cardAddLast4, cardRemoveFirst6,
            cardRemoveLast4, dateUpdate;
    @FXML
    private Label labelStatus, statusError, firstFullName, secondFirstName, secondLastName, thirdFirstName,
            thirdLastName, firstSum, secondSum, thirdSum;
    @FXML
    private ListView<Person> listPersonsSettings;
    @FXML
    private ListView<String> listLeaderboard, listLosers;

    @FXML
    public void initialize() {

        container1.setVisible(false);
        container2.setVisible(true);

        // Lager instans av TokenGetter som sørger for at access token er gyldig til
        // enhver tid
        tokenGetter = new TokenGetter();

        // Leser filer og lager liste med Person-objekter på Person.objPersons og laster
        // lastPurchaseHash til PurchaseReader.lastPurchaseHash
        Filehandler.readFile();
        Filehandler.readHashFile();

        timer = new Timer();
        task = new TimerTask() {
            @Override
            public void run() {
                Multimap<String, Double> newPurchases = PurchaseReader
                        .getNewerPurchases(PurchaseReader.getJsonStringLastHash(PurchaseReader.getLastPurchaseHash()));

                if (!newPurchases.isEmpty()) {
                    PurchaseMapper.mapPurchases(newPurchases);
                    updateGUI();
                }

            }

        };

        // timer.schedule(task, 10 * 1000, 5 * 1000); // Delay: 10 sek, Period: 5 sek

        updateGUI();
    }

    @FXML
    public void onAddPerson() {
        String firstName = this.firstName.getText();
        String lastName = this.lastName.getText();
        String cardNewFirst6 = this.cardNewFirst6.getText();
        String cardNewLast4 = this.cardNewLast4.getText();

        try {
            Person.getObjPersons().add(new Person(firstName, lastName, cardNewFirst6, cardNewLast4));
        }

        catch (Exception e) {
            updateErrorStatus(e.getMessage());
            return;
        }

        mapNewPurchases("2022-08-01");

    }

    @FXML
    public void onAddCard() {
        Person person = listPersonsSettings.getSelectionModel().getSelectedItem();

        if (person == null) {
            updateErrorStatus("Velg en person først");
            return;
        }

        try {
            person.addCard(this.cardAddFirst6.getText(), this.cardAddLast4.getText());
        }

        catch (Exception e) {
            updateErrorStatus(e.getMessage());
            return;
        }

        mapNewPurchases("2022-08-01");
    }

    @FXML
    public void onRemoveCard() {
        Person person = listPersonsSettings.getSelectionModel().getSelectedItem();

        if (person == null) {
            updateErrorStatus("Velg en person først");
            return;
        }

        try {
            person.removeCard(cardRemoveFirst6.getText(), cardRemoveLast4.getText());
        }

        catch (Exception e) {
            updateErrorStatus(e.getMessage());
            return;
        }

        mapNewPurchases("2022-08-01");
    }

    @FXML
    public void onRemovePerson() {
        Person person = listPersonsSettings.getSelectionModel().getSelectedItem();

        if (person == null) {
            updateErrorStatus("Velg en person først");
            return;
        }

        Person.getObjPersons().remove(person);

        updateStatus("Person fjernet", red, true);
        updateGUI();
    }

    @FXML
    public void onUpdatePersonsAug22() {
        mapNewPurchases("2022-08-01");
    }

    @FXML
    public void onUpdatePersonsDate() {
        mapNewPurchases(dateUpdate.getText());
    }

    @FXML
    public void onShowLeaderboard() {
        container1.setVisible(false);
        container2.setVisible(true);
    }

    @FXML
    public void onShowSettings() {
        container2.setVisible(false);
        container1.setVisible(true);
    }

    public void onCloseRequest() {
        tokenGetter.cancel();
        timer.cancel();
        task.cancel();
    }

    /**
     * Tilbakestiller summene og mapper kjøpshistorikk på nytt
     *
     * @param date Tidligste dato for kjøp som skal hentes
     */
    public void mapNewPurchases(String date) {

        // Sjekker om dato er på gyldig format, men ikke nødvendigvis en gyldig dato
        if (!Pattern.compile("\\d{4}-\\d{2}-\\d{2}").matcher(date).matches()) {
            updateErrorStatus("Ikke gyldig format på dato");
            return;
        }

        updateStatus("Oppdaterer kjøpshistorikk...", red, false);

        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {
                PurchaseMapper.resetAmountSpent();
                PurchaseMapper.mapPurchases(PurchaseReader.getPurchasesFromDate(date));
                updateGUI();
                return null;
            }
        };

        task.setOnSucceeded(e -> {
            updateStatus("Ferdig!", green, true);
        });

        new Thread(task).start();
    }

    private void updateStatus(String status, Paint color, boolean changeBack) {

        Platform.runLater(new Runnable() {

            @Override
            public void run() {

                labelStatus.setTextFill(color);
                labelStatus.setText(status);

                if (!changeBack) {
                    return;
                }

                Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(5), event -> {
                    labelStatus.setTextFill(green);
                    labelStatus.setText("OK");
                }));

                timeline.setCycleCount(1);
                timeline.play();
            }
        });
    }

    private void updateErrorStatus(String error) {

        Platform.runLater(new Runnable() {

            @Override
            public void run() {

                statusError.setText(error);

                Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(5), event -> {
                    statusError.setText("");
                }));

                timeline.setCycleCount(1);
                timeline.play();
            }
        });
    }

    private void updateGUI() {

        Platform.runLater(new Runnable() {

            @Override
            public void run() {
                List<Person> listPersonsRanked = new ArrayList<>(
                        Person.getObjPersons().stream().sorted().collect(Collectors.toList()));
                List<Person> listPersonsLosers = new ArrayList<>(
                        Person.getObjPersons().stream().sorted(Comparator.reverseOrder()).collect(Collectors.toList()));

                List<String> listPersonsRankedString;
                List<String> listPersonsLosersString;

                Person firstPerson = listPersonsRanked.get(0);
                Person secondPerson = listPersonsRanked.get(1);
                Person thridPerson = listPersonsRanked.get(2);

                firstFullName.setText(firstPerson.getFirstName() + " " + firstPerson.getLastName());
                firstSum.setText((int) firstPerson.getAmountSpent() + " kr");

                secondFirstName.setText(secondPerson.getFirstName());
                secondLastName.setText(secondPerson.getLastName());
                secondSum.setText((int) secondPerson.getAmountSpent() + " kr");

                thirdFirstName.setText(thridPerson.getFirstName());
                thirdLastName.setText(thridPerson.getLastName());
                thirdSum.setText((int) thridPerson.getAmountSpent() + " kr");

                listPersonsRanked.remove(firstPerson);
                listPersonsRanked.remove(secondPerson);
                listPersonsRanked.remove(thridPerson);

                // Lager liste med hvilken plass på leaderboardet i forkant av Person-objektet
                listPersonsRankedString = listPersonsRanked.stream()
                        .map(p -> listPersonsRanked.indexOf(p) + 4 + ". " + p.getFirstName() + " " + p.getLastName()
                                + ": " + (int) p.getAmountSpent() + " kr")
                        .collect(Collectors.toList());

                // Lager liste med hvilken plass på loser-listen i forkant av Person-objektet
                listPersonsLosersString = listPersonsLosers.stream()
                        .map(p -> listPersonsLosers.indexOf(p) + 1 + ". " + p.getFirstName() + " " + p.getLastName()
                                + ": " + (int) p.getAmountSpent() + " kr")
                        .collect(Collectors.toList());

                listPersonsSettings.getItems().setAll(Person.getObjPersons());
                listLeaderboard.getItems().setAll(listPersonsRankedString);
                listLosers.getItems().setAll(listPersonsLosersString);
            }

        });
    }
}