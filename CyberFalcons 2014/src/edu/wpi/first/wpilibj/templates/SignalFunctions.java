/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.DigitalOutput;

/**
 *
 * @author Nathan
 */
public class SignalFunctions {

    VariableMap vm;
    DigitalOutput catchSignal;

    public SignalFunctions(DigitalOutput cs, VariableMap vMap) {
        catchSignal = cs;
        vm = vMap;
    }
    
    public void updateLights() {
        catchSignal.set(vm.autoCatching);
    }
}
