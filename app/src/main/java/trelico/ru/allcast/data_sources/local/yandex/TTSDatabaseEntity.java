package trelico.ru.allcast.data_sources.local.yandex;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import trelico.ru.allcast.utils.AndroidUtils;

@Entity
public class TTSDatabaseEntity{

    //TODO: ADD LINK

    private String link;
    private String text;
    @PrimaryKey
    private String hash;

    public TTSDatabaseEntity(String text, String hash){
        this.text = text;
        this.hash = hash;
    }

    public TTSDatabaseEntity(String link, String text, String hash){
        this.link = link;
        this.text = text;
        this.hash = hash;
    }

    public TTSDatabaseEntity(){
    }

    public String getText(){
        return text;
    }

    public void setText(String text){
        this.text = text;
    }

    public String getHash(){
        return hash;
    }

    public void setHash(String hash){
        this.hash = hash;
    }

    public String getLink(){
        return link;
    }

    public void setLink(String link){
        this.link = link;
    }

    public String getAudioUri(){
        return AndroidUtils.getAudioFilesDir() + "/" + hash;
    }
}
