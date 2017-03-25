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
    //Most of all functionality goes here
    
    
    //Constructor
    public Model(){
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
        }catch(IOException ex) {
            ex.printStackTrace();
        }
        return "IOException";
    }
    
    //Returns specific register from the data string as a string
    private String getRegister(String data, int register){
        String temp = data.split((register + ":"))[1];
        temp = temp.split(",")[0];
        return temp;
    }
}
