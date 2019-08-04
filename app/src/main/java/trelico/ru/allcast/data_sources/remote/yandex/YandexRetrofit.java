package trelico.ru.allcast.data_sources.remote.yandex;

import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

interface YandexRetrofit{

    public String IAM_TOKEN = "CggVAgAAABoBMRKABLQ6Oun1xWXj2KFjl3DEAd7hyzxSslLXdxU2VpFrmfvkOSYP_apy0U46tfJWBqQyAlKWEzsR7vtHhruXq31EsoXDg_LodNZut69KBt23U5-_aw2CIfCQZKizVn3ioYFTPYBbDAVEt5edI1BDmfZviGs_P_A_QyXovEBwFCNl0sZ1ct7yPaN_hdn27NUFPOVwDVx8Ku7V4FWqmE82xfYPS8tXPBZXJs-ehZQ43pK9Ggyb-ellqc8qfDxa3B5UJXIndIs08hTEO5XC0xcP2NcRbhRiAi5-zVz4jP9yRElL7zBj7IFW_cs3WhrPzhDSRPaEbOttCxjeJ2Keh--iZevisNyCyz9SuqFK_dHId6JOskEzbcJQ-u4-lqYwkPXkdriodVpVQBbjaTgoLzVsnG-gR613oN26mHMu4sVp9-QH5E1C3YyPBj8wqmQpDrLr0jRfX3C2H3Mwg0dAsDM7VmICCldPM89nefP3eWLuxtkQd9Hk40Q7ET6ik2i9Y8VoqwPIBlnUnbWb_cC2LOWYbWWrddPbrppdtqFlYlRfxK3dci4F-oBrta9vKOTSZGsqf7ZVa9B2DlMZBFOe1YI3q_Rha-Ir2opiXiqXVmPJpM40cpfTyhDPsbwlvNuvhuPUlB2rj4XdAy3XUsYqyL-KiDx7swVi_NWBK_oYWmVQE7BFKKc_Gm8KIDE0NTgwNWI4YTI5ODQ4ZGY4ODI2MjRiMzY5Yjg2ZWQ5EJD1iuoFGNDGjeoFIjcKFGFqZWQyZHEwcm5kMTRmdGNnMTBvEgdzZXJ2aWNlKhRiMWdhcDY4MmlnY3R2MGY2ZjNhZzACMAU4AVABWgA";

    /**
     * curl -X POST \
     *      -H "Authorization: Bearer ${IAM_TOKEN}" \
     *      --data-urlencode "text=Hello World" \
     *      -d "lang=en-US&folderId=${FOLDER_ID}" \
     *      "https://tts.api.cloud.yandex.net/speech/v1/tts:synthesize" > speech.ogg
     */

