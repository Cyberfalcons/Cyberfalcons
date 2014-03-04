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
public class CyberFalcons2014Side extends IterativeRobot {

    // Functions
    DriveFunctions df;
    /* CONSTANTS */
    /**
     * Dead-zone tolerance for the joysticks. The keyword 'final' means that
     * this variable can not be assigned to (changed) elsewhere in the code.
     */
    final double DEADZONE = VariableMap.DEADZONE;
    // PWM Channel Pinout - should be in robot map
    int pwmDriveRight = VariableMap.PWM_DRIVERIGHT;
    int pwmDriveLeft = VariableMap.PWM_DRIVELEFT;
    int pwmDriveRight2 = VariableMap.PWM_DRIVERIGHT2;
    int pwmDriveLeft2 = VariableMap.PWM_DRIVELEFT2;
    // Xbox controllers
    XBoxController xboxDriver;
    // Drive motors
    /*Talon*/
    Jaguar talonDriveRight;
    /*Talon*/
    Jaguar talonDriveLeft;
    /*talon*/
    Jaguar talonDriveRight2;
    /*Talon*/
    Jaguar talonDriveLeft2;
    // Operation Variables
    boolean teleopActive = false;
    boolean controlFlip = false;
    boolean controlFlipClean = true;

    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {

        // Xbox Controllers
        xboxDriver = new XBoxController(1); // USB port 1

        // Drive Motors
        talonDriveRight = new /*Talon*/ Jaguar(/*cRIO slot*/1, pwmDriveRight);
        talonDriveLeft = new /*Talon*/ Jaguar(/*cRIO slot*/1, pwmDriveLeft);
        talonDriveRight2 = new /*Talon*/ Jaguar(/*cRIO slot*/1, pwmDriveRight2);
        talonDriveLeft2 = new /*Talon*/ Jaguar(/*cRIO slot*/1, pwmDriveLeft2);

        df = new DriveFunctions(talonDriveRight, talonDriveLeft, talonDriveLeft2, talonDriveRight2, controlFlip, DEADZONE);

    }

    /**
     * This function is called at the start of autonomous
     */
    public void autonomousInit() {
        df.resetDriveSystem();
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
        Watchdog.getInstance().feed(); // Tell watchdog we are running

    }

    /**
     * This function is called at the start of operator control
     */
    public void teleopInit() {
        df.resetDriveSystem();
        if (controlFlip) {
            toggleControlFlip();
        }
    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
        // Tell watchdog we are running
        Watchdog.getInstance().feed();

        // Activate the robot by pushing start button
        if (xboxDriver.getBtnSTART()) {
            teleopActive = true;
        }
        if (!teleopActive) {
            // Only run if teleop has been active
            return;
        }

        drive();
    }

    /**
     * This function is called at the start of test
     */
    public void testInit() {
        df.resetDriveSystem();
    }

    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
        Watchdog.getInstance().feed(); // Tell watchdog we are running
        drive();
    }

    /*
     * Checks what buttons the driver has pressed and acts accordingly
     */
    public void drive() {

        if (xboxDriver.getBtnL3()) {
            toggleControlFlip();
        }

        df.setDriveRight(xboxDriver.getRightY());
        df.setDriveLeft(xboxDriver.getLeftY());

    }

    /**
     Flips the controls to allow easy backwards driving
     */
    public void toggleControlFlip() {
        if (controlFlipClean) {
            controlFlipClean = false;
            controlFlip = !controlFlip;
        } else {
            controlFlipClean = true;
        }
        df.controlFlip = controlFlip;
    }
}
