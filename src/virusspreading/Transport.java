package virusspreading;

import peersim.config.Configuration;
import peersim.core.CommonState;
import peersim.core.Node;
import peersim.core.Protocol;
import peersim.edsim.EDSimulator;

// The transport layer
public class Transport implements Protocol {

    // Variables used to get the latency between two nodes
    private long min;
    private long range;

    public Transport(String prefix) {
        System.out.println("Transport Layer Enabled");

        // Get the values from the config file
        min = Configuration.getInt(prefix + ".mindelay");
        long max = Configuration.getInt(prefix + ".maxdelay");

        if (max < min) {
            System.out.println("The maximum latency cannot be smaller than the minimum latency");
            System.exit(1);
        }
        range = max - min + 1;
    }

    @Override
    public Object clone() {
        return this;
    }

    // To send a message it is added to the queue
    public void send(Node src, Node dest, Object msg, int pid) {
        long delay = getLatency(src, dest);
        EDSimulator.add(delay, msg, dest, pid);
    }

    // Get a random latency between the min and the max
    public long getLatency(Node src, Node dest) {
        return (range == 1 ? min : min + CommonState.r.nextLong(range));
    }
}

