import org.graphstream.algorithm.ConnectedComponents;
import org.graphstream.algorithm.generator.BarabasiAlbertGenerator;
import org.graphstream.algorithm.generator.Generator;
import org.graphstream.graph.BreadthFirstIterator;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.DefaultGraph;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.stream.file.FileSource;
import org.graphstream.stream.file.FileSourceEdge;


import java.io.IOException;
import java.util.*;

public class RI {
    protected static String styleSheet =
            "node {" +
                    "   size: 10px, 10px;" +
                    "   text-size: 15px;" +
                    "	fill-color: grey, red;" +
                    "   text-background-mode: rounded-box;" +
                    "   text-background-color: orange;" +
                    "   text-alignment: above;" +
                    "   fill-mode: gradient-radial;" +
                    "}" +
                    "edge {" +
                    "   text-alignment: above;" +
                    "   text-background-color: orange;" +
                    "   text-background-mode: rounded-box;" +
                    "   text-size: 20px;" +
                    "}";

    public static void main(String[] args) throws IOException {
        System.setProperty("org.graphstream.ui", "swing");
        /*Graph monGenerateur = new DefaultGraph("BarabasiAlbertVariant");
        monGenerateur.setAttribute("ui.stylesheet", styleSheet);
        //monGenerateur.display();
        barabasiAlbertVariantGenerator(monGenerateur, 317080, 0.55f);
        System.out.println("Résultats de mon générateur : ");
        System.out.println("node count : " + monGenerateur.getNodeCount() + " / edge count : " + monGenerateur.getEdgeCount());
        float degreMoyenGenerateur = 0;
        for(int i = 0; i < monGenerateur.getNodeCount(); i++) {
            degreMoyenGenerateur += monGenerateur.getNode(i).getDegree();
        }
        degreMoyenGenerateur /= monGenerateur.getNodeCount();
        System.out.println("Degre moyen : " + degreMoyenGenerateur);

        float coefClusturingGenerateur = 0;
        for(int i = 0; i < monGenerateur.getNodeCount(); i++) {
            Node n = monGenerateur.getNode(i);
            if(n.getDegree() == 1 || n.getDegree() == 0) {
                coefClusturingGenerateur += 0;
            } else {
                coefClusturingGenerateur += (2.0 * n.edges().count() / (n.getDegree() * (n.getDegree() - 1)));
            }
        }
        coefClusturingGenerateur /= monGenerateur.getNodeCount();
        System.out.println("Coefficient de clusturing : " + coefClusturingGenerateur);

        ConnectedComponents composantesConnexesGenerateur = new ConnectedComponents();
        composantesConnexesGenerateur.init(monGenerateur);
        if(composantesConnexesGenerateur.getConnectedComponentsCount() > 0) {
            System.out.println("Le réseau généré est connexe; trouvées : " + composantesConnexesGenerateur.getConnectedComponentsCount());
        } else {
            System.out.println("Le réseau généré n'est pas connexe");
        }

        int nbNoeudsParcoursGenerateur = 10;
        int tailleMaxDistanceGenere = 0;
        List<Node> listGenere = new LinkedList<>();   // Liste des nbNoeudsParcours noeuds aléatoires
        for(int i = 0; i < nbNoeudsParcoursGenerateur+1; i++) {
            int r = (int) (Math.random() * monGenerateur.getNodeCount());
            if(listGenere.contains(monGenerateur.getNode(r))) {       // Evite les doublons
                i--;
            } else {
                listGenere.add(monGenerateur.getNode(r));
            }
        }
        double distanceMoyenneGenere = 0;
        for(int i = 0; i < nbNoeudsParcoursGenerateur+1; i++) {
            BreadthFirstIterator breadthFirstIteratorGenere = new BreadthFirstIterator(listGenere.get(i));
            while(breadthFirstIteratorGenere.hasNext()) {
                breadthFirstIteratorGenere.next();
            }
            for(int j = 0; j < monGenerateur.getNodeCount(); j++) {
                distanceMoyenneGenere += breadthFirstIteratorGenere.getDepthOf(monGenerateur.getNode(j));
                if(breadthFirstIteratorGenere.getDepthOf(monGenerateur.getNode(j)) > tailleMaxDistanceGenere) {
                    tailleMaxDistanceGenere = breadthFirstIteratorGenere.getDepthOf(monGenerateur.getNode(j));
                }
            }
        }
        distanceMoyenneGenere = distanceMoyenneGenere/(listGenere.size()*monGenerateur.getNodeCount());
        System.out.println("Distance moyenne dans le réseau généré : " + distanceMoyenneGenere);

        System.out.println("Dmax généré : " + Math.log(monGenerateur.getNodeCount())/Math.log(degreMoyenGenerateur));*/





        //System.out.println("\nTests sur le réseau donné et sur la variante random : ");
        Graph graph = new DefaultGraph("graph");
        graph.setAttribute("ui.stylesheet", styleSheet);
        String filePath = "src/main/resources/com-dblp.ungraph.txt";
        FileSource fs = new FileSourceEdge();
        fs.addSink(graph);
        try{
            fs.readAll(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("node count : " + graph.getNodeCount() + " / edge count : " + graph.getEdgeCount());

        float degreMoyen = 0;
        for(int i = 0; i < graph.getNodeCount(); i++) {
            degreMoyen += graph.getNode(i).getDegree();
        }
        degreMoyen /= graph.getNodeCount();
        System.out.println("Degre moyen : " + degreMoyen);

        /*Graph randomgraph = new SingleGraph("Random graph");
        Generator gen = new BarabasiAlbertGenerator((int)degreMoyen);
        gen.addSink(randomgraph);
        gen.begin();
        for(int i = 0; i < graph.getNodeCount(); i++) {
            gen.nextEvents();
        }
        gen.end();

        float degreMoyenRandom = 0;
        for(int i = 0; i < randomgraph.getNodeCount(); i++) {
            degreMoyenRandom += randomgraph.getNode(i).getDegree();
        }
        degreMoyenRandom /= randomgraph.getNodeCount();
        System.out.println("Degre moyen réseau random : " + degreMoyenRandom);

        float coefClusturing = 0;
        for(int i = 0; i < graph.getNodeCount(); i++) {
            Node n = graph.getNode(i);
            if(n.getDegree() == 1 || n.getDegree() == 0) {
                coefClusturing += 0;
            } else {
                coefClusturing += (2.0 * n.edges().count() / (n.getDegree() * (n.getDegree() - 1)));
            }
        }
        coefClusturing /= graph.getNodeCount();
        System.out.println("Coefficient de clusturing : " + coefClusturing);

        float coefClusturingAlea = degreMoyen / graph.getNodeCount();
        System.out.println("Coefficiend de clusturing dans un graph aléatoire de même degré moyen : " + coefClusturingAlea);

        float coefClusturingRandom = 0;
        for(int i = 0; i < randomgraph.getNodeCount(); i++) {
            Node n = randomgraph.getNode(i);
            if(n.getDegree() == 1 || n.getDegree() == 0) {
                coefClusturingRandom += 0;
            } else {
                coefClusturingRandom += (2.0 * n.edges().count() / (n.getDegree() * (n.getDegree() - 1)));
            }
        }
        coefClusturingRandom /= randomgraph.getNodeCount();
        System.out.println("Coefficient de clusturing réseau random : " + coefClusturingRandom);

        ConnectedComponents composantesConnexes = new ConnectedComponents();
        composantesConnexes.init(graph);
        if(composantesConnexes.getConnectedComponentsCount() > 0) {
            System.out.println("Le réseau est connexe; trouvées : " + composantesConnexes.getConnectedComponentsCount());
        } else {
            System.out.println("Le réseau n'est pas connexe");
        }

        ConnectedComponents composantesConnexesRandom = new ConnectedComponents();
        composantesConnexesRandom.init(randomgraph);
        if(composantesConnexesRandom.getConnectedComponentsCount() > 0) {
            System.out.println("Le réseau random est connexe; trouvées : " + composantesConnexesRandom.getConnectedComponentsCount());
        } else {
            System.out.println("Le réseau random n'est pas connexe");
        }

        if(degreMoyen > Math.log(graph.getNodeCount())) {
            System.out.println("Le graph aléatoire de même taille et degré moyen est connexe");
        } else {
            System.out.println("Le graph aléatoire de même taille et degré moyen est pas connexe");
        }


        int nbNoeudsParcours = 10;
        List<Integer> distanceDistribution = new ArrayList<>();
        int tailleMaxDistance = 0;
        List<Node> list = new LinkedList<>();   // Liste des nbNoeudsParcours noeuds aléatoires
        for(int i = 0; i < nbNoeudsParcours+1; i++) {
            int r = (int) (Math.random() * graph.getNodeCount());
            if(list.contains(graph.getNode(r))) {       // Evite les doublons
                i--;
            } else {
                list.add(graph.getNode(r));
            }
        }
        double distanceMoyenne = 0;
        for(int i = 0; i < nbNoeudsParcours+1; i++) {
            BreadthFirstIterator breadthFirstIterator = new BreadthFirstIterator(list.get(i));
            while(breadthFirstIterator.hasNext()) {
                breadthFirstIterator.next();
            }
            for(int j = 0; j < graph.getNodeCount(); j++) {
                distanceMoyenne += breadthFirstIterator.getDepthOf(graph.getNode(j));
                distanceDistribution.add(breadthFirstIterator.getDepthOf(graph.getNode(j)));
                if(breadthFirstIterator.getDepthOf(graph.getNode(j)) > tailleMaxDistance) {
                    tailleMaxDistance = breadthFirstIterator.getDepthOf(graph.getNode(j));
                }
            }
        }
        distanceMoyenne = distanceMoyenne/(list.size()*graph.getNodeCount());
        System.out.println("Distance moyenne dans le réseau : " + distanceMoyenne);

        System.out.println("Dmax : " + Math.log(graph.getNodeCount())/Math.log(degreMoyen));


        int nbNoeudsParcoursRandom = 10;
        int tailleMaxDistanceRandom = 0;
        List<Node> listRandom = new LinkedList<>();   // Liste des nbNoeudsParcours noeuds aléatoires
        for(int i = 0; i < nbNoeudsParcoursRandom+1; i++) {
            int r = (int) (Math.random() * randomgraph.getNodeCount());
            if(listRandom.contains(randomgraph.getNode(r))) {       // Evite les doublons
                i--;
            } else {
                listRandom.add(randomgraph.getNode(r));
            }
        }
        double distanceMoyenneRandom = 0;
        for(int i = 0; i < nbNoeudsParcoursRandom+1; i++) {
            BreadthFirstIterator breadthFirstIteratorRandom = new BreadthFirstIterator(listRandom.get(i));
            while(breadthFirstIteratorRandom.hasNext()) {
                breadthFirstIteratorRandom.next();
            }
            for(int j = 0; j < randomgraph.getNodeCount(); j++) {
                distanceMoyenneRandom += breadthFirstIteratorRandom.getDepthOf(randomgraph.getNode(j));
                if(breadthFirstIteratorRandom.getDepthOf(randomgraph.getNode(j)) > tailleMaxDistanceRandom) {
                    tailleMaxDistanceRandom = breadthFirstIteratorRandom.getDepthOf(randomgraph.getNode(j));
                }
            }
        }
        distanceMoyenneRandom = distanceMoyenneRandom/(listRandom.size()*randomgraph.getNodeCount());
        System.out.println("Distance moyenne dans le réseau random : " + distanceMoyenneRandom);


        float eulerConstant = (float) 0.5772156649015328606065120900824024310421;
        double distanceMoyenneAlea = ((Math.log(graph.getNodeCount()) - eulerConstant) / Math.log(degreMoyen)) + 1/2f;
        System.out.println("Distance moyenne dans le réseau aléatoire de même taille : " + distanceMoyenneAlea);*/


        /*int[] barreGraphData = new int[tailleMaxDistance+1];
        for(int i = 0; i < distanceDistribution.size(); i++) {
            barreGraphData[distanceDistribution.get(i)]++;
        }
        FileWriter fw = new FileWriter("/home/julien/Documents/M1/Réseau Interaction/mesureri/gnuplot/dd_dblp_dist.dat");
        String txt = "";
        for (int k = 0; k < tailleMaxDistance; k++) {
            if (distanceDistribution.get(k) != 0) {
                txt += String.format("%d %d\n", k, barreGraphData[k]);
            }
        }
        fw.write(txt);
        fw.close();*/



        /* Partie propagation */
        float degreMoyenCarre = 0;
        for(int i = 0; i < graph.getNodeCount(); i++) {
            degreMoyenCarre += graph.getNode(i).getDegree() * graph.getNode(i).getDegree();
        }
        degreMoyenCarre /= graph.getNodeCount();
        System.out.println("moyenne des degrés <k²> : " + degreMoyenCarre);
        System.out.println("Seuil épidémique lambdaC = <k>/<k²> : " + degreMoyen / degreMoyenCarre);
        if(degreMoyen / degreMoyenCarre > (1/7/1/14)) {     //1/7 pour un mail par semaine et 1/14 pour la mise à jour antivirus une fois toutes les deux semaines
            System.out.println("La maladie persistera");
        } else {
            System.out.println("La maladie disparaitra");
        }
        System.out.println("Le seuil épidémique pour un réseau aléatoire de même  degré est de : " + (1/(degreMoyen+1)));

        /* Simulations */

        /* --- On ne fait rien --- */
        int nbPasse = 5;
        int[][] resultats = new int[nbPasse][90];
        for(int f = 0; f < nbPasse; f++) {
            graph.getNode(0).setAttribute("I", true);   // Le noeud 0 est le patient 0
            graph.getNode(0).setAttribute("flush", (int)(Math.random()*14));
            for(int j = 0; j < graph.getNode(0).getDegree(); j++) {
                graph.getNode(0).setAttribute("mail"+j, (int)(Math.random()*7));
            }
            for(int i = 1; i < graph.getNodeCount(); i++) {
                Node n = graph.getNode(i);
                n.setAttribute("I", false);
                n.setAttribute("flush", (int)(Math.random()*14));   //Mise a jour pas en même temps pour tous le monde
                for(int j = 0; j < graph.getNode(i).getDegree(); j++) {
                    n.setAttribute("mail"+j, (int)(Math.random()*7));
                }
            }
            int nbContamines = 1;

            System.out.println("PASSE " + f);
            for (int i = 0; i < 90; i++) {
                for (int j = 0; j < graph.getNodeCount(); j++) {
                    Node n = graph.getNode(j);

                    if (n.getAttribute("flush").equals(0)) {             // Si c'est le jour de l'antivirus
                        if (n.getAttribute("I").equals(true)) {
                            n.setAttribute("I", false);
                            nbContamines--;
                        }
                        n.setAttribute("flush", (int) (Math.random() * 14));
                    } else {
                        n.setAttribute("flush", (int) n.getAttribute("flush") - 1);
                        List<Node> voisins = new ArrayList<>();
                        for (int h = 0; h < n.getDegree(); h++) {      // Recherche des voisins
                            voisins.add(n.getEdge(h).getOpposite(n));
                        }
                        for (int h = 0; h < voisins.size(); h++) {
                            if (n.getAttribute("mail" + h).equals(0)) {
                                n.setAttribute("mail" + h, (int) (Math.random() * 7));
                                if (n.getAttribute("I").equals(true)) {
                                    voisins.get(h).setAttribute("I", true);
                                    nbContamines++;
                                }
                            } else {
                                n.setAttribute("mail" + h, (int) n.getAttribute("mail" + h) - 1);
                            }
                        }
                        if (n.getAttribute("I").equals(true)) {
                            nbContamines++;
                        }
                    }
                }
                resultats[f][i] = nbContamines;
                System.out.println("NbContaminés jour " + i + " : " + nbContamines);
            }
            nbContamines = 1;
        }
        System.out.println("Résumé en moyenne : ");
        for(int i = 0; i < 90; i++) {
            int moy = 0;
            for(int j = 0; j < nbPasse; j++) {
                moy += resultats[j][i];
            }
            moy/=nbPasse;
            System.out.println((i+1) + " " + moy);
        }
        /* --- On réussit à convaincre 50 % des individus de mettre à jour en permanence leur anti-virus (immunisation aléatoire) --- */

        /* --- On réussit à convaincre 50 % des individus de convaincre un de leurs contacts de mettre à jour en permanence son anti-virus (immunisation sélective) --- */

        /* Fin simulations */
    }

    static void barabasiAlbertVariantGenerator(Graph graphGenere, int nodeCount, float probabilite) {
        int itNode = 0;
        int itEdge = 0;
        while(itNode != nodeCount) {
            if(graphGenere.getNodeCount() == 0) {
                graphGenere.addNode("" + itNode);               // Ajout du noeud initial
                itNode++;
            } else {
                graphGenere.addNode("" + itNode);               // On ajoute un noeud qui sera relié à un autre plus tard avec la probabilité donné
                Node newNode = graphGenere.getNode(itNode);
                itNode++;

                Node randomNode = graphGenere.getNode((int) (Math.random() * itNode));
                while (graphGenere.getNode(randomNode.getIndex()) == graphGenere.getNode(newNode.getIndex())) {     // Vérifie que randomNode != newNode
                    randomNode = graphGenere.getNode((int) (Math.random() * itNode));
                }
                List<Node> voisins = new ArrayList<>();
                for (int i = 0; i < randomNode.getDegree(); i++) {      // Recherche des voisins
                    voisins.add(randomNode.getEdge(i).getOpposite(randomNode));
                }
                if (voisins.size() == 0) {       // cas ou le noeud random choisi est isolé
                    graphGenere.addEdge(String.valueOf(itEdge), newNode, randomNode);
                    itEdge++;
                } else {
                    Random r = new Random();
                    for (int i = 0; i < voisins.size(); i++) {
                        float num = r.nextFloat();
                        if (num < probabilite) {         // on se connecte dessus si la valeur du random est inférieur à notre probabilité
                            graphGenere.addEdge(String.valueOf(itEdge), newNode, voisins.get(i));
                            itEdge++;
                        }
                    }
                }
            }
        }
    }
}



/*
taux de propagation du virus sis :
betha = 1/7
Mu = 1/14
lambda = 1/7/1/14 = 2

seuil épidémique du réseau :
lambda de c = <k>/<k²> = 6.6/
*/