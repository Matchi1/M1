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

#ifndef _G2X_TOOLS_H_
  #define _G2X_TOOLS_H_

  #include <g2x_types.h>

  /* horloge */
  typedef struct
  {
    uint hh; /* heure [0,23]     */
    uint mm; /* minutes [0,59]   */
    uint ss; /* secondes [0,59]  */
    uint ds; /* dizieme  de sec. */
    uint cs; /* centieme de sec. */
    uint ms; /* millieme de sec. */
  } G2Xclock;

  /* calcule le temps "processeur" (cumul des threads) */
  /* retour au format hh:mm:ss:cs:ms                   */
  char*  g2x_ProcTimer(void);
  /* chrono "temps réel"                               */
  char*  g2x_Chrono(void);
  /* récupère le chrono courant                        */
  void   g2x_GetTime(G2Xclock* clock);
  /* temporisation, en seconde                         */
  int    g2x_tempo(double tempo);

  /*!****************************************************************************!*/
  /*! générateurs aléatoires                                                     !*/
  /*! DANS CETTE VERSION, C'EST LA GRAINE PAR DEFAUT                             !*/
  /*! => à l'utiilsateur de faire appel à rand(seed)                             !*/
  /*!****************************************************************************!*/
  /* intervalle [root*(1.-percent), root*(1.+percent)] */
  double g2x_Rand_Percent(double root, double percent);
  /* intervalle [root-delta, root+delta]               */
  double g2x_Rand_Delta(double root, double delta);
  /* intervalle [min, max]                             */
  double g2x_Rand_MinMax(double min, double max);

 #endif

#ifdef __cplusplus
  }
#endif
/*!=============================================================!*/
