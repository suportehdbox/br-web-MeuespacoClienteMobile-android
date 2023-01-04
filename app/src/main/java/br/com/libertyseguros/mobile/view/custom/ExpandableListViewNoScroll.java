package br.com.libertyseguros.mobile.view.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ExpandableListView;


public class ExpandableListViewNoScroll extends ExpandableListView {

    public ExpandableListViewNoScroll(Context context,  AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
