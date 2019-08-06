package loop.tvapp;

import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import es.dmoral.toasty.Toasty;
import loop.tvapp.deleter.IAsyncWorkCompletedCallback;
import loop.tvapp.deleter.ServiceCaller;
import loop.tvapp.model.ContentData;
import loop.tvapp.xvideoplayer.MxTvPlayerWidget;


public class MainActivity extends AppCompatActivity {
    //    MxTvPlayerWidget mNiceVideoPlayer;
    String code;
    List<ContentData> contentDataList;
    ProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);
        code = sharedPreferences.getString("id", null);
//        code = "BL002";
        init();
        setVideoApi();
    }

    private void setVideoApi() {
        progress.setVisibility(View.VISIBLE);
        contentDataList = new ArrayList<>();
        contentDataList.clear();
        ServiceCaller serviceCaller = new ServiceCaller(this);
        serviceCaller.callVideoPlayer(code, new IAsyncWorkCompletedCallback() {
            @Override
            public void onDone(String workName, boolean isComplete) {
                if (isComplete) {
                    ContentData[] contentData = new Gson().fromJson(workName, ContentData[].class);
                    if (contentData != null) {
                        contentDataList.addAll(Arrays.asList(contentData));
                        if (contentDataList != null && contentDataList.size() != 0) {

                            setVideo();
                        } else {
                            Toasty.info(MainActivity.this, "No Data Found").show();
                        }
                    }
                } else {
                    Toasty.error(MainActivity.this, "Something went wrong").show();
                }
                progress.setVisibility(View.GONE);
            }
        });
    }

    private void init() {
        progress = findViewById(R.id.progress);

    }

    int count = 0;
    SimpleExoPlayer player;

    private void setVideo() {
        if (count == contentDataList.size()) {
            count = 0;
            setVideo();
        } else {
            String vv = contentDataList.get(count).getVideo();
            if (vv != null && !vv.equalsIgnoreCase("") && !vv.equalsIgnoreCase("null")) {
                BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
                TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
                TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);
                player = ExoPlayerFactory.newSimpleInstance(MainActivity.this, trackSelector);
                SimpleExoPlayerView simpleExoPlayerView = new SimpleExoPlayerView(MainActivity.this);
                simpleExoPlayerView.setPlayer(player);
                DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(MainActivity.this, Util.getUserAgent(MainActivity.this, "lk"));
                ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
                MediaSource videoSource = new ExtractorMediaSource(Uri.parse("http://dnexus.veteransoftwares.com" + contentDataList.get(count).getVideo()), dataSourceFactory, extractorsFactory, null, null);

//            mNiceVideoPlayer = new MxTvPlayerWidget(MainActivity.this);
                LinearLayout linearLayout = new LinearLayout(this);
                linearLayout.setOrientation(LinearLayout.VERTICAL);

                linearLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT));

                simpleExoPlayerView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT));
                linearLayout.addView(simpleExoPlayerView);
                setContentView(linearLayout);
                player.prepare(videoSource);
                player.setPlayWhenReady(true);
//            player.autoStartPlay("http://dnexus.veteransoftwares.com" + contentDataList.get(count).getVideo(), "Dneux");
                MediaPlayer mp = MediaPlayer.create(this, Uri.parse("http://dnexus.veteransoftwares.com" + contentDataList.get(count).getVideo()));
                if (mp != null) {
                    int a = mp.getDuration();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
//                            player.clearVideoSurface();
                            player.release();
                            count++;
                            setVideo();
                        }
                    }, a);
                }
            } else {
                count++;
                setVideo();

            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (player != null) {
            player.release();
            player = null;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (player != null) {
            player.release();
        }
    }

}
