/* ===============================================================
  E.Incerti - Université Gustave Eiffel - eric.incerti@univ-eiffel.fr
  ============================================================= */

/**
 * Exemple destiné à illustrer l'utlisation de la lib g3x
 * - les principaux types utiles
 * - les principales fonctionalités
 * => le reste, c'est de l'OpenGl
 **/

#include <g3x.h>

/* DES TAS DE VARIABLES GLOBALES !!!       */
/* des couleurs prédéfinies <g3x_colors.h> */
static G3Xcolor rouge  ={1.0,0.2,0.2,0.0};
static G3Xcolor jaune  ={1.0,1.0,0.2,0.0};
static G3Xcolor vert   ={0.2,1.0,0.2,0.0};
static G3Xcolor cyan   ={0.2,1.0,1.0,0.0};
static G3Xcolor bleu   ={0.3,0.3,1.0,0.0};
static G3Xcolor magenta={1.0,0.2,1.0,0.0};

/* flag d'affichage/masquage utilisées par <void ctrl(void);> */
/* le type 'bool' est défini dans <g3x_types.h>               */
static bool FLAG_TEAPOT=true;
static bool FLAG_TORUS =true;
static bool FLAG_CONE  =true;
static bool FLAG_ICOS  =true;

/* un flag attaché à une action */
static bool pivot = false;

/* paramètres géométriques */
/* le type 'G3Xvector' est défini dans <g3x_geom.h> */
static G3Xvector Rot={1.,2.,3.};
static double    angle= 0.00;
static double    rayon= 0.66;
static double    b=0.1;

/* paramètres de 'matière' des objets */
static double alpha = 0.5;
static double ambi  = 0.2;
static double diff  = 0.3;
static double spec  = 0.4;
static double shin  = 0.5;

/* une table de couleurs HSV */
#define MAXCOL 24
static G3Xcolor colmap[MAXCOL];


/*! FONCTION D'INITIALISATION
 *  contient la partie "modélisation"
 *  appelée une seule fois, au début
 * TOUTES LES ALLOCS. DYN. DOIVENT ETRE LA   !*/
void init(void)
{
  /* caméra  <g3x_camlight.h>                                */
  /* param. géométrique de la caméra. cf. gluLookAt(...)     */
  /* FACULTATIF => c'est les valeurs par défaut              */
  g3x_SetPerspective(40.,100.,1.);
  /* position, orientation de la caméra en coord. sphériques */
  /* FACULTATIF => c'est les valeurs par défaut              */
  g3x_SetCameraSpheric(0.25*PI,+0.25*PI,6.,(G3Xpoint){0.,0.,0.});

  /* lumière <g2x_camlight.h>                                */
  /* fixe les param. colorimétriques du spot lumineux        */
  /* lumiere blanche (c'est les valeurs par defaut)          */
  /* FACULTATIF => c'est les valeurs par défaut              */
  g3x_SetLightAmbient (1.,1.,1.);
  g3x_SetLightDiffuse (1.,1.,1.);
  g3x_SetLightSpecular(1.,1.,1.);
  /* fixe la position                                        */
  g3x_SetLightPosition (10.,10.,10.);

  /* initialisation d'une carte de <MAXCOL> couleurs HSV */
  /* [0:rouge | 1/3:vert | 2/3:bleu | 1:rouge]           */
  /* cf. <g2x_colors.h> */
  g3x_ColorMapHSV360(colmap,MAXCOL);
}

/*! FONCTION DE DESSIN PRINCIPALE (c'est le "moteur de rendu")
 *  LA SEULE FONCTION INDISPENSABLE
 *  En 3D, c'est là (et uniquement là) qu'on fait de l'OpenGl
 *  ATTENTION : cette fonction est appelée en duo
 *  avec la fonction d'animation <void anim(void)> dans une
 *  boucle infinie =>surtout pas d'allocation de mémoire ici
 *  Elle ne fait QUE des opérations géom. (transformation)
 *  et TOUT l'affichage : aucun calcul ici                 */
