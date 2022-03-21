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

#define MAXRES 720

/* tailles de la fenêtre (en pixel) */
static int WWIDTH=1080, WHEIGHT=1080;

typedef struct _shape_{
	int n1,n2,n3; /* valeurs d’échantillonnage max - la plupart du temps 2 suffisent*/
	G3Xpoint *vrtx; /* tableau des vertex   - spécifique d’une forme*/
	G3Xvector *norm; /* tableau des normales - spécifique d’une forme*/

	/*méthode d’affichage  - spécifique d’une forme*/
	void (*draw_points)(struct _shape_*, G3Xvector scale_factor); /* mode GL_POINTS*/
	void (*draw_faces )(struct _shape_*, G3Xvector scale_factor); /* mode GL_TRIANGLES ou GL_QUADS */
} Shape;

Shape cube = {0};

static void cube_vertex(Shape *shape, int lines)
{
	int faces = 6;
	int points_per_face = lines * lines;
	int number_points = (faces * points_per_face);
	shape->n1 = lines;
	shape->vrtx = (G3Xpoint*) malloc(sizeof(G3Xpoint) * number_points);
	shape->norm = (G3Xvector*) malloc(sizeof(G3Xvector) * number_points);
	int base = 0;
	for(int i = 0; i < lines - 1; i++) {
		for(int j = 0; j < lines; j++) {
			uint index = base + i * lines + j;
			shape->vrtx[index] = g3x_Point(-0.5 + ((double) i) / lines, -0.5 + ((double) j) / lines, -0.5);
			shape->norm[index] = g3x_Point(0, 0, -1);
		}
	}
	base += lines * lines;
	for(int i = 0; i < lines; i++) {
		for(int j = 0; j < lines; j++) {
			uint index = base + i * lines + j;
			shape->vrtx[index] = g3x_Point(-0.5, -0.5 + ((double) i) / lines, -0.5 + ((double) j) / lines);
			shape->norm[index] = g3x_Point(-1, 0, 0);
		}
	}
	base += lines * lines;
	for(int i = 0; i < lines; i++) {
		for(int j = 0; j < lines; j++) {
			uint index = base + i * lines + j;
			shape->vrtx[index] = g3x_Point(-0.5 + ((double) j) / lines, -0.5, -0.5 + ((double) i) / lines);
			shape->norm[index] = g3x_Point(0, -1, 0);
		}
	}
	base += lines * lines;
	for(int i = 0; i < lines; i++) {
		for(int j = 0; j < lines; j++) {
			uint index = base + i * lines + j;
			shape->vrtx[index] = g3x_Point(-0.5 + ((double) i) / lines, -0.5 + ((double) j) / lines, 0.5);
			shape->norm[index] = g3x_Point(0, 0, 1);
		}
	}
	base += lines * lines;
	for(int i = 0; i < lines; i++) {
		for(int j = 0; j < lines; j++) {
			uint index = base + i * lines + j;
			shape->vrtx[index] = g3x_Point(0.5, -0.5 + ((double) i) / lines, -0.5 + ((double) j) / lines);
			shape->norm[index] = g3x_Point(1, 0, 0);
		}
	}
	base += lines * lines;
	for(int i = 0; i < lines; i++) {
		for(int j = 0; j < lines; j++) {
			uint index = base + i * lines + j;
			shape->vrtx[index] = g3x_Point(-0.5 + ((double) j) / lines, 0.5, -0.5 + ((double) i) / lines);
			shape->norm[index] = g3x_Point(0, 1, 0);
		}
	}
}

/* la fonction d'initialisation : appelée 1 seule fois, au début */
static void init(void)
{
	cube_vertex(&cube, MAXRES);
}

/* la fonction de contrôle : appelée 1 seule fois, juste après <init> */
static void ctrl(void)
{
}

static void draw_cube(void)
{
  int ppas = 10;
  glDisable(GL_LIGHTING);    /* <BALISE.GL>  "débranche" la lumière, pour permettre le tracé en mode point/ligne */
	glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);

	glBegin(GL_POINTS);
	for(int i = 0; i < cube.n1 * cube.n1 * 6; i += 10)
	{
		g3x_Normal3dv(cube.norm[i]);
		g3x_Vertex3dv(cube.vrtx[i]);
	}
    for(int k = 0; k < 6; k++)
    {
        for(int i = 0; i < cube.n1 - 1; i += ppas)
        {
            for(int j = 0; j < cube.n1 - 1; j += ppas)
            {
                g3x_Normal3dv(cube.norm[i + j + k]);
                g3x_Vertex3dv(cube.vrtx[i + j + k]);
            }
            g3x_Normal3dv(cube.norm[i + MAXRES + k]);
            g3x_Vertex3dv(cube.vrtx[i + MAXRES + k]);
        }
        for(int j = 0; j < cube.n1 - 1; j += ppas)
        {
            g3x_Normal3dv(cube.norm[MAXRES + j + k]);
            g3x_Vertex3dv(cube.vrtx[MAXRES + j + k]);
        }
        g3x_Normal3dv(cube.norm[2 * MAXRES + k]);
        g3x_Vertex3dv(cube.vrtx[2 * MAXRES + k]);
    }
	glEnd();

	glBegin(GL_QUADS);
	for(int k = 0; k < 6; k++)
	{
		int base = k * (cube.n1 * cube.n1);
		for(int i = 0; i < cube.n1 - 1; i++)
		{
			for(int j = 0; j < cube.n1 - 1; j++)
			{
				int origin = base + i * cube.n1 + j;
				g3x_Vertex3dv(cube.vrtx[origin]);
				g3x_Vertex3dv(cube.vrtx[origin + 1]);
				g3x_Vertex3dv(cube.vrtx[origin + cube.n1 + 1]);
				g3x_Vertex3dv(cube.vrtx[origin + cube.n1]);
			}
		}
	}
	glEnd();

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
