package com.secretsanta.secretsanta;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import de.hdodenhof.circleimageview.CircleImageView;

import com.github.clans.fab.FloatingActionButton;


import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by Andreu on 31/01/2018.
 */

public class ParticipantListFragment extends Fragment {

    private RecyclerView participantRecyclerView;
    private FloatingActionButton btnAddNewParticipant;
    private ParticipantAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_participant_list, container, false);

        participantRecyclerView = (RecyclerView) view.findViewById(R.id.participant_recycler_view);
        participantRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        this.btnAddNewParticipant = (FloatingActionButton) view.findViewById(R.id.btnAddNewParticipant);
        this.btnAddNewParticipant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewParticipant(v);
            }
        });

        updateUI();

        return view;
    }

    @Override
    public void onResume(){
        super.onResume();
        updateUI();
    }

    public void addNewParticipant(View v){
        Intent i = new Intent(this.getActivity(), ParticipantActivity.class);
        this.startActivity( i );
    }

    private void updateUI() {
        ParticipantLab participantLab = ParticipantLab.get(getContext());
        List<Person> participants = participantLab.getParticipants();
        mAdapter = new ParticipantAdapter(participants);
        participantRecyclerView.setAdapter(mAdapter);
    }

    private class ParticipantHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        //public TextView mTitleTextView;

        private CircleImageView imagePerson;
        private TextView txtNamePerson;

        private Person mPerson;

        public void bindPerson(Person person) {
            mPerson = person;
            txtNamePerson.setText(mPerson.getName());
            if (mPerson.hasImage()) {
                imagePerson.setImageBitmap(mPerson.getImage());
            }
        }

        public ParticipantHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            txtNamePerson = (TextView) itemView.findViewById(R.id.list_name);
            imagePerson = (CircleImageView) itemView.findViewById(R.id.list_picture);
        }

        @Override
        public void onClick(View v) {
            Toast.makeText(getActivity(),mPerson.getName() + " clicked!", Toast.LENGTH_SHORT).show();
            Intent intent = ParticipantActivity.newIntent(getActivity(), mPerson.getId());
            startActivity(intent);
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
                    .inflate(R.layout.list_item_participants, parent, false);
            return new ParticipantHolder(view);
        }
        @Override
        public void onBindViewHolder(ParticipantHolder holder, int position) {
            Person p = lParticipants.get(position);
            //holder.mTitleTextView.setText(p.getName());
            holder.bindPerson(p);
        }
        @Override
        public int getItemCount() {
            return lParticipants.size();
        }
    }
}
