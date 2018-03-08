package com.akash.aboutcanada;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

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
    public ListAdapter(List<CanadianFact> factList,Context context){
        this.factList=factList;
        this.context=context;
        layoutInflater=LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return  factList.size();
    }

    @Override
    public Object getItem(int position) {
        return factList.get(position);
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view==null){
            view=layoutInflater.inflate(R.layout.list_layout,viewGroup,false);
            viewHolder=new ViewHolder();
            viewHolder.title=(TextView) view.findViewById(R.id.textview_title);
            viewHolder.description=(TextView) view.findViewById(R.id.description);
            viewHolder.imageView=(ImageView) view.findViewById(R.id.imageview);
            view.setTag(viewHolder);
        } else viewHolder=(ViewHolder) view.getTag();
        viewHolder.title.setText(factList.get(position).getTitle());
        viewHolder.description.setText(factList.get(position).getDescription());
        viewHolder.imageView.setImageDrawable(factList.get(position).getImage());

        return view;

    }
    @Override
    public long getItemId(int position){
        return position;
    }
//defining a view holder class to hold view for each row
    private class ViewHolder{
        TextView title, description;
        ImageView imageView;
    }
}
