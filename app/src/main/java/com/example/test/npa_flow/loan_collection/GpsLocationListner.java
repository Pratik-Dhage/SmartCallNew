package com.example.test.npa_flow.loan_collection;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;

import java.util.List;

public class GpsLocationListner implements LocationListener {

    Location locationByGps;
    @Override
    public void onLocationChanged(@NonNull Location location) {
        locationByGps = location;
    }

    @Override
    public void onLocationChanged(@NonNull List<Location> locations) {
        LocationListener.super.onLocationChanged(locations);
    }

    @Override
    public void onFlushComplete(int requestCode) {
        LocationListener.super.onFlushComplete(requestCode);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        LocationListener.super.onStatusChanged(provider, status, extras);
    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {
       // LocationListener.super.onProviderEnabled(provider); //this line is causing crash on Turning Location ON

        // When the location provider is enabled
        if (provider.equals(LocationManager.GPS_PROVIDER)) {
            // GPS provider is enabled
            System.out.println("GPS Provider enabled");
        } else if (provider.equals(LocationManager.NETWORK_PROVIDER)) {
            // Network provider is enabled
            System.out.println("Network Provider enabled");
        }
    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {
      //  LocationListener.super.onProviderDisabled(provider); //this line is causing crash on Turning Location OFF

        //  When the location provider is disabled
        if (provider.equals(LocationManager.GPS_PROVIDER)) {
            // GPS provider is disabled
            System.out.println("GPS Provider disabled");
        } else if (provider.equals(LocationManager.NETWORK_PROVIDER)) {
            // Network provider is disabled
            System.out.println("Network Provider disabled");
        }

    }
}
