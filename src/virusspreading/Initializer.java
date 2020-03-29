package virusspreading;

import peersim.config.Configuration;
import peersim.core.CommonState;
import peersim.core.Network;
import peersim.core.Node;

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

    public Initializer(String prefix) {

        // Get the pid of the applicative layer
        applicativePID = Configuration.getPid(prefix + ".protocolPid");
        peopleMetPerDay = Configuration.getInt(prefix + ".peopleMetPerDay");
        chanceBeingInfected = Configuration.getDouble(prefix + ".chanceBeingInfected");
        chanceToGoOut = Configuration.getDouble(prefix + ".chanceToGoOut");

    }

    // Main method of the Initializer
    public boolean execute() {
        Individual emitter, current;
        Node dest;
        Node thisNode;
        Message helloMsg;

        Individual currentIndividual;

        // Size of the network
        sizeNetwork = Network.size();

        // Creation of the message
        helloMsg = new Message(Message.MessageType.MESSAGE, "Hello!!");

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
        }

        System.out.println("Initialization completed");

        System.out.println("Day 0 : Patient 0 has arrived");

        return false;
    }
}