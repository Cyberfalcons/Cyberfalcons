/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.*;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class CyberFalconsPre2014Shifter extends IterativeRobot {

    /* CONSTANTS */
    /**
     * Dead-zone tolerance for the joysticks. The keyword 'final' means that
     * this variable can not be assigned to (changed) elsewhere in the code.
     */
    final double DEADZONE = 0.1;
    // Xbox controllers
    XBoxController xboxDriver;
    // Drive motors
    Victor vicDriveRight;
    Victor vicDriveLeft;
    // Gear shifter
    Solenoid shifter1;
    Solenoid shifter2;
    // Operation Variables
    boolean teleopActive = false;
    boolean controlFlip = true;
    boolean controlFlipClean = true;

    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
        // Xbox Controllers
        xboxDriver = new XBoxController(1); // USB port 1

        // Drive Motors
        vicDriveRight = new Victor(/*cRIO slot*/1, /*PWM channel*/ 1);
        vicDriveLeft = new Victor(/*cRIO slot*/1, /*PWM channel*/ 2);
        // Gear shifter
        shifter1 = new Solenoid(/*cRIO slot*/1, /*channel*/ 7);
        shifter2 = new Solenoid(/*cRIO slot*/1, /*channel*/ 8);

    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
    }

    /**
     * Called every time tele-op mode starts
     */
    public void teleopInit() {
        vicDriveRight.set(0);
        vicDriveLeft.set(0);
        shifter1.set(false);
        shifter2.set(false);
    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
        Watchdog.getInstance().feed(); // Tell watchdog we are running

        // activate the robot by pushing start button
        if (xboxDriver.getBtnSTART()) {
            teleopActive = true;
        }
        if (!teleopActive) {
            return;         //only run if teleop is active
        }

        drive();
    }

    /**
     * Called every time test mode starts
     */
    public void testInit() {
        vicDriveRight.set(0);
        vicDriveLeft.set(0);
        shifter1.set(false);
        shifter2.set(false);
    }

    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
        Watchdog.getInstance().feed(); // Tell watchdog we are running
        drive();
    }

    public void drive() {
        //Driving controls
            /* flips which direction on the joysticks is 'forward'
         * the variable 'controlFlipClean' needs some explanation:
         * it forces the driver to release the button before it recognizes
         * another click. */
        if (xboxDriver.getBtnL3()) {    // left stick click
            if (controlFlipClean) {
                controlFlipClean = false;
                controlFlip = !controlFlip;
            }
        } else {
            controlFlipClean = true;
        }

        // gear shift
        if (xboxDriver.getBtnRB()) {
            shifter1.set(true);
        } else if (xboxDriver.getBtnLB())  {
            shifter2.set(true);

        } else {
            shifter1.set(false);
            shifter2.set(false);
        }
        //controls right side
        double yRight = xboxDriver.getRightY();
        //controls left side
        double yLeft = -xboxDriver.getLeftY();
        //trigger values will change the maximum speed of the robot
        double throttle = xboxDriver.getTriggers();
        //half speed
        if (throttle
                > 0.5) {
            yRight = yRight * 0.5;
            yLeft = yLeft * 0.5;
        }
        //quarter speed
        if (throttle
                < -0.5) {
            yRight = yRight * 0.25;
            yLeft = yLeft * 0.25;
        }
        // flip controls if we need to. Make them go 'backwards'.
        // We need to swap and yRight and yLeft values.
        if (controlFlip) {
            double tempRight = yRight;
            yRight = yLeft;
            yLeft = tempRight;
        }

        /* DEADZONE **
         * The springs in the joysticks wear out so they don't always come
         * back to the centre. To fix this we're going to ignore a small region
         * around the centre.
         */
        if (yRight > -DEADZONE && yRight < DEADZONE) {
            yRight = 0;
        }
        if (yLeft > -DEADZONE && yLeft < DEADZONE) {
            yLeft = 0;
        }

        vicDriveRight.set(yRight);

        vicDriveLeft.set(yLeft);
    } // end of drive
}
