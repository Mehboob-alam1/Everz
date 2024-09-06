package com.example.everz;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class ContactFragment extends Fragment {

    private ImageView imgNewsContact;
    private TextView txtMainHeadingContact, txtContentContact;

    private DatabaseReference databaseReference;
    Blog blog;
    private EditText etFullName, etEmailAddress, etPhoneNumber, etCompanyName, etSubject, etMessage;

    private Button btnSend;
    String value="";

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_contact, container, false);


        imgNewsContact = view.findViewById(R.id.imgNewsContact);
        txtMainHeadingContact = view.findViewById(R.id.txtMainHeadingContact);
        txtContentContact = view.findViewById(R.id.txtContentContact);

        etFullName = view.findViewById(R.id.etFullName);
        etEmailAddress = view.findViewById(R.id.etEmailContact);
        etPhoneNumber = view.findViewById(R.id.etPhoneNumberContact);
        etCompanyName = view.findViewById(R.id.etCompanyName);
        etSubject = view.findViewById(R.id.etYourSubject);
        etMessage = view.findViewById(R.id.etMessage);
        btnSend = view.findViewById(R.id.btnSendMessage);


        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendEmail();
            }
        });
        databaseReference = FirebaseDatabase.getInstance().getReference("blogs").child("Contact");


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    for (DataSnapshot snap : snapshot.getChildren()) {
                        blog = snap.getValue(Blog.class);


                    }
                    Glide.with(requireContext())
                            .load(blog.getImageUrl())
                            .placeholder(R.drawable.simuico)
                            .into(imgNewsContact);
                    txtMainHeadingContact.setText(blog.getTitle());
                    txtContentContact.setText(blog.getDescription());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        readEmail();

        return view;
    }

    private void readEmail() {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("contact");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                     value = snapshot.getValue(String.class);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void sendEmail() {
        String fullName = etFullName.getText().toString();
        String emailAddress = etEmailAddress.getText().toString();
        String phoneNumber = etPhoneNumber.getText().toString();
        String companyName = etCompanyName.getText().toString();
        String subject = etSubject.getText().toString();
        String message = etMessage.getText().toString();

        String emailBody = "Full Name: " + fullName + "\n"
                + "Email Address: " + emailAddress + "\n"
                + "Phone Number: " + phoneNumber + "\n"
                + "Company Name: " + companyName + "\n"
                + "Message: " + message;

        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("plain/text");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{value});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        emailIntent.putExtra(Intent.EXTRA_TEXT, emailBody);

        try {
            startActivity(Intent.createChooser(emailIntent, "Send email..."));
        } catch (android.content.ActivityNotFoundException ex) {
            // Handle the case where no email app is available
        }
    }

}