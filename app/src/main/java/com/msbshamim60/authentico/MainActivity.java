package com.msbshamim60.authentico;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.button.MaterialButton;
import com.msbshamim60.authentico.ui.doc.DocumentAuthActivity;
import com.msbshamim60.authentico.ui.generate.GenerateSignActivity;
import com.msbshamim60.authentico.ui.image.ImageAuthActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MaterialButton buttonImageAuth=findViewById(R.id.button_image_auth);
        MaterialButton buttonDocAuth=findViewById(R.id.button_doc_auth);
        MaterialButton buttonGenerateSign=findViewById(R.id.button_generate_auth);

        buttonImageAuth.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), ImageAuthActivity.class)));
        buttonDocAuth.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), DocumentAuthActivity.class)));
        buttonGenerateSign.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), GenerateSignActivity.class)));
    }
}