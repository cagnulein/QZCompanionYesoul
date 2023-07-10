package com.yesoulchina.support.serialport;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import com.yesoulchina.support.serialport.SerialPortUtils;
import com.yesoulchina.support.serialport.SerialProtocol;
import com.yesoulchina.support.serialport.listener.OpenSerialPortListener;
import com.yesoulchina.support.serialport.utils.HexUtil;
import java.util.Arrays;

public class SerialPortManager {
    private static final int MSG_WHAT_MOCK_DATA = 0;
    private static volatile SerialPortManager manager;
    private int currentBicycleLockState;
    /* access modifiers changed from: private */
    public String deviceSn;
    private boolean isPause;
    private boolean isSerialAvaiable;
    private boolean isWoman;
    private long lastReportTime;
    private Handler mainHadler;
    /* access modifiers changed from: private */
    public SerialPortUtils.OnTonicDataListener onTonicDataListener;
    /* access modifiers changed from: private */
    public int power;
    /* access modifiers changed from: private */
    public int resist;
    /* access modifiers changed from: private */
    public int roundPerMin;
    private double speedPerHourKM;
    private double totalCalorie;
    private double totalDistance;
    private double userWeight;

    public void destroy() {
    }

    public void finishTrain() {
    }

    public void init() {
    }

    /* access modifiers changed from: package-private */
    public void startMockData() {
    }

    public void startTrainForFloat() {
    }

    public static SerialPortManager getManager() {
        if (manager == null) {
            synchronized (SerialPortManager.class) {
                if (manager == null) {
                    manager = new SerialPortManager();
                }
            }
        }
        return manager;
    }

    public String getDeviceSn() {
        return this.deviceSn;
    }

    private SerialPortManager() {
        this.power = 0;
        this.resist = 0;
        this.roundPerMin = 0;
        this.speedPerHourKM = 0.0d;
        this.totalCalorie = 0.0d;
        this.totalDistance = 0.0d;
        this.lastReportTime = 0;
        this.isWoman = false;
        this.userWeight = 60.0d;
        this.isPause = false;
        this.currentBicycleLockState = 0;
        this.deviceSn = "";
        this.isSerialAvaiable = false;
        this.mainHadler = new Handler(Looper.getMainLooper()) {
            public void handleMessage(Message message) {
                super.handleMessage(message);
                if (message != null) {
                    int i = message.what;
                }
            }
        };
        this.onTonicDataListener = new SerialPortUtils.OnTonicDataListener() {
            public void onDataReceived(SerialProtocol.FrameData frameData) {
                if (frameData.type == 2) {
                    int unused = SerialPortManager.this.power = frameData.value / 10;
                } else if (frameData.type == 3) {
                    int unused2 = SerialPortManager.this.resist = frameData.value;
                } else if (frameData.type == 1) {
                    int unused3 = SerialPortManager.this.roundPerMin = frameData.value;
                    SerialPortManager.this.tonicCalcDistance(frameData.value);
                } else if (frameData.type == 7) {
                    try {
                        String unused4 = SerialPortManager.this.deviceSn = HexUtil.hexStringToString(HexUtil.encodeHexStr(Arrays.copyOf(frameData.data, frameData.dataLen)));
                    } catch (Exception unused5) {
                    }
                }
            }
        };
        this.isSerialAvaiable = SerialPortUtils.getManager().openSerialPort(new OpenSerialPortListener() {
            public void onOpenSerialPortFail(Exception exc) {
            }

            public void onOpenSerialPortSuccess() {
                SerialPortUtils.getManager().setOnTonicDataListener(SerialPortManager.this.onTonicDataListener);
                SerialPortUtils.getManager().startTrain();
            }
        });
    }

    /* access modifiers changed from: private */
    public void tonicCalcDistance(int i) {
        double d;
        double d2;
        int i2 = i;
        long elapsedRealtime = SystemClock.elapsedRealtime();
        if (i2 == 0) {
            this.lastReportTime = elapsedRealtime;
            this.speedPerHourKM = 0.0d;
            this.roundPerMin = 0;
            return;
        }
        long j = this.lastReportTime;
        if (j == 0) {
            this.lastReportTime = elapsedRealtime;
            return;
        }
        long j2 = elapsedRealtime - j;
        this.lastReportTime = elapsedRealtime;
        if (!this.isPause) {
            int i3 = this.power;
            if (i3 <= 0 || i3 > 1000) {
                d2 = 0.0d;
                d = 0.0d;
            } else {
                if (this.isWoman) {
                    d2 = (((((((double) i3) * 0.001d) + 0.024d) * this.userWeight) * ((double) j2)) / 60.0d) / 1000.0d;
                } else {
                    d2 = (((((((double) i3) * 0.001d) + 0.028d) * this.userWeight) * ((double) j2)) / 60.0d) / 1000.0d;
                }
                d = ((((((((double) i2) * 262.8d) * 2.0d) * 3.141592653589793d) * 0.23d) * ((double) j2)) / 3600.0d) / 1000.0d;
            }
            if (d2 > 0.0d) {
                this.totalCalorie += d2;
            }
            if (d > 0.0d) {
                this.totalDistance += d;
            }
        }
        this.speedPerHourKM = ((((((double) i2) * 262.8d) * 2.0d) * 3.141592653589793d) * 0.23d) / 1000.0d;
    }

    public int getPower() {
        return this.power;
    }

    public int getResist() {
        return this.resist;
    }

    public int getRoundPerMin() {
        return this.roundPerMin;
    }

    public double getTotalCalorie() {
        return this.totalCalorie;
    }

    public double getTotalDistance() {
        return this.totalDistance;
    }

    public double getSpeedPerHourKM() {
        return this.speedPerHourKM;
    }

    public void startTrain(OpenSerialPortListener openSerialPortListener, boolean z, double d) {
        this.isWoman = z;
        if (d <= 0.0d || d >= 200.0d) {
            this.userWeight = 60.0d;
        } else {
            this.userWeight = d;
        }
        this.resist = 0;
        this.roundPerMin = 0;
        this.speedPerHourKM = 0.0d;
        this.totalCalorie = 0.0d;
        this.totalDistance = 0.0d;
        this.lastReportTime = 0;
        this.isPause = false;
    }

    public void pauseTrain() {
        this.isPause = true;
    }

    public void resumeTrain() {
        this.isPause = false;
        SerialPortUtils.getManager().resumeTrain();
    }

    public void mockData(int i, int i2, int i3) {
        this.power = i;
        this.roundPerMin = i2;
        this.resist = i3;
        tonicCalcDistance(i2);
    }

    public int getCurrentBicycleLockState() {
        return this.currentBicycleLockState;
    }

    public void setCurrentBicycleLockState(int i) {
        this.currentBicycleLockState = i;
    }
}
