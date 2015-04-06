package beldan.guidetofish.CaughtFish;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import beldan.guidetofish.CaughtFishDatabaseHandler;
import beldan.guidetofish.Object_Map_Marker;
import beldan.guidetofish.Object_ParsedFish;
import beldan.guidetofish.R;
import io.nlopez.clusterer.Cluster;
import io.nlopez.clusterer.Clusterable;
import io.nlopez.clusterer.Clusterer;


public class CaughtFishMapActivity extends FragmentActivity implements GoogleMap.OnMapClickListener, GoogleMap.OnInfoWindowClickListener {

    private GoogleMap mMap;
    CaughtFishDatabaseHandler db;
    List<Object_ParsedFish> fishFromDB;
    LatLng myPosition;
    private Clusterer<Object_Map_Marker> clusterer;
    List<Object_Map_Marker> markersList = new ArrayList<Object_Map_Marker>();
    ProgressDialog progressBar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caught_fish_map);

        progressBar = new ProgressDialog(this);
        progressBar.setMessage("Getting fish ....");
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.show();

        db = new CaughtFishDatabaseHandler(this);
        fishFromDB = db.getAllFish();

        setupMapIfNeeded();

    }

    private void setupMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.caught_fish_map))
                    .getMap();
            if (mMap != null) {
                addMarkersToMap();
                setUpMap();
            }
        } else {
            addMarkersToMap();
            setUpMap();
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
        getMenuInflater().inflate(R.menu.menu_caught_fish_map, menu);
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
        String title = marker.getTitle();
        for (Object_ParsedFish fish : fishFromDB) {
            String checkTitle = fish.type + " " + fish.weight + "kg" + " " + fish.length + "cm";
            if (title.equalsIgnoreCase(checkTitle)) {

                Intent i = new Intent(this, CaughtFishDetailsActivity.class);
                i.putExtra("selected_fish", fish);
                startActivity(i);
            }
            else {
                Log.d("Marker", "Marker is not same");
            }

        }
    }

    private void addMarkersToMap() {

        for (Object_ParsedFish fish : fishFromDB) {

            double latitude = Double.parseDouble(fish.latitude);
            double longitude = Double.parseDouble(fish.longitude);
            String title = fish.type + " " + fish.weight + "kg" + " " + fish.length + "cm";
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
                                CaughtFishMapActivity.this)));
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
        textPaint.setTextSize(18 * density);

        c.drawText(String.valueOf(count.toString()), res.getWidth() / 2, res.getHeight() / 3 + textPaint.getTextSize() / 3, textPaint);
        c.drawText("Fish", res.getWidth() / 2, res.getHeight() / 2 + textPaint.getTextSize() / 3, textPaint);


        return res;
    }

}
