/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.AnalogChannel;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;

/**
 *
 * @author Alex
 */
public class SensorFunctions {
    
    final int exampleVal = 500;
    
    DigitalInput ardUltra;
    
    AnalogChannel examplePot; // is example only
    Encoder exampleEnc; // is example only
    
    public SensorFunctions() {
        ardUltra = new DigitalInput(1,9001); // REPLACE WITH MAP THINGS
        examplePot = new AnalogChannel(1,9001); // is example only
        exampleEnc = new Encoder(9001,9001); // is example only
    }
    
    public boolean isBallOnUltraSound() {
        return ardUltra.get();
    }
    
    public int getPot(int potChan) {
        AnalogChannel pot = new AnalogChannel(1,potChan);
        return pot.getValue();
    }
    
    // example of pot boolean
    public boolean pastLimit() {
        return examplePot.getValue() > exampleVal;
    }
    
    // example of enc boolean
    public boolean traveledMax() {
        return exampleEnc.getDistance() > exampleVal;
    }
}
