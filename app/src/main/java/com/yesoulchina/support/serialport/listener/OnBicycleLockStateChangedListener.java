package com.yesoulchina.support.serialport.listener;

public interface OnBicycleLockStateChangedListener {
    void onBicycleLockHardwareError(int i);

    void onBicycleLockStateChanged(int i);
}
