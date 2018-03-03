package com.secretsanta.secretsanta;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class EventActivity extends FragmentActivity {
    private EditText txtDate;
    private EditText txtHour;
    private EditText txtMaxPrice;
    private EditText txtMinPrice;
    private FloatingActionButton btnListDone;
    private FloatingActionButton btnGoBack;
    private ScrollView scrollView;


    public static String urlMap = "";

    private Button btnUbi;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);




        this.txtDate = findViewById(R.id.txtdate);
        this.txtHour =  findViewById(R.id.txtHour);
        this.txtMaxPrice =  findViewById(R.id.maxPrice);
        this.scrollView = findViewById(R.id.scrollView);
        this.txtMinPrice =  findViewById(R.id.minPrice);

        this.btnUbi = findViewById(R.id.btnSetUbication);



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

                if (isInternetConnection()) {
                    if (checkFieldsNotNull(txtDate) && checkFieldsNotNull(txtMaxPrice) && checkFieldsNotNull(txtMinPrice)
                            && checkDateValid()) {
                        yesNoDialog();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Please, check your internet connection", Toast.LENGTH_SHORT).show();
                }

            }
        });

        this.btnUbi.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent i=new Intent(getApplicationContext(),EventMActivity.class);

                startActivity(i);

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

            pl.clear();
            this.btnListDone.setEnabled(false);
        } catch (Exception e) {
            Log.e("ERROR", "->", e.getCause());
        }
    }

    private void doSecretSanta(Person personTo, Person personFrom) {
        String email = personFrom.getEmail().toLowerCase().trim();
        String subject = "Secret Santa Event "+personFrom.getName().trim();

        String htmlMessage = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String now=sdf.format(new Date());

        if (urlMap != "") {
            htmlMessage = "" +
                    "<h1>SECRET SANTA</h1>" +
                    "<h3>Event created: " + now + "</h3>" +
                    "<hr>" +
                    "<h4>Date: " + this.txtDate.getText() + "</h4>" +
                    "<h4>Hour: " + this.txtHour.getText() + " <h4>" +
                    "<h4>Price: " + this.txtMinPrice.getText() + "€ --- " + this.txtMaxPrice.getText() + "€</h4>" +
                    "<h4>Ubication: <h4>" +
                    "<a href=" + urlMap + ">" +
                    "<img src=\"http://www.myiconfinder.com/uploads/iconsets/256-256-9c7aae955a2bcc1811b64c019bd3df28.png\" alt=\"Event ubication\" height=\"100\" width=\"100\"/>" +
                    "</a>" +
                    "<hr>" +
                    "<h4>Your person is.... " + personTo.getName().toUpperCase() + "!!</h4>" +
                    "<p>Don't worry, " + personTo.getName() + " gave us some information to help you to choose the present:</p>" +
                    "<ul>" +
                    "<li>Age: " + personTo.getAge() + "</li>" +
                    "<li>Birthday: " + personTo.getBirthday() + "</li>" +
                    "<li>Likes: " + personTo.getLikes() + "</li>" +
                    "</ul>" +
                    "<hr>" +
                    "<h4>Thanks for use our application!</h4>" +
                    "<h4>Secret Santa <3</h4>" +
                    "";
        } else {
            htmlMessage = "" +
                    "<h1>SECRET SANTA</h1>" +
                    "<h3>Event created: " + now + "</h3>" +
                    "<hr>" +
                    "<h4>Date: " + this.txtDate.getText() + "</4>" +
                    "<h4>Hour: " + this.txtHour.getText() + " <h4>" +
                    "<h4>Price: " + this.txtMinPrice.getText() + "€ --- " + this.txtMaxPrice.getText() + "€</h4>" +
                    "<h4>Ubication: Not selected<h4>" +
                    "<hr>" +
                    "<h4>Your person is.... " + personTo.getName().toUpperCase() + "!!</h4>" +
                    "<p>Don't worry, " + personTo.getName() + " gave us some information to help you to choose the present:</p>" +
                    "<ul>" +
                    "<li>Age: " + personTo.getAge() + "</li>" +
                    "<li>Birthday: " + personTo.getBirthday() + "</li>" +
                    "<li>Likes: " + personTo.getLikes() + "</li>" +
                    "</ul>" +
                    "<hr>" +
                    "<h4>Thanks for use our application!</h4>" +
                    "<h4>Secret Santa <3</h4>" +
                    "";
        }



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
        builder.setMessage("Are you sure?\n\nPlease, be patient!");

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

    private boolean isInternetConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            return  true;
        }
        else
            return  false;
    }


}
