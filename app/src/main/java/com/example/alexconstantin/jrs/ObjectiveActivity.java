package com.example.alexconstantin.jrs;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

public class ObjectiveActivity extends AppCompatActivity {

    private String TAG = ObjectiveActivity.class.getSimpleName();
    TextView objectiveTitle;
    TextView objectiveType;
    TextView objectiveDescription;
    TextView objectiveHTML;

    String HTML;
    String Description;
    Integer ObjectiveId;

    private static String descriptionUrl = "http://www.jrsdr.robertoderesu.com/api/objective";
    private static String htmlUrl = "http://www.jrsdr.robertoderesu.com/api/objectiveHtml";

    GetObjectiveHTML getObjectiveHTML;
    GetObjectiveDescription getObjectiveDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_objective);


        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        Bundle b = getIntent().getExtras();
        ObjectiveId = -1;
        String objType = "";
        String objTitle = "";
        if(b != null) {
            ObjectiveId = b.getInt("objectiveId");
            objTitle = b.getString("objectiveTitle");
            objType = b.getString("objectiveType");
        }

        objectiveTitle = (TextView) findViewById(R.id.objectiveTitle);
        objectiveTitle.setText(objTitle);

        objectiveType = (TextView) findViewById(R.id.objectiveType);
        objectiveType.setText(objType);

        objectiveDescription = (TextView) findViewById(R.id.objectiveDescription);
        objectiveHTML = (TextView) findViewById(R.id.objectiveHTML);

        getObjectiveHTML = new GetObjectiveHTML();
        getObjectiveHTML.execute();

        getObjectiveDescription = new GetObjectiveDescription();
        getObjectiveDescription.execute();
    }

    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        if (id==android.R.id.home) {
            Intent i=new Intent(this,MainActivity.class);
            startActivity(i);
        }
        return true;
    }

    class GetObjectiveDescription extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            ObiectivRequest request = new ObiectivRequest();
            request.setObjectiveId(ObjectiveId);
            String jsonStr = sh.makeServiceCall(descriptionUrl, request);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    Description = jsonObj.getJSONObject("objective").getString("description");

                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });

                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            objectiveDescription.setText(Description);
        }

    }

    class GetObjectiveHTML extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            ObiectivRequest request = new ObiectivRequest();
            request.setObjectiveId(ObjectiveId);
            String jsonStr = sh.makeServiceCall(htmlUrl, request);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    HTML = jsonObj.getString("html");

                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });

                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            objectiveHTML.setText(Html.fromHtml(HTML));
        }

    }

}
