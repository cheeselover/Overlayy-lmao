package solutions.overlayylmao.overlayylmao;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddPresetActivity extends AppCompatActivity {

    @Bind(R.id.title)
    EditText title;

    @Bind(R.id.updateTime)
    EditText refresh;

    @Bind(R.id.horizontalGravity)
    EditText hgravity;

    @Bind(R.id.verticalGravity)
    EditText vgravity;

    @Bind(R.id.xOffset)
    EditText xoffset;

    @Bind(R.id.yOffset)
    EditText yoffset;

    @Bind(R.id.width)
    EditText width;

    @Bind(R.id.height)
    EditText height;

    @Bind(R.id.rotation)
    EditText rotation;

    @Bind(R.id.scaleX)
    EditText scalex;

    @Bind(R.id.scaleY)
    EditText scaley;

    @Bind(R.id.coverStatusBar)
    SwitchCompat coverStatus;

    @Bind(R.id.coverNavBar)
    SwitchCompat coverNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_preset);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.create)
    void create() {
        JSONObject object = new JSONObject();
        try {
            object.put("title", title.getText().toString());
            object.put("updateTime", Integer.parseInt(refresh.getText().toString()));
            object.put("horizontalGravity", Integer.parseInt(hgravity.getText().toString()));
            object.put("verticalGravity", Integer.parseInt(vgravity.getText().toString()));
            object.put("xOffset", Integer.parseInt(xoffset.getText().toString()));
            object.put("yOffset", Integer.parseInt(yoffset.getText().toString()));
            object.put("width", Integer.parseInt(width.getText().toString()));
            object.put("height", Integer.parseInt(height.getText().toString()));
            object.put("rotation", Integer.parseInt(rotation.getText().toString()));
            object.put("scaleX", Integer.parseInt(scalex.getText().toString()));
            object.put("scaleY", Integer.parseInt(scaley.getText().toString()));
            object.put("coverStatusBar", coverStatus.isChecked());
            object.put("coverNavBar", coverNav.isChecked());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, "https://overlayylmao.herokuapp.com/presets", object, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                setResult(RESULT_OK);
                finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AddPresetActivity.this, "oops", Toast.LENGTH_SHORT).show();
            }
        });
        Requester.getInstance(this).addToRequestQueue(request);
    }
}
