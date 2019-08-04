package trelico.ru.allcast.repositories;

import java.util.ArrayList;

public interface PlaylistAPI{

    PlaylistRepo.Track getNext();

    PlaylistRepo.Track getPrevious();

    PlaylistRepo.Track getCurrent();

    ArrayList<PlaylistRepo.Track> getPlaylist();

    void addTrack(PlaylistRepo.Track track);
    void insertTrack(PlaylistRepo.Track track, int position);
    void removeTrack(PlaylistRepo.Track track);
    void clearPlaylist();
}
