package br.com.MondialAssistance.Liberty.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import br.com.MondialAssistance.Liberty.BLL.BLLAutomaker;
import br.com.MondialAssistance.Liberty.BLL.BLLAutomotive;
import br.com.MondialAssistance.Liberty.BLL.BLLClient;
import br.com.MondialAssistance.Liberty.BLL.BLLDirectAssist;
import br.com.MondialAssistance.Liberty.BLL.BLLProperty;
import br.com.MondialAssistance.Liberty.MDL.Action;
import br.com.MondialAssistance.Liberty.MDL.AddressLocation;
import br.com.MondialAssistance.Liberty.MDL.Case;
import br.com.MondialAssistance.Liberty.MDL.ListItem;
import br.com.MondialAssistance.Liberty.Params.ClientParams;
import br.com.MondialAssistance.Liberty.R;
import br.com.MondialAssistance.Liberty.Util.Client;
import br.com.MondialAssistance.Liberty.Util.Utility;
import br.com.MondialAssistance.Liberty.analytics.AnalyticsApplication;
import br.com.MondialAssistance.Liberty.common.CustomApplication;

//import br.com.MondialAssistance.DirectAssist.Util.GoogleAnalytics;

public class ScreenNewAssistance extends Activity implements Runnable {

	protected static final int POLICY = 1;
	protected static final int CAUSE = 2;
	protected static final int PROBLEM = 3;
	protected static final int SERVICE = 4;
	protected static final int LOCATION = 5;
	protected static final int REFERENCE = 6;
	protected static final int SCHEDULE = 7;
	protected final static int DATE_BEGIN = 1;
	protected final static int TIME_BEGIN = 2;
	protected final static int TIME_END = 3;
	final BLLClient client = new BLLClient();
	private int LobID;
	private String PolicyID;
	private String CauseID;
	private Integer ProblemID;
	private String ServiceID;
	private AddressLocation addressLocation;
	private Calendar scheduleStartDate;
	private Calendar scheduleEndDate;
	private Case caseFile = null;
	private ListItem causeSelectedItem, serviceSelectedItem,
			problemSelectedItem;
	private ArrayList<ListItem> items;
	private final int CAUSES_CHECKBOX_ID = 100;
	private final int SERVICES_CHECKBOX_ID = 200;
	private final int PROBLEMS_CHECKBOX_ID = 300;
	// private CtrlListViewListNewAssistanceAdapter
	// adapterCauseListNewAssistance,adapterServiceListNewAssistance;
	ArrayList<ListItem> causeItems, serviceItems, problemItems;
	LinearLayout listCauseAssistance, listServiceAssistance,
			listProblemAssistance, problemLayout;
	private boolean insertProblems = false;
	private Thread thread;
	private EditText referenceEditText;
	private TextView viewScreenName, addressTextview;
	// private ListView listCauseAssistance1,listServiceAssistance;
	private Button btnCreateFile, viewMap, btnCallForAssistance24h;
	RelativeLayout headerCancelButton;
	private ProgressDialog progress;
	private LinearLayout schedulingLayout;
	private Button btnDateBegin;
	private Button btnTimeBegin;
	private Button btnTimeEnd;
	private Button selectPolicy;
	private Calendar calendarBegin;
	private Calendar calendarEnd;
	private DatePickerDialog.OnDateSetListener dateListenerBegin;
	private TimePickerDialog.OnTimeSetListener timeListenerBegin;
	private TimePickerDialog.OnTimeSetListener timeListenerEnd;

	private Tracker mTracker;

	private void registerDateAndTimeListeners() {
		dateListenerBegin = new OnDateSetListener() {
			public void onDateSet(DatePicker view, int year, int monthOfYear,
					int dayOfMonth) {

				calendarBegin.set(year, monthOfYear, dayOfMonth);
				calendarEnd.set(year, monthOfYear, dayOfMonth);

				btnDateBegin.setText("" + dateDisplayFormat(calendarBegin));
				dismissDialog(DATE_BEGIN);
			}
		};

		timeListenerBegin = new OnTimeSetListener() {
			public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

				calendarBegin.set(Calendar.HOUR_OF_DAY, hourOfDay);
				calendarBegin.set(Calendar.MINUTE, minute);
				setData(SCHEDULE, null);
				btnTimeBegin.setText(Utility.getTime(calendarBegin));
				dismissDialog(TIME_BEGIN);

			}
		};

