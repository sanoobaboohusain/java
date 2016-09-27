/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dht11monitor.operations;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Test
 */
public class HandleData {
    
   
    
            
    
    public String parseData(String data){
       String hum = "";
       String temp = "";
       String returnValue = "";
        if((data.startsWith("{"))){
            String dataPart [] = data.split(",");
            for(String s: dataPart){
                if(s.contains("humidity")){
                    String d[] = s.split(":");
                    //System.out.println("hu: "+ d[1]);
                    hum = d[1];
                }else if (s.contains("temp")){
                    String d[] = s.split(":");
                   temp = d[1];
                }
            }
           returnValue = temp + ", "+ hum+", "+getTimeString();
        }
        return returnValue;
    }
    
    public String getTimeString(){
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy h:mm:ss a");
        String formattedDate = sdf.format(date);
        System.out.println(formattedDate); // 12/01/2011 4:48:16 PM
        return formattedDate;
    }
    
}
