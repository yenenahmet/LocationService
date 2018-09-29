package com.example.servicelocation;

/**
 * Created by Ahmet on 05.08.2018.
 */
import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Ahmet on 02.08.2017.
 */

public class LocationService extends Service {
    /*************************************************/
    private static final String TAG = "LocationService";
    private static Location locationGPS;
    private static Location locationNETWROK;
    private static LocationManager locationManagerGPS;
    private static LocationManager locationManagerNetwork;
    private WifiManager wifi;
    private static GPS gpss;
    private static Network network;
    private static ListenerLocation listenerLocation  = new ListenerLocation() {
        @Override
        public void onLocation(Location location, int isGps) {

        }
    };
    /*** Default Variable ***/
    /********************[  Cons Start Command  invariable  ]*************************/
    private static long TimeLoc = 1000 * 3;// 30 second
    private static int ServiceSticky = Service.START_STICKY;
    private static float Distance = 0;
    //******************************************************************//
    private static boolean WifiStatus =true,isGPS = true,isNetwork = true,isOpenSettingActivity =true;
    private static String ToastSettingMessage ="Please turn on Location";
    /**********************[ SETTER ]***********************/
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        if(isGPS){
            gpss = new GPS();
        }
        if(isNetwork){
            network = new Network();
        }
    }
    private class GPS implements  LocationListener{
        public GPS(){
            if (ActivityCompat.checkSelfPermission(LocationService.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(LocationService.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            locationManagerGPS = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManagerGPS.requestLocationUpdates(LocationManager.GPS_PROVIDER, TimeLoc, Distance,this);
            Location location =  locationManagerGPS.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if(location != null){
                locationGPS = location;
            }
        }
        @Override
        public void onLocationChanged(Location location) {
            locationGPS = location;
            listenerLocation.onLocation(location,1);
        }
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {}
        @Override
        public void onProviderEnabled(String provider) {
            if (ActivityCompat.checkSelfPermission(LocationService.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(LocationService.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            locationManagerGPS.requestLocationUpdates(LocationManager.GPS_PROVIDER, TimeLoc, Distance, this);
        }
        @Override
        public void onProviderDisabled(String provider) {
            if(!isNetwork){
                if(isOpenSettingActivity) {
                    try {
                        LocationServiceUtil.OpenLocationSettingsIntent(LocationService.this);
                        Toast.makeText(LocationService.this,ToastSettingMessage,Toast.LENGTH_LONG).show();
                    } catch (Exception ex) {
                        Log.e(TAG + "[SET_ER]", ex.toString());
                    }
                }
            }
        }
    }
    private class Network implements  LocationListener{
        public Network(){
            if (ActivityCompat.checkSelfPermission(LocationService.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(LocationService.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            locationManagerNetwork = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManagerNetwork.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, TimeLoc, Distance,this);
            Location location =  locationManagerNetwork.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if(location != null){
                locationNETWROK = location;
            }
        }
        @Override
        public void onLocationChanged(Location location) {
            locationNETWROK = location;
            listenerLocation.onLocation(location,0);
            if(WifiStatus){
                if(!wifi.isWifiEnabled()){
                    wifi.setWifiEnabled(true);
                }
            }
        }
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }
        @Override
        public void onProviderEnabled(String provider) {
            if (ActivityCompat.checkSelfPermission(LocationService.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(LocationService.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            locationManagerNetwork.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, TimeLoc, Distance, this);
            if(!wifi.isWifiEnabled()){
                wifi.setWifiEnabled(true);
            }
        }
        @Override
        public void onProviderDisabled(String provider) {
            if(isOpenSettingActivity){
                try{
                    LocationServiceUtil.OpenLocationSettingsIntent(LocationService.this);
                    Toast.makeText(LocationService.this,ToastSettingMessage,Toast.LENGTH_LONG).show();
                }catch (Exception ex){
                    Log.e(TAG+"[SET]",ex.toString());
                }
            }
        }
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // The service is starting, due to a call to startService()
        return ServiceSticky;
    }
    //*************************************************//
    //              Get Location                       //
    //*************************************************//
    public static Location getLocation(){
        if(locationGPS != null){
           return  locationGPS;
        }else{
            if(locationNETWROK != null){
               return  locationNETWROK;
            }
        }
        return null;
    }
    public static double getLatitude(){
        Location location = getLocation();
        if(location != null){
            return location.getLatitude();
        }
        return 0;
    }
    public static double getLongitude(){
        Location location = getLocation();
        if(location != null){
            return location.getLongitude();
        }
        return 0;
    }
    public static double getAltitude(){
        Location location = getLocation();
        if(location != null){
            return location.getAltitude();
        }
        return 0;
    }
    //*************************************************//
    //              Get Location                       //
    //*************************************************//
    //**********************************//
    //      Setter                      //
    //**********************************//
    //***********************//
    // Start Setter     Invariable     //
    //**********************//
    public static void setInvariableCommand(long Time,int serviceSticky,float distance){
        TimeLoc = Time;
        ServiceSticky = serviceSticky;
        Distance = distance;
    }
    //***********************//
    // Start Setter      Invariable    //
    //**********************//
    public static void setTime(long time){
        TimeLoc = time;
    }
    public static void setServiceSticky(int sticky){
        ServiceSticky = sticky;
    }
    public static void setDistance(float Dist){
        Distance = Dist;
    }
    public static void setWifiStatus(boolean status){
        WifiStatus = status;
    }
    public static void setIsGPS(boolean GPS){
        isGPS =GPS;
    }
    public static void setIsNetwork(boolean Network){
        isNetwork =Network;
    }
    public static void setIsOpenSettingActivity(boolean OpenSettingActivity){ isOpenSettingActivity = OpenSettingActivity;}
    public static void setToastSettingMessage(String text){
        ToastSettingMessage =text;
    }
    public static void setALL(long time,int sticky,float distance,boolean wifiStatus,boolean GPS,boolean Network,boolean OpenSettingActivity,String ToastMessage){
        TimeLoc =time;
        ServiceSticky =sticky;
        Distance =distance;
        WifiStatus = wifiStatus;
        isGPS =GPS;
        isNetwork =Network;
        isOpenSettingActivity =OpenSettingActivity;
        ToastSettingMessage = ToastMessage;
    }
    //**********************************//
    //      Setter                      //
    // First Set Class || Default SET   //
    // Last StartService(INTENT)        //
    //**********************************//
    public static void StopService(){
        try{
            locationManagerGPS.removeUpdates((LocationListener)gpss);
            locationManagerNetwork.removeUpdates((LocationListener)network);
        }catch (Exception ex){
            Log.e(TAG+"[STOPER]",ex.toString());
        }
        try{
            gpss = null;
            network =null;
        }catch (Exception ex){
            Log.e(TAG+"[STOPER]","_2 :" +ex.toString());
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(wifi.isWifiEnabled()){
            wifi.setWifiEnabled(false);
        }
    }

    public static void setListenerLoca(ListenerLocation listenerLoca){
        listenerLocation = listenerLoca;
    }

}