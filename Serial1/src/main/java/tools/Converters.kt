package tools

fun onesAndTens(onesRAW : Byte,tensRAW : Byte) : Double{
    var ones = onesRAW.toUInt()
    var tens = tensRAW.toUInt()

    if (tens == 0u) {

        return ones.toDouble()

    } else {

        return ( ones + tens * 255u ).toDouble()

    }
}