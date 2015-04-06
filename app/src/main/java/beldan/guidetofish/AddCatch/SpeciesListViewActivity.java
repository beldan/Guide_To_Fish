package beldan.guidetofish.AddCatch;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
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

import beldan.guidetofish.DatabaseHandler_Species;
import beldan.guidetofish.Object_Species;
import beldan.guidetofish.R;


public class SpeciesListViewActivity extends ActionBarActivity implements AdapterView.OnItemClickListener, View.OnClickListener {

    ListView listView;
    List<Object_Species> list;
    DatabaseHandler_Species db;
    Button addBaitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_species_list_view);

        listView = (ListView) findViewById(R.id.species_listView);
        listView.setOnItemClickListener(this);
        registerForContextMenu(listView);

        db = new DatabaseHandler_Species(this);
        list = db.getAllSpecies();

        ArrayList<String> stringList = new ArrayList<>();

        for (Object_Species bait : list) {
            String baitString = bait.name;
            stringList.add(baitString);
        }

        //sort Arraylist alphabetically
        Collections.sort(stringList, String.CASE_INSENSITIVE_ORDER);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.tablecell_layout_centre, stringList);
        listView.setAdapter(adapter);

        addBaitButton = (Button) findViewById(R.id.species_popup_addButton);
        addBaitButton.setOnClickListener(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_species_list_view, menu);
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
        Collections.sort(list,  new Comparator<Object_Species>() {
            @Override
            public int compare(Object_Species lhs, Object_Species rhs) {
                return lhs.name.compareToIgnoreCase(rhs.name);
            }
        });
        Object_Species selectedBait = list.get(position);
        String baitSelected = selectedBait.name;
        Log.d("TAG", "baitSelected " + baitSelected);
        Intent resultIntent = new Intent();
        resultIntent.putExtra("Species_Text", baitSelected);
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    @Override
    public void onClick(View v) {
        if (v == addBaitButton) {
            AlertDialog.Builder builder = new AlertDialog.Builder(SpeciesListViewActivity.this);
            builder.setTitle("Type of Species");

            final EditText input = new EditText(SpeciesListViewActivity.this);
            input.setInputType(InputType.TYPE_CLASS_TEXT);
            builder.setView(input);

            builder.setPositiveButton("SAVE", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String addBaitText = input.getText().toString();
                    Object_Species newBait = new Object_Species();
                    newBait.name = addBaitText;
                    db.addSpecies(newBait);
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

        for (Object_Species bait : list) {
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

        if (v.getId()==R.id.species_listView) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
            menu.setHeaderTitle(list.get(info.position).name);
            menu.add(0, 1, 1, "Delete Species");
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

            List<Object_Species> toDelete = new ArrayList<Object_Species>();

            for (Object_Species fish : list) {
                if (fish.name.equalsIgnoreCase(name)) {
                    DatabaseHandler_Species newdb = new DatabaseHandler_Species(this);
                    newdb.deleteSpecies(fish);
                    toDelete.add(fish);
                }
            }

            list.removeAll(toDelete);


            upDateListView();

            return true;
        }
    }
}
