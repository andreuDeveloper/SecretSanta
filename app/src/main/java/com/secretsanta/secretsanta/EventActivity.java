package com.secretsanta.secretsanta;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.util.Date;
import java.util.Random;

public class EventActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);


        //**// On create things
        bindRandomParticipants();

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
