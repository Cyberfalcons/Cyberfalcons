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
    /*Talon*/Jaguar driveRight;
    /*Talon*/Jaguar driveLeft;
    /*Talon*/Jaguar driveRight2;
    /*Talon*/Jaguar driveLeft2;
  
    boolean controlFlip;

    /**
     *
     * @param dr - the right side drive talon
     * @param dl - the left side drive talon
     * @param dr2 - the second right side drive talon
     * @param dl2 - the second left side drive talon
     * @param cf - the control flip variable
     * @param dz - the joystick Dead-zone
     */
    public DriveFunctions(/*Talon*/Jaguar dr, /*Talon*/Jaguar dl, /*Talon*/Jaguar dl2, 
            /*Talon*/Jaguar dr2, boolean cf, double dz) {
        driveRight = dr;
        driveLeft = dl;
        driveRight2 = dr2;
        driveLeft2 = dl2;
        controlFlip = cf;
        DEADZONE = dz;
    }

    public void resetDriveSystem() {
        setDriveRight(0);
        setDriveLeft(0);

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
                driveLeft2.set(power);
            } else {
                driveLeft.set(-power);
                driveLeft2.set(-power);
            }
        } else {
            driveLeft.set(0);
            driveLeft2.set(0);
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
                driveRight2.set(-power);
            } else {
                driveRight.set(power);
                driveRight2.set(power);
            }
        } else {
            driveRight.set(0);
            driveRight2.set(0);
        }
    }
}
