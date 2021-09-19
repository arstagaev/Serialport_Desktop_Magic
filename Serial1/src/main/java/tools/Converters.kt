package tools

import org.apache.commons.math3.filter.*
import org.apache.commons.math3.linear.Array2DRowRealMatrix
import org.apache.commons.math3.linear.ArrayRealVector
import org.apache.commons.math3.linear.RealMatrix
import org.apache.commons.math3.linear.RealVector
import org.apache.commons.math3.random.JDKRandomGenerator
import org.apache.commons.math3.random.RandomGenerator

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
fun filterKalman(cont: Double): Double {
    var constantVoltage = cont.toDouble() // <=== input
    val measurementNoise = 0.0 //                 in example has been 0.1
    val processNoise = 1e-5    //                 process noise, the number can be set

    // A = [ 1 ]
    val A: RealMatrix = Array2DRowRealMatrix(doubleArrayOf(1.0))
    // B = null
    val B: RealMatrix? = null
    // H = [ 1 ]
    val H: RealMatrix = Array2DRowRealMatrix(doubleArrayOf(1.0))
    // x = [ 10 ]
    var x: RealVector? = ArrayRealVector(doubleArrayOf(constantVoltage))
    // Q = [ 1e-5 ]
    val Q: RealMatrix = Array2DRowRealMatrix(doubleArrayOf(processNoise))
    // P = [ 1 ]
    val P0: RealMatrix = Array2DRowRealMatrix(doubleArrayOf(1.0))
    // R = [ 0.1 ]
    val R: RealMatrix = Array2DRowRealMatrix(doubleArrayOf(measurementNoise))
    val pm: ProcessModel = DefaultProcessModel(A, B, Q, x, P0)
    val mm: MeasurementModel = DefaultMeasurementModel(H, R)
    val filter = KalmanFilter(pm, mm)

    // process and measurement noise vectors
    val pNoise: RealVector = ArrayRealVector(1)
    val mNoise: RealVector = ArrayRealVector(1)
    val rand: RandomGenerator = JDKRandomGenerator()
    filter.predict()

    // simulate the process
    pNoise.setEntry(0, processNoise * rand.nextGaussian())

    // x = A * x + p_noise
    x = A.operate(x).add(pNoise)

    // simulate the measurement
    mNoise.setEntry(0, measurementNoise * rand.nextGaussian())

    // z = H * x + m_noise
    val z = H.operate(x).add(mNoise)
    filter.correct(z)
    // println("> $voltage")
    return filter.stateEstimation[0] // output
}