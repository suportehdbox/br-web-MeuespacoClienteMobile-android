package br.com.libertyseguros.mobile.view.fragment;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import com.google.android.material.snackbar.Snackbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.cardview.widget.CardView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.google.firebase.analytics.FirebaseAnalytics;


import br.com.libertyseguros.mobile.R;
import br.com.libertyseguros.mobile.controller.HomeOnController;
import br.com.libertyseguros.mobile.controller.ListVision360Controller;
import br.com.libertyseguros.mobile.controller.ParcelsController;
import br.com.libertyseguros.mobile.libray.Config;
import br.com.libertyseguros.mobile.libray.FingerprintsAndroid;
import br.com.libertyseguros.mobile.libray.LoadFile;
import br.com.libertyseguros.mobile.libray.PolicyCalc;
import br.com.libertyseguros.mobile.receiver.ServiceFirebaseMessagingService;
import br.com.libertyseguros.mobile.util.AnalyticsApplication;
import br.com.libertyseguros.mobile.util.ManagerDialogCodeBar;
import br.com.libertyseguros.mobile.util.NavigationApplication;
import br.com.libertyseguros.mobile.util.OnBarCode;
import br.com.libertyseguros.mobile.util.OnConnectionResult;
import br.com.libertyseguros.mobile.view.ExtendPagament;
import br.com.libertyseguros.mobile.view.custom.ButtonViewCustom;
import br.com.libertyseguros.mobile.view.custom.ImageViewCustom;
import br.com.libertyseguros.mobile.view.custom.NotificationSnackBar;
import de.hdodenhof.circleimageview.CircleImageView;

public class HomeOn extends Fragment implements View.OnClickListener, NavigationApplication.NavigationListener, OnBarCode {

    private View root;

    private ParcelsController parcelsController;

    private HomeOnController homeOnController;

    private ListVision360Controller listVision360Controller;

    private boolean value;

    private ImageViewCustom ivSinisitro;

    private ImageViewCustom ivWorkshop;

    private ImageViewCustom ivClub;

    private LinearLayout llLoading;

    private ScrollView svContent;

    private Dialog dialogMessage;

    private Dialog dialogMessageTwoButton;

    private Dialog dialogFingerprint;

    private TextView tvMessageDialog;

    private PolicyCalc policyCalc;

    private TextView tvPolicy;

    private TextView tvTitleDescription;

    private TextView tvDescription;

    private ImageView ivDescription;

    TextView tvTitleCircle;


    private LinearLayout containerPayments;
    private LinearLayout ll_loading_payments;

    private CardView vision360CardView;


    private ImageView image_arrow;

    private CircularProgressView circularProgressView_1;

    private CircularProgressView circularProgressView_2;

    private CircularProgressView circularProgressView_3;

    private TextView tvDateCb1;

    private TextView tvDateCb2;

    private TextView tvDateCb3;

    private TextView tvEmail;

    private TextView tvName;

    private TextView tv_expandPayments;

    private LinearLayout llCircle2;

    private LinearLayout llCircle3;

    private ButtonViewCustom btPolicyMore;

    private ButtonViewCustom btDetail;

    private CardView ivStatusClaim;


    private TextView tvStatus;

    private ImageView ivType;

    private FirebaseAnalytics mFirebaseAnalytics;

    private LayoutInflater layoutInflater;

    private LinearLayout llDetail;

    private int indexPayment;

    private boolean errorFinger;

    private FingerprintsAndroid fingerprintsAndroid;

    Toast toast;

    private BroadcastReceiver broadcastReceiver;

    private MenuItem menuWorkshop;

    private Drawable arrow_down;
    private Drawable arrow_up;

    public static HomeOn newInstance(MenuItem menuWorkshop) {

        HomeOn home = new HomeOn();
        home.setMenuWorkshop(menuWorkshop);

        return home;
    }


    public HomeOn() {
    }

