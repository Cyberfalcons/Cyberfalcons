package edu.wpi.first.wpilibj.templates;

public class VariableMap {
    //Constants

    /**
     * Dead-zone tolerance for the joysticks. The keyword 'final' means that
     * this variable can not be assigned to (changed) elsewhere in the code.
     */
    public double DEADZONE = 0.15;
    public boolean autoCatching;
    public boolean autoUpright;
    public boolean pickingUp;
    public boolean pickingFront;
    public boolean autoShooting;
    public boolean holdCalled;
    public boolean fireCalled;
    public boolean jawOpen;
    public int fireCalledCycles;
    public int currentNeckSetPoint;
    public int[] SHOT_POWER_VALUES = {1000, 2000};
    public int FRONT_LOAD_POS = 2000; // the back + 106
    public int BACK_LOAD_POS = 0; // the front - 106
    public int JAW_UPRIGHT_POS = BACK_LOAD_POS + 36;
    public int[] SHOT_POT_VALUES = {BACK_LOAD_POS, BACK_LOAD_POS, BACK_LOAD_POS}; // low far; high far; truss

    /**
     * For Initializing the neck so it is free to move until set by hitting
     * limit switch
     */
    public void freeNeckValues() {
        FRONT_LOAD_POS = 2000; // the back + 100
        BACK_LOAD_POS = 0; // the front - 100
    }

    /**
     * Updates all the Neck potentiometer values based on the reading when a
     * limit switch is pressed
     *
     * @param frontLimit true if the limit switch is at the front of the robot
     * and false if it is at the back
     */
    public void updateNeckPotValues(SensorFunctions sf, boolean frontLimit) {
        if (frontLimit) {
            FRONT_LOAD_POS = sf.getNeckPot();
            BACK_LOAD_POS = FRONT_LOAD_POS - 110;
            JAW_UPRIGHT_POS = BACK_LOAD_POS + 33;
            SHOT_POT_VALUES[0] = BACK_LOAD_POS + 62;
            SHOT_POT_VALUES[1] = BACK_LOAD_POS + 66;
            SHOT_POT_VALUES[2] = BACK_LOAD_POS + 70;
        } else {
            BACK_LOAD_POS = sf.getNeckPot();
            FRONT_LOAD_POS = BACK_LOAD_POS + 110;
            JAW_UPRIGHT_POS = BACK_LOAD_POS + 33;
            SHOT_POT_VALUES[0] = BACK_LOAD_POS + 62;
            SHOT_POT_VALUES[1] = BACK_LOAD_POS + 66;
            SHOT_POT_VALUES[2] = BACK_LOAD_POS + 70;
        }
    }

    /**
     * Updates all the Winch potentiometer values based on the reading when a
     * limit switch is pressed
     */
    public void updateWinchPotValues(SensorFunctions sf) {
        SHOT_POWER_VALUES[0] = sf.getNeckPot() - 3;
        SHOT_POWER_VALUES[1] = sf.getNeckPot() - 50;
    }
    //PWM
    public static int PWM_DRIVERIGHT = 3;
    public static int PWM_DRIVERIGHT2 = 4;
    public static int PWM_DRIVELEFT = 1;
    public static int PWM_DRIVELEFT2 = 2;
    public static int PWM_ROLLER = 5;
    public static int PWM_PICKUP_PIVOT = 6;
    public static int PWM_SHOOTER_WINCH = 7;
    //Relays
    public static int SPIKE_COMPRESSOR = 8;
    //Solonoids
    public static int SOLO_SHIFT_LOW = 3;
    public static int SOLO_SHIFT_HIGH = 4;
    public static int SOLO_FIRE_SHOOTER = 5;
    public static int SOLO_RESET_SHOOTER = 6;
    public static int SOLO_JAW_OPEN = 1;
    public static int SOLO_JAW_CLOSE = 2;
    //Digital IO
    public static int DIO_COMPRESSOR = 1;
    public static int DIO_ULTRA_SENSOR = 2;
    public static int DIO_FRONT_LIMIT = 3;
    public static int DIO_BACK_LIMIT = 4;
    public static int DIO_WINCH_LIMIT = 5;
//    public static int DIO_ENCODER_RIGHT_A = 2;
//    public static int DIO_ENCODER_RIGHT_B = 3;
//    public static int DIO_ENCODER_LEFT_A = 4;
//    public static int DIO_ENCODER_LEFT_B = 5;
    // Analog
    public static int ANALOG_NECK_POT = 1;
    public static int ANALOG_WINCH_POT = 2;
}