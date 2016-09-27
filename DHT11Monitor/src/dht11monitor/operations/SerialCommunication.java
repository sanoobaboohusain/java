/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dht11monitor.operations;

import dht11monitor.ui.MainFrame;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import gnu.io.*;
import java.nio.channels.Channels;
import java.util.Enumeration;
import javax.swing.JTextArea;
import sun.nio.ch.IOUtil;

/**
 *
 * @author sanoob
 */
public class SerialCommunication implements SerialPortEventListener {

    public SerialCommunication() {
    }

    public SerialCommunication(JTextArea text) {
        this.textArea = text;

    }

    public SerialCommunication(MainFrame main) {
        this.main = main;
    }
    JTextArea textArea = new JTextArea();
    SerialPort serialPort;
    MainFrame main;
    HandleData dataHandler = new HandleData();
    private static final String PORT_NAMES[] = {
        "/dev/tty.usbserial-A9007UX1", // Mac OS X
        "/dev/ttyACM0", // Raspberry Pi
        "/dev/ttyACM0", // Linux
        "/dev/ttyACM1",
        "COM4", "COM3", "COM5", // Windows
    };

    private BufferedReader input;
    /**
     * The output stream to the port
     */
    private OutputStream output;
    /**
     * Milliseconds to block while waiting for port open
     */
    private static final int TIME_OUT = 2000;
    /**
     * Default bits per second for COM port.
     */
    private static final int DATA_RATE = 9600;

    public void initialize() {
        // the next line is for Raspberry Pi and 
        // gets us into the while loop and was suggested here was suggested http://www.raspberrypi.org/phpBB3/viewtopic.php?f=81&t=32186
        // System.setProperty("gnu.io.rxtx.SerialPorts", "/dev/ttyACM0");

        CommPortIdentifier portId = null;
        Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();

        //First, Find an instance of serial port as set in PORT_NAMES.
        while (portEnum.hasMoreElements()) {
            CommPortIdentifier currPortId = (CommPortIdentifier) portEnum.nextElement();
            for (String portName : PORT_NAMES) {
                if (currPortId.getName().equals(portName)) {
                    portId = currPortId;
                    break;
                }
            }
        }
        if (portId == null) {
            System.out.println("Could not find COM port.");
            return;
        }

        try {
            // open serial port, and use class name for the appName.
            serialPort = (SerialPort) portId.open(this.getClass().getName(),
                    TIME_OUT);

            // set port parameters
            serialPort.setSerialPortParams(DATA_RATE,
                    SerialPort.DATABITS_8,
                    SerialPort.STOPBITS_1,
                    SerialPort.PARITY_NONE);

            // open the streams
            input = new BufferedReader(new InputStreamReader(serialPort.getInputStream()));
            output = serialPort.getOutputStream();

            // add event listeners
            serialPort.addEventListener(this);
            serialPort.notifyOnDataAvailable(true);
        } catch (Exception e) {
            System.err.println(e.toString());
        }
    }

    /**
     * This should be called when you stop using the port. This will prevent
     * port locking on platforms like Linux.
     */
    public synchronized void close() {
        if (serialPort != null) {
            serialPort.removeEventListener();
            serialPort.close();
        }
    }

    /**
     * Handle an event on the serial port. Read the data and print it.
     */
    public synchronized void serialEvent(SerialPortEvent oEvent) {
        if (oEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
            try {
                //System.out.println(serialPort.getInputStream().);
                //String inputLine=input.readLine();
                //System.out.println(inputLine);
                java.util.Scanner scanner = new java.util.Scanner(serialPort.getInputStream()).useDelimiter("\\A");
                if (scanner.hasNext()) {
                    String data = scanner.next();
                    main.updateText(data);
                    String line = dataHandler.parseData(data);
                    if (MainFrame.saveFileFlag) {
                        try {
                            if (MainFrame.saveFile != null) {
                                new WriteData().write(MainFrame.saveFile, line);
                            } else {
                                new WriteData().write(line);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    //main.updateHumidText(val[0]);
                    //main.updateTempText(val[1]);
                }
            } catch (Exception e) {
                System.err.println(e.toString());
            }
        }
        // Ignore all the other eventTypes, but you should consider the other ones.
    }

    public void sendData(String message) {
        try {
            output.write(message.getBytes());
            output.flush();
            //output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
//        public static void main(String[] args) throws Exception {
//		TransferData main = new TransferData();
//		main.initialize();
//		Thread t=new Thread() {
//			public void run() {
//				//the following line will keep this app alive for 1000 seconds,
//				//waiting for events to occur and responding to them (printing incoming messages to console).
//				try {Thread.sleep(1000000);} catch (InterruptedException ie) {}
//			}
//		};
//		t.start();
//                
//                main.sendData("U");
//                Thread.sleep(5000);
//                main.sendData("L");
//                Thread.sleep(5000);
//                main.sendData("R");
//                Thread.sleep(5000);
//                main.sendData("D");
//                Thread.sleep(5000);
//                main.sendData("S");
//		System.out.println("Started");
//	}
}
