package com.secretsanta.secretsanta;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

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
        String email = personFrom.getEmail().toLowerCase().trim();
        String subject = "Secret Santa Event "+personFrom.getName().trim();
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

        Log.v("SECRET: ", personTo.getName()+" -> "+personFrom.getName());
        SendMail sm = new SendMail(this, email, subject, message);

        sm.execute();
    }
}
