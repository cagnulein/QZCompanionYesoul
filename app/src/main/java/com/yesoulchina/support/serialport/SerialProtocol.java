package com.yesoulchina.support.serialport;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.util.Arrays;

public class SerialProtocol {
    public static int BAUD_RATE = 19200;
    public static int DATA_BITS = 8;
    public static byte DATA_END_CODE = -10;
    public static int FLOW_CONTROL = 0;
    public static int PARITY_BIT = 0;
    public static byte REQUEST_FOR_CLOSE_BICYCLE_LOCK = 82;
    public static byte REQUEST_FOR_INFO = 0;
    public static byte REQUEST_FOR_INSTANTANEOUS_POWER = 68;
    public static byte REQUEST_FOR_OPEN_BICYCLE_LOCK = 84;
    public static byte REQUEST_FOR_QUERY_BICYCLE_LOCK_STATUS = 80;
    public static byte REQUEST_FOR_READ_SN = 87;
    public static byte REQUEST_FOR_RESISTENCE = 73;
    public static byte REQUEST_FOR_RPM = 65;
    public static byte REQUEST_FOR_SHAKEHAND_CRC = -30;
    public static byte REQUEST_FOR_SHAKEHAND_D0 = 69;
    public static byte REQUEST_FOR_SHAKEHAND_D1 = 83;
    public static byte REQUEST_FOR_SHAKEHAND_D2 = 48;
    public static byte REQUEST_FOR_SHAKEHAND_D3 = 50;
    public static byte REQUEST_INFO_START_CODE = -17;
    public static byte REQUEST_SHAKEHAND_START_CODE = -24;
    public static byte REQUEST_START_CODE = -11;
    public static byte RESPONSE_START_CODE = -15;
    public static byte RSP_NAK_CODE = -13;
    public static int STOP_BITS = 1;
    private static final String TAG = "SerialProtocol";
    public static byte[] closeBicycleLockCommand;
    private static byte closeBicycleLockCommandCheckSum;
    private static byte[] deviceInfoCommand = {-17, 0, -17, -10};
    /* access modifiers changed from: private */
    public static byte[] handShakeCommand = {-24, 69, 83, 48, 50, -30, -10};
    public static byte[] openBicycleLockCommand;
    private static byte openBicycleLockCommandCheckSum;
    /* access modifiers changed from: private */
    public static byte[] powerCommand;
    private static byte powerCommandCheckSum;
    public static byte[] queryBicycleLockStatusCommand;
    private static byte queryBicycleLockStatusCommandCheckSum;
    public static byte[] readSnCmd = {-11, 87, 76, -10};
    /* access modifiers changed from: private */
    public static byte[] resistCommand;
    private static byte resistCommandCheckSum;
    /* access modifiers changed from: private */
    public static byte[] rpmCommand;
    private static byte rpmCommandCheckSum;
    public int POWER_DATA_LENGTH = 10;
    public int RESIST_DATA_LENGTH = 8;
    public int RPM_DATA_LENGTH = 8;

    public static class ErrorCode {
        public static int CRC_CODE_ERROR = 4;
        public static int DATA_CODE_ERROR = 5;
        public static int END_CODE_ERROR = 1;
        public static int INSTRUCTION_CODE_ERROR = 6;
        public static int OVERRUN_CODE_ERROR = 3;
        public static int START_CODE_ERROR = 2;
    }

    private static byte getRequestCheckSum(int i, int i2) {
        return (byte) (i + i2);
    }

    public static class FrameData {
        public static final int INVALID_VALUE = -1;
        public static final int TYPE_CLOSE_LOCK = 5;
        public static final int TYPE_NACK = 0;
        public static final int TYPE_OPEN_LOCK = 6;
        public static final int TYPE_POWER = 2;
        public static final int TYPE_QUERY_LOCK_STATUS = 4;
        public static final int TYPE_RESIST = 3;
        public static final int TYPE_RPM = 1;
        public static final int TYPE_SN = 7;
        public static final int TYPE_UNKNOW = -1;
        public byte[] data;
        public int dataLen;
        public int frameSize;
        public int type = 0;
        public int value = -1;

        public static int getType(int i) {
            if (i == SerialProtocol.REQUEST_FOR_RPM) {
                return 1;
            }
            if (i == SerialProtocol.REQUEST_FOR_INSTANTANEOUS_POWER) {
                return 2;
            }
            if (i == SerialProtocol.REQUEST_FOR_RESISTENCE) {
                return 3;
            }
            if (i == SerialProtocol.REQUEST_FOR_QUERY_BICYCLE_LOCK_STATUS) {
                return 4;
            }
            if (i == SerialProtocol.REQUEST_FOR_OPEN_BICYCLE_LOCK) {
                return 6;
            }
            if (i == SerialProtocol.REQUEST_FOR_CLOSE_BICYCLE_LOCK) {
                return 5;
            }
            return i == 87 ? 7 : -1;
        }

        public boolean hasValidData() {
            int i = this.type;
            if (i == 7) {
                return true;
            }
            if (i == 0 || i == -1 || this.value == -1) {
                return false;
            }
            return true;
        }

