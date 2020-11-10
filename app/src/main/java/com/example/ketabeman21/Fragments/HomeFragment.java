package com.example.ketabeman21.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ketabeman21.Activity.Detail_Book;
import com.example.ketabeman21.Activity.Register;
import com.example.ketabeman21.Adapter.DataAdapter4Book;
import com.example.ketabeman21.Helper.SessionManager;
import com.example.ketabeman21.Model.Book;
import com.example.ketabeman21.Model.DB.SQLiteHandler;
import com.example.ketabeman21.Network.JSONResponse;
import com.example.ketabeman21.Network.RequestInterface;
import com.example.ketabeman21.R;
import com.example.ketabeman21.Utils.Constants;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.shehabic.droppy.DroppyClickCallbackInterface;
import com.shehabic.droppy.DroppyMenuItem;
import com.shehabic.droppy.DroppyMenuPopup;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import me.grantland.widget.AutofitHelper;
import me.grantland.widget.AutofitTextView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeFragment extends Fragment implements DataAdapter4Book.BookAdapterListener,DataAdapter4Book.BookDetailAdapterListener {

    private RecyclerView mRecyclerView;
    private ArrayList<Book> mArrayList;
    private DataAdapter4Book mAdapter;
    private ShimmerFrameLayout mShimmerViewContainer;



    private CircleImageView profileImage;
    private ImageView more;
    final String myAddress = Constants.BASE_URL+"/files/profilepics/";
    private SQLiteHandler db;
    private SessionManager sessionManager;
    private Button registerBTN;
    private AutofitTextView username,name;
    HashMap<String, String> user;

    public static final String TITLE = "کتابها";
    View rootView;
    public static HomeFragment newInstance() {

        return new HomeFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView= inflater.inflate(R.layout.fragment_home, container, false);
         setRetainInstance(true);
        registerBTN = rootView.findViewById(R.id.regBtn);
        username = rootView.findViewById(R.id.tv_user);
        name = rootView.findViewById(R.id.tv_name);
        profileImage = rootView.findViewById(R.id.profile);
        more = rootView.findViewById(R.id.more);
        AutofitHelper.create(name);
        AutofitHelper.create(username);

        db = new SQLiteHandler(getContext());
        sessionManager = new SessionManager(getContext());

       if(sessionManager.isLoggedIn()){
            registerBTN.setVisibility(View.GONE);
            username.setVisibility(View.VISIBLE);
            name.setVisibility(View.VISIBLE);
            user = db.getUserDetails();
            Picasso.with(getContext())
                    .load(myAddress+user.get("pic"))
                    .into(profileImage);
            username.setText(user.get("email"));
            name.setText(user.get("name"));
            Log.d("picaso",user.get("pic"));
           more.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   // Assume we have a button in our Layout as follows
                   //Button anchor = (Button) rootView.findViewById(R.id.button1);

                   DroppyMenuPopup.Builder droppyBuilder = new DroppyMenuPopup.Builder(getActivity(),more);

// Add normal items (text only)
                   droppyBuilder.addMenuItem(new DroppyMenuItem("درباره ما"))
                           .addMenuItem(new DroppyMenuItem("خروج از حساب"))
                           .addSeparator();

// Add Item with icon
                 //  droppyBuilder.addMenuItem(new DroppyMenuItem("test3", R.drawable.add));



// Set Callback handler
                   droppyBuilder.setOnClick(new DroppyClickCallbackInterface() {
                       @Override
                       public void call(View v, int id) {
                           Log.d("Clicked on ", String.valueOf(id));
                           switch (id){
                               case 1:
                                   sessionManager.setLogin(false);
                                   db.deleteUsers();
                                   registerUser();
                                   break;
                           }
                       }
                   });

                   DroppyMenuPopup droppyMenu = droppyBuilder.build();

// Then once you click on the button it'll show
// Alternatively you can call droppyMenu.show();
               }
           });
        }else {
           registerUser();
       }


        /*Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);*/
        initViews();
        try {
            loadJSON();
            //Toast.makeText(getContext(), "done", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Force Close", Toast.LENGTH_SHORT).show();
            System.exit(0);

        }
        //setHasOptionsMenu(true);



        return rootView;
    }

    private void registerUser() {
        registerBTN.setVisibility(View.VISIBLE);
        username.setVisibility(View.GONE);
        name.setVisibility(View.GONE);
        registerBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), Register.class);
                startActivity(i);
            }
        });
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
                mAdapter = new DataAdapter4Book(mArrayList,getActivity(), HomeFragment.this, HomeFragment.this);
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
