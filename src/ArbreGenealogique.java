import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * L'arbre généalogique : il contient les personnes (composition)
 * et vérifie les règles de gestion lors de la création d'un lien.
 */
public class ArbreGenealogique {

    private final List<Personne> personnes = new ArrayList<>();

    /** Ajoute une personne à l'arbre. */
    public void ajouter(Personne p) {
        if (p == null) {
            throw new IllegalArgumentException("La personne ne peut pas être vide.");
        }
        if (personnes.contains(p)) {
            throw new IllegalArgumentException(p + " est déjà dans l'arbre.");
        }
        personnes.add(p);
    }


    public void lier(Personne parent, Personne enfant) {
        verifierPresente(parent);
        verifierPresente(enfant);

        if (parent == enfant) {
            throw new IllegalArgumentException("Une personne ne peut pas être son propre parent.");
        }
        if (enfant.parents().contains(parent)) {
            throw new IllegalArgumentException("Ce lien existe déjà.");
        }

        if (enfant.parents().size() >= 2) {
            throw new IllegalArgumentException(enfant + " a déjà 2 parents.");
        }

        if (enfant.descendants().contains(parent)) {
            throw new IllegalArgumentException(
                "Lien impossible : " + parent + " descend déjà de " + enfant + " (boucle).");
        }

        if (parent.getAnneeNaissance() >= enfant.getAnneeNaissance()) {
            throw new IllegalArgumentException(
                "Le parent (" + parent.getAnneeNaissance() + ") doit être né avant l'enfant ("
                + enfant.getAnneeNaissance() + ").");
        }

        enfant.ajouterParent(parent);
    }

    public List<Personne> rechercher(String nom) {
        List<Personne> resultat = new ArrayList<>();
        if (nom == null || nom.isBlank()) {
            return resultat;
        }
        String motif = nom.trim().toLowerCase();
        for (Personne p : personnes) {
            if (p.getNom().toLowerCase().contains(motif)
                    || p.getPrenom().toLowerCase().contains(motif)) {
                resultat.add(p);
            }
        }
        return resultat;
    }

    public List<Personne> descendants(Personne p) {
        verifierPresente(p);
        return p.descendants();
    }

    public List<Personne> getPersonnes() {
        return Collections.unmodifiableList(personnes);
    }

    private void verifierPresente(Personne p) {
        if (p == null || !personnes.contains(p)) {
            throw new IllegalArgumentException("Cette personne n'est pas dans l'arbre.");
        }
    }
}
