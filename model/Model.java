/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.geom.Rectangle2D;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.util.Scanner;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

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
        items = FXCollections.observableArrayList();
    }
    //Singleton
    
    private ObservableList<String> items;
    
    //Most of all functionality goes here
    
    //Returns all the items to be displayed in the listview
    public ObservableList getItems(){
        return items;
    }
    
    public void addItem(String item){
        items.add(item);
    }
    
    public void emptyList(){
        items.clear();
    }
    
    public String fixLength(String type, String value, String unit){
        int width = 320;
        String spaces = "";
        
        Font font = Font.getFont("Default");
        FontMetrics metrics = new FontMetrics(font) {  
        };
        Rectangle2D bounds = metrics.getStringBounds(type + spaces + value + " " + unit, null);  
        int widthInPixels = (int) bounds.getWidth(); 
        
        while(widthInPixels < width){
            spaces += " ";
            bounds = metrics.getStringBounds(type + spaces + value + " " + unit, null);  
            widthInPixels = (int) bounds.getWidth(); 
        }
        
        return (type + spaces + value + " " + unit);
    }
    
    
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
        temp = temp.split(",")[0];
        return temp;
    }
    
    private double round(int decimalPoints, double value){
        return BigDecimal.valueOf(value).setScale(decimalPoints, RoundingMode.CEILING).doubleValue();
    }
    
    public int getSignal(String data) {
        String signal = getRegister(data, 92);
        return signalQuality(signal);
    }

    public double getTemperature(String data) {
        String reg33 = getRegister(data, 33);
        String reg34 = getRegister(data, 34);
        return round(2, temperature(reg33, reg34));
    }

    public double getNegativeEnergy(String data) {
        String reg21 = getRegister(data, 21);
        String reg22 = getRegister(data, 22);
        return negativeEnergy(reg21, reg22);
    }
    
    private int signalQuality(String value) {
        String temp = Long.toBinaryString(Integer.parseInt(value));
        while(temp.length() < 16){
            temp = "0" + temp;
        }
        //We want the latter half. Because modbus...
        String signalBinary = temp.substring(temp.length()/2);
        //2 stands for base 2
        int signalQuality = Integer.parseInt(signalBinary, 2);
        return signalQuality;
    }
    
    //Is this really right? Idk how else to get the correct value though.
    private double negativeEnergy(String strReg21, String strReg22){
        int reg21 = Integer.parseInt(strReg21);
        int reg22 = Integer.parseInt(strReg22);
        return (reg21-reg22-1);
    }
    
    private double temperature(String reg33, String reg34){
        //-1^sign * 2^exponent * mantissa
        int sign;
        int exponent;
        double mantissa;
        
        int highWord = Integer.parseInt(reg33);
        int lowWord = Integer.parseInt(reg34);
        
        String[] highNlow = {Integer.toBinaryString(highWord), Integer.toBinaryString(lowWord)};
        
        int j=0;
        while(highNlow[0].length() < 16 || highNlow[1].length() < 16){
            if(highNlow[0].length() < 16) highNlow[0] = "0"+highNlow[0];
            if(highNlow[1].length() < 16) highNlow[1] = "0"+highNlow[1];
            j++;
        }
        
        String binaryFloat = highNlow[0] + highNlow[1];
        /*https://www.h-schmidt.net/FloatConverter/IEEE754.html
        The sign is stored in bit 32[0].
        The exponent can be computed from bits 24-31[1-8] by subtracting 127.
        The mantissa (also known as significand or fraction) is stored in
        bits 1-23. An invisible leading bit (i.e. it is not actually stored)
        with value 1.0 is placed in front, then bit 9 has a value of 1/2,
        bit 10 has value 1/4 etc. As a result, the mantissa has a value
        between 1.0 and 2. If the exponent reaches -127 (binary 00000000),
        the leading 1 is no longer used to enable gradual underflow.
        */
        sign = Integer.parseInt(binaryFloat.charAt(0)+"");
        String strExp = binaryFloat.substring(1, 9);
        exponent = Integer.parseInt(strExp, 2)-127;
        
        String strMant = binaryFloat.substring(9);
        mantissa = Integer.parseInt(strMant, 2);
        if((exponent + 127) > 0){
            mantissa = (mantissa/8388608)+1; //Don't ask don't tell. This is straight from the internet.
        }else{                               //That value is 2^23, mantissa stored in 1-23. Coincidence? I think not.
            mantissa = mantissa/4194304;
        }
        
        
        double a = Math.pow(-1, sign);
        double b = Math.pow(2, exponent);
        double c = mantissa;
        
        return a*b*c;
    }
    
    
}
