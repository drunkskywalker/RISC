package edu.duke.ece651.team12.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;

import java.util.List;

import edu.duke.ece651.team12.R;

public class GridViewAdapter extends BaseAdapter {
    List<String> mList;
    Context mContext;

    public GridViewAdapter(List<String> mList, Context mContext) {
        this.mList = mList;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View view = layoutInflater.inflate(R.layout.item_unit_init,null);

        EditText et_unit_init = view.findViewById(R.id.et_unit_init);
        String territory_str = "Territory " + (Integer.parseInt(mList.get(position)) + 1);
        System.out.println("GridView=======================");
        System.out.println(territory_str);
        et_unit_init.setHint(territory_str);

        return view;
    }
}