static void draw(void)
{
  glEnable(GL_BLEND); /* active le mode 'transparence' (blending) */
  glBlendFunc(GL_SRC_ALPHA,GL_ONE_MINUS_SRC_ALPHA); /* cf. manuel OpenGl */
    /* paramètres de couleur/matière de l'objet <g2x_colors.h> */
    /* alternative à la méthode OpenGL, peu intuitive [cf. ./demo_scene.c] */
    g3x_Material(rouge,ambi,diff,spec,shin,1.);
    glutSolidSphere(rayon,40,40); /* une sphere (cf. manuel OpenGl) */
    /* un cube transparent */
    /*--------------LIMITES DE LA TRANSPARENCE Gl ---------*/
    /* -> TESTER :  DEFINIR LE CUBE TRANSP. AVANT LA SPHERE */
    /* -> TESTER :  RENDRE LA SPHERE TRANSPARENTE           */
    g3x_Material(vert,ambi,diff,spec,shin,alpha);
    glutSolidCube(1.); /* un cube (cf. manuel OpenGl) */
  glDisable(GL_BLEND); /* désactive le mode blending */

  if (FLAG_TEAPOT)  /* affichage lié à un bouton 'switch' - cf plus bas : fct. void ctrl(void) */
  {
    glPushMatrix(); /* 'ouverture' de la pile de transfo. OpenGl */
      if (pivot) glRotatef(180,0.,1.,0.);
      glTranslatef(0.,0.,1.);    /* une translation */
      glRotatef(angle,0.,0.,1.); /* une rotation autour de l'axe 'z' */
      glRotatef(90,1.,0.,0.);    /* une rotation autour de l'axe 'x' */
      g3x_Material(bleu,ambi,diff,spec,shin,0.3);
      glDisable(GL_CULL_FACE);
      glutSolidTeapot(.5);       /* la teapot OpenGl                 */
      glEnable(GL_CULL_FACE);
    glPopMatrix();
  }
  if (FLAG_TORUS)
  {
    glPushMatrix();
      if (pivot) glRotatef(180,0.,1.,0.);
      glTranslatef(0.,0.,-1.);
      glRotatef(-angle,0.,0.,1.);
      glRotatef(45.,1.,0.,0.);
      glScalef(0.5,0.5,0.5);     /* une homothétie                   */
      g3x_Material(jaune,ambi,diff,spec,shin,1.);
      glutSolidTorus(.2,1.,20,40); /* un tore (cf. manuel OpenGl)    */
    glPopMatrix();
  }
  if (FLAG_CONE)
  {
    glPushMatrix();
      glRotatef(angle,0.,0.,1.);
      glTranslatef(-2.,0.,0.);
      glScalef(0.5,0.5,0.5);
      g3x_Material(cyan,ambi,diff,spec,shin,1.);
      glutSolidCone(1.,1.,20,20); /* un cône (cf. manuel OpenGl)     */
    glPopMatrix();
  }
  if (FLAG_ICOS)
  {
    glPushMatrix();
      glTranslatef(2.,0.,0.);
      glRotatef(angle,0.,0.,1.);
      glScalef(0.5,0.5,0.5);
      g3x_Material(magenta,ambi,diff,spec,shin,1.);
      glutSolidIcosahedron(); /* un icosahèdre [20 faces] (cf. manuel OpenGl) */
    glPopMatrix();
  }

  /* les cubes animes -> cf plus bas, fct. void anim(void) */
  int i=0;
  double a=360./MAXCOL;
  glPushMatrix();
    glRotatef(-2.*b,0.,0.,Rot.z);
    while (i<MAXCOL)
    {
      g3x_Material(colmap[i],ambi,diff,spec,shin,1.);
      glRotatef(a,0.,0.,1.);
      glPushMatrix();
        glTranslatef(1.,0.,0.);
        glRotatef(i*a,1.,0.,0.);
        glRotatef(20.*b,Rot.x,Rot.y,Rot.z);
        glutSolidCube(1.25*PI/MAXCOL);
      glPopMatrix();
      i++;
    }
  glPopMatrix();

}

/*! FONCTION D'ANIMATION (c'est le "moteur de jeu")
 *  ATTENTION : cette fonction est appelée en duo
 *  avec la fonction de desssin <void anim(void)> dans une
 *  boucle infinie =>surtout pas d'allocation de mémoire ici
 *  Elle ne fait QUE du calcul : aucun affichage ici
 *  Elle est bien sûr facultative, si rien ne bouge !*/
void anim(void)
{
  static double pas=0.01; /* vitesse de rotation */
  b+=pas;
  Rot.x+=pas;
  Rot.y-=pas;
  Rot.z+=pas;
  if (Rot.x<-60. || Rot.x>+60.) pas=-pas;

}

/*!    ACTION A EXECUTER EN SORTIE   !*/
/*! libération de mémoire, nettoyage !*/
/*! -> utilise la pile de <atexit()> !*/
static void quit(void)
{
  /* rien à faire ici puisqu'il n'y a pas d'allocation dynamique */
  /* et rien de spécial à faire en sortie de programme           */
  fprintf(stdout,"bye !\n");
}

/*! DES ACTIONS ASSOCIEES A DES OPTIONS DE CONTROLE */
/*! action : variation de couleur                  !*/
static void action_g(void)
{
  rouge.r-=0.01;  rouge.g+=0.01;
  vert.g -=0.01;  vert.b +=0.01;
  bleu.b -=0.01;  bleu.r +=0.01;
}

