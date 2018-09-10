package lucas.main.k

fun main(args: Array<String>) {
    val pll = Seqs.PLLs_PADRAO[8]
    val pbl = PBL("teste", pll, pll)
    val b = Buscador(pbl)
    b.procurar()
}