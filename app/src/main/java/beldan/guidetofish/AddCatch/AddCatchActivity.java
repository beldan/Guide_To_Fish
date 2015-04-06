package beldan.guidetofish.AddCatch;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import beldan.forecastiowrapper.ForecastIO;
import beldan.forecastiowrapper.data.DailyDataPoint;
import beldan.guidetofish.CaughtFishDatabaseHandler;
import beldan.guidetofish.Object_ParsedFish;
import beldan.guidetofish.R;
import beldan.guidetofish.Weather.HourlyResponse;
import beldan.guidetofish.Weather.Query;
import beldan.guidetofish.Weather.WorldWeatherMarineResponse;
import beldan.guidetofish.Weather.WorldWeatherOnlineApiProvider;
import beldan.restrung.cache.RequestCache;
import beldan.restrung.client.ContextAwareAPIDelegate;

public class AddCatchActivity extends Activity implements View.OnClickListener {

    Button takePhotoButton;
    Button saveCatchButton;

    byte[] photo;

    EditText speciesEditText;
    EditText weightText;
    EditText lengthText;
    EditText baitText;
    EditText waterTypeText;

    String fishTypeSelected;
    String speciesText;
    String weightValue;
    String lengthValue;
    String baitValue;
    String ApiKey = "95r7tzmxcnbyxt7g6427m8wz";
    String ForecastAPI = "55f8efccd3b4d4498caa1a139a526aef";

    //variables for the parse call
    String pressurePattern;
    double waveHeight;
    double pressure;
    double tempC;
    double windSpeed;
    String windDirection16pt;
    double windDirectionDegree;
    double humidity;
    double swellHeight;
    double swellDirection;
    String swellDirection16pt;
    double waterTemp;
    String moonPhase;
    double moon;

    double latitude;
    double longitude;

    ProgressDialog dialog;

    private File imageFile;

    private static final int CAMERA_REQUEST = 1888;
    private Handler handler;

