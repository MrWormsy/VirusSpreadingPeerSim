package virusspreading;

import peersim.config.Configuration;
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

    private int pid;

    public Initializer(String prefix) {

        // Get the pid of the applicative layer
        this.pid = Configuration.getPid(prefix + ".protocolPid");
    }

    // Main method of the Initializer
    public boolean execute() {
        int sizeNetwork;
        HelloWorld emitter, current;
        Node dest;
        Message helloMsg;

        // Size of the network
        sizeNetwork = Network.size();

        // Creation of the message
        helloMsg = new Message(Message.HELLOWORLD, "Hello!!");

        if (sizeNetwork < 1) {
            System.err.println("Network size is not positive");
            System.exit(1);
        }

        // Get the applicative layer of the emitter (node 0)
        emitter = (HelloWorld) Network.get(0).getProtocol(this.pid);
        emitter.setTransportLayer(0);

        // For every nodes, we make a link between the applicative layer et and the transport layer then we send a message to the node 0
        for (int i = 1; i < sizeNetwork; i++) {
            dest = Network.get(i);
            current = (HelloWorld) dest.getProtocol(this.pid);
            current.setTransportLayer(i);
            emitter.send(helloMsg, dest);
        }

        System.out.println("Initialization completed");
        return false;
    }
}