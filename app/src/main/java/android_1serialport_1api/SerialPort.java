package android_1serialport_1api;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class SerialPort {
    public static final boolean BLOCK_MODE = true;
    private static final int EVENT_VERIFY = 2;
    private static final int NO_VERIFY = 1;
    private static final int ODD_VERIFY = 3;
    private static final String TAG = "SerialPort";
    private FileDescriptor mFd;
    private FileInputStream mFileInputStream;
    private FileOutputStream mFileOutputStream;

    private native FileDescriptor open(String str, int i, int i2, int i3, int i4, int i5);

    public native int clearBuf();

    public native int close();

    public SerialPort(File file, int i) throws SecurityException, IOException {
        FileDescriptor open = open(file.getAbsolutePath(), i, 8, 1, 1, 0);
        this.mFd = open;
        if (open != null) {
            this.mFileInputStream = new FileInputStream(this.mFd);
            this.mFileOutputStream = new FileOutputStream(this.mFd);
            return;
        }
        throw new IOException();
    }

    public InputStream getInputStream() {
        return this.mFileInputStream;
    }

    public OutputStream getOutputStream() {
        return this.mFileOutputStream;
    }

    static {
        System.loadLibrary("serialport");
    }
}
