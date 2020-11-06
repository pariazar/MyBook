package com.example.ketabeman21.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
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
import com.example.ketabeman21.Adapter.DataAdapter4Book;
import com.example.ketabeman21.Adapter.DataAdapter4Cat;
import com.example.ketabeman21.Model.Book;
import com.example.ketabeman21.Model.Category;
import com.example.ketabeman21.Network.JSONResponse;
import com.example.ketabeman21.Network.RequestInterface;
import com.example.ketabeman21.R;
import com.example.ketabeman21.Utils.Constants;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CategoryFragment extends Fragment implements DataAdapter4Cat.CategoryAdapterListener {

    private RecyclerView mRecyclerView;
    private ArrayList<Category> mArrayList;
    private DataAdapter4Cat mAdapter;
    private ShimmerFrameLayout mShimmerViewContainer;

    View rootView;
    public static CategoryFragment newInstance() {
        return new CategoryFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView= inflater.inflate(R.layout.fragment_category, container, false);
        setRetainInstance(true);

        initViews();
        try {
            loadJSONCategory();
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
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        // true if item was selected
       // mRecyclerView.setLayoutManager(layoutManager);

    }
    private void loadJSONCategory(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RequestInterface request = retrofit.create(RequestInterface.class);
        Call<JSONResponse> call = request.getJSONBook("3");
        call.enqueue(new Callback<JSONResponse>() {
            @Override
            public void onResponse(Call<JSONResponse> call, Response<JSONResponse> response) {

                JSONResponse jsonResponse = response.body();
                mArrayList = new ArrayList<>(Arrays.asList(jsonResponse.getCategory()));
                mAdapter = new DataAdapter4Cat(mArrayList,getActivity(), CategoryFragment.this);
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


    private void loadJSONSubCategory(String catId){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RequestInterface request = retrofit.create(RequestInterface.class);
        Call<JSONResponse> call = request.getSubCat("4",catId);
        call.enqueue(new Callback<JSONResponse>() {
            @Override
            public void onResponse(Call<JSONResponse> call, Response<JSONResponse> response) {

                JSONResponse jsonResponse = response.body();
                mArrayList = new ArrayList<>(Arrays.asList(jsonResponse.getCategory()));
                mAdapter = new DataAdapter4Cat(mArrayList,getActivity(), CategoryFragment.this);
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
    public void onCategorySelected(Category Category) {
        loadJSONSubCategory(Category.getName());
    }
}
