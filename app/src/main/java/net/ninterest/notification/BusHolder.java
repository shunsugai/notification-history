package net.ninterest.notification;

import com.squareup.otto.Bus;

public class BusHolder {

    private static Bus sBus;

    private BusHolder() {
    }

    public static synchronized Bus get() {
        if (sBus == null) {
            sBus = new Bus();
        }
        return sBus;
    }
}
