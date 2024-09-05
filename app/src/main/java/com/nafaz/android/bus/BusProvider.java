package com.nafaz.android.bus;

import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

import org.jetbrains.annotations.Contract;

public class BusProvider {

    private static final Bus BUS = new Bus(ThreadEnforcer.ANY);

    @Contract(pure = true)
    private BusProvider() {
    }

    /**
     * Gets instance.
     *
     * @return the instance
     */
    @Contract(pure = true)
    public static Bus getInstance() {
        return BUS;
    }
}
