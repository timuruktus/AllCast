package trelico.ru.allcast.screens.prepare;


import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.OnClick;
import trelico.ru.allcast.MyApp;
import trelico.ru.allcast.R;
import trelico.ru.allcast.screens.main.MainActivity;
import trelico.ru.allcast.utils.AndroidUtils;


class PrepareFragment extends MvpAppCompatFragment implements IPrepareFragment{

    @BindView(R.id.chooseSourcesApp)
    TextView chooseSourcesApp;
    @BindView(R.id.telegram)
    Button telegram;
    @BindView(R.id.title)
    TextView title;
    @BindString(R.string.understand)
    String understand;
    @BindString(R.string.telegram_hint)
    String telegramHint;
    @BindString(R.string.error_happened)
    String errorHappened;

    @InjectPresenter
    PreparePresenter preparePresenter;
    private String telegramHintTag = "telegram";
    private ClipboardManager clipboard;
    private ClipboardManager.OnPrimaryClipChangedListener clipboardListener;
    protected static final String TELEGRAM_URI = "org.telegram.messenger";

    PrepareFragment(){
        // Required empty public constructor
    }

    protected static PrepareFragment getInstance(){
        return new PrepareFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        clipboard = (ClipboardManager) MyApp.INSTANCE.getSystemService(Context.CLIPBOARD_SERVICE);
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_prepare, container, false);
    }

    @OnClick(R.id.telegram)
    public void onTelegramClicked(){
        HintDialog.createDialog(telegramHint, understand, preparePresenter, TELEGRAM_URI)
                .show(getChildFragmentManager(), telegramHintTag);
    }

    @Override
    public void launchApp(String appUri){
        final boolean isAppInstalled = AndroidUtils.isAppAvailable(MyApp.INSTANCE, appUri);
        if(isAppInstalled){
            Intent telegramIntent = new Intent(Intent.ACTION_MAIN);
            telegramIntent.setPackage(appUri);
            startActivity(telegramIntent);
        } else{
            Toast.makeText(getContext(), "App is not Installed", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void setClipboardListener(){
        clipboard.addPrimaryClipChangedListener(getClipboardListener());
    }

    @Override
    public void removeClipboardListener(){
        clipboard.removePrimaryClipChangedListener(getClipboardListener());
        clipboardListener = null;
    }

    private ClipboardManager.OnPrimaryClipChangedListener getClipboardListener(){
        //Что делать, если фрагмент пересоздался и старый clipboardlistener ушел в небытие?
        if(clipboardListener == null){
            this.clipboardListener = () -> {
                try{
                    String copiedText = clipboard.getPrimaryClip().getItemAt(0).coerceToText(getContext()).toString();
                    if(!copiedText.isEmpty()){
                        AndroidUtils.bringActivityToForeground(getContext(), MainActivity.class);
                        preparePresenter.onLaunchedAppTextCopied(copiedText);
                    }
                    Toast.makeText(getContext(), "Copy:\n" + copiedText, Toast.LENGTH_SHORT).show();
                } catch(NullPointerException ex){
                    Toast.makeText(getContext(), errorHappened, Toast.LENGTH_SHORT).show();
                }
            };
        }
        return clipboardListener;
    }

    @Override
    public void onResume(){
        super.onResume();
        if(clipboardListener != null) removeClipboardListener();
    }
}
