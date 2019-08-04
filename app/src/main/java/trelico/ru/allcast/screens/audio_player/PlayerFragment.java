package trelico.ru.allcast.screens.audio_player;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import butterknife.BindView;
import butterknife.ButterKnife;
import trelico.ru.allcast.MyApp;
import trelico.ru.allcast.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class PlayerFragment extends MvpAppCompatFragment implements IPlayerFragment{


    @BindView(R.id.playerView)
    PlayerView playerView;

    @InjectPresenter
    PlayerPresenter playerPresenter;
    private View view;
    private Context context = MyApp.INSTANCE;

    public PlayerFragment(){
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        view = inflater.inflate(R.layout.fragment_player, container, false);
        ButterKnife.bind(view);
        return view;
    }
}
