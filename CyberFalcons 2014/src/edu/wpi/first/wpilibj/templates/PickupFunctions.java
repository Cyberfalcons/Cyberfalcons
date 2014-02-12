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

    // CONSTRUCTOR METHOD 
    /**
     *
     * @param n = victor motor that tilts the neck back and forth
     * @param r = spike motor that spins the jaw roller
     * @param jo = piston that opens the jaw
     * @param jc = piston that closes the jaw
     * @param sf = sensor class that sends info to robot
     */
    public PickupFunctions(Victor n, Victor r, Solenoid jo, Solenoid jc,AnalogChannel np, SensorFunctions sf, VariableMap vMap) {
        neck = n;
        roller = r;
        jawOpen = jo;
        jawClose = jc;
        neckPot = np;
        sFunctions = sf;
        vm = vMap;
        neckControl = new PIDController(1,1,0,neckPot,neck);
    }

    public void frontPickUp() {
        if (!sFunctions.neckInFrontLoadPosition()) {
            neck.set(-.5);
        } else {
            neck.set(0); //hold position thanks to high gear ratio
            // Spin the rollers to swallow the ball
            moveRollerReverse();
            // Open the jaw
            setJawOpen();
        }
    }

    public void backPickUp() {
        stopJawPistons();
        if (!sFunctions.neckInBackLoadPosition()) {
            neck.set(.5);
        } else {
            neck.set(0); //hold position thanks to high gear ratio
            // Spin the rollers to swallow the ball
            moveRollerReverse();
            // Close the jaw 
            setJawClose();
        }
    }
    
    public void moveToUprightPos() {
        stopJawPistons();
        neckControl.setSetpoint(vm.JAW_UPRIGHT_POS);
        // activate autoneck control if not within 5 ticks of upright position
        if (Math.abs(sFunctions.getNeckPot() - vm.JAW_UPRIGHT_POS) > 5) {
            neckControl.enable();
        } else {
            freeNeck();
        }
    }
    
    public void freeNeck() {
        neckControl.disable();
    }

    public void moveRollerForward() {
        // Move the roller to eject the ball
        roller.set(0.5);
    }

    public void moveRollerReverse() {
        // Move the roller to suck in the ball
        roller.set(-0.5);
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

    public void manualCatch() {
        setJawClose();
    }

    public void setJawOpen() {
        jawOpen.set(true);
        jawClose.set(false);
    }

    public void setJawClose() {
        jawOpen.set(false);
        jawClose.set(true);
    }

    public void stopJawPistons() {
        jawOpen.set(false);
        jawClose.set(false);
    }
}
