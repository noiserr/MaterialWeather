package pl.wro.mm.materialweather.manager;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by noiser on 07.06.15.
 */
public class LocationManager implements LocationListener {

    String privider;
    private boolean canGetLocation;
    Criteria criteria;
    Location location;
    android.location.LocationManager locationManager;

    final Context context;

    public LocationManager(Context context) {
        this.context = context;
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d("LOCATION", "onLocationChanged");
        Log.d("LOCATION", location.getLatitude() + " " + location.getLongitude());
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d("LOCATION", "onStatusChanged");


    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d("LOCATION", "onProviderEnabled");


    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.d("LOCATION", "onProviderDisabled");


    }

    public Location getLocation() {
        try {
            locationManager = (android.location.LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

            // getting GPS status
            boolean isGPSEnabled = locationManager
                    .isProviderEnabled(android.location.LocationManager.GPS_PROVIDER);

            // getting network status
            boolean isNetworkEnabled = locationManager
                    .isProviderEnabled(android.location.LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {
                // no network provider is enabled
            } else {
                this.canGetLocation = true;
                double latitude;
                double longitude;
                if (isNetworkEnabled) {
                    locationManager.requestLocationUpdates(
                            android.location.LocationManager.NETWORK_PROVIDER,
                            100,
                            100f, this);
                    Log.d("LOCATION", "Network Enabled");
                    if (locationManager != null) {
                        location = locationManager
                                .getLastKnownLocation(android.location.LocationManager.NETWORK_PROVIDER);
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }
                    }
                }
                // if GPS Enabled get lat/long using GPS Services
                if (isGPSEnabled) {
                    if (location == null) {
                        locationManager.requestLocationUpdates(
                                android.location.LocationManager.GPS_PROVIDER,
                                100,
                                100f, this);
                        Log.d("LOCATION", "GPS Enabled");
                        if (locationManager != null) {
                            location = locationManager
                                    .getLastKnownLocation(android.location.LocationManager.GPS_PROVIDER);
                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                            }
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        locationManager.removeUpdates(this);
        return location;
    }
}
