package edu.wpi.first.wpilibj.templates;

public class VariableMap{
    //Constants
    /**
     * Dead-zone tolerance for the joysticks. The keyword 'final' means that
     * this variable can not be assigned to (changed) elsewhere in the code.
     */
    public static double DEADZONE = 0.1;
    public static int[] SHOT_POT_VALUES = {1, 2, 3, 4, 5};
    public static int[] SHOT_POWER_VALUES = {1, 2, 3, 4, 5};
    public static int FRONT_LOAD_POS = 123;
    public static int BACK_LOAD_POS = 987;
    //PWM
    public static int PWM_DRIVERIGHT = 1;
    public static int PWM_DRIVERIGHT2 = 2;
    public static int PWM_DRIVELEFT = 3;
    public static int PWM_DRIVELEFT2 = 4;
    public static int PWM_PICKUP_PIVOT = 5;
    public static int PWM_SHOOTER_WINCH = 6;
    //Relays
    public static int SPIKE_COMPRESSOR = 8;
    public static int SPIKE_ROLLER = 2;
    //Solonoids
    public static int SOLO_SHIFT_LOW = 1;
    public static int SOLO_SHIFT_HIGH = 2;
    public static int SOLO_FIRE_SHOOTER = 3;
    public static int SOLO_RESET_SHOOTER = 4;
    public static int SOLO_JAW_OPEN = 5;
    public static int SOLO_JAW_CLOSE = 6;
    //Digital IO
    public static int DIO_COMPRESSOR = 1;
    public static int DIO_ENCODER_RIGHT_A = 2;
    public static int DIO_ENCODER_RIGHT_B = 3;
    public static int DIO_ENCODER_LEFT_A = 4;
    public static int DIO_ENCODER_LEFT_B = 5;
    // Analog
    public static int ANALOG_NECK_POT = 1;
    public static int ANALOG_WINCH_POT = 2;
}