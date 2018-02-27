package com.secretsanta.secretsanta;


import android.app.FragmentTransaction;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Date;
import java.util.Random;

public class EventActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);


        //**// On create things
        //bindRandomParticipants();

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
}
