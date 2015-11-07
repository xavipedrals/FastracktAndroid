package com.grupo20.finapps.fastrackt;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, JSONTransmitter.OnJsonTransmitionCompleted {

    private GoogleMap mMap;
    private Marker myMarker;
    private Fragment f;
    private SharedPreferences prefs;


    private Location getGPS() {
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        List<String> providers = lm.getProviders(true);

        Location l = null;
        Location aux = null;

        for (int i = providers.size() - 1; i > 0; i--) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
            l = lm.getLastKnownLocation(providers.get(i));
            if (l != null) break;
        }

        if(l != null){
            aux = l;
        }
        return aux;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission granted!
                } else {
                    // permission denied!
                }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            mMap = googleMap;

            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {

                    prefs = getSharedPreferences("user", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("lastBankClicked", marker.getTitle());
                    LatLng aux = marker.getPosition();
                    editor.putString("lastBankLat", String.valueOf(aux.latitude));
                    editor.putString("lastBankLng", String.valueOf(aux.longitude));
                    editor.apply();

                    DialogImport dialog = new DialogImport();
                    android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
                    dialog.show(fragmentManager, "tag");
                    return true;
                }
            });

            //prova



            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            Criteria criteria = new Criteria();

            Location location = this.getGPS();

            //Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
            if (location != null)
            {
                Log.d("XAVI", "Hola");
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                        new LatLng(location.getLatitude(), location.getLongitude()), 13));

                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(new LatLng(location.getLatitude(), location.getLongitude()))      // Sets the center of the map to location user
                        .zoom(15)                   // Sets the zoom
                        .build();                   // Creates a CameraPosition from the builder
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                mMap.setMyLocationEnabled(true);

            }
            JSONObject json = new JSONObject();
            JSONTransmitter transmitter = new JSONTransmitter(this);
            transmitter.execute(new JSONObject[]{json});
        }

    }


    @Override
    public void onTransmitionCompleted(JSONObject jsonObject) {
        try {
            JSONArray results = jsonObject.getJSONArray("results");
            JSONObject office0 = results.getJSONObject(0);
            Log.d("MAPS", office0.toString());
            mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(office0.getDouble("latitude"), office0.getDouble("longitude")))
                    .title(office0.getString("name")))
                    .setIcon(BitmapDescriptorFactory.fromResource(R.mipmap.marcvermell));


            JSONObject office1 = results.getJSONObject(1);
            mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(office1.getDouble("latitude"), office1.getDouble("longitude")))
                    .title(office1.getString("name")))
                    .setIcon(BitmapDescriptorFactory.fromResource(R.mipmap.marcgrocnou));


            JSONObject office2 = results.getJSONObject(2);
            mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(office2.getDouble("latitude"), office2.getDouble("longitude")))
                    .title(office2.getString("name")))
                .setIcon(BitmapDescriptorFactory.fromResource(R.mipmap.marcblau));

            JSONObject office3 = results.getJSONObject(3);
            mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(office3.getDouble("latitude"), office3.getDouble("longitude")))
                    .title(office3.getString("name")))
                    .setIcon(BitmapDescriptorFactory.fromResource(R.mipmap.marcgrocnou));

            JSONObject office4 = results.getJSONObject(4);
            mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(office4.getDouble("latitude"), office4.getDouble("longitude")))
                    .title(office4.getString("name")))
                    .setIcon(BitmapDescriptorFactory.fromResource(R.mipmap.marcblau));

            JSONObject office5 = results.getJSONObject(5);
            mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(office5.getDouble("latitude"), office5.getDouble("longitude")))
                    .title(office5.getString("name")))
                    .setIcon(BitmapDescriptorFactory.fromResource(R.mipmap.marcblau));

            JSONObject office6 = results.getJSONObject(6);
            mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(office6.getDouble("latitude"), office6.getDouble("longitude")))
                    .title(office6.getString("name")))
                    .setIcon(BitmapDescriptorFactory.fromResource(R.mipmap.marcvermell));

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
