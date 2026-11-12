import src.
public class TestsRegles {

    private static int reussis = 0;
    private static int echoues = 0;

    public static void main(String[] args) {
        testRegle1DeuxParentsMaximum();
        testRegle2ParentNeAvant();
        testRegle3PasDeBoucle();
        testRegle4DecesApresNaissance();

        // Cas limites et cas valides
        testRegle2MemeAnneeRefusee();
        testRegle4MemeAnneeAcceptee();
        testLienValide();

        System.out.println();
        System.out.println(reussis + " test(s) réussi(s), " + echoues + " échoué(s).");
        System.exit(echoues == 0 ? 0 : 1);
    }

    // ---------- Un test par règle ----------

    /** Règle 1 : une personne a 0, 1 ou 2 parents, jamais plus. */
    static void testRegle1DeuxParentsMaximum() {
        ArbreGenealogique arbre = new ArbreGenealogique();
        Personne mere = creer(arbre, "Marie", Sexe.FEMININ, 1950);
        Personne pere = creer(arbre, "Jean", Sexe.MASCULIN, 1948);
        Personne troisieme = creer(arbre, "Marc", Sexe.MASCULIN, 1940);
        Personne enfant = creer(arbre, "Paul", Sexe.MASCULIN, 1975);

        arbre.lier(mere, enfant);
        arbre.lier(pere, enfant);

        attendreRefus("Règle 1 : un 3e parent est refusé",
            () -> arbre.lier(troisieme, enfant), "2 parents");
    }

    /** Règle 2 : l'année de naissance d'un parent est strictement inférieure à celle de l'enfant. */
    static void testRegle2ParentNeAvant() {
        ArbreGenealogique arbre = new ArbreGenealogique();
        Personne jeune = creer(arbre, "Emma", Sexe.FEMININ, 2005);
        Personne plusAgee = creer(arbre, "Lucie", Sexe.FEMININ, 1978);

        attendreRefus("Règle 2 : un parent né après son enfant est refusé",
            () -> arbre.lier(jeune, plusAgee), "né avant");
    }

    /** Règle 3 : personne ne peut être son propre ancêtre (Léa parent de sa grand-mère). */
    static void testRegle3PasDeBoucle() {
        ArbreGenealogique arbre = new ArbreGenealogique();
        Personne grandMere = creer(arbre, "Odette", Sexe.FEMININ, 1930);
        Personne mere = creer(arbre, "Claire", Sexe.FEMININ, 1955);
        Personne lea = creer(arbre, "Léa", Sexe.FEMININ, 1980);

        arbre.lier(grandMere, mere);
        arbre.lier(mere, lea);

        attendreRefus("Règle 3 : Léa parent de sa grand-mère est refusé (boucle)",
            () -> arbre.lier(lea, grandMere), "boucle");
    }

    /** Règle 4 : si l'année de décès est renseignée, elle est >= à l'année de naissance. */
    static void testRegle4DecesApresNaissance() {
        attendreRefus("Règle 4 : un décès avant la naissance est refusé",
            () -> new Personne("Xavier", "Test", Sexe.MASCULIN, 1990, 1980), "décès");
    }

    // ---------- Cas limites et cas valides ----------

    /** Règle 2 est stricte : deux personnes nées la même année ne peuvent pas être parent/enfant. */
    static void testRegle2MemeAnneeRefusee() {
        ArbreGenealogique arbre = new ArbreGenealogique();
        Personne a = creer(arbre, "Anne", Sexe.FEMININ, 1970);
        Personne b = creer(arbre, "Bruno", Sexe.MASCULIN, 1970);

        attendreRefus("Règle 2 (limite) : même année de naissance refusée",
            () -> arbre.lier(a, b), "né avant");
    }

    /** Règle 4 est large : décès la même année que la naissance est accepté. */
    static void testRegle4MemeAnneeAcceptee() {
        attendreSucces("Règle 4 (limite) : décès la même année que la naissance accepté",
            () -> new Personne("Bébé", "Test", Sexe.FEMININ, 1990, 1990));
    }

    /** Un lien qui respecte toutes les règles est accepté. */
    static void testLienValide() {
        ArbreGenealogique arbre = new ArbreGenealogique();
        Personne mere = creer(arbre, "Marie", Sexe.FEMININ, 1950);
        Personne fils = creer(arbre, "Paul", Sexe.MASCULIN, 1975);

        attendreSucces("Lien valide accepté", () -> arbre.lier(mere, fils));
        verifier("Lien valide : Marie est bien le parent de Paul",
            fils.parents().contains(mere) && mere.enfants().contains(fils));
    }

    // ---------- Petits outils de test ----------

    private static Personne creer(ArbreGenealogique arbre, String prenom, Sexe sexe, int annee) {
        Personne p = new Personne(prenom, "Test", sexe, annee);
        arbre.ajouter(p);
        return p;
    }

    /** Le test réussit si l'action lève une IllegalArgumentException dont le message contient le mot-clé. */
    private static void attendreRefus(String titre, Runnable action, String motCle) {
        try {
            action.run();
            echec(titre, "aucune erreur n'a été levée");
        } catch (IllegalArgumentException e) {
            if (e.getMessage() != null && e.getMessage().contains(motCle)) {
                succes(titre);
            } else {
                echec(titre, "mauvais message : " + e.getMessage());
            }
        }
    }

    /** Le test réussit si l'action ne lève aucune erreur. */
    private static void attendreSucces(String titre, Runnable action) {
        try {
            action.run();
            succes(titre);
        } catch (RuntimeException e) {
            echec(titre, "erreur inattendue : " + e.getMessage());
        }
    }

    private static void verifier(String titre, boolean condition) {
        if (condition) {
            succes(titre);
        } else {
            echec(titre, "condition fausse");
        }
    }

    private static void succes(String titre) {
        reussis++;
        System.out.println("[OK]     " + titre);
    }

    private static void echec(String titre, String raison) {
        echoues++;
        System.out.println("[ÉCHEC]  " + titre + " -> " + raison);
    }
}
