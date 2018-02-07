package com.secretsanta.secretsanta;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Andreu on 31/01/2018.
 */

public class ParticipantListFragment extends Fragment{

    private RecyclerView participantRecyclerView;
    private ParticipantAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_participant_list, container, false);
        participantRecyclerView = (RecyclerView) view.findViewById(R.id.participant_recycler_view);
        participantRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();

        return view;
    }

    private void updateUI() {
        ParticipantLab participantLab = ParticipantLab.get(getActivity());
        List<Person> crimes = participantLab.getParticipants();
        mAdapter = new ParticipantAdapter(crimes);
        participantRecyclerView.setAdapter(mAdapter);
    }

    private class ParticipantHolder extends RecyclerView.ViewHolder {
        public TextView mTitleTextView;
        public ParticipantHolder(View itemView) {
            super(itemView);
            mTitleTextView = (TextView) itemView;
        }
    }

    private class ParticipantAdapter extends RecyclerView.Adapter<ParticipantHolder> {
        private List<Person> lParticipants;
        public ParticipantAdapter(List<Person> participants) {
            lParticipants = participants;
        }

        @Override
        public ParticipantHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater
                    .inflate(android.R.layout.simple_list_item_1, parent, false);
            return new ParticipantHolder(view);
        }
        @Override
        public void onBindViewHolder(ParticipantHolder holder, int position) {
            Person p = lParticipants.get(position);
            holder.mTitleTextView.setText(p.getName());
        }
        @Override
        public int getItemCount() {
            return lParticipants.size();
        }
    }
}
