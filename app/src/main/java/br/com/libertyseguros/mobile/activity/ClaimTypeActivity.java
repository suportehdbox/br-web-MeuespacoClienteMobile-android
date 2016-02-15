/*
 * Copyright (c) 2010, Liberty Mutual
 * Proprietary and Confidential
 * All Rights Reserved
 */

package br.com.libertyseguros.mobile.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import br.com.libertyseguros.mobile.R;
import br.com.libertyseguros.mobile.common.Util;
import br.com.libertyseguros.mobile.constants.Constants;
import br.com.libertyseguros.mobile.database.EventHelper;

/**
 * Provides the view for inputing detail information when the claim type other was selected.
 * 
 * @author N0053575 (Heidi Sturm)
 */
public class ClaimTypeActivity extends LibertyMobileApp implements DialogInterface.OnClickListener
{
	// private static final String TAG = ClaimTypeActivity.class.getName();
	private LinearLayout subTypeLayout;

	//    private EditText subTypeDetails;
	//
	//    private View detailLayout;

	private String currentlySelectedType;

	private static final int SINGLE_LIST_ITEM = 0;

	private static final int FIRST_LIST_ITEM = 1;

	private static final int MIDDLE_LIST_ITEM = 2;

	private static final int LAST_LIST_ITEM = 3;

	//    private static final String MISSING_TYPE_DETAILS = "MISSING_TYPE_DETAILS";

	private static final int CLAIM_TYPE_DETAILS_DIALOG = 0;

	private boolean  wantSave;

	/**
	 * This method will return true if the required fields are populated, otherwise false
	 * 
	 * @return
	 */
	private boolean checkMinReqs()
	{
		try {
			boolean minRequirementsMet = true;

			String missingField = null;

			if (currentlySelectedType == null)
			{
				missingField = getString(R.string.choose_claim_type);
			}
			// << EPO
			//        else if (getString(R.string.cbx_acidente_outros).equals(currentlySelectedType))
			//        {
			//            if (ValidationUtils.isStringEmpty(subTypeDetails.getText().toString()))
			//            {
			//                missingField = MISSING_TYPE_DETAILS;
			//            }
			//        }
			// >>

			if (!minRequirementsMet)
			{
				// Tell the user they haven't met the minimum requirements
				displayInfoAlert(	ClaimTypeActivity.this,
						getString(R.string.por_favor_insira),
						missingField, 
						getString(R.string.btn_ok),
						ClaimTypeActivity.this);
			}

			return minRequirementsMet;
		} catch (Exception e) {
			Util.showException(this, e);
			return false;
		}
	}

	// << EPO
//	/**
//	 * @param id
//	 * @return
//	 * @see android.app.Activity#onCreateDialog(int)
//	 */
//	@Override
//	protected Dialog onCreateDialog(int id)
//	{
//		try {
//			Dialog dialog = null;
//			switch (id)
//			{
//			case CLAIM_TYPE_DETAILS_DIALOG:
//				AlertDialog.Builder builder = new AlertDialog.Builder(this);
//				builder.setMessage("Please describe your type of claim.").setTitle("Claim Type")
//				.setPositiveButton(getString(R.string.btn_ok), new DialogInterface.OnClickListener()
//				{
//					public void onClick(DialogInterface dialog, int id)
//					{
//						dialog.cancel();
//					}
//				}).setCancelable(false);
//				dialog = builder.create();
//				break;
//			default:
//				break;
//			}
//			return dialog;
//		} catch (Exception e) {
//			Util.showException(this, e);
//			return null;
//		}
//	}
	//    /**
	//     * This method will handle a press of the done button
	//     * 
	//     * @param view
	//     */
	//    public void doneButtonPressed(View view)
	//    {
	//        String missingFields = checkMinReqs();
	//        if (missingFields == null)
	//        {
	//            saveForm();
	//            finish();
	//        }
	//        else
	//        {
	//            if (MISSING_TYPE_DETAILS.equals(missingFields))
	//            {
	//                showDialog(CLAIM_TYPE_DETAILS_DIALOG);
	//            }
	//            else
	//            {
	//                displayInfoAlert(ClaimTypeActivity.this, getString(R.string.required_info_missing), missingFields,
	//                    getString(R.string.ok), ClaimTypeActivity.this);
	//            }
	//        }
	//    }

