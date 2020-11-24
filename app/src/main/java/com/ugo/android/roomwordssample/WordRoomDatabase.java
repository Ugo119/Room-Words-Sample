package com.ugo.android.roomwordssample;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

/**
 * Room is a database layer on top of an SQLite database. Room takes care of mundane tasks that
 * you used to handle with a database helper class such as SQLiteOpenHelper.
 * Room uses the DAO to issue queries to its database.
 * By default, to avoid poor UI performance, Room doesn't allow you to issue database queries on
 * the main thread. LiveData applies this rule by automatically running the query asynchronously
 * on a background thread, when needed.
 * exportSchema keeps a history of schema versions. For this practical, we will disable it, since
 * we are not migrating the database.
  */
@Database(entities = {Word.class}, version = 1, exportSchema = false)
public abstract class WordRoomDatabase extends RoomDatabase {
    public abstract WordDao wordDao();

    //Create the WordRoomDatabase as a singleton to prevent having multiple instances of the
    // database opened at the same time, which would be a bad thing
    private static WordRoomDatabase INSTANCE;
    public static WordRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (WordRoomDatabase.class) {
                if (INSTANCE == null) {
                    // Create database here
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            WordRoomDatabase.class, "word_database")
                            // Wipes and rebuilds instead of migrating
                            // if no Migration object.
                            // Migration is not part of this practical.
                            .fallbackToDestructiveMigration()
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

     //To delete all content and repopulate the database whenever the app is started, you create a
     //RoomDatabase.Callback and override the onOpen() method. Because you cannot do Room database
     //operations on the UI thread, onOpen() creates and executes an AsyncTask to add content to
     //the database.
    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {

        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
            new PopulateDbAsync(INSTANCE).execute();
        }
    };

     //Populate the database in the background. Create an inner class PopulateDbAsync that extends
     //AsycTask. Implement the doInBackground() method to delete all words, then create new ones.
    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final WordDao mDao;
        String[] words = {"dolphin", "crocodile", "cobra"};

        PopulateDbAsync(WordRoomDatabase db) {
            mDao = db.wordDao();
        }

        @Override
        protected Void doInBackground(final Void... params) {
            // Start the app with a clean database every time.
            // Not needed if you only populate the database
            // when it is first created
            mDao.deleteAll();

            for (int i = 0; i <= words.length - 1; i++) {
                Word word = new Word(words[i]);
                mDao.insert(word);
            }
            return null;
        }
    }
}
