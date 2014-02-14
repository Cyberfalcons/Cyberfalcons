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
 *
 * @author Nathan Hicks
 */
public class CyberFalcons2014Main extends IterativeRobot {

    // Functions
    VariableMap vm;
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
    Victor winch;
    // Pickup Roller
    Victor roller;
    // Sensors
    AnalogChannel neckPot;
    AnalogChannel winchPot;
    DigitalInput ultra;
    DigitalInput frontLimit;
    DigitalInput backLimit;
    // Operation Variables
    boolean teleopActive = false;
    boolean pickingUp = false;
    boolean pickingFront = true;
    boolean yClean = true;
    boolean autoUpright = false;
    boolean r3Clean = true;
    int currentAutoShot = 0;

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
        airCompressor = new Compressor(vm.DIO_COMPRESSOR, VariableMap.SPIKE_COMPRESSOR);
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

        sf = new SensorFunctions(neckPot, winchPot, ultra, vm/*, driveLeftE, driveRightE*/);
        df = new DriveFunctions(talonDriveRight, talonDriveRight2, talonDriveLeft, talonDriveLeft2,
                /*driveRightE, driveLeftE,*/ shifter1, shifter2, sf, vm);
        shf = new ShootingFunctions(neck, winch, openJaw, closeJaw, fire,
                resetFire, neckPot, sf, vm);
        pf = new PickupFunctions(neck, roller, openJaw, closeJaw, neckPot, sf, vm);

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
        shf.resetShootingSystem();
        pickingUp = false;
        vm.freeNeckValues();
        vm.autoCatching = false;
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
        ballManipulator();
    }

    /**
     * This function is called at the start of test
     */
    public void testInit() {
        df.resetDriveSystem();
        shf.resetShootingSystem();
        vm.freeNeckValues();
        vm.autoCatching = false;
        pickingUp = false;
    }

    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
        drive();
        ballManipulator();
        checkForLimitUpdates();
        winch.set(-Math.abs(xboxOperator.getDpadX()));
        System.out.println("Neck Pot: " + neckPot.getValue() + "\tMax Pot: " + vm.FRONT_LOAD_POS
                + "\tFront Lim: " + frontLimit.get() + "\tBack Lim: " + backLimit.get() + "\tMin Pot: " + vm.BACK_LOAD_POS + "\tUpright Pot: " + vm.JAW_UPRIGHT_POS);
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
        if (xboxDriver.getBtnL3()) { // driver left thumb click switches forward driving direction
            df.toggleControlFlip();
        } else {
            df.controlFlipButtonReleased();
        }

        if (xboxDriver.getBtnRB()) { // driver right bumper shifts to low gear
            df.shiftLow();
        } else if (xboxDriver.getBtnLB()) { // driver left bumper shifts to high gear
            df.shiftHigh();
        } else {
            df.notShifting();
        }

        df.setDriveRight(xboxDriver.getRightY());
        df.setDriveLeft(xboxDriver.getLeftY());
//        }
    }

    public void checkForLimitUpdates() {
        if (!frontLimit.get()) {
            vm.updateNeckPotValues(sf, true);
        }
        if (!backLimit.get()) {
            vm.updateNeckPotValues(sf, false);
        }
    }

    /**
     * Maps all the various shooting and pickup functions to buttons on the
     * driver's and the operator's xbox controller
     */
    public void ballManipulator() {
        // Auto Upright toggles on driver right thumb click
        if (xboxDriver.getBtnR3()) {
            if (r3Clean) {
                r3Clean = false;
                autoUpright = !autoUpright;
            }
        } else {
            r3Clean = true;
        }
        if (autoUpright) {
            pf.moveToUprightPos();
        } else {
            pf.freeNeck();
        }
        // Pickup
        if (xboxDriver.getBtnA()) { // Driver button A starts the frontpickup
            pickingUp = true;
            pickingFront = true;
        } else if (xboxDriver.getBtnB()) { // Driver button B starts the rearpickup
            pickingUp = true;
            pickingFront = false;
        } else if (xboxDriver.getBtnY()) { // Driver button Y ends pickup
            pickingUp = false;
        }
        if (pickingUp) {
            if (pickingFront) { // runs appropriate auto pickup when picking up
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
        if (!pickingUp) { // stops the neck from moving away if picking up
            if (xboxDriver.getRightTrigger()) { // driver right trigger fires at current position
                shf.fire();
            } else if (xboxDriver.getLeftTrigger()) { // driver left trigger fires at the current preset angle
                shf.autoShot(currentAutoShot);
            } else {
                shf.manualAim(xboxOperator.getLeftY()); // operator can manually aim neck using left y axis when not shooting
            }
        }
        if (xboxOperator.getBtnA()) { // the current autoshot will be the first angle when the operator presses A
            currentAutoShot = 0;
        } else if (xboxOperator.getBtnB()) { // the current autoshot will be the first angle when the operator presses B
            currentAutoShot = 1;
        } else if (xboxOperator.getBtnX()) { // the current autoshot will be the first angle when the operator presses X
            currentAutoShot = 2;
        }
        if (xboxOperator.getLeftTrigger()) { // winch will wind to preset 1 when activated if left trigger is pressed by operator
            sf.setShotReadyValue(0);
        } else if (xboxOperator.getRightTrigger()) { // winch will wind to preset 1 when activated if right trigger is pressed by operator
            sf.setShotReadyValue(1);
        }
        // Auto Catch Toggle
        if (xboxOperator.getBtnY()) { // toggles autocatch when y button is pressed by operator
            if (yClean) {
                yClean = false;
                vm.autoCatching = !vm.autoCatching;
            }
        } else {
            yClean = true;
        }
        // Jaw Management
        if (xboxOperator.getBtnBACK()) { // operator can close the jaw with back button
            pf.setJawClose();
            vm.autoCatching = false;
        } else if (xboxOperator.getBtnSTART()) { // operator can open the jaw with start button
            pf.setJawOpen();
            vm.autoCatching = false;
        } else if (vm.autoCatching) { // if autocatch is enabled the automatic catching program will run
            pf.autoCatch();
        } else {
            pf.stopJawPistons();
        }
        // Stay ready to shoot at last set power
//        shf.readyShot();
    }
}
