package com.cryptopay.prototype.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.cryptopay.prototype.Constants;
import com.cryptopay.prototype.domain.Advert;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.cryptopay.prototype.R;
import com.kenai.jffi.Main;

import java.util.Date;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, GoogleMap.OnInfoWindowClickListener {


    private GoogleMap mMap;
    private SupportMapFragment mapFragment;
    private Marker marker;
    private final static int REQUEST_ACCESS_FINE_LOCATION = 1;
    private final static int REQUEST_ACCESS_COARSE_LOCATION = 2;

    private MarkerOptions markerOptions = new MarkerOptions();

    private LocationManager locationManager;

    private int from;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        Intent intent = getIntent();
        from = intent.getIntExtra("from", 0);

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(MapsActivity.this, "Permission Denied", Toast.LENGTH_LONG).show();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_ACCESS_FINE_LOCATION);
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_ACCESS_COARSE_LOCATION);
            }
        } else {
            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000 * 10, 10, locationListener);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000 * 10, 10, locationListener);
        }
        initToolbar();

    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.history_toolbar);
        toolbar.setTitle("Location");
        toolbar.setNavigationIcon(getResources().getDrawable(R.mipmap.ic_arrow_left_thick));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void checkEnabled() {
        Log.i("L1", "Enabled GPS: "
                + locationManager
                .isProviderEnabled(LocationManager.GPS_PROVIDER));
        Log.i("L1", "Enabled NET: "
                + locationManager
                .isProviderEnabled(LocationManager.NETWORK_PROVIDER));
    }

//    public static void displayAlertDialogMessage(Activity activity, String message, DialogInterface.OnClickListener listener) {
//        new AlertDialog.Builder(activity)
//                .setMessage(message)
//                .setPositiveButton("OK", listener)
//                .setNegativeButton("Cancel", null)
//                .show();
//    }

    private LocationListener locationListener = new LocationListener() {

        @Override
        public void onLocationChanged(Location location) {
            LatLng youAreHere = new LatLng(location.getLatitude(), location.getLongitude());
            if (marker != null) {
                marker.remove();
            }
            marker = mMap.addMarker(markerOptions.position(youAreHere).title("You are here"));

            mMap.moveCamera(CameraUpdateFactory.newLatLng(youAreHere));
            showLocation(location);
        }

        @Override
        public void onProviderDisabled(String provider) {
            checkEnabled();
        }

        @SuppressLint("MissingPermission")
        @Override
        public void onProviderEnabled(String provider) {
            checkEnabled();

            showLocation(locationManager.getLastKnownLocation(provider));
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            if (provider.equals(LocationManager.GPS_PROVIDER)) {
                Log.i("L1", "Status: " + String.valueOf(status));
            } else if (provider.equals(LocationManager.NETWORK_PROVIDER)) {
                Log.i("L1", "Status: " + String.valueOf(status));
            }
        }
    };

    private void showLocation(Location location) {
        if (location == null)
            return;
        if (location.getProvider().equals(LocationManager.GPS_PROVIDER)) {
            Log.i("L1", formatLocation(location));
        } else if (location.getProvider().equals(
                LocationManager.NETWORK_PROVIDER)) {
            Log.i("L1", formatLocation(location));
        }
    }

    private String formatLocation(Location location) {
        if (location == null)
            return "";
        return String.format(
                "Coordinates: lat = %1$.4f, lon = %2$.4f, time = %3$tF %3$tT",
                location.getLatitude(), location.getLongitude(), new Date(
                        location.getTime()));
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnInfoWindowClickListener(this);
        mMap.setOnMarkerClickListener(this);
        int height = 100;
        int width = 100;

        if (from == MainActivity.SWITCH_FROM_MAIN) {
            List<Advert> data = Constants.advertDTO.getData();
            BitmapDescriptor bitmap;
            for (Advert advert : data) {
                if (advert.getLatitude() != 0d && advert.getLongitude() != 0d) {
                    byte[] pic = advert.getTypeItem().getPic();
                    if (pic != null) {
                        Bitmap bMap = BitmapFactory.decodeByteArray(pic, 0, pic.length);
                        bitmap = BitmapDescriptorFactory.fromBitmap(Bitmap.createScaledBitmap(bMap, width, height, false));

                    } else {
                        bitmap = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN);
                    }
                    mMap.addMarker(new MarkerOptions().position(new LatLng(advert.getLatitude(), advert.getLongitude())).icon(bitmap
                    ).title(advert.getTitle()).snippet(advert.getDescription())).setTag(advert);
                }

            }
        }
        if (from == ShopActivity.SWITCH_FROM_SHOP) {
            Advert advert = Constants.advertDTO.getData().get(0);
            if (advert.getLatitude() != 0d && advert.getLongitude() != 0d) {
                LatLng latLng = new LatLng(advert.getLatitude(), advert.getLongitude());
                mMap.addMarker(new MarkerOptions().position(latLng).icon(
                        BitmapDescriptorFactory
                                .fromResource(R.mipmap.ic_app)).title(advert.getTitle()));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            }
        }

//        mMap.addMarker(new MarkerOptions().position(new LatLng(-34, 145)).icon(
//                BitmapDescriptorFactory
//                        .defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
//
//        mMap.addMarker(new MarkerOptions().position(new LatLng(-34, 158)).icon(
//                BitmapDescriptorFactory.defaultMarker()));
//
//        mMap.addMarker(new MarkerOptions().position(new LatLng(-34, 155)).icon(
//                BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher)));


    }

    @Override
    public boolean onMarkerClick(Marker marker) {

        return false;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        Advert advert = (Advert) marker.getTag();
        if (advert != null) {
            ShopActivity.advert = advert;
            Intent intent = new Intent(MapsActivity.this, ShopActivity.class);
            startActivity(intent);
        }
    }
}
