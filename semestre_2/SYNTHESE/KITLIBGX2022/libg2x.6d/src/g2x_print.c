/* ===============================================================
  E.Incerti - Universite Gustave Eiffel - eric.incerti@univ-eiffel.fr
       - Librairie G2X - Fonctions de base d'acces public -
                    version 6.0 - Mar.2020
  ============================================================= */


/*=================================================*/
/*=           fonctions d'ecriture                =*/
/*=================================================*/
static void* font =  GLUT_BITMAP_HELVETICA_10;
extern void g2x_SelectFont(int size)
{
  if (size<11) { font=GLUT_BITMAP_HELVETICA_10; return; }
  if (size<15) { font=GLUT_BITMAP_HELVETICA_12; return; }
  font=GLUT_BITMAP_HELVETICA_18;
}

/*====================================================================*/
#include <stdarg.h> /* pour fcts a nbre d'args. variable */
extern void g2x_Print(double x, double y, G2Xcolor col, char pos, const char *format, ...)
{
  va_list ap;
  char    msg[1024];
  va_start(ap, format);
  vsprintf(msg, format, ap);
  va_end(ap);
  char  *c;
  int   len=0;
  for (c=msg; *c!='\0'; c++) len+=glutBitmapWidth(font,*c);
  g2x_Color4fv(col);
  switch (pos)
  {
    case 'c' : x-=0.5*Xpixsize*len; break;
    case 'r' : x-=Xpixsize*(len+4); break;
  }
  y-=4*Ypixsize;
  glRasterPos2d(x,y);
  for (c=msg; *c!='\0'; c++) glutBitmapCharacter(font,*c);
}

/*====================================================================*/
extern void g2x_StaticPrint(int x, int y, G2Xcolor col, char pos, const char *format, ...)
{
  va_list ap;
  char    msg[1024];
  va_start(ap, format);
  vsprintf(msg, format, ap);
  va_end(ap);
  char  *c;
  int   len=0;
  for (c=msg; *c!='\0'; c++) len+=glutBitmapWidth(font,*c);
  g2x_Color4fv(col);
  switch (pos)
  {
    case 'c' : x-=0.5*Xpixsize*len; break;
    case 'r' : x-=Xpixsize*(len+4); break;
  }
  y-=4*Ypixsize;
  glRasterPos2d(G2XpixtowinX1(x),G2XpixtowinY1(y));
  for (c=msg; *c!='\0'; c++) glutBitmapCharacter(font,*c);
}


/*====================================================================*/
/* variantes anciennes version - un peu obsolètes                    */
extern void g2x_WriteStatic(char* msg, int x, int y, G2Xcolor col)
{
  char  *c;
  g2x_Color4fv(col);
  glRasterPos2d(G2XpixtowinX1(x),G2XpixtowinY1(y));
  for (c=msg; *c!='\0'; c++) glutBitmapCharacter(font,*c);
}
