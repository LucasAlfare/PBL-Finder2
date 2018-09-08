package lucas.main.j;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Random;

public class Cubo implements Comparable<Cubo> {
    static Random r = new Random();
    int ul = 0x011233;
    int ur = 0x455677;
    int dl = 0x998bba;
    int dr = 0xddcffe;
    int ml = 0;
    int[] arr = new int[16];

    Cubo(String s) {
        //TODO
    }

    public Cubo() {

    }

    @Override
    public int compareTo(@NotNull Cubo f) {
        if (ul != f.ul) {
            return ul - f.ul;
        }
        if (ur != f.ur) {
            return ur - f.ur;
        }
        if (dl != f.dl) {
            return dl - f.dl;
        }
        if (dr != f.dr) {
            return dr - f.dr;
        }
        return ml - f.ml;
    }

    void copy(Cubo c) {
        this.ul = c.ul;
        this.ur = c.ur;
        this.dl = c.dl;
        this.dr = c.dr;
        this.ml = c.ml;
    }

    public void aplicarSequencia(String sequencia){
        String[] pares = sequencia.replaceAll(" ", "").replaceAll("\\(", "").replaceAll("\\)", "").split("/");

        if (sequencia.startsWith("/")) twist();

        for (String par : pares){
            if (!par.equals("")){
                String[] movimentosPar = par.split(",");
                mover(true, Integer.parseInt(movimentosPar[0]));
                mover(false, Integer.parseInt(movimentosPar[1]));
                twist();
            }
        }

        if (!sequencia.endsWith("/")) twist();
    }

    /**
     * Applies a mover to a cube face (true for top and false to bottom).
     * The {@code mover} param is taked as we have in standard scrambles.
     * <p>
     * Example:
     * (6, -5) -> mover(true, 6) | mover(false, -5)
     *
     * @param face The face you want to mover.
     * @param movimento the value of the mover you want to do.
     */
    public void mover(boolean face, int movimento) {
        this.doMove(face ? (movimento > 0 ? movimento : 12 - (movimento * -1)) : (movimento > 0 ? movimento : 12 - (movimento * -1)) * -1);
    }

    /**
     * Applies a twist (slash "/") to current cube.
     */
    public void twist() {
        if (this.isTwistable()) {
            this.doMove(0);
        }
    }

    /**
     * @param move 0 = twist
     *             [1, 11] = top mover
     *             [-1, -11] = bottom mover
     *             for example, 6 == (6, 0), 9 == (-3, 0), -4 == (0, 4)
     */
    void doMove(int move) {
        move <<= 2;
        if (move > 24) {
            move = 48 - move;
            int temp = ul;
            ul = (ul >> move | ur << (24 - move)) & 0xffffff;
            ur = (ur >> move | temp << (24 - move)) & 0xffffff;
        } else if (move > 0) {
            int temp = ul;
            ul = (ul << move | ur >> (24 - move)) & 0xffffff;
            ur = (ur << move | temp >> (24 - move)) & 0xffffff;
        } else if (move == 0) {
            int temp = ur;
            ur = dl;
            dl = temp;
            ml = 1 - ml;
        } else if (move >= -24) {
            move = -move;
            int temp = dl;
            dl = (dl << move | dr >> (24 - move)) & 0xffffff;
            dr = (dr << move | temp >> (24 - move)) & 0xffffff;
        } else if (move < -24) {
            move = 48 + move;
            int temp = dl;
            dl = (dl >> move | dr << (24 - move)) & 0xffffff;
            dr = (dr >> move | temp << (24 - move)) & 0xffffff;
        }
    }

    public byte pieceAt(int idx) {
        int ret;
        if (idx < 6) {
            ret = ul >> ((5 - idx) << 2);
        } else if (idx < 12) {
            ret = ur >> ((11 - idx) << 2);
        } else if (idx < 18) {
            ret = dl >> ((17 - idx) << 2);
        } else {
            ret = dr >> ((23 - idx) << 2);
        }
        return (byte) (ret & 0x0f);
    }