    String measurement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_catch);

        takePhotoButton = (Button)findViewById(R.id.take_photo_button);
        takePhotoButton.setOnClickListener(this);
        saveCatchButton = (Button)findViewById(R.id.save_catch_button);
        saveCatchButton.setOnClickListener(this);

        waterTypeText = (EditText) findViewById(R.id.waterTypeText);
        speciesEditText = (EditText)findViewById(R.id.species_text);
        weightText = (EditText)findViewById(R.id.weight_text);
        lengthText = (EditText)findViewById(R.id.length_text);
        baitText = (EditText) findViewById(R.id.bait_text);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        measurement = prefs.getString(getString(R.string.measurement_pref), "");
        if (measurement.equalsIgnoreCase("IMPERIAL")) {
            weightText.setHint("in Pounds");
            lengthText.setHint("in Inches");
        }

        baitText.setOnClickListener(this);
        waterTypeText.setOnClickListener(this);
        speciesEditText.setOnClickListener(this);

        dialog = new ProgressDialog(AddCatchActivity.this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_catch, menu);
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
        if (v == takePhotoButton) {
            final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
            AlertDialog.Builder builder = new AlertDialog.Builder(AddCatchActivity.this);
            builder.setTitle("Add Photo");
            builder.setItems(options, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (options[which].equals("Take Photo")) {

                        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(cameraIntent, CAMERA_REQUEST);

                    } else if (options[which].equals("Choose from Gallery")) {
                        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(intent, 2);
                    } else if (options[which].equals("Cancel")) {
                        dialog.dismiss();
                    }
                }
            });
            builder.show();
        } else if (v == saveCatchButton) {
            speciesText = speciesEditText.getText().toString();
            lengthValue = lengthText.getText().toString();
            weightValue = weightText.getText().toString();
            baitValue = baitText.getText().toString();
            if (speciesText.length() <= 0 || lengthText.length() <= 0 || weightText.length() <= 0 || baitValue.length() <= 0 || fishTypeSelected.length() <= 0) {
                Toast.makeText(this, "Please fill out all details", Toast.LENGTH_LONG).show();
            } else {

                dialog.setMessage("Please Wait...");
                dialog.setTitle("Saving Catch");
                dialog.setIndeterminate(true);
                dialog.show();

                getWeatherDetails();
            }
        } else if (v == baitText) {
            Log.d("TAG", "Bait touched");
            Intent i = new Intent(this, BaitListViewActivity.class);
            startActivityForResult(i, 3);
        } else if (v == waterTypeText) {
            AlertDialog.Builder builder = new AlertDialog.Builder(AddCatchActivity.this);
            builder.setTitle("Type of Water");
            builder.setPositiveButton("Salt", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    fishTypeSelected = "Salt";
                    waterTypeText.setText(fishTypeSelected);
                    dialog.dismiss();
                }
            });

            builder.setNegativeButton("Fresh", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    fishTypeSelected = "Fresh";
                    waterTypeText.setText(fishTypeSelected);
                    dialog.dismiss();
                }
            });
            builder.show();
        } else if (v == speciesEditText) {
            Intent i = new Intent(this, SpeciesListViewActivity.class);
            startActivityForResult(i, 4);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == CAMERA_REQUEST) {
                if (data == null) {
                    Log.d("TAG", "data is null");
                } else {
                    Bitmap bmp = (Bitmap) data.getExtras().get("data");
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bmp.compress(Bitmap.CompressFormat.PNG, 60, stream);
                    photo = stream.toByteArray();
                }
            } else if (requestCode == 2) {
                Uri selectedImage = data.getData();
                String[] filePath = { MediaStore.Images.Media.DATA };
                Cursor c = getContentResolver().query(selectedImage,filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                String picturePath = c.getString(columnIndex);
                c.close();

                Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                thumbnail.compress(Bitmap.CompressFormat.PNG, 100, stream);
                photo = stream.toByteArray();
            } else if (requestCode == 3) {
                String newText = data.getStringExtra("PUBLIC_STATIC_STRING_IDENTIFIER");
                baitText.setText(newText);
            } else if (requestCode == 4) {
                String newText = data.getStringExtra("Species_Text");
                speciesEditText.setText(newText);
            }
        } else {
            Log.d("TAG", "There was an error");
            Log.d("TAG", "Error: " + resultCode);
        }
    }

    private void getWeatherDetails() {

        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String provider = locationManager.getBestProvider(criteria, true);
        Location location = locationManager.getLastKnownLocation(provider);

        if (location != null) {
            Log.d("TAG", "location is not null");
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            String ApiKey = "95r7tzmxcnbyxt7g6427m8wz";

            WorldWeatherOnlineApiProvider.getClient().marineQuery(new ContextAwareAPIDelegate<WorldWeatherMarineResponse>(AddCatchActivity.this, WorldWeatherMarineResponse.class, RequestCache.LoadPolicy.NEVER, RequestCache.StoragePolicy.DISABLED) {
                @Override
                public void onResults(WorldWeatherMarineResponse worldWeatherMarineResponse) {
                    List<HourlyResponse> temp = worldWeatherMarineResponse.getData().getMarineConditionList().get(0).getHourly();
                    if (measurement.equalsIgnoreCase("IMPERIAL")) {
                        tempC = temp.get(0).getTempF();
                        windSpeed = temp.get(0).getWindSpeedMiles();
                        swellHeight = temp.get(0).getSwellHeightFT();
                        waterTemp = temp.get(0).getWaterTempF();

                        // do math to convert meters to feet
                        double i = temp.get(0).getWaveHeight();
                        waveHeight = i / 0.3048;

                    } else {
                        tempC = temp.get(0).getTempC();
                        windSpeed = temp.get(0).getWindSpeedKmph();
                        swellHeight = temp.get(0).getSwellHeight();
                        waterTemp = temp.get(0).getWaterTemp();
                        waveHeight = temp.get(0).getWaveHeight();
                    }

                    windDirection16pt = temp.get(0).getWindDir16Point();
                    windDirectionDegree = temp.get(0).getWindDirDegree();
                    humidity = temp.get(0).getHumidity();
                    pressure = temp.get(0).getPressure();
                    Log.d("TAG", "Pressure " + pressure);
                    int last = temp.size() - 1;
                    double lastPressure = temp.get(last).getPressure();
                    Log.d("TAG", "last Pressure " + lastPressure);
                    swellDirection = temp.get(0).getSwellDirectionDegrees();
                    swellDirection16pt = temp.get(0).getSwellDirection16Point();

                    //get the pressure trend
                    if (pressure < lastPressure) { pressurePattern = "Rising"; }
                    else if (pressure > lastPressure) { pressurePattern = "Dropping"; }
                    else if (pressure == lastPressure) { pressurePattern = "Steady"; }
                    
                    getMoonDetails();
                }

                @Override
                public void onError(Throwable e) {
                    Log.d("TAG", "Marine Error" + e);
                }
            }, ApiKey, Query.latLng(latitude, longitude));
        }



    }

    private void getMoonDetails() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ForecastIO forecastIO = new ForecastIO(ForecastAPI);
                    beldan.forecastiowrapper.Forecast forecast = forecastIO.getForecastFor(latitude, longitude);

                    if (forecast != null) {
                        DailyDataPoint daily = forecast.getToday();
                        if (daily != null) {
                            moon = daily.getMoonPhase();
                        }

                        processMoonPhase();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();


    }

    private void processMoonPhase() {
        if (moon > 0 && moon <= 0.03) {
            moonPhase = "moon1";
        }
        else if (moon > 0.03 && moon <= 0.06) {
            moonPhase = "moon2";
        }
        else if (moon > 0.06 && moon <= 0.09) {
            moonPhase = "moon3";
        }
        else if (moon > 0.09 && moon <= 0.12) {
            moonPhase = "moon4";
        }
        else if (moon > 0.12 && moon <= 0.15) {
            moonPhase = "moon5";
        }
        else if (moon > 0.15 && moon <= 0.18) {
            moonPhase = "moon6";
        }
        else if (moon > 0.18 && moon <= 0.21) {
            moonPhase = "moon7";
        }
        else if (moon > 0.21 && moon <= 0.24) {
            moonPhase = "moon8";
        }
        else if (moon > 0.24 && moon <= 0.27) {
            moonPhase = "moon9";
        }
        else if (moon > 0.27 && moon <= 0.30) {
            moonPhase = "moon10";
        }
        else if (moon > 0.30 && moon <= 0.33) {
            moonPhase = "moon11";
        }
        else if (moon > 0.33 && moon <= 0.36) {
            moonPhase = "moon12";
        }
        else if (moon > 0.36 && moon <= 0.39) {
            moonPhase = "moon13";
        }
        else if (moon > 0.39 && moon <= 0.42) {
            moonPhase = "moon14";
        }
        else if (moon > 0.42 && moon <= 0.45) {
            moonPhase = "moon15";
        }
        else if (moon > 0.45 && moon <= 0.48) {
            moonPhase = "moon16";
        }
        else if (moon > 0.48 && moon <= 0.51) {
            moonPhase = "moon17";
        }
        else if (moon > 0.51 && moon <= 0.54) {
            moonPhase = "moon18";
        }
        else if (moon > 0.54 && moon <= 0.57) {
            moonPhase = "moon19";
        }
        else if (moon > 0.57 && moon <= 0.60) {
            moonPhase = "moon20";
        }
        else if (moon > 0.60 && moon <= 0.63) {
            moonPhase = "moon21";
        }
        else if (moon > 0.63 && moon <= 0.66) {
            moonPhase = "moon22";
        }
        else if (moon > 0.66 && moon <= 0.69) {
            moonPhase = "moon23";
        }
        else if (moon > 0.69 && moon <= 0.72) {
            moonPhase = "moon24";
        }
        else if (moon > 0.72 && moon <= 0.75) {
            moonPhase = "moon25";
        }
        else if (moon > 0.75 && moon <= 0.78) {
            moonPhase = "moon26";
        }
        else if (moon > 0.78 && moon <= 0.81) {
            moonPhase = "moon27";
        }
        else if (moon > 0.81 && moon <= 0.84) {
            moonPhase = "moon28";
        }
        else if (moon > 0.84 && moon <= 0.87) {
            moonPhase = "moon29";
        }
        else if (moon > 0.87 && moon <= 0.90) {
            moonPhase = "moon30";
        }
        else if (moon > 0.90 && moon <= 0.95) {
            moonPhase = "moon31";
        }
        else if (moon > 0.95) {
            moonPhase = "moon32";
        }

        dialog.dismiss();
        checkWaterTemp();


    }

    private void checkWaterTemp() {
        AddCatchActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (waterTemp == 0.0) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(AddCatchActivity.this);
                    builder.setTitle("Water Temperature is not available.");
                    builder.setMessage("Would you like to enter it manually?");

                    final EditText input = new EditText(AddCatchActivity.this);
                    input.setInputType(InputType.TYPE_CLASS_NUMBER);
                    builder.setView(input);

                    builder.setPositiveButton("SAVE", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String addBaitText = input.getText().toString();
                            waterTemp = Double.parseDouble(addBaitText);
                            InputMethodManager imm = (InputMethodManager)getSystemService(
                                    Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(input.getWindowToken(), 0);
                            parseResults();
                            dialog.dismiss();
                        }
                    });

                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            parseResults();
                            dialog.dismiss();
                        }
                    });
                    builder.show();
                } else {

                    parseResults();
                }
            }
        });

    }

    private void parseResults() {

        Log.d("TAG", "fishTypeSelected " + fishTypeSelected);

        String date = null;

        if (measurement.equalsIgnoreCase("IMPERIAL")) {
            date = new SimpleDateFormat("MM-dd-yyyy HH:mm a", Locale.ENGLISH).format(new Date());
        } else {
            date = new SimpleDateFormat("dd-MM-yyyy HH:mm a", Locale.ENGLISH).format(new Date());
        }

        String image = null;
        if (fishTypeSelected.equalsIgnoreCase("Salt")) {
            image = "SaltFish";
        } else if (fishTypeSelected.equalsIgnoreCase("Fresh")) {
            image = "FreshFish"; 
        }

        CaughtFishDatabaseHandler db = new CaughtFishDatabaseHandler(this);
        db.addRide(new Object_ParsedFish(fishTypeSelected, speciesText, weightValue, lengthValue, date, String.valueOf(latitude), String.valueOf(longitude), image, String.valueOf(waterTemp), String.valueOf(pressure), pressurePattern, String.valueOf(waveHeight), String.valueOf(swellHeight), moonPhase, measurement, String.valueOf(swellDirection), null, null, String.valueOf(humidity), photo, baitValue ));

        dialog.dismiss();

        Toast.makeText(AddCatchActivity.this, "Your catch has been saved", Toast.LENGTH_LONG).show();

        this.finish();
    }









}
