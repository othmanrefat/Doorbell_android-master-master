package com.example.ref3t.doorbell;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


public class MainActivity extends ActionBarActivity {
    public final static String Key = "100";
    private Firebase ref;//refrence of link firebase of Employee
    private Firebase vistor;//refrence of link firebase of History visitor
    String str_name = "";
    String[] string_tokanizer;
    StringBuffer buffer;
    Map<String, String> push_to_firebase = new HashMap<String, String>();
    String str_id = "";
    String[] tokenid;
    StringBuffer bufferid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //Button visitor click
    public void visitorClickedButton(View view) {
        Intent intent = new Intent(this, Employee.class);//intent to open new activity Employee
        intent.putExtra("key", "visitor");//send data toEmployee activity to how click on button
        startActivity(intent);//start of activity
    }//end button

    //Button Delivary click
    public void DelivaryClickedButton(View view) {
        Intent intent = new Intent(this, Employee.class);//intent to open new activity Employee
        intent.putExtra("key", "delivary");//send data toEmployee activity to how click on button
        startActivity(intent);//start of activity
    }//end button

    //Button Interview click
    public void interClickedButton(View view) {
        interVeiw();//call function to push notification to inter view group
    }//end button

    //function to push the interview group to data base history and push notification to interview group
    public void interVeiw() {
        Firebase.setAndroidContext(getApplication().getApplicationContext());
        ref = new Firebase("https://doorbellyamsafer.firebaseio.com/Interview");//refrence  of firbase interview
        vistor = new Firebase("https://doorbellyamsafer.firebaseio.com/DataVistor");
        final Firebase addhistory = vistor.child("History");
        buffer = new StringBuffer();
        bufferid = new StringBuffer();
        //listener to add the name and time and type to database and push notify to list of interview group
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                buffer.append(snapshot.getValue());
                bufferid.append(snapshot.getValue());
                str_name = buffer.toString();
                str_id = bufferid.toString();
                buffer.setLength(0);
                bufferid.setLength(0);
                string_tokanizer = str_name.split(",");
                tokenid = str_id.split(",");
                Calendar calender = Calendar.getInstance();
                SimpleDateFormat form = new SimpleDateFormat("yyyy-MM-dd      HH:mm:ss");
                //loop to get name in firbase and add the name and type data or time to database
                for (int index = 0; index < string_tokanizer.length; index++) {
                    if (string_tokanizer[index].contains("name=")) {
                        int indeex1 = string_tokanizer[index].indexOf("=");
                        string_tokanizer[index] = string_tokanizer[index].substring(indeex1 + 1, string_tokanizer[index].indexOf("}"));
                        //Toast.makeText(getBaseContext(), string_tokanizer[index]+"  ", Toast.LENGTH_SHORT).show();                         ;
                        String date_time = form.format(calender.getTime());
                        date_time = date_time.replaceAll(" ", "-");
                        push_to_firebase.put("name", string_tokanizer[index]);
                        push_to_firebase.put("type", "Interview");
                        push_to_firebase.put("Time", date_time);
                        addhistory.push().setValue(push_to_firebase);
                    }//end if
                    //if statment to get token and push notify to list interview   token
                    if (tokenid[index].contains("token=")) {
                        int indeex2 = tokenid[index].lastIndexOf("token=");
                        tokenid[index] = tokenid[index].substring(indeex2 + 6, tokenid[index].length());
                        Toast.makeText(getBaseContext(), tokenid[index], Toast.LENGTH_SHORT).show();
                    }//end if
                }//end loop
            }

            @Override
            public void onCancelled(FirebaseError error) {

            }

        });
    }
}
