/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.AnalogChannel;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Victor;

/**
 *
 * @author Nathan Hicks
 */
public class ShootingFunctions {

    boolean shotFired;
    int cyclesSinceLastShot;
    // PID Controller for autoshots
    PIDController neckController;
    // Pickup Pivot Motor
    Victor neck;
    // Neck Potentiometer
    AnalogChannel neckPot;
    // Kicker Winding Motor
    Victor winch;
    // Jaw Pistons
    Solenoid openJaw;
    Solenoid closeJaw;
    // Kicker Release Pistons
    Solenoid fire;
    Solenoid resetFire;
    SensorFunctions sf;
    VariableMap vm;
    PickupFunctions pf;

    /**
     *
     * @param n the victor that controls the neck
     * @param w the spike that controls the shooter winch
     * @param o the solenoid to open the jaw
     * @param c the solenoid to close the jaw
     * @param f the solenoid to fire the shooter
     * @param r the solenoid to reset the shooter
     * @param sFunctions - the class to get sensor data from
     * @param vMap the variable map class
     * @param nc the neck PID controller
     */
    public ShootingFunctions(Victor n, Victor w, Solenoid o, Solenoid c, Solenoid f,
            Solenoid r, AnalogChannel np, SensorFunctions sFunctions, VariableMap vMap,
            PIDController nc, PickupFunctions pFunctions) {
        neck = n;
        winch = w;
        openJaw = o;
        closeJaw = c;
        fire = f;
        resetFire = r;
        neckPot = np;
        sf = sFunctions;
        vm = vMap;
        shotFired = false;
        vm.fireCalled = false;
        neckController = nc;
        pf = pFunctions;
    }

    public void resetShootingSystem() {
        neck.set(0);
        winch.set(0);
        pf.setJawClose();
        fire.set(false);
        resetFire.set(false);
        shotFired = true;
        vm.fireCalled = false;
        cyclesSinceLastShot = 0;
    }

    public void autoShot(int shot) {
        vm.currentNeckSetPoint = vm.SHOT_POT_VALUES[shot];
        neckController.setSetpoint(vm.currentNeckSetPoint);
        if (sf.getNeckPot() == vm.SHOT_POT_VALUES[shot]) {
            holdNeckPosition();
            vm.fireCalled = true;
            if (vm.jawOpen) {
                vm.fireCalledCycles = 10000;
            }
            readyShot();
        }
    }

    public void fire() {
        if (sf.shotReady()) {
            if (!vm.fireCalled) {
                if (vm.jawOpen) {
                    vm.fireCalled = true;
                    vm.fireCalledCycles = 10000;
                } else {
                    vm.fireCalled = true;
                    vm.fireCalledCycles = 0;
                }
            } else if (vm.fireCalledCycles > 50) {
                fire.set(true);
                resetFire.set(false);
                shotFired = true;
                vm.fireCalled = false;
            } else {
                readyShot();
            }
        } else {
            vm.fireCalled = true;
            readyShot();
        }
    }

    public void readyShot() {
        if (shotFired) {
            resetFire.set(false);
            fire.set(false);
            shotFired = false;
            pf.setJawClose();
        } else {
            if (sf.shotReady()) {
                winch.set(0);
                resetFire.set(false);
                fire.set(false);
                cyclesSinceLastShot = 0;
            } else if (cyclesSinceLastShot > 20) {
                resetFire.set(true);
                fire.set(false);
                winch.set(-1);
            } else {
                cyclesSinceLastShot++;
            }
        }
        if (vm.fireCalled) {
            pf.setJawOpen();
            vm.fireCalledCycles++;
        }
    }

    /**
     * Manually aims the pickup/shooter
     *
     * @param direction - a value from -1 to 1
     */
    public void manualAim(double direction) {
        if (direction < -vm.DEADZONE || direction > vm.DEADZONE) {
            if (direction < 0 && !sf.neckPastMax()) { // only allows backwords movement when not past farthest position
                neck.set(direction);
            } else if (direction > 0 && !sf.neckPastMin()) { // only allows forward movement when not past closest position
                neck.set(direction);
            } else {
                holdNeckPosition();
            }
        } else if (!vm.autoUpright && !vm.autoShooting) {
            holdNeckPosition();
        }
    }
    
    /**
     * Manually aims the pickup/shooter.
     * Able to stall/kill itself.
     * @param direction - a value from -1 to 1
     */
    public void manualAimOveride(double direction) {
        if (direction < -vm.DEADZONE || direction > vm.DEADZONE) {
            if (direction < 0) {
                neck.set(direction);
            } else if (direction > 0) {
                neck.set(direction);
            } else {
                holdNeckPosition();
            }
        } else if (!vm.autoUpright && !vm.autoShooting) {
            holdNeckPosition();
        }
    }

    /**
     * Set's neck to hold current position using PID.
     */
    public void holdNeckPosition() {
        neckController.disable();
        neck.set(0);
        vm.holdCalled = true;
    }
}
