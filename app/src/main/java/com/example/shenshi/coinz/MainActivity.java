package com.example.shenshi.coinz;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mapbox.android.core.location.LocationEngine;
import com.mapbox.android.core.location.LocationEngineListener;
import com.mapbox.android.core.location.LocationEnginePriority;
import com.mapbox.android.core.location.LocationEngineProvider;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.Geometry;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.Icon;
import com.mapbox.mapboxsdk.annotations.IconFactory;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.MapboxMapOptions;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerPlugin;
import com.mapbox.mapboxsdk.plugins.locationlayer.modes.CameraMode;
import com.mapbox.mapboxsdk.plugins.locationlayer.modes.RenderMode;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;



public class MainActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener,OnMapReadyCallback, LocationEngineListener,PermissionsListener, MapboxMap.OnMarkerClickListener, MapboxMap.OnMapClickListener {

    private final String TAG = "MainActivity";
    private LatLng cur;
    private LatLng loc_mark;

    //Map
    private MapView mapView;
    private MapboxMap map;
    private PermissionsManager permissionsManager;
    private LocationEngine locationEngine;
    private LocationLayerPlugin locationLayerPlugin;
    private Location originLocation;
    private Button btn;

    private String downloadDate = ""; // Format: YYYY/MM/DD
    private final String preferencesFile = "MyPrefsFile"; // for storing preferences

    //pop menu
    private static String POPUP_CONSTANT = "mPopup";
    private static String POPUP_FORCE_SHOW_ICON = "setForceShowIcon";

