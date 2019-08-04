package trelico.ru.allcast.data_sources.remote.yandex;

public class TTSRequestBody{

    private String text;

    public TTSRequestBody(String text){
        this.text = text;
    }

    public TTSRequestBody(){
    }

    public String getText(){
        return text;
    }

    public void setText(String text){
        this.text = text;
    }
}
