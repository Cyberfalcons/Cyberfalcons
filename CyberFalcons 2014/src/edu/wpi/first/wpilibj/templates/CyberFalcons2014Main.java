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
    // PID Controller
    PIDController neckControl;
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
    DigitalInput winchLimit;
    // Operation Variables
    boolean teleopActive = false;
    boolean yClean = true;
    boolean limitClean = true;
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
        winchLimit = new DigitalInput(/*cRIO slot*/1, VariableMap.DIO_WINCH_LIMIT);
        // PID Controllers
        neckControl = new PIDController(-0.23, 0, -0.1, neckPot, neck);

        sf = new SensorFunctions(neckPot, winchPot, ultra, vm, neckControl, winchLimit/*, driveLeftE, driveRightE*/);
        df = new DriveFunctions(talonDriveRight, talonDriveRight2, talonDriveLeft, talonDriveLeft2,
                /*driveRightE, driveLeftE,*/ shifter1, shifter2, sf, vm);
        pf = new PickupFunctions(neck, roller, openJaw, closeJaw, neckPot, sf, vm, neckControl);
        shf = new ShootingFunctions(neck, winch, openJaw, closeJaw, fire,
                resetFire, neckPot, sf, vm, neckControl, pf);

        // Variable Initializations
        vm.pickingUp = false;
        vm.pickingFront = false;
        vm.autoCatching = false;
        vm.autoUpright = false;

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
        vm.pickingUp = false;
        vm.freeNeckValues();
        vm.autoCatching = false;
        vm.autoUpright = false;
        vm.currentNeckSetPoint = sf.getNeckPot();
        neckControl.setSetpoint(vm.currentNeckSetPoint);
        neckControl.enable();
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
        vm.pickingUp = false;
        vm.autoUpright = false;
        vm.currentNeckSetPoint = sf.getNeckPot();
        neckControl.setSetpoint(vm.currentNeckSetPoint);
        neckControl.disable();
    }

    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
        drive();
        ballManipulator();
        checkForLimitUpdates();
//        if (xboxDriver.getRightTrigger()) {
//            fire.set(true);
//            resetFire.set(false);
//        } else if (fire.get()) {
//            fire.set(false);
//            resetFire.set(true);
//        } else {
//            fire.set(false);
//            resetFire.set(false);
//        }
        System.out.println("\tNeck Max: " + vm.FRONT_LOAD_POS
                + "Neck Min" + vm.BACK_LOAD_POS + "\tNeck Upright: " + vm.JAW_UPRIGHT_POS
                + "\tWinch Pot: " + winchPot.getValue() + "\tWinch Limit: " + winchLimit.get()
                + "\tNeck Pot: " + neckPot.getValue() + "\tNeck Setpoint: " + vm.currentNeckSetPoint);
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

        if (xboxDriver.getBtnLB()) { // driver right bumper shifts to low gear
            df.shiftLow();
        } else if (xboxDriver.getBtnRB()) { // driver left bumper shifts to high gear
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
            if (limitClean) {
                vm.updateNeckPotValues(sf, true);
            }
            vm.currentNeckSetPoint = vm.FRONT_LOAD_POS;
            limitClean = false;
        } else if (!backLimit.get()) {
            if (limitClean) {
                vm.updateNeckPotValues(sf, false);
            }
            vm.currentNeckSetPoint = vm.BACK_LOAD_POS;
            limitClean = false;
        } else {
            limitClean = true;
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
        // Pickup
        if (xboxDriver.getBtnA()) { // Driver button A starts the frontpickup
            vm.pickingUp = true;
            vm.pickingFront = true;
            pf.setJawClose();
        } else if (xboxDriver.getBtnB()) { // Driver button B starts the rearpickup
            vm.pickingUp = true;
            vm.pickingFront = false;
            pf.setJawOpen();
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
        } else if (xboxDriver.getBtnX() || xboxOperator.getBtnLB() || vm.fireCalled) { // rollers spit ball out when driver holds x or operator holds left bumper
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
                shf.autoShot(currentAutoShot);
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
        if (xboxOperator.getBtnBACK() || vm.autoUpright) { // operator can close the jaw with back button
            pf.setJawClose();
            vm.autoCatching = false;
        } else if (xboxOperator.getBtnSTART()) { // operator can open the jaw with start button
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
        } else {
            neckControl.disable();
            shf.manualAim(xboxOperator.getLeftY()); // operator can manually aim neck using left y axis when not shooting
        }
    }
}
