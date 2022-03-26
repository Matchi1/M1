/*!==================================================================
  E.Incerti - Universite Gustave Eiffel - eric.incerti@univ-eiffel.fr
       - Librairie G2X - Fonctions de base d'acces public -
                    version 6d - Jan.2022
  ==================================================================!*/


#ifdef __cplusplus
  extern "C" {
#else
  #define _GNU_SOURCE
#endif

#ifndef _G2X_BASIX_H_
  #define _G2X_BASIX_H_

  #include <g2x.h>

  /*!****************************************************************************!*/
  /*! LA FENETRE GRAPHIQUE                                                       !*/
  /*!****************************************************************************!*/

  /* initialisation de la fenetre et mise en place des parametres Gl             */
  /* 1° fonction à appeler - indispensable                                       */
  void g2x_InitWindow(char* windname, int w, int h);

  /* definit la zone graphique reelle associee a la fenetre                      */
  /* par défaut c'est la zone [(-1.,-1.)->(+1.,+1.)]                             */
  bool g2x_SetWindowCoord(double xmin, double ymin, double xmax, double ymax);

  /*!****************************************************************************!*/
  /*!  LES 4 FONCTIONS ESSENTIELLES                                              !*/
  /*!****************************************************************************!*/
  /* selectionne la fonction d'initialisation des donnees                         */
  void g2x_SetInitFunction(void (*f)(void));

  /* selectionne la fonction des actions de contrôle                              */
  extern void g2x_SetCtrlFunction(void (*f)(void));

  /* selectionne la fonction associee aux actions de dessin                       */
  /* indispensable pour afficher quelque chose                                    */
  void g2x_SetDrawFunction(void (*f)(void));

  /* selectionne la fonction associee aux actions d'animation / calculs           */
  /* indispensable pour animer quelque chose                                      */
  void g2x_SetAnimFunction(void (*f)(void));

  /* selectionne la fonction associee aux actions à faire en sortie (atexit)      */
  void g2x_SetExitFunction(void (*f)(void));


  /* pour sortir -- en cas d'urgence, sinon ça se fait tout seul                  */
  void g2x_Quit(void);

  /*!****************************************************************************!*/
  /*!  LA FONCTION DE DEMARRAGE                                                  !*/
  /*!****************************************************************************!*/
  /* lance la boucle principale (glutMainLoop)                                    */
  /* dernière fonction à appeler                                                  */
  int  g2x_MainStart(void);

  /*!  RECUPERATION DES VARIABLES D'ENVIRONNEMENT                                !*/
  double g2x_GetXMin(void);      /* coordonnee courante en X du coin inf. gauche  */
  double g2x_GetYMin(void);      /* coordonnee courante en Y du coin inf. gauche  */
  double g2x_GetXMax(void);      /* coordonnee courante en X du coin sup. droite  */
  double g2x_GetYMax(void);      /* coordonnee courante en Y du coin sup. droite  */
  double g2x_GetXScale(void);    /* echelle pixel/reel en X                       */
  double g2x_GetYScale(void);    /* echelle pixel/reel en Y                       */
  double g2x_GetXPixSize(void);  /* taille reelle du pixel en X                   */
  double g2x_GetYPixSize(void);  /* taille reelle du pixel en Y                   */
  int    g2x_GetPixWidth(void);  /* largeur courante (en pixels)                  */
  int    g2x_GetPixHeight(void); /* hauteur courante (en pixels)                  */
  void   g2x_SelectDrawWin(void);

  /*!  UTILITAIRES PRATIQUES                                                     !*/
  /* fixe / recupere la couleur de fond de la fenetre                             */
  void   g2x_SetBkgCol(double bkg);
  double g2x_GetBkgCol(void      );
  /* trace les axes (0,x,y)                                                       */
  void   g2x_Axes(void);
  /* trace une grilles des coordonnees                                            */
  void   g2x_Grid(G2Xcolor col1, G2Xcolor col2);
  /* affichage/anim : 1 affichage pour <freq> passes de calcul                    */
  void   g2x_SetRefreshFreq(int freq);
  /* demarre automatiquement l'enregistrement video                               */
  void   g2x_AutoStartVideo(void);

  /*= Affichage de texte =*/
  /* taille de la fonte (10/12/16) */
  void g2x_SelectFont(int size);
  /* affiche en coord. 'pixel' (entiers : x,y) - le reste : comme printf(const char *format, ...) */
  void g2x_StaticPrint(int x, int y, G2Xcolor col, char pos, const char *format, ...);
  /* affiche en coord. 'fenetre' (réeels : x,y) - le reste : comme printf(const char *format, ...) */
  void g2x_Print(double x, double y, G2Xcolor col, char pos, const char *format, ...);
  /* affiche une chaîne prédéfinie */
  void g2x_Write(const char *msg, int x, int y, G2Xcolor col);



 #endif

#ifdef __cplusplus
  }
#endif
/*!=============================================================!*/
