package org.fractalesque.todo;

import android.content.Intent;
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

        final EditText titleInput = (EditText) findViewById(R.id.editText);
        final EditText descriptionInput = (EditText) findViewById(R.id.editText2);

        boolean tempEditMode = false;

        final Task currentTask = (Task)getIntent().getSerializableExtra("item");
        if (getIntent().getExtras() != null) {
            titleInput.setText(currentTask.getTitle());
            descriptionInput.setText(currentTask.getDescription());
            tempEditMode = true;
        }

        final boolean editMode = tempEditMode;

        FloatingActionButton saveButton = (FloatingActionButton) findViewById(R.id.action_save);
        FloatingActionButton deleteButton = (FloatingActionButton) findViewById(R.id.action_delete);
        final Intent navigate = new Intent(this, Listing.class);
        final Mapper mapper = new Mapper(this);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = titleInput.getText().toString();
                String description = descriptionInput.getText().toString();
                if (editMode) {
                    mapper.update(new Task(title, description, currentTask.getId()));
                } else {
                    mapper.insert(new Task(title, description));
                }
                startActivity(navigate);
            }
        });
        if (editMode) {
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mapper.delete(currentTask);
                    startActivity(navigate);
                }
            });
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
