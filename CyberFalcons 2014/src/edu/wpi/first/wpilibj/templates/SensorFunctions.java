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
    // Auto delay signal
    DigitalInput autoSig1;
    DigitalInput autoSig2;
    DigitalInput autoSig3;

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
     * @param as1 the first signal for the autonomous timer
     * @param as2 the second signal for the autonomous timer
     * @param as3 the third signal for the autonomous timer
     */
    public SensorFunctions(AnalogChannel np, AnalogChannel wp, DigitalInput u,
            VariableMap vMap, PIDController nc, DigitalInput wl, DigitalInput fl,
            DigitalInput bl, DigitalInput as1, DigitalInput as2, DigitalInput as3) {
        neckPot = np;
        winchPot = wp;
        ardUltra = u;
        vm = vMap;
        shotReadyValue = 0;
        neckControl = nc;
        winchLimit = wl;
        frontLimit = fl;
        backLimit = bl;
        autoSig1 = as1;
        autoSig2 = as2;
        autoSig3 = as3;
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
    
    /**
     * Uses 3 binary inputs to determine what the total wait time should be in
     * autonomous.
     * 
     * @return the time to wait in seconds
     */
    public int getAutonomousTimer() {
        // signal 1 is 4, signal 2 is 2, signal 1 is 1
        if (autoSig1.get()) {
            if (autoSig2.get()) {
                if (autoSig3.get()) {
                    return 10;
                } else {
                    return 8;
                }
            } else {
                if (autoSig3.get()) {
                    return 7;
                } else {
                    return 6;
                }
            }
        } else {
            if (autoSig2.get()) {
                if (autoSig3.get()) {
                    return 4;
                } else {
                    return 3;
                }
            } else {
                if (autoSig3.get()) {
                    return 2;
                } else {
                    return 0;
                }
            }
        }
    }
    
}
