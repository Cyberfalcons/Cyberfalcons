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
public class CyberFalcons2014Main extends IterativeRobot {

    // Functions
    DriveFunctions df;
    SensorFunctions sf;
    /* CONSTANTS */
    /**
     * Dead-zone tolerance for the joysticks. The keyword 'final' means that
     * this variable can not be assigned to (changed) elsewhere in the code.
     */
    final double DEADZONE = 0.1;
    final int[] shotPotValue = {1, 2, 3, 4, 5};
    final int shotReadyValue = 324;
    // Xbox controllers
    XBoxController xboxDriver;
    // Drive motors
    /*Talon*/
    Jaguar talonDriveRight;
    /*Talon*/
    Jaguar talonDriveLeft;
    // Gear shifter
    Solenoid shifter1;
    Solenoid shifter2;
    // Drive encoders
    Encoder driveLeftE;
    Encoder driveRightE;
    // Sensors
    AnalogChannel neckPot;
    AnalogChannel winchPot;
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
        talonDriveRight = new /*Talon*/ Jaguar(/*cRIO slot*/1, /*PWM channel*/ 1);
        talonDriveLeft = new /*Talon*/ Jaguar(/*cRIO slot*/1, /*PWM channel*/ 2);
        // Gear shifter
        shifter1 = new Solenoid(/*cRIO slot*/1, /*channel*/ 7);
        shifter2 = new Solenoid(/*cRIO slot*/1, /*channel*/ 8);
        // Sensors
        neckPot = new AnalogChannel(/*cRIO slot*/1, /*channel*/ 8);


        sf = new SensorFunctions(neckPot, winchPot, shotReadyValue, driveLeftE, driveRightE);
        df = new DriveFunctions(talonDriveRight, talonDriveLeft, shifter1, shifter2, controlFlip, DEADZONE, sf);

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

        System.out.println(neckPot.getValue());
        drive();
    }

    public void drive() {
        if (xboxDriver.getBtnA()) {
            df.holdPosition();
        } else {
            df.notHoldingPosition();
            
            if (xboxDriver.getBtnL3()) {
                toggleControlFlip();
            }

            if (xboxDriver.getBtnRB()) {
                df.shiftLow();
            } else if (xboxDriver.getBtnLB()) {
                df.shiftHigh();
            } else {
                df.notShifting();
            }

            df.setDriveRight(xboxDriver.getRightY());
            df.setDriveLeft(xboxDriver.getLeftY());
        }
    }

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
