package virusspreading;

import org.omg.Messaging.SYNC_WITH_TRANSPORT;
import peersim.Simulator;
import peersim.config.Configuration;
import peersim.core.CommonState;
import peersim.core.Network;
import peersim.core.Node;

import java.io.IOException;
import java.util.*;

public class Control implements peersim.core.Control {

    public static int nbOfInfected;
    public static int nbImmune;
    public static int nbDead;
    private int day;
    private int hour;

    private int step;

    // This arraylist is used to create smallest list to be shuffled later
    private LinkedList<Integer> ids;

    private LinkedList<Integer> alreadyInfected;

    public Control(String prefix) {
        this.nbOfInfected = 1;
        this.nbImmune = 0;
        this.nbDead = 0;
        this.day = 0;
        this.hour = 0;

        step = Configuration.getInt(prefix + ".step");
    }

    @Override
    public boolean execute() {

        // We get the new hour and if need the new day
        this.hour += step;
        if (hour >= 24) {
            this.hour-=24;
            this.day++;
        }


        // When a certain amount of the population has been infected the government force them stay at home
        if (nbOfInfected >= Initializer.sizeNetwork * Initializer.proportionOfInfectedToDeclareContainment && Initializer.chanceToGoOut != Initializer.chanceToGoOutDuringContainment) {
            Initializer.chanceToGoOut = Initializer.chanceToGoOutDuringContainment;
            System.out.println("Containment has been declared by the government as " + Initializer.proportionOfInfectedToDeclareContainment * 100 + "% of the population is infected !");
        }


        Individual current;
        Message message = new Message(Message.MessageType.MAKE_ACTION, "");;

        // Warn that next step is launched
        for (int i = 0; i < Initializer.sizeNetwork; i++) {
            current = (Individual) Network.get(i).getProtocol(Initializer.applicativePID);

            // We send a message MAKE ACTION which is to tell the node that a new step has arrived thus an action is needed
            current.getTransport().send(Network.get(i), message, current.getMypid(), 0);
        }

        // Here it is used for the data analysis part we output the stats
        // System.out.println(CommonState.getTime() + "," + nbOfInfected + "," + nbImmune + "," + nbDead);

        // Write to the file
        try {
            Initializer.statsFile.write(Simulator.experimentNumber + "," + CommonState.getTime() + "," + nbOfInfected + "," + nbImmune + "," + nbDead + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }

        // If everybody is infected we can end the simulation
        if(nbOfInfected == 0) {

            // If this was the last experiment we can close the file
            if (Simulator.experiments == Simulator.experimentNumber + 1) {
                try {
                    Initializer.statsFile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            // Return true to end
            return true;
        }

        return false;
    }
}
