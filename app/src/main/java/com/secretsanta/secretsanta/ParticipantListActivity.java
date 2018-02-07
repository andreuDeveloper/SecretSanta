package com.secretsanta.secretsanta;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class ParticipantListActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new ParticipantListFragment();
    }
}
