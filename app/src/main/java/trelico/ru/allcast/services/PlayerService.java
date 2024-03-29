package trelico.ru.allcast.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioAttributes;
import android.media.AudioFocusRequest;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.media.session.MediaButtonReceiver;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import trelico.ru.allcast.R;
import trelico.ru.allcast.repositories.playlist.LastTrackException;
import trelico.ru.allcast.repositories.playlist.PlaylistRepo;
import trelico.ru.allcast.screens.audio_player.PlayerActivity;
import trelico.ru.allcast.utils.MediaStyleHelper;

import static android.media.AudioManager.AUDIOFOCUS_GAIN;
import static android.media.AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK;

final public class PlayerService extends Service{

    private final int NOTIFICATION_ID = 404;
    public static final String NOTIFICATION_DEFAULT_CHANNEL_ID = "default_channel";

    private final MediaMetadataCompat.Builder metadataBuilder = new MediaMetadataCompat.Builder();

    private final PlaybackStateCompat.Builder stateBuilder = new PlaybackStateCompat.Builder().setActions(
            PlaybackStateCompat.ACTION_PLAY
                    | PlaybackStateCompat.ACTION_STOP
                    | PlaybackStateCompat.ACTION_PAUSE
                    | PlaybackStateCompat.ACTION_PLAY_PAUSE
                    | PlaybackStateCompat.ACTION_SKIP_TO_NEXT
                    | PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS
    );

    private MediaSessionCompat mediaSession;

    private AudioManager audioManager;
    private AudioFocusRequest audioFocusRequest;
    private boolean audioFocusRequested = false;

    private SimpleExoPlayer exoPlayer;
    private ExtractorsFactory extractorsFactory;
    private DataSource.Factory dataSourceFactory;

    private  PlaylistRepo playlistRepo;

    private static final boolean PAUSE_WHEN_DUCKED = true;

    @Override
    public void onCreate(){
        super.onCreate();

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel notificationChannel =
                    new NotificationChannel(NOTIFICATION_DEFAULT_CHANNEL_ID,
                            getString(R.string.allcast),
                            NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(notificationChannel);

            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build();
            audioFocusRequest = new AudioFocusRequest.Builder(AUDIOFOCUS_GAIN)
                    .setOnAudioFocusChangeListener(audioFocusChangeListener)
                    .setAcceptsDelayedFocusGain(true)
                    .setWillPauseWhenDucked(PAUSE_WHEN_DUCKED)
                    .setAudioAttributes(audioAttributes)
                    .build();
        }

        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        mediaSession = new MediaSessionCompat(this, "AllCast PlayerService");
        mediaSession.setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS
                | MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);
        mediaSession.setCallback(mediaSessionCallback);

        Context appContext = getApplicationContext();

        Intent activityIntent = new Intent(appContext, PlayerActivity.class);
        mediaSession.setSessionActivity(PendingIntent.getActivity(appContext, 0, activityIntent, 0));

        Intent mediaButtonIntent = new Intent(Intent.ACTION_MEDIA_BUTTON, null,
                appContext, MediaButtonReceiver.class);
        mediaSession.setMediaButtonReceiver(PendingIntent
                .getBroadcast(appContext, 0, mediaButtonIntent, 0));

