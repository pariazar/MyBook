package com.example.ketabeman21.Helper;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.example.ketabeman21.Model.Book;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfGState;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

public class ManipulateData {
    private String tmp_path,input_path,tmp_out;
    private Context context;
    public ManipulateData(Context context){
        this.context = context;
    }

    public static void makeReadyPDF(Activity activity
                                    , Context context,String nameOfBook, String input_path,String companyTitle){
        Dexter.withActivity(activity)
                .withPermissions(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            Toast.makeText(context, "All permissions are granted!", Toast.LENGTH_SHORT).show();
                            //startDownload(/*b.getBookURL()*/"http://bayanbox.ir/download/6663019729830843802/20110213120514-629-protected.pdf");
                            File file = new File(input_path);
                            if(file.exists()){
                                Toast.makeText(context, "PDF file ready to encrypt!", Toast.LENGTH_SHORT).show();

                                try {
                                    PdfReader reader = new PdfReader(input_path);
                                    PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "Ketabeman/Books/" + nameOfBook+"_withoutPass"+".pdf"));
                                    stamper.close();
                                    reader.close();
                                    deleteFile(file);
                                    watermarkPdf2(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "Ketabeman/Books/" + nameOfBook+"_withoutPass"+".pdf",
                                            Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "Ketabeman/Books/" +nameOfBook+"_withWaterMark"+".pdf",companyTitle);
                                    file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "Ketabeman/Books/" + nameOfBook+"_withoutPass"+".pdf");
                                    deleteFile(file);
                                   /* encryptPDF(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "Ketabeman/Books/" +nameOfBook+"_withWaterMark_withoutPass"+".pdf",
                                            Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "Ketabeman/Books/"+nameOfBook+".pdf",userph,userph+"NoneStop");
                                    file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "Ketabeman/Books/" +nameOfBook+"_withWaterMark_withoutPass"+".pdf");
                                    deleteFile(file);*/
                                } catch (DocumentException e) {
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                            }
                            else{
                                Toast.makeText(context, "There is no PDF", Toast.LENGTH_SHORT).show();
                            }
                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // show alert dialog navigating to Settings
                            //showSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).
                withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                        Toast.makeText(context, "Error occurred! ", Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread()
                .check();
    }

    private static void deleteFile(File file) {
        if (file.exists()) {
            file.delete();
        }
    }


