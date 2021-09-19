import GaugeMeters.GaugeMaster
import GaugeMeters.GaugeMaster2
import GaugeMeters.GaugeMaster3
import GaugeMeters.GaugeMaster4
import com.fazecast.jSerialComm.SerialPort
import com.fazecast.jSerialComm.SerialPortDataListener
import com.fazecast.jSerialComm.SerialPortEvent
import tools.filterKalman
import tools.onesAndTens
import java.awt.Color
import java.awt.Font
import java.awt.event.ActionListener
import java.awt.event.WindowEvent
import java.awt.event.WindowListener
import java.io.File
import java.io.IOException
import java.util.*
import javax.sound.sampled.AudioSystem
import javax.sound.sampled.LineUnavailableException
import javax.sound.sampled.UnsupportedAudioFileException
import javax.swing.*
import kotlin.concurrent.fixedRateTimer

/**
 * @author >> ars.tagaev
 *
 */

//import com.fazecast.jSerialComm.*;
object MainTwo : JFrame() {

    val serialPort: SerialPort = SerialPort.getCommPort("COM3")

    var gm1 = GaugeMaster()
    var gm2 = GaugeMaster2()
    var gm3 = GaugeMaster3()
    var gm4 = GaugeMaster4()

    var gm5 = GaugeMaster()
    var gm6 = GaugeMaster()
    var gm7 = GaugeMaster()
    var gm8 = GaugeMaster()

    var t = 0
    var asdx = arrayListOf<Int>(1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,33,34,34,32,31,30,29,28,27,26,25,24,23,22,21,20,19,18,17,16,15,14,13,12,11,10,9,8,7,6,5,4,3,2,1,0)
    var asdx2 = arrayListOf<Int>(1,2,3,15,16,17,18,19,26,27,28,29,30,31,32,33,34,6,7,8,9,10,11,12,13,14,15,16)

    var isAllWork = false

    var z = 0L
    var y = 0L

    private lateinit var newData : ByteArray

    @OptIn(ExperimentalUnsignedTypes::class)
    @JvmStatic
    fun main(args: Array<String>) {

        val window = JFrame()
        window.defaultCloseOperation = WindowConstants.EXIT_ON_CLOSE;
        window.setSize(1000, 1000)


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

        gm1.setMax(4095.0)
        gm2.setMax(4095.0)
        gm3.setMax(4095.0)
        gm4.setMax(4095.0)
        gm5.setMax(4095.0)
        gm6.setMax(4095.0)
        gm7.setMax(4095.0)
        gm8.setMax(4095.0)
        //10   220     330      440
        //  110    110     110
        gm1.setBounds(10,10,200,200)
        gm2.setBounds(220,10,200,200)
        gm3.setBounds(430,10,200,200)
        gm4.setBounds(640,10,200,200)
        gm5.setBounds(10,210,200,200)
        gm6.setBounds(220,210,200,200)
        gm7.setBounds(430,210,200,200)
        gm8.setBounds(640,210,200,200)
        // добавление фигур в различные слои
        //lp.add(spmeter, JLayeredPane.DRAG_LAYER)
        //lp.add(label1)
        ////////////////////////////////////////
        val panel = JPanel() //Create JPanel Object
        panel.layout = null
        panel.setBounds(0, 0, 1000, 400) //set dimensions for Panel
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
        panel.add(gm3)
        panel.add(gm4)
        panel.add(gm5)
        panel.add(gm6)
        panel.add(gm7)
        panel.add(gm8)

        panel.add(l2)
        ////////////////
        window.setLayout(null);
        //window.add(label1)
        //window.add(b1)
        //window.add(label2)
        window.add(panel)
//        window.add(l2)
        window.isVisible = true


        //        if(serialPort.openPort())
//            System.out.println("Port opened successfully.");
//        else {
//            System.out.println("Unable to open the port.");
//            return;
//        }
        sound(0)
        initSerialCommunication()
        isAllWork = true
        //serialPort.removeDataListener()
        // Here is looper-checker




        window.addWindowListener(object : WindowListener{
            override fun windowOpened(e: WindowEvent?) {
                //TODO("Not yet implemented")
            }

            override fun windowClosing(e: WindowEvent?) {
               //TODO("Not yet implemented")
            }

            override fun windowClosed(e: WindowEvent?) {
                stopSerialCommunication()
                //TODO("Not yet implemented")
            }

            override fun windowIconified(e: WindowEvent?) {
                //TODO("Not yet implemented")
            }

            override fun windowDeiconified(e: WindowEvent?) {
                //TODO("Not yet implemented")
            }

            override fun windowActivated(e: WindowEvent?) {
               // TODO("Not yet implemented")
            }

            override fun windowDeactivated(e: WindowEvent?) {
               // TODO("Not yet implemented")
            }

        })

        fixedRateTimer("timer", false, 5000L,3000) {
            if (!serialPort.isOpen){
                sound(-1)
                sound(-1)
                sound(-1)

                println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ stopped!")
                stopSerialCommunication()
                initSerialCommunication()
            }
            if (newData.size != 16){
                println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ by size stopped!")
                stopSerialCommunication()
                initSerialCommunication()
                return@fixedRateTimer
            }

            println("qqqq z: ${z} y:${y}")


            if (z > y){
                y = z

            }else if (z == y){
                sound(-1)
                sound(-1)
                sound(-1)
                sound(-1)
                sound(-1)
                sound(-1)

                println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ z ~ y stopped!")
                stopSerialCommunication()
                initSerialCommunication()

            }

        }

    }



