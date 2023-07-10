package com.yesoulchina.support.serialport;

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class Protocol {
    public static byte ACK_REPORT = 30;
    public static final int CHECKSUM_LEN = 1;
    public static byte CMD_HANDSHAKE = 5;
    public static byte HANDSHAKE_ACK = Ascii.DC4;
    public static byte[] HEAD = {-76, 75};
    public static byte REPORT_DATA = 25;
    public static byte[] RESERVED = {1, 1, 1, 1, 1, 1, 1, 1};
    public static byte RSP_HANDSHAKE = 10;
    public static byte SENSOR_TO_APP_HANDSHAKE = 15;
    public static byte[] TAIL = {-16, -16};

    public static class BikeRunStatus {
        public int oneCycleInMills;
        public int powerConsume;
        public int resist;
    }

    public static class FrameOutPut {
        public byte[] realData;
        public int totalLen;
    }

    public static class SensorErrCode {
        private static final int ERR_NONE = 0;
    }

    public static class SerialData {
        public byte[] data;
        public int offset;
    }

    public static class SerialRecvData {
        public byte[] ack;
        public int cmd;
        public int errCode;
        public BikeRunStatus status;
    }

    public static byte[] getHandshake() {
        return getFrame(CMD_HANDSHAKE, (byte[]) null);
    }

    public static byte[] getHandshakeAck() {
        return getFrame(HANDSHAKE_ACK, (byte[]) null);
    }

    public static byte[] getAckFrame(byte[] bArr) {
        return getFrame(ACK_REPORT, bArr);
    }

    public static byte[] getFrame(byte b, byte[] bArr) {
        int i;
        ByteBuffer allocate = ByteBuffer.allocate(100);
        allocate.put(HEAD);
        if (bArr == null || bArr.length == 0) {
            i = RESERVED.length + 1;
        } else {
            i = bArr.length + 1 + RESERVED.length;
        }
        byte b2 = (byte) i;
        allocate.put(b2);
        allocate.put(b);
        if (bArr != null && bArr.length > 0) {
            allocate.put(bArr);
        }
        allocate.put(RESERVED);
        allocate.put(calcChecksum(b2, b, bArr, RESERVED));
        allocate.put(TAIL);
        byte[] bArr2 = new byte[allocate.position()];
        allocate.rewind();
        allocate.get(bArr2);
        return bArr2;
    }

    private static byte[] calcChecksum(byte b, byte b2, byte[] bArr, byte[] bArr2) {
        byte[] bArr3 = {(byte) (b ^ b2)};
        for (byte b3 : bArr) {
            bArr3[0] = (byte) (bArr3[0] ^ b3);
        }
        for (int i = 0; i < RESERVED.length; i++) {
            bArr3[0] = (byte) (bArr3[0] ^ bArr2[i]);
        }
        return bArr3;
    }

    private static byte[] calcChecksum(byte b, byte[] bArr) {
        byte[] bArr2 = {(byte) (b ^ ((~b) & 255))};
        for (byte b2 : bArr) {
            bArr2[0] = (byte) (bArr2[0] ^ b2);
        }
        return bArr2;
    }

    public static SerialRecvData parseFrame(byte[] bArr) {
        if (!(bArr == null || bArr.length == 0)) {
            SerialRecvData serialRecvData = new SerialRecvData();
            serialRecvData.cmd = bArr[0];
            if (bArr[0] == RSP_HANDSHAKE) {
                return serialRecvData;
            }
            if (bArr[0] == SENSOR_TO_APP_HANDSHAKE) {
                serialRecvData.ack = getHandshakeAck();
                return serialRecvData;
            } else if (bArr[0] == REPORT_DATA) {
                byte b = bArr[1];
                if (b == 0) {
                    int length = bArr.length - 2;
                    byte[] bArr2 = new byte[length];
                    System.arraycopy(bArr, 2, bArr2, 0, length);
                    serialRecvData.status = paresReportFrame(bArr2);
                    serialRecvData.ack = getAckFrame(bArr2);
                    return serialRecvData;
                }
                serialRecvData.errCode = b;
                return serialRecvData;
            }
        }
        return null;
    }

    public static BikeRunStatus paresReportFrame(byte[] bArr) {
        BikeRunStatus bikeRunStatus = new BikeRunStatus();
        bikeRunStatus.oneCycleInMills = (((bArr[0] & 255) << 8) + (bArr[1] & 255)) * 10;
        bikeRunStatus.resist = ((((bArr[2] & 255) << 8) + (bArr[3] & 255)) * 100) /*/ AnalyticsListener.EVENT_DROPPED_VIDEO_FRAMES*/;
        bikeRunStatus.powerConsume = ((bArr[4] & 255) << 8) + (bArr[5] & 255);
        return bikeRunStatus;
    }

    public static boolean readByte(SerialData serialData, byte[] bArr) {
        int length = bArr.length;
        int i = serialData.offset;
        int i2 = i + length;
        if (i2 > serialData.data.length) {
            return false;
        }
        for (int i3 = i; i3 < i2; i3++) {
            bArr[i3 - i] = serialData.data[i3];
        }
        serialData.offset += length;
        return true;
    }

    public static FrameOutPut parseSerialData(byte[] bArr, int i) {
        try {
            SerialData serialData = new SerialData();
            byte[] bArr2 = new byte[i];
            System.arraycopy(bArr, 0, bArr2, 0, i);
            serialData.data = bArr2;
            serialData.offset = 0;
            byte[] bArr3 = new byte[HEAD.length];
            if (!readByte(serialData, bArr3) || !bytesEqual(bArr3, HEAD)) {
                return null;
            }
            byte[] bArr4 = new byte[2];
            if (!readByte(serialData, bArr4)) {
                return null;
            }
            int i2 = bArr4[0] & 255;
            if ((bArr4[1] & 255) + i2 != 255) {
                return null;
            }
            byte[] bArr5 = new byte[i2];
            if (!readByte(serialData, bArr5)) {
                return null;
            }
            byte[] bArr6 = new byte[1];
            if (!readByte(serialData, bArr6) || !bytesEqual(bArr6, calcChecksum((byte) i2, bArr5)) || !readByte(serialData, new byte[TAIL.length])) {
                return null;
            }
            FrameOutPut frameOutPut = new FrameOutPut();
            frameOutPut.realData = bArr5;
            frameOutPut.totalLen = i2 + HEAD.length + 2 + 1 + TAIL.length;
            return frameOutPut;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static byte[] readFrame(InputStream inputStream) {
        int read;
        try {
            byte[] bArr = new byte[HEAD.length];
            Log.i("Protocol", "head " + Arrays.toString(bArr));
            if (!read(inputStream, bArr) || !bytesEqual(bArr, HEAD) || (read = inputStream.read()) < 0 || inputStream.read() + read != 255) {
                return null;
            }
            byte[] bArr2 = new byte[read];
            if (!read(inputStream, bArr2)) {
                return null;
            }
            byte[] bArr3 = new byte[1];
            if (read(inputStream, bArr3) && bytesEqual(bArr3, calcChecksum((byte) read, bArr2)) && read(inputStream, new byte[TAIL.length])) {
                return bArr2;
            }
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static boolean read(InputStream inputStream, byte[] bArr) throws IOException {
        int length = bArr.length;
        int i = 0;
        do {
            int read = inputStream.read(bArr, i, length);
            if (read < 0) {
                return false;
            }
            i += read;
            length -= read;
        } while (length > 0);
        return true;
    }

    public static boolean bytesEqual(byte[] bArr, byte[] bArr2) {
        if (bArr == null && bArr2 == null) {
            return true;
        }
        if (bArr == null || bArr2 == null || bArr.length != bArr2.length) {
            return false;
        }
        for (int i = 0; i < bArr.length; i++) {
            if (bArr[i] != bArr2[i]) {
                return false;
            }
        }
        return true;
    }
}
