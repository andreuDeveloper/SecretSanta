package com.secretsanta.secretsanta;


import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class EventActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener {
    private EditText txtDate;
    private EditText txtHour;
    private EditText txtMaxPrice;
    private EditText txtMinPrice;
    private FloatingActionButton btnListDone;
    private FloatingActionButton btnGoBack;
    private ScrollView scrollView;
    private View customView;

    private Button btnUbi;

    private GoogleMap mMap;
    private boolean marked;


    private LatLng markerPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);


        marked = false;

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        this.txtDate = findViewById(R.id.txtdate);
        this.txtHour =  findViewById(R.id.txtHour);
        this.txtMaxPrice =  findViewById(R.id.maxPrice);
        this.scrollView = findViewById(R.id.scrollView);
        this.txtMinPrice =  findViewById(R.id.minPrice);

        this.btnUbi = findViewById(R.id.btnSetUbication);

        //this.customView = findViewById(R.id.customView);

        this.btnGoBack =  findViewById(R.id.btnGoBack);
        this.btnGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        this.btnListDone =  findViewById(R.id.btnListDone);
        this.btnListDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (checkFieldsNotNull(txtDate) && checkFieldsNotNull(txtMaxPrice) && checkFieldsNotNull(txtMinPrice)
                        && checkDateValid()) {
                    yesNoDialog();
                }

            }
        });

        this.btnUbi.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent i=new Intent(getApplicationContext(),EventMActivity.class);

                getApplication().startActivity(i);
            }
        });

        this.customView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        scrollView.requestDisallowInterceptTouchEvent(true);
                        return false;
                    case MotionEvent.ACTION_UP:
                        scrollView.requestDisallowInterceptTouchEvent(false);
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        scrollView.requestDisallowInterceptTouchEvent(true);
                        return  false;
                    default:
                        return true;
                }
            }
        });



    }
    public void onStart(){
        super.onStart();

        EditText txtDate=(EditText) findViewById(R.id.txtdate);
        txtDate.setKeyListener(null);
        EditText txtHour=(EditText) findViewById(R.id.txtHour);
        txtHour.setKeyListener(null);

        txtDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    DialogFragment newFragment = new DateDialog(view);
                    newFragment.show(getSupportFragmentManager(), "DatePicker");
                }
            }
        });

        txtDate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                DialogFragment newFragment = new DateDialog(v);
                newFragment.show(getSupportFragmentManager(), "DatePicker");
            }
        });

        txtHour.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    DialogFragment newFragment = new TimeDialog(view);
                    newFragment.show(getSupportFragmentManager(), "timePicker");
                }
            }
        });

        txtHour.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                DialogFragment newFragment = new TimeDialog(v);
                newFragment.show(getSupportFragmentManager(), "timePicker");
            }
        });

    }

    private void bindRandomParticipants(){
        ParticipantLab pl = ParticipantLab.get(getApplicationContext());

        try {
            Random random = new Random();
            for (int i = 0; i < pl.getNumberParticipants(); i++) {
                Person persFrom = pl.getParticipant(i);
                boolean associated = false;
                while (!associated) {
                    int numRandom = random.nextInt(pl.getNumberParticipants() - 1 - 0 + 1) + 0;
                    if ((numRandom != i) && !(pl.getParticipant(numRandom).havePersonAsociated())) {
                        associated = true;
                        Person persTo = pl.getParticipant(numRandom);
                        persTo.setHavePersonAsociated(true);
                        doSecretSanta(persTo, persFrom);
                    }
                }
            }
        } catch (Exception e) {
            Log.e("ERROR", "->", e.getCause());
        }
    }

    private void doSecretSanta(Person personTo, Person personFrom) {
        String parsedDate;
        String email = personFrom.getEmail().toLowerCase().trim();
        String subject = "Secret Santa Event "+personFrom.getName().trim();
        String htmlMessage = "" +
                "<h1>SECRET SANTA</h1>" +
                "<h3>Event created: "+new Date()+"</h3>" + //Parse DATE
                "<hr>" +
                "<h4>Date: DATE</h5>" +
                "<h4>Hour: HOUR <h5>" +
                "<h4>Price: MIN PRICE€ --- MAX PRICE€</h5>" +
                "<h4>Ubication: <h5>" +
                //"<a href=\"STRING URL MAP HERE">" +
                "<a href=\"https://www.google.com/maps?q=39.57268981421449,2.6280283927917485\">" +
                "<img src=\"http://www.myiconfinder.com/uploads/iconsets/256-256-9c7aae955a2bcc1811b64c019bd3df28.png\" alt=\"Event ubication\" height=\"100\" width=\"100\"/>" +
                "</a>" +
                "<hr>" +
                "<h4>Your person is.... "+personTo.getName().toUpperCase()+"!!</h4>" +
                "<p>Don't worry, "+personTo.getName()+" gave us some information to help you to choose the present:</p>" +
                "<ul>" +
                "<li>Age: "+personTo.getAge()+"</li>" +
                "<li>Birthday: "+personTo.getBirthday()+"</li>" +
                "<li>Likes: "+personTo.getLikes()+"</li>" +
                "</ul>" +
                "<hr>" +
                "<h4>Thanks for use our application!</h4>" +
                "<h4>Secret Santa <3</h4>" +
                "";

        /*
        String message = "\n" +
                "Your person is....." + personTo.getName().toUpperCase()+"" +
                "\n" +
                "\n" +
                "No worries "+personTo.getName()+ " gave us some information to help you to choose the present:" +
                "\n" +
                "\t- AGE:" +personTo.getAge()+"\n" +
                "\t- BIRTHDAY: "+personTo.getBirthday()+"\n" +
                "\t- LIKES: "+personTo.getLikes()+"" +
                "\n\n" +
                "Thanks for use our application!\n\nSecret Santa <3";
        */

        Log.v("SECRET: ", personTo.getName()+" -> "+personFrom.getName());
        SendMail sm = new SendMail(this, email, subject, htmlMessage);

        sm.execute();
    }

    private boolean checkFieldsNotNull(EditText editText){
        if (editText.getText().toString().length() == 0){
            editText.requestFocus();
            Toast.makeText(this, "Complete the field", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private boolean checkDateValid(){
        try {
        int day = Integer.parseInt(txtDate.getText().toString().substring(0,txtDate.getText().toString().indexOf("-")));
        int month = Integer.parseInt(txtDate.getText().toString().substring(txtDate.getText().toString().indexOf("-")+1,
                txtDate.getText().toString().indexOf("-",txtDate.getText().toString().indexOf("-")+1)));
        int year = Integer.parseInt(txtDate.getText().toString().substring(txtDate.getText().toString().indexOf("-",txtDate.getText().toString().indexOf("-")+1)+1,
                txtDate.getText().toString().length()));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String now=sdf.format(new Date());
        Date dnow = sdf.parse(now);
        Date celebration = sdf.parse(year+"-"+month+"-"+day);

        if (dnow.getTime() < celebration.getTime()) {
            return true;
        } else {
            Toast.makeText(this, "Insert a valid Date", Toast.LENGTH_LONG).show();
            txtDate.requestFocus();
            return false;
        }
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void yesNoDialog(){
        // Build an AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // Set a title for alert dialog
        builder.setTitle("Send emails");

        // Ask the final question
        builder.setMessage("Are you sure?\nPlease, be patient");

        // Set the alert dialog yes button click listener
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                bindRandomParticipants();
            }
        });

        // Set the alert dialog no button click listener
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Do something when No button clicked

            }
        });

        AlertDialog dialog = builder.create();
        // Display the alert dialog on interface
        dialog.show();
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
    @Override
    public void onMapReady(GoogleMap googleMap) {

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.v("GRANTED", "No");
            return;
        }
        locationManager.requestLocationUpdates(String.valueOf(locationManager.getBestProvider(new Criteria(), true)).toString(), 1000, 0, this);
        Log.v("GRANTED", "Yes");


        mMap = googleMap;



        markerPosition = new LatLng(39.693826939133615,2.9987646639347076);
        CameraPosition cameraPosition = new CameraPosition.Builder().target(markerPosition).zoom(12.5f).build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        //mMap.addMarker(new MarkerOptions().position(markerPosition));

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng latLng) {


                // Creating a marker
                MarkerOptions markerOptions = new MarkerOptions();

                // Setting the position for the marker
                markerOptions.position(latLng);

                // Setting the title for the marker.
                // This will be displayed on taping the marker
                markerOptions.title("Secret Santa Here");

                // Clears the previously touched position
                mMap.clear();

                // Animating to the touched position
                mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));

                // Placing a marker on the touched position
                mMap.addMarker(markerOptions);

                //Get Marker
                markerPosition = markerOptions.getPosition();

                marked = true;

                //Toast.makeText(getApplicationContext(), markerPosition.toString(), Toast.LENGTH_SHORT).show();
                Toast.makeText(getApplicationContext(), "Placed Secret Santa", Toast.LENGTH_SHORT).show();
                getURLMaps(markerPosition);
            }
        });
    }

    private String getURLMaps(LatLng latLng) {
        String s = "http://maps.google.com/maps?q="+latLng.latitude+","+latLng.longitude+"";
        Log.v("URL MAP", s);
        return s;
    }


    @Override
    public void onLocationChanged(Location loc) {

        if (!marked) {
            mMap.clear();
            String longitude = "Longitude: " + loc.getLongitude();
            Log.v("LONG", longitude);
            String latitude = "Latitude: " + loc.getLatitude();
            Log.v("LAT", latitude);

            markerPosition = new LatLng(loc.getLatitude(), loc.getLongitude());
            mMap.animateCamera(CameraUpdateFactory.newLatLng(markerPosition));
            mMap.addMarker(new MarkerOptions().position(new LatLng(loc.getLatitude(), loc.getLongitude())).title("Secret Santa Here"));
        }

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }


}
