package edu.duke.ece651.team12.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import edu.duke.ece651.team12.R;
import edu.duke.ece651.team12.bean.GameItem;
import edu.duke.ece651.team12.bean.GameList;

public class GameAdapter extends BaseAdapter {
    private Context mContext;
    private GameList mGameList;

    public GameAdapter(Context mContext, GameList mGameList) {
        this.mContext = mContext;
        this.mGameList = mGameList;
    }

    @Override
    public int getCount() {
        return mGameList.geGametListCount();
    }

    @Override
    public Object getItem(int i) {
        return mGameList.getGameItem(i);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        //getView方法中的三个参数，position是指现在是第几个条目；convertView是旧视图，就是绘制好了的视图；parent是父级视图，也就是ListView之类的
        //加载布局管理器
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.item_game_base, null);

        TextView tv_game_seq_num = view.findViewById(R.id.tv_game_seq_num);
        TextView tv_num_player = view.findViewById(R.id.tv_num_player);
        GameItem mGameItem = mGameList.getGameItem(position);
        tv_game_seq_num.setText(mGameItem.getGameSeqNumber());
        tv_num_player.setText(mGameItem.getNumPlayer());

        return view;
    }
}