	//    /**
	//     * @return
	//     */
	//    private AnimationListener getAnimationListener()
	//    {
	//        return new AnimationListener()
	//        {
	//            @Override
	//            public void onAnimationEnd(Animation anim)
	//            {
	//                detailLayout.setVisibility(View.GONE);
	//            }
	//
	//            @Override
	//            public void onAnimationRepeat(Animation anim)
	//            {
	//            }
	//
	//            @Override
	//            public void onAnimationStart(Animation anim)
	//            {
	//            }
	//        };
	//    }
	// >>

	/**
	 * Gets the list of claim sub types available for an auto claim
	 * 
	 * @return the list of claim types for an auto claim
	 */
	private List<String> getAutoClaimSubTypes()
	{
		try {
			// Log.v(TAG, ">>> getAutoClaimTypes()");

			List<String> types = new ArrayList<String>();
			types.add(getString(R.string.cbx_acidente_outro_veiculo));
			types.add(getString(R.string.cbx_acidente_terceiro));
			types.add(getString(R.string.cbx_acidente_capotamento));
			types.add(getString(R.string.cbx_acidente_alagamento));
			types.add(getString(R.string.cbx_acidente_furto_roubo));
			types.add(getString(R.string.cbx_acidente_outros));

			// Log.v(TAG, "<<< getAutoClaimTypes()");

			return types;
		} catch (Exception e) {
			Util.showException(this, e);
			return null;
		}
	}

	/**
	 * Get the list of claim sub types
	 * 
	 * @return
	 */
	private List<String> getClaimSubTypes()
	{
		try {
			List<String> subTypes = null;
			if (Constants.LOB_AUTO.equals(getCurrentEvent().getEventType()))
			{
				subTypes = getAutoClaimSubTypes();
			}
			//	 EPO esta versao somente sinistro de auto
			//        if (Constants.LOB_HOME.equals(getCurrentEvent().getEventType()))
			//        {
			//            subTypes = getHomeClaimSubTypes();
			//        }
			return subTypes;
		} catch (Exception e) {
			Util.showException(this, e);
			return null;
		}
	}

	//	 EPO esta versao somente sinistro de auto
	//    /**
	//     * Gets the list of claim sub types available for a home claim
	//     * 
	//     * @return the list of claim types for a home claim
	//     */
	//    private List<String> getHomeClaimSubTypes()
	//    {
	//        // Log.v(TAG, ">>> getHomeClaimTypes()");
	//
	//        List<String> types = new ArrayList<String>();
	//        types.add(getString(R.string.water_ice_snow_damage));
	//        types.add(getString(R.string.wind_hurricane));
	//        types.add(getString(R.string.hail));
	//        types.add(getString(R.string.fire));
	//        types.add(getString(R.string.theft_vandalism));
	//        types.add(getString(R.string.cbx_acidente_outros));
	//
	//        // Log.v(TAG, "<<< getHomeClaimTypes()");
	//
	//        return types;
	//    }

	/**
	 * @param v
	 */
	private void handleSubTypeClick(View v)
	{
		try {
			String subType = ((TextView) v.findViewById(R.id.tv_sub_type_name)).getText().toString();
			if (!subType.equals(currentlySelectedType))
			{
				ViewGroup parent = (ViewGroup) v.getParent();
				for (int i = 0; i < parent.getChildCount(); i++)
				{
					View child = parent.getChildAt(i);
					ImageView rb = (ImageView) child.findViewById(R.id.iv_radio_button);
					if (v.equals(child))
					{
						rb.setImageResource(R.drawable.check_on);
					}
					else
					{
						rb.setImageResource(R.drawable.check_off);
					}
				}

				// << EPO esta versao n�o permite inserir outro tipo de acidente
				//            if (getString(R.string.cbx_acidente_outros).equals(subType))
				//            {
				//                Animation slideDown = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_down);
				//                detailLayout.setVisibility(View.VISIBLE);
				//                detailLayout.startAnimation(slideDown);
				//            }
				//            else if (getString(R.string.cbx_acidente_outros).equals(currentlySelectedType))
				//            {
				//                Animation slideUp = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up);
				//                slideUp.setAnimationListener(getAnimationListener());
				//                detailLayout.startAnimation(slideUp);
				//            }
				// >>

				currentlySelectedType = subType;
			}
		} catch (Exception e) {
			Util.showException(this, e);
		}
	}

