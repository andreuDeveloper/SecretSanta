package com.secretsanta.secretsanta;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Andreu on 06/02/2018.
 */

public class ParticipantLab {

    private static ParticipantLab sParticipantLab;
    private List<Person> lParticipants;

    public static ParticipantLab get(Context context) {
        if (sParticipantLab == null) {
            sParticipantLab = new ParticipantLab(context);
        }
        return sParticipantLab;
    }
    private ParticipantLab(Context context) {
        this.lParticipants = new ArrayList<>();
        for (int i = 0; i < 2; i++){
            Person p = new Person();
            p.setName("name"+i);
            p.setAge(10+ i*3);
            this.lParticipants.add(p);
        }
    }

    public Person getParticipant(UUID id) {
        for (Person p : lParticipants) {
            if (p.getId().equals(id)) {
                return p;
            }
        }
        return null;
    }


    public List getParticipants(){
        return this.lParticipants;
    }


    public void addParticipant(Person p){
        this.lParticipants.add(p);
    }

    public void removeParticipant(Person p){
        this.lParticipants.remove(p);
    }


}
