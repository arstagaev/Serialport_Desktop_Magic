////import com.fazecast.jSerialComm.SerialPort;
////import com.fazecast.jSerialComm.SerialPortDataListener;
////import com.fazecast.jSerialComm.SerialPortEvent;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.util.Enumeration;
//import java.util.Scanner;
//import java.util.TooManyListenersException;
//import javax.comm.*;
//import javax.swing.JFrame;
//import javax.swing.JSlider;
//
//
//public class SimpleRead implements Runnable, SerialPortEventListener {
//    static CommPortIdentifier portId;
//    static Enumeration portList;
//
//    InputStream inputStream;
//    SerialPort serialPort;
//    Thread readThread;
//
//    public static void main(String[] args) {
//        portList = CommPortIdentifier.getPortIdentifiers();
//
//        while (portList.hasMoreElements()) {
//            portId = (CommPortIdentifier) portList.nextElement();
//            if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
//                if (portId.getName().equals("COM3")) {
//                    //                if (portId.getName().equals("/dev/term/a")) {
//                    SimpleRead reader = new SimpleRead();
//                }
//            }
//        }
//    }
//
//    public SimpleRead() {
//        try {
//            serialPort = (SerialPort) portId.open("SimpleReadApp", 2000);
//        } catch (PortInUseException e) {System.out.println(e);}
//        try {
//            inputStream = serialPort.getInputStream();
//        } catch (IOException e) {System.out.println(e);}
//        try {
//            serialPort.addEventListener(this);
//        } catch (TooManyListenersException e) {System.out.println(e);}
//        serialPort.notifyOnDataAvailable(true);
//        try {
//            serialPort.setSerialPortParams(115200,
//                    SerialPort.DATABITS_8,
//                    SerialPort.STOPBITS_1,
//                    SerialPort.PARITY_NONE);
//        } catch (UnsupportedCommOperationException e) {System.out.println(e);}
//        readThread = new Thread(this);
//        readThread.start();
//    }
//
//    public void run() {
//        try {
//            Thread.sleep(20000);
//        } catch (InterruptedException e) {System.out.println(e);}
//    }
//
//    public void serialEvent(SerialPortEvent event) {
//        switch(event.getEventType()) {
//            case SerialPortEvent.BI:
//            case SerialPortEvent.OE:
//            case SerialPortEvent.FE:
//            case SerialPortEvent.PE:
//            case SerialPortEvent.CD:
//            case SerialPortEvent.CTS:
//            case SerialPortEvent.DSR:
//            case SerialPortEvent.RI:
//            case SerialPortEvent.OUTPUT_BUFFER_EMPTY:
//                break;
//            case SerialPortEvent.DATA_AVAILABLE:
//                byte[] readBuffer = new byte[20];
//
//                try {
//                    while (inputStream.available() > 0) {
//                        int numBytes = inputStream.read(readBuffer);
//                    }
//                    System.out.print(new String(readBuffer));
//                } catch (IOException e) {System.out.println(e);}
//                break;
//        }
//    }
//
////    @Override
////    public void serialEvent(javax.comm.SerialPortEvent serialPortEvent) {
////
////    }
//}
//
////public class Main {
////
////    public static void main(String[] args) {
////        // create a window with a slider
////        JFrame window = new JFrame();
////        JSlider slider = new JSlider();
////        slider.setMaximum(1023);
////        window.add(slider);
////        window.pack();
////        window.setVisible(true);
////
////        System.out.println("HEELO START!");
////
////        SerialPort comPort = SerialPort.getCommPort("COM13");
////        comPort.setBaudRate(115200);
////        //comPort.setComPortParameters(115200,8,1,0);
////        //comPort.setComPortTimeouts(SerialPort.TIMEOUT_NONBLOCKING,0,0);
////        comPort.openPort();
////
////        byte[] bytes =new byte[] {0x74, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 };
////        comPort.writeBytes(bytes,bytes.length+1);
////
////        comPort.addDataListener(new SerialPortDataListener() {
////            @Override
////            public int getListeningEvents() {
////                return SerialPort.LISTENING_EVENT_DATA_AVAILABLE;
////            }
////
////            @Override
////            public void serialEvent(SerialPortEvent event) {
////                if (event.getEventType() != SerialPort.LISTENING_EVENT_DATA_AVAILABLE) {
////                    return;
////                }
////                byte[] newData = new byte[comPort.bytesAvailable()];
////                int numRead = comPort.readBytes(newData, newData.length);
////                System.out.println("Read " + numRead + " bytes.");
////            }
////        });
////
//////        while(true){
//////            byte[] buffer = new byte[15];
//////            int bytesAvailable = comPort.bytesAvailable();
//////
//////            int bytesRead = comPort.readBytes(buffer, Math.min(buffer.length, bytesAvailable));
//////            String response = new String(buffer, 0, bytesRead);
//////            System.out.println("ans " +bytesRead +" =num "+response+" || "+bytesAvailable);
//////        }
////
////
//////        try {
//////            while (true)
//////            {
//////                while (comPort.bytesAvailable() == 0)
//////                    Thread.sleep(20);
//////
//////                byte[] readBuffer = new byte[comPort.bytesAvailable()];
//////                int numRead = comPort.readBytes(readBuffer, readBuffer.length);
//////                System.out.println("Read " + numRead + " bytes.");
//////            }
//////        } catch (Exception e) { e.printStackTrace(); }
////        //comPort.closePort();
////
////    }
////
////}