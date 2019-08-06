package trelico.ru.allcast.repositories.playlist;

import android.net.Uri;

import java.util.ArrayList;

public class PlaylistRepo implements PlaylistAPI{

    private ArrayList<Track> playlist;
    private Track currentTrack;
    private PlaybackStrategy playbackStrategy;

    public PlaylistRepo(ArrayList<Track> playlist, PlaybackStrategy playbackStrategy){
        this.playlist = playlist;
        this.playbackStrategy = playbackStrategy;
    }

    public PlaylistRepo(ArrayList<Track> playlist, Track currentTrack, PlaybackStrategy playbackStrategy){
        this.playlist = playlist;
        this.currentTrack = currentTrack;
        this.playbackStrategy = playbackStrategy;
    }

    @Override
    public Track getNextTrack() throws LastTrackException{
        int currentPosition = playlist.indexOf(currentTrack);
        if(currentPosition < 0){
            currentTrack = playlist.get(0);
            return currentTrack;
        }
        if(playbackStrategy == PlaybackStrategy.REPEAT_TRACK){
            currentTrack = playlist.get(currentPosition);
            return currentTrack;
        }
        if(currentPosition + 1 < playlist.size()){
            currentTrack = playlist.get(currentPosition + 1);
            return currentTrack;
        }
        if(currentPosition + 1 == playlist.size() && playbackStrategy == PlaybackStrategy.REPEAT_PLAYLIST){
            currentTrack = playlist.get(0);
            return currentTrack;
        } else throw new LastTrackException();
    }

    @Override
    public Track getPreviousTrack(){
        int currentPosition = playlist.indexOf(currentTrack);
        return currentPosition == 0 ? playlist.get(0) : playlist.get(currentPosition - 1);
    }

    @Override
    public Track getCurrentTrack(){
        if(currentTrack == null) return playlist.get(0);
        return currentTrack;
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
    public void addTracks(ArrayList<Track> tracks){
        playlist.addAll(tracks);
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


    /**
     * @param position - position of item to be removed
     * @return - true if item removed
     * - false if there is no item at that position
     */
    @Override
    public boolean removeTrack(int position){
        try{
            playlist.remove(position);
            return true;
        } catch(IndexOutOfBoundsException ex){
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public void clearPlaylist(){
        playlist.clear();
    }

    @Override
    public void setPlaybackStrategy(PlaybackStrategy playbackStrategy){
        this.playbackStrategy = playbackStrategy;
    }

    @Override
    public void setCurrentTrack(Track currentTrack){
        this.currentTrack = currentTrack;
    }


    public static class Track{

        private String album;
        private String title;
        private String artist;
        private String text;
        private Uri uri;
        private long durationInMs;
        private String description;

        public Track(){
        }

        public Track(String album, String title, String artist, String text, Uri uri,
                     long durationInMs, String description){
            this.album = album;
            this.title = title;
            this.artist = artist;
            this.text = text;
            this.uri = uri;
            this.durationInMs = durationInMs;
            this.description = description;
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

        public void setAlbum(String album){
            this.album = album;
        }

        public void setTitle(String title){
            this.title = title;
        }

        public void setArtist(String artist){
            this.artist = artist;
        }

        public void setText(String text){
            this.text = text;
        }

        public void setUri(Uri uri){
            this.uri = uri;
        }

        public void setDurationInMs(long durationInMs){
            this.durationInMs = durationInMs;
        }

        public String getDescription(){
            return description;
        }

        public void setDescription(String description){
            this.description = description;
        }
    }

    public enum PlaybackStrategy{
        REPEAT_PLAYLIST, REPEAT_TRACK, DONT_REPEAT
    }
}
