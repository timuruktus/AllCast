package trelico.ru.allcast.screens.prepare;


import android.media.MediaPlayer;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import java.util.ArrayList;

import trelico.ru.allcast.repositories.TTSRepo;

@InjectViewState
public class PreparePresenter extends MvpPresenter<IPrepareFragment> implements PrepareAPI,
        HintDialogListener{

    private static final int TEXT_LENGTH_LIMIT = 5000;
    private ArrayList<String> textsSplitByCharLimit = new ArrayList<>();


    @Override
    public PrepareFragment getInstance(){
        return PrepareFragment.getInstance();
    }

    @Override
    public void onPositiveButtonClicked(String appUri){
        getViewState().launchApp(appUri);
        getViewState().setClipboardListener();
    }

    /**
     * Executes after copying text from recently opened Telegram app.
     * @param text - copied text from Telegram
     */
    protected void onLaunchedAppTextCopied(String text){
        getViewState().removeClipboardListener();
        textsSplitByCharLimit.clear();
        for (int i = 0; i < text.length(); i += TEXT_LENGTH_LIMIT) {
            textsSplitByCharLimit.add(text.substring(i, Math.min(i + TEXT_LENGTH_LIMIT, text.length())));
        }
        MediaPlayer mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnInfoListener();
        TTSRepo ttsRepo = TTSRepo.getInstance();
        ttsRepo.getSpeechUriFromText(textsSplitByCharLimit.get(0))
                .subscribe(audioUri -> {

                })

                //TODO: Handle exceptions

        //TODO: sending yandex request, save it and open music player fragment to play it
    }
}
