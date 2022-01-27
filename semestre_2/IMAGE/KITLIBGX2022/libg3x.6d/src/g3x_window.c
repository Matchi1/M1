/*!===============================================================
  E.Incerti - Universite Gustave Eiffel - eric.incerti@univ-eiffel.fr
       - Librairie G3X - Fonctions de base d'acces public -
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

#include <g3x.h>

#define BLOCSIZE 32
#define MAXBLOC  32

/* tailles par defaut de la fenetre gl */
#define DFT_PIXHEIGHT (GLint)512
#define DFT_PIXWIDTH  (GLint)512

/* tailles des bandeaux */
#define DFT_DIALWIDTH ((GLint)12)

static GLint   xdialwin,ydialwin,cornwin,drawwin,mainwin;
static bool    xdialflag=false, ydialflag=false, cornflag=false;
static char    _WINDNAME_[128]="";

/* tailles de la fenetre : dim. en pixels     */
static GLint     curheight =DFT_PIXHEIGHT; /* */
static GLint     pixheight =DFT_PIXHEIGHT; /* */
static GLint     curwidth  =DFT_PIXWIDTH;  /* */
static GLint     pixwidth  =DFT_PIXWIDTH;  /* */
static GLint     fullwidth,fullheight;

/* PARAMETRES DU BANDEAU LATERAL : position des boutons et scrollbars */
static GLint     Ydialwidth=0,Xdialheight=0;
static GLint     Xbutpos   =0,Ybutpos=12;
static GLint     Xbutw     =0;

/*= Couleur de fond =*/
static GLfloat G3X_BKG=0.0;
extern void    g3x_SetBkgCol(double bkg) { G3X_BKG=(GLfloat)bkg;  }
extern double  g3x_GetBkgCol(void      ) { return (double)G3X_BKG; }

/*static int       KeyMode=PARAM;*/
static GLuint   pushmode=GLUT_UP;
static GLint    moving;

static bool  _IDLE_       = false;
static bool  _RUN_        = false ;
static bool  _AUTODISPLAY_= true ;
static bool  _FULLSCREEN_ = false;
static bool  _INFO_       = false;
/* spécifique 3D */
static bool  _REPERE_     = false;
static bool  _FILL_       = true ;

/*= prototypes =*/
static void (*g3x_Init)(void)=NULL; /*! fonction d'initialisation !*/
static void (*g3x_Ctrl)(void)=NULL; /*! fonction de controle      !*/
static void (*g3x_Idle)(void)=NULL; /*! fonction d'animation      !*/
static void (*g3x_Draw)(void)=NULL; /*! fonction de dessin        !*/
static void (*g3x_Exit)(void)=NULL; /*! fonction de liberation    !*/

extern int  g3x_GetPixWidth(void) { return curwidth;  } /*! largeur courante  !*/
extern int  g3x_GetPixHeight(void){ return curheight; } /*! hauteur coutante  !*/


/*=======================================*/
/*= FONCTIONS DE GESTION DES EVENEMENTS =*/
/*=======================================*/
/*
extern bool g3x_Running(void)  { return _RUN_; } // obsolete
extern void g3x_Stop(void)     { _RUN_=false;  } // obsolete
extern void g3x_Continue(void) { _RUN_=true;   } // obsolete
extern void g3x_Show(void)     { glutPostWindowRedisplay(drawwin); } // obsolete
*/

/*= automatisation - rarement utilisee =*/
extern void g3x_SetAutoDisplay(bool flag) { _AUTODISPLAY_=flag; }
extern void g3x_ToggleAutoDisplay(void)   { _AUTODISPLAY_=!_AUTODISPLAY_; }
static int   _VIDEO_    = 0;
extern void g3x_AutoStartVideo(void)      { _RUN_=true; _VIDEO_=true; }
/*= snapshot attache a des touches ('j'/'p')  =*/
extern void g3x_AutoSnap_jpg(void) { g3x_Snapshot("jpg",_WINDNAME_,curwidth,curheight); }
extern void g3x_AutoSnap_png(void) { g3x_Snapshot("png",_WINDNAME_,curwidth,curheight); }

