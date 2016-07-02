package com.example.aniket.googlemapsintegration;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;


public class MainActivity extends AppCompatActivity  {

    private GoogleMap mMap;
    private LocationManager locationManager;
    protected com.google.android.gms.location.LocationListener locationListener;
    private static final int PLACE_PICKER_REQUEST = 1;
    private TextView mName;
//    private GoogleMap w;
    private TextView mAddress;
  static Double lat,lon;
    private int lenght1=0,length2=0,checkWhichaddress=0;
    private TextView mAttributions,mAddress1;
    private   Button pickerButton,navigate;
    private static final LatLngBounds BOUNDS_MOUNTAIN_VIEW = new LatLngBounds(
            new LatLng(37.398160, -122.180831), new LatLng(37.430610, -121.972090));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialise();

        mAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
mAddress.setEnabled(false);
                checkWhichaddress=1;

//                Intent in =new Intent(MainActivity.this,Main2Activity.class);
                Toast.makeText(MainActivity.this, "Please wait while we redirect to the suggestion page!", Toast.LENGTH_SHORT).show();
//                startActivity(in);
                try {
                    PlacePicker.IntentBuilder intentBuilder =
                            new PlacePicker.IntentBuilder();

                    Intent intent = intentBuilder.build(MainActivity.this);
                    startActivityForResult(intent, PLACE_PICKER_REQUEST);

                } catch (GooglePlayServicesRepairableException
                        | GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });
        mAddress1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAddress1.setEnabled(false);
                checkWhichaddress=2;
//                Intent in =new Intent(MainActivity.this,Main2Activity.class);
//                startActivity(in);
                Toast.makeText(MainActivity.this, "Please wait while we redirect to the suggestion page!", Toast.LENGTH_SHORT).show();
                try {
                    PlacePicker.IntentBuilder intentBuilder =
                            new PlacePicker.IntentBuilder();

                    Intent intent = intentBuilder.build(MainActivity.this);
                    startActivityForResult(intent, PLACE_PICKER_REQUEST);

                } catch (GooglePlayServicesRepairableException
                        | GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });
    pickerButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mAddress.setText("");
            mAddress1.setText("");
        }
    });
    }

    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode, Intent data) {
mAddress.setEnabled(true);
        mAddress1.setEnabled(true);
        if (requestCode == PLACE_PICKER_REQUEST
                && resultCode == Activity.RESULT_OK) {


            final Place place = PlacePicker.getPlace(this, data);
            final CharSequence name = place.getName();
            final CharSequence address = place.getAddress();
            String attributions = (String) place.getAttributions();
            if (attributions == null) {
                attributions = "";
            }
 lenght1=mAddress1.getText().toString().trim().length();
            length2=mAddress.getText().toString().trim().length();


//            mName.setText(name);
            if(checkWhichaddress==2)
            mAddress1.setText(address);

         //   mAttributions.setText(Html.fromHtml(attributions));
            else
            if(checkWhichaddress==1)
            mAddress.setText(address);


        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

void initialise()
{

    mName = (TextView) findViewById(R.id.textView);
    mAddress = (TextView) findViewById(R.id.add2);
    mAddress1=(TextView) findViewById(R.id.add1);

    mAttributions = (TextView) findViewById(R.id.textView3);
    pickerButton = (Button) findViewById(R.id.pickerButton);
    navigate=(Button)findViewById(R.id.navigate);
    navigate.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if((mAddress.getText().toString().length()==0)||(mAddress1.getText().toString().length()==0))
            {
                Toast.makeText(MainActivity.this, "Please pick two address", Toast.LENGTH_SHORT).show();
            }
            else
            {
//                locationManager = (LocationManager) MainActivity.this.getSystemService(Context.LOCATION_SERVICE);
                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
//                    int r=ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding

                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;

                }
//        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,LocationListenerthis);
//                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, (android.location.LocationListener) MainActivity.this);
//                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, (android.location.LocationListener) MainActivity.this);
                String origin=mAddress1.getText().toString();
                String destination=mAddress.getText().toString();
                Toast.makeText(MainActivity.this, mAddress.getText().toString(), Toast.LENGTH_SHORT).show();
                Intent in =new Intent(MainActivity.this,Main2Activity.class);
                in.putExtra("origin",origin);
                in.putExtra("destination",destination);
                startActivity(in);

            }
        }
    });

}







    }