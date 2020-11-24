package com.ugo.android.roomwordssample;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

/**
 * The data access object, or Dao, is an annotated class where you specify SQL queries and
 * associate them with method calls. The DAO must be an interface or abstract class.
 */
@Dao
public interface WordDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Word word);

    @Query("DELETE FROM word_table")
    void deleteAll();

    /**
     * LiveData*: A data holder class that follows the observer pattern, which means that it can
     * be observed. Always holds/caches latest version of data. Notifies its observers when the
     * data has changed. Generally, UI components observe relevant data. LiveData is lifecycle-aware,
     * so it automatically manages stopping and resuming observation based on the state of its
     * observing activity or fragment.
     * @return
     */
    //LiveData, which is a lifecycle library class for data observation, can help your app respond
    // to data changes. If you use a return value of type LiveData in your method description,
    // Room generates all necessary code to update the LiveData when the database is updated.
    @Query("SELECT * from word_table ORDER BY word ASC")
    LiveData<List<Word>> getAllWords();
}
