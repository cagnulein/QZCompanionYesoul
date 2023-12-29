package org.cagnulein.qz_companion_yesoul;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import com.yesoulchina.support.serialport.*;

public class MainActivity extends AppCompatActivity {

    private TextView resistance;
    private TextView cadence;
    private TextView power;

    private final Handler handler = new Handler();
    private final Runnable updateTask = new Runnable() {
        @Override
        public void run() {
            // Update your UI here
            // For example, refresh a TextView, etc.
            cadence.setText("Cadence: " + manager.getRoundPerMin());
            power.setText("Power: " + manager.getPower());
            resistance.setText("Resistance: " + manager.getResist());

            // Schedule the task to run again after 1 second
            handler.postDelayed(this, 1000);
        }
    };

    SerialPortManager manager = SerialPortManager.getManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cadence = findViewById(R.id.cadence);
        resistance = findViewById(R.id.resistance);
        power = findViewById(R.id.power);
    }
}