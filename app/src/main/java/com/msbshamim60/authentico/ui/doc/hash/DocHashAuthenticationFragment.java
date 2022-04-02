package com.msbshamim60.authentico.ui.doc.hash;

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

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textfield.TextInputEditText;
import com.msbshamim60.authentico.CustomDialog;
import com.msbshamim60.authentico.R;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DocHashAuthenticationFragment extends Fragment {

    private static int PICKFILE_RESULT_CODE=77;
    private Map<String,Integer> fileTypeIcon;
    private DocHashAuthenticationViewModel mViewModel;
    private ShapeableImageView receivedImageView;
    private TextView receivedTextView;
    private TextInputEditText sourceHashInputText;
    private MaterialButton pastButton,authenticateButton;
    private ProgressBar progressBar;

    private Uri receivedDocUri;

    public static DocHashAuthenticationFragment newInstance() {
        return new DocHashAuthenticationFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.doc_hash_autentication_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setFileTypeIcon();
        mViewModel = new ViewModelProvider(this).get(DocHashAuthenticationViewModel.class);
        receivedImageView=view.findViewById(R.id.image_view_doc_type_icon_received);
        receivedTextView=view.findViewById(R.id.text_view_doc_title_received);
        sourceHashInputText=view.findViewById(R.id.input_text_doc_hash);
        pastButton =view.findViewById(R.id.button_doc_past_hash);
        authenticateButton=view.findViewById(R.id.button_doc_auth_hash);
        progressBar=view.findViewById(R.id.progress_circular);

        receivedImageView.setOnClickListener(v -> pickFile(PICKFILE_RESULT_CODE) );
        pastButton.setOnClickListener(v ->{
            ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData.Item item = clipboard.getPrimaryClip().getItemAt(0);
            String pasteData =  item.getText().toString();
            if (pasteData!=null){
                sourceHashInputText.setText(pasteData);
            }
        });
        authenticateButton.setOnClickListener(v -> {
            try {
                beginAuthProcess();
            } catch (Exception e) {
                progressBar.setVisibility(View.GONE);
                e.printStackTrace();
            }
        });

        CustomDialog customDialog=new CustomDialog();
        mViewModel.getAuthenticationResult().observe(getViewLifecycleOwner(),aBoolean -> {
            progressBar.setVisibility(View.GONE);
            if (aBoolean)
                customDialog.showDialogAuthentic(getActivity());
            else
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
        if (requestCode == PICKFILE_RESULT_CODE && resultCode == Activity.RESULT_OK) {
            receivedDocUri = data.getData();
            receivedImageView.setImageResource(fileTypeIcon.get(getExtension(receivedDocUri)));
            receivedTextView.setText(getFileTitle(receivedDocUri));
        }else if(requestCode==Activity.RESULT_CANCELED){
            Toast.makeText(getContext(),"Canceled",Toast.LENGTH_SHORT).show();
        }else
            Toast.makeText(getContext(),"Unknown error",Toast.LENGTH_SHORT).show();

    }

    private String getExtension(Uri uri){
        String extension = uri.getPath();
        extension=extension.substring(extension.lastIndexOf(".") + 1);
        extension=extension.toLowerCase();
        Log.d("TAG", "Extension "+extension);
        if(fileTypeIcon.get(extension)!=null)
            return extension;
        return "unknown";
    }

    private String getFileTitle(Uri uri){
        String title = uri.getPath();
        title=title.substring((title.lastIndexOf("/") + 1));
        title=title.toLowerCase();
        Log.d("TAG", "Extension "+title);
        return title;
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

    private void beginAuthProcess() throws Exception {
        if(!checkHash())
            return;
        if(receivedDocUri==null) {
            Toast.makeText(getContext(), "Select Image", Toast.LENGTH_SHORT).show();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        startAuthentication();
    }

    private Boolean startAuthentication() throws Exception {
        mViewModel.authenticate(receivedDocUri,sourceHashInputText.getText().toString());
        return true;
    }

    private Boolean checkHash(){
        String hash= Objects.requireNonNull(sourceHashInputText.getText()).toString();
        Pattern pattern = Pattern.compile("[A-Fa-f0-9]{64}");
        Matcher matcher = pattern.matcher(hash);
        if (!matcher.find()) {
            sourceHashInputText.setError("Enter A valid Hash");
            return false;
        }
        return true;
    }
}