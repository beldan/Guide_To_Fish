package beldan.guidetofish.CaughtFish;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import beldan.guidetofish.CaughtFishDatabaseHandler;
import beldan.guidetofish.Object_ParsedFish;
import beldan.guidetofish.R;
import beldan.guidetofish.Settings.User_Login;


public class CaughtFishDetailsActivity extends ActionBarActivity {

    Object_ParsedFish parsedFish;

    ImageView detailImage;
    TextView dateTextView;
    TextView typeTextView;
    TextView weightTextView;
    TextView lengthTextView;
    TextView latitudeTextView;
    TextView longitudeTextView;
    TextView waterTempTextView;
    TextView pressureTextView;
    TextView pressureTrendTextView;
    TextView waveHeightTextView;
    TextView swellDirection;
    TextView swellHeight;
    TextView humidity;
    TextView baitTextView;
    ImageView moonPhase;
    String username;

    Bitmap myBitmap;

    GoogleMap mMap;

    CaughtFishDatabaseHandler db;

    ProgressDialog pd = null;

    String measurement = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caught_fish_details);

        db = new CaughtFishDatabaseHandler(this);

        detailImage = (ImageView) findViewById(R.id.caught_fish_detail_image);
        dateTextView = (TextView) findViewById(R.id.caught_fish_detail_date);
        weightTextView = (TextView) findViewById(R.id.caught_fish_weight);
        typeTextView = (TextView) findViewById(R.id.caught_fish_detail_type);
        lengthTextView = (TextView) findViewById(R.id.caught_fish_length);
        latitudeTextView = (TextView) findViewById(R.id.caught_fish_latitude);
        longitudeTextView = (TextView) findViewById(R.id.caught_fish_longitude);
        waterTempTextView = (TextView) findViewById(R.id.caught_fish_waterTemp);
        pressureTextView = (TextView) findViewById(R.id.caught_fish_pressure);
        pressureTrendTextView = (TextView) findViewById(R.id.caught_fish_pressure_trend);
        waveHeightTextView = (TextView) findViewById(R.id.caught_fish_waveHeight);
        swellDirection = (TextView) findViewById(R.id.caught_fish_swellDirection);
        swellHeight = (TextView) findViewById(R.id.caught_fish_swellHeight);
        humidity = (TextView) findViewById(R.id.caught_fish_humidity);
        moonPhase = (ImageView) findViewById(R.id.caught_fish_moon);
        baitTextView = (TextView) findViewById(R.id.caught_fish_bait);

        Intent i = getIntent();
        parsedFish = (Object_ParsedFish)i.getSerializableExtra("selected_fish");

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        measurement = prefs.getString(getString(R.string.measurement_pref), "");
        Log.d("TAG", measurement);

        setupViews();
        setupMapIfNeeded();
    }


    private void setupViews() {
        typeTextView.setText(parsedFish.type);
        dateTextView.setText(parsedFish.date);

        Log.d("TAG", measurement);

        if (measurement != null) {
            if (measurement.equalsIgnoreCase("IMPERIAL")) {
                weightTextView.setText(parsedFish.weight + " lb");
                lengthTextView.setText(parsedFish.length + " in");
                swellHeight.setText(parsedFish.swellHeight + " ft");
                waveHeightTextView.setText(parsedFish.waveHeight + " ft");
            } else {
                weightTextView.setText(parsedFish.weight + " kg");
                lengthTextView.setText(parsedFish.length + " cm");
                swellHeight.setText(parsedFish.swellHeight + " m");
                waveHeightTextView.setText(parsedFish.waveHeight + " m");
            }
        }
        baitTextView.setText(parsedFish.bait);
        latitudeTextView.setText(parsedFish.latitude);
        longitudeTextView.setText(parsedFish.longitude);

        //check if there is a valid water temp
        if (parsedFish.waterTemp.equalsIgnoreCase("0.0")) {
            waterTempTextView.setText("No Temp");
        } else {
            if (measurement.equalsIgnoreCase("IMPERIAL")) {
                waterTempTextView.setText(parsedFish.waterTemp + "\u2103");
            } else {
                waterTempTextView.setText(parsedFish.waterTemp + "\u2109");
            }
        }

        //get the pressure trend and set the applicable arrow
        pressureTextView.setText(parsedFish.pressure + " mb");
        Log.d("TAG", "Pressure trend: " + parsedFish.pressureTrend);
        if (parsedFish.pressureTrend != null) {
            if (parsedFish.pressureTrend.equalsIgnoreCase("Rising")) {
                pressureTrendTextView.setText("\u2191");
            } else if (parsedFish.pressureTrend.equalsIgnoreCase("Steady")) {
                pressureTrendTextView.setText("-");
            } else if (parsedFish.pressureTrend.equalsIgnoreCase("Dropping")) {
                pressureTrendTextView.setText("\u2193");
            }
        }
        swellDirection.setText(parsedFish.swellDirection + "\u00B0");
        humidity.setText(parsedFish.humidity + "%");

        //set the photo if there is one
        if (parsedFish.photo != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(parsedFish.photo, 0, parsedFish.photo.length);
            detailImage.setImageBitmap(bitmap);
        } else {
            detailImage.setImageResource(R.drawable.fish);
        }


        //set the moon phase image
        String moon = parsedFish.moonPhase;
        String uri = "drawable/" + moon;
        int imageResource = this.getResources().getIdentifier(uri, null, this.getPackageName());
        moonPhase.setImageResource(imageResource);


    }

    private void setupMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.caught_fish_map))
                    .getMap();
            if (mMap != null) {
                setUpMap();
            }
        } else {
            setUpMap();
        }
    }

    private void setUpMap() {
        double lat = Double.parseDouble(parsedFish.latitude);
        double lng = Double.parseDouble(parsedFish.longitude);
        LatLng position = new LatLng(lat, lng);
        mMap.addMarker(new MarkerOptions().position(position));
        CameraUpdate location = CameraUpdateFactory.newLatLngZoom(position, 10);
        mMap.animateCamera(location);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        getMenuInflater().inflate(R.menu.menu_caught_fish_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.caught_fish_detail_menu_share) {
            Log.d("TAG", "Share selected");

            pd = new ProgressDialog(this);
            pd.setMessage("Please Wait...");
            pd.setTitle("Sharing Catch");
            pd.setIndeterminate(true);
            pd.show();

            getCurrentUser();
            return true;
        } else if (id == R.id.caught_fish_detail_menu_send) {
            View v1 = getWindow().getDecorView().getRootView();
            v1.setDrawingCacheEnabled(true);
            myBitmap = v1.getDrawingCache();
            saveBitmap(myBitmap);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void saveBitmap(Bitmap myBitmap) {
        String filePath = Environment.getExternalStorageDirectory()
                + File.separator + "Pictures/screenshot.png";
        File imagePath = new File(filePath);
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(imagePath);
            myBitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
            sendMail(filePath);
        } catch (FileNotFoundException e) {
            Log.e("TAG", e.getMessage(), e);
        } catch (IOException e) {
            Log.e("TAG", e.getMessage(), e);
        }
    }

    private void sendMail(String filePath) {
        Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
        //emailIntent.putExtra(Intent.EXTRA_EMAIL,
                //new String[] { "receiver@website.com" });
        //emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
        //        "Truiton Test Mail");
        //emailIntent.putExtra(android.content.Intent.EXTRA_TEXT,
        //        "This is an autogenerated mail from Truiton's InAppMail app");
        emailIntent.setType("image/png");
        Uri myUri = Uri.parse("file://" + filePath);
        emailIntent.putExtra(Intent.EXTRA_STREAM, myUri);
        startActivity(Intent.createChooser(emailIntent, "Send fish..."));
    }

    private void getCurrentUser() {
        Parse.initialize(this, "T8y1HoeUayRfOIBI7Kp3h3lmflj1GcjiEqbHUuqU", "8KcnaflyuA9KFcct3clusEJa7ItU4Bz6aNgsFYCx");

        ParseUser currentParseUser = ParseUser.getCurrentUser();
        if (currentParseUser == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Please Log In");
            builder.setMessage("You must be logged in to share your fish.");
            builder.setPositiveButton("LOGIN", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent i = new Intent(CaughtFishDetailsActivity.this, User_Login.class);
                    startActivity(i);
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
        } else {
            username = currentParseUser.getUsername();
            sendToParse();
        }
    }

    private void sendToParse() {
        //todo
        Parse.initialize(this, "T8y1HoeUayRfOIBI7Kp3h3lmflj1GcjiEqbHUuqU", "8KcnaflyuA9KFcct3clusEJa7ItU4Bz6aNgsFYCx");
        ParseObject fish = new ParseObject("Fish");
        fish.put("name", parsedFish.type);
        Log.d("TAG", "species" + parsedFish.water);
        fish.put("species", parsedFish.water);
        fish.put("length", Double.parseDouble(parsedFish.length));
        fish.put("weight", Double.parseDouble(parsedFish.weight));
        fish.put("date", parsedFish.date);
        fish.put("latitude", Double.parseDouble(parsedFish.latitude));
        fish.put("longitude", Double.parseDouble(parsedFish.longitude));
        fish.put("image", parsedFish.image);
        fish.put("humidity", Double.parseDouble(parsedFish.humidity));
        fish.put("moonPhase", parsedFish.moonPhase);
        ParseFile file = new ParseFile(parsedFish.photo);
        fish.put("photofile", file);
        fish.put("pressure", Double.parseDouble(parsedFish.pressure));
        fish.put("pressurePattern", parsedFish.pressureTrend);
        fish.put("swellDirection", Double.parseDouble(parsedFish.swellDirection));
        fish.put("swellHeight", Double.parseDouble(parsedFish.swellHeight));
        fish.put("waterTemp", Double.parseDouble(parsedFish.waterTemp));
        fish.put("waveHeight", Double.parseDouble(parsedFish.waveHeight));
        fish.put("bait", parsedFish.bait);
        fish.put("userName", username);

        fish.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    CaughtFishDetailsActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pd.dismiss();
                            Toast.makeText(CaughtFishDetailsActivity.this, "You have shared your fish!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else {
                    throw new RuntimeException(e);
                 }
            }
        });
    }


}
