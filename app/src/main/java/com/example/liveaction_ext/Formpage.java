package com.example.liveaction_ext;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;


public class Formpage extends AppCompatActivity {
    CardView nextActivity_btn,productcrd,durbles_opn;
    ImageView durble_cls;
    RadioGroup radioGroup;

    String Otherapp ="";
    String Otherapp_music="";
    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
    Switch onlyn_used_swtch;
    String selectedValue,validationmsg;
       Conn_service servic= new Conn_service();

    private String[] selectedArray=   new String[0];;
    Spinner gender,Education,Ocupation,du_spinner,product_spinner;
    int year = 0;
    int month = 0;
    Boolean chek;
    int dayOfMonth = 0;
    CheckBox checkBo;
    private EditText dateEditText;
    Dialog dialog;
    Switch[] switches_froott = new Switch[14];
    Switch[] switches_frms = new Switch[14];

    Switch sw1, sw2, sw3, sw4, sw5, sw6, sw7, sw8, sw9, sw10, sw11, sw12, sw13, sw14;
    Switch sw_1, sw_2, sw_3, sw_4, sw_5, sw_6, sw_7, sw_8, sw_9, sw_10, sw_11, sw_12, sw_13, sw_14;
    int selectedId;
    private List<String> allCheckedItems = new ArrayList<>();


    private Spinner spinner;
    private List<String> items;
    private List<String> checkedItems;
    private CheckBox[] checkboxes;
    private CheckBox[] checkboxe_fdbk;
    String message=" ";
    private DatePickerDialog.OnDateSetListener dateSetListener;
    private CheckBox chk_tw,chk_fw, chk_clr,chk_rf,chk_wm,chk_pc,chk_ac,chk_al;

EditText Et_name,Et_email;

    private String selectedDistrict, selectedState;                 //vars to hold the values of selected State and District
           //declaring TextView to show the errors
    private Spinner stateSpinner, districtSpinner;                  //Spinners
    private ArrayAdapter<CharSequence> stateAdapter, districtAdapter;
//    private StringBuilder selectedOptions;
    private List<String> selected_dur_Options = new ArrayList<>();
    private List<String> selectedOFeedback = new ArrayList<>();
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formpage);


        final String[] select_purchase = {
                "Select","product","Grocery (staples, packaged food, snacks, beverages, dairy items, etc)",
        "Beauty & Personal Care (soaps, shampoo, skin creams, makeup, etc)",
        "Mobile & Mobile accessories",
        "Clothing & Footwear","Children toys & games","None"};//////////cust ui change

        textView = findViewById(R.id.change_text);
        //textView.setText("Select");
Et_name=findViewById(R.id.et_name);
Et_email=findViewById(R.id.et_email);
onlyn_used_swtch= findViewById(R.id.switch_shop_onlyn);
        Education =findViewById(R.id.educationSpinner);
        Ocupation = findViewById(R.id.ocupationSpinner);
        productcrd=findViewById(R.id.productcard);
durbles_opn=findViewById(R.id.durable_click);

        radioGroup = findViewById(R.id.radiogrpp);


sw1=findViewById(R.id.netflix_switch1);
sw2=findViewById(R.id.netflix_switch2);
sw3=findViewById(R.id.prime_switch1);
sw4=findViewById(R.id.prime_switch2);
        sw4.setChecked(true);///cust ui change
sw5=findViewById(R.id.disney_switch1);
sw6=findViewById(R.id.disney_switch2);
sw7=findViewById(R.id.zee5_switch1);

        sw8=findViewById(R.id.zee5_switch2);
        sw9=findViewById(R.id.youtube_switch1);
        sw10=findViewById(R.id.youtube_switch2);
        sw11=findViewById(R.id.other_switch1);
        sw12=findViewById(R.id.other_switch2);
        sw13 = findViewById(R.id.none_switch1);
        sw14 = findViewById(R.id.none_switch2);




        switches_froott[0] = sw1;
        switches_froott[1] = sw2;
        switches_froott[2] = sw3;
        switches_froott[3] = sw4;
        switches_froott[4] = sw5;
        switches_froott[5] = sw6;
        switches_froott[6] = sw7;
        switches_froott[7] = sw8;
        switches_froott[8] = sw9;
        switches_froott[9] = sw10;
        switches_froott[10] = sw11;
        switches_froott[11] = sw12;
        switches_froott[12] = sw13;
        switches_froott[13] = sw14;
