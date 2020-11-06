package com.example.ketabeman21.Fragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.ketabeman21.Adapter.DataAdapterExplore;
import com.example.ketabeman21.R;
import com.flaviofaria.kenburnsview.KenBurnsView;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;
import com.paulrybitskyi.persistentsearchview.PersistentSearchView;
import com.paulrybitskyi.persistentsearchview.listeners.OnSearchConfirmedListener;
import com.paulrybitskyi.persistentsearchview.utils.VoiceRecognitionDelegate;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import devlight.io.library.ntb.NavigationTabBar;

public class ExploreFragment extends Fragment {
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private AppBarLayout appBarLayout;
    private ViewPager mViewPager;
    private RecyclerView uRecyclerView;
    private PersistentSearchView persistentSearchView;
    View rootView;


    public static ExploreFragment newInstance() {

        return new ExploreFragment();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_explore, container, false);
        /*setRetainInstance(true);
        getActivity().invalidateOptionsMenu();*/

        /*uRecyclerView = (RecyclerView)rootView.findViewById(R.id.userBox);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        GridLayoutManager manager1 = new GridLayoutManager(getContext(), 1, GridLayoutManager.HORIZONTAL, false);
        uRecyclerView.setLayoutManager(manager1);
*/
        //((Start)getActivity()).closeMenu();
        collapsingToolbarLayout = (CollapsingToolbarLayout) rootView.findViewById(R.id.toolbar);
        appBarLayout = (AppBarLayout) rootView.findViewById(R.id.appbar);

        setViewPager();
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = true;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    // collapsingToolbarLayout.setTitle(name);
                    //nameTextview.setText(" ");

                    isShow = true;
                } else if(isShow) {
                    collapsingToolbarLayout.setTitle(" ");


                    isShow = false;
                }
            }
        });

       // loadJSONUsers();
        //setHasOptionsMenu(false);










        persistentSearchView = rootView.findViewById(R.id.persistentSearchView);
        persistentSearchView.setOnLeftBtnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // Handle the left button click
            }

        });

        persistentSearchView.setOnClearInputBtnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // Handle the clear input button click
            }

        });

        // Setting a delegate for the voice recognition input
        persistentSearchView.setVoiceRecognitionDelegate(new VoiceRecognitionDelegate(this));

        persistentSearchView.setOnSearchConfirmedListener(new OnSearchConfirmedListener() {

            @Override
            public void onSearchConfirmed(PersistentSearchView searchView, String query) {
                // Handle a search confirmation. This is the place where you'd
                // want to perform a search against your data provider.
                Toast.makeText(getContext(), query, Toast.LENGTH_LONG).show();
            }

        });
        persistentSearchView.setOnLeftBtnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.exit(0);
            }
        });
        // Disabling the suggestions since they are unused in
        // the simple implementation
        persistentSearchView.setSuggestionsDisabled(false);





        return rootView;

    }
   /* private ArrayList<User> uArrayList;
    private DataAdapter4User uAdapter;*/


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Calling the voice recognition delegate to properly handle voice input results
        VoiceRecognitionDelegate.handleResult(persistentSearchView, requestCode, resultCode, data);
    }
    private void setViewPager() {

        mViewPager = (ViewPager) rootView.findViewById(R.id.myProfilePage);
        /*FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
                getActivity().getSupportFragmentManager(), FragmentPagerItems.with(getContext())
                //.add(R.string.mainPage, HomeFragment.class)
                .add("کتاب ها", BookFragment.class)
                .add("موضوعات", CategoryFragment.class)
                .create());

        mViewPager.setAdapter(adapter);*/

        List<Fragment> fragments = new Vector<Fragment>();
        fragments.add(Fragment.instantiate(getContext(), BookFragment.class.getName()));
        fragments.add(Fragment.instantiate(getContext(), CategoryFragment.class.getName()));
        DataAdapterExplore adapter = new DataAdapterExplore(this.getChildFragmentManager(), fragments);
        mViewPager.setAdapter(adapter);

        //  mViewPager.setCurrentItem(0);


        final String[] colors = getResources().getStringArray(R.array.default_preview);
        final NavigationTabBar navigationTabBar = (NavigationTabBar) rootView.findViewById(R.id.ntb);
        final ArrayList<NavigationTabBar.Model> models = new ArrayList<>();
        /*models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.home),
                        Color.parseColor(colors[0])
                ).title("صفحه اصلی")
                        .badgeTitle("NTB")
                        .build()
        );*/
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_collapse_small_holo_light),
                        Color.parseColor(colors[2])
                ).title("کتاب ها")
                        .badgeTitle("state")
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.comments),
                        Color.parseColor(colors[1])
                ).title("موضوعات")
                        .badgeTitle("with")
                        .build()
        );



        navigationTabBar.setModels(models);
        navigationTabBar.setViewPager(mViewPager, 1);

        navigationTabBar.setTitleMode(NavigationTabBar.TitleMode.ACTIVE);
        navigationTabBar.setBadgeGravity(NavigationTabBar.BadgeGravity.BOTTOM);
        navigationTabBar.setBadgePosition(NavigationTabBar.BadgePosition.CENTER);
        navigationTabBar.setTypeface("fonts/custom_font.ttf");
        navigationTabBar.setIsBadged(true);
        navigationTabBar.setIsTitled(true);
        navigationTabBar.setIsTinted(true);
        navigationTabBar.setIsBadgeUseTypeface(true);
        navigationTabBar.setBadgeBgColor(Color.RED);
        navigationTabBar.setBadgeTitleColor(Color.WHITE);
        navigationTabBar.setIsSwiped(true);
        navigationTabBar.setBgColor(Color.DKGRAY);
        navigationTabBar.setBadgeSize(10);
        navigationTabBar.setTitleSize(25);
        navigationTabBar.setIconSizeFraction((float) 0.5);
    }
}
