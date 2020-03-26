package virusspreading;

import peersim.core.CommonState;

public class Control implements peersim.core.Control {

    public Control(String prefix) {

    }

    @Override
    public boolean execute() {

        System.out.println(CommonState.getTime());

        // Every day we want to get n random persons from the graph to be the neighbors of the current guy
        // And if this person is infected he can infect the others with a given chance



        return false;
    }
}
