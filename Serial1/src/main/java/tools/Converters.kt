package tools

import org.apache.commons.math3.filter.*
import org.apache.commons.math3.linear.Array2DRowRealMatrix
import org.apache.commons.math3.linear.ArrayRealVector
import org.apache.commons.math3.linear.RealMatrix
import org.apache.commons.math3.linear.RealVector


fun onesAndTens(onesRAW : Byte,tensRAW : Byte) : Double{
    var ones = onesRAW.toUInt()
    var tens = tensRAW.toUInt()

    if (tens == 0u) {

        return ones.toDouble()

    } else {

        return ( ones + tens * 255u ).toDouble()

    }
}

/**
 * I took it as an example from:
 * @link https://commons.apache.org/proper/commons-math/userguide/filter.html
 */
var constantVoltage: Double = 0.0
var measurementNoise = 10.0
var processNoise = 1e-5

// A = [ 1 ]
var A: RealMatrix = Array2DRowRealMatrix(doubleArrayOf(1.0))

// B = null
var B: RealMatrix? = null

// H = [ 1 ]
var H: RealMatrix = Array2DRowRealMatrix(doubleArrayOf(1.0))

// x = [ 10 ]
var x: RealVector = ArrayRealVector(doubleArrayOf(constantVoltage))

// Q = [ 1e-5 ]
var Q: RealMatrix = Array2DRowRealMatrix(doubleArrayOf(processNoise))

// P = [ 1 ]
var P0: RealMatrix = Array2DRowRealMatrix(doubleArrayOf(1.0))

// R = [ 0.1 ]
var R: RealMatrix = Array2DRowRealMatrix(doubleArrayOf(measurementNoise))

var pm: ProcessModel = DefaultProcessModel(A, B, Q, x, P0)
var mm: MeasurementModel = DefaultMeasurementModel(H, R)
var filter = KalmanFilter(pm, mm)

// process and measurement noise vectors
var pNoise: RealVector = ArrayRealVector(1)
var mNoise: RealVector = ArrayRealVector(1)

fun filterKalman(cont: Double): Double {
    constantVoltage = cont
    /////////////////////
    //val rand: RandomGenerator = JDKRandomGenerator()
    filter.predict()

    // simulate the process
    pNoise.setEntry(0, processNoise)

    // x = A * x + p_noise
    x = A.operate(x).add(pNoise)

    // simulate the measurement
    mNoise.setEntry(0, constantVoltage)

    // z = H * x + m_noise
    val z = H.operate(x).add(mNoise)
    filter.correct(z)
    // println("> $voltage")
    return filter.stateEstimation[0] // output
}
//
//// discrete time interval
//var dt = 0.1
//
//// position measurement noise (meter)
//var measurementNoise = 10.0
//
//// acceleration noise (meter/sec^2)
//var accelNoise = 0.2
//
//// A = [ 1 dt ]
////     [ 0  1 ]
//var A: RealMatrix = Array2DRowRealMatrix(arrayOf(doubleArrayOf(1.0, dt), doubleArrayOf(0.0, 1.0)))
//
//// B = [ dt^2/2 ]
////     [ dt     ]
//var B: RealMatrix = Array2DRowRealMatrix(arrayOf(doubleArrayOf(Math.pow(dt, 2.0) / 2.0), doubleArrayOf(dt)))
//
//// H = [ 1 0 ]
//var H: RealMatrix = Array2DRowRealMatrix(arrayOf(doubleArrayOf(1.0, 0.0)))
//
//// x = [ 0 0 ]
//var x: RealVector = ArrayRealVector(doubleArrayOf(0.0, 0.0))
//
//var tmp: RealMatrix = Array2DRowRealMatrix(
//    arrayOf(
//        doubleArrayOf(Math.pow(dt, 4.0) / 4.0, Math.pow(dt, 3.0) / 2.0), doubleArrayOf(
//            Math.pow(dt, 3.0) / 2.0, Math.pow(dt, 2.0)
//        )
//    )
//)
//
//// Q = [ dt^4/4 dt^3/2 ]
////     [ dt^3/2 dt^2   ]
//var Q = tmp.scalarMultiply(Math.pow(accelNoise, 2.0))
//
//// P0 = [ 1 1 ]
////      [ 1 1 ]
//var P0: RealMatrix = Array2DRowRealMatrix(arrayOf(doubleArrayOf(1.0, 1.0), doubleArrayOf(1.0, 1.0)))
//
//// R = [ measurementNoise^2 ]
//var R: RealMatrix = Array2DRowRealMatrix(doubleArrayOf(Math.pow(measurementNoise, 2.0)))
//
//// constant control input, increase velocity by 0.1 m/s per cycle
//var u: RealVector = ArrayRealVector(doubleArrayOf(0.1))
//
//var pm: ProcessModel = DefaultProcessModel(A, B, Q, x, P0)
//var mm: MeasurementModel = DefaultMeasurementModel(H, R)
//var filter = KalmanFilter(pm, mm)
//
//var rand: RandomGenerator = JDKRandomGenerator()
//
//var tmpPNoise: RealVector = ArrayRealVector(doubleArrayOf(Math.pow(dt, 2.0) / 2.0, dt))
//var mNoise: RealVector = ArrayRealVector(1)
//
//fun kalmanDynamic(input : Double){
//    filter.predict(u)
//
//    // simulate the process
//    val pNoise = tmpPNoise.mapMultiply(accelNoise * rand.nextGaussian())
//
//    // x = A * x + B * u + pNoise
//
//    // x = A * x + B * u + pNoise
//    x = A.operate(x).add(B.operate(u)).add(pNoise)
//
//    // simulate the measurement
//
//    // simulate the measurement
//    mNoise.setEntry(0, measurementNoise * input)
//
//    // z = H * x + m_noise
//
//    // z = H * x + m_noise
//    val z = H.operate(x).add(mNoise)
//
//    filter.correct(z)
//
//    val position = filter.stateEstimation[0]
//    val velocity = filter.stateEstimation[1]
//}