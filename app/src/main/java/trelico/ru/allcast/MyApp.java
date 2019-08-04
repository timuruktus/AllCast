package trelico.ru.allcast;

import android.app.Application;

import androidx.room.Room;

import trelico.ru.allcast.data_sources.local.AppDatabase;
import trelico.ru.allcast.data_sources.remote.NetworkService;

public class MyApp extends Application{

    public static final String D_TAG = "Debug tag";
    public static final String I_TAG = "Info tag";
    public static final String E_TAG = "Error tag";
    public static MyApp INSTANCE;
    private static AppDatabase appDatabase;

    @Override
    public void onCreate(){
        super.onCreate();
        INSTANCE = this;
        appDatabase =  Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "database").build();
    }

    public static AppDatabase getAppDatabase(){
        return appDatabase;
    }
}
