/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import model.Model;

/**
 *
 * @author joel_
 */
public class Controller {
    private static Controller instance;
    
    public Controller(){
        instance = this;
        listView = new ListView();
        listView.applyCss();
    }
    
    public static synchronized Controller getInstance(){
        return instance;
    }
    
    //MVC
    private Model model = Model.getInstance();
    
    //FXML variables
    @FXML
    private Label timeLabel;
    @FXML
    private TextField searchBar;
    @FXML
    private ListView<String> listView;
    
    @FXML
    void listviewDraggingStart(){
        System.out.println("controller.Controller.listviewDraggingStart()");
    }
    @FXML
    void listviewDraggingStop(){
        System.out.println("controller.Controller.listviewDraggingStop()");
    }
    @FXML
    public void searchbarKeyPress(KeyEvent e){
        listView.setItems(model.search(searchBar.getText()));
    }
    @FXML
    public void clearButtonPressed(){
        searchBar.setText("");
        listView.setItems(model.getItems());
    }
    @FXML
    public void searchbarAction(){
        System.out.println("controller.Controller.goButtonPressed()");
    }
    @FXML
    public void refreshPressed(){
        //Refresh all values
        refresh();
    }
    
    private int refresh(){
        //If all went well return 1 else return -9999
        try{
            model.emptyList();
            //get data from the url
            String data = model.urlReader();
            
            //get timestamp
            String timestamp = data.split(",")[0];
            
            //get flow rate
            double flowRate = model.getReal4(data, 1, 2);
            model.addItem(model.fixLength("Flow rate", flowRate+"", "m^3/h"));
            
            //get energy flow rate
            double energyFlowRate = model.getReal4(data, 3, 4);
            model.addItem(model.fixLength("Energy flow rate", energyFlowRate+"", "GJ/h"));
            
            //get velocity
            double velocity = model.getReal4(data, 5, 6);
            model.addItem(model.fixLength("Velocity", velocity+"", "m/s"));
            
            //get fluid sound speed
            double fluidSoundSpeed = model.round(2, model.getReal4(data, 7, 8));
            model.addItem(model.fixLength("Fluid sound speed", fluidSoundSpeed+"", "m/s"));
            
            //get positive accumulator
            double posAcc = model.getLong(data, 9, 10);
            double posAccDecFrac = model.getReal4(data, 11, 12);
            model.addItem(model.fixLength("Positive accumulator", model.round(2,(posAcc*posAccDecFrac))+"", ""));
            
            //get negative accumulator
            double negAcc = model.getLong(data, 13, 14);
            double negAccDecFrac = model.getReal4(data, 15, 16);
            model.addItem(model.fixLength("Negative accumulator", model.round(2, (negAcc*negAccDecFrac))+"", ""));
            
            //get positive energy accumulator
            double posEnergyAcc = model.getLong(data, 17, 18);
            double posNrgAccDecFrac = model.getReal4(data, 19, 20);
            model.addItem(model.fixLength("Positive energy accumulator", (model.round(2, posNrgAccDecFrac*posEnergyAcc))+"", "J"));
            
            //get negative energy accumulator
            double negEnergyAcc = model.getLong(data, 21, 22);
            double negNrgAccDecFrac = model.getReal4(data, 23, 24);
            model.addItem(model.fixLength("Negative energy accumulator", (model.round(2, negNrgAccDecFrac*negEnergyAcc))+"", "J"));
            
            //get net accumulator
            double netAcc = model.getLong(data, 25, 26);
            double netDecFrac = model.getReal4(data, 27, 28);
            model.addItem(model.fixLength("Net accumulator", model.round(2, netAcc*netDecFrac)+"", ""));
            
            //get net energy accumulator
            double netNrgAcc = model.getLong(data, 29, 30);
            double netNrgDecFrac = model.getReal4(data, 31, 32);
            model.addItem(model.fixLength("Net energy accumulator", model.round(2, netNrgAcc*netNrgDecFrac)+"", "J"));
            
            //get inlet temperature value
            double inletTemp = model.round(2, model.getReal4(data, 33, 34));
            model.addItem(model.fixLength("Inlet temperature", inletTemp+"", "C"));
            
            //get outlet temperature value
            double outletTemp = model.round(2, model.getReal4(data, 35, 36));
            model.addItem(model.fixLength("Outlet temperature", outletTemp+"", "C"));
            
            //get analog input AI3
            double ai3 = model.round(2, model.getReal4(data, 37, 38));
            model.addItem(model.fixLength("Analog input AI3", ai3+"", ""));
            
            //get analog input AI4
            double ai4 = model.round(2,  model.getReal4(data, 39, 40));
            model.addItem(model.fixLength("Analog input AI4", ai4+"", ""));
            
            //get analog input AI5
            double ai5 = model.round(2, model.getReal4(data, 41, 42));
            model.addItem(model.fixLength("Analog input AI5", ai5+"", ""));
            
            //get analog input AI5
            double ai3current = model.round(2, model.getReal4(data, 43, 44));
            model.addItem(model.fixLength("Current input AI3", ai3current+"", "mA"));
            
            //get analog input AI5
            double ai4current = model.round(2, model.getReal4(data, 45, 46));
            model.addItem(model.fixLength("Current input AI4", ai4current+"", "mA"));
            
            //get analog input AI5
            double ai5current = model.round(2, model.getReal4(data, 47, 48));
            model.addItem(model.fixLength("Current input AI5", ai5current+"", "mA"));
            
            //get error code
            model.addItem(model.fixLength("Error Code", Integer.toBinaryString(Integer.parseInt(model.getRegister(data, 72))), ""));
            
            //get PT100 resistance of inlet
            double inletResistance = model.round(2, model.getReal4(data, 77, 78));
            model.addItem(model.fixLength("PT100 inlet resistance", inletResistance+"", "Ohm"));
            
            //get PT100 resistance of outlet
            double outletResistance = model.round(2, model.getReal4(data, 79, 80));
            model.addItem(model.fixLength("PT100 outlet resistance", outletResistance+"", "Ohm"));
            
            //get total travel time
            double totalTravelTime = model.round(2, model.getReal4(data, 81, 82));
            model.addItem(model.fixLength("Total travel time", totalTravelTime+"", "µs"));
            
            //get delta travel time
            double deltaTravelTime = model.round(2, model.getReal4(data, 83, 84));
            model.addItem(model.fixLength("Delta travel time", deltaTravelTime+"", "ns"));
                    
            //get upstream travel time
            double upstreamTravelTime = model.round(2, model.getReal4(data, 85, 86));
            model.addItem(model.fixLength("Upstream travel time", upstreamTravelTime+"", "µs"));
            
            //get downstream travel time
            double downstreamTravelTime = model.round(2, model.getReal4(data, 87, 88));
            model.addItem(model.fixLength("Downstream travel time", downstreamTravelTime+"", "µs"));
            
            //get output current
            double outputCurrent = model.round(2, model.getReal4(data, 89, 90));
            model.addItem(model.fixLength("Output current", outputCurrent+"", "mA"));
            
            //get working step
            int workingStep = model.getWorkingStep(data);
            model.addItem(model.fixLength("Working step", workingStep+"", "(0-99)"));
            
            //get signal quality
            int signal = model.getSignal(data);
            model.addItem(model.fixLength("Signal quality", signal+"", "(0-99)"));
            
            //get upstream strength
            int upstreamStrength = Integer.parseInt(model.getRegister(data, 93));
            model.addItem(model.fixLength("Upstream strength", upstreamStrength+"", "(0-2047)"));
            
            //get downstream strength
            int downstreamStrength = Integer.parseInt(model.getRegister(data, 94));
            model.addItem(model.fixLength("Downstream strength", downstreamStrength+"", "(0-2047)"));
            
            //get language used in user interface
            String language = model.getLanguage(data);
            model.addItem(model.fixLength("Language", language, ""));
            
            //get the rate of the measured travel time by the calculated travel time
            
            //get Reynolds number. At least ask him out to dinner first! ;D
            double Reynold = model.round(2, model.getReal4(data, 99, 100));
            model.addItem(model.fixLength("Reynolds number", Reynold+"", ""));;
            
            //update values
            timeLabel.setText(timestamp);
            
            listView.setItems(model.getItems());
            return 1;
        }catch(Exception e){
            e.printStackTrace();
            return -9999;
        }
    }
    
}