        public String toString() {
            return "FrameData{value=" + this.value + ", type=" + this.type + ", frameSize=" + this.frameSize + '}';
        }
    }

    public static class CyclicGetData {
        public int index = -1;

        public byte[] getDataCmd() {
            byte[] bArr;
            int i = this.index;
            if (i == -1) {
                bArr = SerialProtocol.handShakeCommand;
            } else if (i == 0) {
                bArr = SerialProtocol.rpmCommand;
            } else if (i == 1) {
                bArr = SerialProtocol.resistCommand;
            } else if (i == 2) {
                bArr = SerialProtocol.powerCommand;
            } else if (i == 3) {
                bArr = SerialProtocol.resistCommand;
            } else {
                bArr = SerialProtocol.resistCommand;
            }
            int i2 = this.index + 1;
            this.index = i2;
            if (i2 > 3) {
                this.index = 0;
            }
            return bArr;
        }
    }

    static {
        byte b = (byte) (-11 + 65);
        rpmCommandCheckSum = b;
        rpmCommand = new byte[]{-11, 65, b, -10};
        byte b2 = (byte) (-11 + 68);
        powerCommandCheckSum = b2;
        powerCommand = new byte[]{-11, 68, b2, -10};
        byte b3 = (byte) (-11 + 73);
        resistCommandCheckSum = b3;
        resistCommand = new byte[]{-11, 73, b3, -10};
        byte b4 = (byte) (-11 + 84);
        openBicycleLockCommandCheckSum = b4;
        openBicycleLockCommand = new byte[]{-11, 84, b4, -10};
        byte b5 = (byte) (-11 + 82);
        closeBicycleLockCommandCheckSum = b5;
        closeBicycleLockCommand = new byte[]{-11, 82, b5, -10};
        byte b6 = (byte) (-11 + 80);
        queryBicycleLockStatusCommandCheckSum = b6;
        queryBicycleLockStatusCommand = new byte[]{-11, 80, b6, -10};
    }

    public static byte getResponseCheckSum(byte[] bArr, int i) {
        int i2 = 0;
        for (int i3 = 0; i3 <= i; i3++) {
            i2 += bArr[i3];
        }
        return (byte) i2;
    }

    public static boolean hasOneFrame(byte[] bArr, int i) {
        SerialLog.m440d("bufLen------>" + i);
        if (i < 3) {
            return false;
        }
        if (bArr[0] == RSP_NAK_CODE) {
            SerialLog.m440d("RSP_NAK_CODE bufLen------>" + i);
            if (i >= 5) {
                return true;
            }
            return false;
        } else if (bArr[0] != RESPONSE_START_CODE) {
            return false;
        } else {
            SerialLog.m440d("RESPONSE_START_CODE bufLen------>" + i);
            if (i >= bArr[2] + 5) {
                return true;
            }
            return false;
        }
    }

    public static int byteArrayToInt(byte[] bArr) throws Exception {
        return new DataInputStream(new ByteArrayInputStream(bArr)).readInt();
    }

    public static boolean isValidHead(byte b) {
        return b == RSP_NAK_CODE || b == RESPONSE_START_CODE;
    }

    public static FrameData parseFrame(byte[] bArr) {
        FrameData frameData = new FrameData();
        int i = 0;
        if (bArr[0] == RSP_NAK_CODE) {
            frameData.type = 0;
            frameData.frameSize = 5;
            showNoAckInfo(bArr[1], bArr[2]);
        } else if (bArr[0] == RESPONSE_START_CODE) {
            frameData.type = FrameData.getType(bArr[1]);
            byte b = bArr[2];
            frameData.frameSize = b + 5;
            frameData.dataLen = b;
            if (frameData.type != -1 && b >= 0) {
                int i2 = b + 2;
                byte responseCheckSum = getResponseCheckSum(bArr, i2);
                byte b2 = bArr[i2 + 1];
                if (responseCheckSum != b2) {
                    frameData.type = -1;
                    frameData.value = -1;
                    SerialLog.m440d("frameData.type--》 " + frameData.type + "，checkSum( " + responseCheckSum + " ) != dataCheckSum ( " + b2 + " )");
                    return frameData;
                }
                try {
                    if (frameData.type != 7) {
                        int i3 = b - 1;
                        while (i3 >= 0) {
                            i = (i * 10) + (bArr[i2] - 48);
                            i3--;
                            i2--;
                        }
                        frameData.value = i;
                    }
                } catch (Exception unused) {
                }
                frameData.data = Arrays.copyOfRange(bArr, 3, bArr.length - 3);
            }
        }
        return frameData;
    }

    private static void showNoAckInfo(byte b, byte b2) {
        String str;
        if (b == REQUEST_FOR_RPM) {
            str = "rpm";
        } else if (b == REQUEST_FOR_INSTANTANEOUS_POWER) {
            str = "power";
        } else {
            str = b == REQUEST_FOR_RESISTENCE ? "resistance" : "";
        }
        SerialLog.m441i("no ack " + str + " err code:" + b2);
    }
}
