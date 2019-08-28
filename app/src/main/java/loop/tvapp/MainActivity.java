package loop.tvapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;

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
    int count = 0;
    SimpleExoPlayer player;
    SimpleExoPlayerView simpleExoPlayerView;
    DataSource.Factory dataSourceFactory;
    ExtractorsFactory extractorsFactory;

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
                    if (!workName.trim().equalsIgnoreCase("\"no\"")) {
                        ContentData[] contentData = new Gson().fromJson(workName, ContentData[].class);
                        if (contentData != null) {
                            contentDataList.addAll(Arrays.asList(contentData));
                            if (contentDataList != null && contentDataList.size() != 0) {
                                setVideo();
                            } else {
                                Toasty.info(MainActivity.this, "No Data Found").show();
                            }
                        }
                    }
                } else {
                    Toasty.error(MainActivity.this, "Something went wrong").show();
                }
                progress.setVisibility(View.GONE);
            }
        });
    }

    TextView option;

    private void init() {
        progress = findViewById(R.id.progress);
        option = findViewById(R.id.option);
        option.setOnClickListener(v -> {
            showPopup();
        });
    }

    private void showPopup() {
        PopupMenu popup = new PopupMenu(MainActivity.this, option);
        //Inflating the Popup using xml file
        popup.getMenuInflater().inflate(R.menu.menu, popup.getMenu());

        //registering popup with OnMenuItemClickListener
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.action_logout) {
                    SharedPreferences sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.clear();
                    editor.apply();
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
                return true;
            }
        });

        popup.show();//showing popup menu
    }


    private void setVideo() {
        if (count == contentDataList.size()) {
            count = 0;
            setVideo();
        } else {
            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
            TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);
            player = ExoPlayerFactory.newSimpleInstance(MainActivity.this, trackSelector);
            simpleExoPlayerView = new SimpleExoPlayerView(MainActivity.this);
            simpleExoPlayerView.setPlayer(player);
            dataSourceFactory = new DefaultDataSourceFactory(MainActivity.this, Util.getUserAgent(MainActivity.this, "lk"));
            extractorsFactory = new DefaultExtractorsFactory();
            MediaSource videoSource = new ExtractorMediaSource(Uri.parse("http://dnexus.veteransoftwares.com" + contentDataList.get(count).getVideo()), dataSourceFactory, extractorsFactory, null, null);
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
            long a = player.getDuration();
            simpleExoPlayerView.setUseController(false);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    player.release();
                    player = null;
                    count++;
                    setVideo();
                }
            }, a);
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
