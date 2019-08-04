package trelico.ru.allcast.data_sources.remote.yandex;

import io.reactivex.Single;
import trelico.ru.allcast.data_sources.remote.NetworkService;

public class TTSAPI{


    private static TTSAPI ttsapi;
    private static NetworkService networkService;
    private static YandexRetrofit yandexRetrofit; //replaceable


    private TTSAPI(){}

    public static TTSAPI getInstance(){
        if(ttsapi == null){
            ttsapi = new TTSAPI();
            networkService = NetworkService.getInstance();
            yandexRetrofit = networkService.getYandexService().create(YandexRetrofit.class);
        }
        return ttsapi;
    }

    public Single<String> getTTSRawAudioString(TTSRequestBody body, String emotion){
        return yandexRetrofit.getSpeechOgg(body, emotion);
    }
}