		timeListenerEnd = new OnTimeSetListener() {
			public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

				calendarEnd.set(Calendar.HOUR_OF_DAY, hourOfDay);
				calendarEnd.set(Calendar.MINUTE, minute);
				setData(SCHEDULE, null);
				btnTimeEnd.setText(Utility.getTime(calendarEnd));
				dismissDialog(TIME_END);

			}
		};
	}

	private void Initialize() {
		problemLayout = (LinearLayout) findViewById(R.id.problem_linear_layout);
		referenceEditText = (EditText) findViewById(R.id.referencias_edittext);
		View headerView = (View) findViewById(R.id.newAssistance_Header);
		headerCancelButton = (RelativeLayout) headerView
				.findViewById(R.id.btnBack);
		ImageView backIv = (ImageView) headerView
				.findViewById(R.id.backImageView);
		// headerCancelButton.setBackgroundResource(R.drawable.btn_cancelar);
		// headerCancelButton.setVisibility(View.VISIBLE);
		Intent intent = getIntent();
		LobID = intent.getExtras().getInt("LOBID");
		schedulingLayout = (LinearLayout) findViewById(R.id.agendar_relative_layout);
		if (LobID == Utility.PROPERTY) {
			schedulingLayout.setVisibility(View.VISIBLE);
		}
		viewMap = (Button) findViewById(R.id.vermapa_button);
		viewScreenName = (TextView) findViewById(R.id.viewScreenName);
		addressTextview = (TextView) findViewById(R.id.endereco_textview);
		if (LobID == Utility.PROPERTY) {
			viewScreenName.setText(R.string.TitleResidenciaNewAssistance);
		} else if (LobID == Utility.AUTOMOTIVE) {
			viewScreenName.setText(R.string.TitleAutoNewAssistance);
		}
		btnCallForAssistance24h = (Button) findViewById(R.id.callforAssistance_button);
		btnCreateFile = (Button) findViewById(R.id.btnCreateFile);
		selectPolicy = (Button) findViewById(R.id.selectApoliceButton);
		addressLocation = new AddressLocation();
		items = new ArrayList<ListItem>();
		ListItem item;

		item = new ListItem();
		item.setID(POLICY);
		item.setName((LobID == Utility.AUTOMOTIVE) ? getString(R.string.PolicyAutomotive)
				: (LobID == Utility.AUTOMAKER) ? getString(R.string.PolicyAutomotive)
						: getString(R.string.PolicyProperty));
		item.setDescription("");
		items.add(item);

		item = new ListItem();
		item.setID(CAUSE);
		item.setName(getString(R.string.Cause));
		item.setDescription("");
		items.add(item);

		item = new ListItem();
		item.setID(SERVICE);
		item.setName(getString(R.string.Service));
		item.setDescription("");
		items.add(item);

		item = new ListItem();
		item.setID(LOCATION);
		item.setName(getString(R.string.Location));
		item.setOrientationLine(LinearLayout.VERTICAL);
		// item.setDescription((new
		// StringBuilder().append(System.getProperty("line.separator"))
		// .append(System.getProperty("line.separator"))).toString());
		item.setDescription("");
		items.add(item);

		item = new ListItem();
		item.setID(REFERENCE);
		item.setName(getString(R.string.Reference));
		item.setOrientationLine(LinearLayout.VERTICAL);
		item.setDescription("");
		items.add(item);

		if (LobID == Utility.PROPERTY) {

			item = new ListItem();
			item.setID(SCHEDULE);
			item.setName(getString(R.string.Schedule));
			item.setOrientationLine(LinearLayout.VERTICAL);
			item.setDescription("");
			items.add(item);
		}
		causeItems = getCauses();
		serviceItems = getServices();
		problemItems = getProblems();
		listCauseAssistance = (LinearLayout) findViewById(R.id.causa_listview);
		listServiceAssistance = (LinearLayout) findViewById(R.id.service_listview);

		makeTheOptionsView(causeItems, listCauseAssistance, CAUSES_CHECKBOX_ID,
				CAUSE);
		makeTheOptionsView(serviceItems, listServiceAssistance,
				SERVICES_CHECKBOX_ID, SERVICE);

		Calendar Initial = Calendar.getInstance();
		Calendar End = Calendar.getInstance();

		Initial.set(Calendar.MINUTE, 0);
		Initial.set(Calendar.HOUR_OF_DAY, Initial.get(Calendar.HOUR_OF_DAY) + 1);
		calendarBegin = (scheduleStartDate != null) ? (Calendar) intent
				.getExtras().get("CALENDARBEGIN") : Initial;

		End.set(Calendar.MINUTE, 0);
		End.set(Calendar.HOUR_OF_DAY, End.get(Calendar.HOUR_OF_DAY) + 2);
		calendarEnd = (scheduleEndDate != null) ? (Calendar) intent.getExtras()
				.get("CALENDAREND") : End;

		setData(SCHEDULE, null);
		registerDateAndTimeListeners();
		btnDateBegin = (Button) findViewById(R.id.agenda_date__Button);
		btnDateBegin.setText("" + dateDisplayFormat(calendarBegin));

		btnTimeBegin = (Button) findViewById(R.id.agenda_startime_button);
		btnTimeBegin.setText(Utility.getTime(calendarBegin));

		btnTimeEnd = (Button) findViewById(R.id.agenda_endtime_Button);
		btnTimeEnd.setText(Utility.getTime(calendarEnd));

	}

	/*
	 * To display the date in Long format
	 */
	private String dateDisplayFormat(Calendar calendar) {

		Date date = new Date(calendar.getTimeInMillis());
		java.text.DateFormat spdf = SimpleDateFormat.getDateInstance(
				SimpleDateFormat.LONG, Locale.US);
		String dispDateFormat = spdf.format(date);
		// Log.e("",""+dispDateFormat);
		return dispDateFormat;
	}

	/*
	 * To add the cause and the service options to the layout
	 */
	private void makeTheOptionsView(final ArrayList<ListItem> listItems,
			final LinearLayout rootView, final int checkboxId, final int lobId) {
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		for (int i = 0; i < listItems.size(); i++) {
			final ListItem optionItem = listItems.get(i);
			View convertView = inflater.inflate(
					R.layout.ctrl_listview_listnewassistance, null);
			TextView viewLegField1 = (TextView) convertView
					.findViewById(R.id.viewLegField1);
			viewLegField1.setText(optionItem.getName());
			CheckBox checkBox = (CheckBox) convertView
					.findViewById(R.id.checkBoxId);
			checkBox.setTag("" + listItems.get(i).getName());
			checkBox.setId(checkboxId + i);
			checkBox.setOnClickListener(new OnClickListener() {
				public void onClick(View view) {
					// TODO Auto-generated method stub
					CheckBox cBox = (CheckBox) view;

					/*
					 * For Handling the conditions when any of cause is
					 * deselected
					 */
					if (lobId == CAUSE /*
										 * && problemLayout.getVisibility() ==
										 * View.VISIBLE
										 */) {
						problemLayout.setVisibility(View.GONE);
						insertProblems = false;
						// listServiceAssistance.removeAllViews();
						// refreshListViews(serviceItems, listServiceAssistance,
						// SERVICES_CHECKBOX_ID, SERVICE);
						setData(CAUSE, null);
						// setData(SERVICE, null);
						// setData(PROBLEM, null);
					}
					/*
					 * For Handling the conditions when any of service is
					 * deselected
					 */
					if (lobId == SERVICE) {
						setData(SERVICE, null);
					}
					/*
					 * For Handling the conditions when any of problem is
					 * deselected
					 */
					if (lobId == PROBLEM) {
						// setData(SERVICE, null);
						setData(PROBLEM, null);
					}
					if (!cBox.isChecked()) {
						cBox.setChecked(false);
					}
					if (cBox.isChecked()) {
						// layoutLine.setOrientation(item.getOrientationLine());
						setData(lobId, optionItem);

						cBox.setChecked(true);
						for (int i = checkboxId; i < checkboxId
								+ listItems.size(); i++) {
							if (cBox.getId() != i) {
								((CheckBox) rootView.findViewById(i))
										.setChecked(false);
							}
						}
					}
				}
			});

			rootView.addView(convertView);
		}
	}

	/*
	 * For getting the cause options
	 */
	private ArrayList<ListItem> getCauses() {
		// BLLClient client = new BLLClient();
		ArrayList<ListItem> causeItems = client.getCauses(LobID, getResources()
				.getStringArray(R.array.Causes));

		return causeItems;
	}

	/*
	 * For getting the service options
	 */
	private ArrayList<ListItem> getServices() {

		ArrayList<ListItem> serviceItems = client.getServices(
				LobID,
				CauseID,
				(CauseID == null ? getResources().getStringArray(
						R.array.ServiceAll) : getResources().getStringArray(
						R.array.Service)));
		return serviceItems;
	}

	/*
	 * For getting the address through Map
	 */
	private void getLocationThroughMap() {
		Intent intent = new Intent(ScreenNewAssistance.this,
				ScreenMapLocation.class);

		if (addressLocation.getStreetName() != null) {

			intent.putExtra("TYPE", 1);
			intent.putExtra("ADDRESS", addressLocation.getStreetName());
			intent.putExtra("DISTRICT", addressLocation.getDistrict());
			intent.putExtra("HOUSENUMBER", addressLocation.getHouseNumber());
			intent.putExtra("COMPLEMENT", addressLocation.getComplement());
			intent.putExtra("CITY", addressLocation.getCity());
			intent.putExtra("STATE", addressLocation.getState());
			intent.putExtra("ZIP", addressLocation.getZip());
			intent.putExtra("LATITUDE", addressLocation.getLatitude());
			intent.putExtra("LONGITUDE", addressLocation.getLongitude());

		} else
			intent.putExtra("TYPE", 0);

		startActivityForResult(intent, LOCATION);
	}

	/*
	 * For handling the click events
	 */
	private void Events() {
		selectPolicy.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(ScreenNewAssistance.this,
						ScreenListPolicies.class);
				intent.putExtra("LOBID", LobID);
				intent.putExtra("CLOSE", true);

				startActivityForResult(intent, POLICY);
			}
		});
		btnCallForAssistance24h.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"
						+ UtilityScreen.getAssistPhoneNumber(
								ScreenNewAssistance.this, LobID)));
				startActivity(intent);
			}
		});
		headerCancelButton.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		btnCreateFile.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				progress = ProgressDialog.show(ScreenNewAssistance.this,
						getText(R.string.Wait), getText(R.string.CreateCase),
						false, false);

				thread = new Thread(ScreenNewAssistance.this);
				thread.start();

			}
		});
		viewMap.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				getLocationThroughMap();
			}
		});

		btnDateBegin.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				showDialog(DATE_BEGIN);
			}
		});

		btnTimeBegin.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				showDialog(TIME_BEGIN);
			}
		});

		btnTimeEnd.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				showDialog(TIME_END);
			}
		});
	}

	public void run() {

		try {

			if (getCheckAllFieldFilled() == true) {

				String phone = Client.getPhone(getApplicationContext());
				int phoneAreaCode = Client.getPhoneAreaCode(phone);
				int phoneNumber = Client.getPhoneNumber(phone);
				setReferenceData();
				Action action = null;
				// Log.e("","Lob id  is  --- "+LobID);
				switch (LobID) {

				case Utility.AUTOMOTIVE:
					BLLAutomotive automotive = new BLLAutomotive();

					caseFile = automotive.CreateCase(ScreenNewAssistance.this,
							PolicyID, phoneAreaCode, phoneNumber, CauseID,
							ServiceID, ProblemID, addressLocation.getCity()
									.toUpperCase(), addressLocation.getState()
									.toUpperCase(), addressLocation
									.getStreetName(), addressLocation
									.getHouseNumber(), addressLocation
									.getLatitude(), addressLocation
									.getLongitude(), ClientParams.ClientID);
					action = automotive.getAction();

					break;
				case Utility.AUTOMAKER:
					BLLAutomaker automaker = new BLLAutomaker();

					caseFile = automaker.CreateCase(ScreenNewAssistance.this,
							PolicyID, phoneAreaCode, phoneNumber, CauseID,
							ServiceID, ProblemID, addressLocation.getCity()
									.toUpperCase(), addressLocation.getState()
									.toUpperCase(), addressLocation
									.getStreetName(), addressLocation
									.getHouseNumber(), addressLocation
									.getLatitude(), addressLocation
									.getLongitude(), ClientParams.ClientID);
					action = automaker.getAction();

					break;
				case Utility.PROPERTY:
					BLLProperty property = new BLLProperty();

					caseFile = property.CreateCase(ScreenNewAssistance.this,
							PolicyID, phoneAreaCode, phoneNumber, CauseID,
							ServiceID, ProblemID, addressLocation.getCity()
									.toUpperCase(), addressLocation.getState()
									.toUpperCase(), addressLocation
									.getStreetName(), addressLocation
									.getHouseNumber(), addressLocation
									.getLatitude(), addressLocation
									.getLongitude(), ClientParams.ClientID);
					action = property.getAction();

					break;
				}

				String complement = addressLocation.getComplement() == null ? ""
						: addressLocation.getComplement();
				String reference = addressLocation.getReference() == null ? ""
						: addressLocation.getReference();

				if (action.getResultCode() == 0) {

					setProgressMessage(getString(R.string.CheckCoverages));
					BLLDirectAssist directAssist = new BLLDirectAssist();

					directAssist.CheckCoverages(ScreenNewAssistance.this,
							caseFile.getCaseNumber(), PolicyID, phoneAreaCode,
							phoneNumber, CauseID, ServiceID, ProblemID,
							addressLocation.getCity().toUpperCase(),
							addressLocation.getState().toUpperCase(),
							addressLocation.getStreetName(),
							addressLocation.getHouseNumber(),
							addressLocation.getLatitude(),
							addressLocation.getLongitude(),
							String.format("%s %s", complement, reference),
							ClientParams.ClientID);
					action = directAssist.getAction();

					if (action.getResultCode() == 0) {

						setProgressMessage(getString(R.string.CreateServiceDispatch));

						directAssist.CreateServiceDispatch(
								ScreenNewAssistance.this, caseFile
										.getCaseNumber(), PolicyID,
								phoneAreaCode, phoneNumber, CauseID, ServiceID,
								ProblemID, addressLocation.getCity()
										.toUpperCase(), addressLocation
										.getState().toUpperCase(),
								addressLocation.getStreetName(),
								addressLocation.getHouseNumber(),
								addressLocation.getLatitude(), addressLocation
										.getLongitude(), String.format("%s %s",
										complement, reference), addressLocation
										.getDistrict().toUpperCase(), Utility
										.getDateTime(scheduleStartDate,
												Utility.FORMATDATE_YYYYMMDD),
								Utility.getDateTime(scheduleEndDate,
										Utility.FORMATDATE_YYYYMMDD),
								ClientParams.ClientID);

						action = directAssist.getAction();

						if (action.getResultCode() == 0) {

							runOnUiThread(new Runnable() {
								public void run() {

									setMessage(
											getString(
													R.string.CreateFileSuccess)
													.replace(
															"[FILE]",
															caseFile.getCaseNumber()
																	.toString()),
											getString(R.string.CreateFileSuccessHelper),
											true);
								}
							});
						}
					}
				}

				if (action.getResultCode() != 0) {

					runOnUiThread(new Runnable() {
						public void run() {

							setMessage(getString(R.string.Error),
									getString(R.string.ErrorMessage), false);
						}
					});
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			runOnUiThread(new Runnable() {
				public void run() {

					setMessage(getString(R.string.Error),
							getString(R.string.ErrorMessage), false);
				}
			});

		} finally {
			progress.dismiss();
		}
	}

	private ArrayList<ListItem> getProblems() {
		ArrayList<ListItem> probItems = client.getProblems(CauseID,
				getResources().getStringArray(R.array.Problem));
		return probItems;
	}

	public void setCauseSelectedItem(ListItem item) {
		causeSelectedItem = item;

	}

	public ListItem getCauseSelectedItem() {

		return causeSelectedItem;
	}

	public void setServiceSelectedItem(ListItem item) {
		serviceSelectedItem = item;

	}

	public ListItem getServiceSelectedItem() {

		return serviceSelectedItem;
	}

	public ListItem getProblemSelectedItem() {

		return problemSelectedItem;
	}

	public void setProblemSelectedItem(ListItem item) {
		problemSelectedItem = item;

	}

	private void setData(int id, ListItem item) {
		switch (id) {
		case POLICY:
			break;
		case CAUSE:
			setCauseSelectedItem(item);
			setCauseData();
			break;
		case PROBLEM:
			setProblemSelectedItem(item);
			setProblemData();
			break;
		case SERVICE:
			setServiceSelectedItem(item);
			setServiceData();
			break;
		case SCHEDULE:
			setScheduleData();
			break;
		default:
			break;
		}
	}

	private void setScheduleData() {
		scheduleStartDate = calendarBegin;
		scheduleEndDate = calendarEnd;
		if (!validateScheduleCheck()) {
			items.get(6 - ((insertProblems == true) ? 0 : 1)).setDescription(
					new StringBuilder(Utility.getDateTime(scheduleStartDate,
							Utility.FORMATDATE_DDMMYYYY)).append(" ")
							.append(getString(R.string.ScheduleTo)).append(" ")
							.append(Utility.getTime(scheduleEndDate))
							.toString());
		}
	}

	private void setCauseData() {
		if (getCauseSelectedItem() != null) {
			CauseID = getCauseSelectedItem().getCod();
			String cause_name = getCauseSelectedItem().getName();
			// Log.e("","Cause cod and name are -- "+CauseID+" -- "+cause_name);
			items.get(1).setDescription(cause_name);
			// Log.e("","The size of the problem array is  --- "+getProblems().size());
			if (getProblems().size() > 0) {

				if (insertProblems == false) {

					ListItem item = new ListItem();
					item.setID(PROBLEM);
					item.setName(getString(R.string.Problem));
					item.setDescription("");
					items.add(2, item);

					listProblemAssistance = (LinearLayout) findViewById(R.id.problem_listview);
					if (problemLayout.getVisibility() != View.VISIBLE) {
						problemLayout.setVisibility(View.VISIBLE);
						listProblemAssistance.removeAllViews();
						makeTheOptionsView(getProblems(),
								listProblemAssistance, PROBLEMS_CHECKBOX_ID,
								PROBLEM);
					}
					insertProblems = true;
				}

			} else if (insertProblems == true) {
				// Log.e("","Insert problems is true but dnt knw when it comes here");
				ListItem item = client.getDefaultServices(
						LobID,
						CauseID,
						getResources().getStringArray(R.array.Service),
						getResources().getStringArray(
								R.array.CausesDefaultService));
				// Log.e("","The iem came form the problem is --- "+item.getCod()+"--"+item.getName());

				if (item != null) {

					ServiceID = item.getCod();
					String service = item.getName();

					items.get(3 - ((insertProblems == true) ? 0 : 1))
							.setDescription(service);
				}

				items.remove(2);
				ProblemID = null;
				insertProblems = false;

			} else {

				ListItem item = client.getDefaultServices(
						LobID,
						CauseID,
						getResources().getStringArray(R.array.Service),
						getResources().getStringArray(
								R.array.CausesDefaultService));

				if (item != null) {

					ServiceID = item.getCod();
					String service = item.getName();
					ArrayList<ListItem> tempServiceItems = new ArrayList<ListItem>();
					tempServiceItems.add(item);
					/*
					 * for making the valid service item afer selecting the
					 * problem
					 */
					listServiceAssistance.removeAllViews();
					refreshListViews(tempServiceItems, listServiceAssistance,
							SERVICES_CHECKBOX_ID, SERVICE);
					CheckBox cBox = (CheckBox) listServiceAssistance
							.findViewWithTag(item.getName());
					setData(SERVICE, item);
					cBox.setChecked(true);
					cBox.setClickable(false);
					items.get(3 - ((insertProblems == true) ? 0 : 1))
							.setDescription(service);
				}
			}
		} else {
			/*
			 * This is handled when user deselects the option
			 */
			CauseID = "";
			items.get(3 - ((insertProblems == true) ? 0 : 1))
					.setDescription("");
		}
	}

	private void setServiceData() {
		if (getServiceSelectedItem() != null) {
			ServiceID = getServiceSelectedItem().getCod();
			String service = getServiceSelectedItem().getName();
			// Log.e("","Service cod and name are -- "+ServiceID+" -- "+service);
			items.get(3 - ((insertProblems == true) ? 0 : 1)).setDescription(
					service);
		} else {
			ServiceID = "";
			items.get(3 - ((insertProblems == true) ? 0 : 1))
					.setDescription("");
		}

	}

	private void setReferenceData() {
		String referenceString = (referenceEditText.getText().toString());
		// Log.e("","Reference data is  --- "+referenceString);
		addressLocation.setReference(referenceString);

		items.get(5 - ((insertProblems == true) ? 0 : 1)).setDescription(
				addressLocation.getReference());

	}

	private void setProblemData() {
		if (getProblemSelectedItem() != null) {
			ProblemID = getProblemSelectedItem().getID();
			String problem_name = getProblemSelectedItem().getName();
			// Log.e("","problem cod and name are -- "+ProblemID+" -- "+problem_name);
			items.get(2).setDescription(problem_name);

			ListItem item = client.getDefaultServices(LobID, CauseID, ProblemID
					.toString(),
					getResources().getStringArray(R.array.Service),
					getResources()
							.getStringArray(R.array.ProblemDefaultService));

			if (item != null) {

				ServiceID = item.getCod();
				String service = item.getName();
				/*
				 * for making the valid service item afer selecting the problem
				 */
				listServiceAssistance.removeAllViews();
				refreshListViews(serviceItems, listServiceAssistance,
						SERVICES_CHECKBOX_ID, SERVICE);
				CheckBox cBox = (CheckBox) listServiceAssistance
						.findViewWithTag(item.getName());
				setData(SERVICE, item);
				cBox.setChecked(true);
				// cBox.setClickable(false);
				items.get(3 - ((insertProblems == true) ? 0 : 1))
						.setDescription(service);
			} else {

				/*
				 * for making to reset the services item is not valid
				 */
				refreshListViews(serviceItems, listServiceAssistance,
						SERVICES_CHECKBOX_ID, SERVICE);
				setData(SERVICE, null);
			}
		} else {
			ProblemID = 0;
			items.get(3 - ((insertProblems == true) ? 0 : 1))
					.setDescription("");
		}
	}

	private void refreshListViews(ArrayList<ListItem> refreshItems,
			LinearLayout layout, int cBoxId, int id) {
		layout.removeAllViews();
		makeTheOptionsView(refreshItems, layout, cBoxId, id);
	}

	private void setAddressData(Bundle params) {
		addressLocation.setStreetName(params.getString("ADDRESS"));
		addressLocation.setHouseNumber(params.getString("HOUSENUMBER"));
		addressLocation.setComplement(params.getString("COMPLEMENT"));
		addressLocation.setDistrict(params.getString("DISTRICT"));
		addressLocation.setCity(params.getString("CITY"));
		addressLocation.setState(params.getString("STATE"));
		addressLocation.setZip(params.getString("ZIP"));
		addressLocation.setLatitude(params.getDouble("LATITUDE"));
		addressLocation.setLongitude(params.getDouble("LONGITUDE"));
		// Log.e("","address is --- "+addressLocation.toString());
		addressTextview.setText("" + addressLocation.toString());
		items.get(4 - ((insertProblems == true) ? 0 : 1)).setDescription(
				addressLocation.toString());
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.screen_newassistance);

//		GoogleAnalytics.setAnalyticsTracker(this, ClientParams.ClientID);
		AnalyticsApplication analyticsApplication = AnalyticsApplication.getInstance();
		mTracker = analyticsApplication.getDefaultTracker(getApplication());

		Initialize();
		Events();
	}

	protected Dialog onCreateDialog(int id) {

		switch (id) {
		case DATE_BEGIN:
			return new DatePickerDialog(this, dateListenerBegin,
					calendarBegin.get(Calendar.YEAR),
					calendarBegin.get(Calendar.MONTH),
					calendarBegin.get(Calendar.DAY_OF_MONTH));

		case TIME_BEGIN:
			return new TimePickerDialog(this, timeListenerBegin,
					calendarBegin.get(Calendar.HOUR_OF_DAY),
					calendarBegin.get(Calendar.MINUTE), true);
		case TIME_END:
			return new TimePickerDialog(this, timeListenerEnd,
					calendarEnd.get(Calendar.HOUR_OF_DAY),
					calendarEnd.get(Calendar.MINUTE), true);
		}
		return null;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		Bundle params = (data == null) ? null : data.getExtras();

		if (params != null) {

			BLLClient client;

			switch (requestCode) {
			case POLICY:
				PolicyID = params.getString("POLICY");
				String field = params.getString("FIELD");

				items.get(0).setDescription(field);
				selectPolicy.setText("" + field);
				break;
			case LOCATION:

				setAddressData(params);

				break;

			case SCHEDULE:

				break;
			}
			// adapterListNewAssistance.notifyDataSetChanged();
		}
	}

	private void setProgressMessage(final String message) {

		runOnUiThread(new Runnable() {
			public void run() {

				progress.setMessage(message);
			}
		});
	}

	private boolean getCheckAllFieldFilled() {

		final StringBuilder field = new StringBuilder();

		if (PolicyID == null || PolicyID.equals("")) {

			field.append((LobID == Utility.AUTOMOTIVE) ? getString(R.string.PolicyAutomotive)
					: (LobID == Utility.AUTOMAKER) ? getString(R.string.PolicyAutomotive)
							: getString(R.string.PolicyProperty));
		}
		if (CauseID == null || CauseID.equals("")) {

			if (field.length() != 0)
				field.append(", ").append(System.getProperty("line.separator"));

			field.append(getString(R.string.Cause));
		}
		if (insertProblems == true && (ProblemID == null || ProblemID == 0)) {

			if (field.length() != 0)
				field.append(", ").append(System.getProperty("line.separator"));

			field.append(getString(R.string.Problem));
		}
		if (ServiceID == null || ServiceID.equals("")) {

			if (field.length() != 0)
				field.append(", ").append(System.getProperty("line.separator"));

			field.append(getString(R.string.Service));
		}

		if (addressLocation.getStreetName() == null) {

			if (field.length() != 0)
				field.append(", ").append(System.getProperty("line.separator"));

			field.append(getString(R.string.Location));
		}
		// Log.e("",""+CauseID+"---"+ServiceID+"---"+addressLocation.getStreetName()+""+ProblemID);
		// if (addressLocation.getReference() == null ||
		// addressLocation.getReference().equals("")){
		//
		// if (field.length() != 0)
		// field.append(", ")
		// .append(System.getProperty("line.separator"));
		//
		// field.append(getString(R.string.Reference));
		// }
		if (LobID == Utility.PROPERTY
				&& (scheduleStartDate == null || scheduleStartDate == null)) {

			if (field.length() != 0)
				field.append(", ").append(System.getProperty("line.separator"));

			field.append(getString(R.string.Schedule));
		}
		// Log.e("","Total data"+field .toString());

		if (field.length() == 0) {
			return true;
		} else {

			runOnUiThread(new Runnable() {
				public void run() {

					setMessage(
							getString(R.string.HelperCheckPreviousFieldFilled),
							field.toString(), false);
				}
			});

			return false;
		}
	}

	private void setMessage(final String title, final String message,
			final boolean finishActivity) {

		AlertDialog.Builder dialog = new AlertDialog.Builder(
				ScreenNewAssistance.this);
		dialog.setTitle(title);
		dialog.setMessage(message);
		dialog.setNeutralButton(R.string.OK,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {

						if (finishActivity)
							finish();
					}
				});
		dialog.show();
	}

	private boolean validateScheduleCheck() {

		if (calendarBegin.after(calendarEnd)) {

			AlertDialog.Builder alertDialog = new AlertDialog.Builder(
					ScreenNewAssistance.this);
			alertDialog.setTitle(R.string.ScheduleValidadeAlertTitle);
			alertDialog.setMessage(R.string.ScheduleValidadeDateAfter);
			alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
			alertDialog.setNeutralButton(R.string.OK, null);
			alertDialog.show();

			return false;
		}
		return true;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
//		GoogleAnalytics.stopAnalyticsTracker();
	}

	@Override
	protected void onResume() {
		super.onResume();

		Log.i("Google Analytics: ", "Assistência Automotiva: Solicitação");
		mTracker.setScreenName("Assistência Automotiva: Solicitação");
		mTracker.send(new HitBuilders.ScreenViewBuilder().build());

		CustomApplication.activityResumed();
	}

	@Override
	protected void onPause() {
		super.onPause();
		CustomApplication.activityPaused();
	}
}

