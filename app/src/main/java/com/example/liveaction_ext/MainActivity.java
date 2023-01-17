package com.example.liveaction_ext;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.liveaction_int.Dir_serve;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    Button Sbmt_btn,Access_btn;
    Spinner Education,Ocupation ;
    EditText et_abt;
    Dir_serve lets;
    StringBuilder result;
    Profile_data_stream pds;
    Pack_api pa;
    CheckBox tw_wlr,refrgtr,pc,Ac,colrtv,wshingmchine,car,agri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lets = new Dir_serve();
        Dialog dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.data);

        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);
        dialog.getWindow().getAttributes().windowAnimations = R.style.animation;


        Access_btn = dialog.findViewById(R.id.accessicibilty);
        Sbmt_btn= dialog.findViewById(R.id.submit);
        Education =dialog.findViewById(R.id.Edu_spn);
        Ocupation = dialog.findViewById(R.id.ocup_spn);
        tw_wlr=dialog.findViewById(R.id.ckbx_twler);
        refrgtr=dialog.findViewById(R.id.ckbx_refrigtr);
        wshingmchine=dialog.findViewById(R.id.ckbx_wshingmchin);
        car=dialog.findViewById(R.id.ckbx_car);
        colrtv=dialog.findViewById(R.id.ckbx_clrtv);
        agri=dialog.findViewById(R.id.ckbx_agri);
        pc=dialog.findViewById(R.id.ckbx_pc);
        Ac=dialog.findViewById(R.id.ckbx_ac);
        et_abt = dialog.findViewById(R.id.et_about);
        ///////
        ArrayAdapter<CharSequence> edu_adap=ArrayAdapter.createFromResource(this, R.array.Education, android.R.layout.simple_spinner_item);

        edu_adap.setDropDownViewResource(android.R.layout.simple_spinner_item);
        Education.setAdapter(edu_adap);
        ArrayAdapter<CharSequence>occu_adap=ArrayAdapter.createFromResource(this, R.array.Ocupation, android.R.layout.simple_spinner_item);

        occu_adap.setDropDownViewResource(android.R.layout.simple_spinner_item);
        Ocupation.setAdapter(occu_adap);
        result=new StringBuilder();

        if(agri.isChecked()){
            result.append("Agricultural,");
        }
        if(car.isChecked()){
            result.append("Car / Jeep/ Van,");
        }
        if(tw_wlr.isChecked()){
            result.append("two Wheeler,");
        }
        if(Ac.isChecked()){
            result.append("Air Conditioner,");
        }
        if(wshingmchine.isChecked()){
            result.append("Washing machine,");
        }
        if(colrtv.isChecked()){
            result.append("Color tv,");
        }
        if(refrgtr.isChecked()){
            result.append("Refrigerator,");
        }
        if(pc.isChecked()){
            result.append("Personal Computer / Laptop ,");
        }
pa = new Pack_api();

pds= new Profile_data_stream();


        Access_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{READ_EXTERNAL_STORAGE},
                        1);
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{WRITE_EXTERNAL_STORAGE},
                        2);

            }
        });

        Sbmt_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lets.letshit();


//                set.addAll(arrPackage);
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

                StrictMode.setThreadPolicy(policy);
                String education= (String)Education.getSelectedItem();
                String occupation= (String)Ocupation.getSelectedItem();
                String chekbox_choices=result.toString();
                String About = et_abt.getText().toString().trim();
                String U_abt= About;
                String U_edu=  education;
                String U_ocu=  occupation;
                String U_Chek =  chekbox_choices;
                String U_data = About+education+occupation+chekbox_choices;
//                String urlString = "https://perfect-eel-fashion.cyclic.app/user/create"; // URL to call dont touch
//                String urlString = "https://9f88-122-169-92-160.in.ngrok.io/user/create";//temp_url
                String urlString = About+"/user/create";
                try {
                JSONObject jsonBody = new JSONObject();
                jsonBody.put("about", U_abt);
                jsonBody.put("education", U_edu);
                jsonBody.put("occupation", U_ocu);
                jsonBody.put("durables_used", U_Chek);
                String respons = pds.datasender(urlString,jsonBody);              //calling Profile data stream
                    ArrayList pack=pa.pack_rule();

                    SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
                    SharedPreferences.Editor myEdit = sharedPreferences.edit();
                    Set<String> set = new HashSet<String>();
                    set.addAll(pack);

                    myEdit.putStringSet("APP_LIST", set);
                    myEdit.putString("endpt", About);

                    myEdit.putString("Userid", respons);

                    myEdit.apply();
                }
                catch (Exception e) {
                    Log.e("techh", String.valueOf(e));
                    System.out.println("Error in api calling: "+ String.valueOf(e));
                }
                startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS));

            }

            });

        dialog.show();
    }
}