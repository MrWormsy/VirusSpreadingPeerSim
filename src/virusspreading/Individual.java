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

    // Know if the person is infected or not
    private boolean infected;

    // Know if the person is alive or not
    private boolean alive;

    // ===== Constructor =====
    public Individual(String prefix) {
        this.prefix = prefix;

        // Initialization of the ids from the config file
        this.transportPid = Configuration.getPid(prefix + ".transport");
        this.mypid = Configuration.getPid(prefix + ".myself");
        this.transport = null;

        // Set infected as false by default and alive at true
        this.infected = false;
        this.alive = true;
    }

    // ===== Individual methods =====

    public void setInfected() {
        this.infected = true;
    }

    public float getChanceOfBeingInfected() {
        return 0.97f;
    }

    // ===== EDProtocol methods =====

    // Method called when a message is received by
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

    // ===== Getters and Setters =====

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

    public boolean isInfected() {
        return infected;
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }
}