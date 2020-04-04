package virusspreading;

import org.omg.Messaging.SYNC_WITH_TRANSPORT;
import peersim.config.Configuration;
import peersim.core.CommonState;
import peersim.core.Network;
import peersim.core.Node;

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

        Individual current;
        Message message;

        // Loop through all the peers and make them send messages to each others
        for (int i = 0; i < Initializer.sizeNetwork; i++) {

            // We check if this person really want to go out today and if not we continue
            if (CommonState.r.nextFloat() > Initializer.chanceToGoOut) {
                continue;
            }

            current = (Individual) Network.get(i).getProtocol(Initializer.applicativePID);

            // We check if the vaccine has been released
            // If true we can give the vaccine to the person with  a given chance
            if (CommonState.getTime() >= Initializer.timeVaccineFound && current.isInfected() && CommonState.r.nextFloat() <= Initializer.chanceGetVaccine && current.isAlive()) {

                // Make him recover
                current.recover();

            }

            // We see if this person is infected (not to send useless messages)
            if (current.isInfected() && !current.isImmune() && current.isAlive()) {

                message = new Message(Message.MessageType.INFECTION, String.valueOf(current.getMyNode().getID()));

                // We loop through his neighbors and send them an infection message
                for (Integer id : current.getNeighbors()) {
                    current.send(message, Network.get(id));
                }
            }
        }

        System.out.println("Day " + this.day + " at " + this.hour + "h we have " + nbOfInfected + " infected, " + nbImmune + " immunized and " + nbDead + " dead");

        // If everybody is infected we can end the simulation
        if (nbOfInfected == 0) {
            return true;
        }

        return false;
    }
}
