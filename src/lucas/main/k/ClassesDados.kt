package lucas.main.k

data class PLL(val nome: String, val seq: String, val isParidade: Boolean)

data class PBL(val nome: String, val topo: PLL, val base: PLL){
    fun asSetup(): String {
        return sequenciaOtimizada(topo.seq.sequenciaReversa() + base.seq.sequenciaReversa().sequenciaEmBaixo())
    }
}

data class AuxAlg(val nome: String, val seq: String)

data class Resultado(val pbl: PBL, val seq: String, val auxAlgs: ArrayList<AuxAlg>){

    fun tamanhoTwistMetric(): Int {
        return seq.count { "/".contains(it) }
    }

    fun tamanhoTurnMetric(): Int {
        return seq
                .replace(" ", "")
                .replace("(", "")
                .replace(")", "")
                .replace("0", "")
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