/*= - Fonction d'initialisation                                                      =*/
extern void g3x_SetInitFunction(void (*f)(void)) { g3x_Init=f; }
/*= - Fonction de contrôle                                                      =*/
extern void g3x_SetCtrlFunction(void (*f)(void)) { g3x_Ctrl=f; }
/*= - DrawFunc   : procedure d'affichage, associee a <glutPostRedisplay>             =*/
extern void g3x_SetDrawFunction(void (*f)(void)) { g3x_Draw = f; }
/*= - FreeFunc   : procedure de liberation de memoire, appelee a la sortie           =*/
extern void g3x_SetExitFunction(void (*f)(void)) { g3x_Exit = f; }
/*= - IdleFunc   : procedure de la boucle d'action Gl, passee a <glutIdleFunc>       =*/
extern void g3x_SetAnimFunction(void (*f)(void)) { g3x_Idle=f; _IDLE_=true; }

/* frequence d'affichage */
static int  _FAFF_=1;
/* par defaut : tous les pas de calcul sont affiches */
static void _idle_0_(void)
{
  g3x_Idle();
  glutPostWindowRedisplay(drawwin);
}

/* affichage temporise                               */
static void _idle_F_(void)
{
  static int t=0;
  g3x_Idle();
  if (t==_FAFF_) { glutPostWindowRedisplay(drawwin); t=0; }
  t++;
}
/* regle la frequence d'affichage et selctionne la bonne <idle func>  */
extern void g3x_SetRefreshFreq(int freq)
{
  if (_FAFF_==freq) return;
  _FAFF_=freq;
  glutIdleFunc((_IDLE_&&_RUN_)?(_FAFF_>1?_idle_F_:_idle_0_):NULL);
}

/*=================================================*/
/* Code C séparé - ne peut pas être compilé à part */
/* trop de dépendance aux variables globales       */
#include "g3x_camlight.c"  // Caméra & lumière (spécifique 3D)
#include "g3x_ctrlprm.c"   // Contrôle de paramètre (pavé fléché)
#include "g3x_keys.c"      // Gestion clavier
#include "g3x_popup.c"     // Boutons 'popup' (action unique)
#include "g3x_button.c"    // Boutons on/off
#include "g3x_switch.c"    // Switch (Boutons on/off exclusifs)
#include "g3x_scroll.c"    // ScrollBars
#include "g3x_mouse.c"     // Gestion souris
#include "g3x_print.c"     // Gestion texte

/*=================================================*/

/*====================================================================*/
/*=              trace des axes (spécifique 3D)                      =*/
/*====================================================================*/
// trace une flèche
extern void g3x_Arrow(GLfloat r, GLfloat h, G3Xcolor col)
{
  static GLUquadricObj *qobj=NULL;
  glPolygonMode(GL_FRONT_AND_BACK,GL_FILL);
  glPushMatrix();
    if (!qobj) qobj=gluNewQuadric();
    g3x_Material(col,0.2,0.8,0.,0.,0.);
    gluQuadricDrawStyle(qobj,GLU_FILL);
    gluCylinder(qobj,r,r,.8*h,8,1);
    glTranslatef(0.,0.,.8*h);
    glutSolidCone(4.*r,.2*h,4,1);
  glPopMatrix();
  glPolygonMode(_FILL_?GL_FRONT:GL_FRONT_AND_BACK,_FILL_?GL_FILL:GL_LINE);
}

// variante
extern void g3x_ArrowVect(G3Xpoint O, G3Xvector V, GLfloat r, GLfloat h, G3Xcolor col)
{
  static GLUquadricObj *qobj=NULL;
  if (!qobj) qobj=gluNewQuadric();
  glPolygonMode(GL_FRONT_AND_BACK,GL_FILL);
  double l,theta,phi,sinphi;
  l=g3x_Norm(V);
  phi=acos(V.z/l);
  sinphi=sin(phi);
  theta=(G3Xiszero(sinphi)?0.:(V.y>0?+1.:-1.)*acos(V.x/(l*sinphi)));
  glPushMatrix();
    glTranslatef(O.x,O.y,O.z);
    glPushMatrix();
      glRotated(RadToDeg*theta,0.,0.,1.);
      glPushMatrix();
        glRotated(RadToDeg*phi,0.,1.,0.);
        glPushMatrix();
          g3x_Materialfv(GL_FRONT_AND_BACK,GL_AMBIENT,(G3Xcolor){0.1, 0.4, 0.0, 1.0});
          col.a=1.;
          g3x_Materialfv(GL_FRONT_AND_BACK,GL_DIFFUSE,col);
          glMaterialf(GL_FRONT_AND_BACK,GL_SPECULAR,0.0);
          gluQuadricDrawStyle(qobj,GLU_FILL);
          gluCylinder(qobj,r,r,.8*h,8,2);
          glTranslatef(0.,0.,.8*h);
          glutSolidCone(4.*r,.2*h,10,1);
        glPopMatrix();
      glPopMatrix();
    glPopMatrix();
  glPopMatrix();
  glPolygonMode(GL_FRONT_AND_BACK,_FILL_?GL_FILL:GL_LINE);
}

