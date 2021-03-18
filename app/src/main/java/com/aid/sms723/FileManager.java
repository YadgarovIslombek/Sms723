package com.aid.sms723;


import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.aid.sms723.model.Number;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class FileManager {

    private static final String TAG = "FileManager";
    private ArrayList<Number> files;
    Number numberM;
    Context context;

    public ArrayList<Number> createFiles(ArrayList<String> numbersList, int fileSize) {
        files = new ArrayList<>();
        Number file = new Number(fileSize);
        //Limit qo'yish ======--------------------------------
//        if (numbersList.size() <= file.getLimitSize()) {

            for (int i = 0; i < numbersList.size(); i++) {
                file.add(numbersList.get(i));
                if (file.getSize() == fileSize) {

                    files.add(file);

                    file = new Number(fileSize);
                }
            }
            if (numbersList.size() > 0 && numbersList.size() % fileSize > 0) {
                files.add(file);
            }

            Log.d(TAG, "createBatches: " + files.toString());

//        } else {
//            Log.d("TAG", "VAXAXA");
//            Log.d("SizLimt0",String.valueOf(file.getLimitSize()));
//        }
        return files;
    }
    //==================Qatorlarni oqib har bir qatorni qaytaramiz ===================
    public ArrayList<String> ReadNumbers(Context context, Uri uri) throws IOException {
        ArrayList<String> lines = new ArrayList<>();
        InputStream inputStream = context.getContentResolver().openInputStream(uri);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.length() == 13) {
                lines.add(line);
            }
            else{


            }
        }
        if (inputStream != null) {
            inputStream.close();
        }
        reader.close();

        Log.d(TAG, "Linelar: " + lines.toString());
        return lines;
    }





    public ArrayList<String> getFileNames() {
        ArrayList<String> names = new ArrayList<>();
        for (int i = 0; i < files.size(); i++) {
            String builder = "File - " + (i + 1) + "   numbers - " + files.get(i).getSize();
            names.add(builder);
        }
        return names;
    }
}