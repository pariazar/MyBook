package com.example.ketabeman21.Fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.ketabeman21.Adapter.ViewPagerAdapter;
import com.example.ketabeman21.R;
import com.flaviofaria.kenburnsview.KenBurnsView;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

import java.util.ArrayList;

import devlight.io.library.ntb.NavigationTabBar;

public class ExploreFragment extends Fragment {
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private AppBarLayout appBarLayout;
    private KenBurnsView kenBack;
    private ViewPagerAdapter mViewPagerAdapter;
    private ViewPager mViewPager;
    private RecyclerView uRecyclerView;
    View rootView;


    public static ExploreFragment newInstance() {

        return new ExploreFragment();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_explore, container, false);
        //setRetainInstance(true);
        getActivity().invalidateOptionsMenu();

        /*uRecyclerView = (RecyclerView)rootView.findViewById(R.id.userBox);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        GridLayoutManager manager1 = new GridLayoutManager(getContext(), 1, GridLayoutManager.HORIZONTAL, false);
        uRecyclerView.setLayoutManager(manager1);
*/
        //((Start)getActivity()).closeMenu();
        kenBack = rootView.findViewById(R.id.backf);
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
                    kenBack.setVisibility(View.GONE);

                    isShow = true;
                } else if(isShow) {
                    collapsingToolbarLayout.setTitle(" ");
                    kenBack.setVisibility(View.VISIBLE);


                    isShow = false;
                }
            }
        });

       // loadJSONUsers();
        setHasOptionsMenu(false);

        return rootView;

    }
   /* private ArrayList<User> uArrayList;
    private DataAdapter4User uAdapter;*/



    private void setViewPager() {

        mViewPager = (ViewPager) rootView.findViewById(R.id.myProfilePage);
        mViewPagerAdapter = new ViewPagerAdapter((getActivity()).getSupportFragmentManager());
        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
                getActivity().getSupportFragmentManager(), FragmentPagerItems.with(getContext())
                //.add(R.string.mainPage, HomeFragment.class)
                .add("کد ها", BookFragment.class)
                .add("کتابها", OfflineFragment.class)
                .add("ویدیو آموزشی",BookFragment.class)

                .create());
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
                ).title("کد ها")
                        .badgeTitle("state")
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.comments),
                        Color.parseColor(colors[1])
                ).title("کتابها")
                        .badgeTitle("with")
                        .build()
        );

        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.magni),
                        Color.parseColor(colors[3])
                ).title("ویدیو آموزشی")
                        .badgeTitle("icon")
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