/*
 * listAssistance.setOnItemClickListener(new OnItemClickListener() { public void
 * onItemClick(AdapterView<?> adapter, View view, int position, long ID) {
 * 
 * CtrlListViewListNewAssistanceAdapter adapterListNewAssistance =
 * (CtrlListViewListNewAssistanceAdapter) adapter .getAdapter(); ListItem item =
 * adapterListNewAssistance.getItem(position); Intent intent;
 * 
 * switch (item.getID()) { case POLICY: intent = new
 * Intent(ScreenNewAssistance.this, ScreenListPolicies.class);
 * intent.putExtra("LOBID", LobID); intent.putExtra("CLOSE", true);
 * 
 * startActivityForResult(intent, POLICY); break; case CAUSE: // if
 * (getCheckPreviousFieldFilled(POLICY)){ intent = new
 * Intent(ScreenNewAssistance.this, ScreenCauses.class);
 * intent.putExtra("LOBID", LobID);
 * 
 * startActivityForResult(intent, CAUSE); // } break; case PROBLEM: // if
 * (getCheckPreviousFieldFilled(CAUSE)){ intent = new
 * Intent(ScreenNewAssistance.this, ScreenProblems.class);
 * intent.putExtra("CAUSEID", CauseID);
 * 
 * startActivityForResult(intent, PROBLEM); // } break; case SERVICE: // if
 * (getCheckPreviousFieldFilled((insertProblems == true) // ? PROBLEM : CAUSE)){
 * intent = new Intent(ScreenNewAssistance.this, ScreenServices.class);
 * intent.putExtra("CAUSEID", CauseID); intent.putExtra("LOBID", LobID);
 * 
 * startActivityForResult(intent, SERVICE); // } break; case LOCATION:
 * 
 * intent = new Intent(ScreenNewAssistance.this, ScreenMapLocation.class);
 * 
 * if (addressLocation.getStreetName() != null) {
 * 
 * intent.putExtra("TYPE", 1); intent.putExtra("ADDRESS",
 * addressLocation.getStreetName()); intent.putExtra("DISTRICT",
 * addressLocation.getDistrict()); intent.putExtra("HOUSENUMBER",
 * addressLocation.getHouseNumber()); intent.putExtra("COMPLEMENT",
 * addressLocation.getComplement()); intent.putExtra("CITY",
 * addressLocation.getCity()); intent.putExtra("STATE",
 * addressLocation.getState()); intent.putExtra("ZIP",
 * addressLocation.getZip()); intent.putExtra("LATITUDE",
 * addressLocation.getLatitude()); intent.putExtra("LONGITUDE",
 * addressLocation.getLongitude());
 * 
 * } else intent.putExtra("TYPE", 0);
 * 
 * startActivityForResult(intent, LOCATION);
 * 
 * break; case REFERENCE:
 * 
 * intent = new Intent(ScreenNewAssistance.this, ScreenAddressReference.class);
 * intent.putExtra("ADDRESSREFERENCE", addressLocation.getReference());
 * 
 * startActivityForResult(intent, REFERENCE);
 * 
 * break; case SCHEDULE:
 * 
 * intent = new Intent(ScreenNewAssistance.this, ScreenSchedule.class);
 * 
 * intent.putExtra("CALENDARBEGIN", scheduleStartDate);
 * intent.putExtra("CALENDAREND", scheduleEndDate);
 * 
 * startActivityForResult(intent, SCHEDULE);
 * 
 * break; } } });
 */

