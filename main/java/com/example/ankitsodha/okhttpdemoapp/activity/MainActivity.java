package com.example.ankitsodha.okhttpdemoapp.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.IntegerRes;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ankitsodha.okhttpdemoapp.R;
import com.example.ankitsodha.okhttpdemoapp.adapter.ContactAdapter;
import com.example.ankitsodha.okhttpdemoapp.model.Contact;
import com.example.ankitsodha.okhttpdemoapp.other.ServerCall;
import com.example.ankitsodha.okhttpdemoapp.other.UrlConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1;
    EditText etName, etNumber;
    Button btnSave, btnDelete, btnCancel;
    RecyclerView recyclerView;

    ArrayList<Contact> contactArrayList;
    Contact contact;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etName = (EditText) findViewById(R.id.etName);
        etNumber = (EditText) findViewById(R.id.etNumber);

        btnSave = (Button) findViewById(R.id.btnSave);
        btnDelete = (Button) findViewById(R.id.btnDelete);
        btnCancel = (Button) findViewById(R.id.btnCancel);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        String url= UrlConstants.BASE_URL+UrlConstants.SELECT;
        //if(ServerCall.isNetworkConnected(getApplicationContext())) {
            setContactAdapter(url);
        /*}else{
            showAlertDialog();
        }*/

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = etName.getText().toString();
                String number = etNumber.getText().toString();
                String command = btnSave.getText().toString();
                if (command.equals("Save")) {
                    contact = new Contact(name, number);
                    String url=UrlConstants.BASE_URL+UrlConstants.INSERT;
                    insertContact(url,contact);
                } else if (contact != null) {
                    contact.setName(name);
                    contact.setNumber(number);
                    String url=UrlConstants.BASE_URL+UrlConstants.UPDATE;
                    updateContact(url,contact);
                }
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (contact != null) {
                    String url=UrlConstants.BASE_URL+UrlConstants.DELETE;
                    deleteContact(url,contact);
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetAllViews();
            }
        });
    }

    private void showAlertDialog() {
        new AlertDialog.Builder(this)
                .setTitle("No Internet")
                .setMessage("Please Connect To Internet then Press OK.")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String url= UrlConstants.BASE_URL+UrlConstants.SELECT;
                        if(ServerCall.isNetworkConnected(getApplicationContext())) {
                            setContactAdapter(url);
                        }else{
                            showAlertDialog();
                        }
                    }
                })
                .show();
    }

    private void deleteContact(String url, final Contact contact) {
        (new AsyncTask<String, Void, String>(){

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                pd=ProgressDialog.show(MainActivity.this,"","");
            }

            @Override
            protected String doInBackground(String... strings) {
                HashMap<String,String> hashmap=new HashMap<String, String>();
                hashmap.put("id",String.valueOf(contact.getId()));
                String result= ServerCall.getJsonFromUrl(strings[0],hashmap);
                return result;
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                pd.dismiss();
                int count=Integer.parseInt(result.trim());
                if(count>0) {
                    resetAllViews();
                    Toast.makeText(MainActivity.this, "Response : " + result, Toast.LENGTH_SHORT).show();
                    String url = UrlConstants.BASE_URL + UrlConstants.SELECT;
                    setContactAdapter(url);
                }
            }
        }).execute(url);
    }

    private void updateContact(String url,final Contact contact) {
        (new AsyncTask<String, Void, String>(){

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                pd=ProgressDialog.show(MainActivity.this,"","");
            }

            @Override
            protected String doInBackground(String... strings) {
                HashMap<String,String> hashmap=new HashMap<String, String>();
                hashmap.put("id",String.valueOf(contact.getId()));
                hashmap.put("name",contact.getName());
                hashmap.put("number",contact.getNumber());
                String result= ServerCall.getJsonFromUrl(strings[0],hashmap);
                return result;
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                pd.dismiss();
                int count=Integer.parseInt(result.trim());
                if(count>0) {
                    resetAllViews();
                    Toast.makeText(MainActivity.this, "Response : " + result, Toast.LENGTH_SHORT).show();
                    String url = UrlConstants.BASE_URL + UrlConstants.SELECT;
                    setContactAdapter(url);
                }
            }
        }).execute(url);
    }

    private void setContactAdapter(String url) {
        (new AsyncTask<String, Void, String>(){

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                pd=ProgressDialog.show(MainActivity.this,"","");
            }

            @Override
            protected String doInBackground(String... strings) {
                String result= ServerCall.getJsonFromUrl(strings[0],null);
                return result;
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                pd.dismiss();
                try {
                    JSONObject jsonObject=new JSONObject(result);
                    JSONArray jsonArray=jsonObject.getJSONArray("data");
                    contactArrayList=new ArrayList<Contact>();
                    for(int i=0;i<jsonArray.length();i++)
                    {
                        jsonObject=jsonArray.getJSONObject(i);
                        Contact contact=new Contact();
                        contact.setId(Integer.parseInt(jsonObject.getString("id")));
                        contact.setName(jsonObject.getString("name"));
                        contact.setNumber(jsonObject.getString("number"));
                        contactArrayList.add(contact);
                    }
                    ContactAdapter adapter = new ContactAdapter(contactArrayList, MainActivity.this);
                    recyclerView.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }).execute(url);
    }

    private void insertContact(String url,final Contact contact) {
        (new AsyncTask<String, Void, String>(){

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                pd=ProgressDialog.show(MainActivity.this,"","");
            }

            @Override
            protected String doInBackground(String... strings) {
                HashMap<String,String> hashmap=new HashMap<String, String>();
                hashmap.put("name",contact.getName());
                hashmap.put("number",contact.getNumber());
                String result= ServerCall.getJsonFromUrl(strings[0],hashmap);
                return result;
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                pd.dismiss();
                int count=Integer.parseInt(result.trim());
                if(count>0) {
                    resetAllViews();
                    Toast.makeText(MainActivity.this, "Response : " + result, Toast.LENGTH_SHORT).show();
                    String url = UrlConstants.BASE_URL + UrlConstants.SELECT;
                    setContactAdapter(url);
                }
            }
        }).execute(url);
    }

    private void resetAllViews() {
        etName.setText("");
        etNumber.setText("");
        etName.requestFocus();
        btnSave.setText("Save");
        btnDelete.setVisibility(View.GONE);
        contact = null;
    }

    public void setContactData(Contact contact) {
        this.contact = contact;
        etName.setText(contact.getName());
        etNumber.setText(contact.getNumber());
        btnSave.setText("Update");
        btnDelete.setVisibility(View.VISIBLE);
    }

    public void makeCall(String number) {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CALL_PHONE},
                    MY_PERMISSIONS_REQUEST_CALL_PHONE);
            return;
        }
        String uri = "tel:" + number;
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse(uri));
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==MY_PERMISSIONS_REQUEST_CALL_PHONE)
        {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                Toast.makeText(MainActivity.this, "Thank you!!! Now Try Again To Call", Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(MainActivity.this, "Sorry We Can't Call!!!", Toast.LENGTH_SHORT).show();
                // permission denied, boo! Disable the
                // functionality that depends on this permission.
            }
            return;
        }
    }


}
