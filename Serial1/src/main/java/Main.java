//import com.fazecast.jSerialComm.SerialPort;
//import com.fazecast.jSerialComm.SerialPortDataListener;
//import com.fazecast.jSerialComm.SerialPortEvent;
//
//import java.awt.*;
//import java.io.IOException;
//import java.io.PrintWriter;
//import java.util.Arrays;
//import java.util.Scanner;
//import javax.swing.*;
//
////import com.fazecast.jSerialComm.*;
//
//public class Main  {
//
//    public static void main(String[] args) {
//
//        JFrame window = new JFrame();
//        window.setSize(1000,600);
////        JSlider slider = new JSlider();
////        slider.setMaximum(1023);
////        window.add(slider);
////        window.pack();
//        JLabel l1,l2;
//        l1=new JLabel("First Label.");
//        l1.setBounds(0,0, 100,30);
//        l2=new JLabel("Second Label.");
//        l2.setBounds(50,100, 100,30);
//        window.add(l1); window.add(l2);
//        window.setSize(300,300);
//        window.setLayout(null);
//
//        JTextField label1 = new JTextField("Test2");
//        label1.setForeground(Color.RED);
//        label1.setFont(new Font("Serif", Font.PLAIN, 30));
//        label1.setBounds(25,25, 100,30);
//        label1.setText("COM3");
////label1.setBounds(0, 0, 1000, 600);
////        label1.setAlignmentX(0f);
////        label1.setAlignmentY(0f);
//
//        //label1.setLocation(0,0);
//        window.add(label1);
//
//        window.setVisible(true);
//
//        //SerialPort[] ports = SerialPort.getCommPort("COM3");
//        //System.out.println("Select a port:");
//        //int i = 1;
//        //for(SerialPort port : ports)
//        //    System.out.println(i++ +  ": " + port.getSystemPortName());
//        //Scanner s = new Scanner(System.in);
//        //int chosenPort = s.nextInt();
//
//        SerialPort serialPort = SerialPort.getCommPort("COM3");
////        if(serialPort.openPort())
////            System.out.println("Port opened successfully.");
////        else {
////            System.out.println("Unable to open the port.");
////            return;
////        }
//
//        serialPort.setBaudRate(115200);
//        serialPort.setComPortParameters(115200, 8, 1, SerialPort.NO_PARITY);
//        serialPort.setComPortTimeouts(SerialPort.TIMEOUT_SCANNER, 0, 0);
//        serialPort.openPort();
//        serialPort.addDataListener(new SerialPortDataListener() {
//            @Override
//            public int getListeningEvents() {
//                return SerialPort.LISTENING_EVENT_DATA_AVAILABLE;
//            }
//
//            @Override
//            public void serialEvent(SerialPortEvent event) {
//                if (event.getEventType() != SerialPort.LISTENING_EVENT_DATA_AVAILABLE) {
//                    return;
//                }
//                byte[] newData = new byte[serialPort.bytesAvailable()];
//                int numRead = serialPort.readBytes(newData, newData.length);
//
//                if (newData[1] == 0){
//                    l2.setText("~"+newData[0]);
//                }else{
//                    int output = newData[0]+newData[1]*255;
//                    l2.setText(""+output);
//                }
//
//                System.out.println("conv "+ Arrays.toString(newData));
//                //System.out.println("Read " + numRead + " bytes. " + newData[0]);
//                //System.out.println(bytesToHex(newData).toString());
//            }
//        });
//        System.out.println("Lets start!");
//
//        int a= 0;
//        byte[] bb = new byte[]{(byte) 0x74, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 };
//        try {
//            Thread.sleep(2000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        while (a<10){
//            serialPort.writeBytes(bb,1);
////            try {
//                System.out.println("goo "+bb.length);
////                serialPort.getOutputStream().write(bb);
////                //serialPort.getOutputStream().flush();
////            } catch (IOException e) {
////                System.out.println("pizdec "+e.getMessage());
////                e.printStackTrace();
////            }
//            a++;
//        }
//
//
//
//        //serialPort.writeBytes(bb,bb.length);
//        // create a new thread for sending data to the arduino
//
//
//        //while(true){
////
//        //}
//        //Scanner data = new Scanner(serialPort.getInputStream());
//        //int value = 0;
//        //while(data.hasNextLine()){
//        //    try{value = Integer.parseInt(data.nextLine());}catch(Exception e){}
//        //    slider.setValue(value);
//        //}
//        //System.out.println("Done.");
//    }
//    private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();
//    public static String bytesToHex(byte[] bytes) {
//        char[] hexChars = new char[bytes.length * 2];
//        for (int j = 0; j < bytes.length; j++) {
//            int v = bytes[j] & 0xFF;
//            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
//            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
//        }
//        return new String(hexChars);
//    }
////    @Override
////    public int getListeningEvents() {
////        System.out.println("pizdecccc");
////        return SerialPort.LISTENING_EVENT_DATA_AVAILABLE;
////    }
////
////    @Override
////    public void serialEvent(SerialPortEvent event) {
////        byte[] buffer = new byte[event.getSerialPort().bytesAvailable()];
////        event.getSerialPort().readBytes(buffer, buffer.length);
////        System.out.println("size: "+buffer.length);
////        //ReformatBuffer.parseByteArray(buffer);
////    }
//}