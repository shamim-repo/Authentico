package com.msbshamim60.authentico.ui.doc.document;

import android.app.Application;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.msbshamim60.authentico.Authenticator;

public class DocumentAuthenticationViewModel extends AndroidViewModel {
    private MutableLiveData<Boolean> authenticationResult=new MutableLiveData<>();
    private Authenticator authenticator;
    public DocumentAuthenticationViewModel(@NonNull Application application) {
        super(application);
        authenticator=new Authenticator(application);
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void authenticate(Uri sourceUri, Uri receivedUri) throws Exception {
        if (authenticator.generateHashFromDoc(sourceUri).equals(authenticator.generateHashFromDoc(receivedUri))){
            authenticationResult.postValue(true);
        }else
            authenticationResult.postValue(false);
    }

    public MutableLiveData<Boolean> getAuthenticationResult() {
        return authenticationResult;
    }
}