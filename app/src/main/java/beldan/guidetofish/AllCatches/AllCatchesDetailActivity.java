package beldan.guidetofish.AllCatches;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;

import beldan.guidetofish.Object_ParsedFish;
import beldan.guidetofish.R;


public class AllCatchesDetailActivity extends ActionBarActivity {

    Object_ParsedFish parsedFish;

    SubsamplingScaleImageView imageView;
    TextView usernameTextView;
    TextView titleTextView;
    TextView dateTextView;
    TextView lengthTextView;
    TextView weightTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_catches_detail);

        Intent i = getIntent();
        parsedFish = (Object_ParsedFish)i.getSerializableExtra("FISH");

        setUpView();
    }

    private void setUpView() {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        imageView = (SubsamplingScaleImageView)findViewById(R.id.details_imageview);
        //imageView.setMaxHeight(width);
        Bitmap bitmap;
        bitmap = BitmapFactory.decodeByteArray(parsedFish.photo, 0, parsedFish.photo.length);
        //imageView.setImageBitmap(bitmap);
        imageView.setImage(ImageSource.bitmap(bitmap));

        usernameTextView = (TextView)findViewById(R.id.details_username);
        usernameTextView.setText(parsedFish.userName);

        titleTextView = (TextView)findViewById(R.id.details_title);
        titleTextView.setText(parsedFish.type);

        dateTextView = (TextView)findViewById(R.id.details_date);
        dateTextView.setText(parsedFish.date);

        lengthTextView = (TextView) findViewById(R.id.details_length);
        weightTextView = (TextView)findViewById(R.id.details_width);

        if (parsedFish.measurement.equalsIgnoreCase("METRIC")) {
            lengthTextView.setText("Length: " + parsedFish.length + " cm");
            weightTextView.setText("Weight: " + parsedFish.weight + " kg");
        } else {
            lengthTextView.setText("Length: " + parsedFish.length + " in");
            weightTextView.setText("Weight: " + parsedFish.weight + " lb");
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_all_catches_detail, menu);
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
}
