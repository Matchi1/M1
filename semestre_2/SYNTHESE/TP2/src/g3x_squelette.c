/*=================================================================*/
/*= E.Incerti - eric.incerti@univ-eiffel.fr                       =*/
/*= Université Gustave Eiffel                                     =*/
/*= Code "squelette" pour prototypage avec libg3x.6c              =*/
/*=================================================================*/

#include <g3x.h>

#define NBM 720
#define NBP 720

#define CXMAX 10
#define CYMAX 10

/* tailles de la fenêtre (en pixel) */
static int WWIDTH=1080, WHEIGHT=1080;
static G3Xpoint Ps[NBM][NBP];
static G3Xvector Vs[NBM][NBP];

static double Cx = CXMAX/2., Cy = CYMAX/2.;
static G3Xpoint Pc[NBM][NBP][6];
static G3Xvector Vc[NBM][NBP][6];

static void sphere(void)
{
  int p, m;
  double Om = 2*PI/NBM, Pp = PI/NBP;
  for(p = 0; p < NBP; p++)
  {
	  double z = g3x_Radcos(p * Pp);
	  double r = g3x_Radsin(p * Pp);
	  for(m = 0; m < NBM; m++)
	  {
		  Ps[p][m] = g3x_Point(r * (g3x_Radcos(m * Om)), r * g3x_Radsin(m * Om), z);
	  }
  }
}

static void cube(void)
{
  int p, m, n;
  for(p = 0; p < CXMAX; p++)
  {
	  for(m = 0; m < CYMAX; m++)
	  {
		  Pc[p][m][0] = g3x_Point(p/5. - 1, m/5. - 1, -1);
		  Pc[p][m][1] = g3x_Point(p/5. - 1, m/5. - 1, CYMAX / 5. - 1);
		  Pc[p][m][2] = g3x_Point(-1, m/5. - 1, p/5. - 1);
		  Pc[p][m][3] = g3x_Point(CYMAX / 5. - 1, m/5. - 1, p/5. - 1);
		  Pc[p][m][4] = g3x_Point(m/5. - 1, -1, p/5. - 1);
		  Pc[p][m][5] = g3x_Point(m/5. - 1, CYMAX / 5. - 1, p/5. - 1);
	  }
  }
}

/* la fonction d'initialisation : appelée 1 seule fois, au début */
static void init(void)
{
	cube();
}

/* la fonction de contrôle : appelée 1 seule fois, juste après <init> */
static void ctrl(void)
{
}

static void draw_sphere(void)
{
  glDisable(GL_LIGHTING);    /* <BALISE.GL>  "débranche" la lumière, pour permettre le tracé en mode point/ligne */
  	int pas = 20;
	glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);

	glBegin(GL_QUADS);
	int p, m;
	for(p=0; p < NBP - pas - 1; p += pas)
	{
		for(m=0; m < NBM - pas - 1; m += pas)
		{
			g3x_Vertex3dv(Ps[p][m]);
			g3x_Vertex3dv(Ps[p][m+pas]);
			g3x_Vertex3dv(Ps[p+pas][m+pas]);
			g3x_Vertex3dv(Ps[p+pas][m]);
		}
		g3x_Vertex3dv(Ps[p][m]);
		g3x_Vertex3dv(Ps[p][0]);
		g3x_Vertex3dv(Ps[0][0]);
		g3x_Vertex3dv(Ps[0][m]);
	}
	glEnd();

	glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
  glEnable(GL_LIGHTING);
}

static void draw_cube(void)
{
  glDisable(GL_LIGHTING);    /* <BALISE.GL>  "débranche" la lumière, pour permettre le tracé en mode point/ligne */
  	int pas = 20;
	glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);

	glBegin(GL_QUADS);
	int p, m, n;
	for(p=0; p < CXMAX - 1; p++)
	{
		for(m=0; m < CYMAX - 1; m++)
		{
			for(n=0; n < 6; n++)
			{
				g3x_Vertex3dv(Pc[p][m][n]);
				g3x_Vertex3dv(Pc[p][m+1][n]);
				g3x_Vertex3dv(Pc[p+1][m+1][n]);
				g3x_Vertex3dv(Pc[p+1][m][n]);
			}
		}
		g3x_Vertex3dv(Pc[p][m][n]);
		g3x_Vertex3dv(Pc[p][m+1][n]);
		g3x_Vertex3dv(Pc[p][m][n+1]);
		g3x_Vertex3dv(Pc[p][m+1][n+1]);
	}
	glEnd();

	glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
  glEnable(GL_LIGHTING);
}

/* la fonction de dessin : appelée en boucle */
static void draw(void)
{
	//draw_sphere();
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