//////////////////////////////////////////////////
        sw_1=findViewById(R.id.jio_switch1);
        sw_2=findViewById(R.id.jio_switch2);
        sw_3=findViewById(R.id.gana_switch1);
        sw_4=findViewById(R.id.gana_switch2);
        sw_5=findViewById(R.id.spotify_switch1);
        sw_6=findViewById(R.id.spotify_switch2);
        sw_7=findViewById(R.id.zee5_m_switch1);
        sw_7.setChecked(true);////cust ui change
        sw_8=findViewById(R.id.zee5_m_switch2);
        sw_9=findViewById(R.id.youtube_m_Switch1);
        sw_10=findViewById(R.id.youtube_m_Switch2);

        sw_11=findViewById(R.id.others_m_switch1);

        sw_12=findViewById(R.id.others_m_switch2);
        sw_13=findViewById(R.id.nonet_Switch1);
        sw_14 =findViewById(R.id.nonet_Switch2);



        switches_frms[0] = sw_1;
        switches_frms[1] = sw_2;
        switches_frms[2] = sw_3;
        switches_frms[3] = sw_4;
        switches_frms[4] = sw_5;
        switches_frms[5] = sw_6;
        switches_frms[6] = sw_7;
        switches_frms[7] = sw_8;
        switches_frms[8] = sw_9;
        switches_frms[9] = sw_10;
        switches_frms[10] = sw_11;
        switches_frms[11] = sw_12;
        switches_frms[12] = sw_13;
        switches_frms[13] = sw_14;

        checkboxe_fdbk = new CheckBox[]{
               findViewById(R.id.fdbk_chk1),
               findViewById(R.id.fdbk_chk2),
               findViewById(R.id.fdbk_chk3),
               findViewById(R.id.fdbk_chk4),

        };



        dialog = new Dialog(Formpage.this);
        dialog.setContentView(R.layout.dialog_for_durables);

        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        durbles_opn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
            }
        });
durble_cls= dialog.findViewById(R.id.close_dial_durables);
        checkboxes = new CheckBox[]{
                dialog.findViewById(R.id.durable_chk1),
                dialog.findViewById(R.id.durable_chk3),
                dialog.findViewById(R.id.durable_chk4),
                dialog.findViewById(R.id.durable_chk5),
                dialog.findViewById(R.id.durable_chk6),
                dialog.findViewById(R.id.durable_chk7),
                dialog.findViewById(R.id.durable_chk8),
                dialog.findViewById(R.id.durable_chk2)
        };




        for (int i = 0; i < checkboxe_fdbk.length; i++) {
            final int index = i;
            checkboxe_fdbk[i].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    String checkboxText = checkboxe_fdbk[index].getText().toString();
                    if (isChecked) {
                        if (!selectedOFeedback.contains(checkboxText)) {

                            selectedOFeedback.add(checkboxText);


                        }
                    } else {
                        selectedOFeedback.remove(checkboxText);
                    }
                }
            });
        }
        nextActivity_btn=findViewById(R.id.piechart_btn);

        nextActivity_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                try {
//                    StoreData();
//                } catch (JSONException e) {
//                    throw new RuntimeException(e);
//                }
//


                int selectedId = radioGroup.getCheckedRadioButtonId();
               RadioButton radioButton = findViewById(selectedId);
                if (selectedId != -1) {
//          radioButton = findViewById(selectedId);
                    selectedValue = radioButton.getText().toString();
                    Log.e("hvyg",selectedValue);
                }
                else {
                    selectedValue= "not_selected";
                }


                Log.e("denger",selectedValue);
                try {
                    boolean valid = validat();
                    if (valid) {
                      boolean storesuccess=StoreData();
                    if(storesuccess) {
                        startActivity(new Intent(Formpage.this,Chart_page.class));
                    }
                    else{
                        Toast.makeText(Formpage.this, "Data Sending unsuccessful ", Toast.LENGTH_SHORT).show();
                    }
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }


                String selectedOptionsString = String.join(", ", selected_dur_Options);



                Log.e("sttringdurables",selectedOptionsString);

//         startActivity(new Intent(Formpage.this,Manage_screen.class));
            }
        });
