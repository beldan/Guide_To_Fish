package beldan.guidetofish.AddCatch;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.InputType;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import beldan.guidetofish.DatabaseHandler_Bait;
import beldan.guidetofish.Object_Bait;
import beldan.guidetofish.R;


public class BaitListViewActivity extends ActionBarActivity implements AdapterView.OnItemClickListener, View.OnClickListener {

    ListView listView;
    List<Object_Bait> list;
    DatabaseHandler_Bait db;
    Button addBaitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bait_list_view);

        listView = (ListView) findViewById(R.id.bait_listView);
        listView.setOnItemClickListener(this);
        registerForContextMenu(listView);

        db = new DatabaseHandler_Bait(this);
        list = db.getAllBait();

        ArrayList<String> stringList = new ArrayList<>();

        for (Object_Bait bait : list) {
            String baitString = bait.name;
            stringList.add(baitString);
        }

        //sort Arraylist alphabetically
        Collections.sort(stringList, String.CASE_INSENSITIVE_ORDER);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.tablecell_layout_centre, stringList);
        listView.setAdapter(adapter);

        addBaitButton = (Button) findViewById(R.id.bait_popup_addButton);
        addBaitButton.setOnClickListener(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_bait_list_view, menu);
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
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Collections.sort(list, new Comparator<Object_Bait>() {
            @Override
            public int compare(Object_Bait lhs, Object_Bait rhs) {
                return lhs.name.compareToIgnoreCase(rhs.name);
            }
        });
        Object_Bait selectedBait = list.get(position);
        String baitSelected = selectedBait.name;
        Intent resultIntent = new Intent();
        resultIntent.putExtra("PUBLIC_STATIC_STRING_IDENTIFIER", baitSelected);
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    @Override
    public void onClick(View v) {
        if (v == addBaitButton) {
            Log.d("TAG", "Add bait button pressed");
            AlertDialog.Builder builder = new AlertDialog.Builder(BaitListViewActivity.this);
            builder.setTitle("Type of bait");

            final EditText input = new EditText(BaitListViewActivity.this);
            input.setInputType(InputType.TYPE_CLASS_TEXT);
            builder.setView(input);

            builder.setPositiveButton("SAVE", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String addBaitText = input.getText().toString();
                    Object_Bait newBait = new Object_Bait();
                    newBait.name = addBaitText;
                    db.addRide(newBait);
                    list.add(newBait);
                    upDateListView();
                    dialog.dismiss();
                }
            });

            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            builder.show();
        }
    }

    private void upDateListView() {
        ArrayList<String> stringList = new ArrayList<>();

        for (Object_Bait bait : list) {
            String baitString = bait.name;
            stringList.add(baitString);
        }
        //sort Arraylist alphabetically
        Collections.sort(stringList, String.CASE_INSENSITIVE_ORDER);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.tablecell_layout_centre, stringList);
        listView.setAdapter(adapter);
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu,
                                    View v, ContextMenu.ContextMenuInfo menuInfo) {

        if (v.getId()==R.id.bait_listView) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
            menu.setHeaderTitle(list.get(info.position).name);
            menu.add(0, 1, 1, "Delete Bait");
            menu.add(0, 2, 2, "CANCEL");
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        super.onContextItemSelected(item);

        Log.d("TAG", "item " + item.toString());
        String menuName = item.toString();
        if (menuName.equalsIgnoreCase("CANCEL")) {
            return true;
        } else {

            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

            String name = list.get(info.position).name;

            List<Object_Bait> toDelete = new ArrayList<Object_Bait>();

            for (Object_Bait fish : list) {
                if (fish.name.equalsIgnoreCase(name)) {
                    DatabaseHandler_Bait newdb = new DatabaseHandler_Bait(this);
                    newdb.deleteBait(fish);
                    toDelete.add(fish);
                }
            }

            list.removeAll(toDelete);


            upDateListView();

            return true;
        }
    }



}