        exoPlayer = ExoPlayerFactory.newSimpleInstance(appContext,
                new DefaultRenderersFactory(this),
                new DefaultTrackSelector(),
                new DefaultLoadControl());
        exoPlayer.addListener(exoPlayerListener);
        exoPlayer.setPlayWhenReady(true);
        this.dataSourceFactory = new DefaultDataSourceFactory(appContext,
                Util.getUserAgent(this, getString(R.string.app_name)));
        this.extractorsFactory = new DefaultExtractorsFactory();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        MediaButtonReceiver.handleIntent(mediaSession, intent);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        mediaSession.release();
        exoPlayer.release();
    }

    private MediaSessionCompat.Callback mediaSessionCallback = new MediaSessionCompat.Callback(){
        private Uri currentUri;
        int currentState = PlaybackStateCompat.STATE_STOPPED;

        @Override
        public void onPlay(){
            if(!exoPlayer.getPlayWhenReady()){
                startService(new Intent(getApplicationContext(), PlayerService.class));

                PlaylistRepo.Track track = playlistRepo.getCurrentTrack();
                updateMetadataFromTrack(track);

                prepareToPlay(track.getUri());

                if(!audioFocusRequested){
                    audioFocusRequested = true;

                    int audioFocusResult;
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                        audioFocusResult = audioManager.requestAudioFocus(audioFocusRequest);
                    } else{
                        audioFocusResult = audioManager.requestAudioFocus(audioFocusChangeListener,
                                AudioManager.STREAM_MUSIC, AUDIOFOCUS_GAIN);
                    }
                    if(audioFocusResult != AudioManager.AUDIOFOCUS_REQUEST_GRANTED)
                        return;
                }

                mediaSession.setActive(true); // Сразу после получения фокуса

                registerReceiver(becomingNoisyReceiver,
                        new IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY));

                exoPlayer.setPlayWhenReady(true);
            }

            long currentPlayedTimeInMs = exoPlayer.getContentPosition();
            mediaSession.setPlaybackState(stateBuilder.setState(PlaybackStateCompat.STATE_PLAYING,
                    currentPlayedTimeInMs, 1).build());
            currentState = PlaybackStateCompat.STATE_PLAYING;

            refreshNotificationAndForegroundStatus(currentState);
        }

        @Override
        public void onPause(){
            if(exoPlayer.getPlayWhenReady()){
                exoPlayer.setPlayWhenReady(false);
                unregisterReceiver(becomingNoisyReceiver);
            }

            long currentPlayedTimeInMs = exoPlayer.getContentPosition();
            mediaSession.setPlaybackState(stateBuilder.setState(PlaybackStateCompat.STATE_PAUSED,
                    currentPlayedTimeInMs, 1).build());
            currentState = PlaybackStateCompat.STATE_PAUSED;

            refreshNotificationAndForegroundStatus(currentState);
        }

        @Override
        public void onStop(){
            if(exoPlayer.getPlayWhenReady()){
                exoPlayer.setPlayWhenReady(false);
                unregisterReceiver(becomingNoisyReceiver);
            }

            if(audioFocusRequested){
                audioFocusRequested = false;

                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                    audioManager.abandonAudioFocusRequest(audioFocusRequest);
                } else{
                    audioManager.abandonAudioFocus(audioFocusChangeListener);
                }
            }

            mediaSession.setActive(false);
            long currentPlayedTimeInMs = exoPlayer.getContentPosition();
            mediaSession.setPlaybackState(stateBuilder.setState(PlaybackStateCompat.STATE_STOPPED,
                    currentPlayedTimeInMs, 1).build());
            currentState = PlaybackStateCompat.STATE_STOPPED;

            refreshNotificationAndForegroundStatus(currentState);

            stopSelf();
        }

        @Override
        public void onSkipToNext(){
            PlaylistRepo.Track track;
            try{
                track = playlistRepo.getNextTrack();

                updateMetadataFromTrack(track);

                refreshNotificationAndForegroundStatus(currentState);

                prepareToPlay(track.getUri());
            } catch(LastTrackException e){
                e.printStackTrace();
                onStop();
            }
        }

        @Override
        public void onSkipToPrevious(){
            PlaylistRepo.Track track = playlistRepo.getPreviousTrack();
            updateMetadataFromTrack(track);

            refreshNotificationAndForegroundStatus(currentState);

            prepareToPlay(track.getUri());
        }

        private void prepareToPlay(Uri uri){
            if(!uri.equals(currentUri)){
                currentUri = uri;
                ProgressiveMediaSource mediaSource =
                        new ProgressiveMediaSource.Factory(dataSourceFactory, extractorsFactory)
                                .createMediaSource(currentUri);
                exoPlayer.prepare(mediaSource);
            }
        }

        private void updateMetadataFromTrack(PlaylistRepo.Track track){
            metadataBuilder.putString(MediaMetadataCompat.METADATA_KEY_TITLE, track.getTitle());
            metadataBuilder.putString(MediaMetadataCompat.METADATA_KEY_ALBUM, track.getAlbum());
            metadataBuilder.putString(MediaMetadataCompat.METADATA_KEY_ARTIST, track.getArtist());
            metadataBuilder.putLong(MediaMetadataCompat.METADATA_KEY_DURATION, track.getDurationInMs());
            mediaSession.setMetadata(metadataBuilder.build());
        }
    };

    private AudioManager.OnAudioFocusChangeListener audioFocusChangeListener = focusChange -> {
        switch(focusChange){
            case AUDIOFOCUS_GAIN:
                exoPlayer.setVolume(1f); //MAY BE A BUG
                mediaSessionCallback.onPlay(); // Не очень красиво (вызов метода колбэка)
                break;
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                mediaSessionCallback.onPause();
                break;
            case AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK:
                exoPlayer.setVolume(exoPlayer.getVolume() - 0.2f); //MAY BE A BUG
                //TODO: lower sound
                break;
            default:
                mediaSessionCallback.onPause();
                break;
        }
    };

    private final BroadcastReceiver becomingNoisyReceiver = new BroadcastReceiver(){
        @Override
        public void onReceive(Context context, Intent intent){
            // Disconnecting headphones - stop playback
            if(AudioManager.ACTION_AUDIO_BECOMING_NOISY.equals(intent.getAction())){
                mediaSessionCallback.onPause();
            }
        }
    };

    private ExoPlayer.EventListener exoPlayerListener = new ExoPlayer.EventListener(){

        @Override
        public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections){
        }

        @Override
        public void onLoadingChanged(boolean isLoading){
        }

        @Override
        public void onPlayerStateChanged(boolean playWhenReady, int playbackState){
            if(playWhenReady && playbackState == ExoPlayer.STATE_ENDED){
                mediaSessionCallback.onSkipToNext();
            }
        }

        @Override
        public void onPlayerError(ExoPlaybackException error){

        }

        @Override
        public void onPlaybackParametersChanged(PlaybackParameters playbackParameters){

        }
    };

    @Override
    public IBinder onBind(Intent intent){
        return new PlayerServiceBinder();
    }

    public class PlayerServiceBinder extends Binder{

        public void setPlaylistRepo(PlaylistRepo playlistRepo){
            PlayerService.this.playlistRepo = playlistRepo;
        }
        public MediaSessionCompat.Token getMediaSessionToken(){
            return mediaSession.getSessionToken();
        }
    }

    private void refreshNotificationAndForegroundStatus(int playbackState){
        switch(playbackState){
            case PlaybackStateCompat.STATE_PLAYING:{
                startForeground(NOTIFICATION_ID, getNotification(playbackState));
                break;
            }
            case PlaybackStateCompat.STATE_PAUSED:{
                NotificationManagerCompat.from(PlayerService.this).notify(NOTIFICATION_ID, getNotification(playbackState));
                stopForeground(false);
                break;
            }
            default:{
                stopForeground(true);
                break;
            }
        }
    }

    private Notification getNotification(int playbackState){
        NotificationCompat.Builder builder = MediaStyleHelper.from(this, mediaSession);
        builder.addAction(new NotificationCompat.Action(android.R.drawable.ic_media_previous,
                getString(R.string.previous),
                MediaButtonReceiver.buildMediaButtonPendingIntent(this, PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS)));

        if(playbackState == PlaybackStateCompat.STATE_PLAYING)
            builder.addAction(new NotificationCompat.Action(android.R.drawable.ic_media_pause,
                    getString(R.string.pause),
                    MediaButtonReceiver.buildMediaButtonPendingIntent(this, PlaybackStateCompat.ACTION_PLAY_PAUSE)));
        else
            builder.addAction(new NotificationCompat.Action(android.R.drawable.ic_media_play,
                    getString(R.string.play),
                    MediaButtonReceiver.buildMediaButtonPendingIntent(this, PlaybackStateCompat.ACTION_PLAY_PAUSE)));

        builder.addAction(new NotificationCompat.Action(android.R.drawable.ic_media_next,
                getString(R.string.next),
                MediaButtonReceiver.buildMediaButtonPendingIntent(this, PlaybackStateCompat.ACTION_SKIP_TO_NEXT)));
        builder.setStyle(new androidx.media.app.NotificationCompat.MediaStyle()
                .setShowActionsInCompactView(1)
                .setShowCancelButton(true)
                .setCancelButtonIntent(MediaButtonReceiver
                        .buildMediaButtonPendingIntent(this, PlaybackStateCompat.ACTION_STOP))
                .setMediaSession(mediaSession.getSessionToken())); // setMediaSession требуется для Android Wear
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setColor(ContextCompat.getColor(this, R.color.colorPrimaryDark)); // The whole background (in MediaStyle), not just icon background
        builder.setShowWhen(false);
        builder.setPriority(NotificationCompat.PRIORITY_HIGH);
        builder.setOnlyAlertOnce(true);
        builder.setChannelId(NOTIFICATION_DEFAULT_CHANNEL_ID);

        return builder.build();
    }
}
