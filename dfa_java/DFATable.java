import java.util.ArrayList;
import java.util.List;

/**
 * Created by pengkun on 28/11/2018.
 */
public class DFATable {
    private  State currentState;
    private List<State> states;
    private List<Character> alphabet;
    private static String ACCEPT = "accept";
    private static String REJECT = "reject";

    DFATable() {
        this.states = new ArrayList<>();
        this.alphabet = new ArrayList<>();
    }

    public static DFATable createDFATable(String w) {
        String alphabet = "abcdefghijklmnopqrstuvwxyz";
        DFATable table = new DFATable();
        State start = new State(0);
        for(int i = 0; i < 26; i++) {
            table.addCharacter(alphabet.charAt(i));
            if (!w.contains(alphabet.charAt(i)+"")) {
                table.addTransition(alphabet.charAt(i), start, start);
            }
        }
        table.addState(start);
        for(int i =0 ; i < w.length(); i++) {
            State state = new State(i+1);
            table.addState(state);
            table.addTransition(w.charAt(i), start, state);
            start = state;
            if (i == w.length()-1) {
                state.setAcceptable();
                for (int j = 0; j < 26; j++)
                    table.addTransition(alphabet.charAt(j), start, start);
                break;
            }

        }
        table.reset();
        return table;
    }

    void addState(State state) {
        this.states.add(state);
    }

    void addCharacter(Character letter) {
        this.alphabet.add(letter);
    }
    String isAcceptable() {
        if (this.currentState.isAcceptable()) {
            return DFATable.ACCEPT;
        }
        else {
            return DFATable.REJECT;
        }
    }


    int getNumberOfStates() {
        return this.states.size();
    }

    void addTransition(Character input, State from, State to) {
        from.addTransition(input, to);
    }

    List<Character> getAlphabet() {
        return this.alphabet;
    }

    State getState(int index) {
        return this.states.get(index);
    }

    void reset() {
        this.currentState =  states.get(0);
    }

    String start(String input) {
        for(int i = 0; i < input.length(); i++) {
            this.currentState = this.currentState.jump(input.charAt(i));
            if (this.currentState == null)
                return DFATable.REJECT;
        }
        return this.isAcceptable();
    }

    private boolean isDistinguished(State state1, State state2, List<List<State>> partitions) {
        for(int i = 0; i < this.alphabet.size(); i++) {
            boolean toNext = false;
            State s1 = state1.jump(this.alphabet.get(i));
            State s2 = state2.jump(this.alphabet.get(i));
            for (int j = 0; j < partitions.size(); j++) {
                List<State> par =  partitions.get(j);
                if (par.contains(s1) && par.contains(s2)) {
                    toNext = true;
                    break;
                }
            }
            if (!toNext)
                return true;
            else
                return false;
        }
        return false;
    }

    private List<List<State>> doPartition(List<List<State>> partitions) {
         boolean partitioned = false;
        List<List<State>> next =  new ArrayList<>();
         for (int i = 0; i < partitions.size(); i++) {
             List<State> partition = partitions.get(i);
             State picked = partition.get(0);
             List<State> dis = new ArrayList<>();
             for (int j = 1; j < partition.size(); j++) {
                 if(this.isDistinguished(picked, partition.get(j), partitions)) {
                     dis.add(partition.get(j));
                     partitioned = true;
                 }
             }
             if (partitioned) {
                 List<State> res = new ArrayList<>();
                 partition.forEach((state) -> {
                     if (!dis.contains(state))
                         res.add(state);
                 });
                 partitions.subList(i+1, partitions.size()).forEach((p) -> next.add(p));
                 next.add(0, res);
                 next.add(0, dis);
                 return next;
             } else {
                 next.add(partition);
             }
         }
        return null;
    }

    public DFATable minimize() {
        List<List<State>> now = new ArrayList<>();
        List<List<State>> next;
        List<State> finalStates = new ArrayList<>();
        List<State> normalStates = new ArrayList<>();
        this.states.forEach((state) -> {
            if (state.isAcceptable()) {
                finalStates.add(state);
            } else {
                normalStates.add(state);
            }
        });
        now.add(normalStates);
        now.add(finalStates);
        do {
            next = this.doPartition(now);
            if (next != null) {
                now = next;

            }
        } while(next != null);

        DFATable newTable = new DFATable();
        for (int i=0; i < now.size(); i++) {
            State s = new State(i);
            if (now.get(i).get(0).isAcceptable())
                s.setAcceptable();
            newTable.addState(s);
        }
        for(int i=0; i < now.size(); i++) {
            List<State> partition =  now.get(i);
            State picked = partition.get(0);
            for(int j=0; j < this.alphabet.size(); j++) {
                    State n = picked.jump(this.alphabet.get(j));
                    for (int k=0; k < now.size(); k++) {
                        if (now.get(k).contains(n)){
                            newTable.addTransition(this.alphabet.get(j), newTable.getState(i), newTable.getState(k));
                            break;
                        }
                    }
            }
        }
        this.alphabet.forEach((c) -> {
            newTable.addCharacter(c);
        });
        return newTable;
    }


    @Override
    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append("Number of states: "+this.states.size() + "\n");
        buf.append("Accepting states: ");
        this.states.forEach((state) -> {
            if (state.isAcceptable())
                buf.append(state.getId()+" ");
        });
        buf.append("\nAlphabet: ");
        this.alphabet.forEach((c) -> {
            buf.append(c);
        });
        buf.append("\n");
        this.states.forEach((state) -> {
            this.alphabet.forEach((c) -> {
                if (state.jump(c) != null)
                    buf.append(state.jump(c).getId()+" ");
                else buf.append("  ");
            });
            buf.append("\n");
        });
        return buf.toString();
    }
}
