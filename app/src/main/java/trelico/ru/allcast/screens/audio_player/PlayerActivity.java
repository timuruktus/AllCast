package trelico.ru.allcast.screens.audio_player;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.widget.ImageButton;
import android.widget.TextView;

import com.arellomobile.mvp.MvpAppCompatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import trelico.ru.allcast.R;
import trelico.ru.allcast.services.PlayerService;

public class PlayerActivity extends MvpAppCompatActivity implements IPlayerActivity{

    PlayerService.PlayerServiceBinder playerServiceBinder;
    MediaControllerCompat mediaController;
    @BindView(R.id.mainTextTitle)
    TextView mainTextTitle;
    @BindView(R.id.mainText)
    TextView mainText;
    @BindView(R.id.titleText)
    TextView titleText;
    @BindView(R.id.authorText)
    TextView authorText;
    @BindView(R.id.previousButton)
    ImageButton previousButton;
    @BindView(R.id.nextButton)
    ImageButton nextButton;
    @BindView(R.id.playPauseButton)
    ImageButton playPauseButton;
    @BindView(R.id.repeatButton)
    ImageButton repeatButton;

    public static final String PLAYLIST = "Playlist";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        ButterKnife.bind(this);

        bindService(new Intent(this, PlayerService.class),
                getServiceConnection(),
                BIND_AUTO_CREATE);

    }

    private ServiceConnection getServiceConnection(){
        return new ServiceConnection(){
            //TODO: send playlist to a service
            @Override
            public void onServiceConnected(ComponentName name, IBinder service){
                playerServiceBinder = (PlayerService.PlayerServiceBinder) service;
                try{
                    mediaController = new MediaControllerCompat(
                            PlayerActivity.this, playerServiceBinder.getMediaSessionToken());
                    mediaController.registerCallback(
                            new MediaControllerCompat.Callback(){
                                @Override
                                public void onPlaybackStateChanged(PlaybackStateCompat state){
                                    if(state == null)
                                        return;
                                    boolean playing =
                                            state.getState() == PlaybackStateCompat.STATE_PLAYING;
                                    playButton.setEnabled(!playing);
                                    pauseButton.setEnabled(playing);
                                    stopButton.setEnabled(playing);
                                }
                            }
                    );
                } catch(RemoteException e){
                    mediaController = null;
                }
            }

            @Override
            public void onServiceDisconnected(ComponentName name){
                playerServiceBinder = null;
                mediaController = null;
            }
        }
    }

    @OnClick(R.id.previousButton)
    public void onPreviousButtonClicked(){
        mediaController.getTransportControls().skipToPrevious();
    }

    @OnClick(R.id.nextButton)
    public void onNextButtonClicked(){
        mediaController.getTransportControls().skipToNext();
    }

    @OnClick(R.id.playPauseButton)
    public void onPlayPauseButtonClicked(){
        mediaController.getTransportControls().pause();
    }

    @OnClick(R.id.repeatButton)
    public void onRepeatButtonClicked(){

    }
}
