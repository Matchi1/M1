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

#ifndef _G3X_TRANSFO_H
  #define _G3X_TRANSFO_H

  #include <g3x_types.h>

  /*= MATRICE EN COORDONNEES HOMOGENE     =*/
  /* type matrice en coordonnees homogenes */
  /* ATTENTION  : OpenGl gere ça directement en <double mat[16]       */
  /*              et traite ça par colonne plutôt que par ligne       */
  typedef struct { double m[16]; } G3Xhmat;
  /* | 00 04 08 | 12 | */
  /* | 01 05 09 | 13 | */
  /* | 02 06 10 | 14 | */
  /* | 03 07 11 | 15 | */

   /* 1° colonne */
  #define a00  0
  #define a10  1
  #define a20  2
  #define a30  3
  /* 2° colonne */
  #define a01  4
  #define a11  5
  #define a21  6
  #define a31  7
  /* 3° colonne */
  #define a02  8
  #define a12  9
  #define a22 10
  #define a32 11
  /* 4° colonne */
  #define a03 12
  #define a13 13
  #define a23 14
  #define a33 15


  /*=****************************************=*/
  /*=   Identite                             =*/
  /*=****************************************=*/
  G3Xhmat   g3x_Identity(void);
  /*=****************************************=*/
  /*=   MATRICES DE TRANSFORMATION STANDARD  =*/
  /*=****************************************=*/
  G3Xhmat   g3x_Translation1v(G3Xvector t);
  G3Xhmat   g3x_Translation3d(double tx, double ty, double tz);
  G3Xhmat   g3x_Homothetie1v(G3Xvector h);
  G3Xhmat   g3x_Homothetie3d(double hx, double hy, double hz);
  G3Xhmat   g3x_RotationX(double a_radians);
  G3Xhmat   g3x_RotationY(double a_radians);
  G3Xhmat   g3x_RotationZ(double a_radians);
  /*=****************************************=*/
  /*=  Fonctions utiltaires sur les matrices =*/
  /*=****************************************=*/
  /* Produit Matrice(A)*Vecteur(V) -> Vecteur */
  G3Xvector g3x_Mat_x_Vector(G3Xhmat A, G3Xvector V);
  /* Produit Matrice(A)*Point(P) -> Point     */
  G3Xpoint  g3x_Mat_x_Point(G3Xhmat A, G3Xpoint P);
  /* version generique des 2 precedentes      */
  G3Xcoord  g3x_Mat_x_Coord2d(G3Xhmat A, double x, double y, double z, double delta);
  /* Produit Matrice(A)*Matrice(B)->Matrice   */
  G3Xhmat   g3x_Mat_x_Mat(G3Xhmat A,G3Xhmat B);
  /* Affichage de la matrice */
  void g3x_Mat_print(G3Xhmat A);
  /* conversion (transposition) au format de amatrice OpenGl */
  G3Xhmat g3x_FormatGLmat(G3Xhmat src);

#endif

#ifdef __cplusplus
  }
#endif
/*=============================================================================*/
