package org.example.GUI;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import org.example.BusinessLogic.SimulationManager;

import java.util.ArrayList;
import java.util.FormatterClosedException;

public class SimulationFrame  {

    @FXML
    private Text stateText;

    @FXML
    private ComboBox<String> comboBox;

    @FXML
    private TextField maximumArrivalTime;

    @FXML
    private TextField maximumServiceTime;

    @FXML
    private TextField minimumArrivalTime;

    @FXML
    private TextField minimumServiceTime;

    @FXML
    private TextField numberOfClients;

    @FXML
    private TextField simulationTime;

    @FXML
    private Text numberQueuesText;

    @FXML
    private Text errorText;

    @FXML
    private TextField numbersOfQueues;

    @FXML
    private ProgressBar progressBar;

    @FXML
    private Text waittingPeriodText;

    @FXML
    private Button startButton;



    @FXML
    void startButtonAction(MouseEvent event) {
        if(!validateDataInput()){
            errorText.setText("Invalid data");
        }else{
            errorText.setText("");
            int n = Integer.parseInt(numbersOfQueues.getText());
            ObservableList<String> observableList = FXCollections.observableArrayList();
            for (int i = 1; i <= n; i++) {
                String option ="Queue " + i;
                observableList.add(option);

            }
            comboBox.setItems(observableList);
            SimulationManager simulationManager = new SimulationManager(this);
            Thread t = new Thread(simulationManager);
            t.start();
        }

    }
    public boolean validateDataInput(){
        try{
            Integer.parseInt(numbersOfQueues.getText());
            Integer.parseInt(simulationTime.getText());
            Integer.parseInt(numberOfClients.getText());
            Integer.parseInt(minimumArrivalTime.getText());
            Integer.parseInt(maximumArrivalTime.getText());
            Integer.parseInt(minimumServiceTime.getText());
            Integer.parseInt(maximumServiceTime.getText());
        }catch (NumberFormatException e){
            return false;
        }

        return true;
    }

    public ComboBox<String> getComboBox() {
        return comboBox;
    }

    public TextField getMaximumArrivalTime() {
        return maximumArrivalTime;
    }

    public TextField getMaximumServiceTime() {
        return maximumServiceTime;
    }

    public TextField getMinimumArrivalTime() {
        return minimumArrivalTime;
    }

    public TextField getMinimumServiceTime() {
        return minimumServiceTime;
    }

    public TextField getNumberOfClients() {
        return numberOfClients;
    }

    public Text getNumberQueuesText() {
        return numberQueuesText;
    }


    public TextField getNumbersOfQueues() {
        return numbersOfQueues;
    }

    public ProgressBar getProgressBar() {
        return progressBar;
    }

    public Text getStateText() {
        return stateText;
    }

    public Text getWaittingPeriodText() {
        return waittingPeriodText;
    }

    public TextField getSimulationTime() {
        return simulationTime;
    }
}
