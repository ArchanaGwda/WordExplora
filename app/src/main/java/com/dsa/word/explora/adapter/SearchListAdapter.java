package com.dsa.word.explora.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.dsa.word.explora.R;
import com.dsa.word.explora.activities.SearchActivity;
import com.dsa.word.explora.model.SearchResponse;

import java.util.List;

public class SearchListAdapter extends RecyclerView.Adapter<SearchListAdapter.MyViewHolder> {

    private final Context context;
    private List<SearchResponse> searchResponses;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView title;
        private TextView description;
        private ImageView thumbnail;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            description = (TextView) view.findViewById(R.id.description);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
        }
    }


    public SearchListAdapter(List<SearchResponse> searchResponses, Context context) {
        this.searchResponses = searchResponses;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.search_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        SearchResponse searchResponse = searchResponses.get(position);
        holder.title.setText(searchResponse.getTitle());
        holder.description.setText(searchResponse.getDescription());
        if (searchResponse.getThumbnail() != null) {
            Glide.with(context)
                    .load(searchResponse.getThumbnail())
                    .into(holder.thumbnail);
        }
    }

    @Override
    public int getItemCount() {
        return searchResponses.size();
    }
}
