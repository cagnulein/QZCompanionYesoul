package com.yesoulchina.support.serialport.thread;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import com.yesoulchina.support.serialport.Protocol;
import com.yesoulchina.support.serialport.SerialLog;
import com.yesoulchina.support.serialport.SerialPortUtils;
import com.yesoulchina.support.serialport.SerialProtocol;
import com.yesoulchina.support.serialport.listener.OnBicycleReadSnListener;
import com.yesoulchina.support.serialport.utils.HexUtil;
import java.io.InputStream;
import java.util.Arrays;

/* loaded from: classes2.dex */
public class BicycleSNThread extends Thread {
    private static final String TAG = "SN";
    private Handler handler = new Handler(Looper.getMainLooper()) { // from class: com.yesoulchina.support.serialport.thread.BicycleSNThread.1
        @Override // android.os.Handler
        public void handleMessage(Message message) {
            super.handleMessage(message);
            if (message != null) {
                int i = message.what;
                if (i == 0) {
                    int i2 = message.arg1;
                    if (BicycleSNThread.this.onBicycleReadSnListener != null) {
                        BicycleSNThread.this.onBicycleReadSnListener.onError(i2);
                    }
                } else if (i != 1) {
                    return;
                }
                Object obj = message.obj;
                if (BicycleSNThread.this.onBicycleReadSnListener == null || !(obj instanceof String)) {
                    return;
                }
                BicycleSNThread.this.onBicycleReadSnListener.onSuccess(obj.toString());
            }
        }
    };
    private InputStream inputStream;
    private OnBicycleReadSnListener onBicycleReadSnListener;
    private SerialPortUtils serialPortUtils;

    public BicycleSNThread(SerialPortUtils serialPortUtils, InputStream inputStream, OnBicycleReadSnListener onBicycleReadSnListener) {
        this.onBicycleReadSnListener = onBicycleReadSnListener;
        this.inputStream = inputStream;
        this.serialPortUtils = serialPortUtils;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        super.run();
        Protocol.SerialData serialData = new Protocol.SerialData();
        serialData.data = new byte[512];
        serialData.offset = 10;
        int i = 0;
        byte[] bArr = {SerialProtocol.REQUEST_START_CODE, 87, 76, SerialProtocol.DATA_END_CODE};
        if (this.serialPortUtils.sendBuffer(bArr)) {
            while (!isInterrupted()) {
                try {
                    Thread.sleep(50L);
                } catch (Exception unused) {
                    Thread.currentThread().interrupt();
                }
                try {
                    if (this.inputStream.available() > 0) {
                        SerialProtocol.FrameData readTonicData = SerialPortUtils.readTonicData(this.inputStream, serialData);
                        if (readTonicData != null && readTonicData.type == 7) {
                            try {
                                String hexStringToString = HexUtil.hexStringToString(HexUtil.encodeHexStr(Arrays.copyOf(readTonicData.data, readTonicData.dataLen)));
                                SerialLog.m67d("硬件sn -->" + hexStringToString);
                                onReadSnSuccess(hexStringToString);
                                Thread.currentThread().interrupt();
                            } catch (Exception unused2) {
                            }
                        } else {
                            SerialLog.m67d("硬件sn frameData ==null");
                        }
                    } else {
                        if (i > 4) {
                            Thread.currentThread().interrupt();
                        }
                        i++;
                        SerialLog.m67d("硬件sn inputStreamAvailable <= 0");
                    }
                    this.serialPortUtils.sendBuffer(bArr);
                } catch (Exception e) {
                    SerialLog.m67d("硬件sn 原始--> 读取失败--" + e.getLocalizedMessage());
                }
            }
            return;
        }
        onError(1);
    }

    private void onReadSnSuccess(String str) {
        Message message = new Message();
        message.what = 1;
        message.obj = str;
        this.handler.sendMessage(message);
    }

    private void onError(int i) {
        Message message = new Message();
        message.what = 0;
        message.arg1 = i;
        this.handler.sendMessage(message);
    }
}
