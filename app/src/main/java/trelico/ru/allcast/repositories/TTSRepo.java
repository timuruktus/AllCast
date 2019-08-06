package trelico.ru.allcast.repositories;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import trelico.ru.allcast.MyApp;
import trelico.ru.allcast.data_sources.local.AppDatabase;
import trelico.ru.allcast.data_sources.local.FileStorage;
import trelico.ru.allcast.data_sources.local.yandex.TTSDatabaseEntity;
import trelico.ru.allcast.data_sources.local.yandex.TTSDatabaseEntityDAO;
import trelico.ru.allcast.data_sources.remote.yandex.TTSAPI;
import trelico.ru.allcast.data_sources.remote.yandex.TTSRequestBody;
import trelico.ru.allcast.utils.HashUtils;

import static trelico.ru.allcast.data_sources.remote.NetworkService.DEFAULT_EMOTION;

public class TTSRepo{

    private static TTSRepo instance;
    private TTSAPI ttsapi;
    private AppDatabase appDatabase;
    private TTSDatabaseEntityDAO databaseEntityDAO;

    private TTSRepo(){
        ttsapi = TTSAPI.getInstance();
        appDatabase = MyApp.INSTANCE.getAppDatabase();
        databaseEntityDAO = appDatabase.ttsDatabaseEntityDao();
    }

    public static TTSRepo getInstance(){
        if(instance == null) instance = new TTSRepo();
        return instance;
    }

    public Single<String> getSpeechUriFromText(String text, String link){
        String textHash = HashUtils.getHash(text);
        return databaseEntityDAO.getByHash(textHash)
                .flatMap(ttsDatabaseEntity -> {
                    if(ttsDatabaseEntity != null){
                        return Single.just(ttsDatabaseEntity.getAudioUri());
                    }
                    else{
                        return getTTSRawAudioFromWeb(text).flatMap(ttsRawAudio -> {
                            saveTTSResponseToFileAndDB(text, textHash, ttsRawAudio, link);
                            TTSDatabaseEntity ttsResponse = new TTSDatabaseEntity(text, textHash);
                            return Single.just(ttsResponse.getAudioUri());
                        });
                    }
                });
    }

    private void saveTTSResponseToFileAndDB(String text, String hash,
                                            String ttsRawAudioString, String link){
        FileStorage fileStorage = new FileStorage();
        fileStorage.saveStringToFile(hash, ttsRawAudioString);
        TTSDatabaseEntity ttsDatabaseEntity;
        if(link != null && !link.isEmpty()){
            ttsDatabaseEntity = new TTSDatabaseEntity(text, hash, link);
        }else{
            ttsDatabaseEntity = new TTSDatabaseEntity(text, hash);
        }
        MyApp.getAppDatabase().ttsDatabaseEntityDao().insert(ttsDatabaseEntity);
    }

    private Single<String> getTTSRawAudioFromWeb(String text, String emotion){
        return ttsapi.getTTSRawAudioString(new TTSRequestBody(text), emotion);
    }

    private Single<String> getTTSRawAudioFromWeb(String text){
        return getTTSRawAudioFromWeb(text, DEFAULT_EMOTION);
    }

    public void saveTTSEntityToDB(TTSDatabaseEntity entity){
        databaseEntityDAO.insert(entity);
    }

}
