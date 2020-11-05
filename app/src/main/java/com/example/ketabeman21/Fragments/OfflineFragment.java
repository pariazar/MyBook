package com.example.ketabeman21.Fragments;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;


import android.widget.Button;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ketabeman21.Activity.PDF_reader;
import com.example.ketabeman21.Adapter.DataAdapter4OfflineBook;
import com.example.ketabeman21.Adapter.DatabaseHelperOfflineBook;
import com.example.ketabeman21.Model.Book;
import com.example.ketabeman21.R;

public class OfflineFragment extends Fragment implements  DataAdapter4OfflineBook.BookAdapterListener {

    Button show;
    DatabaseHelperOfflineBook database;
    RecyclerView recyclerView;
    DataAdapter4OfflineBook recycler;
    ArrayList<Book> datamodel;
    //private ShimmerFrameLayout mShimmerViewContainer;
    //private SQLiteHandler db;
    private ImageView empty_iv;
    private TextView empty_txt1,empty_txt2;
    private CardView empty_c;
    private RelativeLayout empty_rl;
    View rootView;
    public static OfflineFragment newInstance() {

        return new OfflineFragment();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView= inflater.inflate(R.layout.fragment_offline_book, container, false);
        setRetainInstance(true);
        database = new DatabaseHelperOfflineBook(getContext());
        empty_iv  = rootView.findViewById(R.id.my_empty_list_lb);
        empty_txt1  = rootView.findViewById(R.id.emptylist1_tv);
        empty_txt2  = rootView.findViewById(R.id.emptylist2_tv);
        empty_c  = rootView.findViewById(R.id.noBookCardView);
        empty_rl  = rootView.findViewById(R.id.emrl);
        datamodel =new ArrayList<Book>();
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycle);
        loadData();

        if(database.check_empty_lib()){
            Toast.makeText(getContext(), "true", Toast.LENGTH_SHORT).show();
            recyclerView.setVisibility(View.GONE);
            empty_mode(View.VISIBLE);
        }
        else {
            Toast.makeText(getContext(), "false", Toast.LENGTH_SHORT).show();
            recyclerView.setVisibility(View.VISIBLE);
            empty_mode(View.INVISIBLE);

            try {
                loadData();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        }

        setHasOptionsMenu(true);

        return rootView;
    }

    private void empty_mode(int x) {
        empty_iv.setVisibility(x);
        empty_txt1.setVisibility(x);
        empty_txt2.setVisibility(x);
        empty_c.setVisibility(x);
        empty_rl.setVisibility(x);
    }

    private void loadData(){
        datamodel=  database.getdata();
        recycler =new DataAdapter4OfflineBook(datamodel, OfflineFragment.this,getContext());


        Log.i("HIteshdata",""+datamodel);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(recycler);

    }


    private void search(SearchView searchView) {

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });
    }

    @Override
    public void onBookSelected(Book book) {
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "Ketabeman/Books/" + book.getFullName()+"_withWaterMark"+".pdf");
        open_pdf_in_PDFReader(file,book);

    }

    private void open_pdf_in_PDFReader(File file,Book book) {
        Intent intent = new Intent(getContext(), PDF_reader.class);
        intent.putExtra("pdfPath",file.getAbsolutePath());
        intent.putExtra("book",book);
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        //mShimmerViewContainer.startShimmerAnimation();
    }

    @Override
    public void onPause() {
        //mShimmerViewContainer.stopShimmerAnimation();
        super.onPause();
    }

}
