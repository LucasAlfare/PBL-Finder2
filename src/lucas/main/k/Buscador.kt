package lucas.main.k

import lucas.main.j.Cubo
import java.util.*

class Buscador(val pbl: PBL) {

    val buscas = arrayListOf<Resultado>()
    var log = ""

    fun procurar() {
        val square = Cubo()
        square.aplicarSequencia(pbl.asSetup())
        log = "Targeted PBL: ${pbl.nome}\n"
        log += "Setup to targeted PBL: ${pbl.asSetup()}"

        for (auxAlg1 in SequenciasTemplates.AUX_ALGS){
            for (auxAlg2 in SequenciasTemplates.AUX_ALGS) {
                val sequenciaDeTeste = auxAlg1.seq + auxAlg2.seq
                square.aplicarSequencia(sequenciaDeTeste)

                if (isResolvido(square)){
                    buscas.add(Resultado(pbl, sequenciaOtimizada(sequenciaDeTeste), arrayListOf(auxAlg1, auxAlg2)))
                }

                square.aplicarSequencia(sequenciaDeTeste.sequenciaReversa())
            }
        }
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