/*
 * On activity Result Previous code for reference
 * 
 * case CAUSE: CauseID = params.getString("CAUSE_ID"); String cause_name =
 * params.getString("CAUSE_NAME");
 * 
 * items.get(1).setDescription(cause_name);
 * 
 * client = new BLLClient();
 * 
 * if (client.getProblems(CauseID,
 * getResources().getStringArray(R.array.Problem)).size() > 0) {
 * 
 * if (insertProblems == false) {
 * 
 * ListItem item = new ListItem(); item.setID(PROBLEM);
 * item.setName(getString(R.string.Problem)); item.setDescription("");
 * 
 * items.add(2, item); insertProblems = true; }
 * 
 * } else if (insertProblems == true) {
 * 
 * ListItem item = client.getDefaultServices( LobID, CauseID,
 * getResources().getStringArray(R.array.Service),
 * getResources().getStringArray( R.array.CausesDefaultService));
 * 
 * if (item != null) {
 * 
 * ServiceID = item.getCod(); String service = item.getName();
 * 
 * items.get(3 - ((insertProblems == true) ? 0 : 1)) .setDescription(service); }
 * 
 * items.remove(2); ProblemID = null; insertProblems = false;
 * 
 * } else {
 * 
 * ListItem item = client.getDefaultServices( LobID, CauseID,
 * getResources().getStringArray(R.array.Service),
 * getResources().getStringArray( R.array.CausesDefaultService));
 * 
 * if (item != null) {
 * 
 * ServiceID = item.getCod(); String service = item.getName();
 * 
 * items.get(3 - ((insertProblems == true) ? 0 : 1)) .setDescription(service); }
 * }
 * 
 * break; case PROBLEM: ProblemID = params.getInt("PROBLEM_ID"); String
 * problem_name = params.getString("PROBLEM_NAME");
 * 
 * items.get(2).setDescription(problem_name);
 * 
 * client = new BLLClient(); ListItem item = client.getDefaultServices( LobID,
 * CauseID, ProblemID.toString(),
 * getResources().getStringArray(R.array.Service),
 * getResources().getStringArray( R.array.ProblemDefaultService));
 * 
 * if (item != null) {
 * 
 * ServiceID = item.getCod(); String service = item.getName();
 * 
 * items.get(3 - ((insertProblems == true) ? 0 : 1)) .setDescription(service); }
 * 
 * break; case SERVICE: ServiceID = params.getString("SERVICE_ID"); String
 * service = params.getString("SERVICE_NAME");
 * 
 * items.get(3 - ((insertProblems == true) ? 0 : 1)) .setDescription(service);
 * 
 * break;
 * 
 * case REFERENCE:
 * 
 * addressLocation.setReference(params .getString("ADDRESSREFERENCE"));
 * 
 * items.get(5 - ((insertProblems == true) ? 0 : 1))
 * .setDescription(addressLocation.getReference());
 * 
 * break;
 */
// private boolean getCheckPreviousFieldFilled(int screen) {
//
// switch (screen) {
// case POLICY:
// if (PolicyID == null || PolicyID.equals("")){
// Toast.makeText(this, "Preencha a linha anterior",
// Toast.LENGTH_SHORT).show();
// return false;
// }
// break;
// case CAUSE:
// if (CauseID == null || CauseID.equals("")){
// Toast.makeText(this, "Preencha a linha anterior",
// Toast.LENGTH_SHORT).show();
// return false;
// }
// break;
// case PROBLEM:
// if (ProblemID == null || ProblemID.equals("")){
// Toast.makeText(this, "Preencha a linha anterior",
// Toast.LENGTH_SHORT).show();
// return false;
// }
// break;
// case SERVICE:
// if (ServiceID == null || ServiceID.equals("")){
// Toast.makeText(this, "Preencha a linha anterior",
// Toast.LENGTH_SHORT).show();
// return false;
// }
// break;
// case LOCATION:
// break;
// case REFERENCE:
// break;
// case SCHEDULE:
// break;
// }
// return true;
// }