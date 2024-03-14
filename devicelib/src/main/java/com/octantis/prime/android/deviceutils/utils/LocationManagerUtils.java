package com.octantis.prime.android.deviceutils.utils;

import static android.content.Context.LOCATION_SERVICE;

import android.annotation.SuppressLint;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;


import com.octantis.prime.android.deviceutils.DeviceMain;

import java.util.List;
import java.util.Locale;

public class LocationManagerUtils {


    LocationManager locationManager;
    public String longitude = "";
    public double longitudeDouble = 0.0;
    public String latitude = "";
    public double latitudeDouble = 0.0;
    public String address_details = "";
    public String city = "";
    public String provice = "";
    public String country = "";
    public String largeDistrict = "";
    public String smallDistrict = "";

    public LocationManagerUtils() {
        getNowLocation();
    }

    public void getNowLocation() {
        locationManager = (LocationManager) DeviceMain.getApp().getSystemService(LOCATION_SERVICE);
        Location location = getLastKnownLocation(locationManager);
        if (location != null) {
            longitude = String.valueOf(location.getLongitude());
            latitude = String.valueOf(location.getLatitude());
            Geocoder geocoder = new Geocoder(DeviceMain.getApp(), Locale.getDefault());
            try {
                List<Address> addresses = geocoder.getFromLocation(Double.parseDouble(latitude), Double.parseDouble(longitude), 1);
                if (addresses != null && !addresses.isEmpty()) {
                    Address address = addresses.get(0);
                    latitudeDouble = Double.parseDouble(latitude);
                    longitudeDouble = Double.parseDouble(longitude);
                    country = address.getCountryName();
                    provice = address.getAdminArea();
                    city = address.getSubAdminArea();
                    largeDistrict = address.getLocality();
                    smallDistrict = address.getThoroughfare();
                    address_details = address.getAddressLine(0);
                }
            } catch (Exception e) {
                //可能会报异常，可能是因为手机不支持Google定位导致
                e.printStackTrace();
            }
        }
    }

    @SuppressLint("MissingPermission")
    public Location getLastKnownLocation(LocationManager locationManager) {
        List<String> providers = locationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            Location lastKnownLocation = locationManager.getLastKnownLocation(provider);
            if (lastKnownLocation == null) {
                continue;
            }
            if (bestLocation == null || lastKnownLocation.getAccuracy() < bestLocation.getAccuracy()) {
                bestLocation = lastKnownLocation;
            }
        }
        return bestLocation;
    }
}
