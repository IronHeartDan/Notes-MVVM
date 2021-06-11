package com.example.notes;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class add_edit_note extends AppCompatActivity {

    public static final String EXTRA_ID = "ID";
    public static final String EXTRA_TITLE = "TITLE";
    public static final String EXTRA_DESCRIPTION = "DESCRIPTION";
    public static final String EXTRA_PRIORITY = "PRIORITY";

    private EditText title_input, description_input;
    private NumberPicker priority_input;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        title_input = findViewById(R.id.text_input);
        description_input = findViewById(R.id.description_input);
        priority_input = findViewById(R.id.priority_input);

        priority_input.setMinValue(1);
        priority_input.setMaxValue(10);

        getSupportActionBar().setHomeAsUpIndicator(getDrawable(R.drawable.ic_close));
        Intent intent = getIntent();
        if(intent.hasExtra(EXTRA_ID)){
            setTitle("Edit Note");
            title_input.setText(intent.getStringExtra(EXTRA_TITLE));
            description_input.setText(intent.getStringExtra(EXTRA_DESCRIPTION));
            priority_input.setValue(intent.getIntExtra(EXTRA_PRIORITY,1));
        }else{
            setTitle("Add Note");
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.save, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_note:
                save();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void save() {
        String title = title_input.getText().toString().trim();
        String description = description_input.getText().toString().trim();
        int priority = priority_input.getValue();
        if(title.isEmpty() || description.isEmpty()){
            Toast.makeText(add_edit_note.this,"Please Fill Details",Toast.LENGTH_SHORT).show();
        }else{
            Intent intent = new Intent();
            intent.putExtra(EXTRA_TITLE,title);
            intent.putExtra(EXTRA_DESCRIPTION,description);
            intent.putExtra(EXTRA_PRIORITY,priority);

            int id = getIntent().getIntExtra(EXTRA_ID,-1);
            if(id != -1){
                intent.putExtra(EXTRA_ID,id);
            }

            setResult(RESULT_OK,intent);
            finish();
        }
    }
}