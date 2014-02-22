/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.AnalogChannel;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.PIDController;

/**
 *
 * @author Alex & Nathan Hicks
 */
public class SensorFunctions {

    int shotReadyValue;
    VariableMap vm;
    DigitalInput ardUltra;
    DigitalInput winchLimit;
    AnalogChannel neckPot;
    AnalogChannel winchPot;
//    Encoder driveLeftE;
//    Encoder driveRightE;
    PIDController neckControl;

    /**
     *
     * @param np potentiometer for the neck
     * @param wp potentiometer for the winch
     * @param u the ultrasound sensor
     * @param vMap the variable map class
     * @param nc the PIDController for the neck
     */
    public SensorFunctions(AnalogChannel np, AnalogChannel wp, DigitalInput u,
            VariableMap vMap, PIDController nc, DigitalInput wl/*, Encoder dl, Encoder dr*/) {
        neckPot = np;
        winchPot = wp;
        ardUltra = u;
        vm = vMap;
        shotReadyValue = 0;
        neckControl = nc;
        winchLimit = wl;
//        driveLeftE = dl;
//        driveRightE = dr;
    }

    public boolean isBallOnUltraSound() {
        return ardUltra.get();
    }

    public int getNeckPot() {
        return neckPot.getValue();
    }

    public int getWinchPot() {
        return winchPot.getValue();
    }

    public boolean neckInFrontLoadPosition() {
        if (getNeckPot() >= vm.FRONT_LOAD_POS - 5) {
            return true;
        }
        return false;
    }

    public boolean neckInBackLoadPosition() {
        if (getNeckPot() <= vm.BACK_LOAD_POS + 5) {
            return true;
        }
        return false;
    }

    /**
     * Returns true if neck cannot move back any farther
     *
     * @return
     */
    public boolean neckPastMin() {
        return (getNeckPot() <= vm.BACK_LOAD_POS);
    }

    /**
     * Returns false if neck cannot move forward any farther
     *
     * @return
     */
    public boolean neckPastMax() {
        return (getNeckPot() >= vm.FRONT_LOAD_POS);
    }

    /**
     * Allows the selection of a preset power for shooting
     *
     * @param value - the preset value to choose
     */
    public void setShotReadyValue(int value) {
        shotReadyValue = value;
    }

    public boolean shotReady() {
        return winchPot.getValue() > vm.SHOT_POWER_VALUES[shotReadyValue] || !winchLimit.get(); 
    }
//    public double getLeftDriveEncoder() {
//        return driveLeftE.getDistance();
//    }
//    
//    public double getRightDriveEncoder() {
//        return driveRightE.getDistance();
//    }
//    
//    public void zeroDriveEncoders() {
//        driveLeftE.reset();
//        driveRightE.reset();
//    }
}
