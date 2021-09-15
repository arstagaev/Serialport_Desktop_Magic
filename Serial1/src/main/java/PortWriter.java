//import java.io.*;
//import javax.comm.*;
//import java.util.*;
//
//public class PortWriter
//{
//    static Enumeration ports;
//    static CommPortIdentifier pID;
//    static OutputStream outStream;
//    SerialPort serPort;
//    static String messageToSend = "message!\n";
//
//    public PortWriter() throws Exception{
//        serPort = (SerialPort)pID.open("PortWriter",2000);
//        outStream = serPort.getOutputStream();
//        serPort.setSerialPortParams(115200, SerialPort.DATABITS_8,
//                SerialPort.STOPBITS_1,
//                SerialPort.PARITY_NONE);
//    }
//
//    public static void main(String[] args) throws Exception{
//        ports = CommPortIdentifier.getPortIdentifiers();
//
//        while(ports.hasMoreElements())
//        {
//            pID = (CommPortIdentifier)ports.nextElement();
//            System.out.println("Port " + pID.getName());
//
//            if (pID.getPortType() == CommPortIdentifier.PORT_SERIAL)
//            {
//                if (pID.getName().equals("COM3"))
//                {
//                    PortWriter pWriter = new PortWriter();
//                    System.out.println("COM1 found");
//                }
//            }
//        }
//        //outStream.write(messageToSend.getBytes());
//    }
//}
