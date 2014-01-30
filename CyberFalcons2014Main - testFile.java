/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.CounterBase.EncodingType;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

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
    
    DigitalInput testSwitch;

    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {

//        // Xbox Controllers
//        xboxDriver = new XBoxController(1); // USB port 1
//
//        // Drive Motors
//        talonDriveRight = new /*Talon*/ Jaguar(/*cRIO slot*/1, VariableMap.PWM_DRIVERIGHT);
//        talonDriveLeft = new /*Talon*/ Jaguar(/*cRIO slot*/1, VariableMap.PWM_DRIVELEFT);
//        // Gear shifter
//        shifter1 = new Solenoid(/*cRIO slot*/1, /*channel*/ VariableMap.SOLO_DRIVE_LEFT);
//        shifter2 = new Solenoid(/*cRIO slot*/1, /*channel*/ VariableMap.SOLO_DRIVE_RIGHT);
        // Drive Encoders
//        driveRightE = new Encoder(VariableMap.DIO_ENCODER_RIGHT_A, VariableMap.DIO_ENCODER_RIGHT_B,  false, EncodingType.k4X);
//        driveLeftE = new Encoder(VariableMap.DIO_ENCODER_LEFT_A, VariableMap.DIO_ENCODER_LEFT_B, true, EncodingType.k4X);
//        driveLeftE.setDistancePerPulse(5);
//        driveLeftE.setMaxPeriod(10000);
//        driveLeftE.setMinRate(0.00001);
//        driveLeftE.stop();
//        driveRightE.start();
//        driveLeftE.start();
//        driveRightE.reset();
//        driveLeftE.reset();
        // Sensors
//        neckPot = new AnalogChannel(/*cRIO slot*/1, /*channel*/ 8);
testSwitch = new DigitalInput(1,1);

//        sf = new SensorFunctions(neckPot, winchPot, driveLeftE, driveRightE);
//        df = new DriveFunctions(talonDriveRight, talonDriveLeft, 
//                driveRightE, driveLeftE, shifter1, shifter2, sf);

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
        if (df.controlFlip) {
            df.toggleControlFlip();
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
//        df.resetDriveSystem();
//        driveLeftE.reset();
    }

    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
        Watchdog.getInstance().feed(); // Tell watchdog we are running

//        drive();
//        if (xboxDriver.getBtnB()) {
//            driveLeftE.reset();
//        }
        // Printing to console to get encoder data (here for debugging Jan 25)
//        System.out.println(/*driveLeftE.get() + "     " + */driveLeftE.getDistance() + "      "
//                + driveLeftE.getRaw() + "       " + driveLeftE.getDirection() + "       "
//                + driveLeftE.getRate());
        System.out.println(testSwitch.get());
    }

    /**
     * Maps all the various drive functions to buttons on the driver's xbox
     * controller
     */
    public void drive() {
        if (xboxDriver.getBtnA()) {
            df.holdPosition(0);
        }
        if (xboxDriver.getBtnX()) {
            df.notHoldingPosition();
        }
        if (!df.isHoldingPosition()) {
            if (xboxDriver.getBtnL3()) {
                df.toggleControlFlip();
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
}