	/**
	 * This method will return true if the currently selected values are different than the values stored in the
	 * database.
	 * 
	 * @return
	 */
	private boolean isFormChanged()
	{
		try {
			boolean returnVal = false;
			String origType = getCurrentEvent().getEventSubType();
			if (origType != null)
			{
				if (!origType.equals(currentlySelectedType))
				{
					returnVal = true;
				}
				// << EPO
				//            else if (origType.equals(getString(R.string.cbx_acidente_outros)))
				//            {
				//                String origDetails = getCurrentEvent().getEventSubTypeDetails();
				//                String newDetails = ((EditText) findViewById(R.id.et_claim_type_detail)).getText().toString();
				//
				//                if (origDetails != null)
				//                {
				//                    if (!origDetails.equals(newDetails))
				//                    {
				//                        returnVal = true;
				//                    }
				//                }
				//                else if (newDetails == null)
				//                {
				//                    returnVal = true;
				//                }
				//            }
				// >>
			}
			else if (currentlySelectedType != null)
			{
				returnVal = true;
			}

			return returnVal;
		} catch (Exception e) {
			Util.showException(this, e);
			return false;
		}
	}

	/**
	 * @see android.app.Activity#onBackPressed()
	 */
	@Override
	public void onBackPressed()
	{
		try {
			if (isFormChanged()) {
				// << EPO
				//            displayConfirmAlert(ClaimTypeActivity.this, getString(R.string.go_back_without_saving),
				//                getString(R.string.go_back_without_saving_confirmation), getString(R.string.btn_ok),
				//                getString(R.string.no), ClaimTypeActivity.this);

				if(wantSave) {
					salvaEfecha(null);
				} else{
					// Ask the user if they want to discard their changes
					displayConfirmAlert(ClaimTypeActivity.this,
							getString(R.string.aviso),
							getString(R.string.deseja_salvar_alteracoes),
							getString(R.string.btn_ok),
							getString(R.string.btn_cancelar),
							ClaimTypeActivity.this);
				}
				// >>

			} else {
				finish();
			}
		} catch (Exception e) {
			Util.showException(this, e);
		}
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @see DialogInterface.OnClickListener#onClick(DialogInterface, int)
	 */
	@Override
	public void onClick(DialogInterface dialog, int which)
	{
		try {
			// The user selected yes 
			if (which == -1) {
				wantSave = true;
				salvaEfecha(null);
			} else if (which == -2) {
				// Finish the activity
				finish();
			}
		} catch (Exception e) {
			Util.showException(this, e);
		}
	}

	/**
	 * Initializes the view, setting the layout to the claim type layout, with a dialog theme and the background
	 * blurred. Also adds listeners to the buttons in the view.
	 * 
	 * @param savedInstanceState
	 *            if the activity is being re-initialized after previously being shut down then this Bundle contains the
	 *            data it most recently supplied in {@link Activity.onSaveInstanceState(Bundle)}. Note: Otherwise it is
	 *            null.
	 * @see com.lmig.pm.internet.mobile.android.libertymutual.LibertyMutualActivity#onCreate(Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		try {
			// Log.v(TAG, ">>> onCreate(Bundle savedInstanceState)");

			// Create the view with the claim detail layout
			super.onCreate(savedInstanceState);

			setContentView(R.layout.activity_claim_type);

			setUpNavigationBarTitleOnly(getString(R.string.choose_claim_type));

			if (savedInstanceState != null)
			{
				currentlySelectedType = savedInstanceState.getString("selectedValue");
			}
			else
			{
				currentlySelectedType = getCurrentEvent().getEventSubType();
			}

			subTypeLayout = (LinearLayout) findViewById(R.id.ll_claim_type_list);
			populateSubTypes(subTypeLayout, getClaimSubTypes());

			// << EPO
			//        detailLayout = findViewById(R.id.rl_type_text);
			//
			//        subTypeDetails = (EditText) findViewById(R.id.et_claim_type_detail);
			//        if (getString(R.string.cbx_acidente_outros).equals(currentlySelectedType))
			//        {
			//            subTypeDetails.setText(getCurrentEvent().getEventSubTypeDetails());
			//            detailLayout.setVisibility(View.VISIBLE);
			//        }
			//        else
			//        {
			//            detailLayout.setVisibility(View.GONE);
			//        }
			//        if (Constants.LOB_HOME.equals(getCurrentEvent().getEventType()))
			//        {
			//            subTypeDetails.setHint(getString(R.string.claim_type_detail_hint_home));
			//        }
			//        if (Constants.EVENT_STATUS_SUBMITTED.equals(getCurrentEvent().getEventStatus()))
			//        {
			//            FieldUtils.disableEditText(subTypeDetails);
			//        }
			// >>

			// Log.v(TAG, "<<< onCreate(Bundle savedInstanceState)");
		} catch (Exception e) {
			Util.showException(this, e);
		}
	}

	/**
	 * @param outState
	 * @see android.app.Activity#onSaveInstanceState(Bundle)
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState)
	{
		try {
			// Log.v(TAG, ">>> onSaveInstanceState()");

			outState.putString("selectedValue", currentlySelectedType);

			super.onSaveInstanceState(outState);

			// Log.v(TAG, "<<< onSaveInstanceState()");
		} catch (Exception e) {
			Util.showException(this, e);
		}

	}

	/**
	 * @param layout
	 * @param driver
	 */
	private void populateLayoutFrom(LinearLayout layout, String display, boolean isSelected, int itemType)
	{
		try {
			LinearLayout itemLayout =
					(LinearLayout) LayoutInflater.from(getApplicationContext()).inflate(R.layout.claim_sub_type_item, layout,
							false);

			View visibleLayout = null;
			switch (itemType)
			{
			case SINGLE_LIST_ITEM:
				visibleLayout = itemLayout.findViewById(R.id.ll_claim_sub_type_single);
				break;
			case FIRST_LIST_ITEM:
				visibleLayout = itemLayout.findViewById(R.id.ll_claim_sub_type_first);
				break;
			case MIDDLE_LIST_ITEM:
				visibleLayout = itemLayout.findViewById(R.id.ll_claim_sub_type);
				break;
			case LAST_LIST_ITEM:
				visibleLayout = itemLayout.findViewById(R.id.ll_claim_sub_type_last);
				break;
			default:
				break;
			}
			// We really don't want to add the gone views, so we remove them all, and just add the one we care about.
			itemLayout.removeAllViews();

			visibleLayout.setVisibility(View.VISIBLE);

			TextView subTypeTextView = (TextView) visibleLayout.findViewById(R.id.tv_sub_type_name);
			subTypeTextView.setText(display);

			ImageView subTypeRadioButton = (ImageView) visibleLayout.findViewById(R.id.iv_radio_button);
			if (isSelected)
			{
				subTypeRadioButton.setImageResource(R.drawable.check_on);
			}
			else
			{
				subTypeRadioButton.setImageResource(R.drawable.check_off);
			}

			if (Constants.EVENT_STATUS_DRAFT.equals(getCurrentEvent().getEventStatus()))
			{
				visibleLayout.setOnClickListener(new OnClickListener()
				{
					@Override
					public void onClick(View v)
					{
						handleSubTypeClick(v);
					}
				});
			}
			layout.addView(visibleLayout);
		} catch (Exception e) {
			Util.showException(this, e);
		}
	}

	/**
	 * This method will populate a linear layout with the contacts in the array list
	 * 
	 * @param layout
	 * @param contacts
	 */
	private void populateSubTypes(LinearLayout layout, List<String> subTypes)
	{
		try {
			layout.removeAllViews();
			Iterator<String> subTypeIterator = subTypes.iterator();
			int count = 1;
			while (subTypeIterator.hasNext())
			{
				String type = subTypeIterator.next();
				int itemType;
				if (subTypes.size() == 1)
				{
					itemType = SINGLE_LIST_ITEM;
				}
				else if (subTypes.size() == count)
				{
					itemType = LAST_LIST_ITEM;
				}
				else if (count == 1)
				{
					itemType = FIRST_LIST_ITEM;
				}
				else
				{
					itemType = MIDDLE_LIST_ITEM;
				}

				boolean isChecked = false;
				if (type.equals(currentlySelectedType))
				{
					isChecked = true;
				}
				populateLayoutFrom(layout, type, isChecked, itemType);
				count++;
			}
		} catch (Exception e) {
			Util.showException(this, e);
		}
	}

	/**
	 * This method will persist the selected data to the database.
	 */
	private void saveForm()
	{
		try {
			getCurrentEvent().setEventSubType(currentlySelectedType);

			// << EPO
			//        if (getString(R.string.cbx_acidente_outros).equals(currentlySelectedType))
			//        {
			//            getCurrentEvent().setEventSubTypeDetails(subTypeDetails.getText().toString());
			//        }
			//        else
			//        {
			//            getCurrentEvent().setEventSubTypeDetails(null);
			//        }
			// >>

			EventHelper.update(this, getCurrentEvent());
		} catch (Exception e) {
			Util.showException(this, e);
		}
	}

	/**
	 * Quando clicka em salvar (button_bar.xml).
	 * Quando clicka em voltar e deseja salvar.
	 * 
	 * @param v
	 */
	public void salvaEfecha(View v)
	{
		try {
			if (checkMinReqs())
			{
				saveForm();
				setResult(RESULT_OK);
				finish();
			}
		} catch (Exception e) {
			Util.showException(this, e);
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Util.callGB();
	}
}
