package com.example.easycalendar;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import io.realm.Realm;
import io.realm.RealmResults;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;


import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnRangeSelectedListener;
import com.thebluealliance.spectrum.SpectrumPalette;

import java.util.Calendar;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EventInformationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EventInformationFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private int eventColor= -16537100; //default color
    private Spinner spinner_notification;
    private Spinner spinner_recurrance;
    private Spinner spinner_category;
    private Spinner spinner_emailNotification;
    private ImageButton btn_setNotificationToNone;
    private ImageButton  btn_setRecurranceToNone;
    private ImageButton  btn_showPalette;

    private TextView notiftv;
    private EditText edtTxt_email;
    private EditText edtTxt_notes;
    private EditText edtTxt_eventName;
    private CheckBox chckbox_email;
    private CheckBox checkbox_rememberEmail;
    private TextView tv_startTime;
    private TextView tv_startDate;
    private TextView tv_endTime;
    private TextView tv_endDate;


    TimePickerDialog timePickerDialog;
    private String selectedDate = "2020-05-20";
    private AlertDialog.Builder builder_palette;
    private  View paletteView;
    private  View datePickerView;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public EventInformationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EventInformationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EventInformationFragment newInstance(String param1, String param2) {
        EventInformationFragment fragment = new EventInformationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }




    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_event_information, container, false);
    }

    private void setListeners() {
        btn_setNotificationToNone.setOnClickListener(this);
        btn_setRecurranceToNone.setOnClickListener(this);
        btn_showPalette.setOnClickListener(this);
        spinner_notification.setOnItemSelectedListener(this);
        spinner_emailNotification.setOnItemSelectedListener(this);
        spinner_recurrance.setOnItemSelectedListener(this);
        spinner_category.setOnItemSelectedListener(this);
        tv_startTime.setOnClickListener(this);
        tv_startDate.setOnClickListener(this);
        tv_endTime.setOnClickListener(this);
        tv_endDate.setOnClickListener(this);

        chckbox_email.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    edtTxt_email.setVisibility(View.VISIBLE);
                    edtTxt_email.setClickable(true);
                    checkbox_rememberEmail.setVisibility(View.VISIBLE);
                    if(spinner_emailNotification.getSelectedItemPosition() == 0)
                        spinner_emailNotification.setSelection(spinner_notification.getSelectedItemPosition());
                } else {
                    edtTxt_email.setClickable(false);
                    edtTxt_email.setVisibility(View.INVISIBLE);
                    checkbox_rememberEmail.setVisibility(View.INVISIBLE);
                    spinner_emailNotification.setSelection(0);

                }
            }
        });
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        initViews();
        setListeners();
    }


    private void showPaletteLayout() {
        LayoutInflater inflater = EventInformationFragment.this.getLayoutInflater();
        paletteView = inflater.inflate(R.layout.palette_layout,null);
        builder_palette = new AlertDialog.Builder(getActivity());
        builder_palette.setTitle("Renk Paleti");
        builder_palette.setMessage("Etkinlik Türüne Özel Renk Seçimi");
        builder_palette.setView(paletteView);
        builder_palette.setPositiveButton("Tamam", null);
        builder_palette.show();
        SpectrumPalette spectrumPalette = paletteView.findViewById(R.id.palette);
        spectrumPalette.setOnColorSelectedListener(new SpectrumPalette.OnColorSelectedListener() {
            @Override
            public void onColorSelected(int color) {
                Toast.makeText(getActivity(), "CoLOR : " + color, Toast.LENGTH_SHORT).show();
                btn_showPalette.setBackgroundColor(color);
                eventColor = color;
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_setNotificationToNone:
                spinner_notification.setSelection(0);
                break;
            case R.id.btn_setRecurranceToNone:
                spinner_recurrance.setSelection(0);
                break;
            case R.id.btn_showPalette:
                showPaletteLayout();
                break;
            case R.id.startTime:
                timePick("start");
                break;
            case R.id.endTime:
                timePick("end");
                break;
            case R.id.starDate:
                mydatepick();
                break;
            case R.id.endDate:
                //TODO
                /* take a parameter start or end date for mydatepick func
                   if user selects a date before the start date, unselect the selection
                 */
                mydatepick();
                break;
            default:
                break;
        }
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.spinner_notification:
                if(position != 0 ) {
                    btn_setNotificationToNone.setVisibility(View.VISIBLE);
                    chckbox_email.setClickable(true);
                    spinner_emailNotification.setVisibility(View.VISIBLE);
                    //chckbox_email.setTextColor(R.color.md_black_1000);
                }
                else {
                    btn_setNotificationToNone.setVisibility(View.INVISIBLE);
                    chckbox_email.setClickable(false);
                    spinner_emailNotification.setVisibility(View.INVISIBLE);
                    // chckbox_email.setTextColor(R.color.md_grey_500);
                }
                break;
            case R.id.spinner_recurrance:
                if(position != 0 )
                    btn_setRecurranceToNone.setVisibility(View.VISIBLE);
                else
                    btn_setRecurranceToNone.setVisibility(View.INVISIBLE);
                break;
            case R.id.spinner_emailNotification:
                if(position ==0 )
                    chckbox_email.setChecked(false);
                else
                    chckbox_email.setChecked(true);
                break;
            default:
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void initViews(){



        spinner_notification = getActivity().findViewById(R.id.spinner_notification);
        spinner_recurrance = getActivity().findViewById(R.id.spinner_recurrance);
        spinner_category = getActivity().findViewById(R.id.spinner_category);
        spinner_emailNotification= getActivity().findViewById(R.id.spinner_emailNotification);
        btn_setNotificationToNone = getActivity().findViewById(R.id.btn_setNotificationToNone);
        btn_setRecurranceToNone = getActivity().findViewById(R.id.btn_setRecurranceToNone);
        btn_showPalette =getActivity(). findViewById(R.id.btn_showPalette);
        chckbox_email =getActivity().findViewById(R.id.checkbo_email);
        checkbox_rememberEmail =getActivity().findViewById(R.id.checkbox_rememberEmail);
        edtTxt_email = getActivity().findViewById(R.id.edtTxt_email);
        edtTxt_notes = getActivity().findViewById(R.id.edtTxt_notes);
        edtTxt_eventName = getActivity().findViewById(R.id.edtTxt_eventName);
        tv_startTime = getActivity().findViewById(R.id.startTime);
        tv_startDate = getActivity().findViewById(R.id.starDate);
        tv_endTime = getActivity().findViewById(R.id.endTime);
        tv_endDate = getActivity().findViewById(R.id.endDate);


        ArrayAdapter<CharSequence> adptr_notification = ArrayAdapter.createFromResource(getActivity(),
                R.array.notificationOptions_array, android.R.layout.simple_spinner_item);
        adptr_notification.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_notification.setAdapter(adptr_notification);

        spinner_emailNotification.setAdapter(adptr_notification);

        ArrayAdapter<CharSequence> adptr_recurrance = ArrayAdapter.createFromResource(getActivity(),
                R.array.recurranceOptions_array, android.R.layout.simple_spinner_item);
        adptr_recurrance.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_recurrance.setAdapter(adptr_recurrance);

        ArrayAdapter<CharSequence> adptr_category = ArrayAdapter.createFromResource(getActivity(),
                R.array.categoryOptions_array, android.R.layout.simple_spinner_item);
        adptr_category.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_category.setAdapter(adptr_category);

        // Starting positions of spinners
        spinner_notification.setSelection(1);
        spinner_recurrance.setSelection(0);
        spinner_category.setSelection(0);

        tv_startDate.setText( selectedDate);
        tv_endDate .setText( selectedDate);





    }

    public void timePick(String time_type){
        final Calendar takvim = Calendar.getInstance();
        int saat = takvim.get(Calendar.HOUR_OF_DAY);
        int dakika = takvim.get(Calendar.MINUTE);

        TimePickerDialog tpd = new TimePickerDialog(getActivity(),
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        // hourOfDay ve minute değerleri seçilen saat değerleridir.
                        Time selectedTime = new Time(hourOfDay,minute);
                        if(time_type.equals("start")) {  //StartTimePicker selected
                            tv_startTime.setText(selectedTime.toString());
                            tv_endTime.setText(selectedTime.toString());
                        }else { //EndTimePicker selected
                            Time startTime = new Time(tv_startTime.getText().toString(),":");
                            Boolean oneDayEvent = tv_startDate.getText().toString().equals(tv_endDate.getText().toString());
                            if( oneDayEvent && selectedTime.isBefore(startTime) )
                                //TODO fix
                                //Toast.makeText(, "Hatalı bitiş zamanı", Toast.LENGTH_SHORT).show();
                                Log.i("warning","Hatalı bitiş zamanı");
                            else
                                tv_endTime.setText(hourOfDay + ":" + minute);
                        }

                    }
                }, saat, dakika, true);

        tpd.setButton(TimePickerDialog.BUTTON_POSITIVE, "Tamam", tpd);
        tpd.show();
    }

    public void mydatepick(){
        AlertDialog.Builder builder_date;
        LayoutInflater inflater = EventInformationFragment.this.getLayoutInflater();
        datePickerView = inflater.inflate(R.layout.datepick_layout,null);

        builder_date = new AlertDialog.Builder(getActivity());
        builder_date.setTitle(" Etkinlik Tarihi");
        builder_date.setMessage("Başlangıç ve bitiş tarihi");
        builder_date.setView(datePickerView);
        builder_date.setPositiveButton("Tamam", null);
        builder_date.show();
        MaterialCalendarView calendarView = datePickerView.findViewById(R.id.calendarView);

        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                tv_startDate.setText(date.getDate().toString());
                tv_endDate .setText( date.getDate().toString());
            }
        });

        calendarView.setOnRangeSelectedListener(new OnRangeSelectedListener() {
            @Override
            public void onRangeSelected(@NonNull MaterialCalendarView widget, @NonNull List<CalendarDay> dates) {
                CalendarDay startDay = dates.get(0);
                tv_startDate.setText( startDay.getDate().toString());
                startDay = dates.get(dates.size() - 1);
                tv_endDate.setText( startDay.getDate().toString());
            }
        });
    }





    public MyEvent getInputs(){

        String eventName = edtTxt_eventName.getText().toString();
        int eventCategory  = spinner_category.getSelectedItemPosition();
        //eventColor already setted

        // LocalDate startDate = MyEvent.StringToDate( tv_startDate.getText().toString(), '' );
        //LocalDate endDate = MyEvent.StringToDate( tv_endDate.getText().toString(), '/' );
        String startDate =tv_startDate.getText().toString();
        String endDate = tv_endDate.getText().toString();
        Time startTime = new Time(tv_startTime.getText().toString(),":");
        Time endTime = new Time(tv_endTime.getText().toString(),":");

        int notification = spinner_notification.getSelectedItemPosition();
        String notes = edtTxt_notes.getText().toString();
        int reccurance = spinner_recurrance.getSelectedItemPosition();

        MyEvent myEvent = new MyEvent(eventName, eventCategory,eventColor,startTime,endTime,startDate,endDate,notification,notes,reccurance);
        return myEvent;

    }

    public void setInputs(MyEvent myEvent){

        //TODO edit
       spinner_notification.setSelection(myEvent.getIndex_notification());
      spinner_recurrance.setSelection(myEvent.getIndex_recurrance());
       spinner_category.setSelection(myEvent.getIndex_category());
      spinner_emailNotification.setSelection(myEvent.getEmail_notification());

        edtTxt_email.setText(myEvent.getEmail());
       edtTxt_notes.setText(myEvent.getNotes());
        edtTxt_eventName.setText(myEvent.getEventName());
        tv_startTime.setText(myEvent.getStartTime().toString());
        tv_startDate.setText(myEvent.getStartDate());;
        tv_endTime.setText(myEvent.getEndTime().toString());;
        tv_endDate.setText(myEvent.getEndDate());
    }



}
