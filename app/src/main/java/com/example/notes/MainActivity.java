package com.example.notes;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notes.adapter.noteAdapter;
import com.example.notes.models.Note;
import com.example.notes.viewmodels.NoteViewModel;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView recyclerView;
    private noteAdapter adapter;
    private NoteViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.note_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new noteAdapter();
        recyclerView.setAdapter(adapter);

        viewModel = new ViewModelProvider(this).get(NoteViewModel.class);
        viewModel.getNotes().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
                adapter.submitList(notes);
            }
        });


        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                viewModel.delete(adapter.getNoteAt(viewHolder.getAdapterPosition()));
                Toast.makeText(MainActivity.this, "Note Deleted", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(recyclerView);

        adapter.setOnItemClickListener(new noteAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Note note) {
                Intent intent = new Intent(MainActivity.this, add_edit_note.class);
                intent.putExtra(add_edit_note.EXTRA_ID, note.getId());
                intent.putExtra(add_edit_note.EXTRA_TITLE, note.getTitle());
                intent.putExtra(add_edit_note.EXTRA_DESCRIPTION, note.getDescription());
                intent.putExtra(add_edit_note.EXTRA_PRIORITY, note.getPriority());
                startActivityForResult(intent, 212);
            }
        });

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.add_note) {
            Intent intent = new Intent(MainActivity.this, add_edit_note.class);
            startActivityForResult(intent, 121);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 121 && resultCode == RESULT_OK) {
            Note note = new Note(data.getStringExtra(add_edit_note.EXTRA_TITLE), data.getStringExtra(add_edit_note.EXTRA_DESCRIPTION), data.getIntExtra(add_edit_note.EXTRA_PRIORITY, 1));
            viewModel.insert(note);
        } else if (requestCode == 212 && resultCode == RESULT_OK) {
            int id = data.getIntExtra(add_edit_note.EXTRA_ID, -1);
            Note note = new Note(data.getStringExtra(add_edit_note.EXTRA_TITLE), data.getStringExtra(add_edit_note.EXTRA_DESCRIPTION), data.getIntExtra(add_edit_note.EXTRA_PRIORITY, 1));
            note.setId(id);
            viewModel.update(note);
        } else {
            Toast.makeText(MainActivity.this, "Action Canceled", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.delete, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.delete_all) {
            viewModel.deleteAll();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}