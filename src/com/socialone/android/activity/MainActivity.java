package com.socialone.android.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.ActionProvider;
import android.view.ContextMenu;
import android.view.SubMenu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;
import com.amazon.device.ads.AdError;
import com.amazon.device.ads.AdLayout;
import com.amazon.device.ads.AdListener;
import com.amazon.device.ads.AdProperties;
import com.amazon.device.ads.AdRegistration;
import com.amazon.device.ads.AdTargetingOptions;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.plus.PlusClient;
import com.nineoldandroids.view.ViewHelper;
import com.socialone.android.R;
import com.socialone.android.appnet.adnlib.AppDotNetClient;
import com.socialone.android.appnet.adnlib.response.UserResponseHandler;
import com.socialone.android.condesales.EasyFoursquareAsync;
import com.socialone.android.condesales.listeners.AccessTokenRequestListener;
import com.socialone.android.condesales.listeners.UserInfoRequestListener;
import com.socialone.android.condesales.models.User;
import com.socialone.android.fragment.AboutFragment;
import com.socialone.android.fragment.NotConnectedFragment;
import com.socialone.android.fragment.OptiFeedFragment;
import com.socialone.android.fragment.SocialCheckInFragment;
import com.socialone.android.fragment.SocialConnectFragment;
import com.socialone.android.fragment.SocialFragment;
import com.socialone.android.fragment.StoreFragment;
import com.socialone.android.fragment.UserProfileFragment;
import com.socialone.android.fragment.appnet.AppNetNavFragment;
import com.socialone.android.fragment.facebook.FacebookMainFeedFragment;
import com.socialone.android.fragment.fivehund.FiveHundNavFragment;
import com.socialone.android.fragment.flickr.FlickrNavFragment;
import com.socialone.android.fragment.foursquare.FoursquareNavFragment;
import com.socialone.android.fragment.googeplus.GooglePlusFeedFragment;
import com.socialone.android.fragment.instagram.InstagramFeedFragment;
import com.socialone.android.fragment.tumblr.TumblrMainFeedFragment;
import com.socialone.android.fragment.twitter.TwitterNavFragment;
import com.socialone.android.utils.BlurTransformation;
import com.socialone.android.utils.Constants;
import com.socialone.android.utils.OldBlurTransformation;
import com.socialone.android.utils.RoundTransformation;
import com.socialone.android.utils.SystemBarTintManager;
import com.socialone.android.viewcomponents.NavDrawerItem;
import com.squareup.picasso.Picasso;

import java.util.Random;

import oak.util.FontTypefaceSpan;
import oauth.signpost.OAuth;

//import com.amazon.device.ads.AdLayout;
//import com.amazon.device.ads.AdTargetingOptions;

/**
 * Created by david.hodge on 12/18/13.
 */
public class MainActivity extends SherlockFragmentActivity implements GooglePlayServicesClient.OnConnectionFailedListener {

    DrawerLayout mDrawerLayout;
    FrameLayout mContent;
    ImageView userImage;
    ImageView userBackground;
    TextView userNameText;
    TextView userLocationText;
    ImageView mBlurImage;

    ActionBarDrawerToggle mActionBarDrawerToggle;
    FragmentManager mfragmentManager;
    NavDrawerItem currentNavigationDrawerItem;
    FragmentTransaction ft;

    BlurDrawerToggle blurDrawerToggle;

//    AdLayout adLayout;
//    AdTargetingOptions adTargetingOptions;

    String userShareExtra;

    private UiLifecycleHelper uiHelper;
    Session session;
    String userProfileImageLink;
    String userHeaderImageLink;
    String userName;
    String userLocation;

    PlusClient plusClient;
    private static final int REQUEST_CODE_RESOLVE_ERR = 9000;
    private ConnectionResult mConnectionResult;

