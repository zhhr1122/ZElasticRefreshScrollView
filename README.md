# ZElasticRefreshScrollView
# 介绍
  一款高仿IOS下拉回弹效果的下拉刷新控件
## 使用截图
  默认效果：
  ![image](https://github.com/zhhr1122/ZElasticRefreshScrollView/blob/master/image/image.gif?raw=true)
  
  
  实际使用例子：
  ![image](https://github.com/zhhr1122/ZElasticRefreshScrollView/blob/master/image/image1.gif?raw=true?raw=true)
  
# 用法
## 导入
    直接添加依赖
    compile 'com.zhhr:ZElasticRefresh:1.0.0'
## 使用方法
1.直接在XML中引入布局
   
```
<com.zhhr.ZElasticRefreshScrollView
        android:id="@+id/sv_comic"
        android:layout_width="match_parent"
        android:layout_height="match_parent">

</com.zhhr.ZElasticRefreshScrollView>
```

默认效果如上方效果图

2. 在代码中设置各个布局

```
mScrollView = (ZElasticRefreshScrollView) findViewById(R.id.sv_comic);
//手动添加内容
View mTopView = LayoutInflater.from(this).inflate(R.layout.layout_top, null);
View mContentView = LayoutInflater.from(this).inflate(R.layout.layout_content, null);
View mLoadingView = LayoutInflater.from(this).inflate(R.layout.layout_loading, null);
View mBottomView = LayoutInflater.from(this).inflate(R.layout.layout_bottom, null);

//把对应的布局set进去即可
//设置头部view
mScrollView.setTopView(mTopView);
//设置中间内容部分view
mScrollView.setContentView(mContentView);
//设置加载布局view（不设置默认动画效果如上动图所示，可以不设置）
mScrollView.setLoadingView(mLoadingView);
//设置底部view（不设置默认动画效果如上动图所示，可以不设置）
mScrollView.setBottomView(mBottomView);
```
3. 监听事件

```
    mScrollView.setOnRefreshListener(new ZElasticRefreshScrollView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //刷新事件
                Log.d("zhhr1122","onRefresh");
                mScrollView.setRefreshing(false);
            }
            @Override
            public void onRefreshFinish() {
                //刷新完成
                Log.d("zhhr1122","onRefreshFinish");
            }

            @Override
            public void onLoadMore() {
               //上拉调用
                Log.d("zhhr1122","onLoadMore");
            }
        });
    mScrollView.setOnScrollListener(new ZElasticRefreshScrollView.OnScrollListener() {
            @Override
            public void onScroll(int y) {
                //滑动事件
                Log.d("zhhr1122","onScroll Y="+y);
            }
        });
```
具体实现可以参考DEMO中的代码


