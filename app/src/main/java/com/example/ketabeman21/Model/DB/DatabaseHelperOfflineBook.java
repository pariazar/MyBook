package com.example.ketabeman21.Model.DB;


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
        public static String ISBN = "ISBN";
        public static String AuthorType = "AuthorType";
        public static String Description = "Description";
        public static String BookURL = "BookURL";
        public static String BookPass = "BookPass";
        public static String Jeld = "Jeld";
        public static String BookCoverPicture = "BookCoverPicture";
        public static String BookPrice = "BookPrice";
        public static String PublishYear = "PublishYear";
        public static String WaitingList = "WaitingList";
        public static String ActionType = "ActionType";
        public static String ActionTime = "ActionTime";
        String br;
        File file;
        public DatabaseHelperOfflineBook(Context context) {
            super(context, DATABASE, null, 1);

        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            //  br= "CREATE TABLE mytable(name TEXT,company TEXT,city TEXT,country TEXT);";
            br = "CREATE TABLE "+TABLE+"("+BookID+ " Text, "+Name+ " Text, "+Author+ " Text, "+Edition+ " Text, "+
                    PageNumber+ " Text, "+ISBN+ " Text, "+AuthorType+ " Text, "+Description+ " Text, "
                    +BookURL+ " Text, "+Jeld+ " Text, "+BookPrice+ " Text, "+PublishYear+ " Text, "+WaitingList+ " Text, "
                    +ActionType+ " Text, "+BookPass+ " Text, "+ActionTime+ " Text, "+BookCoverPicture+ " Text);";
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
                String isbn ,
                String authorType ,
                String description ,
                String bookURL ,
                String jeld ,
                String bookCoverPicture ,
                String bookPrice ,
                String publishYear ,
                String waitingList ,
                String bookPass ,
                String actionType ,
                String actionTime ){
            System.out.print("Hello "+br);
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues= new ContentValues();


            contentValues.put(BookID, bookID);
            contentValues.put(Name, bookName);
            contentValues.put(Author, author);
            contentValues.put(Edition, edition);
            contentValues.put(PageNumber, pageNumber);
            contentValues.put(ISBN, isbn);
            contentValues.put(AuthorType, authorType);
            contentValues.put(Description, description);
            contentValues.put(BookURL, bookURL);
            contentValues.put(Jeld, jeld);
            contentValues.put(BookCoverPicture, bookCoverPicture);
            contentValues.put(BookPrice, bookPrice);
            contentValues.put(PublishYear, publishYear);
            contentValues.put(WaitingList, waitingList);
            contentValues.put(ActionType, actionType);
            contentValues.put(BookPass, bookPass);
            contentValues.put(ActionTime, actionTime);

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
    /*
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
                String isbn = cursor.getString(cursor.getColumnIndexOrThrow("ISBN"));
                String author_type = cursor.getString(cursor.getColumnIndexOrThrow("AuthorType"));
                String description = cursor.getString(cursor.getColumnIndexOrThrow("Description"));
                String bookurl = cursor.getString(cursor.getColumnIndexOrThrow("BookURL"));
                String jeld = cursor.getString(cursor.getColumnIndexOrThrow("Jeld"));
                String bookCover = cursor.getString(cursor.getColumnIndexOrThrow("BookCoverPicture"));
                String bookPrice = cursor.getString(cursor.getColumnIndexOrThrow("BookPrice"));
                String publish_year = cursor.getString(cursor.getColumnIndexOrThrow("PublishYear"));
                String waiting_list = cursor.getString(cursor.getColumnIndexOrThrow("WaitingList"));
                String action_type = cursor.getString(cursor.getColumnIndexOrThrow("ActionType"));
                String bookPass = cursor.getString(cursor.getColumnIndexOrThrow("BookPass"));
                String action_time = cursor.getString(cursor.getColumnIndexOrThrow("ActionTime"));
                dataModel.setBookID(Integer.parseInt(bookID));
                dataModel.setName(name);
                dataModel.setActionTime(action_time);
                dataModel.setAuthorType(Integer.parseInt(author_type));
                dataModel.setBookCoverPicture(bookCover);
                dataModel.setBookPrice(Long.parseLong(bookPrice));
                dataModel.setEdition(Integer.parseInt(edition));
                dataModel.setBookURL(bookurl);
                dataModel.setAuthor(author);
                dataModel.setBookPass(bookPass);
                dataModel.setJeld(Integer.parseInt(jeld));
                dataModel.setPublishYear(Integer.parseInt(publish_year));
                dataModel.setWaitingList(Integer.parseInt(waiting_list));
                dataModel.setActionType(Integer.parseInt(action_type));
                dataModel.setPageNumber(Integer.parseInt(page_number));
                dataModel.setDescription(description);
                dataModel.setISBN(Integer.parseInt(isbn));
                stringBuffer.append(dataModel);
                // stringBuffer.append(dataModel);
                file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "Azad_University/Books/" + dataModel.getName()+".pdf");
                if(file.exists()){
                    data.add(dataModel);
                }
                else {
                    deleteBook(String.valueOf(dataModel.getBookID()));
                }
            }

            for (Book mo:data ) {

                Log.i("Hellomo",""+mo.getBookID());
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
        }*/

}
