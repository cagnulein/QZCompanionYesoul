package com.yesoulchina.support.serialport.thread;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import com.yesoulchina.support.serialport.SerialPortManager;
import com.yesoulchina.support.serialport.SerialPortUtils;
import com.yesoulchina.support.serialport.listener.OnBicycleLockStateChangedListener;
import java.io.InputStream;

public class BicycleLockThread extends Thread {
    public static final int BICYCLE_LOCK_CMD_CLOSE = 2;
    public static final int BICYCLE_LOCK_CMD_OPEN = 1;
    public static final int BICYCLE_LOCK_CMD_QUERY = 0;
    private static final String TAG = "智能锁";
    private int cmdType = 0;
    private Handler handler = new Handler(Looper.getMainLooper()) {
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
    /* access modifiers changed from: private */
    public OnBicycleLockStateChangedListener onBicycleLockStateChangedListener;
    private SerialPortUtils serialPortUtils;

    public BicycleLockThread(int i, SerialPortUtils serialPortUtils2, InputStream inputStream2, OnBicycleLockStateChangedListener onBicycleLockStateChangedListener2) {
        this.cmdType = i;
        this.onBicycleLockStateChangedListener = onBicycleLockStateChangedListener2;
        this.inputStream = inputStream2;
        this.serialPortUtils = serialPortUtils2;
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(6:23|24|25|26|27|(2:29|(3:37|(2:39|(1:(3:42|43|44)(1:(1:46)))(2:(3:48|49|50)(1:(1:52))|53))(1:(3:56|57|58)(2:61|(1:63)))|59))(1:64)) */
    /* JADX WARNING: Can't wrap try/catch for region: R(8:19|20|21|(6:23|24|25|26|27|(2:29|(3:37|(2:39|(1:(3:42|43|44)(1:(1:46)))(2:(3:48|49|50)(1:(1:52))|53))(1:(3:56|57|58)(2:61|(1:63)))|59))(1:64))(1:65)|66|67|74|71) */
    /* JADX WARNING: Code restructure failed: missing block: B:68:0x011f, code lost:
        java.lang.Thread.currentThread().interrupt();
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:25:0x005a */
    /* JADX WARNING: Missing exception handler attribute for start block: B:66:0x011a */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void run() {
        /*
            r15 = this;
            super.run()
            r0 = 512(0x200, float:7.175E-43)
            byte[] r0 = new byte[r0]
            com.yesoulchina.support.serialport.Protocol$SerialData r1 = new com.yesoulchina.support.serialport.Protocol$SerialData
            r1.<init>()
            r1.data = r0
            r0 = 0
            r1.offset = r0
            int r2 = r15.cmdType
            r3 = 2
            r4 = 1
            if (r2 != r4) goto L_0x0020
            byte[] r2 = com.yesoulchina.support.serialport.SerialProtocol.openBicycleLockCommand
            com.yesoulchina.support.serialport.SerialPortUtils r5 = r15.serialPortUtils
            boolean r2 = r5.sendBuffer(r2)
            goto L_0x002c
        L_0x0020:
            if (r2 != r3) goto L_0x002b
            byte[] r2 = com.yesoulchina.support.serialport.SerialProtocol.closeBicycleLockCommand
            com.yesoulchina.support.serialport.SerialPortUtils r5 = r15.serialPortUtils
            boolean r2 = r5.sendBuffer(r2)
            goto L_0x002c
        L_0x002b:
            r2 = 1
        L_0x002c:
            if (r2 == 0) goto L_0x0128
            int r2 = r15.cmdType
            if (r2 == r4) goto L_0x0034
            if (r2 != r3) goto L_0x0041
        L_0x0034:
            r5 = 50
            java.lang.Thread.sleep(r5)     // Catch:{ Exception -> 0x003a }
            goto L_0x0041
        L_0x003a:
            java.lang.Thread r2 = java.lang.Thread.currentThread()
            r2.interrupt()
        L_0x0041:
            byte[] r2 = com.yesoulchina.support.serialport.SerialProtocol.queryBicycleLockStatusCommand
            r5 = 1
        L_0x0044:
            if (r5 == 0) goto L_0x012b
            boolean r6 = r15.isInterrupted()
            if (r6 != 0) goto L_0x012b
            r6 = 30
            com.yesoulchina.support.serialport.SerialPortUtils r8 = r15.serialPortUtils     // Catch:{ Exception -> 0x011a }
            boolean r8 = r8.sendBuffer(r2)     // Catch:{ Exception -> 0x011a }
            if (r8 == 0) goto L_0x0115
            java.lang.Thread.sleep(r6)     // Catch:{ Exception -> 0x005a }
            goto L_0x0061
        L_0x005a:
            java.lang.Thread r8 = java.lang.Thread.currentThread()     // Catch:{ Exception -> 0x011a }
            r8.interrupt()     // Catch:{ Exception -> 0x011a }
        L_0x0061:
            java.io.InputStream r8 = r15.inputStream     // Catch:{ Exception -> 0x011a }
            int r8 = r8.available()     // Catch:{ Exception -> 0x011a }
            if (r8 <= 0) goto L_0x010f
            java.io.InputStream r8 = r15.inputStream     // Catch:{ Exception -> 0x011a }
            com.yesoulchina.support.serialport.SerialProtocol$FrameData r8 = com.yesoulchina.support.serialport.SerialPortUtils.readTonicData(r8, r1)     // Catch:{ Exception -> 0x011a }
            if (r8 == 0) goto L_0x011a
            int r9 = r8.type     // Catch:{ Exception -> 0x011a }
            r10 = 4
            if (r9 != r10) goto L_0x011a
            byte[] r8 = r8.data     // Catch:{ Exception -> 0x011a }
            if (r8 == 0) goto L_0x011a
            int r9 = r8.length     // Catch:{ Exception -> 0x011a }
            r11 = 3
            if (r9 < r11) goto L_0x011a
            byte r9 = r8[r0]     // Catch:{ Exception -> 0x011a }
            int r9 = com.yesoulchina.support.serialport.utils.ByteUtils.byte2Int(r9)     // Catch:{ Exception -> 0x011a }
            byte r12 = r8[r4]     // Catch:{ Exception -> 0x011a }
            int r12 = com.yesoulchina.support.serialport.utils.ByteUtils.byte2Int(r12)     // Catch:{ Exception -> 0x011a }
            byte r8 = r8[r3]     // Catch:{ Exception -> 0x011a }
            int r8 = com.yesoulchina.support.serialport.utils.ByteUtils.byte2Int(r8)     // Catch:{ Exception -> 0x011a }
            java.lang.StringBuilder r13 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x011a }
            r13.<init>()     // Catch:{ Exception -> 0x011a }
            java.lang.String r14 = "查询锁状态->D0:"
            r13.append(r14)     // Catch:{ Exception -> 0x011a }
            r13.append(r9)     // Catch:{ Exception -> 0x011a }
            java.lang.String r14 = ",D1:"
            r13.append(r14)     // Catch:{ Exception -> 0x011a }
            r13.append(r12)     // Catch:{ Exception -> 0x011a }
            java.lang.String r14 = ",D2: "
            r13.append(r14)     // Catch:{ Exception -> 0x011a }
            r13.append(r8)     // Catch:{ Exception -> 0x011a }
            java.lang.String r13 = r13.toString()     // Catch:{ Exception -> 0x011a }
            com.yesoulchina.support.serialport.SerialLog.m440d(r13)     // Catch:{ Exception -> 0x011a }
            r13 = 49
            r14 = 48
            if (r8 != r14) goto L_0x00f4
            java.lang.String r8 = "查询锁状态-> 电机无故障"
            com.yesoulchina.support.serialport.SerialLog.m440d(r8)     // Catch:{ Exception -> 0x011a }
            if (r12 != r14) goto L_0x00db
            if (r9 != r14) goto L_0x00cf
            java.lang.String r5 = "查询锁状态->已锁车"
            com.yesoulchina.support.serialport.SerialLog.m440d(r5)     // Catch:{ Exception -> 0x00ff }
            r15.onLockStateChanged(r11)     // Catch:{ Exception -> 0x00ff }
            goto L_0x00ff
        L_0x00cf:
            if (r9 != r13) goto L_0x00ff
            java.lang.String r5 = "查询锁状态->已开锁 "
            com.yesoulchina.support.serialport.SerialLog.m440d(r5)     // Catch:{ Exception -> 0x00ff }
            r15.onLockStateChanged(r10)     // Catch:{ Exception -> 0x00ff }
            goto L_0x00ff
        L_0x00db:
            if (r9 != r14) goto L_0x00e7
            java.lang.String r5 = "查询锁状态->锁车中"
            com.yesoulchina.support.serialport.SerialLog.m440d(r5)     // Catch:{ Exception -> 0x00f2 }
            r15.onLockStateChanged(r4)     // Catch:{ Exception -> 0x00f2 }
            goto L_0x00f2
        L_0x00e7:
            if (r9 != r13) goto L_0x00f2
            java.lang.String r5 = "查询锁状态->开锁中 "
            com.yesoulchina.support.serialport.SerialLog.m440d(r5)     // Catch:{ Exception -> 0x00f2 }
            r15.onLockStateChanged(r3)     // Catch:{ Exception -> 0x00f2 }
        L_0x00f2:
            r5 = 1
            goto L_0x011a
        L_0x00f4:
            if (r8 != r13) goto L_0x0101
            java.lang.String r8 = "查询锁状态-> 电机卡住 "
            com.yesoulchina.support.serialport.SerialLog.m440d(r8)     // Catch:{ Exception -> 0x011a }
            r15.onError(r11)     // Catch:{ Exception -> 0x011a }
        L_0x00ff:
            r5 = 0
            goto L_0x011a
        L_0x0101:
            r9 = 50
            if (r8 != r9) goto L_0x011a
            java.lang.String r8 = "查询锁状态-> 电机空转超时"
            com.yesoulchina.support.serialport.SerialLog.m440d(r8)     // Catch:{ Exception -> 0x011a }
            r15.onError(r3)     // Catch:{ Exception -> 0x011a }
            goto L_0x00ff
        L_0x010f:
            java.lang.String r8 = "inputStreamAvailable <= 0"
            com.yesoulchina.support.serialport.SerialLog.m440d(r8)     // Catch:{ Exception -> 0x011a }
            goto L_0x011a
        L_0x0115:
            java.lang.String r8 = "query cmd error "
            com.yesoulchina.support.serialport.SerialLog.m440d(r8)     // Catch:{ Exception -> 0x011a }
        L_0x011a:
            java.lang.Thread.sleep(r6)     // Catch:{ Exception -> 0x011f }
            goto L_0x0044
        L_0x011f:
            java.lang.Thread r6 = java.lang.Thread.currentThread()
            r6.interrupt()
            goto L_0x0044
        L_0x0128:
            r15.onError(r4)
        L_0x012b:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.yesoulchina.support.serialport.thread.BicycleLockThread.run():void");
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
