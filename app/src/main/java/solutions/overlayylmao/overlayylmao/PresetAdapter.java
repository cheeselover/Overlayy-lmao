package solutions.overlayylmao.overlayylmao;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PresetAdapter extends RecyclerView.Adapter<PresetAdapter.ViewHolder> {
    private JSONArray mDataset;
    private RecyclerView mRecyclerView;
    private Context mParentContext;
    private PresetOnClickListener mClickListener;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mTextView;
        public ViewHolder(TextView v) {
            super(v);
            mTextView = v;
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public PresetAdapter(JSONArray myDataset, Context parentContext, RecyclerView recyclerView) {
        mDataset = myDataset;
        mParentContext = parentContext;
        mRecyclerView = recyclerView;
        mClickListener = new PresetOnClickListener();
    }

    // Create new views (invoked by the layout manager)
    @Override
    public PresetAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        TextView v = (TextView) LayoutInflater.from(parent.getContext()).inflate(R.layout.preset_text_view, parent, false);
        v.setOnClickListener(mClickListener);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        String text = "UNKNOWN PRESET";
        try {
            text = mDataset.getJSONObject(position).getString("title");
        } catch(JSONException e) { }

        holder.mTextView.setText(text);

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.length();
    }

    private class PresetOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            try {
                JSONObject presetJson = mDataset.getJSONObject(mRecyclerView.getChildAdapterPosition(v));
                Preset preset = new Preset();

                preset.coverStatusBar = presetJson.getBoolean("coverStatusBar");
                preset.coverNavBar = presetJson.getBoolean("coverNavBar");
                preset.verticalGravity = presetJson.getInt("verticalGravity");
                preset.horizontalGravity = presetJson.getInt("horizontalGravity");
                preset.height = presetJson.getInt("height");
                preset.width = presetJson.getInt("width");
                preset.updateTime = presetJson.getInt("updateTime");
                preset.xOffset = presetJson.getInt("xOffset");
                preset.yOffset = presetJson.getInt("yOffset");
                preset.rotation = presetJson.getInt("rotation");
                preset.scaleX = presetJson.getInt("scaleX");
                preset.scaleY = presetJson.getInt("scaleY");

                Intent intent = new Intent();
                intent.putExtra(OverlayService.EXTRA_PRESET, preset);
                Activity parentActivity = (Activity) mParentContext;
                parentActivity.setResult(Activity.RESULT_OK, intent);
                parentActivity.finish();
            } catch(JSONException e) { }
        }
    }
}
