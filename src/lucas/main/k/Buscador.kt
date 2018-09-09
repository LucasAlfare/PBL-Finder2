package lucas.main.k

import lucas.main.j.Cubo
import java.util.*
import lucas.main.j.CustomStringUtils.*

class Buscador(val pbl: PBL) {

    val buscas = arrayListOf<Resultado>()
    var log = ""

    fun procurar() {
        val square = Cubo()

        log = "Targeted PBL: ${pbl.nome}\n"

        val s1 = pbl.topo.seq.aoContrario().otimizada()
        val s2 = pbl.base.seq.aoContrario().emBaixo().otimizada()

        square.aplicarSequencia(s1)
        square.aplicarSequencia(s2)

        log += "Setup to get the targeted PBL:\n($s1) and ($s2)\n"
        println(log)

        for (a in SequenciasTemplates.AUX_ALGS){
            for (b in SequenciasTemplates.AUX_ALGS) {
                val sequenciaDeTeste = a.seq + b.seq
                square.aplicarSequencia(sequenciaDeTeste)

                if (isResolvido(square)){
                    buscas.add(Resultado(pbl, sequenciaDeTeste.otimizada(), arrayListOf(a, b)))
                    square.aplicarSequencia(sequenciaDeTeste.aoContrario())
                } else {
                    square.aplicarSequencia(sequenciaDeTeste.aoContrario())
                }
            }
        }

        println(buscas)
    }

    fun isResolvido(teste: Cubo): Boolean {
        val resolvido = Cubo()
        val bytesTopo = arrayListOf<Byte>()
        bytesTopo.addAll(resolvido.getPieces(true))
        bytesTopo.addAll(resolvido.getPieces(true))

        val bytesBase = arrayListOf<Byte>()
        bytesBase.addAll(resolvido.getPieces(false))
        bytesBase.addAll(resolvido.getPieces(false))

        return (Collections.indexOfSubList(bytesTopo, teste.getPieces(true)) != -1) &&
                (Collections.indexOfSubList(bytesBase, teste.getPieces(false)) != -1)
    }
}