package com.example.ketabeman21.Fragments;

import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ketabeman21.Activity.Detail_Book;
import com.example.ketabeman21.Activity.MainActivity;
import com.example.ketabeman21.Adapter.DataAdapter4Book;
import com.example.ketabeman21.Model.Book;
import com.example.ketabeman21.Network.JSONResponse;
import com.example.ketabeman21.Network.RequestInterface;
import com.example.ketabeman21.R;
import com.example.ketabeman21.Utils.Constants;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BookFragment extends Fragment implements DataAdapter4Book.BookAdapterListener,DataAdapter4Book.BookDetailAdapterListener {

    private RecyclerView mRecyclerView;
    private ArrayList<Book> mArrayList;
    private DataAdapter4Book mAdapter;
    private ShimmerFrameLayout mShimmerViewContainer;

    public static final String TITLE = "کتابها";
    View rootView;
    public static BookFragment newInstance() {

        return new BookFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView= inflater.inflate(R.layout.fragment_book, container, false);
         setRetainInstance(true);


        /*Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);*/
        initViews();
        try {
            loadJSON();
            Toast.makeText(getContext(), "done", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Force Close", Toast.LENGTH_SHORT).show();
            System.exit(0);

        }
        //setHasOptionsMenu(true);

        return rootView;
    }






    private void initViews(){
       /* mShimmerViewContainer = (ShimmerFrameLayout) rootView.findViewById(R.id.shimmer_view_container);
        floatingActionButton = rootView.findViewById(R.id.fab);*/
        mRecyclerView = (RecyclerView)rootView.findViewById(R.id.card_recycler_view);
        mShimmerViewContainer = rootView.findViewById(R.id.shimmer_view_container);
        mShimmerViewContainer.startShimmer();
        /*mShimmerViewContainer.setVisibility(View.INVISIBLE);
        mRecyclerView.setHasFixedSize(true);*/
        //RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        // true if item was selected
       // mRecyclerView.setLayoutManager(layoutManager);

    }
    private void loadJSON(){


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RequestInterface request = retrofit.create(RequestInterface.class);
        Call<JSONResponse> call = request.getJSONBook("1");
        call.enqueue(new Callback<JSONResponse>() {
            @Override
            public void onResponse(Call<JSONResponse> call, Response<JSONResponse> response) {

                JSONResponse jsonResponse = response.body();
                mArrayList = new ArrayList<>(Arrays.asList(jsonResponse.getBook()));
                mAdapter = new DataAdapter4Book(mArrayList,getActivity(),BookFragment.this,BookFragment.this);
                mRecyclerView.setAdapter(mAdapter);
                mShimmerViewContainer.stopShimmer();
                mShimmerViewContainer.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<JSONResponse> call, Throwable t) {
                Log.d("Error",t.getMessage());
            }
        });
    }




    private void search(SearchView searchView) {

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                if (mAdapter != null) mAdapter.getFilter().filter(newText);
                return true;
            }
        });
    }

    @Override
    public void onContactSelected(Book book) {
        Toast.makeText(getContext(), book.getFullName(), Toast.LENGTH_SHORT).show();
    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        return super.onOptionsItemSelected(item);

    }
    @Override
    public void onResume() {
        super.onResume();
        // mShimmerViewContainer.startShimmerAnimation();
    }

    @Override
    public void onPause() {
         mShimmerViewContainer.stopShimmer();
        super.onPause();
    }


    @Override
    public void onBookSelected(Book book) {
        Toast.makeText(getContext(), book.getAuthor()+"دانلود کتاب ", Toast.LENGTH_SHORT).show();
        Intent i = new Intent(getContext(), Detail_Book.class);
        i.putExtra("ibook",book);
        startActivity(i);
    }
}