//        selectedOptions = new ArrayList<>();
        for (int i = 0; i < checkboxes.length; i++) {
            final int index = i;
            checkboxes[i].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    String checkboxText = checkboxes[index].getText().toString();
                    if (isChecked) {
                        if (!selected_dur_Options.contains(checkboxText)) {
                            selected_dur_Options.add(checkboxText);
                        }
                    } else {
                        selected_dur_Options.remove(checkboxText);
                    }
                }
            });
        }
        durble_cls.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });


        product_spinner= findViewById(R.id.onlineshoppingSpinner);
        productcrd.setVisibility(View.GONE);
       onlyn_used_swtch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Perform actions based on the switch state
                if (isChecked) {
                    // Switch is ON
                    chek=isChecked;
                    productcrd.setVisibility(View.VISIBLE);

                    // Perform desired actions when the switch is toggled ON
                } else {
                    productcrd.setVisibility(View.GONE);
                    // Switch is OFF
                    // Perform desired actions when the switch is toggled OFF
                }
            }
        });





/////////durables code




        // Create a list of items for the spinner


//        Toast.makeText(MainActivity.this, "Selected items: " + selectedItems.toString(), Toast.LENGTH_SHORT).show();


        dateEditText = findViewById(R.id.dateEditText);


        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                String selectedDate = String.format("%d-%02d-%02d", year, month + 1, dayOfMonth);
                dateEditText.setText(selectedDate);
            }
        };


        dateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        //////cust ui change
        ArrayList<Purchase> listVOs = new ArrayList<>();

        for (int i = 0; i < select_purchase.length; i++) {
            Purchase stateVO = new Purchase();
            stateVO.setTitle(select_purchase[i]);
            stateVO.setSelected(false);
            listVOs.add(stateVO);
        }
        CheckboxAdapter myAdapter = new CheckboxAdapter(Formpage.this, 0,
                listVOs);

        //ArrayAdapter<CharSequence> productList =ArrayAdapter.createFromResource(this, R.array.productslist, R.layout.purchase_online_layout);

        myAdapter.setDropDownViewResource(R.layout.purchase_online_layout);
        product_spinner.setAdapter(myAdapter);


        product_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String ss = product_spinner.getSelectedItem().toString();

                Log.e("in formpage","get item"+myAdapter.getItem(position));

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



