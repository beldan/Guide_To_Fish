package beldan.guidetofish.Settings;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.RequestPasswordResetCallback;

import java.util.List;

import beldan.guidetofish.R;


public class User_Login extends Activity implements View.OnClickListener {

    TextView currentUserTextView;
    EditText usernameEdittext;
    EditText passwordEditText;
    Button loginButton;
    Button createUserButton;
    Button forgotButton;
    ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user__login);

        currentUserTextView = (TextView) findViewById(R.id.user_login_current);
        usernameEdittext = (EditText) findViewById(R.id.user_login_username);
        passwordEditText = (EditText) findViewById(R.id.user_login_password);
        loginButton = (Button) findViewById(R.id.user_login_login);
        createUserButton = (Button) findViewById(R.id.user_login_create);
        forgotButton = (Button) findViewById(R.id.user_login_forgot_password);

        loginButton.setOnClickListener(this);
        createUserButton.setOnClickListener(this);
        forgotButton.setOnClickListener(this);

        getCurrentUser();

    }

    @Override
    protected void onResume() {
        super.onResume();
        getCurrentUser();
    }

    private void getCurrentUser() {
        Parse.initialize(this, "T8y1HoeUayRfOIBI7Kp3h3lmflj1GcjiEqbHUuqU", "8KcnaflyuA9KFcct3clusEJa7ItU4Bz6aNgsFYCx");

        ParseUser currentParseUser = ParseUser.getCurrentUser();
        if (currentParseUser == null) {
            currentUserTextView.setText("No User, Please Sign In");
        } else {
            currentUserTextView.setText("Current User: " + " " + currentParseUser.getUsername());
        }
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_user__login, menu);
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
        if (v == loginButton) {
            progress = new ProgressDialog(this);
            progress.setTitle("Logging In");
            progress.show();
            processFields();

        } else if (v == createUserButton) {
            Intent intent = new Intent(this, User_Create.class);
            startActivity(intent);
        } else if (v == forgotButton) {
            AlertDialog.Builder builder = new AlertDialog.Builder(User_Login.this);
            builder.setTitle("Forgot Login");
            builder.setMessage("Please enter your email to recover username / password");
            final EditText input = new EditText(this);
            builder.setView(input);
            builder.setPositiveButton("Forgot Username", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    progress = new ProgressDialog(User_Login.this);
                    progress.setTitle("Getting Username");
                    progress.show();
                    String inputText = input.getText().toString();
                    forgotUsername(inputText);
                }
            });
            builder.setNeutralButton("Forgot Password", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    progress = new ProgressDialog(User_Login.this);
                    progress.setTitle("Getting Password");
                    progress.show();
                    String inputText = input.getText().toString();
                    forgotPassword(inputText);
                }
            });
            builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            AlertDialog alert = builder.create();
            alert.show();
        }
    }

    private void forgotPassword(String inputText) {
        ParseUser.requestPasswordResetInBackground(inputText, new RequestPasswordResetCallback() {
            @Override
            public void done(ParseException e) {
                progress.dismiss();
                if (e == null) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(User_Login.this);
                    builder.setTitle("Success");
                    builder.setMessage("An email has been sent to reset your password.");

                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

                    AlertDialog alert = builder.create();
                    alert.show();
                }
                else {
                    Log.d("TAG", "Error: " + e.toString());
                }
            }
        });
    }

    private void forgotUsername(String inputText) {
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereMatches("email", inputText);
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> parseUsers, ParseException e) {
                progress.dismiss();
                if (e != null) {
                    Log.d("TAG", "there was an error: " + e);
                } else {
                    for (ParseUser user : parseUsers) {
                        final String name = user.getString("username");
                        AlertDialog.Builder builder = new AlertDialog.Builder(User_Login.this);
                        builder.setTitle("Success");
                        builder.setMessage("Your username is: " + name);

                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                usernameEdittext.setText(name);
                                dialog.cancel();
                            }
                        });

                        AlertDialog alert = builder.create();
                        alert.show();
                    }
                }
            }
        });
    }

    private void processFields() {
        final String usernameText = usernameEdittext.getText().toString();
        final String passwordText = passwordEditText.getText().toString();
        String noUsernameText = "username";
        String noPasswordText = "password";
        String errorText = "No";
        String errorTextJoin = " or ";
        String errorTextEnding = " entered";
        boolean textError = false;

        if (usernameText.length() == 0 || passwordText.length() == 0) {
            textError = true;

            if (passwordText.length() == 0) {
                passwordEditText.requestFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(passwordEditText, InputMethodManager.SHOW_IMPLICIT);
            }
            if (usernameText.length() == 0) {
                usernameEdittext.requestFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(usernameEdittext, InputMethodManager.SHOW_IMPLICIT);
            }
        }

        if (usernameText.length() == 0) {
            textError = true;
            errorText = String.format(errorText + noUsernameText);
        }
        if (passwordText.length() == 0) {
            textError = true;
            if (usernameText.length() == 0) {
                errorText = String.format(errorText + errorTextJoin);
            }
            errorText = String.format(errorText + noPasswordText);
        }

        if (textError) {
            errorText = String.format(errorText + errorTextEnding);
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

        ParseUser.logInInBackground(usernameText, passwordText, new LogInCallback() {
            @Override
            public void done(ParseUser parseUser, ParseException e) {
                if (parseUser != null) {
                    String currentUser = parseUser.getUsername();
                    currentUserTextView.setText(currentUser);
                    passwordEditText.setText("");
                    usernameEdittext.setText("");
                    progress.dismiss();
                } else {
                    if (e == null) {
                        progress.dismiss();
                        AlertDialog.Builder builder = new AlertDialog.Builder(User_Login.this);
                        builder.setTitle("Error");
                        builder.setMessage("Could not Login, please check your username and password, and try again.");
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        AlertDialog alert = builder.create();
                        alert.show();
                    } else {
                        progress.dismiss();
                        AlertDialog.Builder builder = new AlertDialog.Builder(User_Login.this);
                        builder.setTitle("Error");
                        builder.setMessage(e.getMessage());
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        AlertDialog alert = builder.create();
                        alert.show();
                    }
                }
            }
        });
    }
}