    private fun initSerialCommunication() {
        println(">>>serial communication has been started")
        serialPort.baudRate = 115200
        serialPort.setComPortParameters(115200, 8, 1, SerialPort.NO_PARITY)
        serialPort.setComPortTimeouts(SerialPort.TIMEOUT_SCANNER, 0, 0)
        serialPort.openPort()

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

        serialPort.addDataListener(object : SerialPortDataListener {
            override fun getListeningEvents(): Int {
                return SerialPort.LISTENING_EVENT_DATA_AVAILABLE
            }

            override fun serialEvent(event: SerialPortEvent) {
                if (event.eventType != SerialPort.LISTENING_EVENT_DATA_AVAILABLE) {
                    sound(-1)
                    return
                }
                newData = ByteArray(serialPort.bytesAvailable())
                val numRead = serialPort.readBytes(newData, newData.size.toLong())
                //var nnn = uintArrayOf(newData)
                //nnn =

                println("conv " + (newData.toUByteArray()).joinToString() + "[[ ${newData.size}")
                //curPoint =
                firstAnalog( filterKalman( onesAndTens(newData[0],newData[1])))
                secondAnalog(onesAndTens(newData[2],newData[3]))
                thirdAnalog( onesAndTens(newData[4],newData[5]))
                fourthAnalog(onesAndTens(newData[6],newData[7]))

                fiveAnalog( onesAndTens(newData[8],newData[9]))
                sixAnalog(  onesAndTens(newData[10],newData[11]))
                sevenAnalog(onesAndTens(newData[12],newData[13]))
                eightAnalog(onesAndTens(newData[14],newData[15]))
                z++
            }
        })
    }

    fun stopSerialCommunication(){
        serialPort.removeDataListener()
        serialPort.closePort()
    }

    fun sound(typeOfSound : Int) {
        try {
            var soundFile = File("src/main/resources/autopilot_on.wav") //Звуковой файл
            when(typeOfSound){
                0 -> {
                    soundFile = File("src/main/resources/autopilot_on.wav") //Звуковой файл
                }
                -1 -> {
                    soundFile = File("src/main/resources/error.wav") //Звуковой файл
                }
                else -> {
                    soundFile = File("src/main/resources/autopilot_on.wav") //Звуковой файл
                }
            }


            //println(" >>>>>> ${soundFile.absolutePath}")
            //Получаем AudioInputStream
            //Вот тут могут полететь IOException и UnsupportedAudioFileException
            val ais = AudioSystem.getAudioInputStream(soundFile)

            //Получаем реализацию интерфейса Clip
            //Может выкинуть LineUnavailableException
            val clip = AudioSystem.getClip()

            //Загружаем наш звуковой поток в Clip
            //Может выкинуть IOException и LineUnavailableException
            clip.open(ais)
            clip.framePosition = 0 //устанавливаем указатель на старт
            clip.start() //Поехали!!!

            //Если не запущено других потоков, то стоит подождать, пока клип не закончится
            //В GUI-приложениях следующие 3 строчки не понадобятся
            Thread.sleep(clip.microsecondLength / 1000)
            clip.stop() //Останавливаем
            clip.close() //Закрываем
        } catch (exc: IOException) {
            exc.printStackTrace()
        } catch (exc: UnsupportedAudioFileException) {
            exc.printStackTrace()
        } catch (exc: LineUnavailableException) {
            exc.printStackTrace()
        } catch (exc: InterruptedException) {
        }
//        try {
//            val audioInputStream = AudioSystem.getAudioInputStream(this.javaClass.getResource("autopilot_on.wav"))
//            val clip = AudioSystem.getClip()
//            clip.open(audioInputStream)
//            clip.start()
//            // If you want the sound to loop infinitely, then put: clip.loop(Clip.LOOP_CONTINUOUSLY);
//            // If you want to stop the sound, then use clip.stop();
//        } catch (ex: Exception) {
//            ex.printStackTrace()
//        }
    }
    var SMOOTH_SHOW = true
    var acc = 5
    var howLong = 0.5

    var curPoint = 0.0
    var lastNum = 0.0

