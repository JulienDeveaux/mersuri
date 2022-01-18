# Mesures de réseaux d'interaction

Nous allons analyser un réseau de collaboration scientifique en informatique. Le réseau est extrait de DBLP et disponible sur [SNAP](https://snap.stanford.edu/data/com-DBLP.html).

GraphStream permet de mesurer de nombreuses caractéristiques d'un réseau. La plupart de ces mesures sont implantées comme des méthodes statiques dans la classe [`Toolkit`](https://data.graphstream-project.org/api/gs-algo/current/org/graphstream/algorithm/Toolkit.html). Elles vous seront très utiles par la suite.

1. Commencez par télécharger les données et les lire avec GraphStream. GraphStream sait lire ce format. Voir [`FileSourceEdge`](https://data.graphstream-project.org/api/gs-core/current/org/graphstream/stream/file/FileSourceEdge.html) et ce [tutoriel](http://graphstream-project.org/doc/Tutorials/Reading-files-using-FileSource/). Vous pouvez essayer de visualiser le graphe mais pour cette taille ça sera très lent et très peu parlant.

    **DONE**

2. Prenez quelques mesures de base: nombre de nœuds et de liens, degré moyen, coefficient de clustering. Quel sera le coefficient de clustering pour un réseau aléatoire de la même taille et du même degré moyen ?

- nombre de noeuds : 317080
- nombre de liens : 1049866
- degré moyen : 6.622089
- coefficient de clustering : 0.7186894
- coefficient de clustering du réseau aléatoire : 2.08846*10<sup>-5</sup>
- Nous observons que le coefficient de clustering moyen pour le réseau aléatoire est beaucoup plus faible que celui du réseau donné. Cela est dûe au fait que les liens entre les noeuds du réseau aléatoire ne sont forcément connectés ensemble. Cela a pour conséquence de faire un réseau très disparate et peu connecté, et donc disposant de peu de clusters.

3. Le réseau est-il connexe ? Un réseau aléatoire de la même taille et degré moyen sera-t-il connexe ? À partir de quel degré moyen un réseau aléatoire avec cette taille devient connexe ?

- Le réseau a une composante connexe.
- Le réseau aléatoire de la même taille et de degré moyen identique au réseau donné ne sera pas connexe parce que pour avoir une composante géante et donc une composante connexe, il faut que le degré moyen (6.22) soit supérieur au log du nombre de noeuds (12.66).
- Pour qu'il soit connexe, il faudrait donc que le degré moyen ait un degré supérieur à 12.66.

4. Calculez la distribution des degrés et tracez-la avec `gnuplot` (ou avec votre outil préféré) d'abord en échelle linéaire, ensuite en échelle log-log. Est-ce qu'on observe une ligne droite en log-log ? Que cela nous indique ? Tracez la distribution de Poisson avec la même moyenne pour comparaison. Utilisez la commande `fit` de `gnuplot` pour trouver les coefficients de la loi de puissance et tracez-la.

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

- Nous obtenons une distance moyenne dans le réseau entre **6,3** et **7** (en faisant tourner 5 fois le programme).
- L'hypothèse des 6 degrés de séparation semble donc se confirmer.
- La formule $`Dmax \pm log(N)/log(<k>)`$ nous donne **6.7** $`\pm`$ 6.2 -> le réseau est donc petit monde.
- La distance moyenne dans un réseau aléatoire de même caractéristique est **6.39** en utilisant la formule $`d_r = (log(N) - \gamma)/(log(<k>) + 1/2`$ avec $`\gamma`$ la constante d'Euler $`\pm`$ 0.57722.

Pour tracer le graphique de la distribution des distances, nous allons cette fois utiliser ce code-ci, pour avoir la somme des distances en fonction de leur apparition : 
```java
    int[] barreGraphData = new int[tailleMaxDistance+1];
    for(int i = 0; i < distanceDistribution.size(); i++) {
        barreGraphData[distanceDistribution.get(i)]++;
    }
    FileWriter fw = new FileWriter("../../../gnuplot/dd_dblp_dist.dat");
    String txt = "";
    for (int k = 0; k < tailleMaxDistance; k++) {
        if (distanceDistribution.get(k) != 0) {
            txt += String.format("%d %d\n", k, barreGraphData[k]);
        }
    }
    fw.write(txt);
    fw.close();
```
Nous obtenons ce graphique :

![distribution des distances en fonction de leur occurence](/gnuplot/dd_dblp_distrib_distance.png)

- Nous pouvons donc formuler l'hypothèse, en regardant ce graphique, que la distribution des distances suit une loi de poisson.

6. Utilisez les générateurs de GraphStream pour générer un réseau aléatoire et un réseau avec la méthode d'attachement préférentiel (Barabasi-Albert) qui ont la même taille et le même degré moyen. Refaites les mesures des questions précédentes pour ces deux réseaux. Les résultats expérimentaux correspondent-ils aux prédictions théoriques ? Comparez avec le réseau de collaboration. Que peut-on conclure ?

En exécutant les tests un nombre significatif de fois ($`\pm`$**15** fois), nous observons : 
- Le degré moyen du réseau random est légèrement plus élevé en moyenne (**6.995**) que le degré moyen du réseau donné (**6.622**).
- Le coefficient de clusturing du réseau random est en moyenne inférieure (**0.581**) à celui du réseau donné (**0.718**)
- Le réseau random est à chaque fois connexe tout comme le réseau donné et ont à chaque fois qu'une seule composante connexe
- La distance moyenne dans le réseau random est inférieure (**5.055**) à la distance moyenne du réseau donné (**6.932**)

Ces résultats sont très légèrement en dehors de ce que l'on pouvait attendre en termes de résultat.
Cela est surement dû au fait que le réseau donné soit un réseau qui "a un sens", qui est construit pour servir une utilité et est donc façonné d'une certaine manière.
Ici, nos réseaux aléatoires, bien qu'ils soient construits sur la méthode d'attachement préférentiel de Barabasi-Albert, qui tente de recréer les réseaux de connexion entre personnes, nous observons que cette approche, bien que ressemblante à notre réseau, n'est pas assez proche de l'original dans certains domaines (coefficient de clusturing et distance moyenne).

7. (*Question bonus*) S'il y a une caractéristique du réseau de collaboration que le modèle de Barabasi-Albert n'arrive pas à reproduire c'est le coefficient de clustering. Est-ce qu'on peut espérer faire mieux avec une variante de la méthode de copie :

    * Le nouveau nœud choisit au hasard un nœud `v`.
    * Ensuite il parcourt tous les voisins de `v` et se connecte à eux avec probabilité `p`.
    * À la fin il se connecte à `v`

    Essayez d'implanter un tel générateur et voir les résultats qu'il donne.


**DONE**
 
Avec ce nouveau générateur, nous obtenons des résultats parfois différents, parfois ressemblants au réseau initial avec un nombre de noeud identique et un nombre de liens ressemblant (probabilité de **0.55%** de création de lien).
- Le degré moyen est ressemblant (**6.21** pour mon générateur; **6.62** pour le réseau donné).
- le coefficient de clusturing est plus faible avec mon générateur (**0.49**; **0.71**).
- le réseau généré a énormément de composantes connexes (**58965; 1**).
- la distance moyenne dans le réseau généré est négative, ce qui indique que beaucoup de noeuds sont isolés(**-0.8**; **6.43**).

Nous en concluons donc que ce nouveau générateur est ressemblant, mais toujours éloigné de notre réseau initial, surtout au niveau des noeuds isolés.

##Proppagation d'un virus
Les consignes sont les mêmes que pour le premier TP. On travaille sur les mêmes données et la problématique est proche. Utilisez donc le même dépôt sur la forge.

Nos collaborateurs scientifiques communiquent souvent par mail. Malheureusement pour eux, les pièces jointes de ces mails contiennent parfois des virus informatiques. On va étudier la propagation d'un virus avec les hypothèses suivantes :

Un individu envoie en moyenne un mail par semaine à chacun de ses collaborateurs.
Un individu met à jour son anti-virus en moyenne deux fois par mois. Cela nettoie son système mais ne le protège pas de nouvelles infections car le virus mute.
L'épidémie commence avec un individu infecté (patient zéro).

L'échelle de temps utilisé sera le jour.

1. Quel est le taux de propagation du virus ? Quel est le seuil épidémique du réseau ? Comparez avec le seuil théorique d'un réseau aléatoire du même degré moyen.
- Le taux de propagation du virus est de 2 ($`\beta = 1/7 et Mu \pm 1/14, donc \gamma = 1/7/1/14 = 2`$)
- Le seuil épidémique du réseau est de 0.045, qui est strictement inférieur à 2($`\gamma <sub>c</sub> = <k>/<k²> \pm 6.6/144 = 0.045 << 2`$). L'épidémie va donc se propager rapidement
- Le seuil théorique d'un réseau aléatoire de même degré ($`\gamma = 1/(<k>+1) \pm 0.131`$)

**DONE**

2. Simulez la propagation du virus jour par jour pendant trois mois avec les scénarios suivants :
   - On ne fait rien pour empêcher l'épidémie
   - On réussit à convaincre 50 % des individus de mettre à jour en permanence leur anti-virus (immunisation aléatoire)
   - On réussit à convaincre 50 % des individus de convaincre un de leurs contacts de mettre à jour en permanence son anti-virus (immunisation sélective).
   - Pour chacun des trois scénarios, tracez l'évolution de la fraction d'infectés de la population non immunisée. Que peut-on conclure ?

   Attention : La réalisation d'un scénario autour des valeurs critiques est sensible aux conditions initiales. Simulez plusieurs fois chaque scénario afin d'identifier le déroulement typique.


   **DONE**


3. Pour justifier l'efficacité de l'immunisation sélective, calculez le degré moyen des groupes 0 et 1. Comment expliquez-vous la différence ?  

Moyenne Gr0 : 4.48

Moyenne Gr1 : 7.7

La différence entre ces groupes est que le groupe 1 immunisé ayant un plus grand nombre de voisins, permets de ralentir plus efficacement le virus, en immunisant les noeuds à plus fort degré.

**DONE**


4. Du point de vue du virus l'immunisation d'un nœud est équivalente à sa suppression du réseau. Calculez le seuil épidémique du réseau modifié pour chacune des deux stratégies d'immunisation et comparez avec le seuil épidémique du réseau initial.  

Ici, pour le premier scénario, $`<k> = 82203`$ et $`<k²> = 6757333209`$. Pour le second scénario, $`<k> = 163158`$ et $`<k²> = 26620532964`$.
- Le seuil épidémique du réseau lors de la première stratégie est de **1.21e<sup>-5</sup>** qui est inférieur à 2, le taux de propagation du virus. L'épidémie va donc se propager.
- Le seuil épidémique du réseau lors de la deuxième stratégie est de **6.12e<sup>-6</sup>** qui est inférieur à 2, l'épidémie va donc se propager.

Nous voyons ici, que le seuil épidémique du scénario 3 est plus petit que celui du scénario 2, il y aura donc théoriquement plus de cas dans le scénario 3, ce qui est le cas.

**DONE**

5. Simulez l'épidémie avec les mêmes hypothèses et les mêmes scénarios dans un réseau aléatoire et un réseau généré avec la méthode d'attachement préférentiel de la même taille et le même degré moyen. Comparez et commentez les résultats.  


**TODO**