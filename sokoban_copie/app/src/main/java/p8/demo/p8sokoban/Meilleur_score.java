package p8.demo.p8sokoban;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

/**
 * Created by Nico on 30/12/2016.
 */

public class Meilleur_score extends Activity {


    public static TextView textView_score;
    public static int score = 0;
    public static final String PREFS_NAME_SCORE = "sauvegardeMeilleurScore";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // affiche propos.xml
        setContentView(R.layout.score);

        textView_score = (TextView) findViewById(R.id.textView_score);
    }


    protected void onResume() {
        super.onResume();
        // tantative pour recuperer le meilleur score (pas fini et ne fonctionne pas)
        //recuperationMeilleurScore();
        textView_score.setText(" Meilleur score : "+score);
    }


    // tantative pour recuperer le meilleur score (pas fini et ne fonctionne pas)
    /*
    // fonction qui gere la persistance
    public void recuperationMeilleurScore() {
        SharedPreferences settings = getSharedPreferences(PREFS_NAME_SCORE, 0);
        SharedPreferences.Editor editor = settings.edit();
        score = settings.getInt("score", 0);


        return;
    }
    */
}