    public static final int NAV_SHARE = R.id.nav_item_share;
    public static final int NAV_OPTI = R.id.nav_item_optifeed;
    public static final int NAV_FACEBOOK = R.id.nav_item_facebook;
    public static final int NAV_ID_TEST_2 = R.id.nav_item_twitter;
    public static final int NAV_ID_TEST_3 = R.id.nav_item_myspace;
    public static final int NAV_APP_NET = R.id.nav_item_appnet;
    public static final int NAV_TWITTER = R.id.nav_item_twitter;
    public static final int NAV_FOURSQUARE = R.id.nav_item_foursquare;
    public static final int NAV_FLICKR = R.id.nav_item_flickr;
    public static final int NAV_GPLUS = R.id.nav_item_gplus;
    public static final int NAV_INSTAGRM = R.id.nav_item_instagram;
    public static final int NAV_ABOUT = R.id.nav_item_about;
    public static final int NAV_PROFILE = R.id.drawer_user_profile;
    public static final int NAV_TUMBLR = R.id.nav_item_tumblr;
    public static final int NAV_FIVEHUN = R.id.nav_item_fivehundred;
    public static final int NAV_LINKEDIN = R.id.nav_item_linkdin;
    public static final int NAV_STORE = R.id.nav_item_store;
    public static final int NAV_SETTINGS = R.id.nav_item_settings;

    SharedPreferences prefs;
    EasyFoursquareAsync easyFoursquareAsync;
    AppDotNetClient client;
    Context mContext;

    AdLayout adView;
    NavDrawerItem fourItem;
    SystemBarTintManager systemBarTintManager;
    AdView adMob;
    AdTargetingOptions adOptions;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
        }

        systemBarTintManager = new SystemBarTintManager(this);
        systemBarTintManager.setStatusBarTintEnabled(true);
        systemBarTintManager.setStatusBarTintColor(Color.parseColor("#222222"));
        systemBarTintManager.setNavigationBarTintEnabled(true);
        systemBarTintManager.setNavigationBarTintColor(Color.parseColor("#222222"));


        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_root);
        mBlurImage = (ImageView) findViewById(R.id.blur_image);
        mContent = (FrameLayout) findViewById(R.id.fragment_container);

        userImage = (ImageView) findViewById(R.id.user_profile_image);
        userBackground = (ImageView) findViewById(R.id.user_background_image);
        userNameText = (TextView) findViewById(R.id.user_name);
        userLocationText = (TextView) findViewById(R.id.user_location);

        fourItem = (NavDrawerItem) findViewById(R.id.nav_item_foursquare);

        adMob = (AdView) findViewById(R.id.google_ad);
        adView = (AdLayout)findViewById(R.id.ad_view);
        adView.setListener(new AdListener() {
            @Override
            public void onAdLoaded(AdLayout adLayout, AdProperties adProperties) {
                adMob.setVisibility(View.GONE);
                adView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAdExpanded(AdLayout adLayout) {

            }

            @Override
            public void onAdCollapsed(AdLayout adLayout) {

            }

            @Override
            public void onAdFailedToLoad(AdLayout adLayout, AdError adError) {
                Log.e("ad", adError.getMessage());
                adMob.setVisibility(View.VISIBLE);
                adView.setVisibility(View.GONE);
                AdRequest adRequest = new AdRequest.Builder().build();
                adMob.loadAd(adRequest);
            }
        });

        try {
            AdRegistration.setAppKey("9dc03c16047340d88ed5e306f717b2ac");
        } catch (Exception e) {
            Log.e("ad", "Exception thrown: " + e.toString());
            return;
        }

        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        adOptions = new AdTargetingOptions();
        if(prefs.getBoolean("ads", false) == true){

        }else {
            adView.loadAd(adOptions);
        }

        mContext = this;
        mfragmentManager = getSupportFragmentManager();
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(getString(R.string.app_name));
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setIcon(android.R.color.transparent);

        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        if(prefs.getBoolean("foursquare", false)) {
            fourItem.setVisibility(View.VISIBLE);
        }else{
            fourItem.setVisibility(View.GONE);
        }
//        // Get the intent that started this activity
//        Intent intent = getIntent();
//        Uri data = intent.getData();
//
//        // Figure out what to do based on the intent type
//        if (intent.getType().indexOf("image/") != -1) {
//            // Handle intents with image data ...
//        } else if (intent.getType().equals("text/plain")) {
//            setContentFragment(NAV_SHARE);
//            userShareExtra = data.getUserInfo();
//        }

        uiHelper = new UiLifecycleHelper(this, new Session.StatusCallback() {
            @Override
            public void call(Session session, SessionState state, Exception exception) {
                onSessionStateChange(session, state, exception);
            }
        });
        uiHelper.onCreate(savedInstanceState);

        initDrawerLayout();
        getUserInfo();
        if (savedInstanceState == null) {
            setContentFragment(NAV_SHARE);
        }

        userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, SocialConnectFragment.class));
            }
        });
