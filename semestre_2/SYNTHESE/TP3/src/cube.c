#include <g3x.h>

#include "../include/shape.h"
#include "../include/cube.h"

#define FACES 6

void cube_vertex_draw(Shape *cube, G3Xvector scale_factor) {
	for(int face = 0; face < FACES; face++)
	{
		int base = face * (cube->n1 * cube->n1);
		for(int i = 0; i < cube->n1 - 1; i += scale_factor.x)
		{
			for(int j = 0; j < cube->n1 - 1; j += scale_factor.y)
			{
                int k = base + (i ) * cube->n1 + (j ); 
                g3x_Normal3dv(cube->norm[k]); g3x_Vertex3dv(cube->vrtx[k]);
			}
		}
	}
}

void cube_quads_draw(Shape *cube, G3Xvector scale_factor) {
	for(int face = 0; face < FACES; face++)
	{
		int base = face * (cube->n1 * cube->n1);
		for(int i = 0; i < cube->n1 - 1; i += scale_factor.x)
		{
			for(int j = 0; j < cube->n1 - 1; j += scale_factor.y)
			{
                int k = base + (i ) * cube->n1 + (j ); 
                g3x_Normal3dv(cube->norm[k]); g3x_Vertex3dv(cube->vrtx[k]);
                k = base + (i )*cube->n1+(j+1);
                g3x_Normal3dv(cube->norm[k]); g3x_Vertex3dv(cube->vrtx[k]);
                k = base + (i+1)*cube->n1+(j+1);
                g3x_Normal3dv(cube->norm[k]); g3x_Vertex3dv(cube->vrtx[k]);
                k = base + (i+1)*cube->n1+(j ); 
                g3x_Normal3dv(cube->norm[k]); g3x_Vertex3dv(cube->vrtx[k]);
			}
		}
	}
}

/*
void face1(Shape *shape, int lines) {
    int i, j, base = 0;
	for(i = 0; i < lines - 1; i++) {
        uint index = 0;
		for(j = 0; j < lines - 1; j++) {
			index = base + i * lines + j;
			shape->vrtx[index] = g3x_Point(-0.5 + ((double) i) / lines, -0.5 + ((double) j) / lines, -0.5);
			shape->norm[index] = g3x_Point(0, 0, -1);
		}
	}
}

void face2(Shape *shape, int lines) {
    int i, j, base = lines * lines;
	for(int i = 0; i < lines; i++) {
		for(int j = 0; j < lines; j++) {
			uint index = base + i * lines + j;
			shape->vrtx[index] = g3x_Point(-0.5, -0.5 + ((double) i) / lines, -0.5 + ((double) j) / lines);
			shape->norm[index] = g3x_Point(-1, 0, 0);
		}
	}
}

void face3(Shape *shape, int lines) {
    int i, j, base = 2 * lines * lines;
	for(int i = 0; i < lines; i++) {
		for(int j = 0; j < lines; j++) {
			uint index = base + i * lines + j;
			shape->vrtx[index] = g3x_Point(-0.5 + ((double) j) / lines, -0.5, -0.5 + ((double) i) / lines);
			shape->norm[index] = g3x_Point(0, -1, 0);
		}
	}
}

void face4(Shape *shape, int lines) {
    int i, j, base = 3 * lines * lines;
	for(int i = 0; i < lines; i++) {
		for(int j = 0; j < lines; j++) {
			uint index = base + i * lines + j;
			shape->vrtx[index] = g3x_Point(-0.5 + ((double) i) / lines, -0.5 + ((double) j) / lines, 0.5);
			shape->norm[index] = g3x_Point(0, 0, 1);
		}
	}
}

void face5(Shape *shape, int lines) {
    int i, j, base = 4 * lines * lines;
	for(int i = 0; i < lines; i++) {
		for(int j = 0; j < lines; j++) {
			uint index = base + i * lines + j;
			shape->vrtx[index] = g3x_Point(0.5, -0.5 + ((double) i) / lines, -0.5 + ((double) j) / lines);
			shape->norm[index] = g3x_Point(1, 0, 0);
		}
	}
}

void face6(Shape *shape, int lines) {
    int i, j, base = 5 * lines * lines;
	for(int i = 0; i < lines; i++) {
		for(int j = 0; j < lines; j++) {
			uint index = base + i * lines + j;
			shape->vrtx[index] = g3x_Point(-0.5 + ((double) j) / lines, 0.5, -0.5 + ((double) i) / lines);
			shape->norm[index] = g3x_Point(0, 1, 0);
		}
	}
}
*/

void face1(Shape *shape, int lines) {
    int i, j, base = 0;
    uint index = 0;
	for(i = 0; i < lines - 1; i++) {
		for(j = 0; j < lines - 1; j++) {
			index = base + i * lines + j;
			shape->vrtx[index] = g3x_Point(-0.5 + ((double) i) / lines, -0.5 + ((double) j) / lines, -0.5);
			shape->norm[index] = g3x_Point(0, 0, -1);
		}
		shape->vrtx[index + 1] = g3x_Point(-0.5 + ((double) i) / lines, 0.5, -0.5);
	}
    for(j = 0; j < lines - 1; j++) {
        index = base + i * lines + j;
		shape->vrtx[index] = g3x_Point(0.5, -0.5 + ((double) j) / lines, -0.5);
        shape->norm[index] = g3x_Point(0, 0, -1);
    }
    shape->vrtx[index + 1] = g3x_Point(0.5, 0.5, -0.5);
}

