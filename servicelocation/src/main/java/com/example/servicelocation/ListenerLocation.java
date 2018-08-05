package com.example.servicelocation;

import android.location.Location;

/**
 * Created by Ahmet on 05.08.2018.
 */

public interface ListenerLocation {
     void onLocation(Location location, int isGps);
}
