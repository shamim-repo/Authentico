package com.msbshamim60.authentico.ui.doc.document;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.OpenableColumns;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.msbshamim60.authentico.CustomDialog;
import com.msbshamim60.authentico.R;

import java.io.File;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class DocumentAuthenticationFragment extends Fragment {

    private DocumentAuthenticationViewModel mViewModel;
    private Map<String,Integer> fileTypeIcon;
    private static int PICKFILE_RESULT_SOURCE_CODE=47;
    private static int PICKFILE_RESULT_RECEIVED_CODE=74;

    private ShapeableImageView sourceImageView,receivedImageView;
    private TextView sourceTextView,receivedTextView;
    private MaterialButton authenticateButton,pastButton;
    private ProgressBar progressBar;

    private Uri sourceDocUri,receivedDocUri;

    public static DocumentAuthenticationFragment newInstance() {
        return new DocumentAuthenticationFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.document_authentication_fragment, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(DocumentAuthenticationViewModel.class);
        setFileTypeIcon();
        sourceImageView=view.findViewById(R.id.image_view_doc_type_icon_source);
        receivedImageView=view.findViewById(R.id.image_view_doc_type_icon_received);
        authenticateButton=view.findViewById(R.id.button_doc_auth_doc);
        sourceTextView=view.findViewById(R.id.text_view_doc_title_source);
        receivedTextView=view.findViewById(R.id.text_view_doc_title_received);
        progressBar=view.findViewById(R.id.progress_circular);

        sourceImageView.setOnClickListener(v -> pickFile(PICKFILE_RESULT_SOURCE_CODE));
        receivedImageView.setOnClickListener(v -> pickFile(PICKFILE_RESULT_RECEIVED_CODE));

        authenticateButton.setOnClickListener(v -> {
            if (sourceDocUri !=null && receivedDocUri !=null ){
                progressBar.setVisibility(View.VISIBLE);
                try {
                    mViewModel.authenticate(sourceDocUri,receivedDocUri);
                } catch (Exception e) {
                    progressBar.setVisibility(View.GONE);
                    e.printStackTrace();
                }
            }
        });

        CustomDialog customDialog=new CustomDialog();
        mViewModel.getAuthenticationResult().observe(getViewLifecycleOwner(),aBoolean -> {
            progressBar.setVisibility(View.GONE);
            if (aBoolean){
                customDialog.showDialogAuthentic(getActivity());
            }else
                customDialog.showDialogManipulated(getActivity());
        });
    }


    private void pickFile(int PICKFILE_RESULT_CODE){
        Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
        chooseFile.setType("*/*");
        chooseFile = Intent.createChooser(chooseFile, "Choose a file");
        startActivityForResult(chooseFile, PICKFILE_RESULT_CODE);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICKFILE_RESULT_SOURCE_CODE && resultCode == Activity.RESULT_OK) {
            sourceDocUri = data.getData();
            sourceImageView.setImageResource(fileTypeIcon.get(getExtension(sourceDocUri)));
            sourceTextView.setText(getFileTitle(sourceDocUri));
        }else if (requestCode == PICKFILE_RESULT_RECEIVED_CODE && resultCode == Activity.RESULT_OK) {
            receivedDocUri = data.getData();
            receivedImageView.setImageResource(fileTypeIcon.get(getExtension(receivedDocUri)));
            receivedTextView.setText(getFileTitle(receivedDocUri));

        }else if(requestCode==Activity.RESULT_CANCELED){
            Toast.makeText(getContext(),"Canceled",Toast.LENGTH_SHORT).show();
        }else
            Toast.makeText(getContext(),"Unknown error",Toast.LENGTH_SHORT).show();

    }

    private String getExtension(Uri uri){
        Cursor returnCursor =
                requireActivity().getContentResolver().query(uri, null, null, null, null);
        int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
        returnCursor.moveToFirst();
        String extension= returnCursor.getString(nameIndex).toLowerCase(Locale.ROOT);

        extension=extension.substring(extension.lastIndexOf(".")+1);
        Log.d("TAG","to l:"+extension);
        if(fileTypeIcon.get(extension)!=null)
            return extension;
        return "unknown";
    }

    private String getFileTitle(Uri uri){
        Cursor returnCursor =
                requireActivity().getContentResolver().query(uri, null, null, null, null);
        int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
        returnCursor.moveToFirst();
        return returnCursor.getString(nameIndex);
    }

    private void setFileTypeIcon(){
        fileTypeIcon=new HashMap<>();
        fileTypeIcon.put("doc",R.drawable.doc);
        fileTypeIcon.put("docx",R.drawable.doc);
        fileTypeIcon.put("pdf",R.drawable.pdf);
        fileTypeIcon.put("xlsx",R.drawable.spreadsheet);
        fileTypeIcon.put("ppt",R.drawable.pptx);
        fileTypeIcon.put("pptx",R.drawable.pptx);
        fileTypeIcon.put("txt",R.drawable.txt_file);
        fileTypeIcon.put("unknown",R.drawable.unkown);

    }

}