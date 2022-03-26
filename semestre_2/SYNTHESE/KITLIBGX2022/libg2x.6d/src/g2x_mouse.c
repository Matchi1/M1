/*!===============================================================
  E.Incerti - Universite Gustave Eiffel - eric.incerti@univ-eiffel.fr
       - Librairie G2X - Fonctions de base d'acces public -
                    version 6d - Jan.2022
  ================================================================!*/

/*====================================================================*/
/*=      Gestion des 'clic' souris                                   =*/
/*====================================================================*/
static bool     LEFT_CLIC = false;
static G2Xpoint _CLIC_POS_  = { 0, 0 };
static G2Xpoint _MOUSE_POS_ = { 0, 0 };
static GLint startx,starty;

static void (*MouseMoveAction)(G2Xpoint) = NULL;

extern G2Xpoint* g2x_GetClic(void)
{
 if (!LEFT_CLIC) return NULL;
 LEFT_CLIC = false;
 return &_CLIC_POS_;
}

static __inline__ void g2x_CornMouseClic(int button, int state, int x, int y)
{
  glutSetWindow(cornwin);
  if (button!=GLUT_LEFT_BUTTON) return;
  if (state == GLUT_UP)
  {
    pushmode=GLUT_UP;
    moving=false;
    return;
  }
  if (pushmode!=GLUT_UP) return;
  _INFO_ =!_INFO_ ;
  glutPostRedisplay();
  glutShowWindow();
  glutSetWindow(mainwin);
  glutShowWindow();
  return;
}

static __inline__ void g2x_XDialMouseClic(int button, int state, int x, int y)
{
  glutSetWindow(xdialwin);
  if (button!=GLUT_LEFT_BUTTON) return;
  if (state == GLUT_UP)
  {
    pushmode=GLUT_UP;
    moving=false;
    return;
  }
  if (pushmode!=GLUT_UP) return;
  /*! clic Scrollbar!*/
  G2Xscroll *scroll=_SCROLLH_;
  y = Xdialheight-y;
  while (scroll<_SCROLLH_+scrollhnum && pushmode==GLUT_UP)
  {
    if (abs((int)scroll->ycurs-y)<scroll->w && x>(Xscrollstart-2*scroll->w) && x<(Xscrollend  +2*scroll->w))
    {
      moving = true+button;
      pushmode=GLUT_DOWN;
      TheScrollh=scroll;
      TheScrollh->xcurs  = CLIP(Xscrollstart,x,Xscrollend);
      TheScrollh->cursor = (double)(TheScrollh->xcurs-Xscrollstart)/(double)Xscrollrange;
      if (TheScrollh->dprm)
         *TheScrollh->dprm   = (double)(TheScrollh->min + pow(TheScrollh->cursor,TheScrollh->fac)*(TheScrollh->max-TheScrollh->min));
      else
      if (TheScrollh->iprm)
         *TheScrollh->iprm   = (   int)(TheScrollh->min + pow(TheScrollh->cursor,TheScrollh->fac)*(TheScrollh->max-TheScrollh->min));
      glutPostRedisplay();
      glutShowWindow();
      glutSetWindow(mainwin);
      glutShowWindow();
      return;
    }
    scroll++;
  }
  return;
}

static __inline__ void g2x_YDialMouseClic(int button, int state, int x, int y)
{
  glutSetWindow(ydialwin);
  startx = x;
  starty = y;
  if (button==GLUT_MIDDLE_BUTTON) { moving=true+button; return; }
  if (button!=GLUT_LEFT_BUTTON  ) return;

  if (state == GLUT_UP)
  {
    pushmode=GLUT_UP;
    moving=false;
    return;
  }
  if (pushmode!=GLUT_UP) return;
  /*! clic boutons!*/
  if (g2x_SelectPopUp(x,y) || g2x_SelectSwitch(x,y) || g2x_SelectButton(x,y))
  {
    if (ThePopUp && ThePopUp->on)
    {
      ThePopUp->action();
      ThePopUp->on=false;
    }
    ThePopUp =NULL;
    ThePopUp=NULL;
    glutPostRedisplay();
    glutShowWindow(); glutSetWindow(mainwin); glutShowWindow();
    return;
  }
  /*! clic Scrollbar!*/
  y = curheight-y;
  G2Xscroll *scroll=_SCROLLV_;
  while (scroll<_SCROLLV_+scrollvnum && pushmode==GLUT_UP)
  {
    if (abs((int)scroll->xcurs-x)<scroll->w && y>(Yscrollstart-2*scroll->w)
                                            && y<(Yscrollend  +2*scroll->w) )
    {
      moving = true+button;
      pushmode=GLUT_DOWN;
      TheScrollv=scroll;
      TheScrollv->ycurs = CLIP(Yscrollstart,y,Yscrollend);
      TheScrollv->cursor= (double)(TheScrollv->ycurs-Yscrollstart)/(double)Yscrollrange;
      if (TheScrollv->dprm)
         *TheScrollv->dprm   = (double)(TheScrollv->min + pow(TheScrollv->cursor,TheScrollv->fac)*(TheScrollv->max-TheScrollv->min));
      else
      if (TheScrollv->iprm)
         *TheScrollv->iprm   = (   int)(TheScrollv->min + pow(TheScrollv->cursor,TheScrollv->fac)*(TheScrollv->max-TheScrollv->min));
      glutPostRedisplay();
      glutShowWindow(); glutSetWindow(mainwin); glutShowWindow();
        return;
    }
    scroll++;
  }
  return;
}

