package com.secretsanta.secretsanta;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class ParticipantListActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new ParticipantListFragment();
    }


}
