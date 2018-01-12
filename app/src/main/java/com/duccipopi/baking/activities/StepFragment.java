package com.duccipopi.baking.activities;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.duccipopi.baking.R;
import com.duccipopi.baking.dao.Step;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.dash.DashChunkSource;
import com.google.android.exoplayer2.source.dash.DashMediaSource;
import com.google.android.exoplayer2.source.dash.DefaultDashChunkSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;

/**
 * A fragment representing a single Recipe detail screen.
 * This fragment is either contained in a {@link StepsListActivity}
 * in two-pane mode (on tablets) or a {@link StepActivity}
 * on handsets.
 */
public class StepFragment extends Fragment {

    private Step mStep;
    private SimpleExoPlayer mPlayer;

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

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(mStep.getShortDescription());
            }
        }

        // Create the player
        mPlayer = ExoPlayerFactory.newSimpleInstance(
                        new DefaultRenderersFactory(getContext()),
                        new DefaultTrackSelector(), new DefaultLoadControl());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.step_detail, container, false);


        if (mStep != null) {
            // Show step description
            ((TextView) rootView.findViewById(R.id.step_detail)).setText(mStep.getDescription());

            //Prepare player
            ((SimpleExoPlayerView) rootView.findViewById(R.id.player)).setPlayer(mPlayer);
            mPlayer.prepare(buildMediaSource(Uri.parse(mStep.getVideoURL())), true, false);

        }

        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mPlayer.release();
    }

    private MediaSource buildMediaSource(Uri uri) {
        String ua = "ua"; //Util.getUserAgent(getContext(), getContext().getApplicationInfo().name);

        DataSource.Factory manifestDataSourceFactory = new DefaultHttpDataSourceFactory(ua);
        DashChunkSource.Factory dashChunkSourceFactory = new DefaultDashChunkSource.Factory(
                new DefaultHttpDataSourceFactory(ua, new DefaultBandwidthMeter()));
        return new DashMediaSource.Factory(dashChunkSourceFactory, manifestDataSourceFactory)
                .createMediaSource(uri);
    }
}
