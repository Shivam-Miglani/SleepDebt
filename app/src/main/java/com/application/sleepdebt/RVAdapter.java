package com.application.sleepdebt;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zomadmin on 30/11/15.
 */
public class RVAdapter extends RecyclerView.Adapter<RVAdapter.LogViewHolder> {

    private final List<LogItem> listItems;
    Context context;

    public static class LogViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView textView;
        TextView textView2;
        TextView textView3;
        ImageView imageView;


        LogViewHolder(View rowView) {
            super(rowView);
            cv = (CardView)rowView.findViewById(R.id.cv);
            textView = (TextView) rowView.findViewById(R.id.firstLine);
            textView2 = (TextView) rowView.findViewById(R.id.secondLine);
            textView3 = (TextView) rowView.findViewById(R.id.timestamp);
            imageView = (ImageView) rowView.findViewById(R.id.icon);
        }
    }

    RVAdapter(List<LogItem> items,Context context){
        this.listItems = items;
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }


    @Override
    public LogViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.log_item, viewGroup, false);
        LogViewHolder lvh = new LogViewHolder(v);
        return lvh;
    }


    @Override
    public void onBindViewHolder(LogViewHolder logViewHolder, int position) {

        logViewHolder.textView.setText(listItems.get(position).firstLine);
        logViewHolder.textView2.setText(listItems.get(position).secondLine);
        logViewHolder.textView3.setText(listItems.get(position).currentTime);
        String s = listItems.get(position).firstLine;
        if (s.startsWith("Sleep Debt: 0 Hr : 0") || s.startsWith("Sleep Debt: -"))
        {
            logViewHolder.imageView.setImageResource(R.drawable.ic_mountain5);
        } else {
            logViewHolder.imageView.setImageResource(R.drawable.ic_tent);
        }
        logViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar snackbar = Snackbar
                        .make(v, "Swipe to delete the log item.", Snackbar.LENGTH_LONG);

                snackbar.show();
            }

        });

    }
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        //swipe out recycler view
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {

            @Override
            public boolean onMove(
                    final RecyclerView recyclerView,
                    final RecyclerView.ViewHolder viewHolder,
                    final RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                //Remove swiped item from list and notify the RecyclerView
                remove(viewHolder.getAdapterPosition());
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

    }

    public void remove(int position) {
        DatabaseHandler db = new DatabaseHandler(context);
        int id =listItems.get(position).getId();
        listItems.remove(position);
        db.deleteContact(id);
        notifyItemRemoved(position);

    }

}
