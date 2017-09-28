# LocationService
 1) Start Service  Setter 
  *** !!! Contexxt !!! ***
  
  
                   @Override
                    protected void onStart() {
                        super.onStart();
                        LocationService.setContext(this);
                        startService(new Intent(getApplicationContext(), LocationService.class));

                    }
                  *************************************************
  2) Example 
  
                  @Override
                    protected void onStart() {
                        super.onStart();
                         LocationService.setInvariableCommand((1000*6)*10, Service.START_STICKY,0,this);
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
                      LocationService.setContext(this);
                      LocationService.setALL(1000,Service.START_STICKY,0,true,true,true,true," **** ");
                      startService(new Intent(getApplicationContext(), LocationService.class));

                  }
