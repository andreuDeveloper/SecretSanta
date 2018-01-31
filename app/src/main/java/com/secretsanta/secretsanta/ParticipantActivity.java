package com.secretsanta.secretsanta;


import android.support.v4.app.Fragment;


public class ParticipantActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new ParticipantFragment();
    }
}