    public void setPiece(int idx, int value) {
        if (idx < 6) {
            ul &= ~(0xf << ((5 - idx) << 2));
            ul |= value << ((5 - idx) << 2);
        } else if (idx < 12) {
            ur &= ~(0xf << ((11 - idx) << 2));
            ur |= value << ((11 - idx) << 2);
        } else if (idx < 18) {
            dl &= ~(0xf << ((17 - idx) << 2));
            dl |= value << ((17 - idx) << 2);
        } else if (idx < 24) {
            dr &= ~(0xf << ((23 - idx) << 2));
            dr |= value << ((23 - idx) << 2);
        } else {
            ml = value;
        }
    }

    int getParity() {
        int cnt = 0;
        arr[0] = pieceAt(0);
        for (int i = 1; i < 24; i++) {
            if (pieceAt(i) != arr[cnt]) {
                arr[++cnt] = pieceAt(i);
            }
        }
        int p = 0;
        for (int a = 0; a < 16; a++) {
            for (int b = a + 1; b < 16; b++) {
                if (arr[a] > arr[b]) {
                    p ^= 1;
                }
            }
        }
        return p;
    }

    boolean isTwistable() {
        return pieceAt(0) != pieceAt(11)
                && pieceAt(5) != pieceAt(6)
                && pieceAt(12) != pieceAt(23)
                && pieceAt(17) != pieceAt(18);
    }

    public ArrayList<Byte> getPieces(boolean face){
        ArrayList<Byte> r = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            r.add(pieceAt(i + (face ? 0 : 12)));
        }

        return r;
    }

    /**
     * Returns a "binary" representation to this cube.
     * Each edge is a 0 and each corner is a 1.
     * This is usefull to shape analysis.
     * <p>
     * The pieces from top are represented from UB edge to UBL corner (clock-wise).
     * The pieces from bottom are represented from DF edge to DFL corner (clock-wise).
     *
     * @param face the face you want to see binary representation.
     * @return a binary representation of a square-1 face.
     */
    public String binString(boolean face) {
        String r = "";

        r += pieceAt(face ? 0 : 17) % 2 == 0 ? 0 : 1;
        r += pieceAt(face ? 11 : 16) % 2 == 0 ? 0 : 1;
        r += pieceAt(face ? 10 : 15) % 2 == 0 ? 0 : 1;
        r += pieceAt(face ? 9 : 14) % 2 == 0 ? 0 : 1;
        r += pieceAt(face ? 8 : 13) % 2 == 0 ? 0 : 1;
        r += pieceAt(face ? 7 : 12) % 2 == 0 ? 0 : 1;
        r += pieceAt(face ? 6 : 23) % 2 == 0 ? 0 : 1;
        r += pieceAt(face ? 5 : 22) % 2 == 0 ? 0 : 1;
        r += pieceAt(face ? 4 : 21) % 2 == 0 ? 0 : 1;
        r += pieceAt(face ? 3 : 20) % 2 == 0 ? 0 : 1;
        r += pieceAt(face ? 2 : 19) % 2 == 0 ? 0 : 1;
        r += pieceAt(face ? 1 : 18) % 2 == 0 ? 0 : 1;

        return r;
    }

    public String fullBin(){
        return binString(true).concat(binString(false));
    }

    @Override
    public String toString() {

        /*
         * COMEÇA A CONTAR NA PEÇA WO E VAI INDO ANTIHORÁRIO
         *
         * 0 = WO
         * 1 = WOG
         * 2 = WG
         * 3 = WGR
         * 4 = WR
         * 5 = WRB
         * 6 = WB
         * 7 = WBO
         *
         * 8 = YB
         * 9 = YBO
         * 10 = YR
         * 11 = YRB
         * 12 = YG
         * 13 = YRG
         * 14 = YO
         * 15 = YGO
         */

        ArrayList<Byte> aux = new ArrayList<>();

        for (int i = 0; i < 24; i++) {
            aux.add(pieceAt(i));
        }

        //return aux.toString();
        return fullBin();
    }
}
