package p8.demo.p8sokoban;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.TextView;


import static p8.demo.p8sokoban.R.id.jouer;
import static p8.demo.p8sokoban.R.id.textView_coups;
import static p8.demo.p8sokoban.SokobanView.carteHeight;
import static p8.demo.p8sokoban.SokobanView.carteWidth;
import static p8.demo.p8sokoban.SokobanView.meilleurScore;
import static p8.demo.p8sokoban.SokobanView.variableNiveauTest;

// declaration de notre activity heritee de Activity
public class p8_Sokoban extends Activity {


    private SokobanView mSokobanView;

    // String qui permet de retrouver les sauvegarde
    public static final String PREFS_NAME = "sauvegardeTableau";
    public static final String PREFS_NAME_RESET = "sauvegardeVariableNiveau";
    public static final String PREFS_NAME_SCORE = "sauvegardeMeilleurScore";


    // TextView qui vont nous permetre de reprer l id de mes textView dans le XML
    public static TextView textView_temps;
    public static TextView textView_coups;

    // declaration de notre CountDownTimer qui sert a calculer le temps
    public static CountDownTimer x = null;


	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        // initialise notre activity avec le constructeur parent    	
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main);
        mSokobanView = (SokobanView) findViewById(R.id.SokobanView);
        mSokobanView.setVisibility(View.VISIBLE);
        // charge le fichier main.xml comme vue de l'activite
        // recuperation de la vue une voie cree a partir de son id
        // rend visible la vue


        //recuperer les textView pour le nombre de coups et pour le temps
        textView_temps = (TextView) findViewById(R.id.textView_temps);
        textView_coups = (TextView) findViewById(R.id.textView_coups);

        // appel la fonction onTick pour afficher le nombre de coup a la creation de l'activity
        onTick();
    }

    public static int temps = 100;
    public static int tempsTotalSecondes = 0;
    public static int tempsRafraichissement = 100;


    // permet d'afficher le temps pour finir un niveau et d'afficher
    // le nombre de coups restant chaque tempsRafraishissement
    public static void onTick() {

            x = (CountDownTimer) new CountDownTimer(temps,tempsRafraichissement) {

            public void onTick(long millisUntilFinished) {
                textView_coups.setText("coups : "+(SokobanView.nombreCoups));

                tempsTotalSecondes = tempsTotalSecondes + temps;
                //tempsTotalSecondesMoinsUn = tempsTotalSecondes;

                    // si le temps est plus petit que 1000seconde alors on afficher le temps en seconde
                    if((tempsTotalSecondes/10000) < 1000) {
                        textView_temps.setText("temps : " + tempsTotalSecondes / 1000+"s");
                    }
                    // sinon on affiche le temps en minute
                else {
                        textView_temps.setText("temps : " + (tempsTotalSecondes / 1000)/60+"m");
                    }
            }

            public void onFinish() {
                // premet de cree une boucle infinit (losque x a finit on le relance directement)
                x.start();
            }

        }.start();
    }

    // met le temps a 0
    public static void tempsNiveauZero(){
        tempsTotalSecondes = 0;
        return;
    }


    // fonction qui gere la persistance
        public void sauvegardeTableau() {

            SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.putInt("array_size", carteHeight);
            for (int i = 0; i < carteHeight; i++) {
                for (int j = 0; j < carteWidth; j++) {
                    editor.putInt("array_" + i, SokobanView.ref[j][i]);

                    editor.commit();
                }
            }
            return;
        }

    // fonction qui gere la persistance
        public void recuperationTableau() {

            SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
            SharedPreferences.Editor editor = settings.edit();
            int size = settings.getInt("array_size", 0);
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    settings.getInt("array_" + i, SokobanView.ref[j][i]);
                }
            }
            return;
        }

    // fonction qui gere la persistance
        public void sauvegardeVariable() {

            SharedPreferences settings = getSharedPreferences(PREFS_NAME_RESET, 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.putInt("variable", SokobanView.variableNiveau);
        }

    // fonction qui gere la persistance
        public void recuperationVariable() {

            SharedPreferences settings = getSharedPreferences(PREFS_NAME_RESET, 0);
            SharedPreferences.Editor editor = settings.edit();
            settings.getInt("variable", SokobanView.variableNiveau);
            return;
        }


// tantative pour sauvegarder le meilleur score (pas finis et ne fonctionne pas)

    /*
    public static void recuperationScore(int nouveauScore){
        p8_meilleurScore1++;
    }


    // fonction qui gere la persistance
    public void sauvegardeMeilleurScore() {

        SharedPreferences settings = getSharedPreferences(PREFS_NAME_SCORE, 0);
        SharedPreferences.Editor editor = settings.edit();
            editor.putInt("score", p8_meilleurScore1);
    }

    // fonction qui gere la persistance
    public void recuperationMeilleurScore() {

        SharedPreferences settings = getSharedPreferences(PREFS_NAME_SCORE, 0);
        SharedPreferences.Editor editor = settings.edit();
        settings.getInt("score", p8_meilleurScore2);
        return;
    }
*/

    @Override
    protected void onPause() {
        super.onPause();
        // Sauvegarde des paramètres
        // pour pouvoir les restaurer au prochain démarrage
        {
            //sauvegardeMeilleurScore();
            sauvegardeTableau();
            sauvegardeVariable();
            finish();
            // met en pause le onTick
            x.cancel();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

            // relance le onTick
            x.start();

            SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
            int size = settings.getInt("array_size", 0);
            recuperationVariable();
            if (size > 0) {
                recuperationTableau();
                SokobanView.variableNiveau = 1;
                sauvegardeVariable();
            } else {
                SokobanView.variableNiveau = 0;
            }
    }
}