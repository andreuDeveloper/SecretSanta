package com.secretsanta.secretsanta;


import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import java.util.UUID;


public class ParticipantActivity extends SingleFragmentActivity {

    public static final String EXTRA_PARTICIPANT_ID = "participantID";
    public static Intent newIntent(Context packageContext, UUID id) {
        Intent intent = new Intent(packageContext, ParticipantActivity.class);
        intent.putExtra(EXTRA_PARTICIPANT_ID, id);
        return intent;
    }

    @Override
    protected Fragment createFragment() {
        return new ParticipantFragment();
    }
}
