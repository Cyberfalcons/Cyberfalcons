package edu.wpi.first.wpilibj.templates;

public class VariableMap{
    //Constants
    /**
     * Dead-zone tolerance for the joysticks. The keyword 'final' means that
     * this variable can not be assigned to (changed) elsewhere in the code.
     */
    public static double DEADZONE = 0.1;
    public static int[] SHOT_POT_VALUES = {1, 2, 3};
    public static int[] SHOT_POWER_VALUES = {1, 2};
    public static int FRONT_LOAD_POS = 810; // the back + 100
    public static int BACK_LOAD_POS = 710; // the front - 100
    public static int JAW_UPRIGHT_POS = BACK_LOAD_POS + 24;
    
    public void updateNeckPotValues(SensorFunctions sf, boolean frontLimit) {
        if (frontLimit) {
            FRONT_LOAD_POS = sf.getNeckPot();
            BACK_LOAD_POS = FRONT_LOAD_POS - 100;
            JAW_UPRIGHT_POS = BACK_LOAD_POS + 24;
        } else {
            BACK_LOAD_POS = sf.getNeckPot();
            FRONT_LOAD_POS = BACK_LOAD_POS + 100;
            JAW_UPRIGHT_POS = BACK_LOAD_POS + 24;
        }
    }
    //PWM
    public static int PWM_DRIVERIGHT = 1;
    public static int PWM_DRIVERIGHT2 = 2;
    public static int PWM_DRIVELEFT = 3;
    public static int PWM_DRIVELEFT2 = 4;
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
//    public static int DIO_ENCODER_RIGHT_A = 2;
//    public static int DIO_ENCODER_RIGHT_B = 3;
//    public static int DIO_ENCODER_LEFT_A = 4;
//    public static int DIO_ENCODER_LEFT_B = 5;
    // Analog
    public static int ANALOG_NECK_POT = 1;
    public static int ANALOG_WINCH_POT = 2;
}