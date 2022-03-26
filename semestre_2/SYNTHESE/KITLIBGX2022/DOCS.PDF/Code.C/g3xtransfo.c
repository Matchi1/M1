/*=================================================================*/
/*= E.Incerti - eric.incerti@univ-eiffel.fr                       =*/
/*= Université Gustave Eiffel                                     =*/
/*= Code "g3xtransfo.c" les transformations de libg3x.6c          =*/
/*= à comparer à "gltransfo.c"                                    =*/
/*=================================================================*/

#include <g3x.h>

/* tailles de la fenêtre (en pixel) */
static int WWIDTH=512, WHEIGHT=512;

static G3Xhmat M;
static double tx,ty,tz,rx,ry,rz,hx,hy,hz;

/* la fonction d'initialisation : appelée 1 seule fois, au début */
static void init(void)
{
  tx= 0.; ty=0.; tz=0.;
  rx= 0.; ry=0.; rz=0.;
  hx= 1.; hy=1.; hz=1.;
}

/* la fonction de contrôle : appelée 1 seule fois, juste après <init> */
static void ctrl(void)
{
  g3x_CreateScrollv_d("tx",&tx,-2.,+2.,1,"");
  g3x_CreateScrollv_d("ty",&ty,-2.,+2.,1,"");
  g3x_CreateScrollv_d("tz",&tz,-2.,+2.,1,"");
  g3x_CreateScrollv_d("rx",&rx,-PI,+PI,1,"");
  g3x_CreateScrollv_d("ry",&ry,-PI,+PI,1,"");
  g3x_CreateScrollv_d("rz",&rz,-PI,+PI,1,"");
  g3x_CreateScrollh_d("hx",&hx, .1,+2.,1,"");
  g3x_CreateScrollh_d("hy",&hy, .1,+2.,1,"");
  g3x_CreateScrollh_d("hz",&hz, .1,+2.,1,"");
}

/* la fonction de dessin : appelée en boucle */
static void draw(void)
{
  /* mise à jour de la matrice de transformaiton globale*/
  M = g3x_Identity();
  M = g3x_Mat_x_Mat(g3x_Homothetie3d(hx,hy,hz) ,M); /* 1°) l’homothétie            */
  M = g3x_Mat_x_Mat(g3x_RotationX(rx)          ,M); /* 2°) la rotation autour de x */
  M = g3x_Mat_x_Mat(g3x_RotationY(ry)          ,M); /* 3°) la rotation autour de y */
  M = g3x_Mat_x_Mat(g3x_RotationZ(rz)          ,M); /* 4°) la rotation autour de z */
  M = g3x_Mat_x_Mat(g3x_Translation3d(tx,ty,tz),M); /* 5°) la translation          */
  /* paramètres couleur/matière indispensable */
  g3x_Material(G3Xwa,0.1,0.6,0.9,0.9999,0.);
  /* routine d'affichage */
  glPushMatrix();
    glMultMatrixd(M.m);
    glutSolidSphere(1.,40,40);
 glPopMatrix();
}

/* la fonction d'animation (facultatif) */
static void anim(void)
{
}

/* la fonction de sortie  (facultatif) -- atexit() */
static void quit(void)
{
}

/***************************************************************************/
/* La fonction principale : NE CHANGE JAMAIS ou presque                    */
/***************************************************************************/
int main(int argc, char **argv)
{
  /* creation de la fenetre - titre et tailles (pixels) */
  g3x_InitWindow(*argv,WWIDTH,WHEIGHT);

  g3x_SetInitFunction(init); /* fonction d'initialisation */
  g3x_SetCtrlFunction(ctrl); /* fonction de contrôle      */
  g3x_SetDrawFunction(draw); /* fonction de dessin        */
  g3x_SetAnimFunction(NULL); /* fonction d'animation      */
  g3x_SetExitFunction(NULL); /* fonction de sortie        */

  /* lancement de la boucle principale */
  return g3x_MainStart();
  /* RIEN APRES CA */
}
