package trelico.ru.allcast.screens.main;

public interface NavigationManager{

    void navigateTo(int resId);
    void navigateWithPopTo(int fragmentIdToNavigate, int fragmentIdToPop, boolean inclusive);
}