    /**
     * POST /speech/v1/tts:synthesize HTTP/1.1
     * Host: tts.api.cloud.yandex.net
     * Authorization: Bearer CggVAgAAABoBMRKABFAsG7ZsE5Ww7C6S7EMHcS2DMDQNI9nkya_6k3g3ZAKrTXxJIsnPTO4jgtMhMENEYUq-l4xUpTlUKWNVovnM4b4Upt47eZSAl2-jJC14XAPDYNmxKCJmWfFDhjEwmPaT50z6YQNHHllquSWkQNo1loZzg-YPRKia90hQnhmcfcoKpi-8YYqVm3WQub1UiUZuzA6l01_diMPaJ_W22h4dpQeaxa8SmGlrirLz7hCNBtwnWisE_rtZ2T4sTzpDIgGHbjRrv2FTFFfZncnLmGZxqgrTcqXVGhAvi4VE3iGGxNF24rvWhPLVX2gtJ6JTnLxSYM58Fl8R-OHcqVbI7nMU2pijFa4IMDj-lsO98N2VvNO3dZP9Qt_dI3yxcE4eKcKIr46cyYYGnHPQOrDt15On5xGh2302t1t_kbl62v4GGoG_XWcoCheRhJaMItdGuyQacAtH2WRqPIn0PJAqvOgg4i8E_ZdwF5TQhuGJZl7ja1grhAPd0V47Q29VABaS0A2VJzJBQWzLNNRZUNJAU4Rnw9nVWH3RJDQ6vCkBxcN9zEGrrlBfhndgi_IGq_oL0DicTEhToinvRKdkGOzMq2x8ex_RC8fRJ1Q32cTFdH3b902Upn1UOlEefIsJxDUocWcii6Fqj9JHp3C59flQGNPjXI7JLkjq4Qhg27iRjUgedAp9Gm8KIGY4NmI0NGViOTI3MDRlNmRhNjhiZTA0MjA3ZGRkZTFlEP_xiuoFGL_DjeoFIjcKFGFqZWQyZHEwcm5kMTRmdGNnMTBvEgdzZXJ2aWNlKhRiMWdhcDY4MmlnY3R2MGY2ZjNhZzACMAU4AVABWgA
     * Content-Type: application/x-www-form-urlencoded
     * User-Agent: PostmanRuntime/7.13.0
     * Accept:
     *Cache-Control:no-cache
     *Postman-Token:705d9459-3919-4d17-8192-57d0c064a181,594dd1db-0ade-4b6f-973d-122fd51976aa
     *Host:tts.api.cloud.yandex.net
     *accept-encoding:gzip,deflate
     *content-length:21
     *Connection:keep-alive
     *cache-control:no-cache
     *text=Hello%2C+world
     */

    @FormUrlEncoded
    @Headers({"Content-Type: application/x-www-form-urlencoded","Authorization: Bearer CggVAgAAABoBMRKABFAsG7ZsE5Ww7C6S7EMHcS2DMDQNI9nkya_6k3g3ZAKrTXxJIsnPTO4jgtMhMENEYUq-l4xUpTlUKWNVovnM4b4Upt47eZSAl2-jJC14XAPDYNmxKCJmWfFDhjEwmPaT50z6YQNHHllquSWkQNo1loZzg-YPRKia90hQnhmcfcoKpi-8YYqVm3WQub1UiUZuzA6l01_diMPaJ_W22h4dpQeaxa8SmGlrirLz7hCNBtwnWisE_rtZ2T4sTzpDIgGHbjRrv2FTFFfZncnLmGZxqgrTcqXVGhAvi4VE3iGGxNF24rvWhPLVX2gtJ6JTnLxSYM58Fl8R-OHcqVbI7nMU2pijFa4IMDj-lsO98N2VvNO3dZP9Qt_dI3yxcE4eKcKIr46cyYYGnHPQOrDt15On5xGh2302t1t_kbl62v4GGoG_XWcoCheRhJaMItdGuyQacAtH2WRqPIn0PJAqvOgg4i8E_ZdwF5TQhuGJZl7ja1grhAPd0V47Q29VABaS0A2VJzJBQWzLNNRZUNJAU4Rnw9nVWH3RJDQ6vCkBxcN9zEGrrlBfhndgi_IGq_oL0DicTEhToinvRKdkGOzMq2x8ex_RC8fRJ1Q32cTFdH3b902Upn1UOlEefIsJxDUocWcii6Fqj9JHp3C59flQGNPjXI7JLkjq4Qhg27iRjUgedAp9Gm8KIGY4NmI0NGViOTI3MDRlNmRhNjhiZTA0MjA3ZGRkZTFlEP_xiuoFGL_DjeoFIjcKFGFqZWQyZHEwcm5kMTRmdGNnMTBvEgdzZXJ2aWNlKhRiMWdhcDY4MmlnY3R2MGY2ZjNhZzACMAU4AVABWgA"})
    @POST("speech/v1/tts:synthesize")
    Single<String> getSpeechOgg(@Body TTSRequestBody body, @Query("emotion") String emotion);


}
