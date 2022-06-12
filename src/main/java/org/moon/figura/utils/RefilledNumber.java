package org.moon.figura.utils;

import net.minecraft.SharedConstants;

/**
 * class that represents a number that can be refilled
 * the "max" value is the limit the number can reach (rounded up)
 * on each tick, the number will be increased by the "add" value, until it reaches the "max" value
 * the rate of tick is based on {@link SharedConstants#TICKS_PER_SECOND}
 * where the number will attempt to reach back to its max value every second
 */
public class RefilledNumber {

    private double max;
    private double add;

    private double current = 0d;

    //empty/dummy constructor
    public RefilledNumber() {
        this(0d);
    }

    //actual constructor
    public RefilledNumber(double max) {
        set(max);
    }

    //update the current value
    public void tick() {
        current = Math.min(current + add, max);
    }

    //checks if the current value can be consumed and then consumes it if it can
    public boolean use() {
        boolean peek = peek();
        if (peek) current--;
        return peek;
    }

    //checks if the current value can be consumed without consuming it
    public boolean peek() {
        return current >= 1;
    }

    //updates the max value
    public void set(double max) {
        this.max = Math.ceil(max);
        this.add = max / SharedConstants.TICKS_PER_SECOND;
        this.current = Math.min(this.current, max);
    }
}