static __inline__ void g2x_DrawMouseClic(int button, int state, int x, int y)
{
  glutSetWindow(drawwin);
  startx = x;
  starty = y;
  if (button==GLUT_MIDDLE_BUTTON) { moving=true+button; return; }
  if (button!=GLUT_LEFT_BUTTON) return;
  if (state == GLUT_UP)
  {
    pushmode=GLUT_UP;
    moving=false;
    CPT = NULL;
    return;
  }
  if (pushmode!=GLUT_UP) return;

  y = curheight - y;
  _CLIC_POS_.x= G2XpixtowinX1(x);
  _CLIC_POS_.y= G2XpixtowinY1(y);

  /* point de controle ? */
  int    n=0;
  while (n<nbcpt && pushmode==GLUT_UP)
  {
    if (fabs(CTRLPTS[n].add->x -_CLIC_POS_.x)<=fabs(CTRLPTS[n].ray*Xpixsize/Xratio) &&
        fabs(CTRLPTS[n].add->y -_CLIC_POS_.y)<=fabs(CTRLPTS[n].ray*Ypixsize/Yratio)  )
    {
      CPT=CTRLPTS[n].add;
      CPTpos=*CPT;
      pushmode=GLUT_DOWN;
      moving = true+button;
      return;
    }
    n++;
  }
  return;
}

/*====================================================================*/
/*=  Gestion des mouvements de la souris  (clic gauche)              =*/
/*====================================================================*/
static __inline__ void g2x_DrawMouseMove(int x, int y)
{
  glutSetWindow(drawwin);
  switch (moving)
  {
    case true+(GLUT_LEFT_BUTTON)  : // les points de controle
      y = curheight - y;
      if (nbcpt!=0)
      {
          CPT->x = G2XpixtowinX1(x);
          CPT->y = G2XpixtowinY1(y);
          CPT->x = CLIP(Xcmin,CPT->x,Xcmax);
          CPT->y = CLIP(Ycmin,CPT->y,Ycmax);
      }
      glutPostWindowRedisplay(drawwin);
      break;
    case true+GLUT_MIDDLE_BUTTON   : // le zoom
      zoom -= 0.001*(y-starty);
      zoom = MAX(zoom,0.01);
      glutPostWindowRedisplay(drawwin);
      break;
  }
}

/*= recupere la position de la souris =*/
extern G2Xpoint g2x_GetMousePosition(void) { return _CLIC_POS_; }
extern void     g2x_SetMouseMoveAction(void (*action)(G2Xpoint))
{ MouseMoveAction = action;}

/*=   PASSIVE MOUSE FUNCTIONS =*/
static __inline__ void g2x_DrawPassiveMouseMove(int x, int y)
{
  glutSetWindow(drawwin);
  y = curheight - y;
  _MOUSE_POS_.x = G2XpixtowinX1(x);
  _MOUSE_POS_.y = G2XpixtowinY1(y);
  if(MouseMoveAction) (*MouseMoveAction)(_MOUSE_POS_);
}

/*=   MOVE MOUSE FUNCTIONS =*/
static __inline__ void g2x_XDialMouseMove(int x, int y)
{
  glutSetWindow(xdialwin);
  y = curheight - y;
  switch (moving)
  {
    case true+GLUT_LEFT_BUTTON   :
      if (TheScrollh)
      {
        TheScrollh->xcurs = CLIP(Xscrollstart,x,Xscrollend);
        TheScrollh->cursor= (double)(TheScrollh->xcurs-Xscrollstart)/(double)Xscrollrange;
        if (TheScrollh->dprm)
           *TheScrollh->dprm   = (double)(TheScrollh->min + TheScrollh->cursor*(TheScrollh->max-TheScrollh->min));
        else
        if (TheScrollh->iprm)
           *TheScrollh->iprm   = (   int)(TheScrollh->min + TheScrollh->cursor*(TheScrollh->max-TheScrollh->min));
      }
      glutPostRedisplay();
      glutPostWindowRedisplay(drawwin);
  }
}