    var curPoint2= 0.0
    var lastNum2 = 0.0

    var curPoint3= 0.0
    var lastNum3 = 0.0

    var curPoint4= 0.0
    var lastNum4 = 0.0

    var curPoint5= 0.0
    var lastNum5 = 0.0

    var curPoint6= 0.0
    var lastNum6 = 0.0

    var curPoint7= 0.0
    var lastNum7 = 0.0

    var curPoint8= 0.0
    var lastNum8 = 0.0

    private fun firstAnalog(inputP : Double){
        gm1.setSpeed(Date(),inputP)
        return
        if (SMOOTH_SHOW){


            if (inputP > lastNum){

                curPoint += acc

            }else if ( inputP < lastNum ) {

                curPoint -= acc

            }else { // if equals

            }
            lastNum = curPoint

            gm1.setSpeed(Date(),curPoint) //
        }else{
            gm1.setSpeed(Date(),inputP) //
        }
    }

    private fun secondAnalog(inputP : Double){
        if (SMOOTH_SHOW){


            if (inputP > lastNum2){

                curPoint2 += acc

            }else if ( inputP < lastNum2 ) {

                curPoint2 -= acc

            }else { // if equals

            }
            lastNum2 = curPoint2

            gm2.setSpeed(Date(),curPoint2) //
        }else{
            gm2.setSpeed(Date(),inputP) //
        }
    }

    private fun thirdAnalog(inputP : Double){
        if (SMOOTH_SHOW){


            if (inputP > lastNum3){

                curPoint3 += acc

            }else if ( inputP < lastNum3 ) {

                curPoint3 -= acc

            }else { // if equals

            }
            lastNum3 = curPoint3

            gm3.setSpeed(Date(),curPoint3) //
        }else{
            gm3.setSpeed(Date(),inputP) //
        }
    }

    private fun fourthAnalog(inputP : Double){
        if (SMOOTH_SHOW){


            if (inputP > lastNum4){

                curPoint4 += acc

            }else if ( inputP < lastNum4 ) {

                curPoint4 -= acc

            }else { // if equals

            }
            lastNum4 = curPoint4

            gm4.setSpeed(Date(),curPoint4) //
        }else{
            gm4.setSpeed(Date(),inputP) //
        }
    }

    private fun fiveAnalog(inputP : Double){
        if (SMOOTH_SHOW){


            if (inputP > lastNum5){

                curPoint5 += acc

            }else if ( inputP < lastNum5 ) {

                curPoint5 -= acc

            }else { // if equals

            }
            lastNum5 = curPoint5

            gm5.setSpeed(Date(),curPoint5) //
        }else{
            gm5.setSpeed(Date(),inputP) //
        }
    }

    private fun sixAnalog(inputP : Double){
        if (SMOOTH_SHOW){


            if (inputP > lastNum6){

                curPoint6 += acc

            }else if ( inputP < lastNum6 ) {

                curPoint6 -= acc

            }else { // if equals

            }
            lastNum6 = curPoint6

            gm6.setSpeed(Date(),curPoint6) //
        }else{
            gm6.setSpeed(Date(),inputP) //
        }
    }

    private fun sevenAnalog(inputP : Double){
        if (SMOOTH_SHOW){


            if (inputP > lastNum7){

                curPoint7 += acc

            }else if ( inputP < lastNum7 ) {

                curPoint7 -= acc

            }else { // if equals

            }
            lastNum7 = curPoint7

            gm7.setSpeed(Date(),curPoint7) //
        }else{
            gm7.setSpeed(Date(),inputP) //
        }
    }

    private fun eightAnalog(inputP : Double){
        if (SMOOTH_SHOW){


            if (inputP > lastNum8){

                curPoint8 += acc

            }else if ( inputP < lastNum8 ) {

                curPoint8 -= acc

            }else { // if equals

            }
            lastNum8 = curPoint8

            gm8.setSpeed(Date(),curPoint8) //
        }else{
            gm8.setSpeed(Date(),inputP) //
        }
    }
    // Just for demo presentation, non-useful
    private fun launchDemo() {

//        fixedRateTimer("timer", true, 100L, 40) {
////            if (d < 500){
////                if (curPoint < 2.123*(0..100).random()){
////                    curPoint++
////                }else{
////                    curPoint--
////                }
////            } else {
////
////                curPoint--
////
////            }
//            if (t < (100* howLong).toInt()){
//                curPoint += acc
//            } else if (t in (50* howLong).toInt()..(52* howLong).toInt()) {
//                curPoint -= 0.1
//            } else {
//                curPoint -= 0.5
//            }
//
//
//            t++
//            if(t > 100 && curPoint < 0){
//                //t = 0
//                this.cancel()
//            }
//            gm1.setSpeed(Date(),curPoint)
//        }
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



}