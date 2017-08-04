package org.fractalesque.todo;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

public class Editing extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editing);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        final Intent navigate = new Intent(this, Listing.class);
        final EditText titleInput = (EditText) findViewById(R.id.editText);
        final EditText descriptionInput = (EditText) findViewById(R.id.editText2);
        final Mapper mapper = new Mapper(this);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = titleInput.getText().toString();
                String description = descriptionInput.getText().toString();
                mapper.insert(title, description);
                startActivity(navigate);
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
