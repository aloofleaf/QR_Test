package com.example.kskotheim.qr_test;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;

public class MainActivity extends Activity {

    private TextView mResultsTextView;
    private Button mScannerButton;
    private CameraSource mCameraSource;

    Bitmap myQRCode;
    BarcodeDetector barcodeDetector;
    Toast theToast;
    SparseArray<Barcode> barcodes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mResultsTextView = (TextView) findViewById(R.id.results_textview);
        mScannerButton = (Button) findViewById(R.id.scanner_button);


        try {
             myQRCode = BitmapFactory.decodeStream(
                    getAssets().open("qr.png")
            );
            makeToast("loaded image");
        } catch (IOException e) {
            makeToast("error reading image");
            e.printStackTrace();
        }

        barcodeDetector =
                new BarcodeDetector.Builder(this)
                        .setBarcodeFormats(Barcode.QR_CODE)
                        .build();

        Frame myFrame = new Frame.Builder()
                .setBitmap(myQRCode)
                .build();

        if(barcodeDetector.isOperational()) mResultsTextView.setText("barcode detector operational");
        else mResultsTextView.setText("barcode detector not operational");

        barcodes = barcodeDetector.detect(myFrame);

        mCameraSource = new CameraSource.Builder(this, barcodeDetector)
                .setFacing(CameraSource.CAMERA_FACING_FRONT)
                .setRequestedPreviewSize(320, 240)
                .build();

    }

    public void scanQR(View view) {

        int id = view.getId();

        if(id == R.id.scanner_button){

            if(barcodes.size() != 0) {

                // Print the QR code's message
                String displayMe =
                        barcodes.valueAt(0).displayValue;

                mResultsTextView.setText("QR Code Data: " + displayMe);
            }
        }
    }

    private void makeToast (String displayMe){
        if(theToast != null){
            theToast.cancel();
        }
        theToast = Toast.makeText(this, displayMe, Toast.LENGTH_LONG);
        theToast.show();
    }
}
