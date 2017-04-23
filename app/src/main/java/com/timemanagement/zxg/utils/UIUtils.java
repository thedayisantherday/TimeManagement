package com.timemanagement.zxg.utils;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;

/** UI相关工具类
 * Created by zxg on 2016/11/4.
 * QQ:1092885570
 */

public class UIUtils {

    /**
     * 设置EditText编辑框的小数位数（可以先用正则表达式判断是否匹配）
     * @param editText
     * @param decimalDigits 小数位数
     * @author zhuxiaoguang
     */
    public static void setEditTextDecimal(final EditText editText, final int decimalDigits) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //正则表达式匹配
                /*String regEx = "(^10{0,6}(\\.0{1,2})?)|(^[1-9]\\d{0,5}(\\.\\d{1,2})?)";
                Pattern pattern = Pattern.compile(regEx);
                Matcher matcher = pattern.matcher(s.toString().trim());
                boolean isMatch = matcher.find();*/

                if (s.toString().contains(".")) {
                    if (s.length() - 1 - s.toString().indexOf(".") > decimalDigits) {
                        s = s.toString().subSequence(0,
                                s.toString().indexOf(".") + decimalDigits + 1);
                        editText.setText(s);
                        editText.setSelection(s.length());
                    }
                }
                if (s.toString().trim().substring(0).equals(".")) {
                    s = "0" + s;
                    editText.setText(s);
                    editText.setSelection(2);
                }

                if (s.toString().startsWith("0")
                        && s.toString().trim().length() > 1) {
                    if (!s.toString().substring(1, 2).equals(".")) {
                        editText.setText(s.subSequence(0, 1));
                        editText.setSelection(1);
                        return;
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

            }

        });

    }

    /**
     * 获取view的高度和宽度
     * @param view 需要测量的view
     * @return
     */
    public static int[] getViewWidthAndHeight(View view) {
        int _width = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int _height = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        view.measure(_width, _height);
        int height = view.getMeasuredHeight();
        int width = view.getMeasuredWidth();
        return new int[]{width, height};
    }

    /**
     * 根据listview的item调整listview的高度
     * @param listView
     */
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        if (listView == null) {
            return;
        }

        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View itemView = listAdapter.getView(i, null, listView);
//            itemView.measure(0, 0);
//            totalHeight += itemView.getMeasuredHeight();
            totalHeight += getViewWidthAndHeight(itemView)[1];
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }
}
