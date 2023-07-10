package com.yesoulchina.support.serialport.listener;

public interface OpenSerialPortListener {
    void onOpenSerialPortFail(Exception exc);

    void onOpenSerialPortSuccess();
}
