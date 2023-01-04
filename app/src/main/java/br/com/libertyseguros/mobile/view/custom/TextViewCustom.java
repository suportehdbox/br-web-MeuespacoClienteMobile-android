package br.com.libertyseguros.mobile.view.custom;


import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class TextViewCustom extends TextView{

    private Timer timer;

    private TimerTask timerTask;

    private Activity context;

    private ColorStateList color;

    /**
     * @param context
     */
    public TextViewCustom(Context context)
    {
        super(context);

        this.context = (Activity) context;
        this.setFilterTouchesWhenObscured(true);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param context
     * @param attrs
     */
    public TextViewCustom(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        this.context = (Activity) context;

    }

    /**
     * @param context
     * @param attrs
     * @param defStyle
     */
    public TextViewCustom(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
        this.context = (Activity) context;

    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        // TODO Auto-generated method stub
        super.onDraw(canvas);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        // TODO Auto-generated method stub




        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                try {
                    TextView view = (TextView) this;

                    color = view.getTextColors();

                    view.setTextColor(0x30000000);

                    view.invalidate();
                    removeSelect();
                } catch(Exception ex){
                    ex.printStackTrace();
                }
                break;
            }
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL: {

                break;
            }
        }

        return super.onTouchEvent(event);

    }


    public void removeSelect(){
        timer = new Timer();

        timerTask = new TimerTask() {
            @Override
            public void run() {
                context.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        TextView view = (TextView) TextViewCustom.this;
                        view.setTextColor(color);
                    }
                });


            }
        };

        timer.schedule(timerTask, 200);


    }
}
