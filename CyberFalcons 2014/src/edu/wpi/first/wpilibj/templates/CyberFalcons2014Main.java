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
    ShootingFunctions shf;
    PickupFunctions pf;
    // Xbox controllers
    XBoxController xboxDriver;
    XBoxController xboxOperator;
    // Compressor
    Compressor airCompressor;
    // Drive motors
    Talon talonDriveRight;
    Talon talonDriveRight2;
    Talon talonDriveLeft;
    Talon talonDriveLeft2;
    // Gear shifter
    Solenoid shifter1;
    Solenoid shifter2;
    // Drive encoders
//    Encoder driveLeftE;
//    Encoder driveRightE;
    // Pickup Pivot Motor
    Victor neck;
    // Jaw Solenoids
    Solenoid openJaw;
    Solenoid closeJaw;
    // Shooting Solenoids
    Solenoid fire;
    Solenoid resetFire;
    // Shooter Winch
    Relay winch;
    // Pickup Roller
    Relay roller;
    // Sensors
    AnalogChannel neckPot;
    AnalogChannel winchPot;
    // Operation Variables
    boolean teleopActive = false;
    boolean pickingUp = false;
    boolean pickingFront = true;

    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {

        // Xbox Controllers
        xboxDriver = new XBoxController(1); // USB port 1
        xboxOperator = new XBoxController(2); // USB port 2
        // Compressor
        airCompressor = new Compressor(VariableMap.DIO_COMPRESSOR,VariableMap.SPIKE_COMPRESSOR);
        airCompressor.start();
        // Drive Motors
        talonDriveRight = new Talon(/*cRIO slot*/1, VariableMap.PWM_DRIVERIGHT);
        talonDriveRight2 = new Talon(/*cRIO slot*/1, VariableMap.PWM_DRIVERIGHT2);
        talonDriveLeft = new Talon(/*cRIO slot*/1, VariableMap.PWM_DRIVELEFT);
        talonDriveLeft2 = new Talon(/*cRIO slot*/1, VariableMap.PWM_DRIVELEFT2);
        // Gear shifter
        shifter1 = new Solenoid(/*cRIO slot*/1, /*channel*/ VariableMap.SOLO_SHIFT_LOW);
        shifter2 = new Solenoid(/*cRIO slot*/1, /*channel*/ VariableMap.SOLO_SHIFT_HIGH);
        // Drive Encoders
//        driveRightE = new Encoder(VariableMap.DIO_ENCODER_RIGHT_A, VariableMap.DIO_ENCODER_RIGHT_B,  true, EncodingType.k4X);
//        driveLeftE = new Encoder(VariableMap.DIO_ENCODER_LEFT_A, VariableMap.DIO_ENCODER_LEFT_B, false, EncodingType.k4X);
//        driveLeftE.setDistancePerPulse(5);
//        driveLeftE.setMaxPeriod(5);
//        driveLeftE.setMinRate(0.001);
//        driveLeftE.stop();
//        driveRightE.start();
//        driveLeftE.start();
//        driveRightE.reset();
//        driveLeftE.reset();
        // Pickup Pivot
//        neck = new Victor(/*cRIO slot*/1, VariableMap.PWM_PICKUP_PIVOT);
        // Sensors
//        neckPot = new AnalogChannel(/*cRIO slot*/1, /*channel*/ 8);

        sf = new SensorFunctions(neckPot, winchPot/*, driveLeftE, driveRightE*/);
        df = new DriveFunctions(talonDriveRight, talonDriveRight2, talonDriveLeft, talonDriveLeft2,
                /*driveRightE, driveLeftE,*/ shifter1, shifter2, sf);
        shf = new ShootingFunctions(neck, winch, openJaw, closeJaw, fire,
                resetFire, sf);
        pf = new PickupFunctions(neck, roller, openJaw, closeJaw, sf);

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
//        shf.resetShootingSystem();
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
//        ballManipulator();
    }

    /**
     * This function is called at the start of test
     */
    public void testInit() {
        df.resetDriveSystem();
//        shf.resetShootingSystem();
    }

    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
        drive();
//        ballManipulator();
    }

    /**
     * Maps all the various drive functions to buttons on the driver's xbox
     * controller
     */
    public void drive() {
//        if (xboxDriver.getBtnBACK()) {
//            df.holdPosition(0);
//        }
//        if (xboxDriver.getBtnSTART()) {
//            df.notHoldingPosition();
//        }
//        if (!df.isHoldingPosition()) {
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
//        }
    }

    /**
     * Maps all the various shooting and pickup functions to buttons on the
     * driver's and the operator's xbox controller
     */
    public void ballManipulator() {
        // Pickup
        if (xboxDriver.getBtnA()) {
            pickingUp = true;
            pickingFront = true;
        } else if (xboxDriver.getBtnB()) {
            pickingUp = true;
            pickingFront = false;
        } else if (xboxDriver.getBtnY()) {
            pickingUp = false;
        }
        if (pickingUp) {
            if (pickingFront) {
                pf.frontPickUp();
            } else {
                pf.backPickUp();
            }
        } else {
            pf.turnRollerOff();
            pickingUp = false;
        }
        // Shooting
        if (!pickingUp) {
            shf.manualAim(xboxOperator.getLeftY());
        }
        // Manual Jaw Management
        if (xboxOperator.getBtnBACK()) {
            pf.setJawClose();
        } else if (xboxOperator.getBtnSTART()) {
            pf.setJawOpen();
        } else {
            pf.stopJawPistons();
        }
    }
}
