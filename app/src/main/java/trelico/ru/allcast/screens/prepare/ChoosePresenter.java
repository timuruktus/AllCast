package trelico.ru.allcast.screens.prepare;


import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import java.util.ArrayList;

import butterknife.internal.Utils;
import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import trelico.ru.allcast.repositories.TTSRepo;
import trelico.ru.allcast.repositories.playlist.PlaylistRepo;

import static trelico.ru.allcast.screens.audio_player.PlayerActivity.PLAYLIST;

@InjectViewState
public class ChoosePresenter extends MvpPresenter<IChooseFragment> implements ChooseAPI,
        HintDialogListener{

    private static final int TEXT_LENGTH_LIMIT = 5000;
    private ArrayList<String> textsSplitByCharLimit = new ArrayList<>();
    private String appTitle;


    @Override
    public ChooseFragment getInstance(){
        return ChooseFragment.getInstance();
    }

    @Override
    public void onPositiveButtonClicked(String appUri){
        getViewState().launchApp(appUri);
        getViewState().setClipboardListener();
    }

    /**
     * Executes after copying text from recently opened app.
     * @param text - copied text from app
     */
    protected void onLaunchedAppTextCopied(String text){
        getViewState().removeClipboardListener();
        textsSplitByCharLimit.clear();
        for (int i = 0; i < text.length(); i += TEXT_LENGTH_LIMIT) {
            textsSplitByCharLimit.add(text.substring(i, Math.min(i + TEXT_LENGTH_LIMIT, text.length())));
        }
        TTSRepo ttsRepo = TTSRepo.getInstance();
        Single<String> firstSpeechSingle = ttsRepo
                .getSpeechUriFromText(textsSplitByCharLimit.get(0), null)
                .flatMap(audioUri -> {
                    long duration = getTrackDurationInMs(audioUri);
                    PlaylistRepo.Track track = new PlaylistRepo.Track();
                    track.setText(text);
                    track.setDurationInMs(duration);
                    track.setUri(Uri.parse(audioUri));
                    track.setTitle(this.appTitle);
                    ArrayList<PlaylistRepo.Track> playlist = new ArrayList<>();
                    playlist.add(track);

                })
                .subscribeOn(Schedulers.io());

                //TODO: Handle exceptions

        //TODO: sending yandex request, save it and open music player fragment to play it
    }

    private void launchPlayerActivity(ArrayList<String> playlist){
        //TODO: we should pass textsSplitByCharLimit
        Intent intent = new Intent(getBaseContext(), SignoutActivity.class);
        intent.putStringArrayListExtra(PLAYLIST, playlist);
        intent.putExtra("EXTRA_SESSION_ID", sessionId);
        startActivity(intent);
    }

    protected void setLaunchedAppTitle(String title){
        appTitle = title;
    }

    private long getTrackDurationInMs(String uri){
        String mediaPath = Uri.parse(uri).getPath();
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        mmr.setDataSource(mediaPath);
        String duration = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        mmr.release();
        return Long.parseLong(duration);
    }
}
