/**
 * ESTE ARQUIVO CONTÉM AS CLASSES QUE IRÃO
 * MANTER DADOS PARA O PROGRAMA
 */
package lucas.main.kotlinn


data class PLL(val nome: String, val seq: String, val isParidade: Boolean)

data class PBL(val nome: String, val topo: PLL, val base: PLL)

open class Movimento {

    constructor(barra: String)

    constructor(movimentos: IntArray)
}

data class Barra(val barra: String) : Movimento(barra)

data class Par(val movimentos: IntArray) : Movimento(movimentos)

data class Sequencia(val movimentos: ArrayList<Movimento>)

data class AuxAlg(val nome: String, val seq: String)

data class Resultado(val pbl: PBL, val seq: String, val auxAlgs: ArrayList<AuxAlg>){

    fun tamanhoTwistMetric() = seq.count { "/".contains(it) }

    fun tamanhoTurnMetric(): Int {
        return seq
                .replace(" ",  "")
                .replace("(",  "")
                .replace(")",  "")
                .replace("0",  "")
                .replace("/", " ")
                .replace(",", " ")
                .trim()
                .split(" ")
                .size + tamanhoTwistMetric()
    }

    override fun toString(): String {
        var s = "${pbl.topo.nome}/${pbl.base.nome}: $seq"

        s += "   Algs: ["
        for (al in auxAlgs){
            s += "${al.nome} | "
        }
        s += "]"

        return s.replace("| ]", "]") + "   ~{${tamanhoTwistMetric()}/${tamanhoTurnMetric()}}"
    }
}