    @Override
    public void onResume() {
        super.onResume();

        homeOnController.checkPasswordExpired(getActivity());

        NavigationApplication nav = (NavigationApplication) getActivity().getApplication();
        nav.setNavigationListener(this);
        ServiceFirebaseMessagingService.setOnNotificationListener(nav.getListener());

        if (indexPayment >= 0) {
            if (ExtendPagament.isExtends && homeOnController.getPolicyBeans().getPayment()[indexPayment].getInstallmentNumber() == ExtendPagament.numberFinish &&
                    homeOnController.getPolicyBeans().getInsurance().getContract().equalsIgnoreCase(ExtendPagament.contractFinish)) {

                root.findViewWithTag("ivIconExtends" + indexPayment).setVisibility(View.INVISIBLE);
                ((ImageViewCustom) root.findViewWithTag("ivIconPayment" + indexPayment)).setImageDrawable(getResources().getDrawable(R.drawable.icon_oval_payment_pending));
                ((TextView) root.findViewWithTag("tvValuePayment" + indexPayment)).setText(ExtendPagament.valueFinish);
                ((TextView) root.findViewWithTag("tvDatePayment" + indexPayment)).setText(policyCalc.getDateExtends(ExtendPagament.dataFinish, getActivity(), 1));
                indexPayment = -1;
            }
        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);
        root = inflater.inflate(R.layout.fragment_home_on, null, false);

        arrow_down = getActivity().getResources().getDrawable(R.drawable.arrow_down);
        arrow_up = getActivity().getResources().getDrawable(R.drawable.arrow_up_grey);

        fingerprintsAndroid = new FingerprintsAndroid(getActivity());

        layoutInflater = inflater;

        HomeOff.isHomeOff = false;
        indexPayment = -1;

        //        ExtendPagament.isExtends = false;

        AnalyticsApplication application = (AnalyticsApplication) getActivity().getApplication();
        mFirebaseAnalytics = application.getFirebaseAnalitycs();
        mFirebaseAnalytics.setCurrentScreen(getActivity(), "Home Logada", null);

        policyCalc = new PolicyCalc();

        vision360CardView = root.findViewById(R.id.card_vision);

        tvStatus = root.findViewById(R.id.tv_type);

        ivType = root.findViewById(R.id.iv_type);

        btPolicyMore = root.findViewById(R.id.bt_more_policy);
        btPolicyMore.setOnClickListener(this);

        btDetail = root.findViewById(R.id.bt_detail);
        btDetail.setOnClickListener(this);

        llDetail = root.findViewById(R.id.ll_detail);
        llDetail.setOnClickListener(this);

        tvPolicy = root.findViewById(R.id.tv_policy);

        tvTitleDescription =  root.findViewById(R.id.tv_title_description);

        tvDescription = root.findViewById(R.id.tv_description);

        ivDescription = root.findViewById(R.id.iv_description);

        containerPayments = root.findViewById(R.id.ll_container_payments);

        tvName = root.findViewById(R.id.tv_name);

        tvDateCb1 = root.findViewById(R.id.tv_date_cb_1);
        tvDateCb2 = root.findViewById(R.id.tv_date_cb_2);
        tvDateCb3 = root.findViewById(R.id.tv_date_cb_3);


        tvTitleCircle = root.findViewById(R.id.tv_title_circle_1);


        circularProgressView_1 = root.findViewById(R.id.cp_1);

        circularProgressView_2 = root.findViewById(R.id.cp_2);
        circularProgressView_3 =  root.findViewById(R.id.cp_3);

        //llCircle1 = (LinearLayout) root.findViewById(R.id.ll_circle_1);
        llCircle2 = root.findViewById(R.id.ll_circle_2);
        llCircle3 = root.findViewById(R.id.ll_circle_3);

        tv_expandPayments = root.findViewById(R.id.tv_expand_payments);

        ll_loading_payments = root.findViewById(R.id.ll_loading_payments);

        image_arrow = root.findViewById(R.id.iv_arrow_payments);

        tv_expandPayments.setOnClickListener(this);

        llLoading = root.findViewById(R.id.ll_loading);

        svContent = root.findViewById(R.id.sv_content);

        ivSinisitro = root.findViewById(R.id.iv_sinistro);
        ivSinisitro.setOnClickListener(this);
        ivSinisitro.setVisibility(View.GONE);

        ivClub = root.findViewById(R.id.iv_club);
        ivClub.setOnClickListener(this);

        ivWorkshop = root.findViewById(R.id.iv_workshop);
        ivWorkshop.setOnClickListener(this);

        ivStatusClaim = root.findViewById(R.id.cv_status_claim);

        vision360CardView.setOnClickListener(this);

        TextView tvButtonNews = root.findViewById(R.id.button_news);
        tvButtonNews.setPaintFlags(tvButtonNews.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        homeOnController = new HomeOnController(new OnConnectionResult() {
            @Override
            public void onError() {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            if (homeOnController.getTypeConnection() == 1) {
                                if (homeOnController.getTypeError() == 1) {
                                    dialogMessageTwoButton.show();
                                } else {
                                    if (homeOnController.getMessage().getMessage() != null) {
                                        tvMessageDialog.setText(homeOnController.getMessage().getMessage());
                                        dialogMessage.show();
                                    } else {
                                        dialogMessageTwoButton.show();
                                    }
                                }

                            } else {
                                btPolicyMore.setVisibility(View.INVISIBLE);
                            }

                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                });
            }

            @Override
            public void onSucess() {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            if (homeOnController.getTypeConnection() == 1) {


                                showLoading(false);

                                listVision360Controller = new ListVision360Controller(new OnConnectionResult() {
                                    @Override
                                    public void onError() {
                                        try{
                                            vision360CardView.setVisibility(View.INVISIBLE);
                                        }catch (Exception ex){
                                            //Log.i(Config.TAG, ex.toString());
                                        }
                                    }

                                    @Override
                                    public void onSucess() {
                                        try{
                                            if(listVision360Controller.getListVision360Beans() != null
                                                    && listVision360Controller.getListVision360Beans().isPossuiEvento()){
                                                getActivity().runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        vision360CardView.setVisibility(View.VISIBLE);
                                                    }
                                                });
                                            } else {
                                                getActivity().runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        vision360CardView.setVisibility(View.INVISIBLE);
                                                    }
                                                });
                                            }
                                        }catch (Exception ex){
                                            //Log.i(Config.TAG, ex.toString());
                                        }
                                    }
                                });

                                listVision360Controller.checkContentEvent(getActivity(), homeOnController.getPolicyBeans().getInsurance().getPolicy());


                                try {
                                    if (homeOnController.showFingerTips(getActivity())) {

                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                            if (fingerprintsAndroid.isHardwareOk()) {
                                                if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
                                                    ActivityCompat.requestPermissions(getActivity(),
                                                            new String[]{android.Manifest.permission.USE_FINGERPRINT},
                                                            1);
                                                } else {
                                                    startFingerPrints();
                                                }
                                            } else {
                                                //Log.i(Config.TAG, "Fingerprints not present");
                                            }


                                        }

                                    }
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }

