package com.bridj.codingchallenge;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class MainActivity extends AppCompatActivity implements EventsAdapter.OnEventClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private List<Event> events = new ArrayList<>();

    RecyclerView rvEvents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rvEvents = findViewById(R.id.rvEvents);
        rvEvents.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvEvents.setLayoutManager(linearLayoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rvEvents.getContext(),
                linearLayoutManager.getOrientation());
        rvEvents.addItemDecoration(dividerItemDecoration);

        requestData();
    }

    private void requestData() {
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();

        JsonObjectRequest eventsRequest = new JsonObjectRequest(Request.Method.GET, AppConstants.EVENTS_JSON_URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                pDialog.hide();

                try {
                    JSONArray eventsData = response.getJSONArray("events");

                    GsonBuilder gsonBuilder = new GsonBuilder();
                    gsonBuilder.registerTypeAdapter(Date.class, new DateDeserializer());

                    Gson gson = gsonBuilder.create();
                    List<Event> eventList = gson.fromJson(eventsData.toString(), new TypeToken<List<Event>>() {
                    }.getType());
                    filterData(eventList);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                pDialog.hide();
            }
        });
        MainApplication.getInstance().addToRequestQueue(eventsRequest);

    }

    private void filterData(List<Event> eventList) {
        Iterator<Event> itr = eventList.iterator();

        int prevTotal = eventList.size();
        //remove events with zero seats
        while (itr.hasNext()) {
            Event event = itr.next();
            if (event.getAvailableSeats() <= 0)
                itr.remove();

        }
        Log.d(TAG, "Total Number of Events :: " + prevTotal);
        Log.d(TAG, "Number of Events that has seats available :: " + eventList.size());
        Log.d(TAG, "Number of Events removed by filter :: " + (prevTotal - eventList.size()));

        //sort data by date
        Collections.sort(eventList, new Comparator<Event>() {
            @Override
            public int compare(Event event, Event t1) {
                return event.getDate().compareTo(t1.getDate());
            }
        });

        this.events = eventList;
        loadAdapter();
    }

    private void loadAdapter() {
        EventsAdapter eventsAdapter = new EventsAdapter(events, this);
        rvEvents.setAdapter(eventsAdapter);
    }

    @Override
    public void onClickEvent(Event event) {
        Toast.makeText(this, event.getName(), Toast.LENGTH_SHORT).show();
    }
}
