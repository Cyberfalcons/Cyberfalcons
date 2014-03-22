/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.CounterBase.EncodingType;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 *
 * @author Nathan Hicks
 */
public class CyberFalcons2014Main extends IterativeRobot {
// Initializing Varibles
    // Custom Functions for this robot

    VariableMap vm;
    DriveFunctions df;
    SensorFunctions sf;
    ShootingFunctions shf;
    PickupFunctions pf;
    SignalFunctions sigf;
    // PID Controller - in WPI library
    PIDController neckControl;
    PIDController driveRightPID;
    PIDController driveRight2PID;
    PIDController driveLeftPID;
    PIDController driveLeft2PID;
    // Xbox controllers - team 3710 wrapper class
    XBoxController xboxDriver;
    XBoxController xboxOperator;
    // Compressor - in WPI library
    Compressor airCompressor;
    // Drive motors - in WPI library
    Talon talonDriveRight;
    Talon talonDriveRight2;
    Talon talonDriveLeft;
    Talon talonDriveLeft2;
    // Gear shifter - in WPI library
    Solenoid shifter1;
    Solenoid shifter2;
    // Pickup Pivot Motor - in WPI library
    Victor neck;
    // Jaw Solenoids - in WPI library
    Solenoid openJaw;
    Solenoid closeJaw;
    // Shooting Solenoids - in WPI library
    Solenoid fire;
    Solenoid resetFire;
    // Shooter Winch - in WPI library
    Victor winch;
    // Pickup Roller - in WPI library
    Victor roller;
    // Sensors - in WPI library
    AnalogChannel neckPot;
    AnalogChannel winchPot;
    DigitalInput ultra;
    DigitalInput frontLimit;
    DigitalInput backLimit;
    DigitalInput winchLimit;
    Encoder driveLeftE;
    Encoder driveRightE;
    // Light Signals - in WPI library
    DigitalOutput signal1;
    DigitalOutput signal2;
    // Auto delay signal
    DigitalInput autoSig1;
    DigitalInput autoSig2;
    DigitalInput autoSig3;

    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
        vm = new VariableMap();
        // Xbox Controllers
        xboxDriver = new XBoxController(1); // USB port 1
        xboxOperator = new XBoxController(2); // USB port 2
        // Compressor
        airCompressor = new Compressor(VariableMap.DIO_COMPRESSOR, VariableMap.SPIKE_COMPRESSOR);
        airCompressor.start();
        // Drive Motors
        talonDriveRight = new Talon(/*cRIO slot*/1, VariableMap.PWM_DRIVERIGHT);
        talonDriveRight2 = new Talon(/*cRIO slot*/1, VariableMap.PWM_DRIVERIGHT2);
        talonDriveLeft = new Talon(/*cRIO slot*/1, VariableMap.PWM_DRIVELEFT);
        talonDriveLeft2 = new Talon(/*cRIO slot*/1, VariableMap.PWM_DRIVELEFT2);
        // Gear shifter
        shifter1 = new Solenoid(/*cRIO slot*/1, /*channel*/ VariableMap.SOLO_SHIFT_LOW);
        shifter2 = new Solenoid(/*cRIO slot*/1, /*channel*/ VariableMap.SOLO_SHIFT_HIGH);
        // Pickup Pivot
        neck = new Victor(/*cRIO slot*/1, VariableMap.PWM_PICKUP_PIVOT);
        // Pickup Roller
        roller = new Victor(/*cRIO slot*/1, VariableMap.PWM_ROLLER);
        // Jaw Solenoids
        openJaw = new Solenoid(/*cRIO slot*/1, VariableMap.SOLO_JAW_OPEN);
        closeJaw = new Solenoid(/*cRIO slot*/1, VariableMap.SOLO_JAW_CLOSE);
        // Shooter Solenoids
        fire = new Solenoid(/*cRIO slot*/1, VariableMap.SOLO_FIRE_SHOOTER);
        resetFire = new Solenoid(/*cRIO slot*/1, VariableMap.SOLO_RESET_SHOOTER);
        // Shooter winch
        winch = new Victor(/*cRIO slot*/1, VariableMap.PWM_SHOOTER_WINCH);
        // Sensors
        neckPot = new AnalogChannel(/*cRIO slot*/1, VariableMap.ANALOG_NECK_POT);
        winchPot = new AnalogChannel(/*cRIO slot*/1, VariableMap.ANALOG_WINCH_POT);
        ultra = new DigitalInput(/*cRIO slot*/1, VariableMap.DIO_ULTRA_SENSOR);
        frontLimit = new DigitalInput(/*cRIO slot*/1, VariableMap.DIO_FRONT_LIMIT);
        backLimit = new DigitalInput(/*cRIO slot*/1, VariableMap.DIO_BACK_LIMIT);
        winchLimit = new DigitalInput(/*cRIO slot*/1, VariableMap.DIO_WINCH_LIMIT);
        driveRightE = new Encoder(VariableMap.DIO_ENCODER_RIGHT_A, VariableMap.DIO_ENCODER_RIGHT_B, false, EncodingType.k4X);
        driveLeftE = new Encoder(VariableMap.DIO_ENCODER_LEFT_A, VariableMap.DIO_ENCODER_LEFT_B, true, EncodingType.k4X);
        // Encoder setup
        driveRightE.setPIDSourceParameter(PIDSource.PIDSourceParameter.kDistance);
        driveLeftE.setPIDSourceParameter(PIDSource.PIDSourceParameter.kDistance);
        driveRightE.setDistancePerPulse(VariableMap.ENCODER_DISTANCE_PER_PULSE);
        driveLeftE.setDistancePerPulse(VariableMap.ENCODER_DISTANCE_PER_PULSE);
        driveRightE.start();
        driveLeftE.start();
        driveRightE.reset();
        driveLeftE.reset();
        // Light Signals
        signal1 = new DigitalOutput(/*cRIO slot*/1, VariableMap.DIO_CATCH_SIGNAL);
        signal2 = new DigitalOutput(/*cRIO slot*/1, VariableMap.DIO_STANDBY_SIGNAL);
        // PID Controllers
        neckControl = new PIDController(-0.23, 0, -0.1, neckPot, neck);
        driveRightPID = new PIDController(1, 0, 0, driveRightE, talonDriveRight);
        driveRight2PID = new PIDController(1, 0, 0, driveRightE, talonDriveRight2);
        driveLeftPID = new PIDController(1, 0, 0, driveLeftE, talonDriveLeft);
        driveLeft2PID = new PIDController(1, 0, 0, driveLeftE, talonDriveLeft2);