/*= affichage du repere =*/
static __inline__ void g3x_Repere(bool flag)
{
  if (!flag) return;

  static G3Xcolor xcol=(G3Xcolor){0.6,0.0,0.0,1.0};
  static G3Xcolor ycol =(G3Xcolor){0.0,0.6,0.0,1.0};
  static G3Xcolor zcol =(G3Xcolor){0.0,0.0,0.6,1.0};
  glEnable(GL_LIGHTING);
  glPushMatrix();
    g3x_Arrow(0.025,1.0,zcol); /*axe z (bleu) */
    glPushMatrix();
      glRotatef(-90.,1.,0.,0.);
      g3x_Arrow(0.025,1.0,ycol); /* axe y (vert) */
    glPopMatrix();
    glPushMatrix();
      glRotatef(+90.,0.,1.,0.);
      g3x_Arrow(0.025,1.0,xcol); /* axe x (rouge) */
    glPopMatrix();
  glPopMatrix();
}

/*==================================*/
/*=  RESHAPE                       =*/
/*==================================*/
static __inline__ void g3x_CornReshape(int w, int h)
{
  glViewport (0, 0, w, h);
}

static __inline__ void g3x_XDialReshape(int w, int h)
{
  glViewport (0, 0, w, h);
  if (_SCROLLH_) g3x_SetUpScrollh();
}

static __inline__ void g3x_YDialReshape(int w, int h)
{
  glViewport (0, 0, w, h);
  if (_SCROLLV_) g3x_SetUpScrollv();
}

static __inline__ void g3x_DrawReshape(int w, int h)
{
  glViewport (0, 0, w, h);
  glMatrixMode(GL_PROJECTION);
  glLoadIdentity();
  gluPerspective(_near,(GLfloat)w/(GLfloat)h,_open,_far );
}

static __inline__ void g3x_MainReshape(int w, int h)
{
  glViewport(0, 0, w, h);
  curwidth  = w-Ydialwidth;
  curheight = h-Xdialheight;

  if (cornflag)
  {
    glutSetWindow(cornwin);
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
    glutSetWindow(ydialwin);
    glutReshapeWindow(Ydialwidth,curheight);
    glutPositionWindow(curwidth,0);
    glutShowWindow();
  }
  glutSetWindow(drawwin);
  glutReshapeWindow(curwidth,curheight);
  glutPositionWindow(0,0);
  glutShowWindow();
}



