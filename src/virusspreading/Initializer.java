package virusspreading;

import peersim.config.Configuration;
import peersim.core.CommonState;
import peersim.core.Network;
import peersim.core.Node;

import java.util.Collections;
import java.util.LinkedList;

/*
  Module d'initialisation de helloWorld: 
  Fonctionnement:
    pour chaque noeud, le module fait le lien entre la couche transport et la couche applicative
    ensuite, il fait envoyer au noeud 0 un message "Hello" a tous les autres noeuds
 */

/* ===== Initialization module of Virus Spreading =====

    For every nodes we give it up to 10 neighbors randomly selected

*/
public class Initializer implements peersim.core.Control {

    public static int applicativePID;

    public static int peopleMetPerDay;
    public static double chanceBeingInfected;
    public static int sizeNetwork;
    public static double chanceToGoOut;
    public static int nbNeighbors;
    public static int timeVaccineFound;
    public static double chanceGetVaccine;
    public static double chanceToDie;
    public static long incubationPeriod;

    private LinkedList<Integer> ids;

    public Initializer(String prefix) {

        // Get the pid of the applicative layer
        applicativePID = Configuration.getPid(prefix + ".protocolPid");
        peopleMetPerDay = Configuration.getInt(prefix + ".peopleMetPerDay");
        chanceBeingInfected = Configuration.getDouble(prefix + ".chanceBeingInfected");
        chanceToGoOut = Configuration.getDouble(prefix + ".chanceToGoOut");
        chanceGetVaccine = Configuration.getDouble(prefix + ".chanceGetVaccine");
        chanceToDie = Configuration.getDouble(prefix + ".chanceToDie");
        nbNeighbors = Configuration.getInt(prefix + ".nbNeighbors");
        timeVaccineFound = Configuration.getInt(prefix + ".timeVaccineFound");
        incubationPeriod = Configuration.getLong(prefix + ".incubationPeriod");

        // Size of the network
        sizeNetwork = Network.size();

        // This one is used to generate the neighbors (which is an arraylist of id)
        this.ids = new LinkedList<>();
        for (int i = 0; i < sizeNetwork; i++) {
            this.ids.add(i);
        }
    }

    // Main method of the Initializer
    public boolean execute() {
        Individual emitter, current;
        Node dest;
        Node thisNode;
        Message helloMsg;

        Individual currentIndividual;

        if (sizeNetwork < 1) {
            System.err.println("Network size is not positive");
            System.exit(1);
        }

        // For every nodes we link the applicative layer to the transport layer
        for (int i = 0; i < sizeNetwork; i++) {
            dest = Network.get(i);
            current = (Individual) dest.getProtocol(applicativePID);
            current.setTransportLayer(i);

            // Set the first one as the infected
            if (i == 0) {
                current.setInfected();
            }

            // We need to find a list of neighbors for every of them
            Collections.shuffle(this.ids);
            current.getNeighbors().addAll(this.ids.subList(0, nbNeighbors));

            // And we remove himself (if he exists)
            current.getNeighbors().remove((Integer) i);
        }

        System.out.println("Initialization completed");
        System.out.println("Day 0 : Patient 0 has arrived");

        return false;
    }
}