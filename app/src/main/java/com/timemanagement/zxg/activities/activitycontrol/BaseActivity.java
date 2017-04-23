package com.timemanagement.zxg.activities.activitycontrol;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.timemanagement.zxg.timemanagement.R;

/**
 * activity基类
 * Created by zxg on 2016/9/26.
 * QQ:1092885570
 */
public abstract class BaseActivity extends FragmentActivity {

    public static String TAG = BaseActivity.class.getSimpleName();

    protected Context mContext;
    public BaseActivity mthis;
    private LinearLayout ll_base_container;
    protected ViewHolder mViewHolder;
    public TextView tv_left;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        mthis = this;
        //添加到activity栈中
        ActivityManager.getInstance().addActivity(this);

        setContentView(R.layout.activity_base);
        ll_base_container = (LinearLayout) findViewById(R.id.ll_base_container);

        mViewHolder = new ViewHolder();
        mViewHolder.rl_titlebar = findViewById(R.id.rl_titlebar);
        mViewHolder.ll_left = (LinearLayout) findViewById(R.id.ll_left);
        mViewHolder.iv_left = (ImageView) findViewById(R.id.iv_left);
        mViewHolder.tv_left = (TextView) findViewById(R.id.tv_left);
        tv_left = mViewHolder.tv_left;
        mViewHolder.tv_title = (TextView) findViewById(R.id.tv_title);
        mViewHolder.tv_remark = (TextView) findViewById(R.id.tv_remark);
        mViewHolder.iv_center = (ImageView) findViewById(R.id.iv_center);
        mViewHolder.tv_right = (TextView) findViewById(R.id.tv_right);
        mViewHolder.iv_right = (ImageView) findViewById(R.id.iv_right);
        mViewHolder.iv_right1 = (ImageView) findViewById(R.id.iv_right1);
        mViewHolder.iv_right2 = (ImageView) findViewById(R.id.iv_right2);

        initHead(mViewHolder);
    }

    /**
     * 设置布局
     * @param view
     */
    public void setView(View view){
        if(ll_base_container != null)
            ll_base_container.addView(view);
    }

    public void setView(int resourceID){
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(resourceID, null);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        contentView.setLayoutParams(layoutParams);
        setView(contentView);
    }

    /**
     * 初始化Activity头部
     * @param viewHolder
     */
    public abstract void initHead(ViewHolder viewHolder);

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityManager.getInstance().finishActivity(this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            ActivityManager.getInstance().finishActivity(mthis);
        }
        return false;
    }

    public class ViewHolder {
        public View rl_titlebar;
        public LinearLayout ll_left;
        public ImageView iv_left, iv_center, iv_right, iv_right1, iv_right2;
        public TextView tv_left, tv_title, tv_remark, tv_right;
    }
}
