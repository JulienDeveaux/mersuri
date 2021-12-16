<script type="text/javascript"
        src="https://cdnjs.cloudflare.com/ajax/libs/mathjax/2.7.0/MathJax.js?config=TeX-AMS_CHTML"></script>
# Mesures de réseaux d'interaction

Nous allons analyser un réseau de collaboration scientifique en informatique. Le réseau est extrait de DBLP et disponible sur [SNAP](https://snap.stanford.edu/data/com-DBLP.html).

GraphStream permet de mesurer de nombreuses caractéristiques d'un réseau. La plupart de ces mesures sont implantées comme des méthodes statiques dans la classe [`Toolkit`](https://data.graphstream-project.org/api/gs-algo/current/org/graphstream/algorithm/Toolkit.html). Elles vous seront très utiles par la suite.

1. Commencez par télécharger les données et les lire avec GraphStream. GraphStream sait lire ce format. Voir [`FileSourceEdge`](https://data.graphstream-project.org/api/gs-core/current/org/graphstream/stream/file/FileSourceEdge.html) et ce [tutoriel](http://graphstream-project.org/doc/Tutorials/Reading-files-using-FileSource/). Vous pouvez essayer de visualiser le graphe mais pour cette taille ça sera très lent et très peu parlant.

    **DONE**

2. Prenez quelques mesures de base: nombre de nœuds et de liens, degré moyen, coefficient de clustering. Quel sera le coefficient de clustering pour un réseau aléatoire de la même taille et du même degré moyen ?

    **DONE**
- nombre de noeuds : 317080
- nombre de liens : 1049866
- degré moyen : 6.622089
- coefficient de clustering : 0.7186894
- coefficient de clustering du réseau aléatoire : 2.08846*10<sup>-5</sup>
- Nous observons que le coefficient de clustering moyen pour le réseau aléatoire est beaucoup plus faible que celui du réseau donné. Cela est dûe au fait que les liens entre les noeuds du réseau aléatoire ne sont forcément connectés ensemble. Cela a pour conséquence de faire un réseau très disparate et peu connecté, et donc disposant de peu de clusters.


3. Le réseau est-il connexe ? Un réseau aléatoire de la même taille et degré moyen sera-t-il connexe ? À partir de quel degré moyen un réseau aléatoire avec cette taille devient connexe ?

    **DONE**

- Le réseau a une composante connexe.
- Le réseau aléatoire de la même taille et de degré moyen identique au réseau donné ne sera pas connexe parce que pour avoir une composante géante et donc une composante connexe, il faut que le degré moyen (6.22) soit supérieur au log du nombre de noeuds (12.66).
- Pour qu'il soit connexe, il faudrait donc que le degré moyen ait un degré supérieur à 12.66

4. Calculez la distribution des degrés et tracez-la avec `gnuplot` (ou avec votre outil préféré) d'abord en échelle linéaire, ensuite en échelle log-log. Est-ce qu'on observe une ligne droite en log-log ? Que cela nous indique ? Tracez la distribution de Poisson avec la même moyenne pour comparaison. Utilisez la commande `fit` de `gnuplot` pour trouver les coefficients de la loi de puissance et tracez-la.

    **DONE**

    La distribution de degrés $`p_k = \frac{N_k}{N}`$ est la probabilité qu'un nœud choisi au hasard ait degré $`k`$. On peut utiliser [`Toolkit.degreeDistribution()`](https://data.graphstream-project.org/api/gs-algo/current/org/graphstream/algorithm/Toolkit.html#degreeDistribution(org.graphstream.graph.Graph)) pour obtenir $`N_k`$ et normaliser par la suite :

    ```java
    int[] dd = Toolkit.degreeDistribution(graph);
    for (int k = 0; k < dd.length; k++) {
      if (dd[k] != 0) {
        System.out.printf(Locale.US, "%6d%20.8f%n", k, (double)dd[k] / graph.getNodeCount());
      }
    }
    ```

    En traçant la distribution de degrés en échelle log-log on observe une ligne droite pendant plusieurs ordres de grandeur. Cela nous indique une loi de puissance :

    ```math
    p_k = C k^{-\gamma}
    ```

    On utilise ce [script](/gnuplot/plot_dd.gnu) pour tracer la distribution et estimer l'exposant de la loi de puissance.

    ![distribution des degrés log-log](/gnuplot/dd_dblp.png)

En traçant la distribution de degrés en échelle linéaire on observe une ligne courbe avec des valeurs très élevées au départ mais qui tend rapidement vers 0.

   ![distribution des degrés linéaire](/gnuplot/dd_dblp_lineaire.png)

    On a $`\gamma = 2.7 \pm 0.04`$

5. Maintenant on va calculer la distance moyenne dans le réseau. Le calcul des plus courts chemins entre toutes les paires de nœuds prendra plusieurs heures pour cette taille de réseau. C'est pourquoi on va estimer la distance moyenne par échantillonnage en faisant un parcours en largeur à partir de 1000 sommets choisis au hasard. L'hypothèse des six degrés de séparation se confirme-t-elle ? Est-ce qu'il s'agit d'un réseau petit monde ? Quelle sera la distance moyenne dans un réseau aléatoire avec les mêmes caractéristiques ? Tracez également la *distribution* des distances. Formulez une hypothèse sur la loi de cette distribution.

    **DOING**

- Nous obtenons une distance moyenne dans le réseau entre **6,3** et **7** (en faisant tourner 5 fois le programme).
- L'hypothèse des 6 degrés de séparation semble donc se confirmer.
- La formule Dmax \pm log(N)/log(< k >) nous donne 6.7 \pm 6.2 -> le réseau est donc petit monde
- La distance moyenne dans un réseau aléatoire de même caractéristique est **6.39** en utilisant la formule \\(d_{rand} = \frac{\ln{N} - \gamma}{\ln{\langle k \rangle}} + \frac{1}{2}\\)

6. Utilisez les générateurs de GraphStream pour générer un réseau aléatoire et un réseau avec la méthode d'attachement préférentiel (Barabasi-Albert) qui ont la même taille et le même degré moyen. Refaites les mesures des questions précédentes pour ces deux réseaux. Les résultats expérimentaux correspondent-ils aux prédictions théoriques ? Comparez avec le réseau de collaboration. Que peut-on conclure ?

    **TODO**

7. (*Question bonus*) S'il y a une caractéristique du réseau de collaboration que le modèle de Barabasi-Albert n'arrive pas à reproduire c'est le coefficient de clustering. Est-ce qu'on peut espérer faire mieux avec une variante de la méthode de copie :

    * Le nouveau nœud choisit au hasard un nœud `v`.
    * Ensuite il parcourt tous les voisins de `v` et se connecte à eux avec probabilité `p`.
    * À la fin il se connecte à `v`

    Essayez d'implanter un tel générateur et voir les résultats qu'il donne.

    **TODO**
