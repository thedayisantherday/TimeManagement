package com.timemanagement.zxg.widget;

import android.app.AlertDialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.timemanagement.zxg.timemanagement.R;

/**
 * Created by zxg on 16/11/28.
 */
public class CustomDialog {

    private static String TAG = CustomDialog.class.getSimpleName();

    private AlertDialog mAlertDialog;
    private Context mContext;
    private String title; //标题
    private String content; //内容
    private String str_negative; //左边按钮文字
    private String str_positive; //右边按钮文字
    private int resource; //自定义布局资源

    private TextView tv_title;

    //自定义布局回调
    private CustomViewCallBack mCustomViewCallBack;
    public interface CustomViewCallBack {
        void doCustomViewCallBack(Window window);
    }
    public void setCustomViewCallBack(CustomViewCallBack callBack) {
        this.mCustomViewCallBack = callBack;
    }
    //按钮点击回调
    private ClickCallBack mClickCallBack;
    public interface ClickCallBack {
        void onNegative();
        void onPositive();
    }
    public void setClickCallBack(ClickCallBack callBack) {
        this.mClickCallBack = callBack;
    }

    /**
     *
     * @param context
     * @param title 标题
     * @param content 内容
     * @param nagetive 左边按钮文字，为空则不显示左边按钮
     * @param positive 右边按钮文字，为空则不显示右边按钮
     */
    public CustomDialog(Context context, String title, String content, String nagetive, String positive){
        this.mContext = context;
        this.title = title;
        this.content = content;
        this.str_negative = nagetive;
        this.str_positive = positive;
    }

    /**
     *
     * @param context
     * @param title 标题
     * @param content 内容
     * @param resource 自定义布局资源
     */
    public CustomDialog(Context context, String title, String content, int resource){
        this.mContext = context;
        this.title = title;
        this.content = content;
        this.resource = resource;
    }

    /**
     * 显示对话框
     * resource＝0时，加载默认布局layout_dialog_normal
     */
    public void showDialog(){
        mAlertDialog = new AlertDialog.Builder(mContext, R.style.dialog).create();
        mAlertDialog.setCanceledOnTouchOutside(false); //点击对话框外部对话框不消失
        mAlertDialog.show();

        Window window = mAlertDialog.getWindow();
        //resource＝0时，加载默认布局layout_dialog_normal
        if(resource == 0){
            window.setContentView(R.layout.layout_dialog_normal);

            tv_title = (TextView) window.findViewById(R.id.tv_title);
            tv_title.setText(title);
            TextView tv_content = (TextView) window.findViewById(R.id.tv_content);
            tv_content.setText(content);

            LinearLayout ll_btn = (LinearLayout) window.findViewById(R.id.ll_btn);
            //str_positive和str_negative都为空则两个按钮都隐藏
            if (TextUtils.isEmpty(str_positive) && TextUtils.isEmpty(str_negative)){
                ll_btn.setVisibility(View.GONE);
            } else {
                View view_split_line = window.findViewById(R.id.view_split_line);
                TextView tv_positive = (TextView) window.findViewById(R.id.tv_positive);
                //str_positive为空则按钮隐藏
                if (TextUtils.isEmpty(str_positive)) {
                    tv_positive.setVisibility(View.GONE);
                    view_split_line.setVisibility(View.GONE);
                } else {
                    tv_positive.setText(str_positive);
                    tv_positive.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mAlertDialog.dismiss();
                            if (mClickCallBack != null) {
                                mClickCallBack.onPositive();
                            }
                        }
                    });
                }

                TextView tv_negative = (TextView) window.findViewById(R.id.tv_negative);
                //str_negative为空则按钮隐藏
                if (TextUtils.isEmpty(str_negative)) {
                    tv_positive.setVisibility(View.GONE);
                    view_split_line.setVisibility(View.GONE);
                } else {
                    tv_negative.setText(str_negative);
                    tv_negative.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mAlertDialog.dismiss();
                            if (mClickCallBack != null) {
                                mClickCallBack.onNegative();
                            }
                        }
                    });
                }
            }
        } else {
            window.setContentView(resource);
            if (mCustomViewCallBack != null) {
                mCustomViewCallBack.doCustomViewCallBack(window);
            }
        }

        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
    }

    /**
     * 设置点击对话框外部对话框是否消失
     * @param isCancel
     */
    public void setCanceledOnTouchOutside(boolean isCancel){
        if (mAlertDialog != null){
            mAlertDialog.setCanceledOnTouchOutside(isCancel);
        }
    }

    /**
     * 设置是否显示对话框标题
     * @param isVisible
     */
    public void setTitleVisible(boolean isVisible){
        if (tv_title != null){
            if (isVisible) {
                tv_title.setVisibility(View.VISIBLE);
            }else {
                tv_title.setVisibility(View.GONE);
            }
        }
    }

}
