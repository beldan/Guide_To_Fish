package beldan.guidetofish;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import beldan.guidetofish.AddCatch.AddCatchActivity;
import beldan.guidetofish.AllCatches.AllCatchesActivity;
import beldan.guidetofish.CaughtFish.CaughtFishActivity;
import beldan.guidetofish.Settings.SettingsActivity;


public class MainMenu extends ActionBarActivity {

    LinearLayout allCatchesButton;
    LinearLayout addCatchButton;
    LinearLayout myCatchesButton;
    LinearLayout weatherButton;
    LinearLayout friendsButton;
    LinearLayout bragWallButton;
    LinearLayout profileButton;
    LinearLayout fishDataButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        setOnClickListeners();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void setOnClickListeners() {
        //set the onclick listeners for the buttons
        allCatchesButton = (LinearLayout)findViewById(R.id.button1);
        allCatchesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainMenu.this, AllCatchesActivity.class);
                startActivity(i);
            }
        });
        addCatchButton = (LinearLayout)findViewById(R.id.button2);
        addCatchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainMenu.this, AddCatchActivity.class);
                startActivity(i);
            }
        });
        myCatchesButton = (LinearLayout)findViewById(R.id.button3);
        myCatchesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainMenu.this, CaughtFishActivity.class);
                startActivity(i);
            }
        });
        weatherButton = (LinearLayout)findViewById(R.id.button4);
        weatherButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainMenu.this, WeatherActivity.class);
                startActivity(i);
            }
        });
        friendsButton = (LinearLayout)findViewById(R.id.button5);
        friendsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainMenu.this, FriendsActivity.class);
                startActivity(i);
            }
        });
        bragWallButton = (LinearLayout)findViewById(R.id.button6);
        bragWallButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainMenu.this, BragWallActivity.class);
                startActivity(i);
            }
        });
        profileButton = (LinearLayout)findViewById(R.id.button7);
        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainMenu.this, ProfileActivity.class);
                startActivity(i);
            }
        });
        fishDataButton = (LinearLayout)findViewById(R.id.button8);
        fishDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainMenu.this, FishDataActivity.class);
                startActivity(i);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_menu, menu);
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
            Intent i = new Intent(this, SettingsActivity.class);
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
