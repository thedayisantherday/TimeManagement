package com.timemanagement.zxg.widget;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.timemanagement.zxg.activities.EventDayActivity;
import com.timemanagement.zxg.activities.EventEditActivity;
import com.timemanagement.zxg.model.EventModel;
import com.timemanagement.zxg.timemanagement.R;
import com.timemanagement.zxg.utils.DimensionUtils;
import com.timemanagement.zxg.utils.LogUtils;
import com.timemanagement.zxg.utils.Tools;

/**
 * Created by zxg on 17/4/15.
 */

public class EventDialog {

    private Context mContext;

    public View view_event_dialog, view_emergency_sign;
    private TextView tv_event_dialog;
    private RelativeLayout.LayoutParams layoutParams;
    public EventModel mEventModel;
    private int[] importantColor = new int[]{Color.RED, Color.YELLOW, Color.BLACK};
    private int[] emergencyColor1 = new int[]{Color.RED, Color.rgb(141, 75, 187), Color.BLUE};
    private int[] emergencyColor2 = new int[]{Color.argb(96, 255, 0, 0), Color.argb(96, 141, 75, 187), Color.argb(100, 150, 170, 215)};

    public EventDialog(Context context) {
        mContext = context;

        initView();
    }

    private void initView() {
        view_event_dialog = View.inflate(mContext, R.layout.layout_event_dialog, null);
        view_emergency_sign = view_event_dialog.findViewById(R.id.view_emergency_sign);
        tv_event_dialog = (TextView) view_event_dialog.findViewById(R.id.tv_event_dialog);
        tv_event_dialog.setText("新建事件");
        layoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT);

        final EventDialog self = this;
        view_event_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventDayActivity.mEventDialog = self;
                LogUtils.i("view_event_dialog onClick","view_event_dialog onClick");
                EventEditActivity.startSelf(mContext, 1, mEventModel);
            }
        });
    }

    public void setView(ViewGroup viewGroup, int left, int top) {
        layoutParams.setMargins(left, top, 0, 0);
        layoutParams.width = DimensionUtils.getWidthPixels() - left;
        layoutParams.height = 100;
        view_event_dialog.setLayoutParams(layoutParams);
        viewGroup.addView(view_event_dialog);
    }

    public void updateView(ViewGroup viewGroup, int left, int top, int width, int height, String title) {
        layoutParams.setMargins(left, top, 0, 0);
        if (height < 60){ //为了保证事件title显示，EventDialog最小的高度为60px
            height = 60;
        }
//        if (width > 0) {
//            layoutParams.width = width;
//        }
        layoutParams.width = DimensionUtils.getWidthPixels() - left;
        layoutParams.height = height;
        view_event_dialog.setLayoutParams(layoutParams);
        LogUtils.i("color", "importance:" + mEventModel.getImportance() + ", emergency:"+mEventModel.getEmergency());
        view_emergency_sign.setBackgroundColor(emergencyColor1[mEventModel.getEmergency()%emergencyColor1.length]);
        view_event_dialog.setBackgroundColor(emergencyColor2[mEventModel.getEmergency()%emergencyColor2.length]);
        tv_event_dialog.setTextColor(importantColor[mEventModel.getImportance()%importantColor.length]);
//        viewGroup.addView(view_event_dialog);
        if (!Tools.isEmpty(title)) {
            tv_event_dialog.setText(title);
        }
    }
}
