package p8.demo.p8sokoban;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;



public class SokobanView extends SurfaceView implements SurfaceHolder.Callback, Runnable {

    public static int variableNiveau = 0;
    public static int variableNiveauTest = 0;

    public static int meilleurScore;

    // variable qui sert a savoir combien de couleur utiliser suivant le niveau
    public static int nombreCouleur = 6;

    // nombre de coups jouer
    public static int nombreCoups = 0;
    public static int nombreCoupsMoinsUn = 0;

    // variable qui permet de savoir le nombre de coups pour chaque niveaux
    public int niveauUnCoups = 11;
    public int niveauDeuxCoups = 22;
    public int niveauTroisCoups = 33;


    private Canvas c = null;
    private int color_button = 1;
    int color_button_0;
    private boolean ifWin = false;
    private boolean ifLose = false;

    // bitmap pour afficher les case couleur du tableau
    private Bitmap 		blue_square;
    private Bitmap 		red_square;
    private Bitmap 		yellow_square;
    private Bitmap 		purple_square;
    private Bitmap 		green_square;
    private Bitmap 		pink_square;

    // bitmaps pour afficher les boutons de couleur
    private Bitmap 		blue_button;
    private Bitmap 		red_button;
    private Bitmap 		yellow_button;
    private Bitmap 		purple_button;
    private Bitmap 		green_button;
    private Bitmap 		pink_button;

    // bitmap pour l affichage de gagner ou perdu
    private Bitmap 		win;
    private Bitmap 		lose;
    
	// Declaration des objets Ressources et Context permettant d'acceder aux ressources de notre application et de les charger
    private Resources 	mRes;    
    private Context 	mContext;

    // tableau modelisant la carte du jeu
    int[][] carte;
    
    // ancres pour pouvoir centrer la carte du jeu
    int        carteTopAnchor;                   // coordonnees en Y du point d'ancrage de notre carte
    int        carteLeftAnchor;                  // coordonnees en X du point d'ancrage de notre carte

    // taille de la carte
    static int    carteWidth    = 10;
    static int    carteHeight   = 10;

    static final int    carteTileSize = 20;

    // constante modelisant les differentes types de cases
    static final int    Blue = 0;
    static final int    Green = 1;
    static final int    Red = 2;
    static final int    Purple = 3;
    static final int    Pink = 4;
    static final int    Yellow = 5;

    // tableau de reference du terrain taille 10x10
    static int [][] ref = {
            {0,0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0,0}
    };




    // classe qui permet de cree un objet de type case du tableau de jeux
    // avec des methode renvoie leurs coordonne
    private class caseJeux {

        // mes coordonees
        int x,y;

        // constructeur
        public caseJeux(int x, int y) {
            this.x = x;
            this.y = y;
        }

        // renvoi la coordonnee X
        public int renvoiX(){
            return x;
        }

        // renvoi la coordonnee X
        public int renvoiY() {
            return y;
        }
    }


private void changementCouleur(int nouvelleCouleur){

    // couleurRechercher prend la valeur de la premier case de la grille de jeux
    int ancienneCouleur = ref[0][0];

    // si l'ancienne couleur est la meme que la nouvelle alors on fini la fonction
    if (ancienneCouleur == nouvelleCouleur) {
        return;
    }

    Queue<caseJeux> maQueue = new LinkedList<caseJeux>();
    ArrayList<caseJeux> caseTester = new ArrayList<caseJeux>();

    maQueue.add(new caseJeux(0, 0));

    // creation d un objet de type case avec un x et un y
    caseJeux maCase;

    while (!maQueue.isEmpty()) {

        maCase = maQueue.remove();

        // si la case du tabelau ref[y][x] est egale a l'ancienne couleur de depart alors
        // on la remplace par la nouvelle couleur
        if (ref[maCase.renvoiY()][maCase.renvoiX()] == ancienneCouleur) {
            ref[maCase.renvoiY()][maCase.renvoiX()] = nouvelleCouleur;


            if ( (maCase.renvoiX() != 0) && (!caseTester.contains(new caseJeux(maCase.renvoiX() - 1, maCase.renvoiY()))) ) {
                maQueue.add(new caseJeux(maCase.renvoiX() - 1, maCase.renvoiY()));
            }

            if ( (maCase.renvoiX() != carteWidth - 1) && (!caseTester.contains(new caseJeux(maCase.renvoiX() + 1, maCase.renvoiY()))) ) {
                maQueue.add(new caseJeux(maCase.renvoiX() + 1, maCase.renvoiY()));
            }

            if ( (maCase.renvoiY() != 0) && (!caseTester.contains(new caseJeux(maCase.renvoiX(), maCase.renvoiY() - 1))) ) {
                maQueue.add(new caseJeux(maCase.renvoiX(), maCase.renvoiY() - 1));
            }

            if ( (maCase.renvoiY() != carteWidth - 1) && (!caseTester.contains(new caseJeux(maCase.renvoiX(), maCase.renvoiY() + 1))) ) {
                maQueue.add(new caseJeux(maCase.renvoiX(), maCase.renvoiY() + 1));
            }
        }
    }

    // quand le traitement est fini on appel la fonction loadlevel() pour remplire la carte avec les nouvelles
    // valeur de ref
    loadlevel();

    // decremente a chaque fois que changementCouleur(int) est appeller
    // le nombre de coups qu'il nous reste pour gagner
    nombreCoups--;
    return;
}


// change les valeurs dans le tableau ref[][] avec des valeurs aleatoire
    private void createRandomMap(){

        int i,j,n;
        Random rand = new Random();
        for(i=0;i<carteHeight;i++){
            for(j=0;j<carteWidth;j++) {
                n = rand.nextInt(nombreCouleur);
                ref[i][j] = n;
            }
        }
        return;
    }

