package com.example.ketabeman21.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.example.ketabeman21.R;
import com.github.barteksc.pdfviewer.PDFView;

public class PDF_reader extends AppCompatActivity {
    private PDFView pdfView;
    private String address_book;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_p_d_f_reader);
        pdfView = findViewById(R.id.pdfView);
        try {
            address_book = getIntent().getStringExtra("address_book");
            Toast.makeText(this, address_book, Toast.LENGTH_SHORT).show();

        }
        catch (Exception e){
            address_book = "notFound";
        }
        pdfView.fromAsset(address_book)
            //    .pages(0, 2, 1, 3, 3, 3) // all pages are displayed by default
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
                .nightMode(false) // toggle night mode
                .load();
    }
}