---
title: |
	| \vspace{5cm} Rapport TP2 linguistique
subtitle: "M1 Informatique, groupe apprenti"
author: "Mathieu Chan"
date: "2022-01-23"
geometry: margin=3cm
header-includes: |
	\usepackage{titling}
	\usepackage{fancyhdr}
	\usepackage{graphicx}
	\usepackage{caption}
	\usepackage{subcaption}

	\pagestyle{fancy}
	\fancyhead[LO,LE]{Rapport linguistique}

footer-left: "2021-2022"
output:
    pdf_document:
		number_sections: true
		font-size: 10
---

\pagebreak

\tableofcontents

\pagebreak

# Exercice 1

## Question 1.1

\begin{figure}[h]
	\begin{minipage}[t]{7cm}
		\centering
	    \includegraphics[width=7cm]{img/abri_moteur.png}
	    \caption{Graphe des mots abri et moteur comme exemple}
        \label{fig:1}
	\end{minipage}
	\hfill
	\begin{minipage}[t]{7cm}
		\centering
	    \includegraphics[width=7cm]{img/jeu_genou.png}
        \caption{Graphe des mots jeu et genou comme exemple}
        \label{fig:2}
	\end{minipage}
\end{figure}

\begin{figure}[h]
	\begin{minipage}[t]{7cm}
		\centering
	    \includegraphics[width=7cm]{img/moteurA.png}
	    \caption{Graphe des mots moteur (adjectif) comme exemple}
        \label{fig:3}
	\end{minipage}
	\hfill
	\begin{minipage}[t]{7cm}
		\centering
	    \includegraphics[width=7cm]{img/as.png}
	    \caption{Graphe des mots as comme exemple}\label{fig:4}
	\end{minipage}
\end{figure}

\begin{figure}[h]
	\centering
	\includegraphics[width=7cm]{img/pareil.png}
	\caption{Graphe des mots pareil comme exemple}\label{fig:5}
\end{figure}

\pagebreak

## Question 1.2 et 1.3

\begin{figure}[h]
	\centering
	\includegraphics[width=7cm]{img/aérer_obséder.png}
	\caption{Graphe des mots pareil comme exemple}\label{fig:5}
\end{figure}

## Question 1.4

```
jeu,N78
genou,N78
moteur,N79
abri,N79
as,N80
pareil,A80
moteur,A81
aérer,V100
obséder,V100
```

## Question 1.5 et 1.6

```
jeu,jeu.N:ms
jeux,jeu.N:mp
genou,genou.N:ms
genoux,genou.N:mp
moteur,moteur.N:ms
moteurs,moteur.N:mp
abri,abri.N:ms
abris,abri.N:mp
as,as.N:ms
pareil,pareil.A:ms
pareille,pareil.A:fs
pareilles,pareil.A:fp
pareils,pareil.A:mp
moteur,moteur.A:ms
moteurs,moteur.A:mp
motrice,moteur.A:fs
motrices,moteur.A:fp
aère,aérer.V:P3s
aère,aérer.V:P1s
aères,aérer.V:P2s
aérons,aérer.V:P1p
aérez,aérer.V:P2p
aèrent,aérer.V:P3p
obsède,obséder.V:P3s
obsède,obséder.V:P1s
obsèdes,obséder.V:P2s
obsédons,obséder.V:P1p
obsédez,obséder.V:P2p
obsèdent,obséder.V:P3p
```

\pagebreak

# Exercice 2

## Question 2.1

```
jeu,jeu.N:ms	        .N:ms
jeux,jeu.N:mp	        1.N:mp
genou,genou.N:ms	    .N:ms
genoux,genou.N:mp	    1.N:mp
moteur,moteur.N:ms	    .N:ms
moteurs,moteur.N:mp	    1.N:mp
abri,abri.N:ms	        .N:ms
abris,abri.N:mp	        1.N:mp
as,as.N:ms	            .N:ms
pareil,pareil.A:ms	    .A:ms
pareille,pareil.A:fs	2.A:fs
pareilles,pareil.A:fp	3.A:fp
pareils,pareil.A:mp	    1.A:mp
moteur,moteur.A:ms	    .A:ms
moteurs,moteur.A:mp	    1.s:mp
motrice,moteur.A:fs	    4eur.A:fs
motrices,moteur.A:fp	5eur.A:fp
aère,aérer.V:P3s	    3érer.V:P3s
aère,aérer.V:P1s	    3érer.V:P1s
aères,aérer.V:P2s	    4érer.V:P2s
aérons,aérer.V:P1p	    3er.V:P1p
aérez,aérer.V:P2p	    1r.V:P2p
aèrent,aérer.V:P3p	    5érer.V:P3p
obsède,obséder.V:P3s	3éder.V:P3s
obsède,obséder.V:P1s	3éder.V:P1s
obsèdes,obséder.V:P2s	4éder.V:P2s
obsédons,obséder.V:P1p	3er.V:P1p
obsédez,obséder.V:P2p	1r.V:P2p
obsèdent,obséder.V:P3p	5éder.V:P3p
```

## Question 2.2

