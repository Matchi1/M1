/*!===============================================================
  E.Incerti - Universite Gustave Eiffel - eric.incerti@univ-eiffel.fr
       - Librairie G3X - Fonctions de base d'acces public -
                    version 6d - Jan.2022
  ================================================================!*/


/*=================================================*/
/*=           fonctions d'ecriture                =*/
/*=================================================*/
static void* font =  GLUT_BITMAP_HELVETICA_10;
extern void g3x_SelectFont(int size)
{
  if (size<11) { font=GLUT_BITMAP_HELVETICA_10; return; }
  if (size<15) { font=GLUT_BITMAP_HELVETICA_12; return; }
  font=GLUT_BITMAP_HELVETICA_18;
}

/*====================================================================*/
#include <stdarg.h> /* pour fcts a nbre d'args. variable */
extern void g3x_Print(int x, int y, G3Xcolor col, char pos, const char *format, ...)
{
  va_list ap;
  char    msg[1024];
  va_start(ap, format);
  vsprintf(msg, format, ap);
  va_end(ap);
  char  *c;
  int   len=0;
  for (c=msg; *c!='\0'; c++) len+=glutBitmapWidth(font,*c);
  g3x_Color4fv(col);
  switch (pos)
  {
    case 'c' : x-=len/2; break;
    case 'r' : x-=len+4; break;
  }
  y-=4;
  glRasterPos2d(x,y);
  for (c=msg; *c!='\0'; c++) glutBitmapCharacter(font,*c);
}


/*====================================================================*/
/* variante ancienne version - un peu obsolète                        */
extern void g3x_Write(const char *msg, int x, int y, G3Xcolor col)
{
  int i=0;
  glDisable(GL_LIGHTING);
  glDisable(GL_DEPTH_TEST);
  glShadeModel(GL_FLAT);
  glMatrixMode(GL_PROJECTION);
  glOrtho(-0.5,639.5,-0.5,479.5,-1.0,1.0);
  glMatrixMode(GL_MODELVIEW);
  glLoadIdentity();
  glColor4f(col.r,col.g,col.b,col.a);
  glRasterPos2i(x,y);
  while (msg[i]!='\0')
  {
    glutBitmapCharacter(GLUT_BITMAP_HELVETICA_10,msg[i]);
    i++;
  }
  glEnable(GL_LIGHTING);
  glEnable(GL_DEPTH_TEST);
  glShadeModel(GL_SMOOTH);
}
