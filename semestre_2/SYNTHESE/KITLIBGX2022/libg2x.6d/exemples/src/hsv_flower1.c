/*=================================================================*/
/*= E.Incerti - eric.incerti@univ-eiffel.fr                       =*/
/*= Université Gustave Eiffel                                     =*/
/*= Code "squelette" pour prototypage avec libg2x.6c              =*/
/*=================================================================*/

#include <g2x.h>

/* tailles de la fenêtre (en pixel) */
static int WWIDTH=512, WHEIGHT=512;
/* limites de la zone reelle associee a la fenetre */
static double wxmin=-10.,wymin=-10.,wxmax=+10.,wymax=+10.;

/* 2 variables globales (valeurs réglables) */
static int    n=12,p=4;

/* la fonction d'initialisation : appelée 1 seule fois, au début */
static void init(void)
{
  /* zone graphique reelle associee a la fenetre                 */
  g2x_SetWindowCoord(wxmin,wymin,wxmax,wymax);
  /* 2 scrollbar verticaux pour les variables gloables           */
  g2x_CreateScrollv_i("n",&n,6,360,1.,"");
  g2x_CreateScrollh_i("p",&p,1,6,1.,"");
}

/* la fonction de dessin : appelée en boucle */
static void draw(void)
{
  int    i;
  double pas=1./n,r=24.*pas,R=8.;
  double s,xc,yc;
  G2Xcolor col;

  for (s=1.;s>0.;s-=1./(1<<p))
    for (i=0;i<n;i++)
    {
      xc = R*s*g2x_Idxcos((i*720)/n);
      yc = R*s*g2x_Idxsin((i*720)/n);
      col= g2x_hsva_rgba_4f(i*pas,s,1.,0.);
      g2x_FillCircle(xc,yc,r*s,col);
    }
}

/***************************************************************************/
/* La fonction principale : NE CHANGE JAMAIS                               */
/***************************************************************************/
int main(int argc, char **argv)
{
  /* creation de la fenetre - titre et tailles (pixels) */
  g2x_InitWindow(*argv,WWIDTH,WHEIGHT);
  g2x_SetInitFunction(init); /* fonction d'initialisation */
  g2x_SetDrawFunction(draw); /* fonction de dessin        */
  /* lancement de la boucle principale */
  return g2x_MainStart();
  /* RIEN APRES CA */
}
