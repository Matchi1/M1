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

#ifndef _G3X_BASIX_H_
  #define _G3X_BASIX_H_

  #include <g3x_types.h>
  #include <g3x_colors.h>

  /*!-----------------------------------------------!*/
  /*!           LES FONCTIONALITES DE BASE          !*/
  /*!-----------------------------------------------!*/
  /*= initialisation de la fenetre et mise en place des parametres Gl =*/
  void g3x_InitWindow(char* windname, int w, int h);

  /*!-----------------------------------------------!*/
  /*!             dessin et calculs                 !*/
  /*!-----------------------------------------------!*/
  /* initialisation des donnees                                                   */
  void  g3x_SetInitFunction(void (*f)(void));
  /* actions de controle (boutons, scrollbars....)                                */
  void  g3x_SetCtrlFunction(void (*f)(void));
  /* selectionne la fonction associee aux actions de dessin                       */
  void  g3x_SetDrawFunction(void (*f)(void));
  /* selectionne la fonction associee aux actions d'animation / calculs           */
  void  g3x_SetAnimFunction(void (*f)(void));
  /* affichage/anim : 1 affichage pour <freq> passes de calcul                    */
  void  g3x_SetRefreshFreq(int freq);

  /*= lance la boucle principale (glutMainLoop) =*/
  int  g3x_MainStart(void);
  /*= - ExitFunc   : procedure de liberation de memoire, appelee a la sortie     =*/
  void g3x_SetExitFunction(void (*ExitFunc)(void));
  /*= pour sortir =*/
  void g3x_Quit(void);

  /* demarre automatiquement l'enregistrement video */
  void g3x_AutoStartVideo(void);
  /* ecrit le message <msg> en position (x,y) avec la couleur <col>*/
  void g3x_Write(const char *msg, int x, int y, G3Xcolor col);

  int  g3x_GetPixWidth(void);
  int  g3x_GetPixHeight(void);

  void g3x_AutoSnap_jpg(void);
  void g3x_AutoSnap_png(void);

  /*= fixe / recupere la couleur de fond de la fenetre               =*/
  void   g3x_SetBkgCol(double bkg);
  double g3x_GetBkgCol(void      );

  /*= trace une fleche dans la direction (1.,0.,0.) =*/
  void g3x_Arrow(GLfloat r, GLfloat h, G3Xcolor col);
  void g3x_ArrowVect(G3Xpoint O, G3Xvector V, GLfloat r, GLfloat h, G3Xcolor col);
  /*= fait tourner la scene autour de l'axe z, en continu =*/
  void g3x_SetAutoRotationZ(double angle);

  /*= obsoletes =*/
  void g3x_SetAutoDisplay(bool flag);
  void g3x_ToggleAutoDisplay(void);

  /*= Affichage de texte =*/
  /* taille de la fonte (10/12/16) */
  void g3x_SelectFont(int size);
  /* affiche en coord. 'pixel' (x,y) - le reste : comme printf(const char *format, ...) */
  void g3x_Print(int x, int y, G3Xcolor col, char pos, const char *format, ...);
  /* affiche une chaîne prédéfinie */
  void g3x_Write(const char *msg, int x, int y, G3Xcolor col);

  /*= Fonction de gestion des evenements =*/
  bool g3x_Running(void);   /* renvoie true si la boucle d'anim. est active            */
  void g3x_Show(void);      /* force l'affichage dans la fonction de dessin (DrawFunc) */
  void g3x_Stop(void);      /* arrete l'animation (arret sur image)                    */
  void g3x_Continue(void);  /* relance l'animation                                     */


 #endif

#ifdef __cplusplus
  }
#endif
/*===============================================================*/
