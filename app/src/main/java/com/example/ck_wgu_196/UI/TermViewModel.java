package com.example.ck_wgu_196.UI;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.ck_wgu_196.Database.Repository;
import com.example.ck_wgu_196.Entity.Term;

import java.util.List;

public class TermViewModel extends AndroidViewModel {
    private Repository mRepository;
    private LiveData<List<Term>> mAllTerms;

    public TermViewModel(Application application){
        super(application);
        mRepository = new Repository(application);
        mAllTerms = (LiveData<List<Term>>) mRepository.getAllTerms();
    }

    public LiveData<List<Term>> getAllTerms() {
        return mAllTerms;
    }

    public void insert(Term term){
        mRepository.insert(term);
    }

    public void updateTerm(Term term) {
        mRepository.update(term);
    }

    public void deleteTerm(Term term) {
        mRepository.delete(term);
    }
}
