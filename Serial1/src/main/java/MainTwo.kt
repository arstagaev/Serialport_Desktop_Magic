import com.fazecast.jSerialComm.SerialPort
import com.fazecast.jSerialComm.SerialPortDataListener
import com.fazecast.jSerialComm.SerialPortEvent
import java.awt.Color
import java.awt.Font
import java.awt.event.ActionListener
import java.util.*
import javax.swing.*
import kotlin.concurrent.fixedRateTimer


//import com.fazecast.jSerialComm.*;
object MainTwo : JFrame() {

    var gm1 = GaugeMaster()
    var gm2 = GaugeMaster2()

    var t = 0
    var asdx = arrayListOf<Int>(1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,33,34,34,32,31,30,29,28,27,26,25,24,23,22,21,20,19,18,17,16,15,14,13,12,11,10,9,8,7,6,5,4,3,2,1,0)
    var asdx2 = arrayListOf<Int>(1,2,3,15,16,17,18,19,26,27,28,29,30,31,32,33,34,6,7,8,9,10,11,12,13,14,15,16)




    @OptIn(ExperimentalUnsignedTypes::class)
    @JvmStatic
    fun main(args: Array<String>) {
        val window = JFrame()
        window.setSize(1000, 1000)
        // выход при закрытии окна

        defaultCloseOperation = EXIT_ON_CLOSE;
        // определение многослойной панели
        val lp: JLayeredPane = layeredPane

        val l1: JLabel = JLabel("First Label.")
        l1.setBounds(0, 0, 100, 30)
        val l2: JLabel = JLabel("Second Label.LOL")
        l2.setBounds(50, 100, 100, 30)
        val b1 : JButton = JButton("Sec")
        b1.setBounds(300, 10, 100, 30)


        val label1 = JTextField("Test2")
        label1.foreground = Color.RED
        label1.font = Font("Serif", Font.PLAIN, 30)
        label1.setBounds(25, 25, 100, 30)
        label1.text = "COM3"



        gm1.setMax(256.0)
        gm2.setMax(4096.0)
        gm1.setBounds(10,10,200,200)
        gm2.setBounds(220,10,200,200)
        // добавление фигур в различные слои
        //lp.add(spmeter, JLayeredPane.DRAG_LAYER)
        //lp.add(label1)
        ////////////////////////////////////////
        val panel = JPanel() //Create JPanel Object
        panel.layout = null
        panel.setBounds(0, 0, 800, 400) //set dimensions for Panel
        panel.background = Color.RED
        //val b = JButton("ButtonInPanel") //create JButton object
        //b.setBounds(0, 0, 80, 40) //set dimensions for button

        b1.addActionListener(ActionListener {
            t = 0
            launchDemo()
            //panel.isVisible = false
        })


        panel.add(gm1)
        panel.add(gm2)
        panel.add(l2)
        ////////////////
        window.setLayout(null);
        window.add(label1)
        window.add(b1)
        //window.add(label2)
        window.add(panel)
//        window.add(l2)
        window.isVisible = true



        //SerialPort[] ports = SerialPort.getCommPort("COM3");
        //System.out.println("Select a port:");
        //int i = 1;
        //for(SerialPort port : ports)
        //    System.out.println(i++ +  ": " + port.getSystemPortName());
        //Scanner s = new Scanner(System.in);
        //int chosenPort = s.nextInt();
        val serialPort = SerialPort.getCommPort("COM3")
        //        if(serialPort.openPort())
//            System.out.println("Port opened successfully.");
//        else {
//            System.out.println("Unable to open the port.");
//            return;
//        }
        serialPort.baudRate = 115200
        serialPort.setComPortParameters(115200, 8, 1, SerialPort.NO_PARITY)
        serialPort.setComPortTimeouts(SerialPort.TIMEOUT_SCANNER, 0, 0)
        serialPort.openPort()
        serialPort.addDataListener(object : SerialPortDataListener {
            override fun getListeningEvents(): Int {
                return SerialPort.LISTENING_EVENT_DATA_AVAILABLE
            }

            override fun serialEvent(event: SerialPortEvent) {
                if (event.eventType != SerialPort.LISTENING_EVENT_DATA_AVAILABLE) {
                    return
                }
                val newData = ByteArray(serialPort.bytesAvailable())
                val numRead = serialPort.readBytes(newData, newData.size.toLong())
                //var nnn = uintArrayOf(newData)
                //nnn =
                if (newData[1].toUInt() == 0u) {
                    l2.text = "~" + newData[0].toUByte()
                } else {
                    val output = newData[0].toUByte() + newData[1].toUByte() * 255u
                    l2.text = "" + output
                }
                println("conv " + (newData.toUByteArray()).joinToString())
                curPoint = (newData[0]).toUByte().toDouble()
                firstAnalog()

                //println("well: ${newData[3].toUByte()} ${newData[5].toUByte()}")
                //System.out.println("Read " + numRead + " bytes. " + newData[0]);
                //System.out.println(bytesToHex(newData).toString());
            }
        })
        println("Lets start!")
        var a = 0
        val bb = byteArrayOf(0x74.toByte(), 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00)
        try {
            Thread.sleep(2000)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
        while (a < 10) {
            serialPort.writeBytes(bb, 1)
            //            try {
            println("goo " + bb.size)
            //                serialPort.getOutputStream().write(bb);
//                //serialPort.getOutputStream().flush();
//            } catch (IOException e) {
//                System.out.println("pizdec "+e.getMessage());
//                e.printStackTrace();
//            }
            a++
        }

    }
    var acc = 2.0
    var howLong = 0.5
    var curPoint = 0.0
    var curPoint2 = 0.0

    private fun firstAnalog(){
        if (t < (100* howLong).toInt()){
            curPoint += acc
        } else if (t in (50* howLong).toInt()..(52* howLong).toInt()) {
            curPoint -= 0.1
        } else {
            curPoint -= 0.5
        }


        t++
        if(t > 256 && curPoint < 0){
            t = 0
            //this.cancel()
        }
        //if()

        gm1.setSpeed(Date(),curPoint)
    }

    private fun launchDemo() {

        fixedRateTimer("timer", true, 100L, 40) {
//            if (d < 500){
//                if (curPoint < 2.123*(0..100).random()){
//                    curPoint++
//                }else{
//                    curPoint--
//                }
//            } else {
//
//                curPoint--
//
//            }
            if (t < (100* howLong).toInt()){
                curPoint += acc
            } else if (t in (50* howLong).toInt()..(52* howLong).toInt()) {
                curPoint -= 0.1
            } else {
                curPoint -= 0.5
            }


            t++
            if(t > 100 && curPoint < 0){
                //t = 0
                this.cancel()
            }
            gm1.setSpeed(Date(),curPoint)
        }
        fixedRateTimer("timer", true, 100L, 40) {
//            if (d < 500){
//                if (curPoint < 2.123*(0..100).random()){
//                    curPoint++
//                }else{
//                    curPoint--
//                }
//            } else {
//
//                curPoint--
//
//            }
            if (t < (100* howLong).toInt()){
                curPoint2 += acc
            } else if (t in (50* howLong).toInt()..(52* howLong).toInt()) {
                curPoint2 -= 0.1
            } else {
                curPoint2 -= 0.5
            }


            t++
            if(t > 100 && curPoint2 < 0){
                //t = 0
                this.cancel()
            }
            gm2.setSpeed(Date(),curPoint2)
        }
        println("new ${t}")
    }

    var lastN = 0.0
    var ready = 0.0
    fun acc(speedIdeal : Double){
        ready = lastN
        var tim = fixedRateTimer("timer", true, 100L, 10) {
            //spmeter.setSpeed(Date(),25.0*(0..5).random())

            if (ready == speedIdeal){
                lastN = speedIdeal
                this.cancel()
            }else{

                gm1.setSpeed(Date(), ready)

            }
            ready++
            println("~~~ ${lastN} ${ready}  ${speedIdeal}")


        }
    }

}