/*====================================================================*/
/*=                     panneau d'information                        =*/
/*====================================================================*/
static void g3x_Info(bool flag)
{
  if (!flag) return;
  static char msg[128];
  static int x,y,usery;

  glDisable(GL_LIGHTING);
  glDisable(GL_DEPTH_TEST);

  glMatrixMode(GL_PROJECTION);
  glLoadIdentity();
  glOrtho(0,curwidth,0,curheight,-1.0,1.0);
  glMatrixMode(GL_MODELVIEW);
  glLoadIdentity();

  glEnable(GL_BLEND);
  glBlendFunc(GL_SRC_ALPHA,GL_ONE_MINUS_SRC_ALPHA);
  glColor4f(0.5,0.5,0.5,0.5);
  glRecti(10,10,curwidth-10,curheight-10);
  glDisable(GL_BLEND);

  x=20;
  y=curheight-16;
  g3x_SelectFont(12);
  y-=16; g3x_Print(x,y,G3Xw,'l',"Librairie G3X - version 6d");
  g3x_SelectFont(12);
  y-=16; g3x_Print(x,y,G3Xw,'l',"_______________ACTIONS PREDEFINIES_______________");
  y-=16; g3x_Print(x,y,G3Xw,'l',"<ESC>,<Ctrl+q> : quitte le programme proprement      ");
  y-=16; g3x_Print(x,y,G3Xw,'l',"<?> : affiche cette fenetre d'aide                   ");
  y-=16; g3x_Print(x,y,G3Xw,'l',"<Ctrl+f> : plein ecran                               ");
  y-=16; g3x_Print(x,y,G3Xw,'l',"<Ctrl+r> : affiche le repere principal               ");
  y-=16; g3x_Print(x,y,G3Xw,'l',"<Ctrl+w> : inverse la couleur de fond                ");
  y-=16; g3x_Print(x,y,G3Xw,'l',"<Ctrl+j> : snapshot format JPG                       ");
  y-=16; g3x_Print(x,y,G3Xw,'l',"<Ctrl+p> : snapshot format PNG                       ");
  y-=16; g3x_Print(x,y,G3Xw,'l',"<x/X,y/Y,z/Z> : deplacement (-/+) du spot            ");
  if (_IDLE_)
{ y-=16; g3x_Print(x,y,G3Xw,'l',"<SPACE> : stoppe/relance l'animation                 "); }
  y-=16;

  usery = y;
  if (_SCROLLH_ || _SCROLLV_ || _BUTTON_ || _SWITCH_)
  {
    y-=16; g3x_Print(x,y,G3Xg,'l',"_________CONTROLE UTILISATEUR___________");
    G3Xscroll *scroll=_SCROLLH_;
    while (scroll<_SCROLLH_+scrollhnum)
    {
      y-=16;
      g3x_Print(x,y,G3Xc,'l',"Scroll H. <%s> :\t\t%s",scroll->name,strlen(scroll->info)>0?scroll->info:"non renseigne");
      scroll++;
    }
    scroll=_SCROLLV_;
    while (scroll<_SCROLLV_+scrollvnum)
    {
      y-=16;
      g3x_Print(x,y,G3Xc,'l',"Scroll V. <%s> :\t\t%s",scroll->name,strlen(scroll->info)>0?scroll->info:"non renseigne");
      scroll++;
    }
    G3Xbut *but=_BUTTON_;
    while (but<_BUTTON_+butnum)
    {
      y-=16;
      g3x_Print(x,y,G3Xc,'l',"Button  <%s> :\t\t%s" ,but->name,strlen(but->info)>0?but->info:"non renseigne");
      but++;
    }
    G3Xpopup *pop=_POPUP_;
    while (pop<_POPUP_+popnum)
    {
      y-=16;
      g3x_Print(x,y,G3Xc,'l',"PopUp  <%s> :\t\t%s" ,pop->name,strlen(pop->info)>0?pop->info:"non renseigne");
      pop++;
    }
    G3Xswitch *swt=_SWITCH_;
    while (swt<_SWITCH_+switchnum)
    {
      y-=16;
      g3x_Print(x,y,G3Xc,'l',"Switch  <%s> :\t\t%s",swt->name,strlen(swt->info)>0?swt->info:"non renseigne");
      swt++;
    }
  }
  x = 20 + curwidth/2;
  y=curheight-16;
  y-=16;
  y-=16; g3x_Print(x,y,G3Xw,'l',"_________________CAMERA_________________");
  y-=16; g3x_Print(x,y,G3Xw,'l',"coord. cart.   : [%+.3f,%+.3f,%+.3f]",cam.pos->x,cam.pos->y,cam.pos->z);
  y-=16; g3x_Print(x,y,G3Xw,'l',"coord. spher.: [%+.3f,%+.3f,%+.3f]",cam.theta ,cam.phi,cam.dist);
  y-=16;
  y-=16; g3x_Print(x,y,G3Xw,'l',"_________________LUMIERE_________________");
  y-=16; g3x_Print(x,y,G3Xw,'l',"coord. cart.   : [%+.3f,%+.3f,%+.3f]",spot.pos.x,spot.pos.y,spot.pos.z);
  y-=16; g3x_Print(x,y,G3Xw,'l',"coord. spher.: [%+.3f,%+.3f,%+.3f]",spot.theta,spot.phi  ,spot.dist );
  y-=16;

  y = usery;
  if (keynumb>0)
  {
    y-=16; g3x_Print(x,y,G3Xg,'l',"_________ACTIONS UTILISATEUR___________");
    G3Xkey *k;
    for (k=_KEY_; k<_KEY_+keynumb; k++)
    {
      y-=16; g3x_Print(x,y,G3Xc,'l',"<%c> : %s",k->key,k->info);
    }
  }

  glEnable(GL_LIGHTING);
  glEnable(GL_DEPTH_TEST);
  g3x_DrawReshape(curwidth,curheight);
}


