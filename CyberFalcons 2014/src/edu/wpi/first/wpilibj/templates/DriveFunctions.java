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
    // Drive motors
    Talon driveRight;
    Talon driveLeft;
    // Shifting Solenoids
    Solenoid shift1;
    Solenoid shift2;
    // Control Variables
    boolean controlFlip;

    /**
     *
     * @param dr - the right side drive talon
     * @param dl - the left side drive talon
     * @param s1 - the first shifting solenoid
     * @param s2 - the second shifting solenoid
     * @param cf - the control flip variable
     * @param dz - the joystick Dead-zone
     */
    public DriveFunctions(Talon dr, Talon dl, Solenoid s1, Solenoid s2, boolean cf, double dz) {
        driveRight = dr;
        driveLeft = dl;
        shift1 = s1;
        shift2 = s2;
        controlFlip = cf;
        DEADZONE = dz;
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
        if (power > -DEADZONE && power < DEADZONE) {
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
        if (power > -DEADZONE && power < DEADZONE) {
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
}
