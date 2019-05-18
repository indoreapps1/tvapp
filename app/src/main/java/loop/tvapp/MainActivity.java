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
import android.widget.VideoView;

import com.xiao.nicevideoplayer.NiceVideoPlayer;
import com.xiao.nicevideoplayer.NiceVideoPlayerManager;
import com.xiao.nicevideoplayer.TxVideoPlayerController;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import loop.tvapp.adapter.SlidingImage_Adapter;
import loop.tvapp.viewpagerindicator.CirclePageIndicator;


public class MainActivity extends AppCompatActivity {
    NiceVideoPlayer mNiceVideoPlayer;
    CirclePageIndicator circlePageIndicator;
    private static ViewPager mPager;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    private ArrayList<String> ImagesArray;
    ArrayList<Integer> integers = new ArrayList<>();
    RelativeLayout relativeLy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        ImagesArray = new ArrayList<String>();
        circlePageIndicator = findViewById(R.id.indicator);
        mPager = findViewById(R.id.pager);
        mNiceVideoPlayer = findViewById(R.id.nice_video_player);
        relativeLy = findViewById(R.id.relativeLy);
        setVideo();
//        mNiceVideoPlayer.setVisibility(View.VISIBLE);
        long a = mNiceVideoPlayer.getDuration();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mNiceVideoPlayer.setVisibility(View.GONE);
                relativeLy.setVisibility(View.VISIBLE);
                viewPagerSetUp();
            }
        }, 60000);


    }

    private void setVideo() {
        mNiceVideoPlayer.setPlayerType(NiceVideoPlayer.TYPE_IJK); // or NiceVideoPlayer.TYPE_NATIVE
        mNiceVideoPlayer.setUp("http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4", null);
        mNiceVideoPlayer.start();
        mNiceVideoPlayer.releasePlayer();
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

    private void viewPagerSetUp() {
        ImagesArray.add("https://loopfusion.in/assets/img/LoopFusion/android/android-main.jpg");
        ImagesArray.add("https://loopfusion.in/assets/img/LoopFusion/iso/iso_main.jpg");
        ImagesArray.add("https://loopfusion.in/assets/img/LoopFusion/offline/offline-main.jpg");
        NUM_PAGES = ImagesArray.size();
        if (ImagesArray != null && ImagesArray.size() > 0) {
            mPager.setAdapter(new SlidingImage_Adapter(this, ImagesArray));
            circlePageIndicator.setViewPager(mPager);
        }
        final float density = getResources().getDisplayMetrics().density;

//Set circle indicator radius
        circlePageIndicator.setRadius(5 * density);
        // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0;
                    mNiceVideoPlayer.setVisibility(View.VISIBLE);
                    relativeLy.setVisibility(View.GONE);
                    setVideo();
                }
                mPager.setCurrentItem(currentPage++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new

                                    TimerTask() {
                                        @Override
                                        public void run() {
                                            handler.post(Update);
                                        }
                                    }, 11000, 11000);

        // Pager listener over indicator
        circlePageIndicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                currentPage = position;

            }

            @Override
            public void onPageScrolled(int pos, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int pos) {

            }
        });
    }
}
