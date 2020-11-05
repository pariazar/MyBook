package com.example.ketabeman21.Adapter;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

import com.example.ketabeman21.Model.Book;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class DatabaseHelperOfflineBook extends SQLiteOpenHelper {

        public static String DATABASE = "database.db";
        public static String TABLE ="mybooks";
        public static String BookID = "BookID";
        public static String Name = "bookName";
        public static String Author = "writer";
        public static String Edition = "Edition";
        public static String PageNumber = "PageNumber";
        public static String ISBN10 = "ISBN10";
        public static String ISBN13 = "ISBN13";
        public static String Description = "Description";
        public static String BookURL = "BookURL";
        public static String BookCoverPicture = "BookCoverPicture";
        public static String BookPrice = "BookPrice";
        public static String PublishYear = "PublishYear";
        public static String PUBLISHER = "Publisher";
        public static String Langusage = "Langusage";

        String br;
        File file;
        public DatabaseHelperOfflineBook(Context context) {
            super(context, DATABASE, null, 1);

        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            //  br= "CREATE TABLE mytable(name TEXT,company TEXT,city TEXT,country TEXT);";
            br = "CREATE TABLE "+TABLE+"("+BookID+ " Text, "+Name+ " Text, "+Author+ " Text, "+Edition+ " Text, "+
                    PageNumber+ " Text, "+ISBN10+ " Text, "+ISBN13+ " Text, "+PUBLISHER+ " Text, "+Description+ " Text, "
                    +BookURL+ " Text, "+BookPrice+ " Text, "+Langusage+ " Text, "+PublishYear+ " Text, "
                    +BookCoverPicture+ " Text);";
            db.execSQL(br);

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS "+TABLE+" ;");
        }


        public void insertdata(
                String bookID,
                String bookName  ,
                String author ,
                String edition ,
                String pageNumber ,
                String isbn10 ,
                String isbn13 ,
                String publisher ,
                String langusage ,
                String description ,
                String bookURL ,
                String bookCoverPicture ,
                String bookPrice ,
                String publishYear){
            System.out.print("Hello "+br);
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues= new ContentValues();


            contentValues.put(BookID, bookID);
            contentValues.put(Name, bookName);
            contentValues.put(Author, author);
            contentValues.put(Edition, edition);
            contentValues.put(PageNumber, pageNumber);
            contentValues.put(ISBN10, isbn10);
            contentValues.put(ISBN13, isbn13);
            contentValues.put(PUBLISHER, publisher);
            contentValues.put(Description, description);
            contentValues.put(BookURL, bookURL);
            contentValues.put(BookPrice, bookPrice);
            contentValues.put(Langusage, langusage);
            contentValues.put(PublishYear, publishYear);
            contentValues.put(BookCoverPicture, bookCoverPicture);

            db.insert(TABLE,null,contentValues);


        }
        public boolean check_book_ex(int bookID){
            SQLiteDatabase db = getReadableDatabase();
            int numRows = (int)DatabaseUtils.queryNumEntries(db, TABLE,
                    BookID+"=?", new String[] {String.valueOf(bookID)});
            if(numRows==0){
                return false;
            }
            else {
                return true;
            }
        }
    public String getPasswordOfBook(int bid) {
        /*SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE, new String[] { BookPass
                         }, BookID + "=?",
                new String[] { String.valueOf(bid) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        return cursor.getString(0);
*/

        Cursor cursor = null;
        String passOfBook = "";
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            cursor = db.rawQuery("SELECT BookPass FROM mybooks WHERE BookID=?", new String[] {bid + ""});
            if(cursor.getCount() > 0) {
                cursor.moveToFirst();
                passOfBook = cursor.getString(cursor.getColumnIndex("BookPass"));
            }
            return passOfBook;
        }finally {
            cursor.close();
        }
    }
    public boolean check_empty_lib(){
        SQLiteDatabase db = getReadableDatabase();
        int numRows = (int)DatabaseUtils.queryNumEntries(db, TABLE);
        if(numRows==0){
            return true;
        }
        else {
            return false;
        }
    }
        public ArrayList<Book> getdata(){
            // DataModel dataModel = new DataModel();
            ArrayList<Book> data=new ArrayList<>();
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery("select * from "+TABLE+" ;",null);
            StringBuffer stringBuffer = new StringBuffer();
            Book dataModel = null;
            while (cursor.moveToNext()) {
                dataModel= new Book();
                String name = cursor.getString(cursor.getColumnIndexOrThrow("bookName"));
                String bookID = cursor.getString(cursor.getColumnIndexOrThrow("BookID"));
                String author = cursor.getString(cursor.getColumnIndexOrThrow("writer"));
                String edition = cursor.getString(cursor.getColumnIndexOrThrow("Edition"));
                String page_number = cursor.getString(cursor.getColumnIndexOrThrow("PageNumber"));
                String isbn10 = cursor.getString(cursor.getColumnIndexOrThrow("ISBN10"));
                String isbn13 = cursor.getString(cursor.getColumnIndexOrThrow("ISBN13"));
                String publisher = cursor.getString(cursor.getColumnIndexOrThrow("Publisher"));
                String language = cursor.getString(cursor.getColumnIndexOrThrow("Langusage"));
                String description = cursor.getString(cursor.getColumnIndexOrThrow("Description"));
                String bookurl = cursor.getString(cursor.getColumnIndexOrThrow("BookURL"));
                String bookCover = cursor.getString(cursor.getColumnIndexOrThrow("BookCoverPicture"));
                String bookPrice = cursor.getString(cursor.getColumnIndexOrThrow("BookPrice"));
                String publish_year = cursor.getString(cursor.getColumnIndexOrThrow("PublishYear"));

                dataModel.setBookId(bookID);
                dataModel.setFullName(name);
                dataModel.setCover(bookCover);
                dataModel.setPrice(bookPrice);
                dataModel.setEdition(edition);
                dataModel.setBookFile(bookurl);
                dataModel.setAuthor(author);
                dataModel.setYear(publish_year);
                dataModel.setPage(Integer.parseInt(page_number));
                dataModel.setDescription(description);
                dataModel.setISBN10(isbn10);
                dataModel.setISBN13(isbn13);
                dataModel.setPublisher(publisher);
                dataModel.setPublisher(language);
                stringBuffer.append(dataModel);
                // stringBuffer.append(dataModel);
                file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "Ketabeman/Books/" + dataModel.getFullName()+"_withWaterMark"+".pdf");
                if(file.exists()){
                    data.add(dataModel);
                }
                else {
                    deleteBook(String.valueOf(dataModel.getBookId()));
                }
            }

            for (Book mo:data ) {

                Log.i("Hellomo",""+mo.getBookId());
            }

            //

            return data;
        }

        //get password of book for offline object
        public String getDataByID(int id){
            String column3 = null;

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor c = db.rawQuery("select * from "+TABLE+" WHERE "+BookID+" = ?", new String[] { String.valueOf(id)});
            if (c.moveToFirst()){
                do {
                    // Passing values
                    String column1 = c.getString(0);
                    String column2 = c.getString(1);
                    column3 = c.getString(14);
                    // Do something Here with values
                } while(c.moveToNext());
            }
            c.close();
            db.close();
            return column3;
        }
        public boolean deleteBook(String id)
        {
            SQLiteDatabase db = this.getWritableDatabase();
            return db.delete(TABLE, BookID + "=" + id, null) > 0;
        }

}
