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
    //PWM
    public static int PWM_DRIVERIGHT = 1;
    public static int PWM_DRIVERIGHT2 = 2;
    public static int PWM_DRIVELEFT = 3;
    public static int PWM_DRIVELEFT2 = 4;
    public static int PWM_PICKUP_PIVOT = 5;
    public static int PWM_ROLLER = 6;
    public static int PWM_LAUNCHER = 7;
    //Solonoids
    public static int SOLO_DRIVE_LEFT = 1;
    public static int SOLO_DRIVE_RIGHT = 2;
    public static int SOLO_LAUNCHER = 3;
    public static int SOLO_JAW = 4;
    //Digital IO
    public static int DIO_ENCODER_RIGHT_A = 1;
    public static int DIO_ENCODER_RIGHT_B = 2;
    public static int DIO_ENCODER_LEFT_A = 3;
    public static int DIO_ENCODER_LEFT_B = 4;
}