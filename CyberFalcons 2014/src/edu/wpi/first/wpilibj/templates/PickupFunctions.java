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
 * @author Rafael & Nathan Hicks
 */
public class PickupFunctions {

    // PID Controller for autoUpright
    PIDController neckControl;
    // Pickup Pivot Motor (Victor)
    Victor neck;
    // Jaw Roller Motor (Spike)
    Victor roller;
    // Jaw Mouth Pistons (Solenoids)
    Solenoid jawOpen;
    Solenoid jawClose;
    // Neck Potentiometer
    AnalogChannel neckPot;
    // Sensor Functions
    SensorFunctions sFunctions;
    VariableMap vm;
    int pickupEndTimer;

    // CONSTRUCTOR METHOD 
    /**
     *
     * @param n victor motor that tilts the neck back and forth
     * @param r spike motor that spins the jaw roller
     * @param jo piston that opens the jaw
     * @param jc piston that closes the jaw
     * @param sf sensor class that sends info to robot
     * @param vMap the variable map class
     * @param nc the neck PID controller
     */
    public PickupFunctions(Victor n, Victor r, Solenoid jo, Solenoid jc,
            AnalogChannel np, SensorFunctions sf, VariableMap vMap, PIDController nc) {
        neck = n;
        roller = r;
        jawOpen = jo;
        jawClose = jc;
        neckPot = np;
        sFunctions = sf;
        vm = vMap;
        neckControl = nc;
    }

    /**
     * Runs a pickup routine to get the ball from in front
     */
    public void frontPickUp() {
        vm.currentNeckSetPoint = vm.FRONT_LOAD_POS;
        neckControl.setSetpoint(vm.currentNeckSetPoint);
        // Spin the rollers to swallow the ball
        moveRollerForward();
        /* keep picking up until the ball is detected by the ultra sound
         * after which, roll only enough to move the ball to a good position
         * and finaly close on the ball and stop the pickup routine
         */
//        if (sFunctions.isBallOnUltraSound()) { 
//            if (pickupEndTimer > 25) {
//                vm.autoCatching = true;
//                pickupEndTimer = 0;
//                turnRollerOff();
//                vm.pickingUp = false;
//            } else {
//                pickupEndTimer++;
//            }
//        }
    }

    /**
     * Runs a pickup routine to get the ball from behind
     */
    public void backPickUp() {
        vm.currentNeckSetPoint = vm.BACK_LOAD_POS;
        neckControl.setSetpoint(vm.currentNeckSetPoint);
        // Spin the rollers to swallow the ball
        moveRollerReverse();
        /* keep picking up until the ball is detected by the ultra sound
         * after which, roll only enough to move the ball to a good position
         * and finaly close on the ball and stop the pickup routine
         */
//        if (sFunctions.isBallOnUltraSound()) {
//            if (pickupEndTimer > 30) {
//                pickupEndTimer = 0;
//                turnRollerOff();
//                vm.pickingUp = false;
//            } else {
//                pickupEndTimer++;
//            }
//        }
    }

    /**
     * Uses the PID controller that is enabled/disabled in the main class to
     * move to an upright position
     */
    public void moveToUprightPos() {
        vm.autoUpright = true;
        stopJawPistons();
        neckControl.setSetpoint(vm.JAW_UPRIGHT_POS);
        // activate autoneck control if not within 5 ticks of upright position
        if (!(sFunctions.getNeckPot() == vm.JAW_UPRIGHT_POS)) {
            vm.autoUpright = true;
        } else {
            vm.autoUpright = false;
        }
    }

    public void moveRollerForward() {
        // Move the roller to eject the ball
        roller.set(1);
    }

    public void moveRollerReverse() {
        // Move the roller to suck in the ball
        roller.set(-1);

    }

    public void turnRollerOff() {
        roller.set(0);
    }

    public void autoCatch() {
        if (sFunctions.isBallOnUltraSound()) {
            setJawClose();
            vm.autoCatching = false;
        } else {
            setJawOpen();
        }
    }

    public void setJawOpen() {
        jawOpen.set(true);
        jawClose.set(false);
        vm.jawOpen = true;
    }

    public void setJawClose() {
        jawOpen.set(false);
        jawClose.set(true);
        vm.jawOpen = false;
        vm.fireCalledCycles = 0;
    }

    public void stopJawPistons() {
        jawOpen.set(false);
        jawClose.set(false);
    }
}