//                                homeOnController.setSalesForceSDK(homeOnController.getPolicyBeans().getInsurance().getPolicy(),
//                                        homeOnController.getInfoUser().getCpfCnpj(), homeOnController.getPolicyBeans().getInsurance().getBranch());

                                LoadFile loadFile = new LoadFile();
                                loadFile.savePref(Config.TAGPOLICYNUMBER, homeOnController.getPolicyBeans().getInsurance().getPolicy() + "", Config.TAG, getActivity());


                                tvPolicy.setText(homeOnController.getPolicyBeans().getInsurance().getPolicy());
                                tvDescription.setText(homeOnController.getPolicyBeans().getInsurance().getItens()[0].getDescription());

                                circularProgressView_1.setProgress(policyCalc.getPoliycDateDiff(homeOnController.getPolicyBeans().getInsurance().getDataStartPolicy(),
                                        homeOnController.getPolicyBeans().getInsurance().getDataEndPolicy()));

                                tvDateCb1.setText(policyCalc.getDay());


                                try {
                                    if (Integer.parseInt(policyCalc.getDay()) > 1) {
                                        tvTitleCircle.setText(getString(R.string.missing));
                                    } else {
                                        tvTitleCircle.setText(getString(R.string.missing_2));
                                    }
                                } catch (Exception ex) {
                                    if (tvTitleCircle != null) {
                                        tvTitleCircle.setText(getString(R.string.missing));
                                    }
                                }


                                if (homeOnController.getPolicyBeans().getInsurance().getBranch().equals(Config.auto)) {
                                    tvTitleDescription.setText(getActivity().getString(R.string.car));
                                    ivDescription.setImageDrawable(getResources().getDrawable(R.drawable.bt_veichle_image));
                                    llCircle2.setVisibility(View.GONE);
                                    llCircle3.setVisibility(View.GONE);
                                    ivSinisitro.setVisibility(View.VISIBLE);
                                    ivWorkshop.setVisibility(View.VISIBLE);

                                    if(menuWorkshop != null){
                                        menuWorkshop.setVisible(true);
                                    }

                                } else if (homeOnController.getPolicyBeans().getInsurance().getBranch().equals(Config.life)) {
                                    tvTitleDescription.setText(getString(R.string.insured));
                                    ivDescription.setImageDrawable(getResources().getDrawable(R.drawable.icon_people));
                                    llCircle2.setVisibility(View.GONE);
                                    llCircle3.setVisibility(View.GONE);
                                    ivWorkshop.setVisibility(View.GONE);

                                    if(homeOnController.getPolicyBeans().getInsurance().isAllowPHS()){
                                        ivSinisitro.setVisibility(View.VISIBLE);
                                    }

                                    if(menuWorkshop != null){
                                        menuWorkshop.setVisible(false);
                                    }
                                } else {
                                    tvTitleDescription.setText(getString(R.string.address));
                                    ivDescription.setImageDrawable(getResources().getDrawable(R.drawable.icon_home));
                                    llCircle2.setVisibility(View.GONE);
                                    llCircle3.setVisibility(View.GONE);
                                    ivWorkshop.setVisibility(View.GONE);
                                    if(homeOnController.getPolicyBeans().getInsurance().isAllowPHS()){
                                        ivSinisitro.setVisibility(View.VISIBLE);
                                    }

                                    if(menuWorkshop != null){
                                        menuWorkshop.setVisible(false);
                                    }
                                }
