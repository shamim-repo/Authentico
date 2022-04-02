package com.msbshamim60.authentico.ui.image.image;

import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.msbshamim60.authentico.CustomDialog;
import com.msbshamim60.authentico.R;

public class ImageAuthenticationFragment extends Fragment {

    private int sourceImageCode=45;
    private int receivedImageCode=54;
    private Uri receivedImage;
    private Uri sourceImage;
    private ImageAuthenticationViewModel mViewModel;
    private ShapeableImageView sourceImageView;
    private ShapeableImageView receivedImageView;
    private ProgressBar circularProgressIndicator;
    public static ImageAuthenticationFragment newInstance() {
        return new ImageAuthenticationFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.image_autentication_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ImageAuthenticationViewModel.class);
        sourceImageView=view.findViewById(R.id.image_view_source_image);
        receivedImageView=view.findViewById(R.id.image_view_received);
        MaterialButton authenticateButton=view.findViewById(R.id.button_image_auth_image);

        circularProgressIndicator=view.findViewById(R.id.progress_circular);
        authenticateButton.setOnClickListener(v -> {
            if(receivedImage!=null && sourceImage!=null){
                circularProgressIndicator.setVisibility(View.VISIBLE);
                try {
                    mViewModel.authenticate(sourceImage,receivedImage);
                } catch (Exception e) {
                    circularProgressIndicator.setVisibility(View.GONE);
                    e.printStackTrace();
                }
            }
        });
        CustomDialog customDialog=new CustomDialog();
        mViewModel.getAuthenticationResult().observe(getViewLifecycleOwner(),aBoolean -> {
            circularProgressIndicator.setVisibility(View.GONE);
            if (aBoolean){
                Log.d("TAG","Authentic Image");
                customDialog.showDialogAuthentic(getActivity());

            }else {
                Log.d("TAG","Manipulated Image");
                customDialog.showDialogManipulated(getActivity());
            }
        });
        sourceImageView.setOnClickListener(v -> { openGallery(sourceImageCode);});
        receivedImageView.setOnClickListener(v -> { openGallery(receivedImageCode);});
    }
    private void openGallery(int pickerCode) {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, pickerCode);
    }

    @Override

     public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == sourceImageCode){
            sourceImage= data.getData();
            sourceImageView.setImageURI(sourceImage);
        }else if(resultCode == Activity.RESULT_OK && requestCode == receivedImageCode){
            receivedImage= data.getData();
            receivedImageView.setImageURI(receivedImage);
        }else if(resultCode==Activity.RESULT_CANCELED) Toast.makeText(getView().getContext(), "Canceled", Toast.LENGTH_SHORT).show();
        else Toast.makeText(getView().getContext(), "Unknown Error", Toast.LENGTH_SHORT).show();
    }
}