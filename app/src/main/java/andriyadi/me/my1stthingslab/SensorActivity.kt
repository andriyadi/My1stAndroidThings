package andriyadi.me.my1stthingslab

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.hardware.SensorManager.DynamicSensorCallback
import android.os.Bundle
import android.os.Handler
import android.util.Log
import com.google.android.things.contrib.driver.bmx280.Bmx280SensorDriver
import com.google.android.things.contrib.driver.ssd1306.BitmapHelper
import com.google.android.things.contrib.driver.ssd1306.Ssd1306
import java.io.IOException

class SensorActivity : Activity() {

    private val TAG = SensorActivity::class.java.simpleName

    private var bmp280Driver: Bmx280SensorDriver? = null
    private lateinit var mSensorManager: SensorManager

    private var mTemp: Float = 0.0f
    private var mPress: Float = 0.0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mSensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager

        val dynCb = object: DynamicSensorCallback() {
            override fun onDynamicSensorConnected(sensor: Sensor?) {
                super.onDynamicSensorConnected(sensor)

                Log.i(TAG, "Registering ${sensor?.stringType}")

                if (sensor?.type == Sensor.TYPE_AMBIENT_TEMPERATURE || sensor?.type == Sensor.TYPE_PRESSURE) {
                    mSensorManager.registerListener(
                        object: SensorEventListener {
                            override fun onAccuracyChanged(theSensor: Sensor?, accuracy: Int) {
                                Log.i(TAG, "Sensor accuracy changed: " + accuracy)
                            }

                            override fun onSensorChanged(event: SensorEvent?) {
                                if (event != null) {
                                    Log.i(TAG, "Sensor changed: ${event.sensor.stringType} = ${event.values[0]}")

                                    if (event.sensor.type == Sensor.TYPE_AMBIENT_TEMPERATURE) {
                                        mTemp = event.values[0]
                                    } else if (event.sensor.type == Sensor.TYPE_PRESSURE) {
                                        mPress = event.values[0]
                                    }

                                    mHandler.post(uiRunnable)
                                }
                            }

                        }, sensor, SensorManager.SENSOR_DELAY_NORMAL)
                }
            }
        }

        mSensorManager.registerDynamicSensorCallback(dynCb)

        //Then register BMP280
        try {
            bmp280Driver = Bmx280SensorDriver(BoardDefaults.i2cBus, 0x76)
            bmp280Driver?.registerTemperatureSensor()
            bmp280Driver?.registerPressureSensor()
        }
        catch (e: Exception) {
            Log.e(TAG, "Error registering BMP280")
        }

        initScreen()
    }

    //UI-related code

    private val SCREEN_HEIGHT = 96
    private val SCREEN_WIDTH = 128
    private var mScreen: Ssd1306? = null

    private val mHandler = Handler()

    private val uiRunnable = Runnable {
        if (mScreen == null) {
            return@Runnable
        }

        mScreen?.clearPixels()

        val text1 = "T: %.2f °C".format(mTemp)
        val text2 = "P: %.2f Pa".format(mPress)

        //val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        val paint = Paint()
        paint.textSize = 22f
        paint.color = Color.WHITE
        paint.textAlign = Paint.Align.LEFT

        val textAsBitmap = Bitmap.createBitmap(SCREEN_WIDTH, SCREEN_HEIGHT, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(textAsBitmap)
        canvas.drawText(text1, 0.0f, 0.0f + (24.0f), paint)

        canvas.translate(0.0f, 50.0f)

        canvas.drawText(text2, 0.0f, 0.0f, paint)

        BitmapHelper.setBmpData(mScreen, 0, 0, textAsBitmap, true)

        mScreen?.show()
    }

    private fun initScreen() {
        try {
            mScreen = Ssd1306(BoardDefaults.i2cBus)
        } catch (e: IOException) {
            Log.e(TAG, "Error configuring screen", e)
        }
    }
}
