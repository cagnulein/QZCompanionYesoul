package com.yesoulchina.support.serialport;

import android.os.Build;
import android.util.Log;

import android_serialport_api.SerialPort;
import com.yesoulchina.support.serialport.Protocol;
import com.yesoulchina.support.serialport.SerialProtocol;
import com.yesoulchina.support.serialport.listener.OnBicycleLockStateChangedListener;
import com.yesoulchina.support.serialport.listener.OpenSerialPortListener;
import com.yesoulchina.support.serialport.thread.BicycleDataThread;
import com.yesoulchina.support.serialport.thread.BicycleLockThread;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class SerialPortUtils {
    private static final String TAG = "SerialPortUtils";
    private static volatile SerialPortUtils manager;
    private int baudrate = 19200;
    private Thread bicycleLockThread;
    private InputStream inputStream;
    private boolean isSerialAvaiable = false;
    private Thread mReadThread;
    private String model = "";
    private OutputStream outputStream;
    private String path = "";
    private Thread readSnThread;
    private SerialPort serialPort;
    private OnTonicDataListener tonicDataListener;

    public interface OnTonicDataListener {
        void onDataReceived(SerialProtocol.FrameData frameData);
    }

    private SerialPortUtils() {
        String str = Build.MODEL;
        this.model = str;
        if (str == null) {
            return;
        }
        if (str.equalsIgnoreCase("rk3288")) {
            this.path = "/dev/ttyS3";
        } else if (this.model.equalsIgnoreCase("zk-r328")) {
            this.path = "/dev/ttyS1";
        } else if (this.model.equalsIgnoreCase("rk3368")) {
            this.path = "/dev/ttyS4";
        } else if (this.model.equalsIgnoreCase("BX_A133_A11_YESOUL")) {
            this.path = "/dev/ttyAS2";
        } else if (this.model.equalsIgnoreCase("MStar Android TV")) {
            this.path = "/dev/ttyS0";
        } else if (Build.BRAND.equalsIgnoreCase("allwinner")) {
            this.path = "/dev/ttyAS2";
        } else if (this.model.equalsIgnoreCase("rk3128") || this.model.equalsIgnoreCase("rk3128H") || this.model.equalsIgnoreCase("rk3128Z") || this.model.equalsIgnoreCase("rk312x") || this.model.equalsIgnoreCase("rk3128R")) {
            this.path = "/dev/ttyS1";
        } else {
            this.path = "/dev/ttyS2";
        }
    }

    public static SerialPortUtils getManager() {
        if (manager == null) {
            synchronized (SerialPortUtils.class) {
                if (manager == null) {
                    manager = new SerialPortUtils();
                }
            }
        }
        return manager;
    }

    public void startTrain() {
        stopBicycleDataThread();
        BicycleDataThread bicycleDataThread = new BicycleDataThread(this, this.inputStream, this.tonicDataListener);
        this.mReadThread = bicycleDataThread;
        bicycleDataThread.start();
    }

    public void resumeTrain() {
        if (this.mReadThread == null) {
            BicycleDataThread bicycleDataThread = new BicycleDataThread(this, this.inputStream, this.tonicDataListener);
            this.mReadThread = bicycleDataThread;
            bicycleDataThread.start();
        }
    }

    public void finishTrain() {
        try {
            Thread thread = this.mReadThread;
            if (thread != null) {
                thread.interrupt();
                this.mReadThread = null;
            }
        } catch (Exception unused) {
        }
    }

    public boolean openSerialPort(OpenSerialPortListener openSerialPortListener) {
        try {
            SerialPort serialPort2 = new SerialPort(new File(this.path), this.baudrate);
            this.serialPort = serialPort2;
            this.outputStream = serialPort2.getOutputStream();
            this.inputStream = this.serialPort.getInputStream();
            this.isSerialAvaiable = true;
            openSerialPortListener.onOpenSerialPortSuccess();
        } catch (Exception e) {
            this.isSerialAvaiable = false;
            SerialLog.m440d("打开串口失败：" + this.path + "原因：" + e.getLocalizedMessage());
            openSerialPortListener.onOpenSerialPortFail(e);
        }
        return this.isSerialAvaiable;
    }

    public void closeSerialPort() {
        Thread thread = this.mReadThread;
        if (thread != null) {
            thread.interrupt();
            this.mReadThread = null;
        }
        SerialPort serialPort2 = this.serialPort;
        if (serialPort2 != null) {
            serialPort2.clearBuf();
            this.serialPort.close();
            this.serialPort = null;
        }
    }

    public boolean sendBuffer(byte[] bArr) {
        if (bArr == null) {
            return false;
        }
        try {
            OutputStream outputStream2 = this.outputStream;
            if (outputStream2 == null) {
                return false;
            }
            outputStream2.write(bArr);
            return true;
        } catch (IOException unused) {
            return false;
        }
    }

    public void setOnTonicDataListener(OnTonicDataListener onTonicDataListener) {
        this.tonicDataListener = onTonicDataListener;
    }

    public void queryLockStatus(OpenSerialPortListener openSerialPortListener, OnBicycleLockStateChangedListener onBicycleLockStateChangedListener) {
        if (this.serialPort == null) {
            openSerialPort(openSerialPortListener);
        }
        Thread thread = this.bicycleLockThread;
        if (thread != null) {
            thread.interrupt();
        }
        stopBicycleDataThread();
        BicycleLockThread bicycleLockThread2 = new BicycleLockThread(0, this, this.inputStream, onBicycleLockStateChangedListener);
        this.bicycleLockThread = bicycleLockThread2;
        bicycleLockThread2.start();
    }

    private void stopBicycleDataThread() {
        try {
            Thread thread = this.mReadThread;
            if (thread != null) {
                thread.interrupt();
                this.mReadThread = null;
            }
        } catch (Exception unused) {
        }
    }

    public static String formatHexString(byte[] bArr) {
        return formatHexString(bArr, false);
    }

    public static String formatHexString(byte[] bArr, boolean z) {
        if (bArr == null || bArr.length < 1) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (byte b : bArr) {
            String hexString = Integer.toHexString(b & 255);
            if (hexString.length() == 1) {
                hexString = '0' + hexString;
            }
            sb.append(hexString);
            if (z) {
                sb.append(" ");
            }
        }
        return sb.toString().trim();
    }

    public static SerialProtocol.FrameData readTonicData(InputStream inputStream2, Protocol.SerialData serialData) throws IOException {
        SerialProtocol.FrameData parseFrame;
        byte[] bArr = serialData.data;
        int i = serialData.offset;
        int read = i + inputStream2.read(bArr, i, 10);

        StringBuilder hexString = new StringBuilder();
        for (int l = 0; l < read; l++) {
            // Convert each byte to a hexadecimal string and append it to the StringBuilder
            String hex = String.format("%02X ", bArr[l]);
            hexString.append(hex);
        }
        Log.d("QZ", "readTonicData << " + i + " " + read + " " + hexString.toString().trim());

        if (read > 1 && !SerialProtocol.isValidHead(bArr[0])) {
            int i2 = 0;
            while (true) {
                if (i2 >= read) {
                    break;
                } else if (SerialProtocol.isValidHead(bArr[i2])) {
                    read -= i2;
                    System.arraycopy(bArr, i2, bArr, 0, read);
                    break;
                } else {
                    i2++;
                }
            }
        }

        hexString = new StringBuilder();
        for (int l = 0; l < read; l++) {
            // Convert each byte to a hexadecimal string and append it to the StringBuilder
            String hex = String.format("%02X ", bArr[l]);
            hexString.append(hex);
        }
        Log.d("QZ", "readTonicData2 << " + i + " " + read + " " + hexString.toString().trim());

        if (SerialProtocol.hasOneFrame(bArr, read) && (parseFrame = SerialProtocol.parseFrame(bArr)) != null) {
            int i3 = parseFrame.frameSize;
            read -= i3;
            System.arraycopy(bArr, i3, bArr, 0, read);

            hexString = new StringBuilder();
            for (int l = 0; l < read; l++) {
                // Convert each byte to a hexadecimal string and append it to the StringBuilder
                String hex = String.format("%02X ", bArr[l]);
                hexString.append(hex);
            }
            Log.d("QZ", "readTonicData3 << " + i + " " + read + " " + hexString.toString().trim());

            serialData.offset = read;
            if (parseFrame.hasValidData()) {
                return parseFrame;
            }
        }
        serialData.offset = read;
        return null;
    }
}
