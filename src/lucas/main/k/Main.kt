package lucas.main.k

fun main(args: Array<String>) {

    val pll = SequenciasTemplates.PLLs_PADRAO[0]
    val pbl = PBL("TESTE", pll, pll)
    val b = Buscador(pbl)
    b.procurar()
}