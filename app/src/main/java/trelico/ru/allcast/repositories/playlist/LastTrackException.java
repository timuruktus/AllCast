package trelico.ru.allcast.repositories.playlist;

public class LastTrackException extends Exception{

    @Override
    public String getMessage(){
        return "This was the last track in playback without REPEAT_PLAYLIST or REPEAT_TRACK exception";
    }
}
