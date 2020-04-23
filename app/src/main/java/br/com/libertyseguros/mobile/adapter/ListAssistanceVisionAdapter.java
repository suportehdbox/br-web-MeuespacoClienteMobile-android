package br.com.libertyseguros.mobile.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import br.com.libertyseguros.mobile.R;
import br.com.libertyseguros.mobile.beans.AssitanceVision360Beans;
import br.com.libertyseguros.mobile.beans.Vision360Beans;
import br.com.libertyseguros.mobile.libray.Config;
import br.com.libertyseguros.mobile.util.LocaleUtils;

public class ListAssistanceVisionAdapter extends BaseAdapter {

    private LayoutInflater inflater;

    private Context context;

    private List<AssitanceVision360Beans> list;

    private int maxSizeTitle = 20;

    private int colorSelected = 0;

    public ListAssistanceVisionAdapter(Context context, List<AssitanceVision360Beans> list) {
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        View vi = null;

        if (view == null) {
            vi = inflater.inflate(R.layout.item_vision_assistance, null);
        } else {
            vi = view;
        }

        AssitanceVision360Beans item = list.get(position);


        ImageView ivIcon = vi.findViewById(R.id.icon_assistencia_image_view);
        ivIcon.setImageDrawable(getStringResourceByName(item.getImagem()));

        TextView tvTitle = vi.findViewById(R.id.title_assistance_text_view);
        tvTitle.setVisibility(View.GONE);

        TextView tvDescription = vi.findViewById(R.id.description_assistance_text_view);
        tvDescription.setText(firstLetterUpperCase(item.getDescricao()));
        TextView tvDate = vi.findViewById(R.id.date_assistance_text_view);
        tvDate.setText(item.getData());
        TextView tvTime = vi.findViewById(R.id.time_assistance_text_view);
        tvTime.setText(item.getHora());


        return vi;
    }

    private Drawable getStringResourceByName(String aString) {
        String packageName = context.getPackageName();
        int resId = context.getResources().getIdentifier(aString, "drawable", packageName);
        return context.getResources().getDrawable(resId);
    }


    public static String firstLetterUpperCase(String text) {
        if (text != null && text.length() > 0) {
            StringBuilder sb = new StringBuilder(text.toLowerCase());
            sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
            return sb.toString();
        } else {
            return text;
        }
    }

    public String[] toConvertDate(String value, String format, String convertFormat) throws ParseException {

        String dateInfo[] = new String[3];

        SimpleDateFormat sdfDatabase = new SimpleDateFormat(format);

        Date currentdate = sdfDatabase.parse(value);

        SimpleDateFormat sdfDefautl = new SimpleDateFormat(convertFormat);

        dateInfo[0] = sdfDefautl.format(currentdate);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentdate);

        dateInfo[1] = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
        dateInfo[2] = getMonth(calendar.get(Calendar.MONTH) + 1);

        return dateInfo;
    }

    private String getMonth(int month){
        String monthString = "";
        switch(month){
            case 1:
                monthString = "Jan";
                break;
            case 2:
                monthString = "Fev";
                break;
            case 3:
                monthString = "Mar";
                break;
            case 4:
                monthString = "Abr";
                break;
            case 5:
                monthString = "Mai";
                break;
            case 6:
                monthString = "Jun";
                break;
            case 7:
                monthString = "Jul";
                break;
            case 8:
                monthString = "Ago";
                break;
            case 9:
                monthString = "Set";
                break;
            case 10:
                monthString = "Out";
                break;
            case 11:
                monthString = "Nov";
                break;
            case 12:
                monthString = "Dez";
                break;
        }

        return monthString;
    }

    private int getcolor(Vision360Beans vision360Beans) {

            if(vision360Beans.getColor() == -1){
                if(colorSelected == 0 || colorSelected == 3){
                    colorSelected = 1;
                } else{
                    colorSelected++;
                }

            } else {
                colorSelected =  vision360Beans.getColor();
            }



        String packageName = context.getPackageName();
        int resId = context.getResources().getIdentifier("line_vision_" + colorSelected, "color", packageName);
        return context.getResources().getColor(resId);
    }

    public static String toBrazilianRealCurrency(String value) {
       try{

           BigDecimal sum = new BigDecimal(Double.parseDouble(value));

           NumberFormat numberFormat = NumberFormat.getCurrencyInstance(
                   LocaleUtils.getPortugueseBrazilianLocale());

           return numberFormat.format(sum);
       }catch (Exception ex){
           return String.valueOf(value);
       }


    }
}