        /* compteur et max pour animer les zones d'arrivee des diamants */
        int currentStepZone = 0;
        int maxStepZone     = 4;  

        // thread utiliser pour animer les zones de depot des diamants
        private     boolean in = true;
        private     Thread  cv_thread;        
        SurfaceHolder holder;
        Paint paint;
        
    /**
     * The constructor called from the main JetBoy activity
     * 
     * @param context 
     * @param attrs 
     */
    public SokobanView(Context context, AttributeSet attrs) {
        super(context, attrs);
        
        
        // permet d'ecouter les surfaceChanged, surfaceCreated, surfaceDestroyed        
    	holder = getHolder();
        holder.addCallback(this);    
        
        // chargement des images
        mContext	= context;
        mRes 		= mContext.getResources();

        blue_square 		= BitmapFactory.decodeResource(mRes, R.drawable.blue);
        red_square		= BitmapFactory.decodeResource(mRes, R.drawable.red);
        yellow_square		= BitmapFactory.decodeResource(mRes, R.drawable.yellow);
        purple_square	= BitmapFactory.decodeResource(mRes, R.drawable.purple);
        green_square	= BitmapFactory.decodeResource(mRes, R.drawable.green);
        pink_square	= BitmapFactory.decodeResource(mRes, R.drawable.pink);


    	blue_button = BitmapFactory.decodeResource(mRes, R.drawable.big_blue);
        red_button 		= BitmapFactory.decodeResource(mRes, R.drawable.big_red);
        yellow_button 		= BitmapFactory.decodeResource(mRes, R.drawable.big_yellow);
        purple_button 		= BitmapFactory.decodeResource(mRes, R.drawable.big_purple);
        green_button 		= BitmapFactory.decodeResource(mRes, R.drawable.big_green);
        pink_button 		= BitmapFactory.decodeResource(mRes, R.drawable.big_pink);

        win = BitmapFactory.decodeResource(mRes, R.drawable.win);
        lose = BitmapFactory.decodeResource(mRes, R.drawable.lose);
    	
    	// initialisation des parmametres du jeu
    	initparameters();

    	// creation du thread
        cv_thread   = new Thread(this);
        // prise de focus pour gestion des touches
        setFocusable(true);
    }    

    // chargement du niveau a partir du tableau de reference du niveau
    private void loadlevel() {
    	for (int i=0; i< carteHeight; i++) {
            for (int j=0; j< carteWidth; j++) {
                carte[j][i]= ref[j][i];
            }
        }	
    }