//        plusClient = new PlusClient.Builder(this, new GooglePlayServicesClient.ConnectionCallbacks() {
//            @Override
//            public void onConnected(Bundle bundle) {
//                Toast.makeText(mContext, "Welcome from G+ " + plusClient.getCurrentPerson().getName().getGivenName(), Toast.LENGTH_LONG).show();
//            }
//
//            @Override
//            public void onDisconnected() {
//                Log.e("google_place", "g+ login error");
//            }
//        }, this).setActions("http://schemas.google.com/AddActivity", "http://schemas.google.com/CheckInActivity")
//                .setScopes(Scopes.PLUS_LOGIN, Scopes.PLUS_PROFILE, Scopes.APP_STATE).build();
//
//        plusClient.connect();
    }

    private void loadRandomAd(){
        int min = 1;
        int max = 3;

        Random r = new Random();
        int i = r.nextInt(max - min + 1) + min;

        if(i == 1){
            adView.loadAd(adOptions);
        }else if(i == 2){
            adMob.setVisibility(View.VISIBLE);
            adView.setVisibility(View.GONE);
            AdRequest adRequest = new AdRequest.Builder().build();
            adMob.loadAd(adRequest);
        }else{
            adView.loadAd(adOptions);
//            adMob.setVisibility(View.VISIBLE);
//            adView.setVisibility(View.GONE);
//            AdRequest adRequest = new AdRequest.Builder().build();
//            adMob.loadAd(adRequest);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        uiHelper.onSaveInstanceState(outState);
    }

    private void initDrawerLayout() {
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        mActionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.drawable.ic_navigation_drawer,
                R.string.drawer_open, R.string.drawer_close);

        blurDrawerToggle = new BlurDrawerToggle(this,
                mDrawerLayout,
                R.drawable.ic_navigation_drawer,
                R.string.drawer_open,
                R.string.drawer_close,
                mBlurImage);
        mActionBarDrawerToggle.syncState();
        mDrawerLayout.setDrawerListener(mActionBarDrawerToggle);
        mDrawerLayout.setScrimColor(getResources().getColor(R.color.translucent_black));
        brandGlowEffect(mContext, mContext.getResources().getColor(R.color.app_main_color));

        String title = getResources().getString(R.string.app_name);
        SpannableString s = new SpannableString(title);
        s.setSpan(new FontTypefaceSpan(this, "RobotoSlab-Light.ttf"), 0, s.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        getSupportActionBar().setTitle(s);
    }

    private void getUserInfo() {

        if(prefs.getBoolean("facebook", false)) {
            session = ensureFacebookSessionFromCache(mContext);
            Request meRequest = Request.newMeRequest(session, new Request.GraphUserCallback() {
                @Override
                public void onCompleted(GraphUser user, Response response) {
                    if (user != null) {
                        userProfileImageLink = Constants.FACEBOOK_GRAPH + user.getId() + "/picture?type=large";
                        userHeaderImageLink = Constants.FACEBOOK_GRAPH + user.getId() + "/picture?type=large";
                        userName = user.getName();
                        if (user.getLocation() != null) {
                            userLocation = user.getLocation().getCity() + " " + user.getLocation().getState();
                        } else {
                            userLocation = "Location Unavail";
                        }

                        //sets user information
                        userNameText.setText(userName);
                        userLocationText.setText(userLocation);

                        //displays user's profile image
                        Picasso.with(mContext)
                                .load(userProfileImageLink)
                                .resize(200, 200)
                                .centerCrop()
                                .transform(new RoundTransformation())
                                .into(userImage);

                        //use large banner image if available
                        if (Build.VERSION.SDK_INT >= 17) {
                            Picasso.with(mContext)
                                    .load(userHeaderImageLink)
                                    .resize(400, 400)
                                    .centerCrop()
                                    .transform(new BlurTransformation(mContext))
                                    .into(userBackground);
                        } else {
                            Picasso.with(mContext)
                                    .load(userHeaderImageLink)
                                    .resize(400, 400)
                                    .centerCrop()
                                    .transform(new OldBlurTransformation())
                                    .into(userBackground);
                        }
                    }
                }
            });
            meRequest.executeAsync();

        }else if(prefs.getBoolean("foursquare", false)) {
            easyFoursquareAsync = new EasyFoursquareAsync(MainActivity.this);
            easyFoursquareAsync.requestAccess(new AccessTokenRequestListener() {
                @Override
                public void onAccessGrant(String accessToken) {
                    easyFoursquareAsync.getUserInfo(new UserInfoRequestListener() {
                        @Override
                        public void onUserInfoFetched(User user) {
                            //displays user's profile image
                            userNameText.setText(user.getFirstName() + " " + user.getLastName());
                            userLocationText.setText(user.getHomeCity());

                            Picasso.with(mContext)
                                    .load(user.getPhoto())
                                    .resize(200, 200)
                                    .centerCrop()
                                    .transform(new RoundTransformation())
                                    .into(userImage);

                            //use large banner image if available
                            if (Build.VERSION.SDK_INT >= 17) {
                                Picasso.with(mContext)
                                        .load(user.getPhoto())
                                        .resize(400, 400)
                                        .centerCrop()
                                        .transform(new BlurTransformation(mContext))
                                        .into(userBackground);
                            } else {
                                Picasso.with(mContext)
                                        .load(user.getPhoto())
                                        .resize(400, 400)
                                        .centerCrop()
                                        .transform(new OldBlurTransformation())
                                        .into(userBackground);
                            }
                        }

                        @Override
                        public void onError(String errorMsg) {

                        }
                    });
                }

                @Override
                public void onError(String errorMsg) {

                }
            });
        }else if(prefs.getBoolean("appnet", false)) {
            client = new AppDotNetClient(prefs.getString(OAuth.OAUTH_TOKEN, null));
            client.retrieveCurrentUser(new UserResponseHandler() {
                @Override
                public void onSuccess(final com.socialone.android.appnet.adnlib.data.User responseData) {
//                final User data = responseData;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            userNameText.setText(responseData.getName());
                            userLocationText.setText(responseData.getLocale());
                            android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
                            Picasso.with(mContext)
                                    .load(responseData.getAvatarImage().getUrl())
                                    .resize(200, 200)
                                    .centerCrop()
                                    .transform(new RoundTransformation())
                                    .into(userImage);

                            if(Build.VERSION.SDK_INT >= 17) {
                                Picasso.with(mContext)
                                        .load(responseData.getCoverImage().getUrl())
                                        .resize(400, 400)
                                        .centerCrop()
                                        .transform(new BlurTransformation(mContext))
                                        .into(userBackground);
                            }else{
                                Picasso.with(mContext)
                                        .load(responseData.getCoverImage().getUrl())
                                        .resize(400, 400)
                                        .centerCrop()
                                        .transform(new OldBlurTransformation())
                                        .into(userBackground);
                            }

                        }
                    });

                }

            });
        }
    }


    public void navigationDrawerItemClick(View v) {
        setSupportProgressBarIndeterminateVisibility(false);
        switch (v.getId()) {
            default:
                if (currentNavigationDrawerItem != v) {
                    mDrawerLayout.closeDrawers();
                    setContentFragment(v.getId());
                }
        }
    }

    private void setContentFragment(int fragID) {
        Fragment fragment;
        Bundle args = new Bundle();
        switch (fragID) {
            case NAV_PROFILE:
                fragment = new UserProfileFragment();
                break;
            case NAV_SHARE:
                fragment = new SocialCheckInFragment();
                break;
            case NAV_OPTI:
                fragment = new OptiFeedFragment();
                break;
            case NAV_FACEBOOK:
                fragment = new FacebookMainFeedFragment();
                break;
            case NAV_ID_TEST_3:
                fragment = new SocialFragment();
                break;
            case NAV_APP_NET:
                fragment = new AppNetNavFragment();
                break;
            case NAV_GPLUS:
                fragment = new GooglePlusFeedFragment();
                break;
            case NAV_INSTAGRM:
                fragment = new InstagramFeedFragment();
                break;
            case NAV_TWITTER:
                fragment = new TwitterNavFragment();
                break;
            case NAV_TUMBLR:
                fragment = new TumblrMainFeedFragment();
                break;
            case NAV_FOURSQUARE:
                if(prefs.getBoolean("foursquare", false)) {
                    fragment = new FoursquareNavFragment();
                }else{
                    fragment = new NotConnectedFragment();
                }
                break;
            case NAV_FLICKR:
                fragment = new FlickrNavFragment();
                break;
            case NAV_ABOUT:
                fragment = new AboutFragment();
                break;
            case NAV_FIVEHUN:
                fragment = new FiveHundNavFragment();
                break;
            case NAV_STORE:
                fragment = new StoreFragment();
                break;
            case NAV_SETTINGS:
                fragment = new SocialCheckInFragment();
                mContext.startActivity(new Intent(mContext, SocialConnectFragment.class));
//                fragment = new SettingsFragment();
                break;
            default:
                return;
        }

        if(prefs.getBoolean("ads", false) == true){

        }else {
            adView.loadAd(adOptions);
        }
        fragment.setArguments(args);
        fragment.setRetainInstance(true);
        ft = mfragmentManager.beginTransaction();
        ft.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.replace(R.id.fragment_container, fragment).addToBackStack("tag");
        ft.addToBackStack(null);
        ft.commit();
    }

    @Override
    public void onBackPressed() {
        this.finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mActionBarDrawerToggle.onOptionsItemSelected(getMenuItem(item))) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
