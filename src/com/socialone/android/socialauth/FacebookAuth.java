package com.socialone.android.socialauth;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;

import com.socialone.android.utils.Datastore;

import org.brickred.socialauth.android.DialogListener;
import org.brickred.socialauth.android.SocialAuthAdapter;
import org.brickred.socialauth.android.SocialAuthError;

import roboguice.RoboGuice;

//import org.brickred.socialauth.exception.UserDeniedPermissionException;

/**
 * Created by david.hodge on 11/4/13.
 */
public class FacebookAuth extends LoginAdapter {
    SocialAuthAdapter mAuthAdapter;
    Datastore mDatastore;

    public FacebookAuth(final Context context) {
        super(context);
        mDatastore = RoboGuice.getInjector(context).getInstance(Datastore.class);
        mAuthAdapter = new SocialAuthAdapter(new DialogListener() {
            @Override
            public void onComplete(final Bundle bundle) {
//                mDatastore.setTwitterAccessKey(
//                        mAuthAdapter.getCurrentProvider().getAccessGrant().getKey());
//                mDatastore.setTwitterAccessSecret(
//                        mAuthAdapter.getCurrentProvider().getAccessGrant().getSecret());
                if (mListener != null){
                    Handler uiHandler = new Handler(context.getMainLooper());
                    uiHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mListener.onComplete(bundle);
                        }
                    });
                }
            }

            @Override
            public void onError(final SocialAuthError socialAuthError) {
                if (mListener != null) {
//                    Handler uiHandler = new Handler(context.getMainLooper());
//                    uiHandler.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            if (socialAuthError.getInnerException() instanceof UserDeniedPermissionException) {
//                                mListener.onCancel();
//                            } else {
//                                mListener.onError(socialAuthError);
//                            }
//                        }
//                    });
                }
            }

            @Override
            public void onCancel() {
                if (mListener != null) {
                    Handler uiHandler = new Handler(context.getMainLooper());
                    uiHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mListener.onCancel();
                        }
                    });
                }
            }

            @Override
            public void onBack() {
                if (mListener != null) {
                    Handler uiHandler = new Handler(context.getMainLooper());
                    uiHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mListener.onBack();
                        }
                    });
                }
            }
        });
        mAuthAdapter.addCallBack(SocialAuthAdapter.Provider.FACEBOOK, "http://graph.facebook.com");
    }

    @Override
    public void authorize() {
        mAuthAdapter.authorize(mContext, SocialAuthAdapter.Provider.FACEBOOK);
    }

    @Override
    public void signOut() {
        mAuthAdapter.signOut(mContext, SocialAuthAdapter.Provider.FACEBOOK.toString());
//        mDatastore.clearTwitterToken();
    }

    @Override
    public boolean isSignedIn() {
        return mDatastore.getUserLoggedInToTwitter();
    }
}