package beldan.guidetofish.AllCatches;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Typeface;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

import beldan.guidetofish.DatabaseHandler;
import beldan.guidetofish.Object_Map_Marker;
import beldan.guidetofish.Object_ParsedFish;
import beldan.guidetofish.R;
import io.nlopez.clusterer.Cluster;
import io.nlopez.clusterer.Clusterable;
import io.nlopez.clusterer.Clusterer;


public class AllCatchesActivity extends FragmentActivity implements GoogleMap.OnMapClickListener, GoogleMap.OnInfoWindowClickListener {

    private GoogleMap mMap;
    LatLng myPosition;
    Button settingsButton;
    Point p;
    List<ParseObject> parsedObjects = new ArrayList<ParseObject>();
    List<ParseObject> parsedFads = new ArrayList<ParseObject>();
    DatabaseHandler db;
    List<Object_ParsedFish> savedFish = new ArrayList<Object_ParsedFish>();
    List<Object_Map_Marker> markersList = new ArrayList<Object_Map_Marker>();
    private Clusterer<Object_Map_Marker> clusterer;
    String measurement;

    ProgressDialog progressBar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_catches);

        db = new DatabaseHandler(this);
        db.deleteAll();
        
        settingsButton = (Button)findViewById(R.id.mapSettingsButton);
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (p != null) {
                    showPopUp(AllCatchesActivity.this, p);
                }
            }
        });

        progressBar = new ProgressDialog(this);
        progressBar.setMessage("Getting fish from server ....");
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.show();


        Parse.initialize(this, "T8y1HoeUayRfOIBI7Kp3h3lmflj1GcjiEqbHUuqU", "8KcnaflyuA9KFcct3clusEJa7ItU4Bz6aNgsFYCx");
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Fish");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                if (e == null) {
                    parsedObjects = parseObjects;
                    ParseQuery<ParseObject> fadsQuery = ParseQuery.getQuery("Fads");
                    fadsQuery.findInBackground(new FindCallback<ParseObject>() {
                        @Override
                        public void done(List<ParseObject> parseFadObjects, ParseException e) {
                            parsedFads = parseFadObjects;
                            loadAnnotations();
                        }
                    });
                } else {
                    Log.d("PARSE ERROR", e.getMessage());
                }
            }
        });

        setupMapIfNeeded();
    }

    private void loadAnnotations() {

        for (ParseObject object : parsedObjects) {
            Object_ParsedFish fish = new Object_ParsedFish();
            String water = object.getString("species");
            String type = object.getString("name");
            Number weight = object.getNumber("weight");
            String weightString = String.valueOf(weight);
            Number length = object.getNumber("length");
            String lengthString = String.valueOf(length);
            Number latitude = object.getNumber("latitude");
            String latitudeString = String.valueOf(latitude);
            Number longitude = object.getNumber("longitude");
            String longitudeString = String.valueOf(longitude);
            String date = object.getString("date");
            String image = object.getString("image");
            String objectID = object.getString("objectID");
            String userName = object.getString("userName");
            Number humidity = object.getNumber("humidity");
            String humidityString = String.valueOf(humidity);
            measurement = object.getString("measurement");
            String moonPhase = object.getString("moonPhase");
            Number pressure = object.getNumber("pressure");
            String pressureString = String.valueOf(pressure);
            String pressureTrend = object.getString("pressurePattern");
            Number swellDirection = object.getNumber("swellDirection");
            String swellDirectionString = String.valueOf(swellDirection);
            Number swellHeight = object.getNumber("swellHeight");
            String swellHeightString = String.valueOf(swellHeight);
            Number waterTemp = object.getNumber("waterTemp");
            String waterTempString = String.valueOf(waterTemp);
            Number waveHeight = object.getNumber("waveHeight");
            String waveHeightString = String.valueOf(waveHeight);
            String baitString = object.getString("bait");
            ParseFile imageData = object.getParseFile("photofile");
            Log.d("TAG", "imagedata" + imageData);
            byte[] photo;
            try {
                photo = imageData.getData();
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }

            fish.water = water;
            fish.type = type;
            fish.weight = weightString;
            fish.length = lengthString;
            fish.latitude = latitudeString;
            fish.longitude = longitudeString;
            fish.date = date;
            fish.image = image;
            fish.objectID = objectID;
            fish.userName = userName;
            fish.humidity = humidityString;
            fish.measurement = measurement;
            fish.moonPhase = moonPhase;
            fish.pressure = pressureString;
            fish.pressureTrend = pressureTrend;
            fish.swellDirection = swellDirectionString;
            fish.swellHeight = swellHeightString;
            fish.waterTemp = waterTempString;
            fish.waveHeight = waveHeightString;
            fish.photo = photo;
            fish.bait = baitString;

            db.addRide(new Object_ParsedFish(water, type, weightString, lengthString, date, latitudeString, longitudeString, image, waterTempString, pressureString, pressureTrend, waveHeightString, swellHeightString, moonPhase, measurement, swellDirectionString, objectID, userName, humidityString, photo, baitString));

            savedFish.add(fish);

        }
        addMarkersToMap();
    }

    private void addMarkersToMap() {

        for (Object_ParsedFish fish : savedFish) {

            double latitude = Double.parseDouble(fish.latitude);
            double longitude = Double.parseDouble(fish.longitude);
            String title = null;
            if (measurement.equalsIgnoreCase("IMPERIAL")) {
                title = fish.type + " " + fish.weight + "lb" + " " + fish.length + "in";
            } else {
                title = fish.type + " " + fish.weight + "kg" + " " + fish.length + "cm";
            }
            String description = fish.date;
            String type = fish.water;
            LatLng newLatLng = new LatLng(latitude, longitude);

            Object_Map_Marker marker = new Object_Map_Marker();
            marker.setLocationLatLng(newLatLng);
            marker.setName(title);
            marker.setDescription(description);
            marker.setType(type);

            markersList.add(marker);

        }
        progressBar.dismiss();
        initClusterer();
    }

    private void initClusterer() {
        clusterer = new Clusterer<Object_Map_Marker>(this, mMap);
        clusterer.addAll(markersList);
        clusterer.setOnPaintingMarkerListener(new Clusterer.OnPaintingClusterableMarkerListener() {
            @Override
            public MarkerOptions onCreateMarkerOptions(Clusterable clusterable) {

                Object_Map_Marker marker2 = (Object_Map_Marker)clusterable;
                if (marker2.getType().equalsIgnoreCase("Salt")) {
                    Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.saltfish);
                    Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, 80, 80, false);
                    return new MarkerOptions().position(clusterable.getPosition()).title(marker2.getName()).snippet(marker2.getDescription()).icon(BitmapDescriptorFactory.fromBitmap(resizedBitmap));
                } else if (marker2.getType().equalsIgnoreCase("Fresh")) {
                    Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.freshfish);
                    Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, 80, 80, false);
                    return new MarkerOptions().position(clusterable.getPosition()).title(marker2.getName()).snippet(marker2.getDescription()).icon(BitmapDescriptorFactory.fromBitmap(resizedBitmap));
                }
                return null;
            }

            @Override
            public void onMarkerCreated(Marker marker, Clusterable clusterable) {

            }
        });

        clusterer.setOnPaintingClusterListener(new Clusterer.OnPaintingClusterListener() {

            @Override
            public void onMarkerCreated(Marker marker, Cluster cluster) {

            }

            @Override
            public MarkerOptions onCreateClusterMarkerOptions(Cluster cluster) {
                return new MarkerOptions()
                        .title("Zoom in to reveal " + cluster.getWeight() + " fish")
                        .position(cluster.getCenter())
                        .icon(BitmapDescriptorFactory.fromBitmap(getClusteredLabel(cluster.getWeight(),
                                AllCatchesActivity.this)));
            }
        });

    }

    private Bitmap getClusteredLabel(Integer count, Context ctx) {

        float density = getResources().getDisplayMetrics().density;

        Resources r = ctx.getResources();
        Bitmap res = BitmapFactory.decodeResource(r, R.drawable.circle_red);
        res = res.copy(Bitmap.Config.ARGB_8888, true);
        Canvas c = new Canvas(res);

        Paint textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setTypeface(Typeface.DEFAULT_BOLD);
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(16 * density);

        c.drawText(String.valueOf(count.toString()), res.getWidth() / 2, res.getHeight() / 3 + textPaint.getTextSize() / 3, textPaint);
        c.drawText("Fish", res.getWidth() / 2, res.getHeight() / 2 + textPaint.getTextSize() / 3, textPaint);


        return res;
    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        int[] location = new int[2];
        Button button = (Button)findViewById(R.id.mapSettingsButton);

        button.getLocationOnScreen(location);

        p = new Point();
        p.x = location[0];
        p.y = location[1];
    }

    private void showPopUp(final Activity context, Point p) {
        int popupHeight = 800;

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int temp = width / 4;
        int popupWidth = width - temp;

        ScrollView viewGroup = (ScrollView)findViewById(R.id.popupLayout);
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = layoutInflater.inflate(R.layout.activity_popup_window, viewGroup);

        final PopupWindow popup = new PopupWindow(context);
        popup.setContentView(layout);
        popup.setWidth(popupWidth);
        popup.setHeight(popupHeight);
        popup.setFocusable(true);

        popup.showAtLocation(layout, Gravity.CENTER, 0, 0);

        Button popupButton1 = (Button)layout.findViewById(R.id.popupButton1);
        popupButton1.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                      mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                      popup.dismiss();
               }
            }
        );
        Button popupButton2 = (Button)layout.findViewById(R.id.popupButton2);
        popupButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                popup.dismiss();
            }
        });
        Button popupButton3 = (Button)layout.findViewById(R.id.popupButton3);
        popupButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                popup.dismiss();
            }
        });

    }

    private void setupMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    private void setUpMap() {
        mMap.setMyLocationEnabled(true);
        mMap.setOnMapClickListener(this);
        mMap.setOnInfoWindowClickListener(this);
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String provider = locationManager.getBestProvider(criteria, true);
        Location location = locationManager.getLastKnownLocation(provider);

        if(location!=null) {
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            myPosition = new LatLng(latitude, longitude);
            CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(myPosition, 10);
            mMap.animateCamera(yourLocation);

        } else {
            Toast.makeText(getApplicationContext(), "Your location could not be found", Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_all_catches, menu);
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
    public void onMapClick(LatLng latLng) {
        setUpMap();
    }


    @Override
    public void onInfoWindowClick(Marker marker) {
        marker.showInfoWindow();
        //get the infoWindow title
        String title = marker.getTitle();
        String checkTitle = null;
        for (Object_ParsedFish fish : savedFish) {
            if (measurement.equalsIgnoreCase("IMPERIAL")) {
                checkTitle = fish.type + " " + fish.weight + "lb" + " " + fish.length + "in";
            } else {
                checkTitle = fish.type + " " + fish.weight + "kg" + " " + fish.length + "cm";
            }
            if (title.equalsIgnoreCase(checkTitle)) {
                Intent i = new Intent(this, AllCatchesDetailActivity.class);
                i.putExtra("FISH", fish);
                startActivity(i);
            }
            else {
                Log.d("Marker", "Marker is not same");
            }

        }

    }

}
