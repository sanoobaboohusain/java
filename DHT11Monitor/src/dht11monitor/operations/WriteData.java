/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dht11monitor.operations;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

/**
 *
 * @author Test
 */
public class WriteData {
    
    
    public void write(String s) throws Exception{
        File f = new File("E:\\output.txt");
        if(!f.exists()){
            f.createNewFile();
        }else{
            
            BufferedWriter output = new BufferedWriter(new FileWriter(f, true));
            output.append(s);
            output.append(System.lineSeparator());
            output.close();
        }
        
    }
    
    public void write (File file, String data) throws Exception{
     if(!file.exists()){
            file.createNewFile();
        }else{
            
            BufferedWriter output = new BufferedWriter(new FileWriter(file, true));
            output.append(data);
            output.append(System.lineSeparator());
            output.close();
        }   
    }
    
}
