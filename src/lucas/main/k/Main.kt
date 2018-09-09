package lucas.main.k

import java.util.*

fun main(args: Array<String>) {
    val pll = SequenciasTemplates.PLLs_PADRAO[0]
    val pbl = PBL("teste", pll, pll)
    val b = Buscador(pbl)
    b.procurar()
}