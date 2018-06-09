package demo.zhr.zelasticrefreshscrollviewdemo;

import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.zhhr.ZElasticRefreshScrollView;


public class MainActivity extends AppCompatActivity {
    ZElasticRefreshScrollView mScrollView;
    private Handler mHandler= new Handler();

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);
        initActionBar();
        initView();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void initActionBar() {
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.TRANSPARENT);
    }


    private void initView() {
        mScrollView = (ZElasticRefreshScrollView) findViewById(R.id.sv_comic);
        //手动添加内容
       /* View mTopView = LayoutInflater.from(this).inflate(R.layout.layout_top, null);
        View mContentView = LayoutInflater.from(this).inflate(R.layout.layout_content, null);
        View mLoadingView = LayoutInflater.from(this).inflate(R.layout.layout_loading, null);
        View mBottomView = LayoutInflater.from(this).inflate(R.layout.layout_bottom, null);
        mScrollView.setTopView(mTopView);
        mScrollView.setContentView(mContentView);
        mScrollView.setLoadingView(mLoadingView);
        mScrollView.setBottomView(mBottomView);*/
        mScrollView.setOnRefreshListener(new ZElasticRefreshScrollView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.d("zhhr1122","onRefresh");
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mScrollView.setRefreshing(false);
                    }
                },1000);
            }
            @Override
            public void onRefreshFinish() {
                Log.d("zhhr1122","onRefreshFinish");
            }

            @Override
            public void onLoadMore() {
                Log.d("zhhr1122","onLoadMore");
            }
        });
        mScrollView.setOnScrollListener(new ZElasticRefreshScrollView.OnScrollListener() {
            @Override
            public void onScroll(int y) {
                Log.d("zhhr1122","onScroll Y="+y);
            }
        });
    }
}
