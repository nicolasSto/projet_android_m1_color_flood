package p8.demo.p8sokoban;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.SurfaceView;
import android.view.View;

//import static p8.demo.p8sokoban.menu.numOfLevel;

/**
 * Created by Nico on 28/12/2016.
 */

public class Jouer extends Activity {

    public static int niveau = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.jouer);
    }

    // si on clique sur le bouton "nouvelle partie  niveau facile" on lance l'activity p8_sokoban
    // qui permet d'afficher la vue du jeu
    // et on met la variable niveau a 1 qui indique que on lance la vue du jeu en mode facile
    public void buttonNiveauFacile(View view){
        niveau = 1;
        Intent in = new Intent(this,p8_Sokoban.class);
        startActivity(in);
    }

    // si on clique sur le bouton "nouvelle partie niveau moyen" on lance l'activity p8_sokoban
    // qui permet d'afficher la vue du jeu
    // et on met la variable niveau a 2 qui indique que on lance la vue du jeu en mode moyen
    public void buttonNiveauMoyen(View view){
        niveau = 2;
        Intent in = new Intent(this,p8_Sokoban.class);
        startActivity(in);
    }

    // si on clique sur le bouton "nouvelle partie niveau difficile" on lance l'activity p8_sokoban
    // qui permet d'afficher la vue du jeu
    // et on met la variable niveau a 3 qui indique que on lance la vue du jeu en mode difficile
    public void buttonNiveauDifficile(View view){
        niveau = 3;
        Intent in = new Intent(this,p8_Sokoban.class);
        startActivity(in);
    }

    // si on clique sur le bouton "reprendre la partie en cour" on lance l'activity p8_sokoban
    // qui permet d'afficher la vue du jeu
    // et on met la variable niveau a 4 qui indique que on lance la vue du jeu de la partie de avant
    public void buttonResume(View view){
        niveau = 4;
        Intent in = new Intent(this,p8_Sokoban.class);
        startActivity(in);
    }
}
