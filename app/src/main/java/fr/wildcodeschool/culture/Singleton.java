package fr.wildcodeschool.culture;

import android.location.Location;

class Singleton {
    private static Singleton ourInstance;
     Location locationUser;

    private Singleton() {
    }

    public static Singleton getInstance() {
        if (ourInstance == null) {
            ourInstance = new Singleton();
        }
        return ourInstance;
    }

    public Location getLocationUser() {
        return locationUser;
    }

    public void setLocationUser(Location locationUser) {
        this.locationUser = locationUser;
    }
}
