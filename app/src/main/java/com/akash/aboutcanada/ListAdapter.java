package com.akash.aboutcanada;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by akash on 8/03/18.
 */

/* Creating Custome List Adapter class to populate data model to listview
 */

public class ListAdapter extends BaseAdapter {
    LayoutInflater layoutInflater;
    List<CanadianFact> factList;
    Context context;
    int mLastPosition;
    int numberOfItems = 4;

    public ListAdapter(List<CanadianFact> factList, Context context) {
        this.factList = factList;
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return factList.size();
    }

    @Override
    public Object getItem(int position) {
        return factList.get(position);
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            view = layoutInflater.inflate(R.layout.list_layout, viewGroup, false);
            viewHolder = new ViewHolder();
            viewHolder.title = (TextView) view.findViewById(R.id.textview_title);
            viewHolder.description = (TextView) view.findViewById(R.id.description);
            viewHolder.imageView = (ImageView) view.findViewById(R.id.imageview);
            view.setTag(viewHolder);
        } else viewHolder = (ViewHolder) view.getTag();
        if (factList.get(position).getTitle().equalsIgnoreCase("null")){
            viewHolder.title.setText("");
        } else viewHolder.title.setText(factList.get(position).getTitle());
        //Replacing text null to empty because text null doesn't look impressive on user interface
        if (factList.get(position).getDescription().equalsIgnoreCase("null")) {
            viewHolder.description.setText("");
        } else viewHolder.description.setText(factList.get(position).getDescription());


// using Glide library loads images from URL without causing OUT Of Memory error
        Glide.with(context).load(factList.get(position).getImageLink()).into(viewHolder.imageView);


        float initialTranslation = (mLastPosition <= position ? 500f : -500f);

        view.setTranslationY(initialTranslation);
        view.animate()
                .setInterpolator(new DecelerateInterpolator(1.0f))
                .translationY(0f)
                .setDuration(300l)
                .setListener(null);

        // Keep track of the last position we loaded
        mLastPosition = position;

        return view;

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    //defining a view holder class to hold view for each row
    private class ViewHolder {
        TextView title, description;
        ImageView imageView;
    }


    public void addItems(List<CanadianFact> facts){
       // factList.addAll(facts);
        for (CanadianFact fact: facts){
            factList.add(0,fact);
        }

        notifyDataSetChanged();
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        Log.d("newData"," "+factList.size());
    }
}