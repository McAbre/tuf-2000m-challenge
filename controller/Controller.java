/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.Model;

/**
 *
 * @author joel_
 */
public class Controller {
    
    //MVC
    private Model model;
    
    @FXML
    private Label timeLabel;
    @FXML
    private TextField signalField, temperatureField, negativeEnergyField;
    @FXML
    private Button refreshButton;
    @FXML
    private Canvas graphCanvas;
    
    
    
    @FXML
    public void refreshPressed(){
        //Refresh all values
        refresh();
    }
    
    private int refresh(){
        //If all went well return 1 else return -9999
        try{
            String data = model.urlReader();//get data
            //get signal value
            //get temperature value
            //get negative energy value
            
            //update values
            //update(int signal, double temperature, double negEnergyAcc);
            return 1;
        }catch(Exception e){
            return -9999;
        }
    }
    
    //Updates all the values on the screen
    protected void update(){
        //set signal quality
        //set temperature
        //set negative energy
        //set last updated
        
        //OPTIONAL: ADD VALUES TO GRAPH?
    }
}
