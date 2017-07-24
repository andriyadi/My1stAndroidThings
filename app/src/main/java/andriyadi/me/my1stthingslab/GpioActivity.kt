package andriyadi.me.my1stthingslab

import android.app.Activity
import android.os.Bundle
import android.util.Log
import com.google.android.things.contrib.driver.button.Button
import com.google.android.things.pio.Gpio
import com.google.android.things.pio.PeripheralManagerService
import java.io.IOException

class GpioActivity : Activity() {

    private val TAG = "MyThingsLab"

    private var mLedGpio: Gpio? = null
    private var mButton: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val svc = PeripheralManagerService()

        try {
            mLedGpio = svc.openGpio(BoardDefaults.gpioForLED)
            mLedGpio?.setDirection(Gpio.DIRECTION_OUT_INITIALLY_HIGH)

            mButton = Button(BoardDefaults.gpioForButton, Button.LogicState.PRESSED_WHEN_HIGH)
            mButton?.setOnButtonEventListener { button, pressed ->
                mLedGpio?.value = pressed
            }
        }
        catch (e: IOException) {
            Log.e(TAG, "error led gpio")
        }
    }
}