        // Auto timer Signals
        autoSig1 = new DigitalInput(/*cRIO slot*/1, VariableMap.DIO_AUTO_SIG_1);
        autoSig2 = new DigitalInput(/*cRIO slot*/1, VariableMap.DIO_AUTO_SIG_2);
        autoSig3 = new DigitalInput(/*cRIO slot*/1, VariableMap.DIO_AUTO_SIG_3);


        // All the various function classes must be passed the controllers they interact with
        sf = new SensorFunctions(neckPot, winchPot, ultra, vm, neckControl, winchLimit, frontLimit, backLimit, autoSig1, autoSig2, autoSig3);
        df = new DriveFunctions(talonDriveRight, talonDriveRight2, talonDriveLeft, talonDriveLeft2,
                shifter1, shifter2, sf, vm);
        pf = new PickupFunctions(neck, roller, openJaw, closeJaw, neckPot, sf, vm, neckControl);
        shf = new ShootingFunctions(neck, winch, openJaw, closeJaw, fire,
                resetFire, neckPot, sf, vm, neckControl, pf);
        sigf = new SignalFunctions(signal1, signal2, vm, sf);

        // State Variable Initializations within the variable map class
        vm.pickingUp = false;
        vm.pickingFront = false;
        vm.autoCatching = false;
        vm.autoUpright = false;
        vm.teleopActive = false;
        vm.yClean = true;
        vm.limitClean = true;
        vm.dClean = true;
        vm.currentAutoShot = 0;
        vm.movedForward = false;
        vm.autoCycles = 0;
        vm.hasShot = false;

    }

    /**
     * This function is called at the start of autonomous
     */
    public void autonomousInit() {
        df.resetDriveSystem();
        shf.resetShootingSystem();
        vm.freeNeckValues();
        vm.pickingUp = false;
        vm.autoCatching = false;
        vm.autoUpright = false;
        vm.currentNeckSetPoint = sf.getNeckPot();
        neckControl.setSetpoint(vm.currentNeckSetPoint);
        neckControl.enable();
        vm.lightCounter = 0;
        vm.movedForward = false;
        vm.autoCycles = 0;
        vm.hasShot = false;
        df.shiftLow();
        if (sf.getAutonomousTimer() == 12) { // PID Controlled High Shot
            driveRightPID.enable();
            driveRight2PID.enable();
            driveLeftPID.enable();
            driveLeft2PID.enable();
        } else {
            driveRightPID.disable();
            driveRight2PID.disable();
            driveLeftPID.disable();
            driveLeft2PID.disable();
        }
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
        Watchdog.getInstance().feed(); // Tell watchdog we are running
        checkForLimitUpdates();
        if (sf.getAutonomousTimer() == 0) { // all switches off
            if (vm.autoCycles < 27) { // drive forward for a set amount of time
                df.setDriveLeft(-1);
                df.setDriveRight(-1);
            } else { // after driving, shoot
                df.setDriveLeft(0);
                df.setDriveRight(0);
                shf.autoShot(0);
                if (vm.fireCalled && !vm.hasShot) {
                    shf.fire();
                    if (shf.shotFired) {
                        vm.hasShot = true;
                    }
                }
            }
        } else if (sf.getAutonomousTimer() == 12) { // switch 1&2 on but 3 off
            int shotPositionDisplacement = 60; // needs to be swapped for distance after testing
            driveRightPID.setSetpoint(shotPositionDisplacement);
            driveRight2PID.setSetpoint(shotPositionDisplacement);
            driveLeftPID.setSetpoint(shotPositionDisplacement);
            driveLeft2PID.setSetpoint(shotPositionDisplacement);
            if (driveRightPID.onTarget() && driveLeftPID.onTarget()) { // after driving, shoot
                shf.autoShot(0);
                if (vm.fireCalled && !vm.hasShot) {
                    shf.fire();
                    if (shf.shotFired) {
                        vm.hasShot = true;
                    }
                }
            }
        } else if (sf.getAutonomousTimer() == 10) { // all switches on
            if (vm.autoCycles < 20) { // drive forward for a set amount of time
                df.setDriveLeft(-1);
                df.setDriveRight(-1);
            } else { // after driving, stop and do nothing
                df.setDriveLeft(0);
                df.setDriveRight(0);
            }
        } else {
            if (vm.autoCycles > sf.getAutonomousTimer() * 50) {
                vm.currentNeckSetPoint = vm.JAW_UPRIGHT_POS + 20;
                neckControl.setSetpoint(vm.currentNeckSetPoint);
                if (sf.getNeckPot() == vm.currentNeckSetPoint) {
                    pf.moveRollerReverse();
                }
            }
        }
        vm.autoCycles++;
    }

    /**
     * This function is called at the start of operator control
     */
    public void teleopInit() {
        df.resetDriveSystem();
        if (df.controlFlip) {
            df.toggleControlFlip();
        }
        shf.resetShootingSystem();
        vm.pickingUp = false;
        vm.autoCatching = false;
        vm.autoUpright = false;
        vm.currentNeckSetPoint = sf.getNeckPot();
        neckControl.setSetpoint(vm.currentNeckSetPoint);
        neckControl.disable();
        vm.lightCounter = 0;
        df.shiftHigh();

        driveRightPID.disable();
        driveRight2PID.disable();
        driveLeftPID.disable();
        driveLeft2PID.disable();
    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
        // Tell watchdog we are running
        Watchdog.getInstance().feed();
        checkForLimitUpdates();
        // Activate the robot by pushing start button
        if (xboxDriver.getBtnSTART()) {
            vm.teleopActive = true;
        }
        if (!vm.teleopActive) {
            // Only run if teleop has been active
            return;
        }

        drive();
        ballManipulator();
        sigf.updateLights();
    }

    /**
     * This function is called at the start of test
     */
    public void testInit() {
        df.resetDriveSystem();
        shf.resetShootingSystem();
        vm.freeNeckValues();
        vm.autoCatching = false;
        vm.pickingUp = false;
        vm.autoUpright = false;
        vm.currentNeckSetPoint = sf.getNeckPot();
        neckControl.setSetpoint(vm.currentNeckSetPoint);
        neckControl.disable();
        vm.lightCounter = 0;

        driveRightPID.disable();
        driveRight2PID.disable();
        driveLeftPID.disable();
        driveLeft2PID.disable();
    }

    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
        drive();
        ballManipulator();
        checkForLimitUpdates();
        sigf.updateLights();
        // Printout to console for debugging purposes
