package com.example.ketabeman21.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.ketabeman21.Model.Book;
import com.example.ketabeman21.R;

import java.io.File;
import java.util.ArrayList;


/**
 * Created by csa on 3/1/2017.
 */

public class DataAdapter4OfflineBook extends RecyclerView.Adapter<DataAdapter4OfflineBook.Myholder>  implements Filterable {
    ArrayList<Book> dataModelArrayList;
    private BookAdapterListener listener;
    private ArrayList<Book> mFilteredList;
    private Context context;

    public DataAdapter4OfflineBook(ArrayList<Book> dataModelArrayList, BookAdapterListener listener, Context context) {
        this.dataModelArrayList = dataModelArrayList;
        this.listener = listener;
        this.context = context;
        mFilteredList = dataModelArrayList;


    }

    class Myholder extends RecyclerView.ViewHolder{
        TextView name,author;
        ImageView cover;
        Button open_now;
        public Myholder(View itemView) {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.tv_name);
            author = (TextView) itemView.findViewById(R.id.tv_version);
            cover = (ImageView) itemView.findViewById(R.id.image);
            open_now = itemView.findViewById(R.id.detail_button);
            open_now.setText("خواندن");
            open_now.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // send selected contact in callback
                    listener.onBookSelected(mFilteredList.get(getAdapterPosition()));
                }
            });
        }
    }


    @Override
    public Myholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_row, parent, false);
        return new Myholder(view);

    }

    @Override
    public void onBindViewHolder(Myholder holder, int position) {
        Book dataModel=dataModelArrayList.get(position);
        holder.name.setText(dataModel.getFullName());
        holder.author.setText(dataModel.getAuthor());
        String PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "Ketabeman/Books/covers/"+dataModel.getBookId()+".png";

        File imgFile = new  File(PATH);
     //   if(imgFile.exists()){
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            holder.cover.setImageBitmap(myBitmap);
        //}


    }

    @Override
    public int getItemCount() {
        return mFilteredList.size();
    }

    public interface BookAdapterListener {
        void onBookSelected(Book book);
    }
    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String charString = charSequence.toString();

                if (charString.isEmpty()) {

                    mFilteredList = dataModelArrayList;
                } else {

                    ArrayList<Book> filteredList = new ArrayList<>();

                    for (Book androidVersion : dataModelArrayList) {

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
}