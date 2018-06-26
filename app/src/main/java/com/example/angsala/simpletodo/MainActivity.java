package com.example.angsala.simpletodo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<String> items;
    ArrayAdapter<String> itemsAdapter;
    ListView lvItems;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //obtain reference to List View
        lvItems = (ListView) findViewById(R.id.lvItems);
        //Initialize items list
      readItems();
        //Initialize adapter using items list
        itemsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        //wire the adapter to the view
        lvItems.setAdapter(itemsAdapter);

        //mock items
        //items.add("Brush Teeth");
        //items.add("8:00 Class");

        //set up listener on creation
        setupListViewListener();
        }

    private void setupListViewListener(){
        //set ListView's itemLongClickListener
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long l) {
                items.remove(position);
                itemsAdapter.notifyDataSetChanged();
                Log.i("Main Activity", "Removed item " + position);
                writeItems();
                return true;
            }
        });
    }

        public void onAddItem(View viewer){
            EditText etNewItem = (EditText) findViewById(R.id.etNewItem);
            String itemText = etNewItem.getText().toString();
            itemsAdapter.add(itemText);
            etNewItem.setText("");
            writeItems();
            //displays notification to the user
            Toast.makeText(getApplicationContext(),"Item added to the list!", Toast.LENGTH_SHORT).show();
        }
//returns the file in which the data is stored
    private File getDataFile(){
        return new File(getFilesDir(), "todo.txt");
    }
    //read the items from the file system
    private void readItems(){
        //try-catch block to take care off unhandled exceptions
        try {
            // create the array using the content in the file
            items = new ArrayList<String>(FileUtils.readLines(getDataFile(), Charset.defaultCharset()));
        }
        catch (IOException e){
            //print the error to the console
            e.printStackTrace();
            items = new ArrayList<>();
        }
    }

    //write the items to the file system
    private void writeItems(){
        try{
            //save the item list as a line-delimited text file
            FileUtils.writeLines(getDataFile(), items);
        }
        catch (IOException e){
            Log.e("MainActivity", "Error writing file", e);

        }
    }

}
