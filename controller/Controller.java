/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
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
        refresh();
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
    //@FXML
    //private Canvas graphCanvas;
    
    @FXML
    public void refreshPressed(){
        //Refresh all values
        System.out.println(refresh());
    }
    
    private int refresh(){
        //If all went well return 1 else return -9999
        try{
            //get data from the url
            String data = model.urlReader();
            
            //get timestamp
            String timestamp = data.split(",")[0];
            
            //get signal value
            int signal = model.getSignal(data);
            
            //get temperature value
            double temperature = model.getTemperature(data);
            
            //get negative energy value
            double negEnergyAcc = model.getNegativeEnergy(data);
            
            //update values
            update(signal+"", temperature+"", negEnergyAcc+"", timestamp);
            return 1;
        }catch(Exception e){
            return -9999;
        }
    }
    
    //Updates all the values on the screen
    protected void update(String signal, String temperature, String negEnergyAcc, String timestamp){
        //set signal quality
        signalField.setText(signal);
        //set temperature
        temperatureField.setText(temperature);
        //set negative energy
        negativeEnergyField.setText(negEnergyAcc);
        //set last updated
        timeLabel.setText(timestamp);
        
        //OPTIONAL: ADD VALUES TO GRAPH?
    }

}
