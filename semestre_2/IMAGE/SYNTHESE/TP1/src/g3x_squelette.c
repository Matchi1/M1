/*=================================================================*/
/*= E.Incerti - eric.incerti@univ-eiffel.fr                       =*/
/*= Université Gustave Eiffel                                     =*/
/*= Code "squelette" pour prototypage avec libg3x.6c              =*/
/*=================================================================*/

#include <g3x.h>

#define NBM 720
#define NBP 720

/* tailles de la fenêtre (en pixel) */
static int WWIDTH=1080, WHEIGHT=1080;
static G3Xpoint P[NBM][NBP];

/* la fonction d'initialisation : appelée 1 seule fois, au début */
static void init(void)
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
}

/* la fonction de contrôle : appelée 1 seule fois, juste après <init> */
static void ctrl(void)
{
}

/* la fonction de dessin : appelée en boucle */
static void draw(void)
{
	glBegin(GL_QUADS);
	for(int p=0; p < NBP - 1; p++)
	{
		for(int m=0; m < NBM - 1; m++)
		{
			g3x_Vertex3dv(P[p][m]);
			g3x_Vertex3dv(P[p][m+1]);
			g3x_Vertex3dv(P[p+1][m+1]);
			g3x_Vertex3dv(P[p+1][m]);
		}
	}
	glPointSize(4);
	glBegin(GL_POINTS);
	for(int i = 0; i < NBP - 1; i++)
	{
		g3x_Normal3dv(P[i][i]);
		g3x_Vertex3dv(P[i][i]);
	}
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
  g3x_SetAnimFunction(anim); /* fonction d'animation      */
  g3x_SetExitFunction(quit); /* fonction de sortie        */

  /* lancement de la boucle principale */
  return g3x_MainStart();
  /* RIEN APRES CA */
}
