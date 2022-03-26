/*!===============================================================
  E.Incerti - Universite Gustave Eiffel - eric.incerti@univ-eiffel.fr
       - Librairie G2X - Fonctions de base d'acces public -
                    version 6d - Jan.2022
  ================================================================!*/


/*===============================================*/
/*=  ASSOCIER DES FONCTIONALITES A DES TOUCHES  =*/
/*===============================================*/
static int    keynumb=0;
static int    keyblocnumb=0;
static G2Xkey* _KEY_=NULL;

/*= cree une G2Xkey =*/
extern bool g2x_SetKeyAction(char key, void (*action)(void), const char *info)
{
  if (keyblocnumb==MAXBLOC)
  { fprintf(stderr,"\e[1;31m<glx_SetKeyAction>\e[0;31m %d actions : maximum atteint\e[0;31m\n",MAXBLOC*BLOCSIZE); return false;}
  if (keynumb%BLOCSIZE==0)
  { /* si toutes les GLXkey ont ete attribuees on ajoute un bloc */
    if (!(_KEY_=(G2Xkey*)realloc(_KEY_,(keyblocnumb+1)*BLOCSIZE*sizeof(G2Xkey)))) return false;
    memset(_KEY_+keyblocnumb*BLOCSIZE,0,BLOCSIZE*sizeof(G2Xkey));
    keyblocnumb++;
  }
  _KEY_[keynumb].key    = key;
  _KEY_[keynumb].action = action;
  if (info) snprintf(_KEY_[keynumb].info,32,"%s",info);
  keynumb++;
  return true;
}

/*= Execute l'action associee au car. c =*/
static __inline__ bool g2x_ExecuteKeyAction(char c)
{
  G2Xkey *k;
  for (k=_KEY_; k<_KEY_+keynumb; k++)
    if (c==k->key) { k->action(); return true; }
  return false;
}

/*= Libere les KeyActions =*/
static __inline__ void g2x_FreeKeyAction(void)
{
  if (_KEY_) free(_KEY_);
  _KEY_=NULL;
  keyblocnumb=0;
  keynumb=0;
}

/*====================================================
  fonction associee aux interruptions du pave fleche
  parametres :
  - c : caractere saisi
  - x,y : coordonnee du curseur dans la fenetre
  ==================================================*/
static void __inline__ g2x_ArrowKeys(int c, int x, int y)
{
  if (!prmnum) return;
  glutSetWindow(drawwin);
  if (!ThePrm) return glutPostWindowRedisplay(drawwin);

  switch(c)
  {
    case GLUT_KEY_PAGE_UP   : if (ThePrm!=_PRM_+prmnum) ThePrm++;
                              else ThePrm=_PRM_;
                              g2x_PrintParam();
                              if (_AUTODISPLAY_) return glutPostWindowRedisplay(drawwin);
    case GLUT_KEY_PAGE_DOWN : if (ThePrm!=_PRM_       ) ThePrm--;
                              else ThePrm=_PRM_+prmnum;
                              g2x_PrintParam();
                              if (_AUTODISPLAY_) return glutPostWindowRedisplay(drawwin);
  }
  /* Parametre reel */
  if (ThePrm->var.r)
  {
    switch (c)
    {
      case GLUT_KEY_LEFT  : *ThePrm->var.r = ThePrm->min.r; break;
      case GLUT_KEY_RIGHT : *ThePrm->var.r = ThePrm->max.r; break;
      case GLUT_KEY_UP    :
        switch (ThePrm->ope)
        { case '+' : *ThePrm->var.r += ThePrm->pas.r; break;
          case '*' : *ThePrm->var.r *= ThePrm->pas.r; break;
        }
       *ThePrm->var.r=MIN(ThePrm->max.r,*ThePrm->var.r);
        break;
      case GLUT_KEY_DOWN  :
        switch (ThePrm->ope)
        { case '+' : *ThePrm->var.r -= ThePrm->pas.r; break;
          case '*' : *ThePrm->var.r /= ThePrm->pas.r; break;
        }
       *ThePrm->var.r=MAX(ThePrm->min.r,*ThePrm->var.r);
        break;
    }
    g2x_PrintParam();
    if (_AUTODISPLAY_) return glutPostWindowRedisplay(drawwin);
  }
  /* Parametre entier */
  if (ThePrm->var.i)
  {
    switch (c)
    {
      case GLUT_KEY_LEFT  : *ThePrm->var.i = ThePrm->min.i; break;
      case GLUT_KEY_RIGHT : *ThePrm->var.i = ThePrm->max.i; break;
      case GLUT_KEY_UP    :
        switch (ThePrm->ope)
        { case '+' : *ThePrm->var.i += ThePrm->pas.i; break;
          case '*' : *ThePrm->var.i *= ThePrm->pas.i; break;
        }
       *ThePrm->var.i=MIN(ThePrm->max.i,*ThePrm->var.i);
        break;
      case GLUT_KEY_DOWN  :
        switch (ThePrm->ope)
        { case '+' : *ThePrm->var.i -= ThePrm->pas.i; break;
          case '*' : *ThePrm->var.i /= ThePrm->pas.i; break;
        }
       *ThePrm->var.i=MAX(ThePrm->min.i,*ThePrm->var.i);
        break;
    }
    /*! g2x_PrintParam(); !*/
    if (_AUTODISPLAY_) return glutPostWindowRedisplay(drawwin);
  }
}

