package edu.wpi.first.wpilibj.templates;

public class VariableMap {
    //Constants

    public static double ENCODER_DISTANCE_PER_PULSE = 0.0425;//distance in inches
    /**
     * Dead-zone tolerance for the joysticks. The keyword 'final' means that
     * this variable can not be assigned to (changed) elsewhere in the code.
     */
    public double DEADZONE = 0.15;
    public int timerOverload = 100000;
    public boolean autoCatching;
    public boolean autoUpright;
    public boolean pickingUp;
    public boolean pickingFront;
    public boolean autoShooting;
    public boolean holdCalled;
    public boolean fireCalled;
    public boolean jawOpen;
    public int lightCounter = 0;
    // Autonomous Variables
    public boolean movedForward = false; // used to track when robot has finished moving forward
    public boolean hasShot = false; // used to track when the robot has fired a shot
    public int autoCycles = 0; // used to keep track of how many cycles have passed during autonomus for timing purposes
    // Operation Variables - tracks robot state during telop/test
    public boolean teleopActive = false; // whether the driver is ready to operate during teleop
    public boolean yClean = true; // used to track when a button is pressed and held to avoid repeated calling of functions
    public boolean LBClean = true; // used to track when a button is pressed and held to avoid repeated calling of functions
    public boolean dClean = true; // used to track when a button is pressed and held to avoid repeated calling of functions
    public boolean limitClean = true; // used to track when a limit switch is pressed to avoid repeated calling of functions
    public int currentAutoShot = 0; // used to track which angle the automatic shot is set for
    public int fireCalledCycles;
    public int currentNeckSetPoint;
    // Important shot angle stuff
    public int[] SHOT_POWER_VALUES = {2000, 1000}; // high power, low power
    public int FRONT_LOAD_POS = 2000; // the back + 110
    public int BACK_LOAD_POS = 0; // the front - 110
    public int JAW_UPRIGHT_POS = BACK_LOAD_POS + 33;
    public int[] SHOT_POT_VALUES = {FRONT_LOAD_POS, FRONT_LOAD_POS, FRONT_LOAD_POS, FRONT_LOAD_POS}; // low far; high far; truss

    /**
     * For Initializing the neck so it is free to move until set by hitting
     * limit switch
     */
    public void freeNeckValues() {
        FRONT_LOAD_POS = 2000; // the back + 110`
        BACK_LOAD_POS = 0; // the front - 110
        JAW_UPRIGHT_POS = BACK_LOAD_POS + 33;
        SHOT_POT_VALUES[0] = FRONT_LOAD_POS + 62;
        SHOT_POT_VALUES[1] = FRONT_LOAD_POS + 66;
        SHOT_POT_VALUES[2] = FRONT_LOAD_POS + 50;
        SHOT_POT_VALUES[3] = FRONT_LOAD_POS + 47;
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
            BACK_LOAD_POS = FRONT_LOAD_POS - 100;
        } else {
            BACK_LOAD_POS = sf.getNeckPot() - 1;
            FRONT_LOAD_POS = BACK_LOAD_POS + 100;
        }
        JAW_UPRIGHT_POS = BACK_LOAD_POS + 29;
        SHOT_POT_VALUES[0] = FRONT_LOAD_POS - 41;
        SHOT_POT_VALUES[1] = FRONT_LOAD_POS - 34;
        SHOT_POT_VALUES[2] = FRONT_LOAD_POS - 50;
        SHOT_POT_VALUES[3] = FRONT_LOAD_POS - 53;
    }

    /**
     * Updates all the Winch potentiometer values based on the reading when a
     * limit switch is pressed
     */
    public void updateWinchPotValues(SensorFunctions sf) {
        SHOT_POWER_VALUES[0] = sf.getWinchPot() + 3;
        SHOT_POWER_VALUES[1] = sf.getWinchPot() + 50;
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
    public static int SOLO_SHIFT_LOW = 4;
    public static int SOLO_SHIFT_HIGH = 3;
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
    public static int DIO_ENCODER_RIGHT_A = 6;
    public static int DIO_ENCODER_RIGHT_B = 7;
    public static int DIO_ENCODER_LEFT_A = 8;
    public static int DIO_ENCODER_LEFT_B = 9;
    public static int DIO_AUTO_SIG_1 = 10;
    public static int DIO_AUTO_SIG_2 = 11;
    public static int DIO_AUTO_SIG_3 = 12;
    public static int DIO_STANDBY_SIGNAL = 13;
    public static int DIO_CATCH_SIGNAL = 14;
    // Analog
    public static int ANALOG_NECK_POT = 1;
    public static int ANALOG_WINCH_POT = 2;
}