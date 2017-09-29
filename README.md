                                 Manifet ADD
                                            <service
                                            android:name="com.example.servicelocation.LocationService"
                                            android:exported="true" />

# LocationService
 1) Start Service  Setter 

  
  
                   @Override
                    protected void onStart() {
                        super.onStart();
                        startService(new Intent(getApplicationContext(), LocationService.class));

                    }
                  *************************************************
  2) Example 
  
                  @Override
                    protected void onStart() {
                        super.onStart();
                         LocationService.setInvariableCommand((1000*6)*10, Service.START_STICKY,0);
                          startService(new Intent(getApplicationContext(), LocationService.class));

                    }


                    private void LocationGet(){
                        double latitude = LocationService.getLatitude();
                        double longitude = LocationService.getLongitude();
                        Log.e("Location Service :", String.valueOf(latitude)+","+String.valueOf(longitude));
                   } 
    *****************************************************
                   @Override
                  protected void onStart() {
                      super.onStart();
                      LocationService.setALL(1000,Service.START_STICKY,0,true,true,true,true," **** ");
                      startService(new Intent(getApplicationContext(), LocationService.class));
                  }