/*==================================*/
/*=  DISPLAY                       =*/
/*==================================*/
static __inline__ void g3x_CornDisplay(void)
{
  glutSetWindow (cornwin);
  glLoadIdentity();
  gluOrtho2D (0.,(double)Ydialwidth,0.,(double)Xdialheight);
  glClearColor(.75,.80,.85,1.);
  glClear(GL_COLOR_BUFFER_BIT);
  g3x_DrawInfoSwitch();
  glutSwapBuffers();
}

static __inline__ void g3x_XDialDisplay(void)
{
  glutSetWindow (xdialwin);
  glLoadIdentity();
  gluOrtho2D (0.,(double)curwidth,0.,(double)Xdialheight);
  glClearColor(.75,.80,.85,1.);
  glClear(GL_COLOR_BUFFER_BIT);
  G3Xscroll *scroll=_SCROLLH_;
  while (scroll<_SCROLLH_+scrollhnum)
  {
    g3x_DrawScrollh(scroll);
    scroll++;
  }
  glutSwapBuffers();
}

static __inline__ void g3x_YDialDisplay(void)
{
  glutSetWindow (ydialwin);
  glLoadIdentity();
  gluOrtho2D (0.,(double)Ydialwidth,0.,(double)curheight);
  glClearColor(.75,.80,.85,1.);
  glClear(GL_COLOR_BUFFER_BIT);
  if (_BUTTON_) g3x_DrawButtons();
  if (_POPUP_ ) g3x_DrawPopUps();
  if (_SWITCH_) g3x_DrawSwitches();
  G3Xscroll *scroll=_SCROLLV_;
  while (scroll<_SCROLLV_+scrollvnum)
  {
    g3x_DrawScrollv(scroll);
    scroll++;
  }
  glutSwapBuffers();
}


static __inline__ void g3x_DrawDisplay(void)
{
  /* patch dégeulasse pour éviter 4 appels inutiles à g3x_DrawDisplay() */
  /* ... pas réussi à identifier l'origine de ces appels                */
  /* ... en 2D, il faut 5 passages                                      */
  static ulint i=0;
  if ((++i<4)) return;

  glutSetWindow (drawwin);
  glClearColor(G3X_BKG,G3X_BKG,G3X_BKG,1.);
  glClear(GL_COLOR_BUFFER_BIT|GL_DEPTH_BUFFER_BIT);
  glMatrixMode(GL_MODELVIEW);
  glLoadIdentity();
  /*! sp&cifique 3D !*/
  // caméra
  gluLookAt(cam.pos->x,cam.pos->y,cam.pos->z,
            cam.tar->x,cam.tar->y,cam.tar->z,
            cam.upv .x,cam.upv .y,cam.upv .z);
  glEnable(GL_LIGHTING);
  // mode plein ou fil de fer
  glPolygonMode(GL_FRONT_AND_BACK,_FILL_?GL_FILL:GL_LINE);
  // lumière
  GLfloat _POS[4]={spot.pos.x,spot.pos.y,spot.pos.z,1. };
  glLightfv(GL_LIGHT0,GL_POSITION,_POS);

  // repère
  g3x_Repere(_REPERE_);
  /*!-----------------!*/

  glPushMatrix();
    g3x_Draw(); // APPEL A LA FONCTION DE DESSIN UTILISATEUR
  glPopMatrix();

  g3x_Info(_INFO_);
  glFlush();
  glutSwapBuffers();

  if (_VIDEO_ && !g3x_FilmFrame()) g3x_Quit();
}

static __inline__ void g3x_MainDisplay(void)
{
  glutSetWindow(mainwin);
  glLoadIdentity();
  if (cornflag ) g3x_CornDisplay();
  if (xdialflag) g3x_XDialDisplay();
  if (ydialflag) g3x_YDialDisplay();
  g3x_DrawDisplay();

}

