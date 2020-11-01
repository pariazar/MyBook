package com.example.ketabeman21.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;


import com.example.ketabeman21.Model.Book;
import com.example.ketabeman21.R;
import com.example.ketabeman21.Utils.Constants;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class DataAdapter4Book extends RecyclerView.Adapter<DataAdapter4Book.ViewHolder> implements Filterable {
    private ArrayList<Book> mArrayList;
    private ArrayList<Book> mFilteredList;
    private Context context;
    private BookAdapterListener listener;
    private View nView;

    public DataAdapter4Book(ArrayList<Book> arrayList, Context context, BookAdapterListener listener) {
        mArrayList = arrayList;
        mFilteredList = arrayList;
        this.context = context;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view;
        view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_row, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {

        viewHolder.tv_name.setText(mFilteredList.get(i).getName());
        viewHolder.tv_version.setText(mFilteredList.get(i).getAuthor());
        viewHolder.tv_username.setText(mFilteredList.get(i).getISBN10());
        viewHolder.tv_price.setText(mFilteredList.get(i).getPrice());


        Picasso.with(context)
                .load(R.drawable.empty)
                .into(viewHolder.iv_pic, new Callback() {
                    @Override
                    public void onSuccess() {
                        Picasso.with(context)
                                .load(Constants.Image_Directory+mFilteredList.get(i).getCover()) // image url goes here
                                .placeholder(viewHolder.iv_pic.getDrawable())
                                .into(viewHolder.iv_pic);
                    }
                    @Override
                    public void onError() {

                    }
                });
       /* Picasso.with(context)
                .load(mFilteredList.get(i).getProfilePhoto())
                .into(viewHolder.userImage);*/
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

                    ArrayList<Book> filteredList = new ArrayList<>();

                    for (Book androidVersion : mArrayList) {

                        if (androidVersion.getName().toLowerCase().contains(charString) || androidVersion.getName().toLowerCase().contains(charString) || androidVersion.getAuthor().toLowerCase().contains(charString)) {

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
                mFilteredList = (ArrayList<Book>) filterResults.values;
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

                    ArrayList<Book> filteredList = new ArrayList<>();

                    for (Book androidVersion : mArrayList) {

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
                mFilteredList = (ArrayList<Book>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView tv_name,tv_version,tv_username,tv_price;
        private ImageView iv_pic;
       // private CircleImageView userImage;
        public ViewHolder(View view) {
            super(view);

            tv_name = (TextView)view.findViewById(R.id.tv_name);
            tv_username = (TextView)view.findViewById(R.id.username);
            tv_version = (TextView)view.findViewById(R.id.tv_version);
            iv_pic = (ImageView) view.findViewById(R.id.image);
            //userImage = view.findViewById(R.id.iconUser);
            tv_price = view.findViewById(R.id.price);
            nView = view;
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // send selected contact in callback
                    listener.onContactSelected(mFilteredList.get(getAdapterPosition()));
                }
            });
        }
    }
    public interface BookAdapterListener {
        void onContactSelected(Book book);
    }
}