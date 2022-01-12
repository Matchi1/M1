# Exercice 1

## Replace

Il y a la présence de 5 noeuds au total:
- Préélisions
- PréContractions
- Ligatures
- Tiret
- Apostrophe

Les graphes produits permettent de remplacer les séquences reconnues par
des séquences produites

## Sentence

On remarque que sur les noeuds, il n'y a pas la présence de sortie
cela est peut-être dû au fait que la séquence reconnue est celle qui
sera produites.
Elle permet de reconnaître les mots d'une phrase.

## Construct

D'après les observations, les phrases semblent ne pas être cyclique.
On remarque aussi que les types de mots sont correctement attribués
à chaque mot (adjectif, nom, préposition,...).
On remarque aussi qu'il y a plusieurs propositions (noeuds) pour certain
mot s'il y a de l'ambiguïté concernant le type du mot.

# Exercice 2

## Regex

<NB> : trouve toutes les occurences de nombres dans le texte
<WORD> : trouve n'importe quelle mot composé de lettre dans une phrase
environs 71000 mots détectés

## Exemples

<A> : trouve n'importe quelle adjectif dans une phrase
environs 10000 mots détectés

<pouvoir> : trouve toutes les occurences de la forme pouvoir
environs 362 mots détectés

<savoir> : trouve toutes les occurences de la forme savoir
environs 108 mots détectés

<peut,pouvoir.V> : trouve toutes les occurences de la forme pouvoir
avec la forme fléchie 'peut'
environs 64 mots détectés
