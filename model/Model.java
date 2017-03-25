/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.IOException;
import java.net.URL;
import java.util.Scanner;

/**
 *
 * @author joel_
 */
public class Model {
    
    //Singleton
    private static Model instance;
    public static synchronized Model getInstance() {
        if(instance == null){
            instance = new Model();
        }
        return instance;
    }
    private Model(){
        instance = this;
    }
    //Singleton
    
    //Most of all functionality goes here
    
    
    
    
    //Reads data from URL and converts to CSV string
    public String urlReader(){
        try {
            URL url = new URL("http://tuftuf.gambitlabs.fi/feed.txt");
            Scanner s = new Scanner(url.openStream());
            String result = "";
            while(s.hasNext()){
                result += s.nextLine() + ",";
            }
            return result;
        }catch(Exception ex) {
            System.out.println("EXCEPTION MFUCKER");
            ex.printStackTrace();
        }
        return "urlReader failed";
    }
    
    //Returns specific register from the data string as a string
    private String getRegister(String data, int register){
        /**
         * Raw data as csv as such 1:26954, 2:31231, 3:1231231
         * Step 1: Split at "register:"
         * 26954, 2:31231, 3:1231231
         * Step 2: Split at commas and take the first value
         * 26954
        */
        String temp = data.split((register + ":"))[1]; 
        temp = temp.split(",")[0]; //
        return temp;
    }

    public int getSignal(String data) {
        String signal = getRegister(data, 92);
            
        return signalQuality(signal);
    }

    public double getTemperature(String data) {
        String reg33 = getRegister(data, 33);
        String reg34 = getRegister(data, 34);
        return temperature(reg33, reg34);
    }

    public double getNegativeEnergy(String data) {
        String reg21 = getRegister(data, 21);
        String reg22 = getRegister(data, 22);
        
        return negativeEnergy(reg21, reg22);
    }
    
    private int signalQuality(String value) {
        return 7357;
    }
    
    private double negativeEnergy(String reg21, String reg22){
        return 7357.0;
    }
    
    private double temperature(String reg33, String reg34){
        return 7357.0;
    }
}
