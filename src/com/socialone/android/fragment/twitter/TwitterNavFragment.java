package com.socialone.android.fragment.twitter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.socialone.android.R;
import com.viewpagerindicator.TitlePageIndicator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by david.hodge on 1/11/14.
 */
public class TwitterNavFragment extends SherlockFragment {

    View view;
    ViewPager viewPager;
    TitlePageIndicator titlePageIndicator;
    private ArrayList<Fragment> mFragments;
    private ArrayList<String> mtitles;
    FragmentManager fm;
    TwitterMyTweetsFragment twitterMyTweetsFragment = new TwitterMyTweetsFragment();
    TwitterMainFeedFragment twitterMainFeedFragment = new TwitterMainFeedFragment();
    TwitterMentionsFragment twitterMentionsFragment = new TwitterMentionsFragment();
    TwitterRetweetsFragment twitterRetweetsFragment = new TwitterRetweetsFragment();
    TwitterFavoritesFragment twitterFavoritesFragment = new TwitterFavoritesFragment();
    TwitterNearbyTrendsFragment twitterNearbyTrendsFragment = new TwitterNearbyTrendsFragment();
    TwitterFeedStreamFragment twitterFeedStreamFragment = new TwitterFeedStreamFragment();
    TwitterProfileFragment twitterProfileFragment = new TwitterProfileFragment();
    PagerAdapter pagerAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        view = inflater.inflate(R.layout.share_fragment, container, false);

        viewPager = (ViewPager) view.findViewById(R.id.social_view_pager);
        titlePageIndicator = (TitlePageIndicator) view.findViewById(R.id.social_tpi);

        mtitles = new ArrayList<String>();
        mtitles.add("Profile");
        mtitles.add("My Tweets");
        mtitles.add("Feed");
        mtitles.add("Mentions");
        mtitles.add("Retweets");
        mtitles.add("Favorites");
        mtitles.add("Nearby");
        mtitles.add("Stream");

        mFragments =  new ArrayList<Fragment>();
        mFragments.add(twitterProfileFragment);
        mFragments.add(twitterMyTweetsFragment);
        mFragments.add(twitterMainFeedFragment);
        mFragments.add(twitterMentionsFragment);
        mFragments.add(twitterRetweetsFragment);
        mFragments.add(twitterFavoritesFragment);
        mFragments.add(twitterNearbyTrendsFragment);
        mFragments.add(twitterFeedStreamFragment);

        pagerAdapter = new PagerAdapter(getSherlockActivity(), mtitles, mFragments);

        viewPager.setAdapter(pagerAdapter);
        viewPager.setOffscreenPageLimit(8);
        viewPager.setCurrentItem(2);
        titlePageIndicator.setViewPager(viewPager);
        titlePageIndicator.setOnPageChangeListener(socialOPCL);
        titlePageIndicator.setOnCenterItemClickListener(new TitlePageIndicator.OnCenterItemClickListener() {
            @Override
            public void onCenterItemClick(int position) {
                Toast.makeText(getSherlockActivity(), "Center item " + Integer.toString(position), Toast.LENGTH_LONG).show();
            }
        });
        fm = getChildFragmentManager();

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        List<Fragment> fragments = getChildFragmentManager().getFragments();
        if (fragments != null) {
            for (Fragment fragment : fragments) {
                fragment.onActivityResult(requestCode, resultCode, data);
            }
        }
    }

    class PagerAdapter extends FragmentPagerAdapter {
        Context context;
        private LayoutInflater inflater;
        private ArrayList<String> titles;
        private ArrayList<Fragment> mFragments;

        public PagerAdapter(Context context, ArrayList<String> strings, ArrayList<Fragment> fragments){
            super(TwitterNavFragment.this.getChildFragmentManager());
            this.context = context;
            this.titles = strings;
            this.mFragments = fragments;
            this.inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return this.titles.size();

        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }

        @Override
        public Fragment getItem(int i) {
            return mFragments.get(i);
        }

        public void setTitles(ArrayList<String> titles) {
            this.titles = titles;
        }

        public void setFragments(ArrayList<Fragment> fragments) {
            this.mFragments = fragments;
        }
    }

    private ViewPager.OnPageChangeListener socialOPCL = new ViewPager.OnPageChangeListener(){
        @Override
        public void onPageScrolled(int i, float v, int i1) {
        }

        @Override
        public void onPageSelected(int i) {
        }

        @Override
        public void onPageScrollStateChanged(int i) {
        }
    };

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        return;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.social_share_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_share:
//                SocialShareFragment.shareAllThings();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
