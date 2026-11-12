import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class Main {

    private static final Scanner entree = new Scanner(System.in);

    public static void main(String[] args) {
        ArbreGenealogique arbre = new ArbreGenealogique();
        boolean continuer = true;

        System.out.println("=== RACINES : arbre généalogique ===");
        while (continuer) {
            afficherMenu();
            if (!entree.hasNextLine()) {
                break;
            }
            String choix = entree.nextLine().trim();
            try {
                switch (choix) {
                    case "1": ajouterPersonne(arbre); break;
                    case "2": declarerLien(arbre); break;
                    case "3": consulterParente(arbre); break;
                    case "4": afficherDescendants(arbre); break;
                    case "5": chargerExemple(arbre); break;
                    case "0": continuer = false; break;
                    default: System.out.println("Choix inconnu, tapez un chiffre du menu.");
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Erreur : " + e.getMessage());
            }
        }
        System.out.println("Au revoir !");
    }

    private static void afficherMenu() {
        System.out.println();
        System.out.println("1. Ajouter une personne");
        System.out.println("2. Déclarer un lien parent -> enfant");
        System.out.println("3. Consulter la parenté d'une personne");
        System.out.println("4. Afficher les descendants d'une personne");
        System.out.println("5. Charger une famille d'exemple");
        System.out.println("0. Quitter");
        System.out.print("Votre choix : ");
    }

    // ---------- Fonctionnalité 1 ----------
    private static void ajouterPersonne(ArbreGenealogique arbre) {
        String prenom = lire("Prénom : ");
        String nom = lire("Nom : ");
        Sexe sexe = lireSexe(lire("Sexe (F/M) : "));
        int naissance = entier(lire("Année de naissance : "));
        String deces = lire("Année de décès (Entrée si inconnue) : ");

        Personne p;
        if (deces.isBlank()) {
            p = new Personne(prenom, nom, sexe, naissance);
        } else {
            p = new Personne(prenom, nom, sexe, naissance, entier(deces));
        }
        arbre.ajouter(p);
        System.out.println("Personne ajoutée : " + p);
    }

    // ---------- Fonctionnalité 2 ----------
    private static void declarerLien(ArbreGenealogique arbre) {
        Personne parent = choisirPersonne(arbre, "Parent");
        Personne enfant = choisirPersonne(arbre, "Enfant");
        arbre.lier(parent, enfant);
        System.out.println("Lien créé : " + parent + " est parent de " + enfant);
    }

    // ---------- Fonctionnalité 3 ----------
    private static void consulterParente(ArbreGenealogique arbre) {
        Personne p = choisirPersonne(arbre, "Personne");
        System.out.println("Parenté de " + p);
        afficherListe("Parents", p.parents());
        afficherListe("Grands-parents", p.grandsParents());
        afficherListe("Frères et sœurs", p.freresEtSoeurs());
    }

    // ---------- Fonctionnalité 4 ----------
    private static void afficherDescendants(ArbreGenealogique arbre) {
        Personne p = choisirPersonne(arbre, "Personne");
        List<Personne> niveau = new ArrayList<>(p.enfants());
        if (niveau.isEmpty()) {
            System.out.println(p + " n'a pas de descendants.");
            return;
        }
        Set<Personne> vus = new HashSet<>(niveau);
        int generation = 1;
        System.out.println("Descendants de " + p);
        while (!niveau.isEmpty()) {
            System.out.println("Génération " + generation + " :");
            for (Personne d : niveau) {
                System.out.println("  - " + d);
            }
            List<Personne> suivant = new ArrayList<>();
            for (Personne d : niveau) {
                for (Personne e : d.enfants()) {
                    if (vus.add(e)) {
                        suivant.add(e);
                    }
                }
            }
            niveau = suivant;
            generation++;
        }
    }

    // ---------- Famille d'exemple (pour la démonstration) ----------
    private static void chargerExemple(ArbreGenealogique arbre) {
        Personne marie = new Personne("Marie", "Durand", Sexe.FEMININ, 1950);
        Personne jean = new Personne("Jean", "Durand", Sexe.MASCULIN, 1948, 2010);
        Personne paul = new Personne("Paul", "Durand", Sexe.MASCULIN, 1975);
        Personne lucie = new Personne("Lucie", "Durand", Sexe.FEMININ, 1978);
        Personne emma = new Personne("Emma", "Durand", Sexe.FEMININ, 2005);
        for (Personne p : new Personne[] {marie, jean, paul, lucie, emma}) {
            arbre.ajouter(p);
        }
        arbre.lier(marie, paul);
        arbre.lier(jean, paul);
        arbre.lier(marie, lucie);
        arbre.lier(paul, emma);
        System.out.println("Famille d'exemple chargée (5 personnes, 3 générations).");
    }

    // ---------- Outils de saisie et d'affichage ----------

    private static String lire(String question) {
        System.out.print(question);
        return entree.hasNextLine() ? entree.nextLine().trim() : "";
    }

    private static int entier(String texte) {
        try {
            return Integer.parseInt(texte.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("« " + texte + " » n'est pas une année valide.");
        }
    }

    private static Sexe lireSexe(String texte) {
        if (texte.equalsIgnoreCase("F")) {
            return Sexe.FEMININ;
        }
        if (texte.equalsIgnoreCase("M")) {
            return Sexe.MASCULIN;
        }
        throw new IllegalArgumentException("Sexe invalide : tapez F ou M.");
    }

    /** Recherche une personne par nom ou prénom ; propose un choix s'il y en a plusieurs. */
    private static Personne choisirPersonne(ArbreGenealogique arbre, String intitule) {
        String texte = lire(intitule + " (nom ou prénom) : ");
        List<Personne> trouves = arbre.rechercher(texte);
        if (trouves.isEmpty()) {
            throw new IllegalArgumentException("Aucune personne trouvée pour « " + texte + " ».");
        }
        if (trouves.size() == 1) {
            return trouves.get(0);
        }
        for (int i = 0; i < trouves.size(); i++) {
            System.out.println("  " + (i + 1) + ". " + trouves.get(i));
        }
        int numero = entier(lire("Numéro de la personne : "));
        if (numero < 1 || numero > trouves.size()) {
            throw new IllegalArgumentException("Numéro invalide.");
        }
        return trouves.get(numero - 1);
    }

    private static void afficherListe(String titre, List<Personne> personnes) {
        if (personnes.isEmpty()) {
            System.out.println(titre + " : aucun");
            return;
        }
        System.out.println(titre + " :");
        for (Personne p : personnes) {
            System.out.println("  - " + p);
        }
    }
}
