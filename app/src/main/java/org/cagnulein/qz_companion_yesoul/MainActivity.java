package org.cagnulein.qz_companion_yesoul;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.net.DhcpInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.widget.TextView;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import android.util.Log;

import com.yesoulchina.support.serialport.*;

import java.io.IOException;
import java.net.InetAddress;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = "QZ:Service";

    private TextView resistance;
    private TextView cadence;
    private TextView power;

    DatagramSocket socket = null;

    int clientPort = 8002;

    byte[] lmessage = new byte[1024];
    DatagramPacket packet = new DatagramPacket(lmessage, lmessage.length);


    private final Handler handler = new Handler();
    private final Runnable updateTask = new Runnable() {
        @Override
        public void run() {
            // Update your UI here
            // For example, refresh a TextView, etc.
            cadence.setText("Cadence: " + manager.getRoundPerMin());
            power.setText("Power: " + manager.getPower());
            resistance.setText("Resistance: " + manager.getResist());

            sendBroadcast("Changed RPM " + manager.getRoundPerMin());
            sendBroadcast("Changed Watts " + manager.getPower());
            sendBroadcast("Changed Resistance " + manager.getResist());

            // Schedule the task to run again after 1 second
            handler.postDelayed(this, 1000);
        }
    };

    SerialPortManager manager = SerialPortManager.getManager();

    private static void writeLog(String command) {
        Log.d(LOG_TAG, command);
    }

    public void sendBroadcast(String messageStr) {
        StrictMode.ThreadPolicy policy = new   StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        try {

            if(socket == null) {
                socket = new DatagramSocket();
                socket.setBroadcast(true);
            }

            byte[] sendData = messageStr.getBytes();
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, getBroadcastAddress(), this.clientPort);
            socket.send(sendPacket);
            writeLog(messageStr);
        } catch (IOException e) {
            writeLog("IOException: " + e.getMessage());
        }
    }
    InetAddress getBroadcastAddress() throws IOException {
        WifiManager wifi = (WifiManager)    getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        DhcpInfo dhcp = null;
        try {
            dhcp = wifi.getDhcpInfo();
            int broadcast = (dhcp.ipAddress & dhcp.netmask) | ~dhcp.netmask;
            byte[] quads = new byte[4];
            for (int k = 0; k < 4; k++)
                quads[k] = (byte) ((broadcast >> k * 8) & 0xFF);
            return InetAddress.getByAddress(quads);
        } catch (Exception e) {
            writeLog( "IOException: " + e.getMessage());
        }
        byte[] quads = new byte[4];
        return InetAddress.getByAddress(quads);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cadence = findViewById(R.id.cadence);
        resistance = findViewById(R.id.resistance);
        power = findViewById(R.id.power);

        updateTask.run();
    }
}