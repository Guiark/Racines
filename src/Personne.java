import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class Personne {

    private final String prenom;
    private final String nom;
    private final Sexe sexe;
    private final int anneeNaissance;
    private Integer anneeDeces; // facultatif : null = inconnue (0..1)

    private final List<Personne> parents = new ArrayList<>();
    private final List<Personne> enfants = new ArrayList<>();

    /** Crée une personne vivante (ou dont le décès n'est pas connu). */
    public Personne(String prenom, String nom, Sexe sexe, int anneeNaissance) {
        if (prenom == null || prenom.isBlank() || nom == null || nom.isBlank()) {
            throw new IllegalArgumentException("Le prénom et le nom sont obligatoires.");
        }
        if (sexe == null) {
            throw new IllegalArgumentException("Le sexe est obligatoire.");
        }
        this.prenom = prenom.trim();
        this.nom = nom.trim();
        this.sexe = sexe;
        this.anneeNaissance = anneeNaissance;
    }

    /** Crée une personne avec une année de décès. */
    public Personne(String prenom, String nom, Sexe sexe, int anneeNaissance, int anneeDeces) {
        this(prenom, nom, sexe, anneeNaissance);
        setAnneeDeces(anneeDeces);
    }

    // ---------- Accesseurs ----------

    public String getPrenom() { return prenom; }

    public String getNom() { return nom; }

    public Sexe getSexe() { return sexe; }

    public int getAnneeNaissance() { return anneeNaissance; }

    /** @return l'année de décès, ou null si elle est inconnue. */
    public Integer getAnneeDeces() { return anneeDeces; }

    /** Règle 4 : le décès ne peut pas précéder la naissance. */
    public void setAnneeDeces(int anneeDeces) {
        if (anneeDeces < anneeNaissance) {
            throw new IllegalArgumentException(
                "L'année de décès (" + anneeDeces + ") ne peut pas être avant la naissance ("
                + anneeNaissance + ").");
        }
        this.anneeDeces = anneeDeces;
    }

    // ---------- Opérations du diagramme ----------

    /** @return les parents directs (0 à 2). */
    public List<Personne> parents() {
        return Collections.unmodifiableList(parents);
    }

    /** @return les enfants directs. */
    public List<Personne> enfants() {
        return Collections.unmodifiableList(enfants);
    }

    /** @return les grands-parents (les parents de chaque parent). */
    public List<Personne> grandsParents() {
        Set<Personne> resultat = new LinkedHashSet<>();
        for (Personne parent : parents) {
            resultat.addAll(parent.parents);
        }
        return new ArrayList<>(resultat);
    }

    /** @return les frères et sœurs (personnes qui partagent au moins un parent). */
    public List<Personne> freresEtSoeurs() {
        Set<Personne> resultat = new LinkedHashSet<>();
        for (Personne parent : parents) {
            resultat.addAll(parent.enfants);
        }
        resultat.remove(this); // on n'est pas son propre frère
        return new ArrayList<>(resultat);
    }

    /** @return tous les descendants : enfants, petits-enfants, etc. */
    public List<Personne> descendants() {
        Set<Personne> resultat = new LinkedHashSet<>();
        collecterDescendants(this, resultat);
        return new ArrayList<>(resultat);
    }

    private static void collecterDescendants(Personne p, Set<Personne> resultat) {
        for (Personne enfant : p.enfants) {
            if (resultat.add(enfant)) {
                collecterDescendants(enfant, resultat);
            }
        }
    }

    // ---------- Gestion du lien (utilisé par ArbreGenealogique) ----------

    /** Crée le lien dans les deux sens. Les règles sont vérifiées par l'arbre. */
    void ajouterParent(Personne parent) {
        this.parents.add(parent);
        parent.enfants.add(this);
    }

    @Override
    public String toString() {
        String annees = anneeNaissance + "-" + (anneeDeces == null ? "" : anneeDeces);
        return prenom + " " + nom.toUpperCase() + " (" + annees + ")";
    }
}
