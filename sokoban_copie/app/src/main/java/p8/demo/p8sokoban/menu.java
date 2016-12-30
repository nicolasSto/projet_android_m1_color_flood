package p8.demo.p8sokoban;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import static p8.demo.p8sokoban.R.layout.jouer;

/**
 * Created by Nico on 07/11/2016.
 */

public class menu extends Activity {

    //public static int numOfLevel = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);
    }

    // si on clique sur le bouton "Jouer" on affiche jouer.xml
    public void buttonJouer(View view){
        Intent in = new Intent(this,Jouer.class);
        startActivity(in);
    }

    // si on clique sur le bouton "A propos" on affiche propos.xml
    public void aPropos(View view){
        Intent in = new Intent(this,Propos.class);
        startActivity(in);
    }

    // si on clique sur le bouton "Meilleur score" on affiche score.xml
    public void meilleurScore(View view){
        Intent in = new Intent(this,Meilleur_score.class);
        startActivity(in);
    }

}