    // initialisation du jeu
    public void initparameters() {
    	paint = new Paint();
    	paint.setColor(0xff0000);
    	
    	paint.setDither(true);
    	paint.setColor(0xFFFFFF00);
    	paint.setStyle(Paint.Style.STROKE);
    	paint.setStrokeJoin(Paint.Join.ROUND);
    	paint.setStrokeCap(Paint.Cap.ROUND);
    	paint.setStrokeWidth(3);    	
    	paint.setTextAlign(Paint.Align.LEFT);

        if(Jouer.niveau == 1) {
            carte = new int[carteHeight][carteWidth];
            ref = new int[carteHeight][carteWidth];
            createRandomMap();
            variableNiveauTest = 1;
            nombreCouleur = 3;
            nombreCoups = niveauUnCoups;
            nombreCoupsMoinsUn = nombreCoups;
            loadlevel();
            // met le compteur de temps a 0 apres avoir appuyer sur nouvelle partie niveau facile
            p8_Sokoban.tempsNiveauZero();
        }

        if(Jouer.niveau == 2){
            carte = new int[carteHeight][carteWidth];
            ref = new int[carteHeight][carteWidth];
            createRandomMap();
            variableNiveauTest = 1;
            nombreCouleur = 4;
            nombreCoups = niveauDeuxCoups;
            nombreCoupsMoinsUn = nombreCoups;
            loadlevel();
            // met le compteur de temps a 0 apres avoir appuyer sur nouvelle partie niveau moyen
            p8_Sokoban.tempsNiveauZero();
        }

        if(Jouer.niveau == 3){

            carte = new int[carteHeight][carteWidth];
            ref = new int[carteHeight][carteWidth];
            createRandomMap();
            variableNiveauTest = 1;
            nombreCouleur = 6;
            nombreCoups = niveauTroisCoups;
            nombreCoupsMoinsUn = nombreCoups;
            loadlevel();
            // met le compteur de temps a 0 apres avoir appuyer sur nouvelle partie niveau difficile
            p8_Sokoban.tempsNiveauZero();
        }

        if(Jouer.niveau == 4) {

            carte = new int[carteHeight][carteWidth];

            // si pas de sauvegarde on genere une grille aleatoir avec comme difficulter facile
            // sinon on charge la grille sauvegarder
            if( variableNiveau != 1) {
                nombreCoups = niveauUnCoups;
                nombreCoupsMoinsUn = nombreCoups;
                nombreCouleur = 3;
                createRandomMap();
                // met le compteur de temps a 0 apres avoir appuyer sur reprendre la partie
                // si aucune partie est sauvegarder
                p8_Sokoban.tempsNiveauZero();
            }
            color_button = ref[0][0];
            loadlevel();
        }

        carteTopAnchor  = (getHeight()- carteHeight*carteTileSize)/5;
        carteLeftAnchor = (getWidth()- carteWidth*carteTileSize)/2;

        if ((cv_thread!=null) && (!cv_thread.isAlive())) {        	
        	cv_thread.start();
        	Log.e("-FCT-", "cv_thread.start()");
        }
    }    


// positionnement des boutons
    private void paintarrow(Canvas canvas) {
    	canvas.drawBitmap(blue_button, 0, (getHeight()-blue_button.getHeight()) - blue_button.getHeight(), null);
    	canvas.drawBitmap(red_button, (getWidth()-red_button.getWidth())/3, (getHeight()-red_button.getHeight()) - red_button.getHeight(), null);
    	canvas.drawBitmap(yellow_button, (getWidth()-purple_button.getWidth()*1) - ((getWidth()-red_button.getWidth())/3), (getHeight()-yellow_button.getHeight()) - yellow_button.getHeight(), null);
    	canvas.drawBitmap(purple_button, getWidth()-purple_button.getWidth()*1, (getHeight()-blue_button.getHeight()) - purple_button.getHeight(), null);
        canvas.drawBitmap(green_button, ((getWidth()-red_button.getWidth())/3)/2, (getHeight()-blue_button.getHeight()) - green_button.getHeight(), null);
        canvas.drawBitmap(pink_button, (getWidth()-purple_button.getWidth()*2)-(((getWidth()-red_button.getWidth())/3)/2) + (blue_button.getHeight()), (getHeight()-blue_button.getHeight()) - pink_button.getHeight(), null);
    }

    // dessin du gagne si gagne
    private void paintwin(Canvas canvas) {
            canvas.drawBitmap(win, carteLeftAnchor+ 1*carteTileSize, carteTopAnchor+ 10*carteTileSize, null);
            ifWin = true;
    }

    // dessin du perdu si perdu
    private void paintlose(Canvas canvas) {
        canvas.drawBitmap(lose, carteLeftAnchor+ 1*carteTileSize, carteTopAnchor+ 10*carteTileSize, null);
        ifLose = true;
    }
    
