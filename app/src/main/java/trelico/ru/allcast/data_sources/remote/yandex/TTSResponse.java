package trelico.ru.allcast.data_sources.remote.yandex;

public class TTSResponse{

    private String oogFile;
    private String hash;

    public TTSResponse(String oogFile){
        this.oogFile = oogFile;
    }

    public TTSResponse(){
    }

    public String getOogFile(){
        return oogFile;
    }

    public void setOogFile(String oogFile){
        this.oogFile = oogFile;
    }
}
