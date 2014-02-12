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
    boolean fireCalled;
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

    /**
     *
     * @param n - the victor that controls the neck
     * @param w - the spike that controls the shooter winch
     * @param o - the solenoid to open the jaw
     * @param c - the solenoid to close the jaw
     * @param f - the solenoid to fire the shooter
     * @param r - the solenoid to reset the shooter
     * @param sFunctions - the class to get sensor data from
     */
    public ShootingFunctions(Victor n, Victor w, Solenoid o, Solenoid c, Solenoid f,
            Solenoid r, AnalogChannel np, SensorFunctions sFunctions, VariableMap vMap) {
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
        fireCalled = false;
        
        neckController = new PIDController(1,1,0, neckPot, neck);
    }

    public void resetShootingSystem() {
        neck.set(0);
        winch.set(0);
        openJaw.set(false);
        closeJaw.set(false);
        fire.set(false);
        resetFire.set(false);
        shotFired = false;
        fireCalled = false;
    }

    public void autoShot(int shot) {
        if (sf.getNeckPot() < vm.SHOT_POT_VALUES[shot]) {
            neck.set(1);// needs to be changed for PID
        } else if (sf.getNeckPot() > vm.SHOT_POT_VALUES[shot]) {
            neck.set(-1);// needs to be changed for PID
        } else {
            holdNeckPosition();
            fireCalled = true;
            readyShot();
            fire();
        }
    }

    public void fire() {
        if (sf.shotReady()) {
            fire.set(true);
            resetFire.set(false);
            shotFired = true;
            fireCalled = false;
        } else {
            fireCalled = true;
            readyShot();
        }
    }

    public void readyShot() {
        if (shotFired) {
            resetFire.set(true);
            fire.set(false);
            shotFired = false;
            openJaw.set(false);
            closeJaw.set(false);
        } else {
            resetFire.set(false);
            fire.set(false);
            if (sf.shotReady()) {
                winch.set(0);
            } else {
                winch.set(1);
            }
        }
        if (fireCalled) {
            openJaw.set(true);
            closeJaw.set(false);
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
        } else {
            holdNeckPosition();
        }
    }

    /**
     * Set's neck to 0. Because of high gear ratio, neck will hold position.
     */
    public void holdNeckPosition() {
        neck.set(0);
    }
}
