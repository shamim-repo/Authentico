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

import android.util.Log;
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

import java.util.HashMap;
import java.util.Map;

public class HashFromDocumentFragment extends Fragment {
    private static final int SOURCE_Document_PIKING_CODE = 66;
    private ShapeableImageView sourceDocumentIconImageView;
    private TextView hashTextView,titleTextView;
    private MaterialButton copyButton,generateButton;
    private CardView cardView;
    private Uri sourceDocumentUri;
    private ProgressBar progressBar;

    private Map<String,Integer> fileTypeIcon;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_hash_from_document, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setFileTypeIcon();
        sourceDocumentIconImageView=view.findViewById(R.id.image_view_doc_type_icon_source_gen);
        hashTextView=view.findViewById(R.id.textView_hash_text_doc);
        titleTextView=view.findViewById(R.id.text_view_generate_hash_title_goc);
        copyButton=view.findViewById(R.id.button_copy_hash_doc);
        generateButton=view.findViewById(R.id.button_generate_hash_doc);
        cardView=view.findViewById(R.id.cardViewShowHash_doc);
        progressBar=view.findViewById(R.id.progress_circular);

        sourceDocumentIconImageView.setOnClickListener(v -> pickFile(SOURCE_Document_PIKING_CODE));

        generateButton.setOnClickListener(v -> {
            if (sourceDocumentUri!=null){
                progressBar.setVisibility(View.VISIBLE);
                cardView.setVisibility(View.GONE);
                try {
                    String hashString=startGenerateHash(sourceDocumentUri);
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
                Toast.makeText(getContext(),"Select Document",Toast.LENGTH_SHORT).show();
        });

        copyButton.setOnClickListener(v -> copyTask());
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
        if (requestCode == SOURCE_Document_PIKING_CODE && resultCode == Activity.RESULT_OK) {
            sourceDocumentUri = data.getData();
            sourceDocumentIconImageView.setImageResource(fileTypeIcon.get(getExtension(sourceDocumentUri)));
            titleTextView.setText(getFileTitle(sourceDocumentUri));
        }else if(requestCode==Activity.RESULT_CANCELED){
            Toast.makeText(getContext(),"Canceled",Toast.LENGTH_SHORT).show();
        }else
            Toast.makeText(getContext(),"Unknown error",Toast.LENGTH_SHORT).show();

    }

    private String getExtension(Uri uri){
        String extension = uri.getPath();
        extension=extension.substring(extension.lastIndexOf(".") + 1);
        extension=extension.toLowerCase();
        if(fileTypeIcon.get(extension)!=null)
            return extension;
        return "unknown";
    }

    private String getFileTitle(Uri uri){
        String title = uri.getPath();
        title=title.substring((title.lastIndexOf("/") + 1));
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

    private String startGenerateHash(Uri uri) throws Exception {
        Authenticator authenticator=new Authenticator(getActivity().getApplication());
        return authenticator.generateHashFromDoc(uri);
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