/*! action : la même chose dans l'autre sens       !*/
static void action_G(void)
{
  rouge.r+=0.01;  rouge.g-=0.01;
  vert.g +=0.01;  vert.b -=0.01;
  bleu.b +=0.01;  bleu.r -=0.01;
}

void action_popup(void)
{
  pivot = !pivot;
}


/*! action : affiche les parametre camera dans le terminal !*/
static void camera_info(void)
{
  G3Xcamera* cam=g3x_GetCamera();
  fprintf(stderr,"position (x:%lf,y:%lf,z:%lf)\n",cam->pos->x,cam->pos->y,cam->pos->z);
  fprintf(stderr,"cible    (x:%lf,y:%lf,z:%lf)\n",cam->tar->x,cam->tar->y,cam->tar->z);
  fprintf(stderr,"coord. spheriques (d:%lf,theta:%lf,phi:%lf)\n",cam->dist,cam->theta,cam->phi);
}

/*! FONCTION DE CONTROLE  : les boutons, scrollbars, actions .... !*/
/*! exécutée une seule fois, juste après la fonction init()       !*/
/*! types & fonctions définies dans <g2x_ctrls.h>                 !*/
static void ctrl(void)
{
  /* des boutons 'switch' associés à une variable booléene (on/off) */
  g3x_CreateSwitch("teapot",&FLAG_TEAPOT,"affiche/masques la theiere  ");
  g3x_CreateSwitch("torus ",&FLAG_TORUS ,"affiche/masques le tore     ");
  g3x_CreateSwitch("cone  ",&FLAG_CONE  ,"affiche/masques le cone     ");
  g3x_CreateSwitch("icos  ",&FLAG_ICOS  ,"affiche/masques l'icosahedre");

  /* définition des scrollbars */
  g3x_SetScrollWidth(6);           /* pour régler la largeur (valeur par défaut 4) */

  int id;
  /* des scrollbars vericaux       */
  id=g3x_CreateScrollv_d("ray",&rayon,0.1,  2.0,1,"rayon sphere     ");
  g3x_SetScrollColor(id,G3Xrb_c);  /* pour changer la "couleur" du scrollbar       */
  id=g3x_CreateScrollv_d("ang",&angle,0.0,360.0,1,"angle rotation   ");
  g3x_SetScrollColor(id,G3Xgb_c);
  id=g3x_CreateScrollv_d("alf",&alpha,0.0,  1.0,1,"transparence cube");
  g3x_SetScrollColor(id,G3Xbb_c);

  g3x_SetScrollWidth(4);
  /* et des scrollbars horizontaux */
  id=g3x_CreateScrollh_d("shin",&shin,0.0,1.0,1,"intensite lumiere brillance ");
  g3x_SetScrollColor(id,G3Xrc_c);
  id=g3x_CreateScrollh_d("spec",&spec,0.0,1.0,1,"intensite lumiere speculaire");
  g3x_SetScrollColor(id,G3Xgc_c);
  id=g3x_CreateScrollh_d("diff",&diff,0.0,1.0,1,"intensite lumiere diffuse   ");
  g3x_SetScrollColor(id,G3Xbc_c);
  id=g3x_CreateScrollh_d("ambi",&ambi,0.0,1.0,1,"intensite lumiere ambiante  ");

  /* action attachées à des touches clavier */
  g3x_SetKeyAction('g',action_g,"variation de couleur (sphere/teapot)");
  g3x_SetKeyAction('G',action_G,"variation de couleur (sphere/teapot)");
  g3x_SetKeyAction('c',camera_info,"pos./dir. de la camera => terminal");

  g3x_CreatePopUp("toggle",action_popup,"echange teapot/tore");
}

/*! la fonction principale : change presque jamais !*/
int main(int argc, char** argv)
{
  /* initialisation de la fenêtre graphique et paramétrage Gl <g3x_window.h> */
  g3x_InitWindow(*argv,768,512);

  /* définition des fonctions d'exécution <g3x_window.h> */
  g3x_SetInitFunction(init); /* la fonction d'initialisation */
  g3x_SetCtrlFunction(ctrl); /* la fonction de contrôle      */
  g3x_SetDrawFunction(draw); /* la fonction de dessin        */
  g3x_SetExitFunction(quit); /* la fonction de sortie        */
  g3x_SetAnimFunction(anim); /* la fonction d'animation      */

  /* boucle d'exécution principale <g3x_window.h> */
  return g3x_MainStart();
  /* rien après ça !!! */
}
