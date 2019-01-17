package com.dfa;

import java.util.TreeMap;

public class State {
    private boolean acceptable;
    private int id;
    private TreeMap<Character, State> transitions;

    State(int id){
        this.id = id;
        this.transitions = new TreeMap<>();
    }

    int getId() {
        return id;
    }

    void setAcceptable() {
        this.acceptable = true;
    }
    void addTransition(Character input, State nextState) {
        this.transitions.put(input, nextState);
    }

    State jump(Character input) {
        return this.transitions.get(input);
    }

    boolean isAcceptable() {
        return this.acceptable;
    }

    @Override
    public String toString() {
        StringBuilder format =  new StringBuilder();
        format.append("id is: " + this.id+"\n");
        format.append("is accepting state: " + this.acceptable + "\n");
        format.append("transition is: \n");
        this.transitions.forEach((Character input, State state) -> {
            format.append(input + "->" + state.getId() + "\n");
        });
        return format.toString();
    }

}
