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
 * @author Nathan Hicks
 */
public class ShootingFunctions {

    int DEADZONE;
    boolean shotFired = false;
    Victor neck;
    Relay winch;
    Solenoid openJaw;
    Solenoid closeJaw;
    Solenoid fire;
    Solenoid resetFire;
    int[] potShotValue;
    SensorFunctions sf;

    public ShootingFunctions(Victor n, Relay w, Solenoid o, Solenoid c, Solenoid f,
            Solenoid r, int[] shotValues, int dz, SensorFunctions sFunctions) {
        neck = n;
        winch = w;
        openJaw = o;
        closeJaw = c;
        fire = f;
        resetFire = r;
        potShotValue = shotValues;
        DEADZONE = dz;
        sf = sFunctions;
    }

    public void autoShot(int shot) {
        if (sf.getNeckPot() < potShotValue[shot]) {
            neck.set(1);// needs to be changed for PID
        } else if (sf.getNeckPot() > potShotValue[shot]) {
            neck.set(-1);// needs to be changed for PID
        } else {
            // set to hold position using PID
            fire();
        }
    }

    public void fire() {
        if (sf.shotReady()) {
            fire.set(true);
            resetFire.set(false);
            shotFired = true;
        } else {
            readyShot();
        }
    }

    public void readyShot() {
        if (shotFired) {
            resetFire.set(true);
            fire.set(false);
            shotFired = false;
        } else {
            resetFire.set(false);
            fire.set(false);
            if (sf.shotReady()) {
                winch.set(Relay.Value.kOff);
            } else {
                winch.set(Relay.Value.kOn);
            }
        }
    }

    public void manualAim(int direction) {
        if (direction > -DEADZONE && direction < DEADZONE) {
            neck.set(direction);
        } else {
            // set to hold position using PID 
        }
    }
    
    public void freeNeck() {
        neck.set(0);
    }
}
