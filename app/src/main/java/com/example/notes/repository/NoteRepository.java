package com.example.notes.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.notes.dao.noteDao;
import com.example.notes.database.NoteDatabase;
import com.example.notes.models.Note;

import java.util.List;

public class NoteRepository {

    private noteDao noteDao;
    private LiveData<List<Note>> notes;


    public NoteRepository(Application application) {
        NoteDatabase database = NoteDatabase.getInstance(application);
        noteDao = database.noteDao();
        notes = noteDao.getAllNotes();
    }


    public void insert(Note note) {
        new InsertAsyncTask(noteDao).execute(note);
    }

    public void update(Note note) {
        new UpdateAsyncTask(noteDao).execute(note);
    }

    public void delete(Note note) {
        new DeleteAsyncTask(noteDao).execute(note);
    }

    public void deleteAll() {
        new DeleteAllAsyncTask(noteDao).execute();
    }

    public LiveData<List<Note>> getNotes() {
        return notes;
    }


    private static class InsertAsyncTask extends AsyncTask<Note, Void, Void> {
        private noteDao noteDao;

        private InsertAsyncTask(noteDao noteDao) {
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            noteDao.insert(notes[0]);
            return null;
        }
    }


    private static class UpdateAsyncTask extends AsyncTask<Note, Void, Void> {
        private noteDao noteDao;

        private UpdateAsyncTask(noteDao noteDao) {
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            noteDao.update(notes[0]);
            return null;
        }
    }

    private static class DeleteAsyncTask extends AsyncTask<Note, Void, Void> {
        private noteDao noteDao;

        private DeleteAsyncTask(noteDao noteDao) {
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            noteDao.delete(notes[0]);
            return null;
        }
    }

    private static class DeleteAllAsyncTask extends AsyncTask<Void, Void, Void> {
        private noteDao noteDao;

        private DeleteAllAsyncTask(noteDao noteDao) {
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            noteDao.deleteAll();
            return null;
        }
    }

}