//                                 if (homeOnController.getPolicyBeans().getBranch().equals(Config.auto)) {
//                                     circularProgressView_2.setProgress(policyCalc.getIPVA(homeOnController.licensePlate()));
//                                     tvDateCb2.setText(policyCalc.getDay());
//
//                                     circularProgressView_3.setProgress(policyCalc.getLinc(homeOnController.licensePlate()));
//                                     tvDateCb3.setText(policyCalc.getDay());
//                                 }


                                updatePaymentsView();

                                homeOnController.getListPolicy(getActivity(), true);
                                // homeOnController.getPolicyBeans().getClaim().setClaimType(50);

                                if (homeOnController.getPolicyBeans().getInsurance().getItens()[0].getClaim() == null) {
                                    ivStatusClaim.setVisibility(View.GONE);
                                } else {
                                    ivStatusClaim.setVisibility(View.VISIBLE);

                                    switch (homeOnController.getPolicyBeans().getInsurance().getItens()[0].getClaim().getStatusClaim()) {
                                        case 0:
                                            ivStatusClaim.setVisibility(View.GONE);
                                            break;
                                        case 10:
                                            ivType.setImageDrawable(getResources().getDrawable(R.drawable.status_10));
                                            tvStatus.setText(getResources().getString(R.string.status_10));
                                            break;
                                        case 20:
                                            ivType.setImageDrawable(getResources().getDrawable(R.drawable.status_20));
                                            tvStatus.setText(getResources().getString(R.string.status_20));
                                            break;
                                        case 30:
                                            ivType.setImageDrawable(getResources().getDrawable(R.drawable.status_30));
                                            tvStatus.setText(getResources().getString(R.string.status_30));
                                            break;
                                        case 40:
                                            ivType.setImageDrawable(getResources().getDrawable(R.drawable.status_40));
                                            tvStatus.setText(getResources().getString(R.string.status_40));
                                            break;
                                        case 50:
                                            ivType.setImageDrawable(getResources().getDrawable(R.drawable.status_50));
                                            tvStatus.setText(getResources().getString(R.string.status_50));
                                            break;
                                        case 90:
                                            tvStatus.setText(getResources().getString(R.string.status_90));
                                            break;
                                        case 100:
                                            ivType.setImageDrawable(getResources().getDrawable(R.drawable.status_100));
                                            tvStatus.setText(getResources().getString(R.string.status_100));
                                            break;
                                    }
                                }


                                int type = homeOnController.getPolicyBeans().getInsurance().getItens()[0].getClaim().getStatusClaim();

                                if (type == 10 || type == 40 || type == 100) {
                                    tvStatus.setTextColor(getResources().getColor(R.color.text_default_7));
                                } else if (type == 100) {
                                    tvStatus.setTextColor(getResources().getColor(R.color.text_default_5));
                                } else {
                                    tvStatus.setTextColor(getResources().getColor(R.color.text_default_4));
                                }

                                tvStatus.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        homeOnController.openVehicleAccidentStatus(getActivity());
                                    }
                                });

                                ivType.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        homeOnController.openVehicleAccidentStatus(getActivity());
                                    }
                                });

                                ivStatusClaim.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        homeOnController.openVehicleAccidentStatus(getActivity());
                                    }
                                });



                            } else if(homeOnController.getTypeConnection() == 3){
                                ll_loading_payments.setVisibility(View.GONE);
                                containerPayments.setVisibility(View.VISIBLE);
                                tv_expandPayments.setVisibility(View.VISIBLE);
                                image_arrow.setVisibility(View.VISIBLE);
                                updatePaymentsView();


                            } else {
                                if (homeOnController.getSizeListPolicy() > 1) {
                                    btPolicyMore.setVisibility(View.VISIBLE);
                                } else {
                                    btPolicyMore.setVisibility(View.INVISIBLE);
                                }
                            }


                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }



                    }
                });
            }

        }, getActivity());


        tvName = (TextView) root.findViewById(R.id.tv_name);
        tvName.setText(homeOnController.getInfoUser().getUserName());

        tvEmail = (TextView) root.findViewById(R.id.tv_email);
        tvEmail.setText(homeOnController.getInfoUser().getEmail());

        TextDrawable drawable1 = null;
        ImageView ivLetter = (ImageView) root.findViewById(R.id.iv_letter);

        try {
            drawable1 = TextDrawable.builder()
                    .buildRound(homeOnController.getInfoUser().getUserName().substring(0, 1), getResources().getColor(R.color.background_status_bar));
            ivLetter.setImageDrawable(drawable1);

        }catch (Exception ex){
            //Log.i(Config.TAG, ex.toString());
        }


        CircleImageView ci = root.findViewById(R.id.profile_image);

        if (homeOnController.getImageUser(getActivity(), ci)) {
            ci.setVisibility(View.VISIBLE);
            ivLetter.setVisibility(View.GONE);
        } else {
            ci.setVisibility(View.GONE);
            ivLetter.setVisibility(View.VISIBLE);
        }


        configDialog();

        showLoading(true);
        homeOnController.getHome(getActivity());

        llCircle2.setVisibility(View.GONE);
        llCircle3.setVisibility(View.GONE);

        NavigationApplication nav = (NavigationApplication) getActivity().getApplication();

        nav.setNavigationListener(this);


        IntentFilter filter = new IntentFilter();

        filter.addAction("br.com.libertyseguros.reloadScreen");

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                homeOnController.getHome(getActivity());
            }
        };

        getActivity().registerReceiver(broadcastReceiver, filter);




        return root;
    }




    private void updatePaymentsView(){
        if(homeOnController.getPolicyBeans().getPayment() == null){
            return;
        }
        containerPayments.removeAllViews();

        LinearLayout lastLine = null;
        for (int i = 0; i < homeOnController.getPolicyBeans().getPayment().length; i++) {
            View item_detail = layoutInflater.inflate(R.layout.item_payment_policy, null);
            containerPayments.addView(item_detail);

            TextView tvTitle = (TextView) item_detail.findViewById(R.id.tv_titlePayment);

            TextView tvDatePayment = (TextView) item_detail.findViewById(R.id.tv_date);
            tvDatePayment.setTag("tvDatePayment" + i);

            TextView tvValuePayment = (TextView) item_detail.findViewById(R.id.tv_value);
            tvValuePayment.setTag("tvValuePayment" + i);

            TextView titlePayment = (TextView) item_detail.findViewById(R.id.tv_title_ipayment);
            titlePayment.setTag("titlePayment" + i);

            ImageViewCustom ivIconPayment = (ImageViewCustom) item_detail.findViewById(R.id.iv_icon_payment);
            ivIconPayment.setTag("ivIconPayment" + i);

            TextView tv_detail = item_detail.findViewById(R.id.tv_view_details);
            tv_detail.setVisibility(View.VISIBLE);

            final int indexPayment = i;
            tv_detail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    homeOnController.openParcels(getActivity(), indexPayment);

                }
            });

            lastLine = item_detail.findViewById(R.id.ll_line_coverages);

            ButtonViewCustom btViewPayment = item_detail.findViewById(R.id.bt_view_payment);
            btViewPayment.setVisibility(View.GONE);

            ImageViewCustom ivIconExtends = item_detail.findViewById(R.id.iv_icon_extends);
            ivIconExtends.setTag("ivIconExtends" + i);

            ivIconExtends.setOnClickListener(HomeOn.this);

            tvDatePayment.setText(policyCalc.getDate(homeOnController.getPolicyBeans().getPayment()[i].getDueDate(), getActivity(), 1));

            tvValuePayment.setText(policyCalc.getMoney(homeOnController.getPolicyBeans().getPayment()[i].getValue(), getActivity()));


            switch (homeOnController.getPolicyBeans().getPayment()[i].getShowComponent()) {
                default:
                    ivIconExtends.setVisibility(View.INVISIBLE);
                    break;
                case 1:
                case 4:
                case 7:
                case 3:
                    ivIconExtends.setVisibility(View.VISIBLE);
                    ivIconExtends.setImageDrawable(getResources().getDrawable(R.drawable.icon_oval_pay_now));
                    break;
                case 2:
                case 5:
                case 6:
                    ivIconExtends.setVisibility(View.VISIBLE);
                    ivIconExtends.setImageDrawable(getResources().getDrawable(R.drawable.icon_oval_extends));
                    break;
            }


            if (homeOnController.getPolicyBeans().getPayment()[i].getInstallmentNumber() > 0 && homeOnController.getPolicyBeans().getPayment()[i].getAmountOfInstallment() > 1) {
                String text = "<b>" + getString(R.string.item_parcels) + " " + homeOnController.getPolicyBeans().getPayment()[i].getInstallmentNumber()
                        + " " + getString(R.string.in) + " " + homeOnController.getPolicyBeans().getPayment()[i].getAmountOfInstallment() + "</b>";

                tvTitle.setText(Html.fromHtml(text));
            } else {
                tvTitle.setText(getString(R.string.payment));
            }


            if (homeOnController.getPolicyBeans().getPayment()[i].getNextValue() != null) {

                if (!homeOnController.getPolicyBeans().getPayment()[i].getNextValue().equals("0.00") && homeOnController.getPolicyBeans().getPayment()[i].getNextDueDate() != null) {
                    String text = getString(R.string.next_payment) + "<b>" + " " +
                            policyCalc.getDate(homeOnController.getPolicyBeans().getPayment()[i].getNextDueDate(), getActivity(), 2) + "</b>" +
                            getString(R.string.next_payment_1) + " " + "<b>" + policyCalc.getMoney(homeOnController.getPolicyBeans().getPayment()[i].getNextValue(), getActivity()) + "</b>";

                    titlePayment.setText(Html.fromHtml(text));
                }

            }


            if (homeOnController.getPolicyBeans().getPayment()[i].getStatus() == 1) {
                if (homeOnController.getPolicyBeans().getPayment()[i].getNextValue() != null) {
                    ivIconPayment.setImageDrawable(getResources().getDrawable(R.drawable.icon_oval_payment_success));
                } else {
                    ivIconPayment.setImageDrawable(getResources().getDrawable(R.drawable.icon_oval_payment_finish));
                    titlePayment.setText(getResources().getString(R.string.parcel_finish));
                }
            } else if (homeOnController.getPolicyBeans().getPayment()[i].getStatus() == 2) {
                ivIconPayment.setImageDrawable(getResources().getDrawable(R.drawable.icon_oval_payment_late));
            } else if (homeOnController.getPolicyBeans().getPayment()[i].getStatus() == 3) {
                ivIconPayment.setImageDrawable(getResources().getDrawable(R.drawable.processamento));
            } else if (homeOnController.getPolicyBeans().getPayment()[i].getStatus() == 4) {
                ivIconPayment.setImageDrawable(getResources().getDrawable(R.drawable.analise));
            } else {
                ivIconPayment.setImageDrawable(getResources().getDrawable(R.drawable.icon_oval_payment_pending));
            }


        }
        if (lastLine != null) {
            lastLine.setVisibility(View.GONE);
        }
    }
    @Override
    public void onDestroyView() {
        // TODO Auto-generated method stub
        super.onDestroyView();


        android.app.Fragment fragment = getActivity().getFragmentManager()
                .findFragmentById(R.id.mapwhere);
        if (null != fragment) {
            android.app.FragmentTransaction ft = getActivity()
                    .getFragmentManager().beginTransaction();
            ft.remove(fragment);
            ft.commit();
        }

        getActivity().unregisterReceiver(broadcastReceiver);
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_example) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * On Click
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_workshop:
                homeOnController.openWorkShop(getActivity());
                break;
            case R.id.iv_club:
                homeOnController.openClub(getActivity());
                break;
            case R.id.iv_sinistro:
                homeOnController.openAssistance(getActivity());
                break;
            case R.id.bt_more_policy:
                homeOnController.openListPolicy(getActivity());
                break;
            case R.id.card_vision:
                homeOnController.openListVision(getActivity());
                break;
            case R.id.bt_detail:
            case R.id.ll_detail:
                homeOnController.openDetailList(getActivity());
                break;
            case R.id.tv_expand_payments:

                if(homeOnController.getPolicyBeans().getPayment() == null){
                    tv_expandPayments.setVisibility(View.GONE);
                    tv_expandPayments.setText(getString(R.string.collapse));
                    image_arrow.setVisibility(View.GONE);
                    image_arrow.setImageDrawable(arrow_up);
                    ll_loading_payments.setVisibility(View.VISIBLE);
                    homeOnController.getPaymentInfo(getActivity());
                    return;
                }

                if(containerPayments.getVisibility() == View.VISIBLE){
                    tv_expandPayments.setText(getString(R.string.expand));
                    containerPayments.setVisibility(View.GONE);
                    image_arrow.setImageDrawable(arrow_down);
                }else{
                    tv_expandPayments.setText(getString(R.string.collapse));
                    containerPayments.setVisibility(View.VISIBLE);
                    image_arrow.setImageDrawable(arrow_up);
                }

                break;
            case R.id.iv_icon_extends:
                indexPayment = Integer.parseInt(String.valueOf(v.getTag()).replace("ivIconExtends", ""));
                parcelsController = new ParcelsController(new OnConnectionResult() {
                    @Override
                    public void onError() {
                        showLoading(false);

                        if (parcelsController.getTypeError() == 1) {
                            tvMessageDialog.setText(getString(R.string.message_error_server));
                            dialogMessage.show();
                        } else {
                            tvMessageDialog.setText(parcelsController.getMessage().getMessage());
                            dialogMessage.show();
                        }

                    }

                    @Override
                    public void onSucess() {
                        try {
                            showLoading(false);
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ManagerDialogCodeBar managerDialogCodeBar = new ManagerDialogCodeBar(getActivity());
                                    managerDialogCodeBar.createDialog(parcelsController.getBarCodeBeans());
                                }
                            });

                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }, homeOnController.getPolicyBeans().getInsurance().getPolicy(),
                        homeOnController.getPolicyBeans().getInsurance().getContract(),
                        homeOnController.getPolicyBeans().getInsurance().getCiaCode(),
                        homeOnController.getPolicyBeans().getInsurance().getIssuances()[indexPayment] + "");

                homeOnController.openExtend(getActivity(), indexPayment, this);


                break;

        }
    }


    /**
     * Config Dialogs
     */
    private void configDialog() {

        dialogFingerprint = new Dialog(getActivity(), R.style.AppThemeDialog);
        dialogFingerprint.setCancelable(false);
        dialogFingerprint.setContentView(R.layout.dialog_fingerprints);

        TextView tvCancelFinger = (TextView) dialogFingerprint.findViewById(R.id.tv_cancel);

        tvCancelFinger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fingerprintsAndroid.cancel();
                dialogFingerprint.dismiss();
            }
        });


        dialogMessage = new Dialog(getActivity(), R.style.AppThemeDialog);
        dialogMessage.setCancelable(true);

        dialogMessage.setContentView(R.layout.dialog_message);

        tvMessageDialog = (TextView) dialogMessage.findViewById(R.id.tv_dialog_message);

        TextView tvOk = (TextView) dialogMessage.findViewById(R.id.tv_ok);

        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogMessage.dismiss();
                // getActivity().finish();

                showLoading(false);

                svContent.setVisibility(View.GONE);


            }
        });


        dialogMessageTwoButton = new Dialog(getActivity(), android.R.style.Theme_Translucent_NoTitleBar);

        dialogMessageTwoButton.setContentView(R.layout.dialog_message_two_button);

        TextView tvCancel = (TextView) dialogMessageTwoButton.findViewById(R.id.tv_cancel);

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogMessageTwoButton.dismiss();
                getActivity().finish();
            }
        });

        TextView tvTryAgain = (TextView) dialogMessageTwoButton.findViewById(R.id.tv_try_again);

        tvTryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogMessageTwoButton.dismiss();
                showLoading(true);
                homeOnController.getHome(getActivity());

            }
        });

    }

    public MenuItem getMenuWorkshop() {
        return menuWorkshop;
    }

    public void setMenuWorkshop(MenuItem menuWorkshop) {
        this.menuWorkshop = menuWorkshop;
    }

    /* Show progress loading
    * @param v
    * @param m
    */
    private void showLoading(boolean v) {
        this.value = v;

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (value) {
                    llLoading.setVisibility(View.VISIBLE);
                    svContent.setVisibility(View.GONE);
                } else {
                    llLoading.setVisibility(View.GONE);
                    svContent.setVisibility(View.VISIBLE);
                }
            }
        });

    }

    @Override
    public void OnNotificationReceived(String message) {
        CoordinatorLayout Clayout = (CoordinatorLayout) root.findViewById(R.id.snackbarlocation);
        NotificationSnackBar.showNotification(getContext().getApplicationContext(), Clayout, message);
    }

    @Override
    public void OnDataPlanChange(boolean freeNavigation) {
        String msg = getString(R.string.free_navigation);
        if (!freeNavigation) {
            //msg = getString(R.string.paid_navigation);
            return;
        }
        CoordinatorLayout Clayout = (CoordinatorLayout) root.findViewById(R.id.snackbarlocation);

        Snackbar snackbar = Snackbar.make(Clayout, msg, Snackbar.LENGTH_SHORT);
        snackbar.setDuration(4000);
        snackbar.show();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startFingerPrints();

                }
                return;
            }

        }
    }

    private void startFingerPrints() {
        dialogFingerprint.show();
        fingerprintsAndroid.start(new FingerprintsAndroid.OnFingerprintsListener() {
            @Override
            public void onError() {

                if (toast != null) {
                    toast.cancel();
                }
                toast = Toast.makeText(getActivity(), getString(R.string.message_fingertips_error), Toast.LENGTH_LONG);
                toast.show();
            }

            @Override
            public void onSucess() {

                dialogFingerprint.dismiss();

                if (toast != null) {
                    toast.cancel();
                }
                toast = Toast.makeText(getActivity(), getString(R.string.message_fingertips_success), Toast.LENGTH_LONG);
                toast.show();

                homeOnController.enableFingerprints(getActivity());
            }
        });

    }



    public void onBarCode(int parcelNumber) {
        showLoading(true);

        parcelsController.getCod(getActivity(), parcelNumber);
    }
}