void face2(Shape *shape, int lines) {
    int i, j, base = lines * lines;
    uint index = 0;
	for(i = 0; i < lines - 1; i++) {
		for(j = 0; j < lines - 1; j++) {
			index = base + i * lines + j;
			shape->vrtx[index] = g3x_Point(-0.5, -0.5 + ((double) i) / lines, -0.5 + ((double) j) / lines);
			shape->norm[index] = g3x_Point(-1, 0, 0);
		}
		shape->vrtx[index + 1] = g3x_Point(-0.5, -0.5 + ((double) i) / lines, 0.5);
	}
    for(j = 0; j < lines - 1; j++) {
        index = base + i * lines + j;
		shape->vrtx[index] = g3x_Point(-0.5, 0.5, -0.5 + ((double) j) / lines);
        shape->norm[index] = g3x_Point(-1, 0, 0);
    }
    shape->vrtx[index + 1] = g3x_Point(-0.5, 0.5, 0.5);
}

void face3(Shape *shape, int lines) {
    int i, j, base = 2 * lines * lines;
    uint index = 0;
	for(i = 0; i < lines - 1; i++) {
		for(j = 0; j < lines - 1; j++) {
			index = base + i * lines + j;
			shape->vrtx[index] = g3x_Point(-0.5 + ((double) j) / lines, -0.5, -0.5 + ((double) i) / lines);
			shape->norm[index] = g3x_Point(0, -1, 0);
		}
		shape->vrtx[index + 1] = g3x_Point(0.5, -0.5, -0.5 + ((double) i) / lines);
	}
    for(j = 0; j < lines - 1; j++) {
        index = base + i * lines + j;
		shape->vrtx[index] = g3x_Point(-0.5 + ((double) j) / lines, -0.5, 0.5);
        shape->norm[index] = g3x_Point(0, -1, 0);
    }
    shape->vrtx[index + 1] = g3x_Point(0.5, -0.5, 0.5);
}

void face4(Shape *shape, int lines) {
    int i, j, base = 3 * lines * lines;
    uint index = 0;
	for(i = 0; i < lines - 1; i++) {
		for(j = 0; j < lines - 1; j++) {
			index = base + i * lines + j;
			shape->vrtx[index] = g3x_Point(-0.5 + ((double) i) / lines, -0.5 + ((double) j) / lines, 0.5);
			shape->norm[index] = g3x_Point(0, 0, 1);
        }
		shape->vrtx[index + 1] = g3x_Point(-0.5 + ((double) i) / lines, 0.5, 0.5);
	}
    for(j = 0; j < lines - 1; j++) {
        index = base + i * lines + j;
		shape->vrtx[index] = g3x_Point(0.5, -0.5 + ((double) j) / lines, 0.5);
        shape->norm[index] = g3x_Point(0, 0, 1);
    }
    shape->vrtx[index + 1] = g3x_Point(0.5, 0.5, 0.5);
}

void face5(Shape *shape, int lines) {
    int i, j, base = 4 * lines * lines;
    uint index = 0;
	for(int i = 0; i < lines - 1; i++) {
		for(int j = 0; j < lines - 1; j++) {
			index = base + i * lines + j;
			shape->vrtx[index] = g3x_Point(0.5, -0.5 + ((double) i) / lines, -0.5 + ((double) j) / lines);
			shape->norm[index] = g3x_Point(1, 0, 0);
		}
		shape->vrtx[index + 1] = g3x_Point(0.5, -0.5 + ((double) i) / lines, 0.5);
	}
    for(int j = 0; j < lines - 1; j++) {
        index = base + i * lines + j;
		shape->vrtx[index] = g3x_Point(0.5, 0.5, -0.5 + ((double) j) / lines);
        shape->norm[index] = g3x_Point(1, 0, 0);
    }
    shape->vrtx[index + 1] = g3x_Point(0.5, 0.5, 0.5);
}

void face6(Shape *shape, int lines) {
    int i, j, base = 5 * lines * lines;
    uint index = 0;
	for(int i = 0; i < lines - 1; i++) {
		for(int j = 0; j < lines - 1; j++) {
			index = base + i * lines + j;
			shape->vrtx[index] = g3x_Point(-0.5 + ((double) j) / lines, 0.5, -0.5 + ((double) i) / lines);
			shape->norm[index] = g3x_Point(0, 1, 0);
		}
		shape->vrtx[index + 1] = g3x_Point(0.5, 0.5, -0.5 + ((double) i) / lines);
	}
    for(int j = 0; j < lines - 1; j++) {
        index = base + i * lines + j;
		shape->vrtx[index] = g3x_Point(-0.5 + ((double) j) / lines, 0.5, 0.5);
        shape->norm[index] = g3x_Point(0, 1, 0);
    }
    shape->vrtx[index + 1] = g3x_Point(0.5, 0.5, 0.5);
}

void cube_init(Shape *shape, int lines) {
	int points_per_face = lines * lines;
	int number_points = (FACES * points_per_face);
	shape->n1 = lines;
	shape->vrtx = (G3Xpoint*) malloc(sizeof(G3Xpoint) * number_points);
	shape->norm = (G3Xvector*) malloc(sizeof(G3Xvector) * number_points);
    face1(shape, lines);
    face2(shape, lines);
    face3(shape, lines);
    face4(shape, lines);
    face5(shape, lines);
    face6(shape, lines);
    shape->draw_points = cube_vertex_draw;
    shape->draw_faces = cube_quads_draw;
}
