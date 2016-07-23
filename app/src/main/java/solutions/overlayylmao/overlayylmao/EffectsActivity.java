package solutions.overlayylmao.overlayylmao;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class EffectsActivity extends AppCompatActivity {

    @Bind(R.id.preset_list_view)
    RecyclerView mPresetRecyclerView;

    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;
    JSONArray mPresets;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_effects);
        ButterKnife.bind(this);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mPresetRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mPresetRecyclerView.setLayoutManager(mLayoutManager);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, "https://overlayylmao.herokuapp.com/presets", null, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                mAdapter = new PresetAdapter(response);
                mPresetRecyclerView.setAdapter(mAdapter);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        Requester.getInstance(this).addToRequestQueue(jsonArrayRequest);
    }
}
