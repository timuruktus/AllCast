package trelico.ru.allcast.data_sources.remote;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkService{

    private static NetworkService instance;
    private static final String YANDEX_TTS_URL = "https://tts.api.cloud.yandex.net/";
    private Retrofit yandexService;
    public static final String DEFAULT_EMOTION = "good";
    public static final String GOOD_EMOTION = "good";
    public static final String EVIT_EMOTION = "evil";
    public static final String NEUTRAL_EMOTION = "neutral";

    private NetworkService() {
        yandexService = new Retrofit.Builder()
                .baseUrl(YANDEX_TTS_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static NetworkService getInstance() {
        if (instance == null) {
            instance = new NetworkService();
        }
        return instance;
    }


    public Retrofit getYandexService(){
        return yandexService;
    }
}
