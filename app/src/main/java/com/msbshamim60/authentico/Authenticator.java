package com.msbshamim60.authentico;

import android.app.Application;
import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;

public class Authenticator {
    private Application application;
    public Authenticator(Application application){
        this.application=application;
    }

    public String generateHashFromImage(Uri uri) throws Exception {
        byte[] inputData = convertImageToByte(uri);
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(inputData);
        byte[] hashByte = md.digest();
        return returnHex(hashByte);
    }


    public String generateHashFromDoc(Uri uri) throws Exception {

        byte[] inputData =getBytesFromDocURi(uri);
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(inputData);
        byte[] hashByte = md.digest();
        return returnHex(hashByte);
    }

    private byte[] getBytesFromDocURi(Uri uri) throws IOException {
        InputStream iStream =   application.getContentResolver().openInputStream(uri);
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = iStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

    public byte[] convertImageToByte(Uri uri){
        byte[] data = null;

        try {
            ContentResolver cr = application.getBaseContext().getContentResolver();
            String extension = application.getContentResolver().getType(uri);
            InputStream inputStream = cr.openInputStream(uri);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(getCompressFormat(extension), 100, baos);
            data = baos.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return data;
    }

    private Bitmap.CompressFormat getCompressFormat(String extension){
        extension=extension.toUpperCase();
        switch (extension){
            case "PNG": return Bitmap.CompressFormat.PNG;
            case "WEBP": return Bitmap.CompressFormat.WEBP;
            default: return Bitmap.CompressFormat.JPEG;
        }
    }

    static String returnHex(byte[] hash) throws Exception {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if(hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}