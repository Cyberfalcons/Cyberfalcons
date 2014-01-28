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
    int shotReadyValue;
    
    DigitalInput ardUltra;
    AnalogChannel neckPot;
    AnalogChannel winchPot;
    Encoder driveLeftE;
    Encoder driveRightE;
    AnalogChannel examplePot; // is example only
    Encoder exampleEnc; // is example only
    
    /**
     * 
     * @param np - potentiometer for the neck
     * @param wp - potentiometer for the winch
     * @param srv - shot ready value
     * @param dl - left drive encoder
     * @param dr - right drive encoder
     */
    public SensorFunctions(AnalogChannel np, AnalogChannel wp, Encoder dl, Encoder dr) {
//        ardUltra = new DigitalInput(1,9001); // REPLACE WITH MAP THINGS
//        examplePot = new AnalogChannel(1,9001); // is example only
//        exampleEnc = new Encoder(9001,9001); // is example only
        neckPot = np;
        winchPot = wp;
        shotReadyValue = VariableMap.SHOT_READY_VALUE;
        driveLeftE = dl;
        driveRightE = dr;
    }
    
    public boolean isBallOnUltraSound() {
        return ardUltra.get();
    }
    
    public int getPot(int potChan) {
        AnalogChannel pot = new AnalogChannel(1,potChan);
        return pot.getValue();
    }
    
    public int getNeckPot() {
        return neckPot.getValue();
    }
    
    public boolean shotReady() {
        return winchPot.getValue() > shotReadyValue;
    }
    
    public double getLeftDriveEncoder() {
        return driveLeftE.getDistance();
    }
    
    public double getRightDriveEncoder() {
        return driveRightE.getDistance();
    }
    
    public void zeroDriveEncoders() {
        driveLeftE.reset();
        driveRightE.reset();
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
