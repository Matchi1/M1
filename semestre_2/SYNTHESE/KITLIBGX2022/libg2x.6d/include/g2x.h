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

  #ifndef _G2X_H_
    #define _G2X_H_

  /* les lib. standards /usr/lib */
  #include <stdio.h>
  #include <stdlib.h>
  #include <string.h>
  #include <unistd.h>
  #include <time.h>
  #include <sys/types.h>
  #include <sys/times.h>
  #include <sys/time.h>
  #include <math.h>

  /* OpenGl & FreeGlut /usr/lib */
  #include <GL/gl.h>
  #include <GL/glut.h>

  /* les libs. locales */
  #include <g2x_types.h>      /* types primaires et macros diverses        */
  #include <g2x_geom.h>       /* primitives et opérateurs géométriques     */
  #include <g2x_colors.h>     /* gestions des couleurs                     */
  #include <g2x_tools.h>      /* utilitaires divers                        */
  #include <g2x_capture.h>    /* routines de capture d'ecran image/video   */
  #include <g2x_control.h>     /* gestion des boutons, scrollbars, souris, clavier  */
  #include <g2x_window.h>     /* routines et fonctions de base             */

  #include <g2x_draw.h>       /* trace en mode reel                        */
  #include <g2x_geoalgo.h>    /* algo. geometriques en mode reel           */
  #include <g2x_transfo.h>    /* transfo. en coordonnees homogenes 4x4     */
  #include <g2x_polygon.h>    /* manipulation de polygones (listes)        */
  #include <g2x_pnm.h>        /* images au format PNM                      */

  #endif

#ifdef __cplusplus
  }
#endif
/*!=============================================================!*/
