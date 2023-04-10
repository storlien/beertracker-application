package com.carlst.javafx;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Collectors;

import com.carlst.Filehandler;
import com.carlst.Person;
import com.carlst.PurchaseMapper;
import com.carlst.PurchaseReader;
import com.carlst.TokenGetter;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

public class AppController {

    private TokenGetter tokenGetter;
    private Timer timer;
    private TimerTask task;
    
    @FXML private Pane container1, container2;
    @FXML private TextField firstName, lastName, cardNewFirst6, cardNewLast4, cardAddFirst6, cardAddLast4, cardRemoveFirst6, cardRemoveLast4, dateUpdate;
    @FXML private Label labelStatus, firstFullName, secondFirstName, secondLastName, thirdFirstName, thirdLastName, firstSum, secondSum, thirdSum;
    @FXML private ListView<Person> listPersonsSettings;
    @FXML private ListView<String> listLeaderboard, listLosers;

    @FXML
    public void initialize() {

        container1.setVisible(false);
        container2.setVisible(true);

        // Lager instans av TokenGetter som sørger for at access token er gyldig til enhver tid
        tokenGetter = new TokenGetter();

        // Leser fil og dermed lager liste med Person-objekter på Person.objPersons
        Filehandler.readFile();
        Filehandler.readHashFile();

        timer = new Timer();
        task = new TimerTask() {
            @Override
            public void run() {
                PurchaseMapper.mapPurchases(PurchaseReader.getNewerPurchases(PurchaseReader.getJsonStringLastHash(PurchaseReader.getLastPurchaseHash())));
                updateGUI();
            }
            
        };

        timer.schedule(task, 5 * 1000, 5 * 1000); // Delay: 10 sek, Period: 5 sek

        updateGUI();
    }

    @FXML
    public void onAddPerson() {
        String firstName = this.firstName.getText();
        String lastName = this.lastName.getText();
        String cardNewFirst6 = this.cardNewFirst6.getText();
        String cardNewLast4 = this.cardNewLast4.getText();

        Person.getObjPersons().add(new Person(firstName, lastName, cardNewFirst6, cardNewLast4));

        mapNewPurchases("2022-08-01");
    }

    @FXML
    public void onAddCard() {
        Person person = listPersonsSettings.getSelectionModel().getSelectedItem();
        
        if (person != null) {
            person.addCard(this.cardAddFirst6.getText(), this.cardAddLast4.getText());
        }

        else {
            updateStatus("Velg en person");
        }
    }

    @FXML
    public void onRemoveCard() {
        Person person = listPersonsSettings.getSelectionModel().getSelectedItem();
        
        if (person != null) {
            person.removeCard(cardRemoveFirst6.getText(), cardRemoveLast4.getText());
        }

        else {
            updateStatus("Velg en person");
        }
    }

    @FXML
    public void onUpdatePersonsAug22() {
        mapNewPurchases("2022-08-01");
    }

    @FXML
    public void onUpdatePersonsDate() {
        String date = dateUpdate.getText();
        mapNewPurchases(date);
    }

    @FXML
    public void onRemovePerson() {
        Person.getObjPersons().remove(listPersonsSettings.getSelectionModel().getSelectedIndex());
        updateStatus("Person fjernet");
        updateGUI();
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

    // Tilbakestiller summene og mapper kjøpshistorikk på nytt
    public void mapNewPurchases(String date) {

        updateStatus("Oppdaterer kjøpshistorikk...");

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
            updateStatus("Ferdig!");
        });

        new Thread(task).start();
    }

    private void updateStatus(String status) {
        labelStatus.setText(status);
    }

    private void updateGUI() {

        Platform.runLater(new Runnable() {

            @Override
            public void run() {
                List<Person> listPersonsRanked = new ArrayList<>(Person.getObjPersons().stream().sorted().collect(Collectors.toList()));
                List<Person> listPersonsLosers = new ArrayList<>(Person.getObjPersons().stream().sorted(Comparator.reverseOrder()).collect(Collectors.toList()));

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
                                                            .map(p -> listPersonsRanked.indexOf(p) + 4 + ". " + p.getFirstName() + " " + p.getLastName() + ": " + p.getAmountSpent())
                                                            .collect(Collectors.toList());

                // Lager liste med hvilken plass på loser-listen i forkant av Person-objektet
                listPersonsLosersString = listPersonsLosers.stream()
                                                            .map(p -> listPersonsLosers.indexOf(p) + 1 + ". " + p.getFirstName() + " " + p.getLastName() + ": " + p.getAmountSpent())
                                                            .collect(Collectors.toList());

                listPersonsSettings.getItems().setAll(Person.getObjPersons());
                listLeaderboard.getItems().setAll(listPersonsRankedString);
                listLosers.getItems().setAll(listPersonsLosersString);
            }

        });
    }

}