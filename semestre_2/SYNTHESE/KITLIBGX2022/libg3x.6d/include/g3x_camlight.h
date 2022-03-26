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

#ifndef _G3X_CAMLIGHT_H
  #define _G3X_CAMLIGHT_H

  #include <g3x_types.h>
  #include <g3x_colors.h>

  /*==================================================================*/
  /* Gestion de la Caméra et de la Lumière                            */
  /*==================================================================*/

  /*!-----------------------------------------------!*/
  /*!                 GESTION CAMERA                !*/
  /*!-----------------------------------------------!*/

  /* Le type Camera  */
  typedef struct
  {
    G3Xpoint  *pos; /* point d'attache (point de la scene)     */
    G3Xpoint  *tar; /* point de visee  (point de la scene)     */
    G3Xvector  upv; /* verticale                               */
    /* coord. spheriques dans le repere local                  */
    double    theta,phi,dist;
    /* parametres d'ouverture (-> man gluLookAt)               */
    double    near,far,open;
  } G3Xcamera;

  /*  parametre camera (cf. <gluLookat>  */
  void g3x_SetPerspective(float near, float far, float open);
  /*  coord. spheriques */
  void g3x_SetCameraTheta(double   theta);
  void g3x_SetCameraPhi  (double   phi  );
  void g3x_SetCameraDist (double   dist );
  void g3x_SetCameraTar  (G3Xpoint target);
  /*  les 3 coord. spheriques + point de visee */
  void g3x_SetCameraSpheric(double theta, double phi, double dist, G3Xpoint target);
  /*  meme chose en cartesien */
   void g3x_SetCameraCartesian(G3Xpoint position, G3Xpoint target);
  /*  pour attacher la camera a un point mobile de la scene */
  void g3x_SetCameraTracking(G3Xpoint *position, G3Xpoint *target);
  /*= recup-re les param. geometrique de la camera =*/
  G3Xcamera* g3x_GetCamera(void);
  /*= recupere la distance de la camera a la cible =*/
  double g3x_GetCameraDist(void);

  /*!-----------------------------------------------!*/
  /*!               GESTION LUMIERE                 !*/
  /*!-----------------------------------------------!*/
  /* Le type Lumiere */
  typedef struct
  {
    G3Xpoint   pos;            /* position  cartesienne        */
    double    theta,phi,dist;  /* coordonnees spheriques       */
    /* couleurs de la lumiere                                  */
    G3Xcolor   ambi;           /* ambiante                     */
    G3Xcolor   diff;           /* diffuse                      */
    G3Xcolor   spec;           /* speculaire                   */
    double    dx,dy,dz;        /* pas de deplacement clavier   */
  } G3Xlight;


  G3Xlight* g3x_GetLight(void);
  /* fixe les param. colorimetriques du spot lumineux */
  void g3x_SetLightAmbient  (float x, float y, float z);
  void g3x_SetLightDiffuse  (float x, float y, float z);
  void g3x_SetLightSpecular (float x, float y, float z);
  /* fixe la position du spot lumineux                */
  void g3x_SetLightPosition (float x, float y, float z);
  /* fixe les pas de deplacement du spot ('x'/'X', 'y'/'Y', 'z'/'Z',) */
  void g3x_SetSpotSteps     (float dx, float dy, float dz);
  /*  les 3 coord. spheriques + point de visee */
  void g3x_SetLightSpheric(double theta, double phi, double dist);
  /*  meme chose en cartesien */
  void g3x_SetLightCartesian(G3Xpoint position) ;


#endif

#ifdef __cplusplus
  }
#endif
