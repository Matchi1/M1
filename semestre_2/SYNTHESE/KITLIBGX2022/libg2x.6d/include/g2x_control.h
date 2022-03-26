/*!===============================================================
  E.Incerti - Universite Gustave Eiffel - eric.incerti@univ-eiffel.fr
       - Librairie G2X - Fonctions de base d'acces public -
                    version 6.d - Jan.2022
  ================================================================!*/

#ifdef __cplusplus
   extern "C" {
#else
  #define _GNU_SOURCE
#endif

#ifndef _G2X_CONTOL_H
  #define _G2X_CONTOL_H

  #include <g2x.h>

  #define NAMESIZE  15
  #define INFOSIZE 127

  /* les points 'cliquables' à la souris*/
  typedef struct
  {
    G2Xpoint* add;  /* adresse du point   */
    int       ray;  /* rayon de selection */
  } G2Xctrlpt;

  /* les scrollbars, associés à un paramètre réel ou entier */
  /* liés à l'adresse d'une variable (double/int)           */
  typedef struct
  {
    double*  dprm;           /* var. <double>    */
    int*     iprm;           /* var. <int>       */
    double   cursor;         /* valeur du curseur*/
    double   min,max;        /* valeurs min/max  */
    double   fac;            /* facteur d'échelle*/
    int      xcurs,ycurs;    /* pos. du curseur  */
    int      w;              /* largeur du curs. */
    int      id;             /* num. du curseur  */
    char     name[7];        /* nom              */
    char     info[INFOSIZE]; /* info associée    */
    G2Xcolor bkg;            /* couleur de fond  */
  } G2Xscroll;

  /* les boutons on/off associés à un flag booléen */
  typedef struct
  {
    int  num; /* numero attribue au bouton      */
    int  x,y; /* coordonnees du bouton          */
    int  len; /* taille du nom                  */
    char name[NAMESIZE+1]; /* nom               */
    char info[INFOSIZE];   /* info associée     */
    bool on;               /* flag actif/inactif*/
  } G2Xbut;

  /* les boutons pop-up : lancent une action */
  typedef struct
  {
    int  num; /* numero attribue au bouton      */
    int  x,y; /* coordonnees du bouton          */
    int  len; /* taille du nom                  */
    char name[NAMESIZE+1]; /* nom               */
    char info[INFOSIZE];   /* info associée     */
    void (*action)(void);  /* action associée   */
    bool on;               /* flag actif/inactif*/
  } G2Xpopup;

  /* les switch (boutons exclusifs) :
   * associés à une variable globale booléene   */
  typedef struct
  {
    int   num; /* numero attribue au bouton      */
    int   x,y; /* coordonnees du bouton          */
    bool *flag;/* variable booléenne associée    */
    int   len; /* taille du nom                  */
    char  name[NAMESIZE+1]; /* nom               */
    char  info[INFOSIZE];   /* info associée     */
  } G2Xswitch;

  /* les touches clavier, associées à une action */
  typedef struct
  {
    char key;               /* la touche clavier */
    void (*action)();       /* action associée   */
    char info[INFOSIZE];    /* info associée     */
  }  G2Xkey;

  /*= parametres contrôlable - pavé fléché : entier ou reel  =*/
  typedef struct {double  r; int  i;} G2Xparam; /* les paramétres */
  typedef struct {double* r; int* i;} G2Xadres; /* leurs adresses */
  typedef struct
  {
    char     ope;            /* arithm.('+') ou geom.('*')                */
    char     nom[NAMESIZE+1];/* nom du parametre (pour menu)              */
    G2Xadres var;            /* adresse du parametre                      */
    G2Xparam pas,min,max;    /* pas et bornes pour parametre              */
    G2Xparam org,val,old;    /* valeurs originale, courante et precedente */
  } G2Xprm;


  /*!-----------------------------------------------!*/
  /*!             FONCTIONS DE CONTROLE             !*/
  /*!-----------------------------------------------!*/

  /*! gestion des points 'cliquables' (spécifique 2D)                           !*/
  /* définit un point comme 'point de contrôle', via son adresse                 */
  /* => P doit être défini en variable globale                                   */
  bool      g2x_SetControlPoint(G2Xpoint* P, int  ray);
  /* récupère l'adresse du point 'cliqué'                                        */
  G2Xpoint* g2x_GetControlPoint(void);
  /* renvoie <true> si un point est sélectionné                                  */
  bool      g2x_GetCpt();
  /* remet le point à son ancienne position                                      */
  void      g2x_ReplaceControlPoint(void);
  /* supprime un point de contrôle                                               */
  void      g2x_CleanControlPoint(G2Xpoint* pt);
  /* supprime tous les points de contrôle                                        */
  void      g2x_CleanAllControlPoint(void);

  /*! gestion des actions associees a une touche clavier                         !*/
  /* ATTENTION : les touches predefinies ne sont pas reattribuables               */
  /* predefinies : <ESP><ESC><q><Q><Ctrl+F><+><-><=><?><fleches>                  */
  bool g2x_SetKeyAction(char key, void (*action)(void), const char *info);

  /*! gestion des mouvements de la souris (sans click)                           !*/
  /* execute l'action en fonction de la position de la souris                     */
  void g2x_SetMouseMoveAction(void (*action)(G2Xpoint _CLIC_POSITION_));
  /* Retourne les coord. de la souris.                                            */
  G2Xpoint g2x_GetMousePosition(void);
  /* Retourne <true> si clic detecte.     */
  G2Xpoint* g2x_GetClic(void);

  /*! gestion des boutons poussoirs                                              !*/
  /* bouton renvoyant un simple numero                                            */
  /* a utiliser avec switch (g2x_GetButton()) { case 1 : ..... break; ... }       */
  int g2x_CreateButton(const char *text, const char *info);
  /* retourne le num. du boutton courant                                          */
  int g2x_GetButton(void);

  /*! bouttons 'ON/OFF' : bascule la variable flag false<->true                  !*/
  bool g2x_CreateSwitch(const char *txt, bool *flag, const char *info);
  /* retourne l'id du popup courant                                               */
  int  g2x_GetSwitch(void);

  /*! lance l'action associee, une seule fois (pop-up)                           !*/
  /* equivalent a l'attachement de l'action a une touche clavier (cf. en dessous) */
  bool g2x_CreatePopUp(const char *name, void (*action)(void), const char *info);
  int  g2x_GetPopUp(void);

  /*! gestion des scrollbars                                                     !*/
  /* cree un Scroll horizontal directement attache au parametre reel <param>
     nom :  nom du scroll affiche sur la fenetre
     prm :  pointeur sur la variable (TYPE DOUBLE) attachee au scroll
     min,max : bornes inferieurs et superieure de parcours du scroll
     mf  : mapping x\in[0-1] -> x^(mf) -- mf<1 : log / mf>1 : exp / mf=1 : lin.
     info :  facultatif (0/NULL/"") texte apparaissant dans la fenetre d'aide
     retour  : id (numero) du scroll, -1 en cas d'echec                           */
  int g2x_CreateScrollh_d(const char *nom, double* prm, double min, double max, double mf, const char *info);
  /* meme chose pour un parametre entier (TYPE INT)                               */
  int g2x_CreateScrollh_i(const char *nom, int*    prm, int    min, int    max, double mf, const char *info);
  /* cree un Scroll anonyme [0.,1.]  - initilise en position <init>               */
  int g2x_CreateAnonymousScrollv(const char *name, double init, const char *info);

  /* memes choses pour les scroll verticaux                                       */
  int g2x_CreateScrollv_d(const char *nom, double* prm, double min, double max, double mf, const char *info);
  int g2x_CreateScrollv_i(const char *nom, int*    prm, int    min, int    max, double mf, const char *info);
  int g2x_CreateAnonymousScrollv(const char *name, double init, const char *info);

  /* position courante du curseur dans l'intevalle [min,max] du scroll  n° <id>   */
  /* utile surtout avec les scroll anonymes                                       */
  double g2x_GetScrollCursor(int id);

  /* pour changer la couleur du scroll                                            */
  void   g2x_SetScrollColor(int id, G2Xcolor col);
  /* ajuste la largeur des scrolls (4<=w<=16) defaut:8                            */
  void   g2x_SetScrollWidth(int width) ;

  /*! Controle de parametre de type <int> et <double> avec le pave fleche        !*/
  /*  param : adresse du parametre (doit etre de type <int> ou <double>           */
  /*  nom   : identification du parametre qui apparait dans le menu deroulant     */
  /*  min,max : borne inf. et sup. de la zone de variation du parametre           */
  /*  mode : '+' pour mode arithmetique (*param += pas)                           */
  /*         '*' pour mode geometrique  (*param *= pas)                           */
  /*  pas  : pas d'avancement (exp. 0.01 en mode '+' , 1.01 en mode '*'           */
  /*  retour : <false> en cas d'echec d'enregistrement                            */
  bool g2x_SetControlParameter_i(int    *param, char* name, int    pas, int    min,    int max, char mode);
  bool g2x_SetControlParameter_d(double *param, char* name, double pas, double min, double max, char mode);
  /* teste si le parametre controlable <param> a ete modifie                       */
  bool g2x_ChangeControlParameter(void *param);


 #endif

#ifdef __cplusplus
  }
#endif
/*!=============================================================!*/
