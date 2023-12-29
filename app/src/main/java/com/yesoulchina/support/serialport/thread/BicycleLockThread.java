package com.yesoulchina.support.serialport.thread;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import com.yesoulchina.support.serialport.Protocol;
import com.yesoulchina.support.serialport.SerialLog;
import com.yesoulchina.support.serialport.SerialPortManager;
import com.yesoulchina.support.serialport.SerialPortUtils;
import com.yesoulchina.support.serialport.SerialProtocol;
import com.yesoulchina.support.serialport.listener.OnBicycleLockStateChangedListener;
import com.yesoulchina.support.serialport.utils.ByteUtils;
import java.io.InputStream;

/* loaded from: classes2.dex */
public class BicycleLockThread extends Thread {
    public static final int BICYCLE_LOCK_CMD_CLOSE = 2;
    public static final int BICYCLE_LOCK_CMD_OPEN = 1;
    public static final int BICYCLE_LOCK_CMD_QUERY = 0;
    private static final String TAG = "智能锁";
    private int cmdType;
    private Handler handler = new Handler(Looper.getMainLooper()) { // from class: com.yesoulchina.support.serialport.thread.BicycleLockThread.1
        @Override // android.os.Handler
        public void handleMessage(Message message) {
            super.handleMessage(message);
            if (message != null) {
                int i = message.what;
                if (i == 0) {
                    int i2 = message.arg1;
                    if (BicycleLockThread.this.onBicycleLockStateChangedListener != null) {
                        BicycleLockThread.this.onBicycleLockStateChangedListener.onBicycleLockHardwareError(i2);
                    }
                } else if (i != 1) {
                    return;
                }
                int i3 = message.arg1;
                if (BicycleLockThread.this.onBicycleLockStateChangedListener != null) {
                    BicycleLockThread.this.onBicycleLockStateChangedListener.onBicycleLockStateChanged(i3);
                }
            }
        }
    };
    private InputStream inputStream;
    private OnBicycleLockStateChangedListener onBicycleLockStateChangedListener;
    private SerialPortUtils serialPortUtils;

    public BicycleLockThread(int i, SerialPortUtils serialPortUtils, InputStream inputStream, OnBicycleLockStateChangedListener onBicycleLockStateChangedListener) {
        this.cmdType = 0;
        this.cmdType = i;
        this.onBicycleLockStateChangedListener = onBicycleLockStateChangedListener;
        this.inputStream = inputStream;
        this.serialPortUtils = serialPortUtils;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        boolean sendBuffer;
        byte[] bArr;
        super.run();
        Protocol.SerialData serialData = new Protocol.SerialData();
        serialData.data = new byte[512];
        serialData.offset = 0;
        int i = this.cmdType;
        if (i == 1) {
            sendBuffer = this.serialPortUtils.sendBuffer(SerialProtocol.openBicycleLockCommand);
        } else {
            sendBuffer = i == 2 ? this.serialPortUtils.sendBuffer(SerialProtocol.closeBicycleLockCommand) : true;
        }
        if (sendBuffer) {
            int i2 = this.cmdType;
            if (i2 == 1 || i2 == 2) {
                try {
                    Thread.sleep(50L);
                } catch (Exception unused) {
                    Thread.currentThread().interrupt();
                }
            }
            byte[] bArr2 = SerialProtocol.queryBicycleLockStatusCommand;
            boolean z = true;
            while (z && !isInterrupted()) {
                try {
                    if (this.serialPortUtils.sendBuffer(bArr2)) {
                        try {
                            Thread.sleep(30L);
                        } catch (Exception unused2) {
                            Thread.currentThread().interrupt();
                        }
                        if (this.inputStream.available() > 0) {
                            SerialProtocol.FrameData readTonicData = SerialPortUtils.readTonicData(this.inputStream, serialData);
                            if (readTonicData != null && readTonicData.type == 4 && (bArr = readTonicData.data) != null && bArr.length >= 3) {
                                int byte2Int = ByteUtils.byte2Int(bArr[0]);
                                int byte2Int2 = ByteUtils.byte2Int(bArr[1]);
                                int byte2Int3 = ByteUtils.byte2Int(bArr[2]);
                                SerialLog.m67d("查询锁状态->D0:" + byte2Int + ",D1:" + byte2Int2 + ",D2: " + byte2Int3);
                                if (byte2Int3 == 48) {
                                    SerialLog.m67d("查询锁状态-> 电机无故障");
                                    if (byte2Int2 == 48) {
                                        if (byte2Int == 48) {
                                            try {
                                                SerialLog.m67d("查询锁状态->已锁车");
                                                onLockStateChanged(3);
                                            } catch (Exception unused3) {
                                            }
                                        } else if (byte2Int == 49) {
                                            SerialLog.m67d("查询锁状态->已开锁 ");
                                            onLockStateChanged(4);
                                        }
                                        z = false;
                                    } else {
                                        if (byte2Int == 48) {
                                            try {
                                                SerialLog.m67d("查询锁状态->锁车中");
                                                onLockStateChanged(1);
                                            } catch (Exception unused4) {
                                            }
                                        } else if (byte2Int == 49) {
                                            SerialLog.m67d("查询锁状态->开锁中 ");
                                            onLockStateChanged(2);
                                        }
                                        z = true;
                                    }
                                } else {
                                    if (byte2Int3 == 49) {
                                        SerialLog.m67d("查询锁状态-> 电机卡住 ");
                                        onError(3);
                                    } else if (byte2Int3 == 50) {
                                        SerialLog.m67d("查询锁状态-> 电机空转超时");
                                        onError(2);
                                    }
                                    z = false;
                                }
                            }
                        } else {
                            SerialLog.m67d("inputStreamAvailable <= 0");
                        }
                    } else {
                        SerialLog.m67d("query cmd error ");
                    }
                } catch (Exception unused5) {
                }
                try {
                    Thread.sleep(30L);
                } catch (Exception unused6) {
                    Thread.currentThread().interrupt();
                }
            }
            return;
        }
        onError(1);
    }

    private void onLockStateChanged(int i) {
        SerialPortManager.getManager().setCurrentBicycleLockState(i);
        Message message = new Message();
        message.what = 1;
        message.arg1 = i;
        this.handler.sendMessage(message);
    }

    private void onError(int i) {
        Message message = new Message();
        message.what = 0;
        message.arg1 = i;
        this.handler.sendMessage(message);
    }
}
