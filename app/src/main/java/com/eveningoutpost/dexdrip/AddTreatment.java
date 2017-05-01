package com.eveningoutpost.dexdrip;

import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.TextView;

import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;
import com.wdullaer.materialdatetimepicker.time.Timepoint;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class AddTreatment extends DialogFragment {
    private static final String ARG_PRESET_TIME = "paramPresetTime";
    private static final String ARG_PRESET_CARBS = "paramPresetCarbs";
    private static final String ARG_PRESET_INSULIN = "paramPresetInsulin";

    private Long mPresetTime;
    private Double mPresetCarbs;
    private Double mPresetInsulin;

    @BindView(R.id.timeValueLabel)
    TextView timeValueTextView;

    @BindView(R.id.bloodGlucoseEditText)
    TextView bloodGlucoseEditTextView;

    @BindView(R.id.carbsEditText)
    TextView carbsEditTextView;

    @BindView(R.id.carbsSuggestionButton)
    Button carbsSuggestionButton;

    @BindView(R.id.insulinEditText)
    TextView insulinEditTextView;

    @BindView(R.id.insulinSuggestionButton)
    Button insulinsuggestionButton;

    private OnAddTreatmentInteractionListener mListener;
    private long mChosenTime = 0;
    private SimpleDateFormat mFormatter;

    public AddTreatment() {
        // Required empty public constructor
    }

    public static AddTreatment newInstance(Long presetTime, Double presetCarbs, Double presetInsulin) {
        AddTreatment fragment = new AddTreatment();
        Bundle args = new Bundle();
        if (presetTime != null)
            args.putLong(ARG_PRESET_TIME, presetTime);
        if (presetCarbs != null)
            args.putDouble(ARG_PRESET_CARBS, presetCarbs);
        if (presetInsulin != null)
            args.putDouble(ARG_PRESET_INSULIN, presetInsulin);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_treatment, container, false);
        ButterKnife.bind(this, view);

        if (mPresetCarbs != null && mPresetCarbs > 0) {
            String suggestion = mPresetCarbs.toString();
            carbsSuggestionButton.setVisibility(View.VISIBLE);
            carbsSuggestionButton.setText(suggestion);
        } else {
            carbsSuggestionButton.setVisibility(View.GONE);
        }

        if (mPresetInsulin != null && mPresetInsulin > 0) {
            String suggestion = mPresetInsulin.toString();
            insulinsuggestionButton.setVisibility(View.VISIBLE);
            insulinsuggestionButton.setText(suggestion);
        } else {
            insulinsuggestionButton.setVisibility(View.GONE);
        }

        Date date = new Date();
        mChosenTime = date.getTime();
        String formattedTime = mFormatter.format(mChosenTime);
        timeValueTextView.setText(formattedTime);

        //On keyboard action save, trigger a save
        bloodGlucoseEditTextView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    onSave();
                    handled = true;
                }
                return handled;
            }
        });

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mChosenTime = getArguments().getLong(ARG_PRESET_TIME);
            mPresetCarbs = getArguments().getDouble(ARG_PRESET_CARBS);
            mPresetInsulin = getArguments().getDouble(ARG_PRESET_INSULIN);
        }

        mFormatter = new SimpleDateFormat("hh:mm", Locale.getDefault());
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnAddTreatmentInteractionListener) {
            mListener = (OnAddTreatmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnAddTreatmentInteractionListener");
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Window window = getDialog().getWindow();
        Point size = new Point();

        if (window != null) {
            Display display = window.getWindowManager().getDefaultDisplay();
            display.getSize(size);
            int width = size.x;

            window.setLayout((int) (width * 0.90), WindowManager.LayoutParams.WRAP_CONTENT);
            window.setGravity(Gravity.CENTER);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    @OnClick(R.id.timeValueLabel)
    public void onTimeEditClick() {

        Calendar now = Calendar.getInstance();
        TimePickerDialog timerPickerDialog = TimePickerDialog.newInstance(
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
                        Calendar cal = Calendar.getInstance();
                        cal.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        cal.set(Calendar.MINUTE, minute);
                        cal.set(Calendar.SECOND, second);
                        mChosenTime = cal.getTime().getTime();

                        String formattedTime = mFormatter.format(cal.getTime());
                        timeValueTextView.setText(formattedTime);
                    }
                },
                now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE),
                now.get(Calendar.SECOND),
                false
        );

        /*
        Prevent selecting a time > 1hr from now
         */
        int maxHour = 0;
        if(now.get(Calendar.HOUR_OF_DAY) < 24) {
            maxHour = now.get(Calendar.HOUR_OF_DAY) + 1;
        } else {
            maxHour = 24;
        }

        timerPickerDialog.setThemeDark(true);
        timerPickerDialog.setMaxTime(new Timepoint(maxHour, now.get(Calendar.MINUTE)));
        timerPickerDialog.setTitle("Select Time");
        timerPickerDialog.show(getActivity().getFragmentManager(), "Datepickerdialog");
    }

    @OnClick(R.id.insulinSuggestionButton)
    public void onInsulinSuggestionButtonClick() {
        String insulinValue = mPresetInsulin.toString();
        insulinEditTextView.setText(insulinValue);
        insulinsuggestionButton.setVisibility(View.GONE);
        carbsSuggestionButton.setVisibility(View.GONE);
    }

    @OnClick(R.id.carbsSuggestionButton)
    public void onCarbsSuggestionButtonClick() {
        String carbsValue = mPresetCarbs.toString();
        carbsEditTextView.setText(carbsValue);
        carbsSuggestionButton.setVisibility(View.GONE);
        insulinsuggestionButton.setVisibility(View.GONE); //Hide the alternative button as
    }

    @OnClick(R.id.saveTreatmentButton)
    public void onSave() {

        Double carbValue = null;
        Double insulinValue = null;
        Double glucoseValue = null;

        if (carbsEditTextView.getText().length() > 0) {
            carbValue = Double.parseDouble(carbsEditTextView.getText().toString());
        }
        if (insulinEditTextView.getText().length() > 0) {
            insulinValue = Double.parseDouble(insulinEditTextView.getText().toString());
        }
        if (bloodGlucoseEditTextView.getText().length() > 0) {
            glucoseValue = Double.parseDouble(bloodGlucoseEditTextView.getText().toString());
        }

        mListener.onTreatmentSave(mChosenTime, glucoseValue, carbValue, insulinValue);
        getDialog().dismiss();
    }


    public interface OnAddTreatmentInteractionListener {
        void onTreatmentSave(long dateTime, Double bloodGlucose, Double carbs, Double insulin);
    }
}
