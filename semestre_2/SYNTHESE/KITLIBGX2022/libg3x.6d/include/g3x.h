/*!==================================================================
  E.Incerti - Universite Gustave Eiffel - eric.incerti@univ-eiffel.fr
       - Librairie G3X - Fonctions de base d'acces public -
                    version 6d - Jan.2022
  ==================================================================!*/

#ifdef __cplusplus
  extern "C" {
#else
  #define _GNU_SOURCE
#endif

  #ifndef _G3X_H_
    #define _G3X_H_

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

  /* OpenGl, Glu  & FreeGlut /usr/lib */
  #include <GL/gl.h>
  #include <GL/glu.h>
  #include <GL/glut.h>

  /* les libs. locales */
  #include <g3x_types.h>       /* types primaires et macros diverses                */
  #include <g3x_geom.h>        /* types primaires et macros diverses                */
  #include <g3x_colors.h>      /* gestions des couleurs                             */
  #include <g3x_tools.h>       /* utilitaires divers                                */
  #include <g3x_capture.h>     /* routines de capture d'ecran image/video           */
  #include <g3x_camlight.h>    /* gestion de la caméra et de la lumière             */
  #include <g3x_control.h>     /* gestion des boutons, scrollbars, souris, clavier  */
  #include <g3x_window.h>      /* routines et fonctions de base                     */

  #include <g3x_transfo.h>     /* transfo. en coordonnees homogenes 4x4             */
  #include <g3x_quaternions.h> /* quaternions                                       */
  #include <g3x_pnm.h>         /* image au format PNM                               */

  #endif

#ifdef __cplusplus
  }
#endif
/*===============================================================*/
