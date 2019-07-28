package com.example.c302_p07_addressbook;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;


public class ViewContactDetailsActivity extends AppCompatActivity {

    private EditText etFirstName, etLastName, etMobile;
    private Button btnUpdate, btnDelete;
    private int contactId;
    private AsyncHttpClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_contact_details);

        etFirstName = findViewById(R.id.etFirstName);
        etLastName = findViewById(R.id.etLastName);
        etMobile = findViewById(R.id.etMobile);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnDelete = findViewById(R.id.btnDelete);

        Intent intent = getIntent();
        contactId = intent.getIntExtra("contact_id", -1);

        client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add("id" , String.valueOf(contactId));
        //TODO: call getContactDetails.php with the id as a parameter
        //TODO: set the text fields with the data retrieved

        client.get("http://10.0.2.2/C302_P07/getContactById.php" , params,  new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        try {
                            Log.i("JSON Results: ", response.toString());

                                String firstName = response.getString("firstname");
                                String lastname = response.getString("lastname");
                                String mobile = response.getString("mobile");
                                etFirstName.setText(firstName);
                                etLastName.setText(lastname);
                                etMobile.setText(mobile);



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

                btnUpdate.setOnClickListener(new View.OnClickListener()

            {
                @Override
                public void onClick (View v){
                btnUpdateOnClick(v);
            }
            });

                btnDelete.setOnClickListener(new View.OnClickListener()

            {
                @Override
                public void onClick (View v){
                btnDeleteOnClick(v);
            }
            });
        }//end onCreate


        private void btnUpdateOnClick (View v){
            //TODO: retrieve the updated text fields and set as parameters to be passed to updateContact.php
            String fn = etFirstName.getText().toString();
            String ln = etLastName.getText().toString();
            String mn = etMobile.getText().toString();
            if(fn.length() == 0 || ln.length() == 0 || mn.length() == 0){
                Toast.makeText(getApplicationContext(), "Fields cannot be empty", Toast.LENGTH_SHORT).show();

            }else {
                RequestParams params = new RequestParams();
                params.add("firstname", etFirstName.getText().toString());
                params.add("lastname", etLastName.getText().toString());
                params.add("mobile", etMobile.getText().toString());
                params.add("id", String.valueOf(contactId));

                client.post("http://10.0.2.2/C302_P07/updateContact.php", params, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        try {
                            Log.i("JSON Results: ", response.toString());
                            Toast.makeText(getApplicationContext(), response.getString("message"), Toast.LENGTH_SHORT).show();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
                finish();
            }

        }//end btnUpdateOnClick

        private void btnDeleteOnClick (View v){
            //TODO: retrieve the id and set as parameters to be passed to deleteContact.php
            RequestParams params = new RequestParams();
            params.add("id" , String.valueOf(contactId));
            client.post("http://10.0.2.2/C302_P07/deleteContacts.php", params, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    Log.i("JSON Results: ", response.toString());
                    Toast.makeText(getApplicationContext(), "Contact record is deleted successfully", Toast.LENGTH_SHORT).show();

                }
            });
            finish();


        }//end btnDeleteOnClick

    }
    //end class