static __inline__ void g2x_YDialMouseMove(int x, int y)
{
  glutSetWindow(ydialwin);
  y = curheight - y;
  switch (moving)
  {
    case true+GLUT_LEFT_BUTTON   :
      if (TheScrollv)
      {
        TheScrollv->ycurs = CLIP(Yscrollstart,y,Yscrollend);
        TheScrollv->cursor= (double)(TheScrollv->ycurs-Yscrollstart)/(double)Yscrollrange;
        if (TheScrollv->dprm)
           *TheScrollv->dprm   = (double)(TheScrollv->min + TheScrollv->cursor*(TheScrollv->max-TheScrollv->min));
        else
        if (TheScrollv->iprm)
           *TheScrollv->iprm   = (   int)(TheScrollv->min + TheScrollv->cursor*(TheScrollv->max-TheScrollv->min));
      }
      glutPostRedisplay();
      glutPostWindowRedisplay(drawwin);
  }
}

/*====================================================================*/
/*=  fonction associee aux evenements de menu.                       =*/
/*=  - item : code associe au menu selectionne                       =*/
/*====================================================================*/
#define _MENU_CTRL_ 100
#define _MENU_CLIC_ 200
#define _MENU_FORM_ 300
#define _MENU_MPEG_ 400
#define _MENU_EXIT_ 500

/*====================================================================*/
/*=  Gestion des menus de la souris (clic droit)                     =*/
/*====================================================================*/
static const char* _RIGHT_BUT_[] = {"cam","light",NULL};
static const char* _IMG_CODEC_[] = {"jpg","pnm","png","gif","eps","bmp","tif","ras",NULL};
static const char* _VID_CODEC_[] = {"mp4","flv","x264","mpg2",NULL};

extern G2Xprm *_PRM_,  /* liste des parametres*/
              *ThePrm; /* parametre courant   */
extern int     prmnum;

static void g2x_MainMenu(int item)
{
  if (prmnum && item<_MENU_CLIC_) /* les parametres utilisateur */
  {
    item-=_MENU_CTRL_;
    ThePrm=_PRM_;
     while (item--) ThePrm++;
    return;
  }
  if (item<_MENU_MPEG_)            /* choix format snapshot */
  {
    const char **f=_IMG_CODEC_;
    item-=_MENU_FORM_;
    while (item--) f++;
    /*if (!_FULLSCREEN_)*/ g2x_Snapshot(*f,_WINDNAME_,curwidth,curheight);
    return;
  }
  if (item <_MENU_EXIT_)           /* choix format video    */
  {
    const char **f=_VID_CODEC_;
    item-=_MENU_MPEG_;
    _VIDEO_++;
    while (item--) { f++; _VIDEO_++; }
    g2x_PlugCapture(_WINDNAME_,0,0,curwidth,curheight);
    return;
  }
  if (item==_MENU_EXIT_) return g2x_Quit(); /* exit */
}

static void g2x_SubMenu(void)
{
  /* CONSTRUCTION DU MENU SOURIS */
  const char **f;
  int   mp=_MENU_CTRL_ ,submenup,p;
  int   mm=_MENU_CLIC_ ,submenum;
  int   mf=_MENU_FORM_ ,submenuf;
  int   mv=_MENU_MPEG_ ,submenuv;
  submenup=glutCreateMenu(g2x_MainMenu);
  p=0;
  ThePrm=_PRM_;
  while (p<prmnum) glutAddMenuEntry(_PRM_[p++].nom,mp++);

  /* fonctionalites clic droit */
  submenum=glutCreateMenu(g2x_MainMenu);
  f=_RIGHT_BUT_;
  while (*f) glutAddMenuEntry(*f++,mm++);
  /* choix format et snapshot        */
  submenuf=glutCreateMenu(g2x_MainMenu);
  f=_IMG_CODEC_;
  while (*f) glutAddMenuEntry(*f++,mf++);
  /* choix format et lancement video */
  if (_IDLE_==true)
  {
    submenuv=glutCreateMenu(g2x_MainMenu);
    f=_VID_CODEC_;
    while (*f) glutAddMenuEntry(*f++,mv++);
  }

  glutCreateMenu(g2x_MainMenu);
  if (prmnum) glutAddSubMenu("param",submenup);
  glutAddSubMenu("photo",submenuf);
  if (g2x_Idle)  glutAddSubMenu("video",submenuv);
  glutAddMenuEntry("exit ",_MENU_EXIT_);
  glutAttachMenu(GLUT_RIGHT_BUTTON);
}