/*==============================================================================*/
/*= fonction associee aux interruptions clavier : quelques touches predefinies =*/
/*= parametres :                                                               =*/
/*= - c : caractere saisi                                                      =*/
/*= - x,y : coordonnee du curseur dans la fenetre                              =*/
/*==============================================================================*/
static void __inline__ g2x_Keyboard(uchar c, int x, int y)
{
  glutSetWindow(mainwin);
  switch(c)
  { // <Ctrl+'f'> plein écran -----------------------------------------
    case 6  : _FULLSCREEN_ = !_FULLSCREEN_;
              switch (_FULLSCREEN_)
              {
                case true :
                  fullwidth=curwidth;
                  fullheight=curheight;
                  glutFullScreen();
                  break;
                default :glutReshapeWindow(fullwidth,fullheight);
              }
              glutPostRedisplay(); break;
    // <Ctrl+g> affichage/masque la grille (spécifique 2D) ------------
    case 7  : _GRID_=!_GRID_; glutPostWindowRedisplay(drawwin); break;
    // <Ctrl+j>/<Ctrl+p> capture d'écran jpeg, png --------------------
    case 10 : g2x_PlugCapture(_WINDNAME_,0,0,curwidth,curheight);
              g2x_Snapshot("jpg",_WINDNAME_,curwidth,curheight);  break;
    case 16 : g2x_PlugCapture(_WINDNAME_,0,0,curwidth,curheight);
        g2x_Snapshot("png",_WINDNAME_,curwidth,curheight);  break;
    // <Ctrl+w> inverser couleur de fond ------------------------------
    case 23 : G2X_BKG=1.-G2X_BKG;
         glutPostWindowRedisplay(drawwin); break;
    // <Ctrl+'q'> ou <ESC> : sort du programme ------------------------
    case 27  : case 17  : g2x_Quit();
    // <ESPACE> stoppe/relance l'animation <SPACE> --------------------
    case ' ': _RUN_=!_RUN_;
         // mise a jour de la fonction d'anim
         glutIdleFunc((_IDLE_&&_RUN_)?(_FAFF_>1?_idle_F_:_idle_0_):NULL);
         break;
    // info -----------------------------------------------------------
    case '?': _INFO_ =!_INFO_ ; return glutPostRedisplay();
    // zoom (spécifique 2D) -------------------------------------------
    case '+': zoom*=1.1; glutPostWindowRedisplay(drawwin); break;
    case '-': zoom/=1.1; glutPostWindowRedisplay(drawwin); break;
    case '=': zoom =1.0; glutPostWindowRedisplay(drawwin); break;

    // les autres actions liees a une touche, définies par l'utilisateur
    default  : if (g2x_ExecuteKeyAction(c)) glutPostRedisplay();
               else fprintf(stderr,"KEY '%c' (%d) : nothing attached\t\r",c,(int)c);
  }
}


