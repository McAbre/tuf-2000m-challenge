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
        int width = 315;
        String spaces = "";
        
        Font font = new Font("Arial", Font.PLAIN, 14);
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
    public String getRegister(String data, int register){
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
    
    public double round(int decimalPoints, double value){
        return BigDecimal.valueOf(value).setScale(decimalPoints, RoundingMode.CEILING).doubleValue();
    }
    
    public double getLong(String data, int reg0, int reg){
        String reg1 = getRegister(data, reg0);
        String reg2 = getRegister(data, reg); 
        return longParse(reg1, reg2);
    }
    public double getReal4(String data, int reg0, int reg){
        String reg1 = getRegister(data, reg0);
        String reg2 = getRegister(data, reg);
        return magnitudeDecider(reg1, reg2);
    }
    
    
    public String getLanguage(String data){
        int langNum = Integer.parseInt(getRegister(data, 96));
        String language = "";
        switch(langNum){
            case 0: language  = "English"; break;
            case 1: language  = "Chinese"; break;
            default: language = "Error";
        }
        return language;
    }
    
    public int getWorkingStep(String data){
        String workingStep = getRegister(data, 92);
        String temp = Long.toBinaryString(Integer.parseInt(workingStep));
        while(temp.length() < 16){
            temp = "0" + temp;
        }
        //We want the first half.
        String workingBinary = temp.substring(0, temp.length()/2);
        //2 stands for base 2
        int workingSt = Integer.parseInt(workingBinary, 2);
        return workingSt;
    }
    
    public int getSignal(String data) {
        String signal = getRegister(data, 92);
        return signalQuality(signal);
    }
    
    private int signalQuality(String value) {
        String temp = Long.toBinaryString(Integer.parseInt(value));
        while(temp.length() < 16){
            temp = "0" + temp;
        }
        //We want the latter half.
        String signalBinary = temp.substring(temp.length()/2);
        //2 stands for base 2
        int signalQuality = Integer.parseInt(signalBinary, 2);
        return signalQuality;
    }
    
    //Is this really right? Idk how else to get the correct value though.
    private double longParse(String strReg1, String strReg2){
        int reg1 = Integer.parseInt(strReg1);
        int reg2 = Integer.parseInt(strReg2);
        return (reg1-reg2-1);
    }
    
    private double real4(String reg1, String reg2){
        //-1^sign * 2^exponent * mantissa
        int sign;
        int exponent;
        double mantissa;
        
        int highWord = Integer.parseInt(reg1);
        int lowWord = Integer.parseInt(reg1);
        
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
        
        //                      -1^sign   * 2^exponent            * mantissa
        double real4 = Math.pow(-1, sign) * Math.pow(2, exponent) * mantissa;
        return real4;
    }
    
    //Calculates distance from 0 (think x*10 or x*10^-1) where the ^n is the magnitude
    private int magnitude (double number){
        int mag = 0;
        
        //Number is between 0 and 1 (this most likely means it looks like this 0.0000000000001 ish)
        if(number < 1 && number > 0){
            while(number < 1){
                number = number * 10;
                mag++;
            }
        //Number is greater than 1
        }else if(number > 1){
            while(number > 1){
                number = number / 10;
                mag++;
            }
        //Number is less than or equal to 0
        }else{
            number = number*(-1);//Flips number to be positive
            //Number is less than 0 and greater than -1(before the flip to positive)
            if(number < 1 && number > 0){
                while(number < 1){
                    number = number * 10;
                    mag++;
                }
            }
            //Number is less than or equal to -1 (before the flip to positive)
            else{
                while(number > 1){
                    number = number / 10;
                    mag++;
                }
            }
            
        }
        
        return mag;
    }
    private double magnitudeDecider(String reg1, String reg2){
        double result;
        //Reality check... Maybe the temperature isn't 0.0000000001C or a few million C
        double val1 = real4(reg1, reg2);
        double val2 = real4(reg2, reg1);
        int mag1 = magnitude(val1);
        int mag2 = magnitude(val2);
        System.out.println("magnitude("+reg1+","+reg2+"): " + "val1="+val1+" val2="+val2 + " mag= " + mag1 + " | " + mag2);
        if(mag1 < mag2 && mag1 != 0 || mag1 == 0 && mag2 > 12){
            result = val1;
        }else{
            result = val2;
        }
        return result;
    }
    
}