//        System.out.println("\tNeck Max: " + vm.FRONT_LOAD_POS
//                + "Neck Min" + vm.BACK_LOAD_POS + "\tNeck Upright: " + vm.JAW_UPRIGHT_POS
//                + "\tWinch Pot: " + winchPot.getValue() + "\tWinch Limit: " + winchLimit.get()
//                + "\tNeck Pot: " + neckPot.getValue() + "\tNeck Setpoint: " + vm.currentNeckSetPoint
//                + "\tFront Limit: " + frontLimit.get() + "\tBack Limit: " + backLimit.get());
        System.out.println(driveLeftE.getDistance() + "\t" + driveLeftE.getDirection() 
                + "\t" + driveRightE.getDistance() + "\t" + driveRightE.getDirection());

    }

    /**
     * Maps all the various drive functions to buttons on the driver's xbox
     * controller
     */
    public void drive() {
        if (xboxDriver.getBtnL3()) { // driver left thumb click switches forward driving direction
            df.toggleControlFlip();
        } else {
            df.controlFlipButtonReleased();
        }

        if (xboxDriver.getBtnLB()) { // driver left bumper shifts to low gear
            df.shiftLow();
        } else if (xboxDriver.getBtnRB()) { // driver right bumper shifts to high gear
            df.shiftHigh();
        } else {
            df.notShifting();
        }
        // set drive speed to the input from the left and right thumbsticks
        df.setDriveRight(xboxDriver.getRightY());
        df.setDriveLeft(xboxDriver.getLeftY());
//        }
    }

    public void checkForLimitUpdates() {
        if (!frontLimit.get()) {
            if (!vm.limitClean) {
                vm.updateNeckPotValues(sf, true);
            }
            vm.currentNeckSetPoint = vm.FRONT_LOAD_POS;
            vm.limitClean = false;
        } else if (backLimit.get()) {
            if (vm.limitClean) {
                vm.updateNeckPotValues(sf, false);
            }
            vm.currentNeckSetPoint = vm.BACK_LOAD_POS;
            vm.limitClean = false;
        } else {
            vm.limitClean = true;
        }
        if (!winchLimit.get()) {
            vm.updateWinchPotValues(sf);
            sf.setShotReadyValue(0);
        }
    }

    /**
     * Maps all the various shooting and pickup functions to buttons on the
     * driver's and the operator's xbox controller
     */
    public void ballManipulator() {
        vm.holdCalled = false;
        // Auto Upright enabled while driver right thumb click
        if (xboxDriver.getBtnR3()) {
            vm.autoUpright = true;
            vm.pickingUp = false;
            vm.autoShooting = false;
        } else {
            vm.autoUpright = false;
        }
        // Pickup Routines
        if (xboxDriver.getBtnA()) { // Driver button A starts the frontpickup
            vm.pickingUp = true;
            vm.pickingFront = true;
        } else if (xboxDriver.getBtnB()) { // Driver button B starts the rearpickup
            vm.pickingUp = true;
            vm.pickingFront = false;
        } else if (xboxDriver.getBtnY()) { // Driver button Y ends pickup
            vm.pickingUp = false;
            vm.autoShooting = false;
            vm.autoUpright = false;
            neckControl.disable();
        }
        if (vm.pickingUp) {
            if (vm.pickingFront) { // runs appropriate auto pickup when picking up
                pf.frontPickUp();
            } else {
                pf.backPickUp();
            }
        } else if (xboxDriver.getBtnX() || xboxOperator.getBtnLB()) { // rollers spit ball out when driver holds x or operator holds left bumper
            pf.moveRollerReverse();
        } else if (xboxOperator.getBtnRB()) { // roller pulls ball in while operator holds right bumper
            pf.moveRollerForward();
        } else { // shuts off rollers when not picking up
            pf.turnRollerOff();
        }
        // Shooting
        // Stay ready to shoot at last set power but be able to manually winch
        if (xboxOperator.getDpadX() > 0.5 && winchLimit.get() && !sf.shotReady()) {
            winch.set(-.5);
        } else {
            shf.readyShot();
        }
        if (!vm.pickingUp) { // stops the neck from moving away if picking up
            if (xboxDriver.getRightTrigger()) { // driver right trigger fires at current position
                shf.fire();
                vm.autoShooting = false;
            } else if (xboxDriver.getLeftTrigger()) { // driver left trigger fires at the current preset angle
                shf.autoShot(vm.currentAutoShot);
                vm.autoShooting = true;
            } else if (vm.autoUpright) {
                pf.moveToUprightPos();
                vm.autoShooting = false;
            } else {
                vm.fireCalled = false;
                vm.autoShooting = false;
            }
        } else {
            vm.autoShooting = false;
        }
        if (xboxOperator.getBtnA()) { // the current autoshot will be the first angle when the operator presses A
            vm.currentAutoShot = 0;
        } else if (xboxOperator.getBtnB()) { // the current autoshot will be the first angle when the operator presses B
            vm.currentAutoShot = 1;
        } else if (xboxOperator.getBtnX()) { // the current autoshot will be the first angle when the operator presses X
            vm.currentAutoShot = 2;
        }
        if (xboxOperator.getBtnBACK()) { // winch will wind to preset 1 when activated if back button is pressed by operator
            sf.setShotReadyValue(0);
        } else if (xboxOperator.getBtnSTART()) { // winch will wind to preset 2 when activated if start button is pressed by operator
            sf.setShotReadyValue(1);
        }
        // Auto Catch Toggle
        if (xboxOperator.getBtnY()) { // toggles autocatch when y button is pressed by operator
            if (vm.yClean) {
                vm.yClean = false;
                vm.autoCatching = !vm.autoCatching;
            }
        } else {
            vm.yClean = true;
        }
        // Jaw Management
        if (xboxOperator.getLeftTrigger() || vm.autoUpright || xboxDriver.getBtnA()) { // operator can close the jaw with left trigger
            pf.setJawClose();
            vm.autoCatching = false;
        } else if (xboxOperator.getRightTrigger() || xboxDriver.getBtnB()) { // operator can open the jaw with right trigger
            pf.setJawOpen();
            vm.autoCatching = false;
        } else if (vm.autoCatching) { // if autocatch is enabled the automatic catching program will run
            pf.autoCatch();
        } else if (!vm.pickingUp && !vm.fireCalled) {
            pf.stopJawPistons();
        }
        // Decide if neck pid should be active
        if ((vm.autoShooting || vm.autoUpright || vm.pickingUp) && !vm.holdCalled) {
            neckControl.enable();
        } else if (xboxOperator.getBtnR3()) {
            shf.manualAimOveride(xboxOperator.getLeftY()); // manually aim neck using left y axis when not shooting and ignore limits
        } else {
            neckControl.disable();
            shf.manualAim(xboxOperator.getLeftY()); // operator can manually aim neck using left y axis when not shooting
        }
        // Standby Light
        if (xboxOperator.getDpadX() < -0.5) {
            if (vm.dClean) {
                vm.lightCounter++;
                vm.dClean = false;
            }
        } else {
            vm.dClean = true;
        }
    }
}
