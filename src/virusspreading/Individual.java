package virusspreading;

import peersim.config.Configuration;
import peersim.core.CommonState;
import peersim.core.Network;
import peersim.core.Node;
import peersim.edsim.EDProtocol;

import java.util.ArrayList;
import java.util.LinkedList;

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

    // Know if the person is immune to the virus
    private boolean immune;

    // Neighbors
    private LinkedList<Integer> neighbors;

    // ===== Constructor =====
    public Individual(String prefix) {
        this.prefix = prefix;

        // Initialization of the ids from the config file
        this.transportPid = Configuration.getPid(prefix + ".transport");
        this.mypid = Configuration.getPid(prefix + ".myself");
        this.transport = null;

        // Init the list of Neighbors
        this.neighbors = new LinkedList<>();

        // Set infected as false by default and alive at true
        this.infected = false;
        this.alive = true;
        this.immune = false;
    }

    // ===== Individual methods =====

    public void setInfected() {

        // If the person is not already infected
        if (!this.infected && !this.immune && this.isAlive()) {
            // He has a given chance not to catch the virus
            if (CommonState.r.nextFloat() <= Initializer.chanceBeingInfected) {
                this.infected = true;
                Control.nbOfInfected++;

                // Now we send a message to itself with the number of time we need to wait while the virus incubates
                Message message = new Message(Message.MessageType.END_INCUBATION, "");
                this.transport.send(getMyNode(), message, this.mypid, Initializer.incubationPeriod);
            }
        }

    }

    // We make the person recover and we set him immune to the virus
    public void recover() {

        if (this.infected && !this.immune) {
            this.infected = false;
            this.immune = true;
            Control.nbImmune++;
            Control.nbOfInfected--;
        }

    }

    public float getChanceOfBeingInfected() {
        return 0.97f;
    }

    // ===== EDProtocol methods =====

    // Method called when a message is received by
    @Override
    public void processEvent(Node node, int pid, Object event) {

        Message message = (Message) event;

        // We see what kind of message we have received
        if (message.getType() == Message.MessageType.INFECTION.getTypeID()) {

            // Set infected only if not already infected
            if (!this.isInfected()) {
                this.setInfected();
            }
        }

        // If the message is a incubation end message we check if the person die of get immune to the virus
        else if (message.getType() == Message.MessageType.END_INCUBATION.getTypeID()) {

            // If the person is unlucky we make it die
            if (CommonState.r.nextFloat() <= Initializer.chanceToDie) {
                kill();
            } else {
                recover();
            }

        }

        // this.receive((Message) event);
    }

    private void kill() {
        if (this.alive && this.infected) {
            this.alive = false;
            Control.nbDead++;
            Control.nbOfInfected--;
        }
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

    public LinkedList<Integer> getNeighbors() {
        return neighbors;
    }

    public void setNeighbors(LinkedList<Integer> neighbors) {
        this.neighbors = neighbors;
    }

    public boolean isImmune() {
        return immune;
    }

    public void setImmune(boolean immune) {
        this.immune = immune;
    }
}