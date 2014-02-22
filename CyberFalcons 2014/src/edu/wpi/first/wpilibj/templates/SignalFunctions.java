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
    SensorFunctions sf;
    DigitalOutput catchSignal;
    DigitalOutput standbySignal;

    public SignalFunctions(DigitalOutput cs, DigitalOutput ss,VariableMap vMap, SensorFunctions sFunctions) {
        catchSignal = cs;
        standbySignal = ss;
        vm = vMap;
        sf = sFunctions;
    }
    
    public void updateLights() {
        catchSignal.set(vm.autoCatching);
        if (!vm.autoCatching && vm.standby && sf.shotReady()) {
            standbySignal.set(true);
        } else {
            standbySignal.set(false);
        }
    }
}