    // dessin de la carte du jeu
    private void paintcarte(Canvas canvas) {
    	for (int i=0; i< carteHeight; i++) {
            for (int j=0; j< carteWidth; j++) {
                switch (carte[i][j]) {
                    case Blue:
                        canvas.drawBitmap(blue_square, carteLeftAnchor+ j*carteTileSize, carteTopAnchor+ i*carteTileSize, null);
                        break;
                    case Red:
                        canvas.drawBitmap(red_square, carteLeftAnchor+ j*carteTileSize, carteTopAnchor+ i*carteTileSize, null);
                        break;
                    case Yellow:
                        canvas.drawBitmap(yellow_square, carteLeftAnchor+ j*carteTileSize, carteTopAnchor+ i*carteTileSize, null);
                        break;
                    case Purple:
                        canvas.drawBitmap(purple_square, carteLeftAnchor+ j*carteTileSize, carteTopAnchor+ i*carteTileSize, null);
                        break;
                    case Green:
                        canvas.drawBitmap(green_square, carteLeftAnchor+ j*carteTileSize, carteTopAnchor+ i*carteTileSize, null);
                        break;
                    case Pink:
                        canvas.drawBitmap(pink_square, carteLeftAnchor+ j*carteTileSize, carteTopAnchor+ i*carteTileSize, null);
                        break;

                }
            }
        }
    }

