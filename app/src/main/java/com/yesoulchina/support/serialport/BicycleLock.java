package org.cagnulein.qz_companion_yesoul;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public class BicycleLock {
    public static final int ERROR_CODE_ELECTRIC_MOTOR_IDLING_TIMEOUT = 2;
    public static final int ERROR_CODE_ELECTRIC_MOTOR_STUCK = 3;
    public static final int ERROR_CODE_OTHER = 0;
    public static final int ERROR_CODE_WRITE_CMD_FAIL = 1;
    public static final int STATE_LOCKED = 3;
    public static final int STATE_LOCKING = 1;
    public static final int STATE_UNKNOWN = 0;
    public static final int STATE_UNLOCKED = 4;
    public static final int STATE_UNLOCKING = 2;

    @Retention(RetentionPolicy.SOURCE)
    public @interface ErrorCode {
    }

    @Target({ElementType.PARAMETER, ElementType.FIELD, ElementType.METHOD})
    @Retention(RetentionPolicy.SOURCE)
    public @interface State {
    }
}
