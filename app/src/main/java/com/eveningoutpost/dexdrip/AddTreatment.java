package com.eveningoutpost.dexdrip;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AddTreatment.OnAddTreatmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AddTreatment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddTreatment extends DialogFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    
    //TODO preset time, carbs and insulin
    private static final String ARG_PRESET_TIME = "paramPresetTime";
    private static final String ARG_PRESET_CARBS = "paramPresetCarbs";
    private static final String ARG_PRESET_INSULIN = "paramPresetInsulin";

    // TODO: Rename and change types of parameters
    private Integer mPresetTime;
    private Double mPresetCarbs;
    private Double mPresetInsulin;
    
    @BindView(R.id.timeEditText)
    TextView timeEditTextView;
    
    @BindView(R.id.bloodGlucoseEditText)
    TextView bloodGlucoseEditTextView;
    
    @BindView(R.id.carbsEditText)
    TextView carbsEditTextView;
    
    @BindView(R.id.insulinEditText)
    TextView insulinEditTextView;

    private OnAddTreatmentInteractionListener mListener;

    public AddTreatment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment AddTreatment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddTreatment newInstance(Integer presetTime, Double presetCarbs, Double presetInsulin) {
        AddTreatment fragment = new AddTreatment();
        Bundle args = new Bundle();
        args.putInt(ARG_PRESET_TIME, presetTime);
        args.putDouble(ARG_PRESET_CARBS, presetCarbs);
        args.putDouble(ARG_PRESET_INSULIN, presetInsulin);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mPresetTime = getArguments().getInt(ARG_PRESET_TIME);
            mPresetCarbs = getArguments().getDouble(ARG_PRESET_CARBS);
            mPresetInsulin = getArguments().getDouble(ARG_PRESET_INSULIN);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_treatment, container, false);
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
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
    
    @OnClick(R.id.saveTreatmentButton)
    public void onSave() {
        //TODO get treatment from the fragment views and validate
        timeEditTextView.getText();
        mListener.onTreatmentSave(new Date(), 10.0, 11.0, 12.0); //TODO
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnAddTreatmentInteractionListener {
        void onTreatmentSave(Date dateTime, Double bloodGlucose, Double carbs, Double insulin);
    }
}
