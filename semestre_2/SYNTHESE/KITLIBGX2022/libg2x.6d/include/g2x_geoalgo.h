/*!==================================================================
  E.Incerti - Universite Gustave Eiffel - eric.incerti@univ-eiffel.fr
       - Librairie G2X - Fonctions de base d'acces public -
                    version 6d - Jan.2022
  ==================================================================!*/
#ifdef __cplusplus
  extern "C" {
#endif

#ifndef _G2X_GEOALGO_H
  #define _G2X_GEOALGO_H

  #include <g2x.h>

  /* determine la porjection P du point C sur la dte (AB) -- retour : t tq AP=t.AB */
  /* t<0. P est 'avant A', 0.<t<1. P est sur [A,B], 1.<t P est 'après B'           */
  double    g2x_ProjPntDte(G2Xpoint A,G2Xpoint B,G2Xpoint C, G2Xpoint *P);

  /* inteserction de 2 droites -- retour:VRAI/FAUX, intersection dans I */
  bool      g2x_DroiteInterDroite(G2Xpoint A, G2Xvector AB, G2Xpoint C, G2Xvector CD, G2Xpoint *I);

  /*intersection des segments [AB] et [CD] - comme les dtes, mais inters. sur les 2 segments */
  bool      g2x_SegmentInterSegment(G2Xpoint A, G2Xpoint B, G2Xpoint C, G2Xpoint D, G2Xpoint *I);

/*! Intersection Segment/Cercle :
  *  0 : [AB] entierement dans le cercle
  * -1 : A et B confondus ou Inters. Dte/Cercle hors de [AB] ou pas d'intesection Dte/Cercle
  * +1 : [AB] tangent au Cercle
  * +2 : A dedans, B dehors
  * +3 : A dehors, B dedans
  * +4 : A et B dehors, 2 intersections
  ---------------------------------------------------------------------------------------!*/
  int       g2x_SegmentInterCercle(G2Xpoint A, G2Xpoint B, G2Xpoint C, double r, G2Xpoint *I1, G2Xpoint *I2);

/*! Intersection Cercle/Cercle
  * -2 : centres des cercles confondus
  * -1 : un cercle contient l'autre
  *  0 : pas d'intersection, cercles disjoints
  * +1 : deux intersections
  * +2 : cercles tangents
  -----------------------------------------------!*/
  int       g2x_CercleInterCercle(G2Xpoint C1, double r1, G2Xpoint C2, double r2, G2Xpoint *I1, G2Xpoint *I2);

  /*! Cercles inscrit et circonscrit au triangle ABC !*/
  void      g2x_CercleInscrit(G2Xpoint A, G2Xpoint B, G2Xpoint C, G2Xpoint *CCI, double *rci);
  void      g2x_CercleCirconscrit(G2Xpoint A, G2Xpoint B, G2Xpoint C, G2Xpoint *CCC, double *rcc);

   /* clipping d'un segment [AB] sur un rectangle parallele auyx axes (DownLeft,UpRight..) -- retour:VRAI/FAUX  */
  bool      g2x_CohenSutherland(G2Xpoint *A, G2Xpoint *B, G2Xpoint DL, G2Xpoint UL, G2Xpoint UR, G2Xpoint DR);

#endif

#ifdef __cplusplus
  }
#endif
/*!=============================================================!*/
