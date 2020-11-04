package com.example.ketabeman21.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.example.ketabeman21.R;
import com.github.barteksc.pdfviewer.PDFView;

import java.io.File;

public class PDF_reader extends AppCompatActivity {
     private File file;
    private PDFView pdfView;
    private String address_book;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_p_d_f_reader);
        pdfView = findViewById(R.id.pdfView);
        try {
            address_book = getIntent().getStringExtra("pdfPath");
            Toast.makeText(this, address_book, Toast.LENGTH_SHORT).show();

        }
        catch (Exception e){
            address_book = "notFound";
        }
        ActivityCompat.requestPermissions(PDF_reader.this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_SETTINGS},
                1);
        initPDF();


    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    file = new File(address_book);
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            initPDF();
                        }

                    }, 2000);
                } else {
                    Toast.makeText(PDF_reader.this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    private void initPDF() {
        pdfView.fromFile(file)
                .enableSwipe(true) // allows to block changing pages using swipe
                .swipeHorizontal(false)
                .enableDoubletap(true)
                .defaultPage(0)
                .enableAnnotationRendering(false) // render annotations (such as comments, colors or forms)
                .password(null)
                .scrollHandle(null)
                .enableAntialiasing(true) // improve rendering a little bit on low-res screens
                // spacing between pages in dp. To define spacing color, set view background
                .spacing(0)
                .autoSpacing(false) // add dynamic spacing to fit each page on its own on the screen
                .fitEachPage(false) // fit each page to the view, else smaller pages are scaled relative to largest page.
                .pageSnap(false) // snap pages to screen boundaries
                .pageFling(false) // make a fling change only a single page like ViewPager
                .nightMode(true) // toggle night mode
                .load();;
    }
}