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
import android.text.Layout;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.util.Random;

import static com.android.digantjagtap.shakyrandomgenerator.R.id.adView;

public class MainActivity extends AppCompatActivity {

    private AdView mAdView;
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
        setContentView(R.layout.activity_main);

        MobileAds.initialize(this, "ca-app-pub-5382733936047144~2353664128");
//ca-app-pub-5382733936047144/2333482829
        mAdView = (AdView) findViewById(adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


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

                try {
                    // minEditText.setText(s);
                    if (!maxEditText.getText().toString().isEmpty() && !minEditText.getText().toString().isEmpty()) {
                        int min = Integer.parseInt(minEditText.getText().toString());
                        int max = Integer.parseInt(maxEditText.getText().toString());
                        if (max > min) {
                            allowRandomCompute = true;
                            messageText.setText("Shake it now!");
                        } else {
                            messageText.setText("Min should be lesser than Max!");
                            allowRandomCompute = false;
                        }
                    } else {
                        allowRandomCompute = false;
                    }
                }
                catch(NumberFormatException e){
                    messageText.setText("Min Number too long");
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

                try {
                    // minEditText.setText(s);
                    if (!maxEditText.getText().toString().isEmpty() && !minEditText.getText().toString().isEmpty()) {
                        int min = Integer.parseInt(minEditText.getText().toString());
                        int max = Integer.parseInt(maxEditText.getText().toString());
                        if (max > min) {
                            allowRandomCompute = true;
                            messageText.setText("Shake it now!");
                        } else {
                            messageText.setText("Min should be lesser than Max!");
                            allowRandomCompute = false;
                        }
                    } else {
                        allowRandomCompute = false;
                    }
                }
                catch(NumberFormatException e){
                    messageText.setText("Max Number too long");
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
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
    }

    /**
     * Hides the soft keyboard
     */
    public void hideSoftKeyboard() {
        if(getCurrentFocus()!=null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    /**
     * Shows the soft keyboard
     */
    public void showSoftKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        view.requestFocus();
        inputMethodManager.showSoftInput(view, 0);
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
           if (shake > 10) {


               if(allowRandomCompute){
                   hideSoftKeyboard();
                   int minValue = Integer.parseInt(minEditText.getText().toString());
                   int maxValue = Integer.parseInt(maxEditText.getText().toString());
                   int random = new Random().nextInt((maxValue - minValue) + 1) + minValue;
                   resultText.setText(Integer.toString(random));
                   shakeImage.setVisibility(View.INVISIBLE);
                   resultText.setVisibility(View.VISIBLE);
                   // Check if text is too long
                   Layout l = resultText.getLayout();
                   resultText.setTextSize(100);
                   reduceTextSize(l,0);
               }
              /* Log.d("V", String.valueOf(allowRandomCompute));
               Toast toast = Toast.makeText(getApplicationContext(), "Do not share me" + allowRandomCompute, Toast.LENGTH_SHORT);
               toast.show(); */
           }
       }

       private void reduceTextSize(Layout l, int type){
           int sizeType[] = {70,60,50};
           if (l != null) {
               int lines = l.getLineCount();
               if (lines > 0)
                   if (l.getEllipsisCount(lines-1) > 0)
                   {
                       resultText.setTextSize(sizeType[type]);
                       if(type < sizeType.length-1)
                        reduceTextSize(l,type+1);
                   }
           }
       }

       @Override
       public void onAccuracyChanged(Sensor sensor, int i) {

       }
   };
}
