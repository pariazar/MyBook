package com.example.ketabeman21.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.example.ketabeman21.Model.Category;
import com.example.ketabeman21.Model.Category;
import com.example.ketabeman21.R;
import com.example.ketabeman21.Utils.Constants;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class DataAdapter4Cat extends RecyclerView.Adapter<DataAdapter4Cat.ViewHolder> implements Filterable {
    private ArrayList<Category> mArrayList;
    private ArrayList<Category> mFilteredList;
    private Context context;
    private CategoryAdapterListener listener;
    String letter;
    ColorGenerator generator = ColorGenerator.MATERIAL;
    private View nView;

    public DataAdapter4Cat(ArrayList<Category> arrayList, Context context, CategoryAdapterListener listener) {
        mArrayList = arrayList;
        mFilteredList = arrayList;
        this.context = context;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view;
        view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_row_category, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {

        viewHolder.tv_title.setText("\t"+ mFilteredList.get(i).getName());
        letter = String.valueOf(mFilteredList.get(i).getName().charAt(0));

        TextDrawable drawable = TextDrawable.builder()
                .buildRound(letter, generator.getRandomColor());

        viewHolder.iv_pic.setImageDrawable(drawable);



    }

    @Override
    public int getItemCount() {
        return mFilteredList.size();
    }

    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String charString = charSequence.toString();

                if (charString.isEmpty()) {

                    mFilteredList = mArrayList;
                } else {

                    ArrayList<Category> filteredList = new ArrayList<>();

                    for (Category androidVersion : mArrayList) {

                        if (androidVersion.getName().toLowerCase().contains(charString) || androidVersion.getName().toLowerCase().contains(charString) || androidVersion.getName().toLowerCase().contains(charString)) {

                            filteredList.add(androidVersion);
                        }
                    }

                    mFilteredList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mFilteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mFilteredList = (ArrayList<Category>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public Filter getFilteronCat() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String charString = charSequence.toString();

                if (charString.isEmpty()) {

                    mFilteredList = mArrayList;
                } else {

                    ArrayList<Category> filteredList = new ArrayList<>();

                    for (Category androidVersion : mArrayList) {

                        if (androidVersion.getCatid().toLowerCase().contains(charString)) {

                            filteredList.add(androidVersion);
                        }
                    }

                    mFilteredList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mFilteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mFilteredList = (ArrayList<Category>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView tv_title;
        private ImageView iv_pic;
        private CardView card_view;
       // private CircleImageView userImage;
        public ViewHolder(View view) {
            super(view);

            tv_title = (TextView)view.findViewById(R.id.item_title);
            iv_pic = (ImageView) view.findViewById(R.id.item_letter);
            card_view = view.findViewById(R.id.card_view);
            nView = view;
            card_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // send selected contact in callback
                    listener.onCategorySelected(mFilteredList.get(getAdapterPosition()));
                }
            });


        }
    }
    public interface CategoryAdapterListener {
        void onCategorySelected(Category Category);
    }

}