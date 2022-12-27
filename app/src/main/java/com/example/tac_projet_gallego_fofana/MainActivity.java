package com.example.tac_projet_gallego_fofana;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.tac_projet_gallego_fofana.api.API_Client;
import com.example.tac_projet_gallego_fofana.api.API_Interface;
import com.example.tac_projet_gallego_fofana.api.Genre;
import com.example.tac_projet_gallego_fofana.api.GenreCatalog;
import com.example.tac_projet_gallego_fofana.api.Movie;
import com.example.tac_projet_gallego_fofana.api.MovieCatalog;
import com.example.tac_projet_gallego_fofana.data.entity.FavMovie;
import com.example.tac_projet_gallego_fofana.recycler.CustomAdapter;

import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private String TAG = "MATDAV";
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private CustomAdapter customAdapter;
    private Map<Integer, String> genre_dictionary = new HashMap<>();
    private MainActivityViewModel mainActivityViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Toast.makeText(this, "on create main", Toast.LENGTH_SHORT).show();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // on récupère le gestionnaire de fragments
        FragmentManager fragmentManager = getSupportFragmentManager();

        // on commence une opération (add, remove, replace,...) sur les fragments
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // on cherche à savoir s'il y a déjà un fragment avec le 'tag' F1.
        // Si non, on crée le fragment 1, et on affiche le fragment 1.
        // Si oui, on ne fait rien.
        Fragment fragmentM = fragmentManager.findFragmentByTag("Movie");
        if (fragmentM == null) {
            // Si ce fragment F1 n'existe pas, alors on commence par le créer
            MovieFragment fragmentMovie = MovieFragment.newInstance();

            // on demande à la transaction de l'ajouter avec le tag 'F1'
            fragmentTransaction.add(R.id.all_Fragments, fragmentMovie, "Movie");
            // si on garde le addToBackStack() ci-dessous, le fragment est "mémorisé" dans la pile;
            // et donc peut être retrouvé après un 'back' plus tard.
            //          fragmentTransaction.addToBackStack(null);
            // Dans ce cas, quand on supprimera le fragment avec un remove, il faudra aussi
            // appeler fragmentManager.popBackStack();  pour réellement supprimer le
            // fragment, sinon il n'est pas détruit !
        } else {
            // Pour information, le fragment 1 est ici à l'écran, pas forcément visible, mais dans l'état RESUMED. Pour le savoir :
            // Toast.makeText(MainActivity.this, fragment1.getLifecycle().getCurrentState().toString(), Toast.LENGTH_SHORT).show();
            fragmentTransaction.show(fragmentM);
        }

        // on cherche à savoir s'il y a déjà un fragment avec le 'tag' F2 à l'écran
        /*Fragment fragmentF = fragmentManager.findFragmentByTag("Favoris");
        if (fragmentF != null) {
            // on demande à la transaction de cacher ce fragment 2
            fragmentTransaction.hide(fragmentF);
        }
*/
        // on exécute la transaction (qui pourrait contenir plusieurs ordres en même temps)
        fragmentTransaction.commit();

/*
        // affichage du fragment au clic sur le bouton Fragment Two
        findViewById(R.id.showFragmentTwo).

            setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    // on récupère le gestionnaire de fragments
                    FragmentManager fragmentManager = getSupportFragmentManager();

                    // on commence une opération (add, remove, replace,...) sur les fragments
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                    // on cherche à savoir s'il y a déjà un fragment avec le 'tag' F2.
                    // Si non, on crée le fragment 2, et on affiche le fragment 2.
                    // Si oui, on ne fait rien.
                    Fragment fragment2 = fragmentManager.findFragmentByTag("F2");
                    if (fragment2 == null) {
                        // Si ce fragment F2 n'existe pas, alors on commence par le créer
                        FragmentTwo fragmentTwo = FragmentTwo.newInstance();

                        // on demande à la transaction de l'ajouter avec le tag 'F2'
                        fragmentTransaction.add(R.id.lesFragments, fragmentTwo, "F2");
                        // si on garde le addToBackStack() ci-dessous, le fragment est "mémorisé" dans la pile;
                        // et donc peut être retrouvé après un 'back' plus tard.
                        //          fragmentTransaction.addToBackStack(null);
                        // Dans ce cas, quand on supprimera le fragment avec un remove, il faudra aussi
                        // appeler fragmentManager.popBackStack();  pour réellement supprimer le
                        // fragment, sinon il n'est pas détruit !
                    } else fragmentTransaction.show(fragment2);

                    // on cherche à savoir s'il y a déjà un fragment avec le 'tag' F1 à l'écran
                    Fragment fragment1 = fragmentManager.findFragmentByTag("F1");
                    if (fragment1 != null) {
                        // on demande à la transaction de cacher ce fragment 2
                        fragmentTransaction.hide(fragment1);
                    }

                    // on exécute la transaction (qui pourrait contenir plusieurs ordres en même temps)
                    fragmentTransaction.commit();
                }

            });
*/
    }

    @Override
    protected void onResume() {
        super.onResume();
        Toast.makeText(this, "on resume main", Toast.LENGTH_SHORT).show();
    }
}