package com.dsa.word.explora.activities;


import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dsa.word.explora.R;
import com.dsa.word.explora.adapter.SearchListAdapter;
import com.dsa.word.explora.fragments.AppConstants;
import com.dsa.word.explora.fragments.WebViewActivity;
import com.dsa.word.explora.model.SearchResponse;
import com.dsa.word.explora.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity implements AppConstants {

    private static final String TAG = "SearchActivity";
    private MenuItem searchMenuItem;
    private SearchView searchView;
    private TextView textView;
    private RecyclerView searchViewRecycler;
    private SearchListAdapter searchListAdapter;
    List<SearchResponse> searchResponses = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        textView = findViewById(R.id.text_view);
        searchViewRecycler = findViewById(R.id.search_view);
        searchViewRecycler.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), searchViewRecycler, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent webViewIntent = new Intent(SearchActivity.this, WebViewActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(TITLE, searchResponses.get(position).getTitle());
                bundle.putString(URL, EXTRAS_DETAIL_URL + +searchResponses.get(position).getPageId());
                webViewIntent.putExtras(bundle);
                startActivity(webViewIntent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);
        searchMenuItem = menu.findItem(R.id.search);
        searchView = (SearchView) searchMenuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                getResults(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (!newText.isEmpty()) {
                    getResults(newText);
                } else {
                    Toast.makeText(SearchActivity.this, "Search field can't be empty", Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });
        return true;
    }

    /**
     * Gets the results for searched text
     *
     * @param query test to be searched
     */
    private void getResults(String query) {
        if (Utils.isInternetAvailable(SearchActivity.this)) {
            if (!query.isEmpty()) {
                //Make api call
                RequestQueue queue = Volley.newRequestQueue(SearchActivity.this);
                String finalUrl = "https://en.wikipedia.org//w/api.php?action=query&format=json&prop=pageimages%7Cpageterms&generator=prefixsearch&redirects=1&formatversion=2&piprop=thumbnail&pithumbsize=50&pilimit=10&wbptterms=description&gpslimit=10&gpssearch=" + query;
                StringRequest stringRequest = new StringRequest(Request.Method.GET, finalUrl,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.d(TAG, "RESPONSE: " + response);
                                handleResponse(response);
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "ERROR: " + error);
                    }
                });
                queue.add(stringRequest);
            } else {
                Toast.makeText(SearchActivity.this, "Search field can't be empty", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(SearchActivity.this, R.string.check_internet_connection, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Handles the wiki response
     *
     * @param response response from wiki api
     */
    private void handleResponse(String response) {
        try {
            JSONObject objResult = new JSONObject(response);
            JSONObject responseQuery = objResult.getJSONObject("query");
            JSONArray pagesArray = responseQuery.getJSONArray("pages");
            Log.d(TAG, "RESPONSE: " + pagesArray);
            searchResponses.clear();
            if (pagesArray != null) {
                for (int i = 0; i < pagesArray.length(); i++) {
                    JSONObject page = pagesArray.getJSONObject(i);
                    SearchResponse searchResponse = new SearchResponse();
                    searchResponse.setPageId(page.getInt("pageid"));
                    searchResponse.setTitle(page.getString("title"));
                    if (page.has("thumbnail")) {
                        JSONObject thumbnail = page.getJSONObject("thumbnail");
                        searchResponse.setThumbnail(thumbnail.getString("source"));
                    }
                    if (page.has("terms")) {
                        JSONObject terms = page.getJSONObject("terms");
                        JSONArray description = terms.getJSONArray("description");
                        String desc = description.getString(0);
                        searchResponse.setDescription(desc);
                    }
                    searchResponses.add(searchResponse);
                }
            }

            if (searchResponses.size() > 0) {
                textView.setVisibility(View.GONE);
            }
            updateListView(searchResponses);

        } catch (JSONException e) {
            Log.e(TAG, "JSONException: " + e);
        }
    }

    /**
     * Updates the listView
     *
     * @param searchResponses model class with list data
     */
    private void updateListView(List<SearchResponse> searchResponses) {

        searchListAdapter = new SearchListAdapter(searchResponses, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        searchViewRecycler.setLayoutManager(mLayoutManager);
        searchViewRecycler.setItemAnimator(new DefaultItemAnimator());
        searchViewRecycler.setAdapter(searchListAdapter);
        searchListAdapter.notifyDataSetChanged();
    }


    /**
     * Class to handle the click event of recyclerView
     */
    private static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }

        public interface ClickListener {
            void onClick(View view, int position);

            void onLongClick(View view, int position);
        }
    }
}
