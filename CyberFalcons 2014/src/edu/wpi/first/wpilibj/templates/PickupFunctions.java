/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Victor;

/**
 *
 * @author Rafael
 */
public class PickupFunctions {
    
    // Neck Motor (Victor)
    Victor neck;
    // Jaw Roller Motor (Spike)
    Relay roller; 
    // Jaw Mouth Pistons (Solenoids)
    Solenoid jawOpen;
    Solenoid jawClose;
    // Sensor Functions
    SensorFunctions sFunctions;
    // Operation Variables
    int frontLoadPos;
    int backLoadPos;
    
    // CONSTRUCTOR METHOD 
    /**
     * 
     * @param n = victor motor that tilts the neck back and forth
     * @param r = spike motor that spins the jaw roller 
     * @param jo = piston that opens the jaw 
     * @param jc = piston that closes the jaw
     * @param sf = sensor class that sends info to robot
     * @param fPos = integer that defines the front load position
     * @param bPos = integer that defines the back load position
     */
    public PickupFunctions(Victor n, Relay r, Solenoid jo, Solenoid jc, SensorFunctions sf, int fPos, int bPos) {
        neck = n;           
        roller = r;         
        jawOpen = jo;       
        jawClose = jc;
        sFunctions = sf;
        frontLoadPos = fPos;
        backLoadPos = bPos; 
    }
    
    public void frontPickUp() {
        if (sFunctions.getNeckPot() < frontLoadPos) {
            neck.set(1);
        }
        else {
            neck.set(0); // Set to hold position using PID
            // Spin the rollers to swallow the ball
            moveRollerReverse();
            // Open the jaw
            setJawOpen();
        }
    }
    
    public void backPickUp() {
        stopJawPistons();
        if (sFunctions.getNeckPot() > backLoadPos) {
            neck.set(-1);
        }
        else {
            neck.set(0); //Set to the hold position using PID
            // Spin the rollers to swallow the ball
            moveRollerReverse();
            // Close the jaw 
            setJawClose();
        }
    }
    
    public void moveRollerForward() {
        // Set the direction for the roller to eject the ball and turn it on
        roller.setDirection(Relay.Direction.kReverse);
        roller.set(Relay.Value.kOn);
    }
    
    public void moveRollerReverse() {
        // Set the direction for the roller to suck in the ball and turn it on
        roller.setDirection(Relay.Direction.kReverse);
        roller.set(Relay.Value.kOn);
    }
    
    public void turnRollerOff() {
        roller.set(Relay.Value.kOff);
    }
    
    public void autoCatch() {
        if (sFunctions.isBallOnUltraSound()) {
            setJawClose();
        }
        else {
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
