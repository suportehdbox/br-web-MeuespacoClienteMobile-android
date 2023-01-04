package br.com.libertyseguros.mobile.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;

import java.util.ArrayList;

import br.com.libertyseguros.mobile.R;
import br.com.libertyseguros.mobile.beans.InstallmentsBeans;
import br.com.libertyseguros.mobile.beans.PolicyBeansV2;
import br.com.libertyseguros.mobile.libray.PolicyCalc;
import br.com.libertyseguros.mobile.util.OnBarCode;
import br.com.libertyseguros.mobile.view.DialogPayments;
import br.com.libertyseguros.mobile.view.ExtendPagament;
import br.com.libertyseguros.mobile.view.custom.ImageViewCustom;

public class ListParcelAdapter extends BaseAdapter {

    private final LayoutInflater inflater;

    private final Context context;
    private final PolicyCalc policyCalc;
    private final OnBarCode barcode;
    private final PolicyBeansV2.InsurancesV2 currentInsurances;
    public int issuance;
    public int index = -1;
    private ArrayList<InstallmentsBeans> list;
    private ImageViewCustom ivExtends;
    private DialogPayments dialogPayments;

    public ListParcelAdapter(Context context, ArrayList<InstallmentsBeans> list, int issuance, OnBarCode barcode, PolicyBeansV2.InsurancesV2 insuranceSel) {
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
        this.issuance = issuance;
        policyCalc = new PolicyCalc();
        this.barcode = barcode;
        this.currentInsurances = insuranceSel;
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
    public View getView(int i, View view, ViewGroup viewGroup) {
        View vi;
        if (view == null) {
            vi = inflater.inflate(R.layout.item_parcels, null);
        } else {
            vi = view;
        }
        LinearLayout llLine = vi.findViewById(R.id.ll_line);

        TextView tvSize = vi.findViewById(R.id.tv_size);
        InstallmentsBeans installment = list.get(i);
        tvSize.setText(installment.getNumber() + "");

        TextView tvDate = vi.findViewById(R.id.due_date);
        tvDate.setText(policyCalc.getDate(installment.getDueDate(), context, 2));

        TextView tvValue = vi.findViewById(R.id.value);
        tvValue.setText(policyCalc.getMoney(installment.getValue().replace("R$", "") + "", context));

        TextView tvStatus = vi.findViewById(R.id.tv_status);

        ivExtends = vi.findViewById(R.id.iv_extends);
        ivExtends.setTag(i + "");

        if ("PX".equals(installment.getCodigoTipoModalidadeCobranca()) && installment.isExpired()) {
            installment.setStatus(2);
        }

        switch (installment.getStatus()) {
            case 1:
                tvStatus.setText(context.getString(R.string.status_1));
                tvStatus.setTextColor(context.getResources().getColor(R.color.text_default_7));
                llLine.setBackgroundColor(context.getResources().getColor(R.color.status_1));
                ivExtends.setVisibility(View.GONE);
                break;
            case 2:
                tvStatus.setText(context.getString(R.string.status_2));
                tvStatus.setTextColor(context.getResources().getColor(R.color.text_default_8));
                llLine.setBackgroundColor(context.getResources().getColor(R.color.status_2));
                configButtonExtends(i);
                break;
            case 3:
                tvStatus.setText(context.getString(R.string.status_3_novo));
                tvStatus.setTextColor(context.getResources().getColor(R.color.text_default_14));
                llLine.setBackgroundColor(context.getResources().getColor(R.color.status_4));
                configButtonExtends(i);
                break;
            case 4:
                tvStatus.setText(context.getString(R.string.status_4));
                tvStatus.setTextColor(context.getResources().getColor(R.color.text_default_14));
                llLine.setBackgroundColor(context.getResources().getColor(R.color.status_4));
                configButtonExtends(i);
                break;
            default:
                tvStatus.setText(context.getString(R.string.status_3));
                tvStatus.setTextColor(context.getResources().getColor(R.color.text_default_4));
                ivExtends.setVisibility(View.GONE);
                llLine.setBackgroundColor(context.getResources().getColor(R.color.status_3));

                configButtonExtends(i);
                break;
        }

        if (installment.getShowComponent() > 0) {
            final int index = i;

            ivExtends.setOnClickListener(v -> {
                dialogPayments = new DialogPayments(context, currentInsurances, list.get(index), issuance, barcode);
                dialogPayments.show();
            });
        }


        return vi;
    }

    private Drawable getDrawable(int id) {
        return ResourcesCompat.getDrawable(context.getResources(), id, null);
    }

    private void configButtonExtends(int i) {
        InstallmentsBeans installment = list.get(i);
        switch (installment.getShowComponent()) {
            case 0:
                ivExtends.setVisibility(View.GONE);
                break;
            case 3:
                ivExtends.setImageDrawable(getDrawable(R.drawable.icon_pay_now_blue));
                ivExtends.setTag(i);
                ivExtends.setVisibility(View.VISIBLE);
                break;
            case 2:
            case 5:
            case 6:
                ivExtends.setImageDrawable(getDrawable(R.drawable.bt_extends));
                ivExtends.setTag(i);
                ivExtends.setVisibility(View.VISIBLE);
                break;
            case 1:
            case 4:
            case 7:
                if (installment.getStatus() == 2) {
                    ivExtends.setImageDrawable(getDrawable(R.drawable.icon_pay_now_red));
                } else if (installment.getStatus() == 3 || installment.getStatus() == 4) {
                    ivExtends.setImageDrawable(getDrawable(R.drawable.icon_pay_now_blue));
                } else if (!"PX".equals(installment.getCodigoTipoModalidadeCobranca())) {
                    ivExtends.setImageDrawable(getDrawable(R.drawable.icon_pay_now_yellow));
                } else {
                    ivExtends.setImageDrawable(getDrawable(R.drawable.icon_pay_now_clean));
                }
                ivExtends.setTag(i);
                ivExtends.setVisibility(View.VISIBLE);
                break;
            case 8:
                ivExtends.setImageDrawable(getDrawable(R.drawable.icon_pay_now_yellow));
                ivExtends.setTag(i);
                ivExtends.setVisibility(View.VISIBLE);
                break;
        }
    }

    public void updateList(ArrayList<InstallmentsBeans> list) {
        this.list = list;
        this.notifyDataSetChanged();
    }

    public void openExtend(Context context, int index) {
        this.index = index;
        Intent it = new Intent(context, ExtendPagament.class);
        it.putExtra("contract", currentInsurances.getContract() + "");
        it.putExtra("installment", list.get(index).getNumber() + "");
        it.putExtra("issuance", this.issuance + "");
        it.putExtra("ciaCode", currentInsurances.getCiaCode() + "");
        it.putExtra("cliCode", currentInsurances.getCliCode() + "");

        boolean payment = list.get(index).getCodigoTipoModalidadeCobranca().equalsIgnoreCase("fb");

        it.putExtra("payment", payment);
        context.startActivity(it);
    }

}
