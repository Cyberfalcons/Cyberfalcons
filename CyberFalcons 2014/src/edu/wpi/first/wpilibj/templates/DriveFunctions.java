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
    PIDController pidCL;
    PIDController pidCR;
    // Drive motors
    /*Talon*/Jaguar driveRight;
    /*Talon*/Jaguar driveLeft;
    // Shifting Solenoids
    Solenoid shift1;
    Solenoid shift2;
    // Drive encoders
    Encoder driveLeftE;
    Encoder driveRightE;
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
    public DriveFunctions(/*Talon*/Jaguar dr, /*Talon*/Jaguar dl, Encoder drE, Encoder dlE, Solenoid s1, 
            Solenoid s2, SensorFunctions sFunctions) {
        driveRight = dr;
        driveLeft = dl;
        driveRightE = drE;
        driveLeftE = dlE;
        shift1 = s1;
        shift2 = s2;
        controlFlip = false;
        controlFlipClean = true;
        DEADZONE = VariableMap.DEADZONE;
        sf = sFunctions;
        holdingPosition = false;
        pidCL = new PIDController(1,1,0,driveLeftE,driveLeft);
        pidCR = new PIDController(1,1,0,driveRightE,driveRight);
    }

    public void resetDriveSystem() {
        setDriveRight(0);
        setDriveLeft(0);
        notShifting();
        notHoldingPosition();
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
    
    public void holdPosition(double position) {
        if (!pidCL.isEnable()) {
            driveLeftE.reset();
            pidCL.enable();
        }
        if (!pidCR.isEnable()) {
            driveRightE.reset();
            pidCR.enable();
        }
        pidCL.setSetpoint(position);
        pidCR.setSetpoint(position);
    }
    
    public void notHoldingPosition() {
        pidCL.disable();
        pidCR.disable();
    }
    
    public boolean isHoldingPosition() {
        if (pidCL.isEnable() || pidCR.isEnable()) {
            return true;
        }
        return false;
    }
    
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
