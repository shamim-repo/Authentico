package com.msbshamim60.authentico.ui.image.hash;

import android.app.Application;
import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.msbshamim60.authentico.Authenticator;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

public class ImageHashAuthenticationViewModel extends AndroidViewModel {

    private MutableLiveData<Boolean> authenticationResult=new MutableLiveData<>();
    private Authenticator authenticator;
    public ImageHashAuthenticationViewModel(@NonNull Application application) {
        super(application);
        authenticator=new Authenticator(application);
    }


    public MutableLiveData<Boolean> getAuthenticationResult() {
        return authenticationResult;
    }

    public void authenticate(Uri uri, String sourceHash) throws Exception {

        sourceHash =sourceHash.toLowerCase();
        if(sourceHash.equals(authenticator.generateHashFromImage(uri))) {
            Log.d("TAG", "True");
            authenticationResult.postValue(true);
        }
        else {
            authenticationResult.postValue(false);
            Log.d("TAG", "False");
        }
    }
}