/*==================================
 OPERATION A REALISER AVANT DE QUITTER
  - fermer la fenetre graphique
  - liberer les diverses listes
  - fabrique le MPEG eventuel
  ==================================*/
extern void g3x_Quit(void)
{
  if (g3x_Exit) g3x_Exit();
  g3x_FreeKeyAction();
  g3x_FreeCtrlPrm();
  g3x_FreeButtons();
  g3x_FreePopUps();
  g3x_FreeSwitches();
  g3x_FreeScrolls();
  glutDestroyWindow(mainwin);

  switch (_VIDEO_){
    case 0 : break;
    case 1 : g3x_MakeMpeg4(); break;
    case 2 : g3x_MakeFlv();   break;
    case 3 : g3x_MakeAvi();   break;
    case 4 : g3x_MakeMpeg();  break;
    default: break;
  }
  g3x_UnplugCapture();
  exit(0);
}

/*==============================================================================*/
/*= Fonction d'initialisation de la fenetre et mise en place des parametres Gl =*/
/*= windname : nom de la fenetre                                               =*/
/*= w, h : largeur/hauteur de la denetre (en pixel)                            =*/
/*==============================================================================*/
extern void g3x_InitWindow(char* windname, int w, int h)
{
  /* juste pour que <glutInit> soit content... */
  int   argc=1;
  char* argv[]={""};

  strncpy(_WINDNAME_,windname,127);
  /* initialisation des parametres gl */
  glutInit(&argc,argv);
  glutInitWindowSize(w,h);
  glutInitWindowPosition(0,0);
  glutInitDisplayMode(GLUT_RGBA|GLUT_DOUBLE|GLUT_DEPTH);

  pixwidth = curwidth  = w;
  pixheight= curheight = h;

  /* parametres camera */
  cam.pos   = &cam_default_pos;
  cam.tar   = &cam_default_tar;
  cam.upv.x= 0.;
  cam.upv.y= 0.;
  cam.upv.z= 1.;
  cam.theta = 0.25*PI;
  cam.phi   = 0.25*PI;
  cam.dist  = 10.;

  /* parametres lumiere */
  spot.theta= 0.25*PI;
  spot.phi  = 0.25*PI;
  spot.dist = 10.;
  g3x_SetSpotSteps(0.125,0.125,0.125);
  g3x_SetLightSpheric(0.25*PI,+0.25*PI,10.);
  g3x_SetLightAmbient (1.,1.,1.);
  g3x_SetLightDiffuse (1.,1.,1.);
  g3x_SetLightSpecular(1.,1.,1.);

  // INITIALISATION DES TABLES TRIGO
  g3x_InitTrigonometric();
}



/*==================================
  ==================================*/
