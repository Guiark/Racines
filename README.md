# Racines : gestion d'arbres généalogiques

Projet de TD/TP BTS SIO 2e année, « UML pour les développeurs ».
Petit logiciel en console pour l'association Mémoire Vive : enregistrer des personnes, les relier et retrouver leurs parentés.

## Prérequis

- Java 11 ou plus (`javac -version` pour vérifier)

## Lancer le programme

Depuis la racine du dépôt :

```
javac -encoding UTF-8 -d out src/*.java
java -cp out Main
```

Le menu propose :

| Choix | Fonctionnalité |
|---|---|
| 1 | Ajouter une personne (prénom, nom, sexe, année de naissance, année de décès si connue) |
| 2 | Déclarer un lien parent -> enfant |
| 3 | Consulter la parenté (parents, grands-parents, frères et sœurs) |
| 4 | Afficher les descendants, génération par génération |
| 5 | Charger une famille d'exemple (pratique pour la démonstration) |

## Lancer les tests (bonus)

Un test par règle de gestion, plus quelques cas limites :

```
javac -encoding UTF-8 -d out src/*.java tests/*.java
java -cp out TestsRegles
```

Résultat attendu : `7 test(s) réussi(s), 0 échoué(s).`

## Les quatre règles de gestion

| N° | Règle | Où elle est vérifiée |
|---|---|---|
| 1 | Deux parents maximum | `ArbreGenealogique.lier()` |
| 2 | Le parent est né strictement avant l'enfant | `ArbreGenealogique.lier()` |
| 3 | Pas de boucle (personne n'est son propre ancêtre) | `ArbreGenealogique.lier()` |
| 4 | Année de décès >= année de naissance | `Personne` (constructeur et `setAnneeDeces()`) |

Une règle enfreinte déclenche une `IllegalArgumentException` avec un message clair, affiché par le menu : le programme ne plante jamais.

## Organisation du dépôt

```
racines/
├─ README.md
├─ docs/
│  ├─ cas-utilisation.png  + .drawio
│  ├─ classes.png          + .mmd
│  └─ fiche-declarer-lien.md
├─ src/     Sexe, Personne, ArbreGenealogique, Main
└─ tests/   TestsRegles
```


## Captures d'écran

*(à ajouter : une capture du menu, de la consultation d'une parenté et d'un message d'erreur)*
