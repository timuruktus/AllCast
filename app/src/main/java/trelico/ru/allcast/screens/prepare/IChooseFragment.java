package trelico.ru.allcast.screens.prepare;

import com.arellomobile.mvp.MvpView;

interface IChooseFragment extends MvpView{

    void launchApp(String appUri);
    void setClipboardListener();
    void removeClipboardListener();

}
