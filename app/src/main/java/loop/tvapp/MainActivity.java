package loop.tvapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.gson.Gson;
import com.khizar1556.mkvideoplayer.MKPlayerActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import es.dmoral.toasty.Toasty;
import loop.tvapp.adapter.SlidingImage_Adapter;
import loop.tvapp.framework.IAsyncWorkCompletedCallback;
import loop.tvapp.framework.ServiceCaller;
import loop.tvapp.model.ContentData;
import loop.tvapp.viewpagerindicator.CirclePageIndicator;
import loop.tvapp.xvideoplayer.MxTvPlayerWidget;
import loop.tvapp.xvideoplayer.MxVideoPlayer;
import loop.tvapp.xvideoplayer.MxVideoPlayerWidget;


public class MainActivity extends AppCompatActivity {
    MxTvPlayerWidget mNiceVideoPlayer;
    String code;
    List<ContentData> contentDataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);
        code= sharedPreferences.getString("id", null);
        init();
        setVideoApi();
    }

    private void setVideoApi() {
        contentDataList = new ArrayList<>();
        contentDataList.clear();
        ServiceCaller serviceCaller = new ServiceCaller(this);
        serviceCaller.callVideoPlayer(Integer.parseInt(code), new IAsyncWorkCompletedCallback() {
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
            }
        });
    }

    private void init() {
//        mNiceVideoPlayer = findViewById(R.id.nice_video_player);

    }

    int count = 0;

    private void setVideo() {
        if (count == contentDataList.size()) {
            count = 0;
            setVideo();
        } else {
//            Toast.makeText(this, count+""+contentDataList.get(count).getVideo(), Toast.LENGTH_SHORT).show();
            mNiceVideoPlayer = new MxTvPlayerWidget(MainActivity.this);
            LinearLayout linearLayout = new LinearLayout(this);
            linearLayout.setOrientation(LinearLayout.VERTICAL);

            linearLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT));

            mNiceVideoPlayer.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT));
            linearLayout.addView(mNiceVideoPlayer);
            setContentView(linearLayout);
            mNiceVideoPlayer.autoStartPlay("http://dnexus.veteransoftwares.com" + contentDataList.get(count).getVideo(), "Dneux");
            MediaPlayer mp = MediaPlayer.create(this, Uri.parse("http://dnexus.veteransoftwares.com" + contentDataList.get(count).getVideo()));
            int a = mp.getDuration();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mNiceVideoPlayer.clearFocus();
                    mNiceVideoPlayer.release();
                    count++;
                    setVideo();
                }
            }, a);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mNiceVideoPlayer != null) {
            mNiceVideoPlayer.release();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (mNiceVideoPlayer != null) {
            mNiceVideoPlayer.release();
        }
    }

}
