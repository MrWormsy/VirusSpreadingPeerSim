package virusspreading;

import org.omg.Messaging.SYNC_WITH_TRANSPORT;
import peersim.core.CommonState;
import peersim.core.Network;

import java.util.ArrayList;

public class Control implements peersim.core.Control {

    private int nbOfInfected;
    private int day;

    public Control(String prefix) {
        this.nbOfInfected = 1;
        this.day = 0;
    }

    @Override
    public boolean execute() {
        Individual current;
        Individual buddy;
        int randomID = 0;

        ArrayList<Integer> peopleThatGoesOutside = new ArrayList<>();

        // Add one day
        this.day++;

        // Every day we want to get n random persons from the graph to be the neighbors of the current guy
        // And if this person is infected he can infect the others with a given chance
        for (int id = 0; id < Initializer.sizeNetwork; id++) {

            // Each individual has a given chance to go out today and to spread the virus to the people that go out

            current = (Individual) Network.get(id).getProtocol(Initializer.applicativePID);

            // Only if this Individual is infected we continue to infect more people
            if (current.isInfected() && current.isAlive()) {
                for (int i = 0; i < Initializer.peopleMetPerDay; i++) {

                    do { randomID = CommonState.r.nextInt(Initializer.sizeNetwork); } while (randomID == id);

                    buddy = ((Individual) Network.get(randomID).getProtocol(Initializer.applicativePID));

                    // If this guy is alive, not infected yet and not lucky enough, we infect him
                    if (!buddy.isInfected() && buddy.isAlive() && CommonState.r.nextFloat() <= buddy.getChanceOfBeingInfected()) {
                        buddy.setInfected();

                        // Add the number of infected
                        this.nbOfInfected++;
                    }

                }
            }
        }

        System.out.println("Day " + this.day + ", " + this.nbOfInfected + " infected");

        return false;
    }
}
