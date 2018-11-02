package com.example.ahmet.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.text.method.ScrollingMovementMethod;
import android.util.SparseArray;
import com.google.android.gms.samples.vision.barcodereader.BarcodeCapture;
import com.google.android.gms.samples.vision.barcodereader.BarcodeGraphic;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.barcode.Barcode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import xyz.belvi.mobilevisionbarcodescanner.BarcodeRetriever;

public class MainActivity extends BaseBarcodeCaptureActivity  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        AppCompatButton btnStart = findViewById(R.id.StartPause);
        btnStart.setOnClickListener(v -> {
            setBarcodeCapture(false);
            if(btnStart.getText().equals("Start")){
                btnStart.setText("Pause");
                barcodeCapture.resume();
                ClearALL();
            }else{
                btnStart.setText("Start");
                barcodeCapture.pause();
            }
            barcodeCapture.refresh(true);
        });
    }

}
