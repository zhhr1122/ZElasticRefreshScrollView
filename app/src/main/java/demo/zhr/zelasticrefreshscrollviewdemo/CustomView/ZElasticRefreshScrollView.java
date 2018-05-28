package demo.zhr.zelasticrefreshscrollviewdemo.CustomView;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import demo.zhr.zelasticrefreshscrollviewdemo.R;
import demo.zhr.zelasticrefreshscrollviewdemo.Utils.DisplayUtil;


/***
 * 下拉回弹的ScrollView
 * @author 皓然
 *
 */
public class ZElasticRefreshScrollView extends ScrollView {

    private RelativeLayout inner;
    private LinearLayout mMoveView;
    private View mLoadingBottom;
    private View mTopView;
    private float y;
    private Rect normal = new Rect();
    private int height;
    private boolean isRefresh;
    private boolean isAction_UP;
    private RefreshListener listener;
    private View mLoadingTop;
    private ImageView mLoadingTopImg;
    private TextView mLoadingText;
    public static final int  SCROLL_TO_UP =1;
    public static final int  SCROLL_TO_DOWN =2;
    int oldY= 0;
    private View view;
    private int mTopViewHieght = 0;
    private int mLoadingViewHeight = 0;
    private int offset = 15;
    private int actionbarHeight;
    private boolean isCustomLoadingView;
    private boolean isAllowRefresh = true;

