package com.application.basicstepcounter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity
{
    private TextView textView;  //For showing the value
    private double MagPrevious = 0;  //something to compare with the Magnitude
    private Integer stepCounter =0;  //starting of the step counting


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textView); //For showing the value
        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);  //getting the sensor service
        Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);  //calling an object name sensor, in that object calling the accelerometer
        SensorEventListener stepDetector = new SensorEventListener()  //for getting the value from the sensor
        {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent)  //whenever there is a value change the value will come to the  sensorEvent
            {
                if (sensorEvent != null)  //extracting the values
                {
                    float x_acceleration = sensorEvent.values[0];
                    float y_acceleration = sensorEvent.values[1];
                    float z_acceleration = sensorEvent.values[2];

                    double Magnitude = Math.sqrt(x_acceleration * x_acceleration + y_acceleration * y_acceleration + z_acceleration * z_acceleration); // measuring magnitude
                    double MagnitudeDelta = Magnitude - MagPrevious;  //Comparing with the MagPrevious
                    MagPrevious = Magnitude;


                    if (MagnitudeDelta > 6)
                    {
                        stepCounter++;
                    }

                    textView.setText(stepCounter.toString());  //Send updated value to the text value

                }

            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy)
            {

            }
        };

        sensorManager.registerListener(stepDetector, sensor, SensorManager.SENSOR_DELAY_NORMAL);  //(Must do) register the sensorManager


    }

    //Save previous data
    protected void onPause()  //when the app is paused
    {
        super.onPause(); //For overwriting
        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);  //data should be private
        SharedPreferences.Editor editor = sharedPreferences.edit();  //editing the SharedPreferences
        editor.clear();  //clearing the before data
        editor.putInt("stepCounter", stepCounter);  //it will save the stepCounter
        editor.apply();
    }

    protected void onStop()  //when the app is stopped
    {
        super.onStop(); //For overwriting
        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);  //data should be private
        SharedPreferences.Editor editor = sharedPreferences.edit();  //editing the SharedPreferences
        editor.clear();  //clearing the before data
        editor.putInt("stepCounter", stepCounter);  //it will save the stepCounter
        editor.apply();
    }

    @Override
    protected void onResume() //when the app is resumed
    {
        super.onResume();
        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);  //data should be private
        stepCounter = sharedPreferences.getInt("stepCounter", 0);  //show the previous stepCounter
    }
}
