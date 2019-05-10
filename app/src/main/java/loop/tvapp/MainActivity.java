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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        ImagesArray = new ArrayList<String>();
        integers.add(1);
        integers.add(2);
        for (int i = 0; integers.size() > i; i++) {
            if (i % 2 == 0) {
//                videoView.setVideoURI(Uri.parse("http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"));
                mNiceVideoPlayer = findViewById(R.id.nice_video_player);
                mNiceVideoPlayer.setPlayerType(NiceVideoPlayer.TYPE_IJK); // or NiceVideoPlayer.TYPE_NATIVE
                mNiceVideoPlayer.setUp("http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4", null);
//        TxVideoPlayerController controller = new TxVideoPlayerController(this);
//        controller.setTitle("LFS");
//        controller.setImage(R.drawable.ic_palyer_share);
//        mNiceVideoPlayer.setController(controller);
                mNiceVideoPlayer.start();
                mNiceVideoPlayer.releasePlayer();
                if (mNiceVideoPlayer.isCompleted()) {
                    mNiceVideoPlayer.start();
                }
            } else {
                viewPagerSetUp();
            }
        }
//
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
        mPager = findViewById(R.id.pager);
        ImagesArray.add("https://www.google.com/imgres?imgurl=https%3A%2F%2Fimages.pexels.com%2Fphotos%2F556416%2Fpexels-photo-556416.jpeg%3Fcs%3Dsrgb%26dl%3Dbridge-clouds-cloudy-556416.jpg%26fm%3Djpg&imgrefurl=https%3A%2F%2Fwww.pexels.com%2Fsearch%2Fnature%2520wallpaper%2F&docid=2DdgupkIVkp2zM&tbnid=WDcwRZIw-wUARM%3A&vet=10ahUKEwi5pbLQwpDiAhWJr48KHf1OB1YQMwh1KA0wDQ..i&w=4920&h=3251&safe=active&bih=738&biw=1440&q=wallpaper&ved=0ahUKEwi5pbLQwpDiAhWJr48KHf1OB1YQMwh1KA0wDQ&iact=mrc&uact=8");
        ImagesArray.add("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTHN2Mp-hEWSIUMgLVdnUwaP0V5x9dvpluFd8zsq0EMhzCQfsek");
        ImagesArray.add("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcREax0X2F8gAyEn4SCkEsvMTD_1GI-ztygMpXMoadL3WheSh0G3");
        circlePageIndicator = findViewById(R.id.indicator);
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
                                    }, 1000, 1000);

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