//        mActionBarDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
//        mActionBarDrawerToggle.onConfigurationChanged(newConfig);
    }

    private android.view.MenuItem getMenuItem(final MenuItem item) {
        return new android.view.MenuItem() {
            @Override
            public int getItemId() {
                return item.getItemId();
            }

            public boolean isEnabled() {
                return true;
            }

            @Override
            public boolean collapseActionView() {
                // TODO Auto-generated method stub
                return false;
            }

            @Override
            public boolean expandActionView() {
                // TODO Auto-generated method stub
                return false;
            }

            @Override
            public ActionProvider getActionProvider() {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public View getActionView() {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public char getAlphabeticShortcut() {
                // TODO Auto-generated method stub
                return 0;
            }

            @Override
            public int getGroupId() {
                // TODO Auto-generated method stub
                return 0;
            }

            @Override
            public Drawable getIcon() {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public Intent getIntent() {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public ContextMenu.ContextMenuInfo getMenuInfo() {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public char getNumericShortcut() {
                // TODO Auto-generated method stub
                return 0;
            }

            @Override
            public int getOrder() {
                // TODO Auto-generated method stub
                return 0;
            }

            @Override
            public SubMenu getSubMenu() {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public CharSequence getTitle() {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public CharSequence getTitleCondensed() {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public boolean hasSubMenu() {
                // TODO Auto-generated method stub
                return false;
            }

            @Override
            public boolean isActionViewExpanded() {
                // TODO Auto-generated method stub
                return false;
            }

            @Override
            public boolean isCheckable() {
                // TODO Auto-generated method stub
                return false;
            }

            @Override
            public boolean isChecked() {
                // TODO Auto-generated method stub
                return false;
            }

            @Override
            public boolean isVisible() {
                // TODO Auto-generated method stub
                return false;
            }

            @Override
            public android.view.MenuItem setActionProvider(ActionProvider actionProvider) {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public android.view.MenuItem setActionView(View view) {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public android.view.MenuItem setActionView(int resId) {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public android.view.MenuItem setAlphabeticShortcut(char alphaChar) {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public android.view.MenuItem setCheckable(boolean checkable) {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public android.view.MenuItem setChecked(boolean checked) {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public android.view.MenuItem setEnabled(boolean enabled) {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public android.view.MenuItem setIcon(Drawable icon) {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public android.view.MenuItem setIcon(int iconRes) {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public android.view.MenuItem setIntent(Intent intent) {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public android.view.MenuItem setNumericShortcut(char numericChar) {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public android.view.MenuItem setOnActionExpandListener(OnActionExpandListener listener) {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public android.view.MenuItem setOnMenuItemClickListener(OnMenuItemClickListener menuItemClickListener) {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public android.view.MenuItem setShortcut(char numericChar, char alphaChar) {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public void setShowAsAction(int actionEnum) {
                // TODO Auto-generated method stub

            }

            @Override
            public android.view.MenuItem setShowAsActionFlags(int actionEnum) {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public android.view.MenuItem setTitle(CharSequence title) {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public android.view.MenuItem setTitle(int title) {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public android.view.MenuItem setTitleCondensed(CharSequence title) {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public android.view.MenuItem setVisible(boolean visible) {
                // TODO Auto-generated method stub
                return null;
            }
        };
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        uiHelper.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        uiHelper.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        uiHelper.onPause();

    }

    private void onSessionStateChange(Session session, SessionState state, Exception exception) {
        if (state.isOpened()) {
//            Toast.makeText(this, "connected", Toast.LENGTH_SHORT).show();
//            getUserInfo();
        } else if (state.isClosed()) {
            Toast.makeText(this, "error", Toast.LENGTH_SHORT).show();
        }
    }

//    @Override
//    public void onDrawerSlide(View view, float v) {
//        //To change body of implemented methods use File | Settings | File Templates.
//    }
//
//    @Override
//    public void onDrawerOpened(View view) {
//        //To change body of implemented methods use File | Settings | File Templates.
//    }
//
//    @Override
//    public void onDrawerClosed(View view) {
//        navigationDrawerItemClick(view);
//    }
//
//    @Override
//    public void onDrawerStateChanged(int i) {
//        //To change body of implemented methods use File | Settings | File Templates.
//    }

    public static Session ensureFacebookSessionFromCache(Context context) {
        Session activeSession = Session.getActiveSession();
        if (activeSession == null || !activeSession.getState().isOpened()) {
            activeSession = Session.openActiveSessionFromCache(context);
        }
        return activeSession;
    }

    public class BlurDrawerToggle extends ActionBarDrawerToggle implements DrawerLayout.DrawerListener {
        RenderScript rs;
        Activity mActivity;
        ImageView mBlurImage;

        public BlurDrawerToggle(Activity activity, DrawerLayout drawerLayout,
                                int drawerImageRes, int openDrawerContentDescRes, int closeDrawerContentDescRes, ImageView blurImage) {
            super(activity, drawerLayout, drawerImageRes, openDrawerContentDescRes, closeDrawerContentDescRes);
            mActivity = activity;
            mBlurImage = blurImage;
        }

        @Override
        public void onDrawerSlide(final View drawerView, final float slideOffset) {
            super.onDrawerSlide(drawerView, slideOffset);
            if (slideOffset > 0.0f) {
                setBlurAlpha(slideOffset);
            } else {
                clearBlurImage();
            }
        }

        @Override
        public void onDrawerClosed(View view) {
            clearBlurImage();
        }


        private void setBlurAlpha(float slideOffset) {
//            if (mBlurImage.getVisibility() != View.VISIBLE) {
            setBlurImage();
//            }
            ViewHelper.setAlpha(mBlurImage, slideOffset);
        }

        public Bitmap transform(Bitmap bitmap) {

            //TODO use renderscript v8 or dont do this on 2.3 devices
            rs = RenderScript.create(mActivity);
            // Create another bitmap that will hold the results of the filter.
            Bitmap blurredBitmap = Bitmap.createBitmap(bitmap);

            // Allocate memory for Renderscript to work with
            Allocation input = Allocation.createFromBitmap(rs, bitmap, Allocation.MipmapControl.MIPMAP_FULL, Allocation.USAGE_SCRIPT);
            Allocation output = Allocation.createTyped(rs, input.getType());

            // Load up an instance of the specific script that we want to use.
            ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
            script.setInput(input);

            // Set the blur radius
            script.setRadius(25);

            // Start the ScriptIntrinisicBlur
            script.forEach(output);

            // Copy the output to the blurred bitmap
            output.copyTo(blurredBitmap);
            bitmap.recycle();

            return blurredBitmap;
        }


        public void setBlurImage() {
            mDrawerLayout.setDrawingCacheEnabled(true);
            mDrawerLayout.buildDrawingCache();
            mBlurImage.setVisibility(View.VISIBLE);
            Bitmap downScaled = Bitmap.createBitmap(mDrawerLayout.getDrawingCache());
            downScaled = transform(downScaled);
            mBlurImage.setImageBitmap(downScaled);
        }

        public void clearBlurImage() {
            mBlurImage.setVisibility(View.GONE);
            mBlurImage.setImageBitmap(null);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == REQUEST_CODE_RESOLVE_ERR && resultCode == RESULT_OK) {
//            mConnectionResult = null;
//            plusClient.connect();
//        }
        uiHelper.onActivityResult(requestCode, resultCode, data);
        try {
            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
            fragment.onActivityResult(requestCode, resultCode, data);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            Log.e("google_place", "result has resolution");
            try {
                connectionResult.startResolutionForResult(this, REQUEST_CODE_RESOLVE_ERR);
                Log.e("google_place", "trying....");
            } catch (IntentSender.SendIntentException e) {
                plusClient.connect();
                Log.e("google_place", e.toString());
            }
        }
        mConnectionResult = connectionResult;
        Log.e("google_place", connectionResult.toString());

    }

    @Override
    public void onStart() {
        super.onStart();
        EasyTracker.getInstance().activityStart(this); // Add this method.
//        plusClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        EasyTracker.getInstance().activityStop(this); // Add this method.
//        plusClient.disconnect();
    }

    static void brandGlowEffect(Context context, int brandColor) {
        //glow
        int glowDrawableId = context.getResources().getIdentifier("overscroll_glow", "drawable", "android");
        Drawable androidGlow = context.getResources().getDrawable(glowDrawableId);
        androidGlow.setColorFilter(brandColor, PorterDuff.Mode.SRC_IN);
        //edge
        int edgeDrawableId = context.getResources().getIdentifier("overscroll_edge", "drawable", "android");
        Drawable androidEdge = context.getResources().getDrawable(edgeDrawableId);
        androidEdge.setColorFilter(brandColor, PorterDuff.Mode.SRC_IN);
    }

    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        final int bits1 = WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION;
        if (on) {
            winParams.flags |= bits;
            winParams.flags |= bits1;
        } else {
            winParams.flags &= ~bits;
            winParams.flags &= ~bits1;
        }
        win.setAttributes(winParams);
    }
}