    //logout
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //logout
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener(){
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser()==null)
                {
                    startActivity(new Intent(MainActivity.this, Login_activity.class));
                }
            }
        };

        //download Map
        String mapURL = "http://homepages.inf.ed.ac.uk/stg/coinz/2018/10/03/coinzmap.geojson";
        DownloadFileTask myTask = new DownloadFileTask();
        myTask.execute(mapURL);

        //Map
        Mapbox.getInstance(this, getString(R.string.access_token));

        setContentView(R.layout.activity_main);
        btn = (Button) findViewById(R.id.button);


        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu){
        MenuInflater inflater = getMenuInflater ();
        inflater.inflate (R.menu.option_menu, menu);
        return true;
    }

    public boolean onMenuItemSelect (MenuItem item){
        showPopup (findViewById(item.getItemId()));
        return true;
    }

    private void showPopup(View view) {
        PopupMenu popup = new PopupMenu(MainActivity.this, view);
        try {
            // Reflection apis to enforce show icon
            Field[] fields = popup.getClass().getDeclaredFields();
            for (Field field : fields) {
                if (field.getName().equals(POPUP_CONSTANT)) {
                    field.setAccessible(true);
                    Object menuPopupHelper = field.get(popup);
                    Class<?> classPopupHelper = Class.forName(menuPopupHelper.getClass().getName());
                    Method setForceIcons = classPopupHelper.getMethod(POPUP_FORCE_SHOW_ICON, boolean.class);
                    setForceIcons.invoke(menuPopupHelper, true);
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(this);
        popup.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.wallet:
                Toast.makeText(MainActivity.this, "You clicked delete", Toast.LENGTH_SHORT).show();
                break;
            case R.id.bank:
                Toast.makeText(MainActivity.this, "You clicked delete", Toast.LENGTH_SHORT).show();
                break;
            case R.id.logout:
                mAuth.signOut();
        }
        return false;
    }



    @Override
    public void onMapReady(MapboxMap mapboxMap) {
        if (mapboxMap == null){
            Log.d(TAG,"[OnMapReady] mapBox is null" );
        }else {
            map = mapboxMap;


            map.getUiSettings().setCompassEnabled(true);
            map.getUiSettings().setZoomControlsEnabled(true);

            enableLocation();
        }

        // adding markers
        Log.d(TAG,"result is "+ DownloadCompleteRunner.result.length());
        Log.d(TAG,"result is done");


        List<Feature> coins = FeatureCollection.fromJson(DownloadCompleteRunner.result).features();
        Log.d(TAG,"result is size"+coins);
        for (int i =0;i<coins.size();i++){

            Feature coin =  coins.get(i);

            //get coordinates
            Geometry g = coin.geometry();
            Point p = (Point) g;
            List<Double> c = p.coordinates();
            double lng = c.get(0);
            double lat = c.get(1);
            LatLng loc = new LatLng(lat,lng);
            Log.d(TAG,"loc is "+loc);
            //get properties
            JsonObject j = coin.properties();
            String cur = j.get("currency").toString();
            String val = j.get("value").toString();
            String id = j.get("id").toString();
            String symbol = j.get("marker-symbol").toString();
            String color = (j.get("marker-color").toString());


            map.addMarker(new MarkerOptions().
                    title(id).
                    snippet("currency:"+cur+"\n"+"value:"+val).
                    position(new LatLng(lat,lng))
            );
            mapboxMap.addOnMapClickListener(this);
            mapboxMap.setOnMarkerClickListener(this);
        }

    }

    private void enableLocation(){
        if (PermissionsManager.areLocationPermissionsGranted(this)){
            Log.d(TAG,"Permission are granted");
            initializeLocationEngine();
            initializeLocationLayer();
        }else{
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(this);
        }

    }

    @SuppressWarnings("MissingPermission")
    private void initializeLocationEngine(){
        locationEngine = new LocationEngineProvider(this).obtainBestLocationEngineAvailable();
        locationEngine.setPriority(LocationEnginePriority.HIGH_ACCURACY);
        locationEngine.activate();




        Location lastLocation = locationEngine.getLastLocation();
        if (lastLocation != null){
            originLocation = lastLocation;
            setCameraPosition(lastLocation);
        }else {
            locationEngine.addLocationEngineListener(this);
        }
    }

    @SuppressWarnings("MissingPermission")
    private void initializeLocationLayer(){
        if (mapView ==null){
            Log.d(TAG,"mapView is null");
        }else {
            if (map == null){
                Log.d(TAG,"map is null");
            }else{
                locationLayerPlugin = new LocationLayerPlugin(mapView, map, locationEngine);
                locationLayerPlugin.setLocationLayerEnabled(true);
                locationLayerPlugin.setCameraMode(CameraMode.TRACKING);
                locationLayerPlugin.setRenderMode(RenderMode.NORMAL);
            }
        }
    }

    private void setCameraPosition(Location location) {
        LatLng latLng = new LatLng(location.getLatitude(),location.getLongitude());
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13.0));
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location == null){
            Log.d(TAG,"[OnLocationChanged] location is null");
        }else {
            Log.d(TAG,"[onLocationChanged] location is not null");
            originLocation = location;
            setCameraPosition(location);
        }
    }

    @SuppressWarnings("MissingPermission")
    @Override
    public void onConnected() {
        Log.d(TAG,"[onConnected] requesting location updates");
        locationEngine.requestLocationUpdates();
    }



    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {
        Log.d(TAG,"Permission: " + permissionsToExplain.toString());
    }

    @Override
    public void onPermissionResult(boolean granted) {
        Log.d(TAG,"[onPermissionResult] granted == " + granted);
        if (granted) {
            enableLocation();
        }
        else{
            Toast.makeText(getApplicationContext(),"Permission is not granted",Toast.LENGTH_SHORT).show();
            return;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @SuppressWarnings("MissingPermission")
    @Override
    public void onStart() {

        super.onStart();

        //logout

        mAuth.addAuthStateListener(mAuthListener);

        //remembering the last date that a map was downloaded

        // Restore preferences
        SharedPreferences settings = getSharedPreferences(preferencesFile,
                Context.MODE_PRIVATE);
        // use ”” as the default value (this might be the first time the app is run)
        downloadDate = settings.getString("lastDownloadDate", "");
        Log.d(TAG, "[onStart] Recalled lastDownloadDate is ’" + downloadDate + "’");


        //Map
        if (locationEngine != null){
            locationEngine.requestLocationUpdates();
        }
        if (locationLayerPlugin != null){
            locationLayerPlugin.onStart();
        }


        mapView.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();

        //writing out updated preference
        Log.d(TAG, "[onStop] Storing lastDownloadDate of " + downloadDate);
        // All objects are from android.context.Context
        SharedPreferences settings = getSharedPreferences(preferencesFile,
                Context.MODE_PRIVATE);
        // We need an Editor object to make preference changes.
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("lastDownloadDate", downloadDate);
        // Apply the edits!
        editor.apply();

        //Map
        if (locationEngine !=null){
            locationEngine.removeLocationUpdates();
        }
        if (locationLayerPlugin != null) {
            locationLayerPlugin.onStop();
        }
        mapView.onStop();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (locationEngine != null){
            locationEngine.deactivate();
        }
        mapView.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
        btn.setVisibility(View.VISIBLE);
        return false;
    }

    public void onMapClick(@NonNull LatLng point) {
        btn.setVisibility(View.GONE);
    }


   /* @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
        LatLng loc = marker.getPosition();
        String id = marker.getTitle();
        String snippet = marker.getSnippet();
        String [] ss = snippet.split("\\s");
        String cur = ss[1];
        double value = Double.parseDouble(ss [3]);
        return true;
    }
    */
}
