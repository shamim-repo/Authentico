package com.msbshamim60.authentico.ui.generate;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.msbshamim60.authentico.Authenticator;
import com.msbshamim60.authentico.R;

public class HashFormImageFragment extends Fragment {

    private static final int SOURCE_IMAGE_PIKING_CODE = 66;
    private ShapeableImageView sourceImageView;
    private TextView hashTextView;
    private MaterialButton copyButton,generateButton;
    private CardView cardView;
    private Uri sourceImageUri;
    private ProgressBar progressBar;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_hash_form_image, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sourceImageView=view.findViewById(R.id.image_view_source_image_gen);
        hashTextView=view.findViewById(R.id.textView_hash_text);
        copyButton=view.findViewById(R.id.button_copy_hash_img);
        generateButton=view.findViewById(R.id.button_generate_hash_img);
        cardView=view.findViewById(R.id.cardViewShowHash);
        progressBar=view.findViewById(R.id.progress_circular);

        sourceImageView.setOnClickListener(v -> openGallery());

        generateButton.setOnClickListener(v -> {
            if (sourceImageUri!=null){
                progressBar.setVisibility(View.VISIBLE);
                cardView.setVisibility(View.GONE);
                try {
                    String hashString=startGenerateHash(sourceImageUri);
                    if (hashString!=null && !hashString.equals("")){
                        cardView.setVisibility(View.VISIBLE);
                        hashTextView.setText(hashString);
                        progressBar.setVisibility(View.GONE);
                    }
                } catch (Exception e) {
                    progressBar.setVisibility(View.GONE);
                    e.printStackTrace();
                }
            }else
                Toast.makeText(getContext(),"Select Image",Toast.LENGTH_SHORT).show();
        });

        copyButton.setOnClickListener(v -> copyTask());
    }

    private void openGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, SOURCE_IMAGE_PIKING_CODE);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == SOURCE_IMAGE_PIKING_CODE){
            sourceImageUri= data.getData();
            sourceImageView.setImageURI(sourceImageUri);
        } else if(resultCode==Activity.RESULT_CANCELED) Toast.makeText(getView().getContext(), "Canceled", Toast.LENGTH_SHORT).show();
        else Toast.makeText(getView().getContext(), "Unknown Error", Toast.LENGTH_SHORT).show();
    }

    private String startGenerateHash(Uri uri) throws Exception {
        Authenticator authenticator=new Authenticator(getActivity().getApplication());
        return authenticator.generateHashFromImage(uri);
    }

    private void copyTask(){
        ClipboardManager clipboard = (ClipboardManager)
                getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
        String hashString=hashTextView.getText().toString();
        if (hashString.length()!=64){
            Toast.makeText(getContext(),"Copying Failed",Toast.LENGTH_SHORT).show();
            return;
        }
        ClipData clip = ClipData.newPlainText("hashString", hashString);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(getContext(),hashString,Toast.LENGTH_SHORT).show();
    }

}