extern int g3x_MainStart(void)
{
  pixwidth = curwidth  = pixwidth -Ydialwidth;
  pixheight= curheight = pixheight-Xdialheight;

  if ((mainwin=glutCreateWindow(_WINDNAME_)) == GL_FALSE)
  { fprintf(stderr,"\e[0;31merreur creation de la fenetre\e[0m\n"); exit(1); }

  // CREATION DE LA FENETRE DE DESSIN
  if ((drawwin=glutCreateSubWindow(mainwin,0,0,curwidth,curheight)) == GL_FALSE)
  { fprintf(stderr,"\e[0;31merreur creation de la fenetre\e[0m\n"); exit(1); }

  /* fonctions principales */
  glutMouseFunc(g3x_DrawMouseClic);
  glutMotionFunc(g3x_DrawMouseMove);
  glutKeyboardFunc(g3x_Keyboard);
  glutSpecialFunc(g3x_ArrowKeys);
  glutPassiveMotionFunc(g3x_DrawPassiveMouseMove);
  glutReshapeFunc(g3x_DrawReshape);
  glutDisplayFunc(g3x_DrawDisplay);

  /* initialisation des parametres 3D */
  glutInitDisplayMode(GLUT_RGBA|GLUT_DOUBLE|GLUT_DEPTH);
  glClearColor(0.,0.,0.,0.);
  glEnable(GL_LIGHT0);
  glEnable(GL_DEPTH_TEST);
  glEnable(GL_NORMALIZE);

  g3x_SetLightSpheric(spot.theta, spot.phi, spot.dist);
  GLfloat _POS[4]={spot.pos.x,spot.pos.y,spot.pos.z,1. };
  glLightfv(GL_LIGHT0,GL_POSITION      ,_POS);
  glLightfv(GL_LIGHT0,GL_AMBIENT       ,(GLfloat*)&spot.ambi);
  glLightfv(GL_LIGHT0,GL_DIFFUSE       ,(GLfloat*)&spot.diff);
  glLightfv(GL_LIGHT0,GL_SPECULAR      ,(GLfloat*)&spot.spec);

  glMaterialfv(GL_FRONT_AND_BACK,GL_AMBIENT  ,(GLfloat*)&spot.ambi);
  glMaterialfv(GL_FRONT_AND_BACK,GL_DIFFUSE  ,(GLfloat*)&spot.diff);
  glMaterialfv(GL_FRONT_AND_BACK,GL_SPECULAR ,(GLfloat*)&spot.spec);
  glMaterialf (GL_FRONT_AND_BACK,GL_SHININESS,0.);

  // APPEL A LA FONCTION DE DEMARRAGE (init)
  if (g3x_Init) g3x_Init();
  if (g3x_Ctrl) g3x_Ctrl();

  g3x_SubMenu();

  // CREATION DES FENETRES DE CONTROLE, SI NECESSAIRE
  if (_SCROLLH_)
  { /* bandeau horizontal (si presence scrollbars horiz)            */
    xdialflag=true;
    if ((xdialwin=glutCreateSubWindow(mainwin,0,0,curwidth,Xdialheight)) == GL_FALSE)
    { fprintf(stderr,"\e[0;31merreur creation de la fenetre\e[0m\n"); exit(1); }
    glutReshapeFunc(g3x_XDialReshape);
    glutDisplayFunc(g3x_XDialDisplay);
    glutMouseFunc(g3x_XDialMouseClic);
    glutMotionFunc(g3x_XDialMouseMove);
    glEnable(GL_BLEND);
    glBlendFunc (GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
  }
  if (_SCROLLV_ || _SWITCH_ || _BUTTON_ || _POPUP_)
  { /* bandeau vertical (si presence scrollbars vert. ou boutons) */
    ydialflag=true;
    if ((ydialwin=glutCreateSubWindow(mainwin,0,0,Ydialwidth,curheight)) == GL_FALSE)
    { fprintf(stderr,"\e[0;31merreur creation de la fenetre\e[0m\n"); exit(1); }
    glutReshapeFunc(g3x_YDialReshape);
    glutDisplayFunc(g3x_YDialDisplay);
    glutMouseFunc(g3x_YDialMouseClic);
    glutMotionFunc(g3x_YDialMouseMove);
    glEnable(GL_BLEND);
    glBlendFunc (GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
    if (_SWITCH_)
    {
      g3x_InitSwitchOn();
      g3x_InitSwitchOff();
    }
    if (_BUTTON_)
    {
      g3x_InitButOn();
      g3x_InitButOff();
    }
    if (_POPUP_)
    {
      g3x_InitPopOn();
      g3x_InitPopOff();
    }
  }
  if (xdialflag && ydialflag)
  { /* coin bas/gauche "?"                                          */
    cornflag=true;
    if ((cornwin=glutCreateSubWindow(mainwin,0,0,Ydialwidth,Xdialheight)) == GL_FALSE)
    { fprintf(stderr,"\e[0;31merreur creation de la fenetre\e[0m\n"); exit(1); }
    glutReshapeFunc(g3x_CornReshape);
    glutDisplayFunc(g3x_CornDisplay);
    glutMouseFunc(g3x_CornMouseClic);
    glEnable(GL_BLEND);
    glBlendFunc (GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
    g3x_InitCornSwitchOn();
    g3x_InitCornSwitchOff();
  }

  // FENETRE PRINCIPALE
  curwidth +=Ydialwidth;
  curheight+=Xdialheight;

  glutSetWindow(mainwin);
  glutDisplayFunc(g3x_MainDisplay);
  glutReshapeFunc(g3x_MainReshape);
  glutReshapeWindow(curwidth,curheight);

  glPolygonMode((_FILL_?GL_FRONT:GL_FRONT_AND_BACK),(_FILL_?GL_FILL:GL_LINE));

  /*= LANCEMENT BOUCLE PRINCIPALE OpenGl =*/
  glutMainLoop();
  return 1;
}

#ifdef __cplusplus
  }
#endif

