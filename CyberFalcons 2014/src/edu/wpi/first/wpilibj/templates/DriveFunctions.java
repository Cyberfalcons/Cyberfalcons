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

    // Drive motors
    Talon driveRight;
    Talon driveRight2;
    Talon driveLeft;
    Talon driveLeft2;
    // Shifting Solenoids
    Solenoid shift1;
    Solenoid shift2;
    // Sensor access
    SensorFunctions sf;
    // Constant Variable Access
    VariableMap vm;
    // Control Variables
    boolean controlFlip;
    boolean controlFlipClean;

    /**
     *
     * @param dr the right side drive talon
     * @param dl the left side drive talon
     * @param s1 the first shifting solenoid
     * @param s2 the second shifting solenoid
     * @param sFunctions the sensor functions class
     * @param vMap the variable map class
     */
    public DriveFunctions(Talon dr, Talon dr2, Talon dl, Talon dl2, Solenoid s1,
            Solenoid s2, SensorFunctions sFunctions, VariableMap vMap) {
        driveRight = dr;
        driveRight2 = dr2;
        driveLeft = dl;
        driveLeft2 = dl2;
        shift1 = s1;
        shift2 = s2;
        controlFlip = false;
        controlFlipClean = true;
        vm = vMap;
        sf = sFunctions;
    }

    public void resetDriveSystem() {
        setDriveRight(0);
        setDriveLeft(0);
        notShifting();
    }

    /**
     * Sets the power to be given to the left drive motors
     *
     * @param power - should be a directly given joystick input
     */
    public void setDriveLeft(double power) {
        if (power < -vm.DEADZONE || power > vm.DEADZONE) {
            if (!controlFlip) {
                driveLeft.set(-power);
            } else {
                driveRight.set(-power); // if controls are flipped the commanded left side is the physical right side
            }
        } else {
            if (!controlFlip) {
                driveLeft.set(0);
            } else {
                driveRight.set(0);
            }
        }
        if (!controlFlip) {
            driveLeft2.set(driveLeft.get());
        } else {
            driveRight2.set(driveRight.get());
        }
    }

    /**
     * Sets the power to be given to the right drive motors
     *
     * @param power - should be a directly given joystick input
     */
    public void setDriveRight(double power) {
        if (power < -vm.DEADZONE || power > vm.DEADZONE) {
            if (controlFlip) {
                driveLeft.set(power);// if controls are flipped the commanded left side is the physical left side
            } else {
                driveRight.set(power);
            }
        } else {
            if (controlFlip) {
                driveLeft.set(0);
            } else {
                driveRight.set(0);
            }
        }
        if (controlFlip) {
            driveLeft2.set(driveLeft.get());
        } else {
            driveRight2.set(driveRight.get());
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

    /**
     * Switches the direction that is forward for driving.
     */
    public void toggleControlFlip() {
        if (controlFlipClean) {
            controlFlipClean = false;
            controlFlip = !controlFlip;
        }
    }

    public void controlFlipButtonReleased() {
        controlFlipClean = true;
    }
}
