package virusspreading;

import org.omg.Messaging.SYNC_WITH_TRANSPORT;
import peersim.core.CommonState;
import peersim.core.Network;

import java.util.*;

public class Control implements peersim.core.Control {

    private int nbOfInfected;
    private int day;

    // This arraylist is used to create smallest list to be shuffled later
    private LinkedList<Integer> ids;

    private LinkedList<Integer> alreadyInfected;

    public Control(String prefix) {
        this.nbOfInfected = 1;
        this.day = 0;

        this.ids = new LinkedList<>();

        // Init and add patient 0
        this.alreadyInfected = new LinkedList<>();
        this.alreadyInfected.add(0);

        // Add all the integers between 0 and the size of the network - 1
        for (int i = 0; i < Initializer.sizeNetwork; i++) {
            this.ids.add(i);
        }
    }

    @Override
    public boolean execute() {
        // Add one day
        this.day++;

        Individual current;
        Individual buddy;
        int randomID = 0;

        LinkedList<Integer> peopleContaninatedToday = new LinkedList<>();
        LinkedList<Integer> metPersons;

        LinkedList<Integer> tempList;


        // First things first we want to know who will go outside, and those persons only can get contaminated

        // We will shuffle the ids list and retrieve a sublist of n persons
        tempList = new LinkedList<>((LinkedList<Integer>) this.ids.clone());
        Collections.shuffle(tempList);

        LinkedList<Integer> peopleThatGoesOutside = new LinkedList<>(tempList.subList(0, (int) (Initializer.chanceToGoOut * Initializer.sizeNetwork)));

        /*
        for (int i = 0; i < Initializer.chanceToGoOut * Initializer.sizeNetwork; i++) {

            do {
                randomID = CommonState.r.nextInt(Initializer.sizeNetwork);
            } while (peopleThatGoesOutside.contains(i));

            peopleThatGoesOutside.add(randomID);

        }
        */

        // Then we loop through all the poeple outside and we check if they are infected to infect more persons.
        // However, if you have been infected, you catch the virus only at the end of the day
        for (Integer random : peopleThatGoesOutside) {

            // TODO In the first place we will only go further if the current person is infected (maybe later we can say that if this person is in contact with a infected one, he get the virus)

            current = (Individual) Network.get(random).getProtocol(Initializer.applicativePID);

            if (current.isInfected()) {

                // We will suffle the persons that go outside, remove the current guy and then pick the ids of the people met
                tempList = new LinkedList<>((LinkedList<Integer>) peopleThatGoesOutside.clone());
                tempList.remove(random);

                Collections.shuffle(tempList);

                metPersons = new LinkedList<>(tempList.subList(0, Initializer.peopleMetPerDay));

                //System.out.println(metPersons.size());

                // Each persons can be in relation with a certain amount of individuals per day

                /*
                for (int i = 0; i < Initializer.peopleMetPerDay; i++) {

                    // We loop while the person has not been met today and the person is not itself
                    do {
                        randomID = peopleThatGoesOutside.get(CommonState.r.nextInt(peopleThatGoesOutside.size()));
                    } while (metPersons.contains(randomID) || randomID == random);

                    metPersons.add(randomID);
                }
                */

                // We need to remove the duplicated ones

                // We can add the persons met today as infected
                peopleContaninatedToday.addAll(metPersons);
            }
        }

        // Remove duplicates from the people contaminated today

        // Create a new LinkedHashSet  and add elements within
        Set<Integer> set = new LinkedHashSet<>();
        set.addAll(peopleContaninatedToday);

        // Clear the list and add the set with no duplicates (as a set cannot have duplicates)
        peopleContaninatedToday.clear();
        peopleContaninatedToday.addAll(set);

        // Remove the persons already infected
        peopleContaninatedToday.removeAll(this.alreadyInfected);

        // As we have all the contaminated persons today we set all of them as infected at the end of the day
        for (Integer person : peopleContaninatedToday) {
            ((Individual) Network.get(person).getProtocol(Initializer.applicativePID)).setInfected();
        }

        this.alreadyInfected.addAll(peopleContaninatedToday);

        // Print stats
        System.out.println("Day " + this.day + " : " + peopleContaninatedToday.size() + " have been contaminated with a total of " + this.alreadyInfected.size() + " Individuals");


        /*

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
         */


        // Here we want to test if the simulation can end (ie. for the moment all the people are infected)
        if (this.alreadyInfected.size() == Initializer.sizeNetwork) {
            return true;
        }


        return false;
    }
}
