package trelico.ru.allcast.repositories;

import android.net.Uri;

import java.util.ArrayList;

public class PlaylistRepo implements PlaylistAPI{


    private ArrayList<Track> playlist = new ArrayList<>();

    public PlaylistRepo(){
    }

    public PlaylistRepo(ArrayList<Track> playlist){
        this.playlist = playlist;
    }

    @Override
    public Track getNext(){
        return null;
    }

    @Override
    public Track getPrevious(){
        return null;
    }

    @Override
    public Track getCurrent(){
        return null;
    }

    @Override
    public ArrayList<Track> getPlaylist(){
        return playlist;
    }

    @Override
    public void addTrack(Track track){
        playlist.add(track);
    }

    @Override
    public void insertTrack(Track track, int position){
        if(position >= 0 && position < playlist.size()) playlist.add(position, track);
        else playlist.add(track);
    }

    @Override
    public void removeTrack(Track track){
        playlist.remove(track);
    }

    @Override
    public void clearPlaylist(){
        playlist.clear();
    }

    public static class Track{

        private String album;
        private String title;
        private String artist;
        private String text;
        private Uri uri;
        private long durationInMs;

        public Track(String album, String title, String artist, String text, Uri uri, long durationInMs){
            this.album = album;
            this.title = title;
            this.artist = artist;
            this.text = text;
            this.uri = uri;
            this.durationInMs = durationInMs;
        }

        public String getTitle(){
            return title;
        }

        public String getArtist(){
            return artist;
        }

        public Uri getUri(){
            return uri;
        }

        public long getDurationInMs(){
            return durationInMs;
        }

        public String getAlbum(){
            return album;
        }

        public String getText(){
            return text;
        }
    }
}
