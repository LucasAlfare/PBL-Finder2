package lucas.main.javaa;

import java.util.ArrayList;

/**
 * ORIGINALLY WRITTEN BY CS
 */
public class Cubo {

    private int ul = 0x011233;
    private int ur = 0x455677;
    private int dl = 0x998bba;
    private int dr = 0xddcffe;
    private int ml = 0;

    public Cubo() {
        //pass
    }

    /**
     * Aplica uma sequencia neste cubo. A sequencia pode ser nos seguintes formatos:
     * / (x, y) /
     * /(x, y)/
     * /x, y/
     * /x,y/
     *
     * @param sequencia sequencia a ser aplicada neste cubo.
     */
    public void aplicarSequencia(String sequencia){
        String[] pares = sequencia.replaceAll(" ", "").replaceAll("\\(", "").replaceAll("\\)", "").split("/");

        if (sequencia.startsWith("/")) barra();

        for (String par : pares){
            if (!par.equals("")){
                String[] movimentosPar = par.split(",");
                mover(true, Integer.parseInt(movimentosPar[0]));
                mover(false, Integer.parseInt(movimentosPar[1]));
                barra();
            }
        }

        if (!sequencia.endsWith("/")) barra();
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
    private void mover(boolean face, int movimento) {
        this.doMove(face ? (movimento > 0 ? movimento : 12 - (movimento * -1)) : (movimento > 0 ? movimento : 12 - (movimento * -1)) * -1);
    }

    /**
     * Applies a barra (slash "/") to current cube.
     */
    private void barra() {
        if (this.isTwistable()) {
            this.doMove(0);
        }
    }

    /**
     * @param move 0 = barra
     *             [1, 11] = top mover
     *             [-1, -11] = bottom mover
     *             for example, 6 == (6, 0), 9 == (-3, 0), -4 == (0, 4)
     */
    private void doMove(int move) {
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
        } else {
            move = 48 + move;
            int temp = dl;
            dl = (dl >> move | dr << (24 - move)) & 0xffffff;
            dr = (dr >> move | temp << (24 - move)) & 0xffffff;
        }
    }

    private byte pieceAt(int idx) {
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

    private boolean isTwistable() {
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
    private String binString(boolean face) {
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

    private String fullBin(){
        return binString(true).concat(binString(false));
    }

    private void copy(Cubo c) {
        this.ul = c.ul;
        this.ur = c.ur;
        this.dl = c.dl;
        this.dr = c.dr;
        this.ml = c.ml;
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
