package com.android.digantjagtap.shakyrandomgenerator;

/**
 * Created by digantjagtap on 9/13/17.
 */

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.startapp.android.publish.adsCommon.StartAppAd;
import com.startapp.android.publish.adsCommon.StartAppSDK;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private StartAppAd startAppAd = new StartAppAd(this);
    private SensorManager sm;

    private float acelVal;
    private float acelLast;
    private float shake;

    private EditText minEditText;
    private EditText maxEditText;
    private TextView messageText;
    private TextView resultText;
    private ImageView shakeImage;

    private boolean allowRandomCompute = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StartAppSDK.init(this, "208948028", true);
        StartAppAd.disableSplash();
        setContentView(R.layout.activity_main);

        messageText = (TextView) findViewById(R.id.messageText);
        minEditText = (EditText) findViewById(R.id.editText);
        maxEditText = (EditText) findViewById(R.id.editText2);
        shakeImage = (ImageView) findViewById(R.id.shakeImage);
        resultText = (TextView) findViewById(R.id.resultTextView);

        minEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
               // minEditText.setText(s);
                if(!maxEditText.getText().toString().isEmpty() && !minEditText.getText().toString().isEmpty()){
                    int min = Integer.parseInt(minEditText.getText().toString());
                    int max = Integer.parseInt(maxEditText.getText().toString());
                    if(max > min){
                        allowRandomCompute = true;
                        messageText.setText("Shake it now!");
                    }
                    else{
                        messageText.setText("Min should be lesser than Max!");
                        allowRandomCompute = false;
                    }
                }
                else{
                    allowRandomCompute = false;
                }
            }
        });

        maxEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                // minEditText.setText(s);
                if(!maxEditText.getText().toString().isEmpty() && !minEditText.getText().toString().isEmpty()){
                    int min = Integer.parseInt(minEditText.getText().toString());
                    int max = Integer.parseInt(maxEditText.getText().toString());
                    if(max > min){
                        allowRandomCompute = true;
                        messageText.setText("Shake it now!");
                    }
                    else{
                        messageText.setText("Min should be lesser than Max!");
                        allowRandomCompute = false;
                    }
                }
                else{
                    allowRandomCompute = false;
                }
            }
        });


        sm = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sm.registerListener(sensorListener, sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);

        acelVal = SensorManager.GRAVITY_EARTH;
        acelLast = SensorManager.GRAVITY_EARTH;
        shake = 0.00f;
    }

    @Override
    protected void onResume() {
        super.onResume();
        startAppAd.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        startAppAd.onPause();
    }

    @Override
    public void onBackPressed(){
        startAppAd.onBackPressed();
        super.onBackPressed();
    }

   private final SensorEventListener sensorListener = new SensorEventListener() {
       @Override
       public void onSensorChanged(SensorEvent sensorEvent) {

           float x = sensorEvent.values[0];
           float y = sensorEvent.values[1];
           float z = sensorEvent.values[2];

           acelLast = acelVal;
           acelVal = (float) Math.sqrt((double) (x*x + y*y + z*z));
           float delta = acelVal - acelLast;
           shake = shake * 0.9f + delta;
           if (shake > 12) {


               if(allowRandomCompute){
                   int minValue = Integer.parseInt(minEditText.getText().toString());
                   int maxValue = Integer.parseInt(maxEditText.getText().toString());
                   int random = new Random().nextInt((maxValue - minValue) + 1) + minValue;
                   resultText.setText(Integer.toString(random));
                   shakeImage.setVisibility(View.INVISIBLE);
                   resultText.setVisibility(View.VISIBLE);
               }
              /* Log.d("V", String.valueOf(allowRandomCompute));
               Toast toast = Toast.makeText(getApplicationContext(), "Do not share me" + allowRandomCompute, Toast.LENGTH_SHORT);
               toast.show(); */
           }
       }

       @Override
       public void onAccuracyChanged(Sensor sensor, int i) {

       }
   };
}
