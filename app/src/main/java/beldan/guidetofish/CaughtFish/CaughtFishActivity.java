package beldan.guidetofish.CaughtFish;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import beldan.guidetofish.CaughtFishDatabaseHandler;
import beldan.guidetofish.Object_ParsedFish;
import beldan.guidetofish.R;


public class CaughtFishActivity extends ActionBarActivity implements AdapterView.OnItemClickListener {

    ListView listView;
    List<Object_ParsedFish> list;
    List<ListView_RowItem> mListViewRowItems;
    ListView_Custom_Adapter adapter;
    CaughtFishDatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caught_fish);

        listView = (ListView)findViewById(R.id.caught_fish_listView);
        listView.setOnItemClickListener(this);
        registerForContextMenu(listView);

        getFish();

    }


    private void getFish() {
        Log.d("TAG", "get fish called");
        db = new CaughtFishDatabaseHandler(this);
        list = db.getAllFish();

        mListViewRowItems = new ArrayList<ListView_RowItem>();

        String title = "";
        String subtitle = "";
        byte[] icon = null;

        for (Object_ParsedFish fish : list) {
            title = fish.type;
            subtitle = fish.date;
            icon = fish.photo;

            ListView_RowItem item = new ListView_RowItem(title, subtitle, icon);
            mListViewRowItems.add(item);
        }

        adapter = new ListView_Custom_Adapter(this, mListViewRowItems);
        listView.setAdapter(adapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_caught_fish, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.caught_fish_menu_mapView) {
            Intent i = new Intent(this, CaughtFishMapActivity.class);
            startActivity(i);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Object_ParsedFish selectedFish = list.get(position);
        Intent i = new Intent(this, CaughtFishDetailsActivity.class);
        i.putExtra("selected_fish", selectedFish);
        startActivity(i);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu,
                                    View v, ContextMenu.ContextMenuInfo menuInfo) {

        if (v.getId()==R.id.caught_fish_listView) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
            menu.setHeaderTitle(list.get(info.position).type);
            menu.add(0, 1, 1, "Delete Fish");
            menu.add(0, 2, 2, "CANCEL");
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        super.onContextItemSelected(item);
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        String name = list.get(info.position).date;

        List<Object_ParsedFish> toDelete = new ArrayList<Object_ParsedFish>();

        for (Object_ParsedFish fish : list) {
            if (fish.date.equalsIgnoreCase(name)) {
                CaughtFishDatabaseHandler newdb = new CaughtFishDatabaseHandler(this);
                newdb.deleteFish(fish);
                toDelete.add(fish);
            }
        }

        list.removeAll(toDelete);


        updateListView();

        return true;
    }

    private void updateListView() {
        mListViewRowItems = new ArrayList<ListView_RowItem>();

        String title = "";
        String subtitle = "";
        byte[] icon = null;

        for (Object_ParsedFish fish : list) {
            title = fish.type;
            subtitle = fish.date;
            icon = fish.photo;

            ListView_RowItem item = new ListView_RowItem(title, subtitle, icon);
            mListViewRowItems.add(item);
        }

        adapter = new ListView_Custom_Adapter(this, mListViewRowItems);
        listView.setAdapter(adapter);
    }


}
