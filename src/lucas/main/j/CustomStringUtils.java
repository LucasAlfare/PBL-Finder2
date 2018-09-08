package lucas.main.j;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class CustomStringUtils {

    /**
     * Returns a otimized String representation
     * for a old String sequence using recursion.
     *
     * @param old Sequence to be refactored
     * @return otimized String
     */
    public static String otimizedSequence(String old) {
        ArrayList<String> aux = new ArrayList<>(Arrays.asList(old.replaceAll(" ", "").split("/")));
        //remove os itens vazios..
        aux.removeAll(Collections.singletonList(""));

        int indexOfZeros = Collections.indexOfSubList(aux, Collections.singletonList("0,0"));

        if (indexOfZeros != -1){
            if (indexOfZeros >= 1 && indexOfZeros < aux.size() - 1){ //pelo meio...

                String[] previousPair = aux.get(indexOfZeros - 1).split(",");
                String[] nextPair = aux.get(indexOfZeros + 1).split(",");

                //remove os pares velhos
                aux.remove(indexOfZeros - 1);
                aux.remove(indexOfZeros);
                aux.remove(indexOfZeros - 1);

                int sumA = (Integer.parseInt(previousPair[0]) + Integer.parseInt(nextPair[0]));
                int sumB = (Integer.parseInt(previousPair[1]) + Integer.parseInt(nextPair[1]));

                int x = sumA > 6 ? ((12 - sumA) * -1) : (sumA < 0 ? (sumA < -6 ? 12 - sumA : sumA) : sumA);
                int y = sumB > 6 ? ((12 - sumB) * -1) : (sumB < 0 ? (sumB < -6 ? 12 - sumB : sumB) : sumB);

                //a+c,b+d
                aux.add(indexOfZeros - 1, x + "," + y);

                return otimizedSequence(strListToSequence(aux, old));
            }
            else if (indexOfZeros == 0){ //no começo..
                aux.remove(0);
                return otimizedSequence(strListToSequence(aux, old.replaceFirst("/", "")));
            } else { //no fim...
                aux.remove(aux.size() - 1);
                return otimizedSequence(strListToSequence(aux, old));
            }
        }  else {
            return strListToSequence(aux, old);
        }
    }

    /**
     * Converts a list of String into a sequence string (formated).
     *
     * @param strings  list containing all pairs
     * @param original original sequence passed
     * @return formatted sequence from pairs list.
     */
    private static String strListToSequence(ArrayList<String> strings, String original){
        ArrayList<String> hold = new ArrayList<>();

        for (String x : strings) {
            String[] aux2 = x.split(",");
            if (!x.equals("")) {
                hold.add((Integer.parseInt(aux2[0])) + "," + (Integer.parseInt(aux2[1])));
            }
        }

        //cleans toString list
        String r = hold.toString().replaceAll(", ", "/").replaceAll("\\[", "").replaceAll("]", "").replaceAll(" ", "");

        //re-adds twists
        if (original.startsWith("/")) r = "/" + r;
        if (original.endsWith("/") || original.endsWith("/0,0")) r += "/";

        return r;
    }

    /**
     * Reverses a square-1 sequence.
     *
     * Example:
     * original ->  (1,-3)/
     * out      -> /(-1,3)
     *
     * @param algorithm original sequence do be reversed.
     * @return a reversed string from original param.
     */
    public static String reversedSequence(String algorithm) {
        ArrayList<String> hold = new ArrayList<>();
        //cleans and splits
        ArrayList<String> aux = new ArrayList<>(Arrays.asList(algorithm.replaceAll(" ", "").split("/")));

        //reverses
        Collections.reverse(aux);

        //inverses
        for (String x : aux) {
            String[] aux2 = x.split(",");
            if (!x.equals("")) {
                hold.add((Integer.parseInt(aux2[0]) * -1) + "," + (Integer.parseInt(aux2[1]) * -1));
            }
        }

        //cleans toString list
        String r = hold.toString().replaceAll(", ", "/").replaceAll("\\[", "").replaceAll("]", "").replaceAll(" ", "");

        //re-adds twists
        if (algorithm.startsWith("/")) r += "/";
        if (algorithm.endsWith("/")) r = "/" + r;

        return r;
    }
}
