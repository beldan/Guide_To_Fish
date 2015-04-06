package beldan.guidetofish.Settings;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.parse.Parse;
import com.parse.ParseUser;

import beldan.guidetofish.R;


public class SettingsActivity extends Activity implements View.OnClickListener {

    Button metricButton;
    Button imperialButton;
    Button changeUserButton;
    TextView userTextView;

    String username = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        metricButton = (Button)findViewById(R.id.settings_metric_button);
        metricButton.setOnClickListener(this);
        imperialButton = (Button)findViewById(R.id.settings_imperial_button);
        imperialButton.setOnClickListener(this);
        changeUserButton = (Button) findViewById(R.id.settings_change_user_button);
        changeUserButton.setOnClickListener(this);
        userTextView = (TextView) findViewById(R.id.settings_userText);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        String measurement = prefs.getString(getString(R.string.measurement_pref), "");
        Log.d("TAG", measurement);

        if (measurement.equalsIgnoreCase("METRIC")) {
            metricButton.setBackgroundColor(Color.WHITE);
            metricButton.hasFocus();
        } else if (measurement.equalsIgnoreCase("IMPERIAL")) {
            imperialButton.hasFocus();
            imperialButton.setBackgroundColor(Color.WHITE);
        }

        getCurrentUser();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getCurrentUser();
    }

    private void getCurrentUser() {
        Parse.initialize(this, "T8y1HoeUayRfOIBI7Kp3h3lmflj1GcjiEqbHUuqU", "8KcnaflyuA9KFcct3clusEJa7ItU4Bz6aNgsFYCx");
        ParseUser user = ParseUser.getCurrentUser();
        if (user != null) {
            username = user.getUsername();
        } else {
            username = "No User, Please Login";
        }

        userTextView.setText(username);
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_settings, menu);
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
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();

        if (v == metricButton) {
            Log.d("TAG", "metric selected");
            imperialButton.setBackgroundResource(R.drawable.borders);
            metricButton.setBackgroundColor(Color.WHITE);
            editor.putString(getString(R.string.measurement_pref), "METRIC");
            editor.apply();
            metricButton.hasFocus();
        } else if (v == imperialButton) {
            Log.d("TAG", "imperial selected");
            metricButton.setBackgroundResource(R.drawable.borders);
            imperialButton.setBackgroundColor(Color.WHITE);
            editor.putString(getString(R.string.measurement_pref), "IMPERIAL");
            editor.apply();
            imperialButton.hasFocus();
        } else if (v == changeUserButton) {
            Intent i = new Intent(this, User_Login.class);
            startActivity(i);
        }
    }
}