    public boolean setMetaData(Book book, String userID){
        File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "Ketabeman/TempFiles");
        try{
            if(dir.mkdir()) {
                tmp_path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "Ketabeman/TempFiles/" + book.getFullName()+".pdf";
                addLableToPDF(book,userID);
                return true;
            } else {
                if(dir.exists()) {
                    tmp_path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "Ketabeman/TempFiles/" + book.getFullName()+".pdf";
                    addLableToPDF(book,userID);
                    return true;
                }
                else {
                    return false;
                }
            }
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }

    }

    private void addLableToPDF(Book book,String userID) {
        try {
            Document document = new Document();
            //output of modify PDF with Lable
            PdfCopy copy = new PdfCopy(document, new FileOutputStream(tmp_path));
            document.open();
            PdfReader ReadInputPDF;
            int number_of_pages;
            //address of input PDF that should change quickly
            input_path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "Ketabeman/Books/" + book.getName()+".pdf";
            ReadInputPDF = new PdfReader(input_path);
            number_of_pages = ReadInputPDF.getNumberOfPages();
            for (int page = 0; page < number_of_pages; ) {
                copy.addPage(copy.getImportedPage(ReadInputPDF, ++page));
            }
            document.addTitle(book.getFullName());
            document.addAuthor(book.getAuthor());
            document.addCreator(userID);
            document.addSubject(String.valueOf(book.getISBN10()));
            document.addKeywords(String.valueOf(book.getBookId()));
            document.close();
            pdf_deleteTemp_1();
            pdf_deleteTemp_2();
        } catch (Exception i) {
            Toast.makeText(context, "یک مشکلی در حین آماده سازی فایل PDF پیش آمده است.", Toast.LENGTH_SHORT).show();
        }
    }
    public void pdf_deleteTemp_1(){
        InputStream in;
        OutputStream out;

        try {

            in = new FileInputStream(tmp_path);
            out = new FileOutputStream(input_path);

            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            in.close();

            // write the output file
            out.flush();
            out.close();
        } catch (Exception e) {
            Log.e("tag", e.getMessage());
        }

        File pdfFile = new File(tmp_path);
        if(pdfFile.exists()){
            pdfFile.delete();
        }

    }
    public void pdf_deleteTemp_2 () {

        InputStream in;
        OutputStream out;

        try {

            in = new FileInputStream(tmp_path);
            out = new FileOutputStream(input_path);

            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            in.close();

            // write the output file
            out.flush();
            out.close();
        } catch (Exception e) {
            Log.e("tag", e.getMessage());
        }

        File pdfFile = new File(Environment.getExternalStorageDirectory() +  "/" + "1234567.pdf");
        if(pdfFile.exists()){
            pdfFile.delete();
        }
    }
    //-------------------------------------------

    public boolean manipulatePdf(Book book,String pass) throws IOException,DocumentException  {
        tmp_out = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "Ketabeman/TempFiles/" + book.getFullName()+".pdf";
        tmp_path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "Ketabeman/Books/" + book.getFullName()+".pdf";
        if(ex_tmp()){
            PdfReader reader = new PdfReader(tmp_path, pass.getBytes());
            PdfStamper stamper = null;
            try {
                stamper = new PdfStamper(reader, new FileOutputStream(tmp_out));
                stamper.close();
                reader.close();
                return true;
            } catch (DocumentException e) {
                e.printStackTrace();
                return false;
            }
        }
        else {
            return false;
        }
    }
    public boolean ex_tmp(){
        File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "Ketabeman/TempFiles");
        try{
            if(dir.mkdir()) {
                return true;
            } else {
                if(dir.exists()) {
                    return true;
                }
                else {
                    return false;
                }
            }
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }


    public static void encryptPDF(String notEncrypted,String encrypted,String userPass,String ownerPass) throws DocumentException, IOException {
        Document document = new Document();
        PdfReader reader = new PdfReader(notEncrypted);
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(encrypted));
        //first input : user password , second input : is owner password
        stamper.setEncryption(userPass.getBytes(), ownerPass.getBytes(),PdfWriter.ALLOW_COPY, PdfWriter.ENCRYPTION_AES_256);
        stamper.close();
        reader.close();
    }
    public static void watermarkPdf2(String src, String dest,String sID) throws IOException, DocumentException {
        PdfReader reader = new PdfReader(src);
        int n = reader.getNumberOfPages();
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(dest));
        // text watermark
        Font f = new Font(Font.FontFamily.HELVETICA, 70);
        Phrase p = new Phrase(sID, f);
        // image watermark
        /*Image img = Image.getInstance(IMG);
        float w = img.getScaledWidth();
        float h = img.getScaledHeight();*/
        // transparency
        PdfGState gs1 = new PdfGState();
        gs1.setFillOpacity(0.1f);
        // properties
        PdfContentByte over;
        Rectangle pagesize;
        float x, y;
        // loop over every page
        for (int i = 1; i <= n; i++) {
            pagesize = reader.getPageSizeWithRotation(i);
            x = (pagesize.getLeft() + pagesize.getRight()) / 2;
            y = (pagesize.getTop() + pagesize.getBottom()) / 2;
            over = stamper.getOverContent(i);
            over.saveState();
            over.setGState(gs1);
            if (i % 2 == 1)
                ColumnText.showTextAligned(over, Element.ALIGN_CENTER, p, x, y, 0);
            else
                //over.addImage(img, w, 0, 0, h, x - (w / 2), y - (h / 2));
                over.restoreState();
        }
        stamper.close();
        reader.close();
    }

}
