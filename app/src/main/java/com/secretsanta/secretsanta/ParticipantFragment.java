package com.secretsanta.secretsanta;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;
import com.github.clans.fab.FloatingActionButton;
import java.util.Calendar;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_CANCELED;


public class ParticipantFragment extends Fragment {

    private FloatingActionButton btnSaveParticipant;
    private EditText txtPersonName;
    private EditText txtPersonBirthday;
    private EditText txtPersonEmail;
    private EditText txtPersonLikes;
    private CircleImageView imgProfile;
    private Bitmap bitmapPhoto;
    private boolean pictureDone = false;
    private int age;

    private Person mParticipant = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID participantId = (UUID) getActivity().getIntent()
                .getSerializableExtra(ParticipantActivity.EXTRA_PARTICIPANT_ID);

        if (participantId != null) {
            mParticipant = ParticipantLab.get(getActivity()).getParticipant(participantId);
            Toast.makeText(getContext(), "Visualizar", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "Agregar nuevo", Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_participant, container, false);

        //Areas
        this.txtPersonName = (EditText) v.findViewById(R.id.person_name_text);
        this.txtPersonBirthday = (EditText) v.findViewById(R.id.txtdate);
        this.txtPersonEmail = (EditText) v.findViewById(R.id.email_address_text);
        this.txtPersonLikes = (EditText) v.findViewById(R.id.likes_text);
        this.btnSaveParticipant = (FloatingActionButton) v.findViewById(R.id.btnSaveParticipant);
        this.btnSaveParticipant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveNewParticipant();
            }
        });
        this.imgProfile = (CircleImageView) v.findViewById(R.id.profile_image);
        this.imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doPicture();
            }
        });

        if (mParticipant != null) {
            completeFiledsWithPersonInfo();
        }





        return v;
    }

    private void completeFiledsWithPersonInfo(){
        Log.d("AAAAA","Entrado porque persona no es nula");
        Log.d("AAAAA", mParticipant.getName());
        imgProfile.setImageBitmap(mParticipant.getImage());
        txtPersonName.setText(mParticipant.getName());
        //txtPersonBirthday.setText(mParticipant.getAge());
        txtPersonEmail.setText(mParticipant.getEmail());
        //txtPersonLikes.setText(mParticipant.getLikes());
    }

    private void saveNewParticipant(){
        if (checkFieldsNotNull(txtPersonName) && checkFieldsNotNull(txtPersonBirthday) && checkFieldsNotNull(txtPersonEmail)
                && checkDateValid() && isEmailValid(txtPersonEmail.getText().toString())) {

                    ParticipantLab participantLab = ParticipantLab.get(getContext());
                    Person p = new Person();
                    p.setName(txtPersonName.getText().toString());
                    p.setAge(age);
                    p.setEmail(txtPersonEmail.getText().toString());
                    if (pictureDone) {
                        p.setImage(bitmapPhoto);
                    } else {
                        p.setImage(BitmapFactory.decodeResource(getResources(), R.mipmap.photo));
                    }

                    participantLab.addParticipant(p);
                    Toast.makeText(getContext(), "Added successfully", Toast.LENGTH_LONG).show();


                    Intent i = new Intent(getActivity(), ParticipantListActivity.class);
                    startActivity(i);
                    getActivity().finish();
        }
    }


    private boolean checkFieldsNotNull(EditText editText){
        if (editText.getText().toString().length() == 0){
            editText.requestFocus();
            Toast.makeText(getContext(), "Complete the field", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private boolean checkDateValid(){
        int day = Integer.parseInt(txtPersonBirthday.getText().toString().substring(0,txtPersonBirthday.getText().toString().indexOf("-")));
        int month = Integer.parseInt(txtPersonBirthday.getText().toString().substring(txtPersonBirthday.getText().toString().indexOf("-")+1,
                txtPersonBirthday.getText().toString().indexOf("-",txtPersonBirthday.getText().toString().indexOf("-")+1)));
        int year = Integer.parseInt(txtPersonBirthday.getText().toString().substring(txtPersonBirthday.getText().toString().indexOf("-",txtPersonBirthday.getText().toString().indexOf("-")+1)+1,
                txtPersonBirthday.getText().toString().length()));

        age = getAge(year, month, day);

        if (age > 0) {
            return true;
        } else {
            Toast.makeText(getContext(), "Insert a valid birthday", Toast.LENGTH_LONG).show();
            txtPersonBirthday.requestFocus();
            return false;
        }
    }

    private boolean isEmailValid(String email) {
        if (android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return true;
        } else{
            Toast.makeText(getContext(), "Insert a valid email", Toast.LENGTH_LONG).show();
            txtPersonEmail.requestFocus();
            return false;
        }
    }

    public void onStart(){
        super.onStart();

        EditText txtDate=(EditText) getView().findViewById(R.id.txtdate);
        txtDate.setKeyListener(null);
        txtDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    DateDialog dialog = new DateDialog(view);
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    dialog.show(ft, "DatePicker");
                }
            }
        });

        txtDate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                DateDialog dialog = new DateDialog(v);
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                dialog.show(ft, "DatePicker");
            }
        });

    }


    private int getAge(int year, int month, int day){
        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        dob.set(year, month, day);

        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)){
            age--;
        }
        return age;
    }

    private void doPicture() {
        //Intent takePictureIntent = new Intent("android.media.action.IMAGE_CAPTURE");
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(takePictureIntent, 6666);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,requestCode,data);
        if(resultCode != RESULT_CANCELED) {
            bitmapPhoto = (Bitmap) data.getExtras().get("data");
            this.imgProfile.setImageBitmap(bitmapPhoto);
            pictureDone = true;
        }
    }

}
