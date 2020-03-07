package com.example.foodsetgo;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.foodsetgo.Commom.Common;
import com.example.foodsetgo.Model.User;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

import static android.content.Context.MODE_PRIVATE;

/**
 * Fragment representing the login screen for FoodSetGo.
 */

public class LoginActivity extends Fragment {


    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        final View view = inflater.inflate(R.layout.login_activity, container, false);
        final TextInputLayout passwordTextInput = view.findViewById(R.id.password_text_input);
        final TextInputEditText passwordEditText = view.findViewById(R.id.password_edit_text);
        final EditText phoneEditText = view.findViewById(R.id.phone_edit_text);
        MaterialButton loginButton = view.findViewById(R.id.login_button);
        MaterialButton newSignupButton = view.findViewById(R.id.new_signup_button);

        Boolean loggedIn = false;

        //Firebase
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference userTable = database.getReference("User");

        Paper.init(getContext());

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog mDialog = new ProgressDialog(LoginActivity.this.getContext());
                mDialog.setMessage("Please wait...");
                if(isNetworkAvailable())
                {
                    mDialog.show();
                    userTable.addValueEventListener(new ValueEventListener() {

                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {


                            if (dataSnapshot.child(phoneEditText.getText().toString()).exists())
                            {
                                mDialog.dismiss();
                                User user = dataSnapshot.child(phoneEditText.getText().toString()).getValue(User.class);

                                if (user.getPassword().equals(passwordEditText.getText().toString()))
                                {
                                    Paper.book().write(Common.USER_KEY, phoneEditText.getText().toString());
                                    Paper.book().write(Common.PWD_KEY, passwordEditText.getText().toString());
                                    Toast.makeText(getContext(), "Logged In", Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(view.getContext(), HomeActivity.class);
                                    Common.currentUser = user;
                                    startActivity(i);
                                    getActivity().finish();

                                }
                                else
                                {
                                    Toast.makeText(getContext(), "Incorrect Password", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else
                            {
                                mDialog.dismiss();
                                Toast.makeText(getContext() , "User not registered. Please Signup.", Toast.LENGTH_LONG).show();
                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
                else
                {
                    mDialog.dismiss();
                    Toast.makeText(getContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();

                }
            }
        });

        newSignupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(view.getContext(), SignupActivity.class);
                startActivity(i);
                getActivity().finish();
            }
        });

        return view;
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}