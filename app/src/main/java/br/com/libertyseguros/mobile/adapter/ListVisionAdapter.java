package br.com.libertyseguros.mobile.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.google.android.gms.common.util.Strings;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import br.com.libertyseguros.mobile.R;
import br.com.libertyseguros.mobile.beans.Vision360Beans;
import br.com.libertyseguros.mobile.libray.Config;
import br.com.libertyseguros.mobile.util.LocaleUtils;

public class ListVisionAdapter extends BaseAdapter {

    private LayoutInflater inflater;

    private Context context;

    private List<Vision360Beans> list;

    private int maxSizeTitle = 20;

    private int colorSelected = 0;

    public ListVisionAdapter(Context context, List<Vision360Beans> list) {
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
            vi = inflater.inflate(R.layout.item_vision, null);
        } else {
            vi = view;
        }

        Vision360Beans item = list.get(position);

        LinearLayout llBackgroundInfo = vi.findViewById(R.id.ll_background_info_vision);
        llBackgroundInfo.setBackgroundColor(getcolor(item));
        list.get(position).setColor(colorSelected);

        ImageView ivLetter =  vi.findViewById(R.id.profile_image);

        TextView tvTitle = vi.findViewById(R.id.tv_title_vision);
        TextView tvValue = vi.findViewById(R.id.tv_value);
        TextView tvDate = vi.findViewById(R.id.tv_date_vision);
        TextView tvMonth = vi.findViewById(R.id.tv_month_vision);
        TextView tvFraq = vi.findViewById(R.id.tv_fran_vision);

        LinearLayout llLineStart = vi.findViewById(R.id.line_start_detail_vision);
        LinearLayout llLineEnd = vi.findViewById(R.id.line_end_detail_vision);

        if(position == 0){
            llLineStart.setVisibility(View.INVISIBLE);
        } else {
            llLineStart.setVisibility(View.VISIBLE);
        }

        if(position == (list.size() - 1)){
            llLineEnd.setVisibility(View.INVISIBLE);
        } else {
            llLineEnd.setVisibility(View.VISIBLE);
        }


        int sizeTitle = item.getDescricao().length();

        if(sizeTitle >= maxSizeTitle){
            tvTitle.setText(item.getDescricao().substring(0, maxSizeTitle) + " _");
        } else {
            String titleEnd = " ";
            for (int ind = sizeTitle; ind < maxSizeTitle; ind++) {
                titleEnd += "_";
            }

            tvTitle.setText(item.getDescricao() + titleEnd);
        }

        String valorPago = item.getValorPago();
        tvValue.setText(toBrazilianRealCurrency(valorPago));


        String valorFranquia = item.getValorFranquia();
        if(!valorFranquia.equals("0.0")){
            tvFraq.setText(context.getString(R.string.valor_franquia, "" + toBrazilianRealCurrency(valorFranquia)));
        } else {
            tvFraq.setVisibility(View.GONE);
        }

        try{
            String date[] = item.getDataOcorrencia().split("T");
            String dateInfo[] = toConvertDate(date[0], "yyyy-MM-dd", "dd.MM.yyyy");

            tvDate.setText(dateInfo[0]);

            TextDrawable drawable1 = TextDrawable.builder()
                    .beginConfig()
                    .textColor(Color.BLACK)
                    .endConfig()
                    .buildRound(String.valueOf(position + 1), context.getResources().getColor(R.color.background_date_vision));

            ivLetter.setImageDrawable(drawable1);

            tvMonth.setText(dateInfo[2]);



        }catch (Exception ex){
            Log.i(Config.TAG, ex.toString());
        }


        return vi;
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
