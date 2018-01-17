package com.duccipopi.baking.activities;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.duccipopi.baking.R;
import com.duccipopi.baking.dao.Step;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.dash.DashChunkSource;
import com.google.android.exoplayer2.source.dash.DashMediaSource;
import com.google.android.exoplayer2.source.dash.DefaultDashChunkSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;

/**
 * A fragment representing a single Recipe detail screen.
 * This fragment is either contained in a {@link StepsListActivity}
 * in two-pane mode (on tablets) or a {@link StepActivity}
 * on handsets.
 */
public class StepFragment extends Fragment {

    private static final String SIS_PLAYER_POSITION = "current_position";
    private static final String SIS_PLAYER_WINDOW = "current_window";

    private Step mStep;
    private SimpleExoPlayer mPlayer;
    private SimpleExoPlayerView mPlayerView;
    private long mPlaybackPosition;
    private int mCurrentWindow;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public StepFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ActivityContract.ARG_ITEM)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            mStep = getArguments().getParcelable(ActivityContract.ARG_ITEM);

            /*Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(mStep.getShortDescription());
            }*/
        }

        if (savedInstanceState != null) {

            if (savedInstanceState.containsKey(SIS_PLAYER_POSITION)) {
                mPlaybackPosition = savedInstanceState.getLong(SIS_PLAYER_POSITION);
            }

            if (savedInstanceState.containsKey(SIS_PLAYER_WINDOW)) {
                mCurrentWindow = savedInstanceState.getInt(SIS_PLAYER_WINDOW);
            }
        }

    }

    private void initializePlayer() {
        if (mPlayer == null && mPlayerView != null) {
            // Create the player
            mPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), new DefaultTrackSelector());

            mPlayer.seekTo(mCurrentWindow, mPlaybackPosition);
            mPlayer.setPlayWhenReady(mPlaybackPosition > 0);

            mPlayer.prepare(buildMediaSource(Uri.parse(mStep.getVideoURL())), mPlaybackPosition > 0, false);

            mPlayerView.setPlayer(mPlayer);

        }
    }

    private void releasePlayer() {
        if (mPlayer != null) {
            mPlaybackPosition = mPlayer.getCurrentPosition();
            mCurrentWindow = mPlayer.getCurrentWindowIndex();
            mPlayer.release();
            mPlayer = null;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.step_detail, container, false);


        if (mStep != null) {
            // Show step description
            ((TextView) rootView.findViewById(R.id.step_detail)).setText(mStep.getDescription());

            //Prepare player
            SimpleExoPlayerView playerView = rootView.findViewById(R.id.player);
            ImageView imageView = rootView.findViewById(R.id.thumbnail);
            if (!mStep.getVideoURL().isEmpty()) {
                mPlayerView = playerView;
                playerView.setVisibility(View.VISIBLE);
                initializePlayer();
            }
            if (!TextUtils.isEmpty(mStep.getThumbnailURL())) {
                imageView.setVisibility(View.VISIBLE);
                Picasso.with(getContext())
                        .load(Uri.parse(mStep.getThumbnailURL()))
                        .into(imageView);
            }

            if (playerView.getVisibility() == View.VISIBLE
                    || imageView.getVisibility() == View.VISIBLE) {
                rootView.findViewById(R.id.media_frame).setVisibility(View.VISIBLE);
            }
        }

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (mPlayer != null) {
            outState.putLong(SIS_PLAYER_POSITION, mPlayer.getCurrentPosition());
            outState.putInt(SIS_PLAYER_WINDOW, mPlayer.getCurrentWindowIndex());
        }
        super.onSaveInstanceState(outState);
    }

    private MediaSource buildMediaSource(Uri uri) {
        String ua = Util.getUserAgent(getContext(), getContext().getApplicationInfo().name);

        return new ExtractorMediaSource.Factory(
                new DefaultHttpDataSourceFactory(ua)).
                createMediaSource(uri);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            initializePlayer();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Util.SDK_INT <= 23 || mPlayer == null) {
            initializePlayer();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releasePlayer();
    }

}
