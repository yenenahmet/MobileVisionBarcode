package com.example.ahmet.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
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

public class BaseBarcodeCaptureActivity extends AppCompatActivity  implements BarcodeRetriever {

    protected BarcodeCapture barcodeCapture = null;
    protected AppCompatTextView barcodetext;
    protected List<String> barcodeList;
    protected ArrayList<String> barcodeListReturn;
    protected HashMap<String,Integer> hashMapMach;

    protected void setBarcodeCapture(boolean flash){
        barcodeCapture.setShowDrawRect(true)
                .setSupportMultipleScan(false)
                .setTouchAsCallback(false)
                .shouldAutoFocus(true)
                .setShowFlash(flash)
                .setBarcodeFormat(Barcode.ALL_FORMATS)
                .setCameraFacing(CameraSource.CAMERA_FACING_BACK)
                .setShouldShowText(true);
    }

    private boolean isValidBarcode(String rawValue){
        for(String value : barcodeListReturn){
            if(value.equals(rawValue)){
                return true;
            }
        }
        return false;
    }

    protected void init(){
        barcodeCapture = (BarcodeCapture) getSupportFragmentManager().findFragmentById(R.id.barcode);
        if (barcodeCapture != null) {
            barcodeCapture.setRetrieval(this);
        }
        barcodetext = findViewById(R.id.BarcodeText);
        barcodetext.setMovementMethod(new ScrollingMovementMethod());
        barcodeList = new ArrayList<>();
        barcodeList.add("\n");
        hashMapMach = new HashMap<>();
        barcodeListReturn = new ArrayList<>();
        barcodeCapture.pause();
        barcodeCapture.refresh(true);
    }

    private boolean isHash(String value){
        barcodeList.add(value);
        for(String values:barcodeList){
            if(values.equals(value) && hashMapMach != null){
                Integer valuesMach =  hashMapMach.get(value);

                if(valuesMach == null)
                    valuesMach = 0;

                valuesMach  = valuesMach +1 ;
                hashMapMach.put(value,valuesMach);
                if(valuesMach >2){
                    hashMapMach.put(value,0);
                    barcodeList.remove(value);
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(barcodeCapture != null){
            barcodeCapture.pause();
            barcodeCapture.refresh(true);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Util.ScreenBarClear(this);
        ClearALL();
        if(barcodeCapture != null){
            barcodeCapture.resume();
            barcodeCapture.refresh(true);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(barcodeCapture != null){
            barcodeCapture.onDestroy();
            barcodeCapture = null;
        }
        hashMapMach = null;
        barcodeListReturn = null;
        barcodeList = null;
    }

    @Override
    public void onRetrieved(Barcode barcode) {
        if(isHash(barcode.rawValue) ) {
            if(!isValidBarcode(barcode.rawValue)) {
                runOnUiThread(() -> {
                    barcodeListReturn.add(barcode.rawValue);
                    barcodetext.append(barcode.rawValue + "\n");
                });
            }
        }
    }

    @Override
    public void onRetrievedMultiple(Barcode closetToClick, List<BarcodeGraphic> barcode) {

    }

    @Override
    public void onBitmapScanned(SparseArray<Barcode> sparseArray) {

    }

    @Override
    public void onRetrievedFailed(String reason) {

    }

    @Override
    public void onPermissionRequestDenied() {

    }


    protected void ClearALL(){
        barcodeList.clear();
        hashMapMach.clear();
        barcodetext.setText("");
        barcodeListReturn.clear();
    }

    private void ResultIntent(){
        Intent returnIntent = new Intent();
        returnIntent.putStringArrayListExtra("result",barcodeListReturn);
        setResult(Activity.RESULT_OK,returnIntent);
        finish();
    }
}
