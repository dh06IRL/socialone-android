package com.socialone.android.fragment.twitter;

import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.haarman.listviewanimations.swinginadapters.prepared.SwingBottomInAnimationAdapter;
import com.socialone.android.R;
import com.socialone.android.utils.BlurTransformation;
import com.socialone.android.utils.Constants;
import com.squareup.picasso.Picasso;

import org.brickred.socialauth.android.DialogListener;
import org.brickred.socialauth.android.SocialAuthAdapter;
import org.brickred.socialauth.android.SocialAuthError;

import java.util.List;

import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.conf.ConfigurationBuilder;

/**
 * Created by david.hodge on 1/13/14.
 */
public class TwitterFollowingFragment extends SherlockFragment {


    View view;
    ListView listView;
    GoogleCardsAdapter googleCardsAdapter;
    SocialAuthAdapter mAuthAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        view = inflater.inflate(R.layout.social_checkin_list, container, false);
        listView = (ListView) view.findViewById(R.id.activity_googlecards_listview);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        twitterSetup();
    }

    private void setUpTwit4j(){
        try{
            ConfigurationBuilder cb = new ConfigurationBuilder();
            cb.setDebugEnabled(true)
                    .setOAuthConsumerKey(Constants.TWIT_CONSUMER_KEY)
                    .setOAuthConsumerSecret(Constants.TWIT_CONSUMER_SECRET)
                    .setOAuthAccessToken(mAuthAdapter.getCurrentProvider().getAccessGrant().getKey())
                    .setOAuthAccessTokenSecret(mAuthAdapter.getCurrentProvider().getAccessGrant().getSecret());
            TwitterFactory tf = new TwitterFactory(cb.build());
            Twitter twitter = tf.getInstance();
            long cursor = -1;
            List<User> statuses = twitter.getFriendsList(twitter.getId(),cursor);
            googleCardsAdapter = new GoogleCardsAdapter(getSherlockActivity(), statuses);
            SwingBottomInAnimationAdapter swingBottomInAnimationAdapter = new SwingBottomInAnimationAdapter(googleCardsAdapter);
            swingBottomInAnimationAdapter.setInitialDelayMillis(300);
            swingBottomInAnimationAdapter.setAbsListView(listView);
            listView.setAdapter(swingBottomInAnimationAdapter);
            googleCardsAdapter.setData(statuses);
            Log.d("twitter", "twitter4j " + statuses.toString());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void twitterSetup(){
        mAuthAdapter = new SocialAuthAdapter(new DialogListener() {
            @Override
            public void onComplete(Bundle bundle) {
                setUpTwit4j();
            }

            @Override
            public void onError(SocialAuthError socialAuthError) {
                Log.e("twitter", "auth adapter " + socialAuthError.getMessage());
            }

            @Override
            public void onCancel() {
                //stub
            }

            @Override
            public void onBack() {
                //stub
            }
        });
        mAuthAdapter.addCallBack(SocialAuthAdapter.Provider.TWITTER, Constants.TWITTER_CALLBACK);
        mAuthAdapter.authorize(getSherlockActivity(), SocialAuthAdapter.Provider.TWITTER);
    }

    public class GoogleCardsAdapter extends BaseAdapter {

        private Context mContext;
        private List<User> mFeed;
        private boolean mShouldReturnEmpty = true;

        public GoogleCardsAdapter(Context context, List<User> feed) {
            mContext = context;
            mFeed = feed;
        }

        public void setData(List<User> feed){
            mFeed = feed;
        }

        @Override
        public boolean isEmpty() {
            return mShouldReturnEmpty && super.isEmpty();
        }

        @Override
        public int getCount() {
            return mFeed.size();
        }

        @Override
        public User getItem(int position) {
            return mFeed.get(position);
        }

        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            View view = convertView;
            if (view == null) {
                view = LayoutInflater.from(mContext).inflate(R.layout.friends_list_item, parent, false);

                viewHolder = new ViewHolder();
                viewHolder.userBackground = (ImageView) view.findViewById(R.id.user_background_image);
                viewHolder.userProfile = (ImageView) view.findViewById(R.id.user_profile_image);
                viewHolder.userRealName = (TextView) view.findViewById(R.id.user_real_name);
                viewHolder.userUserName = (TextView) view.findViewById(R.id.user_user_name);

                view.setTag(viewHolder);

            } else {
                viewHolder = (ViewHolder) view.getTag();
            }

            final User post = getItem(position);

            Picasso.with(mContext)
                    .load(post.getProfileImageURL())
                    .resize(100, 100)
                    .centerCrop()
                    .into(viewHolder.userProfile);

            Picasso.with(mContext)
                    .load(post.getProfileBannerURL())
                    .resize(750, 750)
                    .centerCrop()
                    .transform(new BlurTransformation(mContext))
                    .into(viewHolder.userBackground);

            viewHolder.userUserName.setText("@" + post.getScreenName());
            viewHolder.userRealName.setText(post.getName());

            return view;
        }

        public class ViewHolder {
            ImageView userBackground;
            ImageView userProfile;
            TextView userRealName;
            TextView userUserName;
        }

        public String stripHtml(String html) {
            return Html.fromHtml(html).toString();
        }
    }
}
