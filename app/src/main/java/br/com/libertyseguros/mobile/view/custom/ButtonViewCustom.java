package br.com.libertyseguros.mobile.view.custom;


import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import androidx.appcompat.widget.AppCompatButton;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.Button;

import java.util.Timer;
import java.util.TimerTask;

public class ButtonViewCustom extends AppCompatButton{

    private Timer timer;

    private TimerTask timerTask;

    private Activity context;

    /**
     * @param context
     */
    public ButtonViewCustom(Context context)
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
    public ButtonViewCustom(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        this.context = (Activity) context;

        Button view = (Button) ButtonViewCustom.this;
        view.getBackground().clearColorFilter();
        view.invalidate();

    }

    /**
     * @param context
     * @param attrs
     * @param defStyle
     */
    public ButtonViewCustom(Context context, AttributeSet attrs, int defStyle)
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
                    Button view = (Button) this;
                    view.getBackground().setColorFilter(Color.parseColor("#95B7B2B0"), PorterDuff.Mode.SRC_ATOP);
                    view.invalidate();removeSelect();
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
                        Button view = (Button) ButtonViewCustom.this;
                        view.getBackground().clearColorFilter();
                        view.invalidate();
                    }
                });


            }
        };

        timer.schedule(timerTask, 500);


    }
}
