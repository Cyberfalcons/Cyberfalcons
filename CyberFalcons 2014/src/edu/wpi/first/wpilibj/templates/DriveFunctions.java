/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.*;

/**
 *
 * @author Nathan Hicks
 */
public class DriveFunctions {

    /**
     * Dead-zone tolerance for the joysticks. The keyword 'final' means that
     * this variable can not be assigned to (changed) elsewhere in the code.
     */
    final double DEADZONE;
    // PID Controller
//    PIDController pidCL;
//    PIDController pidCL2;
//    PIDController pidCR;
//    PIDController pidCR2;
    // Drive motors
    Talon driveRight;
    Talon driveRight2;
    Talon driveLeft;
    Talon driveLeft2;
    // Shifting Solenoids
    Solenoid shift1;
    Solenoid shift2;
    // Drive encoders
//    Encoder driveLeftE;
//    Encoder driveRightE;
    // Sensor access
    SensorFunctions sf;
    // Control Variables
    boolean controlFlip;
    boolean controlFlipClean;
    boolean holdingPosition;

    /**
     *
     * @param dr - the right side drive talon
     * @param dl - the left side drive talon
     * @param s1 - the first shifting solenoid
     * @param s2 - the second shifting solenoid
     * @param cf - the control flip variable
     * @param dz - the joystick Dead-zone
     */
    public DriveFunctions(Talon dr, Talon dr2, Talon dl, Talon dl2, /*Encoder drE, Encoder dlE,*/ Solenoid s1, 
            Solenoid s2, SensorFunctions sFunctions) {
        driveRight = dr;
        driveRight2 = dr2;
        driveLeft = dl;
        driveLeft2 = dl2;
//        driveRightE = drE;
//        driveLeftE = dlE;
        shift1 = s1;
        shift2 = s2;
        controlFlip = false;
        controlFlipClean = true;
        DEADZONE = VariableMap.DEADZONE;
        sf = sFunctions;
        holdingPosition = false;
//        pidCL = new PIDController(1,1,0,driveLeftE,driveLeft);
//        pidCL2 = new PIDController(1,1,0,driveLeftE,driveLeft2);
//        pidCR = new PIDController(1,1,0,driveRightE,driveRight);
//        pidCR2 = new PIDController(1,1,0,driveRightE,driveRight2);
    }

    public void resetDriveSystem() {
        setDriveRight(0);
        setDriveLeft(0);
        notShifting();
//        notHoldingPosition();
    }

    /**
     * Sets the power to be given to the left drive motors
     *
     * @param power - should be a directly given joystick input
     */
    public void setDriveLeft(double power) {
        if (power < -DEADZONE || power > DEADZONE) {
            if (controlFlip) {
                driveLeft.set(power);
            } else {
                driveLeft.set(-power);
            }
        } else {
            driveLeft.set(0);
        }
        driveLeft2.set(driveLeft.get());
    }

    /**
     * Sets the power to be given to the right drive motors
     *
     * @param power - should be a directly given joystick input
     */
    public void setDriveRight(double power) {
        if (power < -DEADZONE || power > DEADZONE) {
            if (controlFlip) {
                driveRight.set(-power);
            } else {
                driveRight.set(power);
            }
        } else {
            driveRight.set(0);
        }
        driveRight2.set(driveRight.get());
    }

    /**
     * Shifts to high gear.
     */
    public void shiftHigh() {
        shift1.set(true);
        shift2.set(false);
    }

    /**
     * Shifts to low gear.
     */
    public void shiftLow() {
        shift1.set(false);
        shift2.set(true);
    }

    /**
     * Turns off shifting solenoids. Should always be called when not shifting.
     */
    public void notShifting() {
        shift1.set(false);
        shift2.set(false);
    }
    
//    public void holdPosition(double position) {
//        if (!pidCL.isEnable()) {
//            driveLeftE.reset();
//            pidCL.enable();
//            pidCL2.enable();
//        }
//        if (!pidCR.isEnable()) {
//            driveRightE.reset();
//            pidCR.enable();
//            pidCR2.enable();
//        }
//        pidCL.setSetpoint(position);
//        pidCL2.setSetpoint(position);
//        pidCR.setSetpoint(position);
//        pidCR2.setSetpoint(position);
//    }
//    
//    public void notHoldingPosition() {
//        pidCL.disable();
//        pidCL2.disable();
//        pidCR.disable();
//        pidCR2.disable();
//    }
//    
//    public boolean isHoldingPosition() {
//        if (pidCL.isEnable() || pidCR.isEnable()) {
//            return true;
//        }
//        return false;
//    }
    
    /**
     * Switches the direction that is forward.
     */
    public void toggleControlFlip() {
        if (controlFlipClean) {
            controlFlipClean = false;
            controlFlip = !controlFlip;
        } else {
            controlFlipClean = true;
        }
    }
}
