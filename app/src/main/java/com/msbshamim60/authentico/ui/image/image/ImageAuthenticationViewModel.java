package com.msbshamim60.authentico.ui.image.image;

import android.app.Application;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.msbshamim60.authentico.Authenticator;


public class ImageAuthenticationViewModel extends AndroidViewModel {
    private MutableLiveData<Boolean> authenticationResult=new MutableLiveData<>();
    private Authenticator authenticator;


    public ImageAuthenticationViewModel(@NonNull Application application) {
        super(application);
        authenticator=new Authenticator(application);
    }

    public MutableLiveData<Boolean> getAuthenticationResult() {
        return authenticationResult;
    }

    public void authenticate(Uri sourceImage,Uri receivedImage) throws Exception {
        if (authenticator.generateHashFromImage(sourceImage)
                .equals(authenticator.generateHashFromImage(receivedImage))){
            Log.d("TAG","Authentic");
            authenticationResult.postValue(true);
        }else {
            Log.d("TAG", "Manipulated");
            authenticationResult.postValue(false);
        }
    }
}