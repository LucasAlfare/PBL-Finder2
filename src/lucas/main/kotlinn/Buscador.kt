package lucas.main.kotlinn

import lucas.main.javaa.Cubo
import java.util.*

/**
 * ESTA CLASSE VAI BUSCAR PELOS PBLs
 *
 * A busca baseia-se, basicamente, em testar 2 sequencias em um cubo previamente
 * "embaralhado" ao respectivo caso de PBL repassado e, sempre que uma sequência
 * resolver este cubo teste a mesma será adicionada a uma lista.
 *
 * Este algoritmo é executado dentro do método {@code procurar()}.
 */
class Buscador(val pbl: PBL) {

    val buscas = arrayListOf<Resultado>()
    var log = ""
    var procurando = false

    /**
     * método que busca por todas as sequencias que resolvem
     * o PBL escolhido.
     */
    fun procurar() {
        val square = Cubo()

        log = "Targeted PBL: ${pbl.nome}\n"

        val s1 = pbl.topo.seq.aoContrario().otimizada()
        val s2 = pbl.base.seq.aoContrario().emBaixo().otimizada()

        square.aplicarSequencia(s1)
        square.aplicarSequencia(s2)

        log += "Setup to get the targeted PBL:\n($s1) and ($s2)\n"
        println(log)

        val t = Thread(Runnable {
            for (a in Seqs.AUX_ALGS){
                for (b in Seqs.AUX_ALGS) {
                    procurando = true
                    val sequenciaDeTeste = a.seq + b.seq
                    square.aplicarSequencia(sequenciaDeTeste)

                    if (square.isResolvido()) {
                        buscas.add(Resultado(pbl, sequenciaDeTeste.otimizada(), arrayListOf(a, b)))
                    }

                    square.aplicarSequencia(sequenciaDeTeste.aoContrario())
                }
            }
            procurando = false

            println(buscas)
        })

        t.start()
    }

    /**
     * Esta função é extendida na classe Cubo e serve para
     * indicar se o respectivo square-1 está resolvido ou não.
     */
    fun Cubo.isResolvido(): Boolean {
        val resolvido = Cubo()
        val bytesTopo = arrayListOf<Byte>()
        bytesTopo.addAll(resolvido.getPieces(true))
        bytesTopo.addAll(resolvido.getPieces(true))

        val bytesBase = arrayListOf<Byte>()
        bytesBase.addAll(resolvido.getPieces(false))
        bytesBase.addAll(resolvido.getPieces(false))

        return (Collections.indexOfSubList(bytesTopo, getPieces(true)) != -1) &&
                (Collections.indexOfSubList(bytesBase, getPieces(false)) != -1)
    }
}