package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.*;

/**
 * Written by FIRST Robotics Team 3710. This is a wrapper class to allow easy
 * use of an XBox360 controller.
 *
 * @author cyberfalcons
 */
public class XBoxController extends Joystick {

    /**
     * CONSTRUCTOR
     */
    public XBoxController(int usbPort) {
        super(usbPort);
    }

    /**
     * ****** BUTTONS ***********
     */
    public boolean getBtnA() {
        return getRawButton(1);
    }

    public boolean getBtnB() {
        return getRawButton(2);
    }

    public boolean getBtnX() {
        return getRawButton(3);
    }

    public boolean getBtnY() {
        return getRawButton(4);
    }

    public boolean getBtnLB() {
        return getRawButton(5);
    }

    public boolean getBtnRB() {
        return getRawButton(6);
    }

    public boolean getBtnBACK() {
        return getRawButton(7);
    }

    public boolean getBtnSTART() {
        return getRawButton(8);
    }

    /**
     * Left Stick Click.
     */
    public boolean getBtnL3() {
        return getRawButton(9);
    }

    /**
     * Right Stick Click
     */
    public boolean getBtnR3() {
        return getRawButton(10);
    }
    
    /**
     * Right Trigger converted to a button.
     */
    public boolean getRightTrigger() {
        if (getTriggers() < -.1) {
            return true;
        }
        return false;
    }

    /**
     * Left Trigger converted to a button.
     */
    public boolean getLeftTrigger() {
        if (getTriggers() > .1) {
            return true;
        }
        return false;
    }

    /**
     * ******** AXIS **************
     */
    /**
     * Left thumb stick, X-axis
     */
    public double getLeftX() {
        return getRawAxis(1);
    }

    /**
     * Left thumb stick, Y-axis
     */
    public double getLeftY() {
        return getRawAxis(2);
    }

    /**
     * Right thumb stick, X-axis
     */
    public double getRightX() {
        return getRawAxis(4);
    }

    /**
     * Right thumb stick, Y-axis
     */
    public double getRightY() {
        return getRawAxis(5);
    }

    /**
     * This one's a little tricky. Both triggers come in as one axis. left is
     * positive and right is negative. Their two values add to give the result,
     * so pressing both gives 0.
     */
    public double getTriggers() {
        return getRawAxis(3);
    }
    
    /**
     * For some reason the D-pad registers as an axis, not a button and only the
     * x-axis shows up. We have no way of using the D-pad y-axis.
     */
    public double getDpadX() {
        return getRawAxis(6);
    }
}
