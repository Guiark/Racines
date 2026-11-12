# Fiche de cas d'utilisation : Déclarer un lien parent-enfant

| | |
|---|---|
| **Nom** | Déclarer un lien parent-enfant |
| **Acteur principal** | Généalogiste |
| **Objectif** | Enregistrer qu'une personne est le parent d'une autre personne de l'arbre. |
| **Déclencheur** | Le généalogiste choisit « Déclarer un lien parent -> enfant » dans le menu. |
| **Cas inclus** | Rechercher une personne (utilisé deux fois : pour le parent et pour l'enfant) |

## Préconditions

- Les deux personnes ont déjà été ajoutées à l'arbre (cas « Ajouter une personne »).

## Postcondition (succès)

- Le lien est enregistré : le parent apparaît dans les parents de l'enfant, et l'enfant dans les enfants du parent.
- L'arbre respecte toujours les quatre règles de gestion.

## Scénario nominal

1. Le généalogiste choisit « Déclarer un lien parent -> enfant ».
2. Le système demande le **parent** (nom ou prénom).
3. Le généalogiste saisit un texte ; le système exécute *Rechercher une personne* et identifie le parent (s'il y a plusieurs résultats, le généalogiste choisit un numéro).
4. Le système demande l'**enfant** (nom ou prénom).
5. Le généalogiste saisit un texte ; le système exécute *Rechercher une personne* et identifie l'enfant.
6. Le système vérifie les règles de gestion (aucune n'est enfreinte).
7. Le système enregistre le lien dans les deux sens (parent -> enfant et enfant -> parent).
8. Le système confirme : « Lien créé : *parent* est parent de *enfant* ».

## Scénarios alternatifs et d'erreur

| N° | Étape | Situation | Réaction du système |
|---|---|---|---|
| E1 | 3 ou 5 | Aucune personne ne correspond au texte saisi. | Message « Aucune personne trouvée », retour au menu. Aucun lien créé. |
| E2 | 3 ou 5 | Plusieurs personnes correspondent et le numéro saisi est invalide. | Message « Numéro invalide », retour au menu. |
| E3 | 6 | Le parent et l'enfant sont la même personne. | Message « Une personne ne peut pas être son propre parent ». |
| E4 | 6 | Ce lien existe déjà. | Message « Ce lien existe déjà ». |
| E5 | 6 | **Règle 1** : l'enfant a déjà deux parents. | Message « … a déjà 2 parents », lien refusé. |
| E6 | 6 | **Règle 3** : le parent descend déjà de l'enfant (ex. : Léa parent de sa grand-mère). | Message « Lien impossible … (boucle) », lien refusé. |
| E7 | 6 | **Règle 2** : le parent n'est pas né strictement avant l'enfant. | Message « Le parent doit être né avant l'enfant », lien refusé. |

Dans tous les cas d'erreur, le programme affiche un message clair, ne modifie pas l'arbre et revient au menu principal (jamais de plantage).

## Règles de gestion concernées

1. Deux parents maximum.
2. L'année de naissance d'un parent est strictement inférieure à celle de son enfant.
3. Personne ne peut être son propre ancêtre (pas de boucle).
4. (Année de décès >= année de naissance : vérifiée à l'ajout d'une personne, pas lors du lien.)
