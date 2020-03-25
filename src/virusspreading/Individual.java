package virusspreading;

import peersim.config.Configuration;
import peersim.core.Network;
import peersim.core.Node;
import peersim.edsim.EDProtocol;

import java.util.ArrayList;

public class Individual implements EDProtocol {

    // Id of the transport layer
    private int transportPid;

    // Object of the transport layer
    private Transport transport;

    // Identifier of the current layer (the applicative one)
    private int mypid;

    // The id of the node
    private int nodeId;

    // Prefix of the layer (name of the variable of the layer given by the config file)
    private String prefix;

    // List of neighbors
    private ArrayList<Node> neighbors;

    // Constructor
    public Individual(String prefix) {
        this.prefix = prefix;

        // Initialization of the ids from the config file
        this.transportPid = Configuration.getPid(prefix + ".transport");
        this.mypid = Configuration.getPid(prefix + ".myself");
        this.transport = null;
        this.neighbors = new ArrayList<>();
    }

    // Method called when a message is received by the layer VirusSpreading of the node
    @Override
    public void processEvent(Node node, int pid, Object event) {
        this.receive((Message) event);
    }

    // Method needed for the creation of the network (by cloning)
    @Override
    public Object clone() {
        return new Individual(this.prefix);
    }

    // Link between a Object of the applicative layer and an Object from the transport layer which is on the same node
    public void setTransportLayer(int nodeId) {
        this.nodeId = nodeId;
        this.transport = (Transport) Network.get(this.nodeId).getProtocol(this.transportPid);
    }

    // Send a message via the transport layer
    public void send(Message msg, Node dest) {
        msg.setContent(msg.getContent());
        this.transport.send(getMyNode(), dest, msg, this.mypid);
    }

    // Do something when a message is received
    private void receive(Message msg) {
        System.out.println(this + ": Received " + msg.getContent());
    }

    // Get the current node
    public Node getMyNode() {
        return Network.get(this.nodeId);
    }

    @Override
    public String toString() {
        return "Node " + this.nodeId;
    }

    public int getTransportPid() {
        return transportPid;
    }

    public void setTransportPid(int transportPid) {
        this.transportPid = transportPid;
    }

    public Transport getTransport() {
        return transport;
    }

    public void setTransport(Transport transport) {
        this.transport = transport;
    }

    public int getMypid() {
        return mypid;
    }

    public void setMypid(int mypid) {
        this.mypid = mypid;
    }

    public int getNodeId() {
        return nodeId;
    }

    public void setNodeId(int nodeId) {
        this.nodeId = nodeId;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public ArrayList<Node> getNeighbors() {
        return neighbors;
    }

    public void setNeighbors(ArrayList<Node> neighbors) {
        this.neighbors = neighbors;
    }
}