/*=================================================================*/
/*= E.Incerti - eric.incerti@univ-eiffel.fr                       =*/
/*= Université Gustave Eiffel                                     =*/
/*= Code d'illustration pour prototypage avec libg2x.6c           =*/
/*=================================================================*/

#include <g2x.h>

/* tailles de la fenêtre (en pixel) */
static int WWIDTH=512, WHEIGHT=512;
/* limites de la zone reelle associee a la fenetre */
static double   wxmin=-10.,wymin=-10.,wxmax=+10.,wymax=+10.;

G2Xpoint  A;
G2Xvector u;
double    r;
bool      FILLDISK;

/* la fonction d'initialisation */
static void init(void)
{
  /* zone graphique reelle associee a la fenetre                 */
  /* si cette fonction n'est pas appelée, les valeurs par défaut */
  /* sont (-1.,-1)->(+1.,+1.)                                    */
  g2x_SetWindowCoord(wxmin,wymin,wxmax,wymax);

  /* les objets de la scène */
  A=(G2Xpoint ){-4.,+1.};
  u=(G2Xvector){0.005,0.003};
  r=1.;
  FILLDISK=false;
}

/* la fonction de contrôle : appelée 1 seule fois, juste après <init> */
static void ctrl(void)
{
  g2x_SetControlPoint(&A,10); // le centre du cercle est un point de contrôle
  g2x_CreateScrollv_d("ray",&r,0.5,5.,.5,"rayon de la balle"); // un scrollbar pour le rayon
  g2x_CreateSwitch("F.",&FILLDISK,"Disque plein ou pas"); // un 'switch' pour le remplissage
}

/* la fonction de dessin */
static void draw()
{ /*! tout ce qu'il y a à dessiner !*/
  if (FILLDISK) g2x_FillCircle(A.x,A.y,r,G2Xc);
  g2x_Circle(A.x,A.y,r,G2Xr,2);
}

/* la fonction d'animation (facultatif) */
static void anim()
{ /*! si il y a des choses à faire bouger.... !*/
  A.x+=u.x; if (A.x-r<wxmin || A.x+r>wxmax) u.x=-u.x;
  A.y+=u.y; if (A.y-r<wymin || A.y+r>wymax) u.y=-u.y;
}

/* la fonction de sortie  (facultatif) */
static void quit()
{
  fprintf(stderr,"Bye !\n\n");
}

/***************************************************************************/
/* La fonction principale : NE CHANGE JAMAIS                               */
/***************************************************************************/
int main(int argc, char **argv)
{
  /* creation de la fenetre - titre et tailles (pixels) */
  g2x_InitWindow(*argv,WWIDTH,WHEIGHT);
  g2x_SetInitFunction(init); /* fonction d'initialisation */
  g2x_SetCtrlFunction(ctrl); /* fonction de contrôle      */
  g2x_SetDrawFunction(draw); /* fonction de dessin        */
  g2x_SetAnimFunction(anim); /* fonction d'animation      */
  g2x_SetExitFunction(quit); /* fonction de sortie        */
  /* lancement de la boucle principale */
  return g2x_MainStart();
  /* RIEN APRES CA */
}
