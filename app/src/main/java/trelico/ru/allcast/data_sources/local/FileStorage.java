package trelico.ru.allcast.data_sources.local;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import trelico.ru.allcast.MyApp;

public class FileStorage{

    private Context context = MyApp.INSTANCE;

//    @SuppressLint("CheckResult")
//    public void saveStringToFile(String fileName, String stringToSave){
//        Observable.empty()
//                .onErrorReturn(error -> Log.e(E_TAG, error.getMessage()))
//                .subscribe(o -> {
//                    Context context = MyApp.INSTANCE;
//                    FileOutputStream outputStream;
//                    outputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE);
//                    outputStream.write(stringToSave.getBytes());
//                    outputStream.close();
//        });
//
//    }

    public void saveStringToFile(String fileName, String stringToSave){
        FileOutputStream outputStream;
        try{
            outputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            outputStream.write(stringToSave.getBytes());
            outputStream.close();
        } catch(FileNotFoundException ex){
            ex.printStackTrace();
        } catch(IOException ex){
            ex.printStackTrace();
        }
    }

//    public String getAudioFromHash(String uri){
//        FileInputStream inputStream;
//        try{
//            inputStream = context.openFileInput(uri);
//            inputStream.
//        } catch(FileNotFoundException ex){
//            ex.printStackTrace();
//        } catch(IOException ex){
//            ex.printStackTrace();
//        }
//    }
}
