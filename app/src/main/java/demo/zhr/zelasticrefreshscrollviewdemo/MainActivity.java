package demo.zhr.zelasticrefreshscrollviewdemo;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import demo.zhr.zelasticrefreshscrollviewdemo.CustomView.ZElasticRefreshScrollView;

public class MainActivity extends AppCompatActivity {
    ZElasticRefreshScrollView mScrollView;
    private Handler mHandler= new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);
        initView();
    }

    private void initView() {
        mScrollView = (ZElasticRefreshScrollView) findViewById(R.id.sv_comic);
        View mTopView = LayoutInflater.from(this).inflate(R.layout.layout_top, null);
        mScrollView.setTopView(mTopView);
        mScrollView.setRefreshListener(new ZElasticRefreshScrollView.RefreshListener() {
            @Override
            public void onActionDown() {

            }

            @Override
            public void onActionUp() {

            }

            @Override
            public void onRefresh() {
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mScrollView.setRefreshing(false);
                    }
                },1000);
            }

            @Override
            public void onRefreshFinish() {

            }

            @Override
            public void onLoadMore() {

            }

            @Override
            public void onScroll(int y) {

            }
        });
    }
}