///////////////////////////////cust ui change
        gender=findViewById(R.id.genderSpinner);
        ArrayAdapter<CharSequence> gendersip=ArrayAdapter.createFromResource(this, R.array.Gender, android.R.layout.simple_spinner_item);

        gendersip.setDropDownViewResource(android.R.layout.simple_spinner_item);
        gender.setAdapter(gendersip);

        ArrayAdapter<CharSequence> edu_adap=ArrayAdapter.createFromResource(this, R.array.Education, android.R.layout.simple_spinner_item);

        edu_adap.setDropDownViewResource(android.R.layout.simple_spinner_item);
        Education.setAdapter(edu_adap);
        ArrayAdapter<CharSequence>occu_adap=ArrayAdapter.createFromResource(this, R.array.Ocupation, android.R.layout.simple_spinner_item);

        occu_adap.setDropDownViewResource(android.R.layout.simple_spinner_item);
        Ocupation.setAdapter(occu_adap);

        stateSpinner = findViewById(R.id.spinner_indian_states);

        stateAdapter = ArrayAdapter.createFromResource(this,R.array.array_indian_states, R.layout.spinner_layout);
        stateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        stateSpinner.setAdapter(stateAdapter);
        stateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Define City Spinner but we will populate the options through the selected state
                districtSpinner = findViewById(R.id.spinner_indian_districts);

                selectedState = stateSpinner.getSelectedItem().toString();      //Obtain the selected State

                int parentID = parent.getId();
                if (parentID == R.id.spinner_indian_states){
                    switch (selectedState){
                        case "Select": districtAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                                R.array.array_default_districts, R.layout.spinner_layout);
                            break;
                        case "Andhra Pradesh": districtAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                                R.array.array_andhra_pradesh_districts, R.layout.spinner_layout);
                            break;
                        case "Arunachal Pradesh": districtAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                                R.array.array_arunachal_pradesh_districts, R.layout.spinner_layout);
                            break;
                        case "Assam": districtAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                                R.array.array_assam_districts, R.layout.spinner_layout);
                            break;
                        case "Bihar": districtAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                                R.array.array_bihar_districts, R.layout.spinner_layout);
                            break;
                        case "Chhattisgarh": districtAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                                R.array.array_chhattisgarh_districts, R.layout.spinner_layout);
                            break;
                        case "Goa": districtAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                                R.array.array_goa_districts, R.layout.spinner_layout);
                            break;
                        case "Gujarat": districtAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                                R.array.array_gujarat_districts, R.layout.spinner_layout);
                            break;
                        case "Haryana": districtAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                                R.array.array_haryana_districts, R.layout.spinner_layout);
                            break;
                        case "Himachal Pradesh": districtAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                                R.array.array_himachal_pradesh_districts, R.layout.spinner_layout);
                            break;
                        case "Jharkhand": districtAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                                R.array.array_jharkhand_districts, R.layout.spinner_layout);
                            break;
                        case "Karnataka": districtAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                                R.array.array_karnataka_districts, R.layout.spinner_layout);
                            break;
                        case "Kerala": districtAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                                R.array.array_kerala_districts, R.layout.spinner_layout);
                            break;
                        case "Madhya Pradesh": districtAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                                R.array.array_madhya_pradesh_districts, R.layout.spinner_layout);
                            break;
                        case "Maharashtra": districtAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                                R.array.array_maharashtra_districts, R.layout.spinner_layout);
                            break;
                        case "Manipur": districtAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                                R.array.array_manipur_districts, R.layout.spinner_layout);
                            break;
                        case "Meghalaya": districtAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                                R.array.array_meghalaya_districts, R.layout.spinner_layout);
                            break;
                        case "Mizoram": districtAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                                R.array.array_mizoram_districts, R.layout.spinner_layout);
                            break;
                        case "Nagaland": districtAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                                R.array.array_nagaland_districts, R.layout.spinner_layout);
                            break;
                        case "Odisha": districtAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                                R.array.array_odisha_districts, R.layout.spinner_layout);
                            break;
                        case "Punjab": districtAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                                R.array.array_punjab_districts, R.layout.spinner_layout);
                            break;
                        case "Rajasthan": districtAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                                R.array.array_rajasthan_districts, R.layout.spinner_layout);
                            break;
                        case "Sikkim": districtAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                                R.array.array_sikkim_districts, R.layout.spinner_layout);
                            break;
                        case "Tamil Nadu": districtAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                                R.array.array_tamil_nadu_districts, R.layout.spinner_layout);
                            break;
                        case "Telangana": districtAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                                R.array.array_telangana_districts, R.layout.spinner_layout);
                            break;
                        case "Tripura": districtAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                                R.array.array_tripura_districts, R.layout.spinner_layout);
                            break;
                        case "Uttar Pradesh": districtAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                                R.array.array_uttar_pradesh_districts, R.layout.spinner_layout);
                            break;
                        case "Uttarakhand": districtAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                                R.array.array_uttarakhand_districts, R.layout.spinner_layout);
                            break;
                        case "West Bengal": districtAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                                R.array.array_west_bengal_districts, R.layout.spinner_layout);
                            break;
                        case "Andaman and Nicobar Islands": districtAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                                R.array.array_andaman_nicobar_districts, R.layout.spinner_layout);
                            break;
                        case "Chandigarh": districtAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                                R.array.array_chandigarh_districts, R.layout.spinner_layout);
                            break;
                        case "Dadra and Nagar Haveli": districtAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                                R.array.array_dadra_nagar_haveli_districts, R.layout.spinner_layout);
                            break;
                        case "Daman and Diu": districtAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                                R.array.array_daman_diu_districts, R.layout.spinner_layout);
                            break;
                        case "Delhi": districtAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                                R.array.array_delhi_districts, R.layout.spinner_layout);
                            break;
                        case "Jammu and Kashmir": districtAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                                R.array.array_jammu_kashmir_districts, R.layout.spinner_layout);
                            break;
                        case "Lakshadweep": districtAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                                R.array.array_lakshadweep_districts, R.layout.spinner_layout);
                            break;
                        case "Ladakh": districtAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                                R.array.array_ladakh_districts, R.layout.spinner_layout);
                            break;
                        case "Puducherry": districtAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                                R.array.array_puducherry_districts, R.layout.spinner_layout);
                            break;
                        default:  break;
                    }
                    districtAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);     // Specify the layout to use when the list of choices appears
                    districtSpinner.setAdapter(districtAdapter);        //Populate the list of Districts in respect of the State selected

                    //To obtain the selected District from the spinner
                    districtSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            selectedDistrict = districtSpinner.getSelectedItem().toString();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                        }
                    });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



    }
    private boolean validat() throws JSONException {

        String Name= Et_name.getText().toString().trim();
        String Email= Et_email.getText().toString().trim();
        String genders= (String)gender.getSelectedItem();
        String Education_= (String)Education.getSelectedItem();
        String Ocupation_= (String)Ocupation.getSelectedItem();
//
        boolean tr = isValidName(Name);
        boolean tr2 = isValidEmail(Email);
   boolean tr3 = isvalidcheck( selected_dur_Options);
   boolean tr13 = isvalidcheck( selectedOFeedback);
        boolean tr4 = isValidcity(selectedDistrict);
        boolean tr5 = isValidstate(selectedState);
        boolean tr6 = isValidocupation(Ocupation_);
        boolean tr7 = isValidgender(genders);
        boolean tr8 = isValideducation(Education_);
boolean tr9 = isValidage(dayOfMonth,month,year);
boolean tr10 = isradios(selectedValue);
        boolean tr11 = isAnySwitchottChecked(switches_froott);
        boolean tr12 = isAnySwitchottChecked(switches_frms);
        if (!tr) {
            validationmsg = "Invalid Name";
        }
        if (!tr2) {
            validationmsg = "Invalid Email";
        }
        if (!tr3) {
            validationmsg = "Invalid Durables";
        }
        if (!tr4) {
            validationmsg = "Invalid District";
        }
        if (!tr5) {
            validationmsg = "Invalid State";
        }
        if (!tr6) {
            validationmsg = "Invalid Occupation";
        }
        if (!tr7) {
            validationmsg = "Invalid gender";
        }
        if (!tr8) {
            validationmsg = "Invalid Education";
        }
        if (!tr9) {
            validationmsg = "Invalid DOB";
        }
        if (!tr10) {
            validationmsg = "Invalid selection";
        }
        if (!tr11) {
            validationmsg = "Invalid OTT selection";
        }
        if (!tr12) {
            validationmsg = "Invalid Music_app selection";
        }
        if (!tr13) {
            validationmsg = "Invalid feedback";
        }

            if (tr && tr2 && tr3 && tr13 && tr4 && tr5 && tr6 && tr7 && tr8 && tr9 && tr10 && tr11 && tr12) {
//            if (tr13) {
            Log.e("","check");
            return true;
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            // Set the title and message for the dialog
            builder.setTitle("Validation Error");
            builder.setMessage(validationmsg);

            // Set a positive button and its click listener
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Handle the click event (if needed)
                }
            });

            // Create and show the alert dialog
            AlertDialog alertDialog = builder.create();
                alertDialog.show();
                return false;
        }


    }
    private JSONArray OttData() throws JSONException {
        JSONArray arry_ott = new JSONArray();

        JSONObject app1 = new JSONObject();
        app1.put("app_name", "Netflix");
        app1.put("installed", sw1.isChecked());
        app1.put("watched_in_last_year", sw2.isChecked());

        JSONObject app2 = new JSONObject();
        app2.put("app_name", "Prime Video");
        app2.put("installed", sw3.isChecked());
        app2.put("watched_in_last_year", sw4.isChecked());
        JSONObject app3 = new JSONObject();
        app2.put("app_name", "Disney+Hotstar");
        app2.put("installed", sw5.isChecked());
        app2.put("watched_in_last_year", sw6.isChecked());
        JSONObject app4 = new JSONObject();
        app2.put("app_name", "Zee5");
        app2.put("installed", sw7.isChecked());
        app2.put("watched_in_last_year", sw8.isChecked());
        JSONObject app5 = new JSONObject();
        app2.put("app_name", "Youtube");
        app2.put("installed", sw9.isChecked());
        app2.put("watched_in_last_year", sw10.isChecked());
        JSONObject app6 = new JSONObject();
        app2.put("app_name", Otherapp);
        app2.put("installed", sw11.isChecked());
        app2.put("watched_in_last_year", sw12.isChecked());

        JSONObject app7 = new JSONObject();
        app2.put("app_name", "None");
        app2.put("installed", sw13.isChecked());
        app2.put("watched_in_last_year", sw14.isChecked());

        arry_ott.put(app1);
        arry_ott.put(app2);
        arry_ott.put(app3);
        arry_ott.put(app4);
        arry_ott.put(app5);
        arry_ott.put(app6);


        return arry_ott;
    }
    private JSONArray MusictData() throws JSONException {
        JSONArray arry_music = new JSONArray();

        JSONObject app1 = new JSONObject();
        app1.put("app_name", "Jio savan");
        app1.put("installed", sw_1.isChecked());
        app1.put("watched_in_last_year", sw_2.isChecked());

        JSONObject app2 = new JSONObject();
        app2.put("app_name", "Gana");
        app2.put("installed", sw_3.isChecked());
        app2.put("watched_in_last_year", sw_4.isChecked());
        JSONObject app3 = new JSONObject();
        app2.put("app_name", "Spotify");
        app2.put("installed", sw_5.isChecked());
        app2.put("watched_in_last_year", sw_6.isChecked());
        JSONObject app4 = new JSONObject();
        app2.put("app_name", "Zee5");
        app2.put("installed", sw_7.isChecked());
        app2.put("watched_in_last_year", sw_8.isChecked());
        JSONObject app5 = new JSONObject();
        app2.put("app_name", "Youtube");
        app2.put("installed", sw_9.isChecked());
        app2.put("watched_in_last_year", sw_10.isChecked());
        JSONObject app6 = new JSONObject();
        app2.put("app_name", Otherapp_music);
        app2.put("installed", sw_11.isChecked());
        app2.put("watched_in_last_year", sw_12.isChecked());

        JSONObject app7 = new JSONObject();
        app2.put("app_name", "None");
        app2.put("installed", sw_13.isChecked());
        app2.put("watched_in_last_year", sw_14.isChecked());


        arry_music.put(app1);
        arry_music.put(app2);
        arry_music.put(app3);
        arry_music.put(app4);
        arry_music.put(app5);
        arry_music.put(app6);


    return arry_music;
    }
    private boolean StoreData() throws JSONException {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        String longi="";
        String lati="";
        SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        longi= sh.getString("longitude","");
  lati=sh.getString("latitude","");


        String mobile=sh.getString("mobile_number","");
String Name= Et_name.getText().toString().trim();


String Email= Et_email.getText().toString().trim();
        String genders= (String)gender.getSelectedItem();
        String Education_= (String)Education.getSelectedItem();
        String Ocupation_= (String)Ocupation.getSelectedItem();

    String selectedOptionsString = String.join(", ", selected_dur_Options);
        String choice = String.join(", ", selectedOFeedback);
        String prdo = (String)product_spinner.getSelectedItem();






        JSONArray jsonAry = new JSONArray();
        jsonAry.put("value1");
        jsonAry.put("value2");
        jsonAry.put("value3");
JSONArray ottdta=  OttData();
JSONArray musicdata = MusictData();

        JSONObject jsonBody = new JSONObject();
        jsonBody.put("mobile_no",mobile);
        jsonBody.put("name", Name);
        jsonBody.put("birthDate", dayOfMonth);
        jsonBody.put("birthMonth", month);
        jsonBody.put("birthYear", year);
        jsonBody.put("city", selectedDistrict);
        jsonBody.put("state",selectedState);
        jsonBody.put( "gender", genders);
        jsonBody.put("emailId", Email);
        jsonBody.put("education",Education_);
        jsonBody.put( "occupation", Ocupation_);

        jsonBody.put( "durablesUsed", selectedOptionsString);
        jsonBody.put("shoppedOnline", chek);
        jsonBody.put("productsPurchasedOnline",  prdo);
        jsonBody.put("lastOnlineWatch", selectedValue);
        jsonBody.put("ottWatchLastYear", ottdta);
        jsonBody.put("musicAppsLastYear", musicdata);
        jsonBody.put("feedbackChoice",  choice);
jsonBody.put("latitude",lati);
        jsonBody.put("longitude",longi);

String userId= servic.formdatasend(jsonBody, "/user/create");
        try {
            JSONObject jsonResponse = new JSONObject(userId);
            int uId = jsonResponse.getInt("user_id");
            SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
            SharedPreferences.Editor myEdit = sharedPreferences.edit();

            myEdit.putInt("UID",uId);
            myEdit.putString("username",Name);

            myEdit.apply();
            Log.e("userid", String.valueOf(uId));
            if (uId!=0){
                return true;
            }else {
                return false;
            }

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
//        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
//        SharedPreferences.Editor myEdit = sharedPreferences.edit();
//
//        myEdit.putInt("UID",userId);
//        myEdit.putString("username",userName);
//
//        myEdit.apply();
//        Log.e("major",uid);


    }
    private static class DropdownItem {
        private String text;
        private boolean checked;

        public DropdownItem(String text, boolean checked) {
            this.text = text;
            this.checked = checked;
        }

        public String getText() {
            return text;
        }

        public boolean isChecked() {
            return checked;
        }

        public void setChecked(boolean checked) {
            this.checked = checked;
        }

        @Override
        public String toString() {
            return text;
        }
    }
    private void showDatePickerDialog() {
        // Get the current date
        Calendar calendar = Calendar.getInstance();
       year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
         dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        // Create a DatePickerDialog using the separate OnDateSetListener instance
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, dateSetListener, year, month, dayOfMonth);
        datePickerDialog.show();

    }
    public static boolean isValidName(String name ) {
        // Check if the name is null or empty
        if (name == null || name.trim().isEmpty()) {

            return false;
        }

        // Check if the name contains only letters or spaces
        for (char c : name.toCharArray()) {
            if (!Character.isLetter(c) && c != ' ') {
                return false;
            }
        }

        // Name is valid
        return true;
    }
    public static boolean isValidstate(String state) {
        // Check if the name is null or empty
       if(Objects.equals(state, "Select"))
       {
          return false;
       }

        // Name is valid
        return true;
    }
    public static boolean isvalidcheck(List<String>  selectedOptions){

        if (!selectedOptions.isEmpty()) {
            // The selectedOptions ArrayList is not empty
            // Your code here
            Log.e("jgiohfo","empty");
             return true ;

        } else {
            // The selectedOptions ArrayList is empty
            // Your code here
            return false;
        }
    }


    public static boolean isValidcity(String city) {
        // Check if the name is null or empty
        if(Objects.equals(city, "Select"))
        {
            return false;
        }

        // Name is valid
        return true;
    }
    public static boolean isValidgender(String gender) {
        // Check if the name is null or empty
        if(Objects.equals(gender, "Gender"))
        {
            return false;
        }

        // Name is valid
        return true;
    }


    public static boolean isValidEmail(String email) {
        // Check if the email is null or empty
        if (email == null || email.trim().isEmpty()) {
            return false;
        }

        // Check if the email matches the regular expression pattern
        Pattern pattern = Pattern.compile(EMAIL_REGEX);
        return pattern.matcher(email).matches();
    }
    public static boolean isValidage(int a, int b, int c) {
        return a != 0 && b != 0 && c != 0;
    }
    public static boolean isValideducation(String educatio) {
        // Check if the name is null or empty
        if(Objects.equals(educatio, "Education"))
        {
            return false;
        }

        // Name is valid
        return true;
    }
    public static boolean isValidocupation(String ocupton) {
        // Check if the name is null or empty
        if(Objects.equals(ocupton, "Ocupation"))
        {
            return false;
        }

        // Name is valid
        return true;
    }
    public static boolean isproduct(String prod) {
        // Check if the name is null or empty
        if(prod=="Products")
        {
            return false;
        }

        // Name is valid
        return true;
    }
    public static boolean isradios(String selection) {


        if (Objects.equals(selection, "not_selected")) {
    return false;
        } else {
            // No RadioButton is selected
            // Handle the case when no option is selected
        return true;
        }

    }

    public boolean isAnySwitchottChecked(Switch[] switches) {
        for (Switch aSwitch : switches) {
            if (aSwitch.isChecked()) {
                return true; // At least one switch is checked
            }
        }
        return false; // No switches are checked
    }


}