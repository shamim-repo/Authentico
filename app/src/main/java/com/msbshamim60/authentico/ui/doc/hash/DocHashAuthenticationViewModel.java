package com.msbshamim60.authentico.ui.doc.hash;

import android.app.Application;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.msbshamim60.authentico.Authenticator;

public class DocHashAuthenticationViewModel extends AndroidViewModel {
    private MutableLiveData<Boolean> authenticationResult=new MutableLiveData<>();
    private Authenticator authenticator;
    public DocHashAuthenticationViewModel(@NonNull Application application) {
        super(application);
        authenticator=new Authenticator(application);
    }
    public void authenticate(Uri uri,String hash) throws Exception {
        if(authenticator.generateHashFromDoc(uri).equals(hash)){
            authenticationResult.postValue(true);
        }else
            authenticationResult.postValue(false);
    }

    public MutableLiveData<Boolean> getAuthenticationResult() {
        return authenticationResult;
    }
}