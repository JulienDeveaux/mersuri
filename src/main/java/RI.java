import org.graphstream.algorithm.ConnectedComponents;
import org.graphstream.algorithm.Toolkit;
import org.graphstream.algorithm.generator.BarabasiAlbertGenerator;
import org.graphstream.algorithm.generator.Generator;
import org.graphstream.graph.BreadthFirstIterator;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.DefaultGraph;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.stream.file.FileSource;
import org.graphstream.stream.file.FileSourceEdge;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

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
        Graph graph = new DefaultGraph("graph");
        graph.setAttribute("ui.stylesheet", styleSheet);
        System.setProperty("org.graphstream.ui", "swing");
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

        Graph randomgraph = new SingleGraph("Random graph");
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
        System.out.println("Distance moyenne dans le réseau aléatoire de même taille : " + distanceMoyenneAlea);


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
    }
}
