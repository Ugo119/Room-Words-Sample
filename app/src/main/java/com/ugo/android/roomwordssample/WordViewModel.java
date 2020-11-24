package com.ugo.android.roomwordssample;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

/**
 * The ViewModel is a class whose role is to provide data to the UI and survive configuration
 * changes. A ViewModel acts as a communication center between the Repository and the UI.
 * WARNING: *Never pass context into ViewModel instances.
 * Do not store Activity, Fragment, or View instances or their Context in the ViewModel.
 */
public class WordViewModel extends AndroidViewModel {
    //Add a private member variable to hold a reference to the Repository as well as LiveData member
    //variable to cache the list of words.
    private WordRepository mRepository;
    private LiveData<List<Word>> mAllWords;

    public WordViewModel (Application application) {
        super(application);
        mRepository = new WordRepository(application);
        mAllWords = mRepository.getAllWords();
    }

    //Add a "getter" method that gets all the words. This completely hides the
    // implementation from the UI.
    LiveData<List<Word>> getAllWords() {
        return mAllWords;
    }

    //Create a wrapper insert() method that calls the Repository's insert() method. In this way,
    // the implementation of insert() is completely hidden from the UI.
    public void insert(Word word) {
        mRepository.insert(word);
    }
}
