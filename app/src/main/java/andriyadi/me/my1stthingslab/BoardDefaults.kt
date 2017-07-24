package andriyadi.me.my1stthingslab

import android.os.Build

import com.google.android.things.pio.PeripheralManagerService

object BoardDefaults {
    private val DEVICE_EDISON_ARDUINO = "edison_arduino"
    private val DEVICE_EDISON = "edison"
    private val DEVICE_RPI3 = "rpi3"
    private val DEVICE_NXP = "imx6ul"
    private var sBoardVariant = ""

    val i2cBus: String
        get() {
            when (boardVariant) {
                DEVICE_EDISON_ARDUINO -> return "I2C6"
                DEVICE_EDISON -> return "I2C1"
                DEVICE_RPI3 -> return "I2C1"
                DEVICE_NXP -> return "I2C2"
                else -> throw IllegalArgumentException("Unknown device: " + Build.DEVICE)
            }
        }

    val gpioForButton: String
        get() {
            when (boardVariant) {
                DEVICE_RPI3 -> return "BCM5"
                else -> throw IllegalArgumentException("Unknown device: " + Build.DEVICE)
            }
        }

    val gpioForLED: String
        get() {
            when (boardVariant) {
                DEVICE_RPI3 -> return "BCM6"
                else -> throw IllegalArgumentException("Unknown device: " + Build.DEVICE)
            }
        }

    private // For the edison check the pin prefix
            // to always return Edison Breakout pin name when applicable.
    val boardVariant: String
        get() {
            if (!sBoardVariant.isEmpty()) {
                return sBoardVariant
            }
            sBoardVariant = Build.DEVICE
            if (sBoardVariant == DEVICE_EDISON) {
                val pioService = PeripheralManagerService()
                val gpioList = pioService.gpioList
                if (gpioList.size != 0) {
                    val pin = gpioList[0]
                    if (pin.startsWith("IO")) {
                        sBoardVariant = DEVICE_EDISON_ARDUINO
                    }
                }
            }
            return sBoardVariant
        }

}