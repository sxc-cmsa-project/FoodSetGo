package com.example.foodsetgo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

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

public class SignupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        final TextInputLayout passwordTextInput = findViewById(R.id.password_text_input);
        final TextInputEditText passwordEditText = findViewById(R.id.password_edit_text);
        final EditText emailEditText = findViewById(R.id.email_edit_text);
        final EditText phoneEditText = findViewById(R.id.phone_edit_text);
        final EditText nameEditText = findViewById(R.id.name_edit_text);
        MaterialButton signupButton = findViewById(R.id.signup_button);
        MaterialButton alreadySignedup = findViewById(R.id.already_signed_button);

        //Firebase
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference userTable = database.getReference("User");

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final ProgressDialog mDialog = new ProgressDialog(SignupActivity.this);
                mDialog.setMessage("Please wait...");
                mDialog.show();

                userTable.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if (isValidEmail(emailEditText.getText()))
                        {
                            if (isPhoneValid(phoneEditText.getText())) {
                                if (!isPasswordValid(passwordEditText.getText())) {
                                    mDialog.dismiss();
                                    passwordTextInput.setError(getString(R.string.error_password));
                                } else {
                                    passwordTextInput.setError(null);

                                    if (dataSnapshot.child(phoneEditText.getText().toString()).exists())
                                    {
                                        mDialog.dismiss();
                                        Toast.makeText(SignupActivity.this, "User already registered. Please Login.", Toast.LENGTH_SHORT).show();
                                    }
                                    else
                                    {
                                        mDialog.dismiss();
                                        User user = new User(nameEditText.getText().toString(), passwordEditText.getText().toString(),emailEditText.getText().toString());
                                        userTable.child(phoneEditText.getText().toString()).setValue(user);
                                        Intent i = new Intent(SignupActivity.this, HomeActivity.class);
                                        Common.currentUser = user;
                                        startActivity(i);
                                        finish();
                                        Toast.makeText(SignupActivity.this, "Signed In", Toast.LENGTH_SHORT).show();

                                    }


                                    /**/
                                }
                            }
                            else
                            {
                                mDialog.dismiss();
                                Toast.makeText(SignupActivity.this, "Invalid phone number", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else
                        {
                            mDialog.dismiss();
                            Toast.makeText(SignupActivity.this, "Invalid email address", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        // Clear the error once more than 8 characters are typed.
        passwordEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (isPasswordValid(passwordEditText.getText())) {
                    passwordTextInput.setError(null); //Clear the error
                }
                return false;
            }
        });

        alreadySignedup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SignupActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    private boolean isPasswordValid(@Nullable Editable text) {
        return text != null && text.length() >= 8;
    }

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    private  boolean isPhoneValid(CharSequence target) {
        String MobilePattern = "[0-9]{10}";
        return (!TextUtils.isEmpty(target) && target.toString().matches(MobilePattern));
    }
}
