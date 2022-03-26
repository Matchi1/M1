#pragma once

#include <g3x.h>

typedef struct _shape_{
	int n1,n2,n3; /* valeurs d’échantillonnage max - la plupart du temps 2 suffisent*/
	G3Xpoint *vrtx; /* tableau des vertex   - spécifique d’une forme*/
	G3Xvector *norm; /* tableau des normales - spécifique d’une forme*/

	/*méthode d’affichage  - spécifique d’une forme*/
	void (*draw_points)(struct _shape_*, G3Xvector scale_factor); /* mode GL_POINTS*/
	void (*draw_faces )(struct _shape_*, G3Xvector scale_factor); /* mode GL_TRIANGLES ou GL_QUADS */
} Shape;
