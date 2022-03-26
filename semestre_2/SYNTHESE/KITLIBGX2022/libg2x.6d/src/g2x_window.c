/*!===============================================================
  E.Incerti - Universite Gustave Eiffel - eric.incerti@univ-eiffel.fr
       - Librairie G2X - Fonctions de base d'acces public -
                    version 6d - Jan.2022
  ================================================================!*/

#ifdef __cplusplus
  extern "C" {
#else
  #define _GNU_SOURCE
#endif

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <time.h>
#include <math.h>
#include <ctype.h>
#include <sys/times.h>
#include <unistd.h>

#include <GL/gl.h>
#include <GL/glu.h>
#include <GL/glut.h>

#include <g2x.h>

#define BLOCSIZE 32
#define MAXBLOC  32

/* tailles par defaut de la fenetre gl */
#define DFT_PIXHEIGHT (GLint)512
#define DFT_PIXWIDTH  (GLint)512

/* tailles des bandeaux */
#define DFT_DIALWIDTH ((GLint)12)


static GLint  xdialwin,ydialwin,cornwin,drawwin,mainwin;
static bool   xdialflag=false, ydialflag=false, cornflag=false;
static char   _WINDNAME_[128]="";

/* tailles de la fenetre : dim. en pixels     */
static GLint  curheight =DFT_PIXHEIGHT; /* */
static GLint  pixheight =DFT_PIXHEIGHT; /* */
static GLint  curwidth  =DFT_PIXWIDTH;  /* */
static GLint  pixwidth  =DFT_PIXWIDTH;  /* */
static GLint  fullwidth,fullheight;

/* spécifique 2D : dimensions de la feetre reelle  */
#define G2XpixtowinX1(x) (double)(Xcmin  + (x)*Xpixsize)
#define G2XpixtowinY1(y) (double)(Ycmin  + (y)*Ypixsize)
static double Xwmin,Xwmax,Ywmin,Ywmax;
static double Xcmin,Xcmax,Ycmin,Ycmax;
static double Xpixsize,Ypixsize;
static double Xratio,Yratio;
static double zoom=1.;

/* PARAMETRES DU BANDEAU LATERAL : position des boutons et scrollbars */
static GLint  Ydialwidth=0,Xdialheight=0;
static GLint  Xbutpos   =0,Ybutpos=12;
static GLint  Xbutw     =0;

/*= Couleur de fond                                                  =*/
static GLfloat G2X_BKG=1.0;
extern void    g2x_SetBkgCol(double bkg) { G2X_BKG=(GLfloat)bkg;  }
extern double  g2x_GetBkgCol(void      ) { return (double)G2X_BKG; }

/*static int  KeyMode=PARAM;*/
static GLuint pushmode=GLUT_UP;
static GLint  moving;

static bool  _IDLE_       = false;
static bool  _RUN_        = false ;
static bool  _AUTODISPLAY_= true ;
static bool  _FULLSCREEN_ = false;
static bool  _INFO_       = false;
/* spécifique 2D */
static bool  _GRID_       = false;
static int   _BKGLIST_ ;

/*= prototypes =*/
static void (*g2x_Init)(void)=NULL;             /*! fonction d'initialisation !*/
static void (*g2x_Ctrl)(void)=NULL;             /*! fonction de contrôle      !*/
static void (*g2x_Idle)(void)=NULL;             /*! fonction d'animation      !*/
static void (*g2x_Draw)(void)=NULL;             /*! fonction de dessin        !*/
static void (*g2x_Exit)(void)=NULL;             /*! fonction de sortie        !*/
/* spécifique 2D */
static void (*g2x_BkgF)(void)=NULL;             /*! fonction de background    !*/



extern int    g2x_GetPixWidth(void) { return curwidth;  }
extern int    g2x_GetPixHeight(void){ return curheight; }
/* spécifique 2D */
extern double g2x_GetXPixSize(void) { return Xpixsize;  }
extern double g2x_GetYPixSize(void) { return Ypixsize;  }
extern double g2x_GetXMin(void)     { return Xcmin;     }
extern double g2x_GetXMax(void)     { return Xcmax;     }
extern double g2x_GetYMin(void)     { return Ycmin;     }
extern double g2x_GetYMax(void)     { return Ycmax;     }
extern double g2x_GetXRatio(void)   { return Xratio;    }
extern double g2x_GetYRatio(void)   { return Yratio;    }

extern void   g2x_SelectDrawWin(void) { glutSetWindow(drawwin); }

/*=======================================*/
/*= FONCTIONS DE GESTION DES EVENEMENTS =*/
/*=======================================*/
/*
extern bool g2x_Running(void)  { return _RUN_; } // obsolete
extern void g2x_Stop(void)     { _RUN_=false;  } // obsolete
extern void g2x_Continue(void) { _RUN_=true;   } // obsolete
extern void g2x_Show(void)     { glutPostWindowRedisplay(drawwin); } // obsolete
*/

/*= automatisation - rarement utilisee =*/
extern void g2x_SetAutoDisplay(bool flag) { _AUTODISPLAY_=flag; }
extern void g2x_ToggleAutoDisplay(void)   { _AUTODISPLAY_=!_AUTODISPLAY_; }
static int   _VIDEO_ = 0;
extern void g2x_AutoStartVideo(void)      { _RUN_=true; _VIDEO_=1; }
/*= snapshot attache a des touches ('j'/'p')  =*/
extern void g2x_AutoSnap_jpg(void) { g2x_Snapshot("jpg",_WINDNAME_,curwidth,curheight); }
extern void g2x_AutoSnap_png(void) { g2x_Snapshot("png",_WINDNAME_,curwidth,curheight); }

/*= - Fonction d'initialisation                                                      =*/
extern void g2x_SetInitFunction(void (*f)(void)) { g2x_Init=f; }
/*= - Fonction de contrôle                                                      =*/
extern void g2x_SetCtrlFunction(void (*f)(void)) { g2x_Ctrl=f; }
/*= - DrawFunc   : procedure d'affichage, associee a la fenetre de dessin 'drawwin'  =*/
extern void g2x_SetDrawFunction(void (*f)(void)) { g2x_Draw = f; }
/*= - FreeFunc   : procedure de liberation de memoire, appelee a la sortie           =*/
extern void g2x_SetExitFunction(void (*f)(void)) { g2x_Exit = f; }
/*= - IdleFunc   : procedure de la boucle d'action Gl, passee a <glutIdleFunc>       =*/
extern void g2x_SetAnimFunction(void (*f)(void)) { g2x_Idle=f; _IDLE_=true; }
/* spécifique 2D */
/*= - BkgFunc   : procedure d'affichage "background" : non modifie                   =*/
extern void g2x_SetBkgFunction(void (*f)(void)) { g2x_BkgF = f; }

/* frequence d'affichage */
static int  _FAFF_=1;
/* par defaut : tous les pas de calcul sont affiches */
static void _idle_0_(void)
{
  g2x_Idle();
  glutPostWindowRedisplay(drawwin);
}

/* affichage temporise                               */
static void _idle_F_(void)
{
  static int t=0;
  g2x_Idle();
  if (t==_FAFF_) { glutPostWindowRedisplay(drawwin); t=0; }
  t++;
}

/* regle la frequence d'affichage et selctionne la bonne <idle func>  */
extern void g2x_SetRefreshFreq(int freq)
{
  if (_FAFF_==freq) return;
  _FAFF_=freq;
  glutIdleFunc((_IDLE_&&_RUN_)?(_FAFF_>1?_idle_F_:_idle_0_):NULL);
}

/*=================================================*/
/* Code C séparé - ne peut pas être compilé à part */
/* trop de dépendance aux variables globales       */
#include "g2x_ctrlpnt.c"   // Gestion des points 'cliquables' (spécifique 2D)
#include "g2x_ctrlprm.c"   // Contrôle de paramètre (pavé fléché)
#include "g2x_keys.c"      // Gestion clavier
#include "g2x_popup.c"     // Boutons 'popup' (action unique)
#include "g2x_button.c"    // Boutons on/off
#include "g2x_switch.c"    // Switch (Boutons on/off exclusifs)
#include "g2x_scroll.c"    // ScrollBars
#include "g2x_mouse.c"     // Gestion souris
#include "g2x_print.c"     // Gestion texte

/*=================================================*/

/*====================================================================*/
/*=              trace des axes (spécifique 2D)                      =*/
/*====================================================================*/
extern void g2x_Axes(void)
{
  glutSetWindow(drawwin);
  g2x_Color4fv(G2Xwb);
  glLineWidth(1.);
  glBegin(GL_LINES);
    glVertex2d(Xcmin,0);
    glVertex2d(Xcmax,0);
    glVertex2d(0,Ycmin);
    glVertex2d(0,Ycmax);
  glEnd();
}

/*====================================================================*/
extern void g2x_Grid(G2Xcolor col1, G2Xcolor col2)
{
  glutSetWindow(drawwin);
  double x,y;
  double pasx=0.05*(Xwmax-Xwmin),
         pasy=0.05*(Ywmax-Ywmin);
  g2x_Color4fv(col2);
  glLineWidth(1.);
  glBegin(GL_LINES);
  for (x=+pasx;x>Xcmin;x-=pasx) { glVertex2d(x,Ycmin); glVertex2d(x,Ycmax); }
  for (x=-pasx;x<Xcmax;x+=pasx) { glVertex2d(x,Ycmin); glVertex2d(x,Ycmax); }
  for (y=+pasy;y<Ycmax;y+=pasy) { glVertex2d(Xcmin,y); glVertex2d(Xcmax,y); }
  for (y=-pasy;y>Ycmin;y-=pasy) { glVertex2d(Xcmin,y); glVertex2d(Xcmax,y); }
  g2x_Color4fv(col1);
  glVertex2d(Xcmin,0);
  glVertex2d(Xcmax,0);
  glVertex2d(0,Ycmin);
  glVertex2d(0,Ycmax);
  glEnd();
}

/*====================================================================*/
static void g2x_GridDft(bool flag)
{
  if (!flag) return;
  glutSetWindow(drawwin);
  double x,y,c=0.75*(1.-0.5*G2X_BKG);
  double pasx=0.05*(Xwmax-Xwmin),
         pasy=0.05*(Ywmax-Ywmin);
  glColor4f(c,c,c,0.5);
  glLineWidth(1.);
  glBegin(GL_LINES);
  for (x=+pasx;x>Xcmin;x-=pasx) { glVertex2d(x,Ycmin); glVertex2d(x,Ycmax); }
  for (x=-pasx;x<Xcmax;x+=pasx) { glVertex2d(x,Ycmin); glVertex2d(x,Ycmax); }
  for (y=+pasy;y<Ycmax;y+=pasy) { glVertex2d(Xcmin,y); glVertex2d(Xcmax,y); }
  for (y=-pasy;y>Ycmin;y-=pasy) { glVertex2d(Xcmin,y); glVertex2d(Xcmax,y); }
  c=(1.-G2X_BKG);
  glColor4f(c,c,c,0.5);
  glVertex2d(Xcmin,0);
  glVertex2d(Xcmax,0);
  glVertex2d(0,Ycmin);
  glVertex2d(0,Ycmax);
  glEnd();
}

/*====================================================================*/
/*=                     panneau d'information                        =*/
/*====================================================================*/
static void g2x_Info(bool flag)
{
  if (!flag) return;
  static char msg[128];
  static int x,y,usery;
/*
  static uint id=0;
  if (id==0)
  {
    id=1+glGenLists(1);
    glNewList(id-1, GL_COMPILE);
*/
  glPolygonMode(GL_FRONT_AND_BACK,GL_FILL);
  g2x_Color4fv(G2Xk_b);
  glBegin(GL_QUADS);
    glVertex2d(G2XpixtowinX1(        +10),G2XpixtowinY1(         +10));
    glVertex2d(G2XpixtowinX1(        +10),G2XpixtowinY1(curheight-10));
    glVertex2d(G2XpixtowinX1(curwidth-10),G2XpixtowinY1(curheight-10));
    glVertex2d(G2XpixtowinX1(curwidth-10),G2XpixtowinY1(         +10));
  glEnd();
  glPolygonMode(GL_FRONT_AND_BACK,GL_LINE);

  x=20;
  y=curheight-16;
  g2x_SelectFont(12);
  y-=16; g2x_StaticPrint(x,y,G2Xw,'l',"Librairie G2X - version 6d");
  g2x_SelectFont(12);
  y-=16; g2x_StaticPrint(x,y,G2Xw,'l',"_______________ACTIONS PREDEFINIES_______________");
  y-=16; g2x_StaticPrint(x,y,G2Xw,'l',"<ESC>,<Ctrl+q> : quitte le programme proprement      ");
  y-=16; g2x_StaticPrint(x,y,G2Xw,'l',"<?> : affiche cette fenetre d'aide                   ");
  y-=16; g2x_StaticPrint(x,y,G2Xw,'l',"<Ctrl+f> : plein ecran                               ");
  y-=16; g2x_StaticPrint(x,y,G2Xw,'l',"<Ctrl+r> : affiche le repere principal               ");
  y-=16; g2x_StaticPrint(x,y,G2Xw,'l',"<Ctrl+w> : inverse la couleur de fond                ");
  y-=16; g2x_StaticPrint(x,y,G2Xw,'l',"<Ctrl+j> : snapshot format JPG                       ");
  y-=16; g2x_StaticPrint(x,y,G2Xw,'l',"<Ctrl+p> : snapshot format PNG                       ");
  y-=16; g2x_StaticPrint(x,y,G2Xw,'l',"[+],[-],[=] : facteurs de zoom ([=]:1.)              ");
  if (_IDLE_)
{ y-=16; g2x_StaticPrint(x,y,G2Xw,'l',"<SPACE> : stoppe/relance l'animation                 "); }
  y-=16;

  usery = y;
  if (_SCROLLH_ || _SCROLLV_ || _BUTTON_ || _SWITCH_)
  {
    y-=16; g2x_StaticPrint(x,y,G2Xg,'l',"_________CONTROLE UTILISATEUR___________");
    G2Xscroll *scroll=_SCROLLH_;
    while (scroll<_SCROLLH_+scrollhnum)
    {
      y-=16;
      g2x_StaticPrint(x,y,G2Xc,'l',"Scroll H. <%s> :\t\t%s",scroll->name,strlen(scroll->info)>0?scroll->info:"non renseigne");
      scroll++;
    }
    scroll=_SCROLLV_;
    while (scroll<_SCROLLV_+scrollvnum)
    {
      y-=16;
      g2x_StaticPrint(x,y,G2Xc,'l',"Scroll V. <%s> :\t\t%s",scroll->name,strlen(scroll->info)>0?scroll->info:"non renseigne");
      scroll++;
    }
    G2Xbut *but=_BUTTON_;
    while (but<_BUTTON_+butnum)
    {
      y-=16;
      g2x_StaticPrint(x,y,G2Xc,'l',"Button  <%s> :\t\t%s" ,but->name,strlen(but->info)>0?but->info:"non renseigne");
      but++;
    }
    G2Xpopup *pop=_POPUP_;
    while (pop<_POPUP_+popnum)
    {
      y-=16;
      g2x_StaticPrint(x,y,G2Xc,'l',"PopUp  <%s> :\t\t%s" ,pop->name,strlen(pop->info)>0?pop->info:"non renseigne");
      pop++;
    }
    G2Xswitch *swt=_SWITCH_;
    while (swt<_SWITCH_+switchnum)
    {
      y-=16;
      g2x_StaticPrint(x,y,G2Xc,'l',"Switch  <%s> :\t\t%s",swt->name,strlen(swt->info)>0?swt->info:"non renseigne");
      swt++;
    }
  }
  x = 20 + curwidth/2;
  y = usery;
  if (keynumb>0)
  {
    y-=16; g2x_StaticPrint(x,y,G2Xg,'l',"_________ACTIONS UTILISATEUR___________");
    G2Xkey *k;
    for (k=_KEY_; k<_KEY_+keynumb; k++)
    {
      y-=16; g2x_StaticPrint(x,y,G2Xc,'l',"<%c> : %s",k->key,k->info);
    }
  }
  /*
  glEndList();
  }
  else glCallList(id-1);
  */
}

/*====================================================================*/
/* Spécifique 2D                                                      */
static void g2x_RescaleWindow(void)
{
  Xratio    = curwidth/(zoom*pixwidth );
  Yratio    = curheight/(zoom*pixheight);
  Xcmin     = Xwmin*Xratio;
  Xcmax     = Xwmax*Xratio;
  Ycmin     = Ywmin*Yratio;
  Ycmax     = Ywmax*Yratio;
  Xpixsize  = (Xcmax-Xcmin)/curwidth ;
  Ypixsize  = (Ycmax-Ycmin)/curheight;
}


/*==================================*/
/*=  RESHAPE                       =*/
/*==================================*/
static __inline__ void g2x_CornReshape(int w, int h)
{
  glViewport (0, 0, w, h);
}

static __inline__ void g2x_XDialReshape(int w, int h)
{
  glViewport (0, 0, w, h);
  if (_SCROLLH_) g2x_SetUpScrollh();
}

static __inline__ void g2x_YDialReshape(int w, int h)
{
  glViewport (0, 0, w, h);
  if (_SCROLLV_) g2x_SetUpScrollv();
}

static __inline__ void g2x_DrawReshape(int w, int h)
{
  glViewport (0, 0, w, h);
}

static __inline__ void g2x_MainReshape(int w, int h)
{
  glViewport (0, 0, w, h);
  curwidth  = w-Ydialwidth;
  curheight = h-Xdialheight;

  if (cornflag)
  {
    glutSetWindow (cornwin);
    glutReshapeWindow(Ydialwidth,Xdialheight);
    glutPositionWindow(curwidth,curheight);
    glutShowWindow();
  }
  if (xdialflag)
  {
    glutSetWindow (xdialwin);
    glutReshapeWindow(curwidth,Xdialheight);
    glutPositionWindow(0,curheight);
    glutShowWindow();
  }
  if (ydialflag)
  {
    glutSetWindow (ydialwin);
    glutReshapeWindow(Ydialwidth,curheight);
    glutPositionWindow(curwidth,0);
    glutShowWindow();
  }
  glutSetWindow (drawwin);
  glutReshapeWindow(curwidth,curheight);
  glutPositionWindow(0,0);
  glutShowWindow();
}


/*==================================*/
/*=  DISPLAY                       =*/
/*==================================*/
static __inline__ void g2x_CornDisplay(void)
{
  glutSetWindow (cornwin);
  glLoadIdentity();
  gluOrtho2D (0.,(double)Ydialwidth,0.,(double)Xdialheight);
  glClearColor(.75,.80,.85,1.);
  glClear(GL_COLOR_BUFFER_BIT);
  g2x_DrawInfoSwitch();
  glutSwapBuffers();
}

static __inline__ void g2x_XDialDisplay(void)
{
  glutSetWindow (xdialwin);
  glLoadIdentity();
  gluOrtho2D (0.,(double)curwidth,0.,(double)Xdialheight);
  glClearColor(.75,.80,.85,1.);
  glClear(GL_COLOR_BUFFER_BIT);
  G2Xscroll *scroll=_SCROLLH_;
  while (scroll<_SCROLLH_+scrollhnum)
  {
    g2x_DrawScrollh(scroll);
    scroll++;
  }
  glutSwapBuffers();
}

static __inline__ void g2x_YDialDisplay(void)
{
  glutSetWindow (ydialwin);
  glLoadIdentity();
  gluOrtho2D (0.,(double)Ydialwidth,0.,(double)curheight);
  glClearColor(.75,.80,.85,1.);
  glClear(GL_COLOR_BUFFER_BIT);
  if (_BUTTON_) g2x_DrawButtons();
  if (_POPUP_ ) g2x_DrawPopUps();
  if (_SWITCH_) g2x_DrawSwitches();
  G2Xscroll *scroll=_SCROLLV_;
  while (scroll<_SCROLLV_+scrollvnum)
  {
    g2x_DrawScrollv(scroll);
    scroll++;
  }
  glutSwapBuffers();
}

static __inline__ void g2x_DrawDisplay(void)
{
  /* patch dégeulasse pour éviter 4 appels inutiles à g2x_DrawDisplay() */
  /* ... pas réussi à identifier l'origine de ces appels                */
  /* ... en 3D, 3 passages suffisent                                    */
  static ulint i=0;
  if ((++i<4)) return;

  glutSetWindow (drawwin);
  g2x_RescaleWindow();
  glClearColor(G2X_BKG,G2X_BKG,G2X_BKG,1.);
  glClear(GL_COLOR_BUFFER_BIT);
  glLoadIdentity();
  /*! sp&cifique 2D !*/
  glOrtho(Xwmin*Xratio,Xwmax*Xratio,Ywmin*Yratio,Ywmax*Yratio,-1.,1.);
  // repère
  g2x_GridDft(_GRID_);
  // fond d'écran
  if (g2x_BkgF!=NULL) glCallList(_BKGLIST_);
  /*!-----------------!*/

  glPushMatrix();
    g2x_Draw(); // APPEL A LA FONCTION DE DESSIN UTILISATEUR
  glPopMatrix();

  g2x_Info(_INFO_);
  glFlush();
  glutSwapBuffers();

  if (_VIDEO_ && !g2x_FilmFrame()) g2x_Quit();
}

static void g2x_MainDisplay(void)
{
  glutSetWindow(mainwin);
  glLoadIdentity();
  if (cornflag ) g2x_CornDisplay();
  if (xdialflag) g2x_XDialDisplay();
  if (ydialflag) g2x_YDialDisplay();
  g2x_DrawDisplay();
}

/*==================================
 OPERATION A REALISER AVANT DE QUITTER
  - fermer la fenetre graphique
  - liberer les diverses listes
  - fabrique le MPEG eventuel
  ==================================*/
extern void g2x_Quit(void)
{
  if (g2x_Exit) g2x_Exit();
  g2x_FreeKeyAction();
  g2x_FreeCtrlPrm();
  g2x_FreeButtons();
  g2x_FreePopUps();
  g2x_FreeSwitches();
  g2x_FreeScrolls();
  glutDestroyWindow(mainwin);

  switch (_VIDEO_){
    case 0 : break;
    case 1 : g2x_MakeMpeg4(); break;
    case 2 : g2x_MakeFlv();   break;
    case 3 : g2x_MakeAvi();   break;
    case 4 : g2x_MakeMpeg();  break;
    default: break;
  }
  g2x_UnplugCapture();
  exit(0);
}


/*=======================================================*/
/*=     GESTION DE LA FENETRE EN COORDONNES REELLES     =*/
/*!          SPECIFIQUE 2D - N'EXISTE PAS EN 3D         !*/
/*=======================================================*/
extern bool g2x_SetWindowCoord(double xmin, double ymin, double xmax, double ymax)
{
  if (xmin>=xmax || ymin>=ymax)
  { fprintf(stderr,"\e[0;31m\t<SetWindowCoord(xmin=%f,ymin=%f,xmax=%f,ymax=%f)> : Mauvaises coordonnees\e[0m\n"
                  ,xmin,ymin,xmax,ymax); return false;}

  /* limites initiales de la fenetre reelle */
  Xcmin = Xwmin = xmin;
  Xcmax = Xwmax = xmax;
  Ycmin = Ywmin = ymin;
  Ycmax = Ywmax = ymax;
  g2x_RescaleWindow();
  return true;
}

/*==============================================================================*/
/*= Fonction d'initialisation de la fenetre et mise en place des parametres Gl =*/
/*= windname : nom de la fenetre                                               =*/
/*= w, h : largeur/hauteur de la denetre (en pixel)                            =*/
/*==============================================================================*/
extern void g2x_InitWindow(char* windname, int w, int h)
{
  /* juste pour que <glutInit> soit content... */
  int   argc=1;
  char* argv[]={""};

  strncpy(_WINDNAME_,windname,127);
  /* initialisation des parametres gl */
  glutInit(&argc,argv);
  glutInitWindowSize(w,h);
  glutInitWindowPosition(0,0);
  glutInitDisplayMode(GLUT_RGBA|GLUT_DOUBLE);

  pixwidth = curwidth  = w;
  pixheight= curheight = h;
  /*! specifique 2D - zone graphique reelle par defaut !*/
  g2x_SetWindowCoord(-1.,-1.,+1.,+1.);

  // INITIALISATION DES TABLES TRIGO
  g2x_InitTrigonometric();

}



/*==================================*/
/* FONCTION PRINCIPALE              */
/*==================================*/
extern int g2x_MainStart(void)
{
  pixwidth = curwidth  = pixwidth -Ydialwidth;
  pixheight= curheight = pixheight-Xdialheight;

  if ((mainwin=glutCreateWindow(_WINDNAME_)) == GL_FALSE)
  { fprintf(stderr,"\e[0;31merreur creation de la fenetre\e[0m\n"); exit(1); }

  // CREATION DE LA FENETRE DE DESSIN
  if ((drawwin=glutCreateSubWindow(mainwin,0,0,curwidth,curheight)) == GL_FALSE)
  { fprintf(stderr,"\e[0;31merreur creation de la fenetre\e[0m\n"); exit(1); }

  /* fonctions principales */
  glutMouseFunc(g2x_DrawMouseClic);
  glutMotionFunc(g2x_DrawMouseMove);
  glutKeyboardFunc(g2x_Keyboard);
  glutSpecialFunc(g2x_ArrowKeys);
  glutPassiveMotionFunc(g2x_DrawPassiveMouseMove);
  glutReshapeFunc(g2x_DrawReshape);
  glutDisplayFunc(g2x_DrawDisplay);
  glEnable(GL_BLEND);
  glBlendFunc (GL_ONE_MINUS_SRC_ALPHA, GL_SRC_ALPHA);

  /* spécifique 2D */
  if (g2x_BkgF!=NULL)
  {
    _BKGLIST_=glGenLists(1);
    glNewList(_BKGLIST_,GL_COMPILE);
      g2x_BkgF();
    glEndList();
  }

  // APPEL A LA FONCTION DE DEMARRAGE (init)
  if (g2x_Init) g2x_Init();
  if (g2x_Ctrl) g2x_Ctrl();

  g2x_SubMenu();

  // CREATION DES FENETRES DE CONTROLE, SI NECESSAIRE
  if (_SCROLLH_)
  { // bandeau horizontal (si presence scrollbars horiz)
    xdialflag=true;
    if ((xdialwin=glutCreateSubWindow(mainwin,0,0,curwidth,Xdialheight)) == GL_FALSE)
    { fprintf(stderr,"\e[0;31merreur creation de la fenetre\e[0m\n"); exit(1); }
    glutReshapeFunc(g2x_XDialReshape);
    glutDisplayFunc(g2x_XDialDisplay);
    glutMouseFunc(g2x_XDialMouseClic);
    glutMotionFunc(g2x_XDialMouseMove);
    glEnable(GL_BLEND);
    glBlendFunc (GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
  }
  if (_SCROLLV_ || _SWITCH_ || _BUTTON_ || _POPUP_)
  { // bandeau vertical (si presence scrollbars vert. ou boutons)
    ydialflag=true;
    if ((ydialwin=glutCreateSubWindow(mainwin,0,0,Ydialwidth,curheight)) == GL_FALSE)
    { fprintf(stderr,"\e[0;31merreur creation de la fenetre\e[0m\n"); exit(1); }
    glutReshapeFunc(g2x_YDialReshape);
    glutDisplayFunc(g2x_YDialDisplay);
    glutMouseFunc(g2x_YDialMouseClic);
    glutMotionFunc(g2x_YDialMouseMove);
    glEnable(GL_BLEND);
    glBlendFunc (GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
    if (_SWITCH_)
    {
      g2x_InitSwitchOn();
      g2x_InitSwitchOff();
    }
    if (_BUTTON_)
    {
      g2x_InitButOn();
      g2x_InitButOff();
    }
    if (_POPUP_)
    {
      g2x_InitPopOn();
      g2x_InitPopOff();
    }
  }
  if (xdialflag && ydialflag)
  { // coin bas/gauche "?"
    cornflag=true;
    if ((cornwin=glutCreateSubWindow(mainwin,0,0,Ydialwidth,Xdialheight)) == GL_FALSE)
    { fprintf(stderr,"\e[0;31merreur creation de la fenetre\e[0m\n"); exit(1); }
    glutReshapeFunc(g2x_CornReshape);
    glutDisplayFunc(g2x_CornDisplay);
    glutMouseFunc(g2x_CornMouseClic);
    glEnable(GL_BLEND);
    glBlendFunc (GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
    g2x_InitCornSwitchOn();
    g2x_InitCornSwitchOff();
  }

  // FENETRE PRINCIPALE
  curwidth +=Ydialwidth;
  curheight+=Xdialheight;

  glutSetWindow(mainwin);
  glutDisplayFunc(g2x_MainDisplay);
  glutReshapeFunc(g2x_MainReshape);
  glutReshapeWindow(curwidth,curheight);

  /*= LANCEMENT BOUCLE PRINCIPALE OpenGl =*/
  glutMainLoop();
  return 1;
}


#ifdef __cplusplus
  }
#endif

