package br.com.libertyseguros.mobile.view.custom;


import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

import java.util.Timer;
import java.util.TimerTask;

public class ImageViewCustom extends com.inthecheesefactory.thecheeselibrary.widget.AdjustableImageView{

    private Timer timer;

    private TimerTask timerTask;

    private Activity context;

    /**
     * @param context
     */
    public ImageViewCustom(Context context)
    {
        super(context);

        this.context = (Activity) context;
        // TODO Auto-generated constructor stub
        this.setFilterTouchesWhenObscured(true);

    }

    /**
     * @param context
     * @param attrs
     */
    public ImageViewCustom(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        this.context = (Activity) context;

        ImageView view = (ImageView) ImageViewCustom.this;
        view.getDrawable().clearColorFilter();
        view.invalidate();

    }

    /**
     * @param context
     * @param attrs
     * @param defStyle
     */
    public ImageViewCustom(Context context, AttributeSet attrs, int defStyle)
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
                    ImageView view = (ImageView) this;
                    view.getDrawable().setColorFilter(0x10000000, PorterDuff.Mode.SRC_ATOP);
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
                        ImageView view = (ImageView) ImageViewCustom.this;
                        view.getDrawable().clearColorFilter();
                        view.invalidate();
                    }
                });


            }
        };

        timer.schedule(timerTask, 500);


    }
}
