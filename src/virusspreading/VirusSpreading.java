package virusspreading;

import peersim.config.Configuration;
import peersim.core.Network;
import peersim.core.Node;
import peersim.edsim.EDProtocol;

public class VirusSpreading implements EDProtocol {

    // Id of the transport layer
    private int transportPid;

    // Object of the transport layer
    private HWTransport transport;

    // Identifier of the current layer (the applicative one)
    private int mypid;

    // The id of the node
    private int nodeId;

    // Prefix of the layer (name of the variable of the layer given by the config file)
    private String prefix;

    // Constructor
    public VirusSpreading(String prefix) {
        this.prefix = prefix;

        // Initialization of the ids from the config file
        this.transportPid = Configuration.getPid(prefix + ".transport");
        this.mypid = Configuration.getPid(prefix + ".myself");
        this.transport = null;
    }

    // Method called when a message is received by the layer VirusSpreading of the node
    public void processEvent(Node node, int pid, Object event) {
        this.receive((Message) event);
    }

    // Method needed for the creation of the network (by cloning)
    @Override
    public Object clone() {
        return new VirusSpreading(this.prefix);
    }

    // Link between a Object of the applicative layer and an Object from the transport layer which is on the same node
    public void setTransportLayer(int nodeId) {
        this.nodeId = nodeId;
        this.transport = (HWTransport) Network.get(this.nodeId).getProtocol(this.transportPid);
    }

    //envoi d'un message (l'envoi se fait via la couche transport)
    public void send(Message msg, Node dest) {
        this.transport.send(getMyNode(), dest, msg, this.mypid);
    }

    //affichage a la reception
    private void receive(Message msg) {
        System.out.println(this + ": Received " + msg.getContent());
    }

    //retourne le noeud courant
    private Node getMyNode() {
        return Network.get(this.nodeId);
    }

    public String toString() {
        return "Node " + this.nodeId;
    }


}