    // permet d'identifier si la partie est gagnee (toutes les cases de la meme couleur)
    private boolean isWon() {

        int i,j;

        for (i=0; i<carteHeight; i++) {
            for (j = 0; j < carteWidth; j++) {

                if (color_button_0 != ref[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    // fonction qui regarde si on a perdu ou non
    private boolean isLose(){

            if(nombreCoups <= 0) {
                return true;
            }
        return false;
    }

    
    //dessin du jeu (fond uni, en fonction du jeu gagne ou pas dessin du plateau et du joueur des diamants et des fleches)
    private void nDraw(Canvas canvas) {

		canvas.drawRGB(44,44,44);

    	if (isWon()) {
        	paintcarte(canvas);
        	paintwin(canvas);
            createRandomMap();
            loadlevel();
        }
        else if((isLose() == true) && (isWon() != true)) {
            paintcarte(canvas);
            paintlose(canvas);
            createRandomMap();
            loadlevel();
        }
        else if((isLose() != true) && (isWon() != true)){
                loadlevel();
                paintcarte(canvas);
                paintarrow(canvas);
                changementCouleur(color_button);
        }
    }

    // callback sur le cycle de vie de la surfaceview
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    	Log.i("-> FCT <-", "surfaceChanged "+ width +" - "+ height);
    	initparameters();
    }

    public void surfaceCreated(SurfaceHolder arg0) {
    	Log.i("-> FCT <-", "surfaceCreated");    	        
    }

    
    public void surfaceDestroyed(SurfaceHolder arg0) {
    	Log.i("-> FCT <-", "surfaceDestroyed");    	        
    }    


    public void joinThread() {

        try {
            cv_thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    /**
     * run (run du thread cree)
     * on endort le thread, on modifie le compteur d'animation, on prend la main pour dessiner et on dessine puis on lib�re le canvas
     */
    public void run() {
    	Canvas c = null;
        while (in) {

            try {
                if(ifWin == false) {
                    cv_thread.sleep(40);
                    currentStepZone = (currentStepZone + 1) % maxStepZone;
                }
                if(ifWin == true) {
                    cv_thread.sleep(2000);
                    ifWin = false;
                    if(Jouer.niveau == 1){
                        nombreCoups = nombreCoups + niveauUnCoups;
                    }
                    if(Jouer.niveau == 2){
                        nombreCoups = nombreCoups + niveauDeuxCoups;
                    }
                    if(Jouer.niveau == 3){
                        nombreCoups = nombreCoups + niveauTroisCoups;
                    }
                    if(Jouer.niveau == 4){
                        nombreCoups = nombreCoups + nombreCoupsMoinsUn;
                    }

                    // met le compteur de temps a 0 apres avoir lancer un nouveau niveau
                    p8_Sokoban.tempsNiveauZero();
                }

                    if(ifLose == false) {
                        cv_thread.sleep(40);
                        currentStepZone = (currentStepZone + 1) % maxStepZone;
                    }
                    if(ifLose == true){
                        cv_thread.sleep(2000);
                        ifLose = false;
                        if(Jouer.niveau == 1){
                            nombreCoups = niveauUnCoups;
                        }
                        if(Jouer.niveau == 2){
                            nombreCoups = niveauDeuxCoups;
                        }
                        if(Jouer.niveau == 3){
                            nombreCoups = niveauTroisCoups;
                        }
                        if(Jouer.niveau == 4){
                            nombreCoups = nombreCoupsMoinsUn;
                        }

                        // met le compteur de temps a 0 apres avoir lancer un nouveau niveau
                        p8_Sokoban.tempsNiveauZero();

                    }

                try {
                    c = holder.lockCanvas(null);
                    nDraw(c);
                } finally {
                	if (c != null) {
                		holder.unlockCanvasAndPost(c);
                    }
                    else {
                        joinThread();
                    }
                }
            } catch(Exception e) {
            	Log.e("-> RUN <-", "PB DANS RUN");
            }
        }


    }
    

    // fonction permettant de recuperer les evenements tactiles
    public boolean onTouchEvent (MotionEvent event) {

        int action = event.getAction();
        int x = (int) event.getX();
        int y = (int) event.getY();

        // coordonnees x et y de chaques boutons
        int x_blue = 0;
        int y_blue = (getHeight()-blue_button.getHeight()) - blue_button.getHeight();

        int x_red = (getWidth()-red_button.getWidth())/3;
        int y_red = (getHeight()-red_button.getHeight()) - red_button.getHeight();

        int x_yellow = (getWidth()-purple_button.getWidth()*1) - ((getWidth()-red_button.getWidth())/3);
        int y_yellow = (getHeight()-yellow_button.getHeight()) - yellow_button.getHeight();

        int x_purple = getWidth()-purple_button.getWidth()*1;
        int y_purple = (getHeight()-blue_button.getHeight()) - purple_button.getHeight();

        int x_green = ((getWidth()-red_button.getWidth())/3)/2;
        int y_green = (getHeight()-blue_button.getHeight()) - green_button.getHeight();

        int x_pink = (getWidth()-purple_button.getWidth()*2)-(((getWidth()-red_button.getWidth())/3)/2) + (blue_button.getHeight());
        int y_pink = (getHeight()-blue_button.getHeight()) - pink_button.getHeight();



        // permet de jouer avec la couleur du bouton que on a toucher
        switch(action){
            case MotionEvent.ACTION_DOWN:


                if (x >= x_blue && x < (x_blue + blue_button.getWidth())
                        && y >= y_blue && y < (y_blue + blue_button.getHeight()) && (nombreCouleur >= 3)) {
                    Log.i("bingo", "BLUE");
                    color_button = 0;
                    color_button_0 = 0;
                }

                if (x >= x_green && x < (x_green + green_button.getWidth() )
                        && y >= y_green && y < (y_green + green_button.getHeight()) && (nombreCouleur >= 3)) {
                    Log.i("bingo", "GREEN");
                    color_button = 1;
                    color_button_0 = 1;
                }

                if (x >= x_red && x < (x_red + red_button.getWidth())
                        && y >= y_red && y < (y_red + red_button.getHeight()) && (nombreCouleur >= 3)) {
                    Log.i("bingo", "RED");
                    color_button = 2;
                    color_button_0 = 2;
                }

                if (x >= x_purple && x < (x_purple + purple_button.getWidth())
                        && y >= y_purple && y < (y_purple + purple_button.getHeight()) && (nombreCouleur >= 4)) {
                    Log.i("bingo", "PURPLE");
                    color_button = 3;
                    color_button_0 = 3;
                }

                if (x >= x_pink && x < (x_pink + pink_button.getWidth())
                        && y >= y_pink && y < (y_pink + pink_button.getHeight()) && (nombreCouleur >= 5)) {
                    Log.i("bingo", "PINK");
                    color_button = 4;
                    color_button_0 = 4;
                }

                if (x >= x_yellow && x < (x_yellow + yellow_button.getWidth())
                        && y >= y_yellow && y < (y_yellow + yellow_button.getHeight()) && (nombreCouleur >= 6)) {
                    Log.i("bingo", "YELLOW");
                    color_button = 5;
                    color_button_0 = 5;
                }
                break;
        }
    	return super.onTouchEvent(event);
    }
}
