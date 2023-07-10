package com.yesoulchina.support.serialport.thread;

import android.os.SystemClock;
import com.yesoulchina.support.serialport.SerialPortUtils;
import java.io.InputStream;

public class BicycleDataThread extends Thread {
    public static final int READ_INTERVAL = 200;
    private InputStream inputStream;
    private long lastReadSnTime = SystemClock.elapsedRealtime();
    private int readSnCount = 0;
    private SerialPortUtils serialPortUtils;
    private SerialPortUtils.OnTonicDataListener tonicDataListener;

    public BicycleDataThread(SerialPortUtils serialPortUtils2, InputStream inputStream2, SerialPortUtils.OnTonicDataListener onTonicDataListener) {
        this.inputStream = inputStream2;
        this.serialPortUtils = serialPortUtils2;
        this.tonicDataListener = onTonicDataListener;
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(13:3|4|(1:10)|11|(5:13|(1:18)(1:17)|19|20|21)|24|25|26|27|28|35|31|1) */
    /* JADX WARNING: Missing exception handler attribute for start block: B:27:0x006d */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void run() {
        /*
            r12 = this;
            super.run()
            r0 = 512(0x200, float:7.175E-43)
            byte[] r0 = new byte[r0]
            com.yesoulchina.support.serialport.Protocol$SerialData r1 = new com.yesoulchina.support.serialport.Protocol$SerialData
            r1.<init>()
            r1.data = r0
            r0 = 0
            r1.offset = r0
            com.yesoulchina.support.serialport.SerialProtocol$CyclicGetData r0 = new com.yesoulchina.support.serialport.SerialProtocol$CyclicGetData
            r0.<init>()
            r2 = 0
        L_0x0018:
            boolean r4 = r12.isInterrupted()
            if (r4 != 0) goto L_0x0077
            java.io.InputStream r4 = r12.inputStream     // Catch:{ Exception -> 0x0075 }
            int r4 = r4.available()     // Catch:{ Exception -> 0x0075 }
            if (r4 <= 0) goto L_0x0035
            java.io.InputStream r4 = r12.inputStream     // Catch:{ Exception -> 0x0075 }
            com.yesoulchina.support.serialport.SerialProtocol$FrameData r4 = com.yesoulchina.support.serialport.SerialPortUtils.readTonicData(r4, r1)     // Catch:{ Exception -> 0x0075 }
            if (r4 == 0) goto L_0x0035
            com.yesoulchina.support.serialport.SerialPortUtils$OnTonicDataListener r5 = r12.tonicDataListener     // Catch:{ Exception -> 0x0075 }
            if (r5 == 0) goto L_0x0035
            r5.onDataReceived(r4)     // Catch:{ Exception -> 0x0075 }
        L_0x0035:
            long r4 = android.os.SystemClock.elapsedRealtime()     // Catch:{ Exception -> 0x0075 }
            long r6 = r4 - r2
            r8 = 200(0xc8, double:9.9E-322)
            int r10 = (r6 > r8 ? 1 : (r6 == r8 ? 0 : -1))
            if (r10 <= 0) goto L_0x0067
            int r6 = r12.readSnCount     // Catch:{ Exception -> 0x0075 }
            r7 = 4
            if (r6 >= r7) goto L_0x005a
            long r7 = r12.lastReadSnTime     // Catch:{ Exception -> 0x0075 }
            long r7 = r4 - r7
            r9 = 50000(0xc350, double:2.47033E-319)
            int r11 = (r7 > r9 ? 1 : (r7 == r9 ? 0 : -1))
            if (r11 <= 0) goto L_0x005a
            int r6 = r6 + 1
            r12.readSnCount = r6     // Catch:{ Exception -> 0x0075 }
            r12.lastReadSnTime = r4     // Catch:{ Exception -> 0x0075 }
            byte[] r2 = com.yesoulchina.support.serialport.SerialProtocol.readSnCmd     // Catch:{ Exception -> 0x0075 }
            goto L_0x005e
        L_0x005a:
            byte[] r2 = r0.getDataCmd()     // Catch:{ Exception -> 0x0075 }
        L_0x005e:
            com.yesoulchina.support.serialport.SerialPortUtils r3 = r12.serialPortUtils     // Catch:{ Exception -> 0x0065 }
            r3.sendBuffer(r2)     // Catch:{ Exception -> 0x0065 }
            r2 = r4
            goto L_0x0067
        L_0x0065:
            r2 = r4
            goto L_0x0018
        L_0x0067:
            r4 = 30
            java.lang.Thread.sleep(r4)     // Catch:{ InterruptedException -> 0x006d }
            goto L_0x0018
        L_0x006d:
            java.lang.Thread r4 = java.lang.Thread.currentThread()     // Catch:{ Exception -> 0x0075 }
            r4.interrupt()     // Catch:{ Exception -> 0x0075 }
            goto L_0x0018
        L_0x0075:
            goto L_0x0018
        L_0x0077:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.yesoulchina.support.serialport.thread.BicycleDataThread.run():void");
    }
}
