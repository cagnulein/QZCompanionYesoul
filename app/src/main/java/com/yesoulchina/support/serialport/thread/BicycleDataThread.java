package com.yesoulchina.support.serialport.thread;

import android.os.SystemClock;
import android.util.Log;

import com.yesoulchina.support.serialport.Protocol;
import com.yesoulchina.support.serialport.SerialPortUtils;
import com.yesoulchina.support.serialport.SerialProtocol;
import java.io.InputStream;

/* loaded from: classes2.dex */
public class BicycleDataThread extends Thread {
    public static final int READ_INTERVAL = 200;
    private InputStream inputStream;
    private SerialPortUtils serialPortUtils;
    private SerialPortUtils.OnTonicDataListener tonicDataListener;
    private int readSnCount = 0;
    private long lastReadSnTime = SystemClock.elapsedRealtime();

    public BicycleDataThread(SerialPortUtils serialPortUtils, InputStream inputStream, SerialPortUtils.OnTonicDataListener onTonicDataListener) {
        this.inputStream = inputStream;
        this.serialPortUtils = serialPortUtils;
        this.tonicDataListener = onTonicDataListener;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        byte[] dataCmd;
        SerialProtocol.FrameData readTonicData;
        SerialPortUtils.OnTonicDataListener onTonicDataListener;
        super.run();
        Protocol.SerialData serialData = new Protocol.SerialData();
        serialData.data = new byte[512];
        serialData.offset = 0;
        SerialProtocol.CyclicGetData cyclicGetData = new SerialProtocol.CyclicGetData();
        Log.d("QZ", "BicycleDataThread start");
        long j = 0;
        while (!isInterrupted()) {
            try {
                if(this.inputStream.available() > 0)  {
                    Log.d("QZ", "<< " + this.inputStream.available() + " bytes!");
                }
                if (this.inputStream.available() > 0 && (readTonicData = SerialPortUtils.readTonicData(this.inputStream, serialData)) != null && (onTonicDataListener = this.tonicDataListener) != null) {
                    Log.d("QZ", readTonicData.toString());
                    onTonicDataListener.onDataReceived(readTonicData);
                }
                long elapsedRealtime = SystemClock.elapsedRealtime();
                if (elapsedRealtime - j > 200) {
                    int i = this.readSnCount;
                    if (i < 4 && elapsedRealtime - this.lastReadSnTime > 50000) {
                        this.readSnCount = i + 1;
                        this.lastReadSnTime = elapsedRealtime;
                        dataCmd = SerialProtocol.readSnCmd;
                    } else {
                        dataCmd = cyclicGetData.getDataCmd();
                    }
                    try {
                        this.serialPortUtils.sendBuffer(dataCmd);
                        Log.d("QZ", ">>" + dataCmd);
                        j = elapsedRealtime;
                    } catch (Exception unused) {
                        j = elapsedRealtime;
                    }
                }
                try {
                    Thread.sleep(30L);
                } catch (InterruptedException unused2) {
                    Thread.currentThread().interrupt();
                }
            } catch (Exception unused3) {
            }
        }
    }
}
