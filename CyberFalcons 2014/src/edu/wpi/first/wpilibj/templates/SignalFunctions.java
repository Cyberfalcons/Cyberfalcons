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
    DigitalOutput signal1;
    DigitalOutput signal2;

    public SignalFunctions(DigitalOutput cs, DigitalOutput ss, VariableMap vMap, SensorFunctions sFunctions) {
        signal1 = cs;
        signal2 = ss;
        vm = vMap;
        sf = sFunctions;
    }

    public void updateLights() {
        if (vm.autoCatching) { // ready to catch(green)
            signal1.set(true);
            signal2.set(true);
        } else {
            switch (vm.lightCounter % 3) {
                case 0:
                    // do not pass (alliance colour)
                    signal1.set(false);
                    signal2.set(true);
                    break;
                case 1:
                    // idle(yellow)
                    signal1.set(true);
                    signal2.set(false);
                    break;
                case 2:
                    // ready to catch (green)
                    signal1.set(true);
                    signal2.set(true);
                    break;
            }
        }
    }
}
