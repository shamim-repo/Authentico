package com.msbshamim60.authentico.ui.image.hash;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.service.autofill.RegexValidator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textfield.TextInputEditText;
import com.msbshamim60.authentico.CustomDialog;
import com.msbshamim60.authentico.R;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ImageHashAuthenticationFragment extends Fragment {

    private Uri imageUri;
    private ImageHashAuthenticationViewModel mViewModel;
    private ShapeableImageView imageView;
    private TextInputEditText imageHashEditText;
    private ProgressBar circularProgressIndicator;
    public static ImageHashAuthenticationFragment newInstance() {
        return new ImageHashAuthenticationFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.image_hash_autentication_fragment, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ImageHashAuthenticationViewModel.class);

        imageView=view.findViewById(R.id.image_view_received_hash);
        imageHashEditText=view.findViewById(R.id.input_text_image_hash);
        MaterialButton materialButton=view.findViewById(R.id.button_image_auth_hash);
        MaterialButton pastButton=view.findViewById(R.id.button_image_past_hash);
        circularProgressIndicator=view.findViewById(R.id.progress_circular);
        pastButton.setOnClickListener(v -> {
            ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData.Item item = clipboard.getPrimaryClip().getItemAt(0);
            String pasteData =  item.getText().toString();

            if (pasteData!=null){
                imageHashEditText.setText(pasteData);
            }
        });
        materialButton.setOnClickListener(v -> {
            try {
                beginAuthProcess();
            } catch (Exception e) {
                circularProgressIndicator.setVisibility(View.GONE);
                e.printStackTrace();
            }
        });
        imageView.setOnClickListener(v -> {
            openGallery();
        });
        CustomDialog customDialog=new CustomDialog();
        mViewModel.getAuthenticationResult().observe(getViewLifecycleOwner(), aBoolean -> {
            circularProgressIndicator.setVisibility(View.GONE);
            if(aBoolean){
                //Authentic Image
                Log.d("TAG","Authentic Image");
                customDialog.showDialogAuthentic(getActivity());
            }else {
                //Manipulated Image
                Log.d("TAG","Manipulated Image");
                customDialog.showDialogManipulated(getActivity());
            }
        });

    }

    private void beginAuthProcess() throws Exception {
        if(imageUri==null) {
            Toast.makeText(getContext(), "Select Image", Toast.LENGTH_SHORT).show();
            return;
        }
        if(!checkHash())
            return;
        circularProgressIndicator.setVisibility(View.VISIBLE);
        startAuthentication();
    }

    private Boolean startAuthentication() throws Exception {
        mViewModel.authenticate(imageUri,imageHashEditText.getText().toString());
        return true;
    }

    private void openGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery,1);

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == 1){
            imageUri= data.getData();
            imageView.setImageURI(imageUri);
        }else if(resultCode==Activity.RESULT_CANCELED) Toast.makeText(getView().getContext(), "Canceled", Toast.LENGTH_SHORT).show();
        else Toast.makeText(getView().getContext(), "Unknown Error", Toast.LENGTH_SHORT).show();
    }

    private Boolean checkHash(){
        String hash= Objects.requireNonNull(imageHashEditText.getText()).toString();
        Pattern pattern = Pattern.compile("[A-Fa-f0-9]{64}");
        Matcher matcher = pattern.matcher(hash);
        if (!matcher.find()) {
            imageHashEditText.setError("Enter A valid Hash");
            return false;
        }
        return true;
    }


}