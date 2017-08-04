package org.fractalesque.todo;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.*;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.content.Context;
import android.widget.TextView;


public class Listing extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listing);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Mapper mapper = new Mapper(this);
        Task[] tasks = mapper.getTasks();
        ListView listview = (ListView) findViewById(R.id.listview);
        final ArrayAdapter<Task> adapter = new MyAdapter(this, android.R.layout.simple_list_item_1, tasks);
        listview.setAdapter(adapter);
        final Intent editIntent = new Intent(this, Editing.class);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Task thisTask = adapter.getItem(position);
                editIntent.putExtra("item", thisTask);
                startActivity(editIntent);
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        final Intent intent = new Intent(this, Editing.class);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_listing, menu);
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

    private class MyAdapter extends ArrayAdapter<Task> {

        private final Context context;
        private final Task[] tasks;

        MyAdapter(Context context, int textViewRessourceId, Task[] tasks) {
            super(context, textViewRessourceId, tasks);
            this.context = context;
            this.tasks = tasks;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(android.R.layout.simple_list_item_2, parent, false);
                TextView title = convertView.findViewById(android.R.id.text1);
                TextView description = convertView.findViewById(android.R.id.text2);
                title.setText(this.tasks[position].getTitle());
                description.setText(this.tasks[position].getDescription());
            }
            return convertView;
        }
    }

}
