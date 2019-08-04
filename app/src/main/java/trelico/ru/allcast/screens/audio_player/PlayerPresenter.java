package trelico.ru.allcast.screens.audio_player;

import android.content.Context;
import android.net.Uri;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import trelico.ru.allcast.MyApp;

@InjectViewState
public class PlayerPresenter extends MvpPresenter<IPlayerFragment>{


    private Context context = MyApp.INSTANCE;

    @Override
    protected void onFirstViewAttach(){
        super.onFirstViewAttach();

    }


}
