package trelico.ru.allcast.repositories.playlist;

import java.util.ArrayList;

public interface PlaylistAPI{

    PlaylistRepo.Track getNextTrack() throws LastTrackException;
    PlaylistRepo.Track getPreviousTrack();
    PlaylistRepo.Track getCurrentTrack();

    ArrayList<PlaylistRepo.Track> getPlaylist();

    void addTrack(PlaylistRepo.Track track);
    void addTracks(ArrayList<PlaylistRepo.Track> tracks);
    void setCurrentTrack(PlaylistRepo.Track currentTrack);
    void setPlaybackStrategy(PlaylistRepo.PlaybackStrategy playbackStrategy);
    void insertTrack(PlaylistRepo.Track track, int position);
    void removeTrack(PlaylistRepo.Track track);
    boolean removeTrack(int position);
    void clearPlaylist();
}
