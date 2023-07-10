package org.cagnulein.qz_companion_yesoul;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import com.yesoulchina.support.serialport.*;

public class MainActivity extends AppCompatActivity {

    SerialPortManager manager = SerialPortManager.getManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}