    public ZElasticRefreshScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        view = LayoutInflater.from(getContext()).inflate(R.layout.layout_elasticview, null);
        addView(view);
    }

    public ZElasticRefreshScrollView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ZElasticRefreshScrollView(Context context) {
        this(context,null);
    }

    @Override
    protected void onScrollChanged(int x, int y, int oldx, int oldy) {
        super.onScrollChanged(x, y, oldx, oldy);
        if(actionbarHeight==0){
            int[] position = new int[2];
            getLocationOnScreen(position);
            actionbarHeight = position[1];
            Log.d("zhhr1122","actionbarHeight="+actionbarHeight);
        }
        height = inner.getMeasuredHeight()-mLoadingBottom.getMeasuredHeight()- DisplayUtil.getScreenHeight(getContext())+actionbarHeight;
        if(y>=height){
            this.scrollTo(0,height);
        }
        if(listener!=null){
            if(y - oldY>0){
                listener.onScroll(SCROLL_TO_DOWN);
            }else if(y - oldY<0&&y<height){
                listener.onScroll(SCROLL_TO_UP);
            }
            oldY = y;
        }
    }
    //获取到ScrollView内部的子View,并赋值给inner
    protected void onFinishInflate() {
        if (getChildCount() > 0) {
            inner = (RelativeLayout) getChildAt(0);
        }
        mMoveView = (LinearLayout) inner.getChildAt(0);
        mTopView = inner.getChildAt(1);
        mLoadingTop = mMoveView.getChildAt(0);
        if(!isCustomLoadingView){
            RelativeLayout DefaltLoadingTop = (RelativeLayout) mLoadingTop;
            mLoadingText = (TextView) ((LinearLayout)DefaltLoadingTop.getChildAt(0)).getChildAt(1);
            mLoadingTopImg = (ImageView) inner.findViewById(R.id.iv_loading_top);
            initAnimation();
        }
        mLoadingBottom = mMoveView.getChildAt(2);
        setOverScrollMode(OVER_SCROLL_NEVER);//取消5.0效果
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mTopViewHieght = mTopView.getMeasuredHeight();
        mLoadingViewHeight = mLoadingTop.getMeasuredHeight();
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) mMoveView.getLayoutParams();
        lp.setMargins(0,mTopViewHieght-mLoadingViewHeight,0,0);
        mMoveView.setLayoutParams(lp);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private void initAnimation() {
        mLoadingTopImg.setImageResource(R.drawable.loading_top);
        AnimationDrawable animationDrawable = (AnimationDrawable) mLoadingTopImg.getDrawable();
        animationDrawable.start();
    }

    //重写滑动方法
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (inner == null) {
            return super.onTouchEvent(ev);
        } else {
            if(isRefresh&&listener!=null){
                return super.onTouchEvent(ev);
            }
            commOnTouchEvent(ev);
            //防止拉取过度的问题
            if(mMoveView.getTop()>(mTopViewHieght-mLoadingViewHeight)&&getScrollY()==0||mMoveView.getBottom()<inner.getMeasuredHeight()&&getScrollY() ==inner.getMeasuredHeight()-mLoadingBottom.getMeasuredHeight()-DisplayUtil.getScreenHeight(getContext())){
                return false;
            }
            return super.onTouchEvent(ev);
        }
    }

    public void commOnTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                y = ev.getY();
                //Log.d("zhr", "MotionEvent.ACTION_DOWN  y=" + y);
                if(!isCustomLoadingView){
                    mLoadingText.setText("下拉刷新");
                }

                if(listener!=null){
                    listener.onActionDown();
                }
                break;
            case MotionEvent.ACTION_UP:
                if(listener!=null){
                    listener.onActionUp();
                }
                if (isNeedAnimation()) {
                    // Log.v("mlguitar", "will up and animation");
                    if(mMoveView.getTop()>(mTopViewHieght+offset)&&listener!=null){
                        RefreshAnimation();
                        if(!isCustomLoadingView){
                            mLoadingText.setText("努力加载中...");
                        }
                        //animation();
                        isRefresh = true;
                        if(listener!=null){
                            listener.onRefresh();
                        }
                    }else{
                        //Log.v("zhhr112233", "getScrollY="+getScrollY()+",height="+height);
                        if(getScrollY()==height&&listener!=null){
                            listener.onLoadMore();
                        }
                        animation();
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                final float preY = y;
                float nowY = ev.getY();
                //Log.d("zhr", "MotionEvent.ACTION_MOVE  nowY=" + nowY + ";preY=" + preY);
                /**
                 * size=4 表示 拖动的距离为屏幕的高度的1/4
                 */
                int deltaY;
                deltaY = (int) Math.sqrt(Math.abs(nowY - preY)*2) ;
                // 滚动
                y = nowY;
                // 当滚动到最上或者最下时就不会再滚动，这时移动布局
                if (isNeedMove(nowY)) {
                    if (normal.isEmpty()) {
                        // 保存正常的布局位置
                        normal.set(mMoveView.getLeft(), mMoveView.getTop(),mMoveView.getRight(), mMoveView.getBottom());
                        return;
                    }
                    if (nowY > preY) {
                        //Log.d("zhhr","deltaY="+deltaY);
                        //Log.d("zhhr1122","mMoveView.getTop()="+mMoveView.getTop()+",mMoveView.getBottom="+mMoveView.getBottom()+"height="+height);
                        mMoveView.layout(mMoveView.getLeft(), mMoveView.getTop() + deltaY, mMoveView.getRight(),
                                mMoveView.getBottom() + deltaY);
                    } else if(nowY < preY){
                        //Log.d("zhhr1122","mMoveView.getTop()="+mMoveView.getTop()+",mMoveView.getBottom="+mMoveView.getBottom()+"height="+height);
                        mMoveView.layout(mMoveView.getLeft(), mMoveView.getTop() - deltaY, mMoveView.getRight(),
                                mMoveView.getBottom() - deltaY);
                    }

                    if(mMoveView.getTop()>(mTopViewHieght+offset)&&listener!=null){
                        if(!isCustomLoadingView){
                            mLoadingText.setText("松开即可刷新");
                        }

                    }
                    // 移动布局
                }
                break;
            default:
                break;
        }
    }

    // 开启动画移动

    public void animation() {
        // 开启移动动画
        TranslateAnimation ta = new TranslateAnimation(0, 0, mMoveView.getTop()-(mTopViewHieght-mLoadingViewHeight), normal.top-(mTopViewHieght-mLoadingViewHeight));
        Interpolator in = new DecelerateInterpolator();
        ta.setInterpolator(in);
        ta.setDuration(300);
        mMoveView.startAnimation(ta);
        // 设置回到正常的布局位置
        mMoveView.layout(normal.left, normal.top, normal.right, normal.bottom);
        normal.setEmpty();
    }

    public void RefreshAnimationFinish() {
        // 开启移动动画
        TranslateAnimation ta = new TranslateAnimation(0, 0, mMoveView.getTop()-(mTopViewHieght-mLoadingViewHeight), normal.top-(mTopViewHieght-mLoadingViewHeight));
        Interpolator in = new DecelerateInterpolator();
        ta.setInterpolator(in);
        ta.setDuration(300);
        ta.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if(listener!=null){
                    listener.onRefreshFinish();
                }
                if(!isCustomLoadingView){
                    mLoadingText.setText("下拉刷新");
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        if(!isCustomLoadingView){
            mLoadingText.setText("刷新完成");
        }
        mMoveView.startAnimation(ta);
        // 设置回到正常的布局位置
        mMoveView.layout(normal.left, normal.top, normal.right, normal.bottom);
        normal.setEmpty();
    }

    public void RefreshAnimation() {
        // 开启移动动画
        TranslateAnimation ta = new TranslateAnimation(0, 0, mMoveView.getTop()-(mTopViewHieght+offset), normal.top-(mTopViewHieght-mLoadingViewHeight));
        Interpolator in = new DecelerateInterpolator();
        ta.setInterpolator(in);
        ta.setDuration(300);
        mMoveView.startAnimation(ta);
        // 设置回到正常的布局位置
        mMoveView.layout(normal.left, normal.top+mLoadingViewHeight, normal.right, normal.bottom+mLoadingViewHeight);
        //normal.setEmpty();
    }

    // 是否需要开启动画  
    public boolean isNeedAnimation() {
        return !normal.isEmpty();
    }

    // 是否需要移动布局  
    public boolean isNeedMove(float nowY) {
        int scrollY = getScrollY();
        return scrollY == 0 && (nowY > mTopView.getMeasuredHeight()&&isAllowRefresh) || scrollY == height;
    }

    /**
     * 设置刷新状态
     * @param isRefresh
     */
    public void setRefreshing(boolean isRefresh){
        if(this.isRefresh!=isRefresh){
            this.isRefresh = isRefresh;
            if(isRefresh == false){
                RefreshAnimationFinish();
            }
        }
    }

    public boolean isRefreshing(){
        return isRefresh;
    }

    /**
     * 设置顶部View
     * @param view
     */
    public void setTopView(View view) {
        inner.removeViewAt(1);
        inner.addView(view,1);
        onFinishInflate();
    }

    /**
     * 设置加载View
     * @param view
     */
    public void setLoadingView(View view) {
        isCustomLoadingView = true;
        mMoveView.removeViewAt(0);
        mMoveView.addView(view,0);
        onFinishInflate();
    }

    /**
     * 设置数据view
     * @param view
     */
    public void setContentView(View view) {
        mMoveView.removeViewAt(1);
        mMoveView.addView(view,1);
        onFinishInflate();
    }

    /**
     * 设置底部view
     * @param view
     */
    public void setBottomView(View view) {
        mMoveView.removeViewAt(2);
        if(view ==null){
            View mView = new View(getContext());
            mView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,1));
            view = mView;
        }
        mMoveView.addView(view,2);
        onFinishInflate();
    }

    public void disable() {
        isAllowRefresh = false;
    }

    public interface RefreshListener{
        void onActionDown();
        void onActionUp();
        void onRefresh();
        void onRefreshFinish();
        void onLoadMore();
        void onScroll(int y);
    }

    public void setRefreshListener(RefreshListener listener){
        this.listener = listener;
    }

}
