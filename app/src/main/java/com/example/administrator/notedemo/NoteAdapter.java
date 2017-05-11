package com.example.administrator.notedemo;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Administrator on 2017/5/3.
 */

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> {
        private LayoutInflater inflater;
        private List<Note> list;
        private Context mContext;

    public NoteAdapter(Context context, List<Note> noteList) {
        list = noteList;
        mContext = context;
        inflater = LayoutInflater.from(mContext);
    }


    @Override
    public NoteAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view  = inflater.inflate(R.layout.note_item,
                parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Note note = list.get(position);
        holder.tv_title.setText(note.getTitle());
        holder.tv_text.setText(note.getContent());
        holder.iv_image.setImageBitmap(BitmapFactory.decodeFile(note.getImage_url()));

    }
    @Override
    public int getItemCount() {
        return list.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tv_title;
        TextView tv_text;
        ImageView iv_image;
        public ViewHolder(View view) {
            super(view);
            tv_title = (TextView) view.findViewById(R.id.tv_title);
            tv_text = (TextView) view.findViewById(R.id.tv_text);
            iv_image = (ImageView) view.findViewById(R.id.iv_image);
        }
    }

}
