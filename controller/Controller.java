/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
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
    private TextField signalField, temperatureField, negativeEnergyField;
    @FXML
    private ListView<String> listView;
    
    //private Canvas graphCanvas;
    
    @FXML
    public void refreshPressed(){
        //Refresh all values
        System.out.println(refresh());
    }
    
    private int refresh(){
        //If all went well return 1 else return -9999
        try{
            model.emptyList();
            //get data from the url
            String data = model.urlReader();
            
            //get timestamp
            String timestamp = data.split(",")[0];
            
            //get positive accumulator
            //double posAcc = model.getPositiveAccumulator(data);
            //model.addItem(model.fixLength("Positive accumulator", posAcc, "unit"))
            
            //get signal value
            int signal = model.getSignal(data);
            model.addItem(model.fixLength("Signal quality", signal+"", "unit"));
            
            //get temperature value
            double temperature = model.getTemperature(data);
            model.addItem(model.fixLength("Temperature", temperature+"", "celcius"));
            
            //get negative energy value
            double negEnergyAcc = model.getNegativeEnergy(data);
            model.addItem(model.fixLength("Negative energy accumulator", negEnergyAcc+"", "unit"));
            
            //update values
            System.out.println("timeLabel: " + timeLabel);
            timeLabel.setText(timestamp);
            
            System.out.println("Listview items: " + listView.getItems().size());
            listView.setItems(model.getItems());
            return 1;
        }catch(Exception e){
            e.printStackTrace();
            return -9999;
        }
    }
    
    @FXML
    void listviewDraggingStart(){
        System.out.println("controller.Controller.listviewDraggingStart()");
    }
    
    @FXML
    void listviewDraggingStop(){
        System.out.println("controller.Controller.listviewDraggingStop()");
    }
    
    
}
