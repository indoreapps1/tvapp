package loop.tvapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.gson.Gson;
import com.xiao.nicevideoplayer.NiceVideoPlayer;
import com.xiao.nicevideoplayer.NiceVideoPlayerManager;
import com.xiao.nicevideoplayer.TxVideoPlayerController;

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


public class MainActivity extends AppCompatActivity {
    NiceVideoPlayer mNiceVideoPlayer;
    String code;
    List<ContentData> contentDataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Bundle bundle = getIntent().getExtras();
        code = bundle.getString("code");
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
        mNiceVideoPlayer = findViewById(R.id.nice_video_player);

    }

    int count = 0;

    private void setVideo() {
//        for (count = 0; contentDataList.size() > count; count++) {
        mNiceVideoPlayer.setPlayerType(NiceVideoPlayer.TYPE_IJK); // or NiceVideoPlayer.TYPE_NATIVE
//            mNiceVideoPlayer.setUp("http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4", null);
        if (count == contentDataList.size()) {
            count = 0;
            setVideo();
        } else {
            if (contentDataList.get(count).getVideo() != null) {
                mNiceVideoPlayer.setUp("http://dnexus.veteransoftwares.com" + contentDataList.get(count).getVideo(), null);
                long a = mNiceVideoPlayer.getDuration();
                mNiceVideoPlayer.start();
//                Toast.makeText(this, "" + a, Toast.LENGTH_SHORT).show();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        setVideo();
                        count++;
                    }
                }, a);
                mNiceVideoPlayer.releasePlayer();
            } else {
                count++;
                setVideo();
                Toasty.error(MainActivity.this, "Invalid Video Url").show();
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        NiceVideoPlayerManager.instance().releaseNiceVideoPlayer();
    }

    @Override
    public void onBackPressed() {
        if (NiceVideoPlayerManager.instance().onBackPressd()) return;
        super.onBackPressed();
    }

}
