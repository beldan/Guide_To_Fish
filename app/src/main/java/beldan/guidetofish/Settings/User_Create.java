package beldan.guidetofish.Settings;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import beldan.guidetofish.R;


public class User_Create extends ActionBarActivity implements View.OnClickListener, View.OnTouchListener {

    EditText username;
    EditText email;
    EditText password;
    EditText repeatPassword;
    Button createUserButton;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user__create);

        username = (EditText) findViewById(R.id.user_create_username);
        username.setOnTouchListener(this);
        email = (EditText) findViewById(R.id.user_create_email);
        email.setOnTouchListener(this);
        password = (EditText) findViewById(R.id.user_create_password);
        password.setOnTouchListener(this);
        repeatPassword = (EditText) findViewById(R.id.user_create_repeat_password);
        repeatPassword.setOnTouchListener(this);
        createUserButton = (Button) findViewById(R.id.user_create_createButton);
        createUserButton.setOnClickListener(this);
        createUserButton.setClickable(false);
        createUserButton.setEnabled(false);

        checkFieldEntries();
    }

    private void checkFieldEntries() {
        if (createUserButton.isEnabled()) {
            return;
        } else {
            if (username.length() > 0 && email.length() > 0 && password.length() > 0) {
                createUserButton.setEnabled(true);
                createUserButton.setClickable(true);
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_user__create, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if (v == createUserButton) {
            dialog = new ProgressDialog(this);
            dialog.setTitle("Creating User Account");
            dialog.show();
            processFieldEntries();
        }
    }

    private void processFieldEntries() {
        String emailText = email.getText().toString();
        final String usernameText = username.getText().toString();
        final String passwordText = password.getText().toString();
        String repeatPasswordText = repeatPassword.getText().toString();
        String errorText = "Please ";
        String usernameBlankText = "enter a Username";
        String emailBlankText = "enter an Email";
        String passwordBlankText = "enter a Password";
        String joinText = ", and ";
        String passwordMismatchText = "enter the same password twice";

        boolean textError = false;

        if (usernameText.length() == 0 || emailText.length() == 0 || passwordText.length() == 0 || repeatPasswordText.length() == 0) {
            textError = true;

            if (usernameText.length() == 0) {
                errorText = String.format(errorText + usernameBlankText);
                username.requestFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(username, InputMethodManager.SHOW_IMPLICIT);
            }
            if (emailText.length() == 0) {
                errorText = String.format(errorText + emailBlankText);
                email.requestFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(email, InputMethodManager.SHOW_IMPLICIT);
            }
            if (passwordText.length() == 0) {
                errorText = String.format(errorText + passwordBlankText);
                password.requestFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(password, InputMethodManager.SHOW_IMPLICIT);
            }
            if (repeatPasswordText.length() == 0) {
                errorText = String.format(errorText + passwordMismatchText);
                repeatPassword.requestFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(repeatPassword, InputMethodManager.SHOW_IMPLICIT);
            }
        } else if (!passwordText.equalsIgnoreCase(repeatPasswordText)) {
            textError = true;
            errorText = String.format(errorText + passwordMismatchText);
            password.requestFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(password, InputMethodManager.SHOW_IMPLICIT);
        }
        if (textError) {
            dialog.dismiss();
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(errorText);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            AlertDialog alert = builder.create();
            alert.show();
        }

        ParseUser newUser = new ParseUser();
        newUser.setUsername(usernameText);
        newUser.setEmail(emailText);
        newUser.setPassword(passwordText);
        newUser.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    dialog.dismiss();
                    AlertDialog.Builder builder = new AlertDialog.Builder(User_Create.this);
                    builder.setTitle(e.getMessage());
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                } else {
                    ParseUser.logInInBackground(usernameText, passwordText, new LogInCallback() {
                        @Override
                        public void done(ParseUser parseUser, ParseException e) {
                            if (e == null) {
                                dialog.dismiss();
                                AlertDialog.Builder builder = new AlertDialog.Builder(User_Create.this);
                                builder.setTitle("Success");
                                builder.setMessage("You have successfully signed up. Would you like to create your profile now?");
                                /*
                                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        //create intent to create profile

                                        Intent intent = new Intent(User_Create.this, Profile_Create_Activity.class);
                                        startActivity(intent);

                                    }
                                });
                                */
                                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                                AlertDialog alert = builder.create();
                                alert.show();
                            }
                        }
                    });

                }
            }
        });
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        checkFieldEntries();
        return false;

    }
}
