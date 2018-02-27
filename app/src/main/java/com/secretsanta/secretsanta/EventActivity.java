package com.secretsanta.secretsanta;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class EventActivity extends AppCompatActivity {
    private EditText txtDate;
    private EditText txtHour;
    private EditText txtMaxPrice;
    private EditText txtMinPrice;
    private FloatingActionButton btnListDone;
    private FloatingActionButton btnGoBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);


        this.txtDate = findViewById(R.id.txtdate);
        this.txtHour =  findViewById(R.id.txtHour);
        this.txtMaxPrice =  findViewById(R.id.maxPrice);
        this.txtMinPrice =  findViewById(R.id.minPrice);

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
                "<h5>Place: </h5>" +
                "<h5>Price: MIN PRICE €--- MAX PRICE €</h5>" +
                "<h5>Price: HOUR <h5>" +
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
        builder.setMessage("Are you sure?");

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
}
