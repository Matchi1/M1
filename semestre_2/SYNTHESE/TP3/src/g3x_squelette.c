/*=================================================================*/
/*= E.Incerti - eric.incerti@univ-eiffel.fr                       =*/
/*= Université Gustave Eiffel                                     =*/
/*= Code "squelette" pour prototypage avec libg3x.6c              =*/
/*=================================================================*/

#include <g3x.h>

#include "../include/cube.h"
#include "../include/shape.h"

#define NBM 720
#define NBP 720

#define CXMAX 10
#define CYMAX 10

#define MAXRES 720

/* tailles de la fenêtre (en pixel) */
static int WWIDTH=1080, WHEIGHT=1080;

Shape cube = {0};

/* la fonction d'initialisation : appelée 1 seule fois, au début */
static void init(void)
{
	cube_init(&cube, MAXRES);
}

/* la fonction de contrôle : appelée 1 seule fois, juste après <init> */
static void ctrl(void)
{
}

static void draw_cube(void)
{
  G3Xvector scale = {50, 50, 1};
  glDisable(GL_LIGHTING);    /* <BALISE.GL>  "débranche" la lumière, pour permettre le tracé en mode point/ligne */
	glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);

	glBegin(GL_POINTS);
    cube.draw_points(&cube, scale);
	glEnd();

    /*
	glBegin(GL_QUADS);
    cube.draw_faces(&cube, scale);
	glEnd();
    */

	glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
  glEnable(GL_LIGHTING);
}

/* la fonction de dessin : appelée en boucle */
static void draw(void)
{
	draw_cube();
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