```
1 jeu	        .N:ms
2 jeux	        1.N:mp
3 genou 	    .N:ms
4 genoux 	    1.N:mp
5 moteur 	    .N:ms
6 moteurs 	    1.N:mp
7 abri 	        .N:ms
8 abris 	    1.N:mp
9 as 	        .N:ms
10 pareil 	    .A:ms
11 pareille 	2.A:fs
12 pareilles 	3.A:fp
13 pareils 	    1.A:mp
14 moteur 	    .A:ms
15 moteurs 	    1.s:mp
16 motrice 	    4eur.A:fs
17 motrices 	5eur.A:fp
18 aère 	    3érer.V:P3s
19 aères 	    4érer.V:P2s
20 aérons 	    3er.V:P1p
21 aérez 	    1r.V:P2p
22 aèrent 	    5érer.V:P3p
23 obsède 	    3éder.V:P3s
24 obsèdes 	    4éder.V:P2s
25 obsédons 	3er.V:P1p
26 obsédez 	    1r.V:P2p
27 obsèdent 	5éder.V:P3p
```

## Question 2.3

D'après la liste des étiquettes ci-dessus, on peut observer qu'il y a 27 listes
d'étiquettes différentes.

Voici le graphe pour les formes fléchies des mots *jeu*, *genou*, *aérer* et
*obséder*.

\begin{figure}[h]
	\centering
	\caption{Graphe des mots fléchies jeu, genou, aérer et obséder}
	\includegraphics[width=13cm]{img/graphe1.png}
    \label{fig:1}
\end{figure}

On peut remarquer qu'il n'y a pas eu de minimisation du graphe de mots fléchies.

\pagebreak

## Question 2.4

\begin{figure}[h]
	\centering
	\caption{Résultat de la compression du dictionnaire}
	\includegraphics[width=7cm]{img/compress.png}
    \label{fig:1}
\end{figure}

La fenêtre obtenue après la compression des données indique les information
suivantes:

* La taille du binaire crée (232 octets)
* Le nombre de fichiers parcourus (1 seul fichier)
* Le nombre de lignes parcourues (29 lignes)
* Le nombre de code flexionnels produit

Les questions précédentes représentent ce que fait la compression en interne.

La compression va créer un transducteur minimal qui sera stocker dans un *.bin*,
et la liste des formes fléchies dans un *.inf*

\pagebreak

## Question 2.5

\begin{figure}[h]
	\begin{minipage}[t]{7cm}
		\centering
	    \includegraphics[width=7cm]{img/apply-custom.png}
	    \caption{Résultat de l'application du dictionnaire produit}
	\end{minipage}
	\hfill
	\begin{minipage}[t]{7cm}
		\centering
	    \includegraphics[width=7cm]{img/apply-default.png}
        \caption{Résultat de l'application du dictionnaire par défaut}
	\end{minipage}
\end{figure}

On peut remarquer que la liste des mots composés est vide dans la figure 9
contrairement à la figure 10, ce qui est normal car il n'y a que des mots
simples dans le dictionnaire produit.

Le nombre de mots non reconnus est également très différent, 9411 pour le
dictionnaire produit contre 480 pour le dictionnaire par défaut.

## Question 2.6

On remarque qu'il y a beaucoup de mots inconnus pour un texte utilisant le
dictionnaire produit, ce qui est logique car il y a très peu de mots qui sont
couverts par le dictionnaire.

On peut également observé qu'il y a quand même des mots déjà gérer par Unitex,
c'est notamment le cas pour les déterminants et les prépositions.

On peut assumer qu'il y a un dictionnaire qui est toujours appliqué au texte.

On peut voir dans l'image ci-dessous qu'il y a plusieurs niveau de détails pour
les déterminants et les prépositions, malgré le fait qu'on ne traite pas ces
cas dans le dictionnaire produit.

\begin{figure}[h]
	\centering
    \includegraphics[width=15cm]{img/construct-automate.png}
    \caption{Automate d'une phrase utilisant le dictionnaire produit}
\end{figure}

\pagebreak

# Exercice 3

\begin{figure}[h]
	\begin{minipage}[t]{7cm}
		\centering
	    \includegraphics[width=7cm]{img/grave.png}
	    \caption{Graphe du mot grave en verlan}
	\end{minipage}
	\hfill
	\begin{minipage}[t]{7cm}
		\centering
	    \includegraphics[width=7cm]{img/rage.png}
        \caption{Graphe du mot grave en verlan}
	\end{minipage}
	\hfill
	\begin{minipage}[t]{7cm}
        \centering
        \includegraphics[width=7cm]{img/parti.png}
        \caption{Graphe du mot parti en verlan}
	\end{minipage}
\end{figure}

On créer le dictionnaire composé des lignes suivantes:

```
grave,A100
parti,A101
rage,N100
```

On applique la command **Inflect...** sur le dictionnaire, on obtient le fichier
composé des lignes suivantes:

```
veugra,grave.A:ms
tipar,parti.A:ms
geura,rage.N:fs
```

\pagebreak

On applique ensuite la commmand **Check Format...** pour vérifier le format du
fichier *dela-verlanflx.dic*, le fichier est bien du type DELAF comme indiquer
ci-dessous.

```
-----------------------------------
-------------  Stats  -------------
-----------------------------------
File: /home/chan/workspace/Unitex-GramLab/Unitex/French/Dela/dela-verlanflx.dic
Type: DELAF
3 lines read
3 simple entries for 3 distinct lemmas
0 compound entry for 0 distinct lemma
-----------------------------------
----  All chars used in forms  ----
-----------------------------------
a (0061)
e (0065)
g (0067)
i (0069)
p (0070)
r (0072)
t (0074)
u (0075)
v (0076)
-------------------------------------------------------------
----    2 grammatical/semantic codes used in dictionary  ----
-------------------------------------------------------------
A
N
-----------------------------------------------------
----    2 inflectional codes used in dictionary  ----
-----------------------------------------------------
ms
fs
```

