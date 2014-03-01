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
    DigitalInput frontLimit;
    DigitalInput backLimit;
    AnalogChannel neckPot;
    AnalogChannel winchPot;
    PIDController neckControl;

    /**
     *
     * @param np potentiometer for the neck
     * @param wp potentiometer for the winch
     * @param u the ultrasound sensor
     * @param vMap the variable map class
     * @param nc the PIDController for the neck
     * @param wl the winch limit switch
     * @param fl the front limit switch
     * @param bl the back limit switch
     */
    public SensorFunctions(AnalogChannel np, AnalogChannel wp, DigitalInput u,
            VariableMap vMap, PIDController nc, DigitalInput wl, DigitalInput fl, DigitalInput bl) {
        neckPot = np;
        winchPot = wp;
        ardUltra = u;
        vm = vMap;
        shotReadyValue = 0;
        neckControl = nc;
        winchLimit = wl;
        frontLimit = fl;
        backLimit = bl;
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

    /**
     * Returns true if neck cannot move back any farther
     *
     * @return
     */
    public boolean neckPastMin() {
        return backLimit.get();
    }

    /**
     * Returns false if neck cannot move forward any farther
     *
     * @return
     */
    public boolean neckPastMax() {
        return frontLimit.get();
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
        return /*winchPot.getValue() > vm.SHOT_POWER_VALUES[shotReadyValue] ||*/ !winchLimit.get();
    }
}
