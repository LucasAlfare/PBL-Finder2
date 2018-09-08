package lucas.main.k

import java.util.*
import kotlin.collections.ArrayList

fun String.sequenciaEmBaixo(): String {
    this.replace(")", "")
    this.replace("(", "")
    this.replace(" ", "")
    return (if (this[0] == '/') "/6,6/-1,1" else "/6,6/-1,1/0,0/") +
            this +
            (if (this[length - 1] != '/') "/6,6/-1,1" else "1,-1/6,6/")
}

fun String.sequenciaReversa(): String {
    this.replace(")", "")
    this.replace("(", "")
    this.replace(" ", "")

    val holder = arrayListOf<String>()

    for (s in this.split("/").reversed()){
        val ss = s.split(",")
        if (s != ""){
            holder.add((ss[0].toInt() * -1).toString() + "," + (ss[1].toInt() * -1))
        }
    }

    var r = holder.toString().replace(", ", "/").replace("[", "").replace("]", "").replace(" ", "")
    if (this[0] == '/') r += "/"
    if (this[length - 1] == '/') "/" + r

    return r
}

fun sequenciaOtimizada(old: String): String {
    val aux = arrayListOf<String>()
    aux.addAll(old.replace(" ", "").split("/"))
    //remove os itens vazios..
    aux.removeAll(Collections.singletonList(""))

    val indice00 = aux.indexOf("0,0")

    if (indice00 != -1) {
        if (indice00 >= 1 && indice00 < aux.size - 1) { //pelo meio...

            val pAnterior = aux[indice00 - 1].split(",")
            val pSeguinte = aux[indice00 + 1].split(",")

            //remove os pares velhos
            aux.removeAt(indice00 - 1)
            aux.removeAt(indice00)
            aux.removeAt(indice00 - 1)

            val sumA = Integer.parseInt(pAnterior[0]) + Integer.parseInt(pSeguinte[0])
            val sumB = Integer.parseInt(pAnterior[1]) + Integer.parseInt(pSeguinte[1])

            val x = if (sumA > 6) (12 - sumA) * -1 else if (sumA < 0) if (sumA < -6) 12 - sumA else sumA else sumA
            val y = if (sumB > 6) (12 - sumB) * -1 else if (sumB < 0) if (sumB < -6) 12 - sumB else sumB else sumB

            //a+c,b+d
            aux.add(indice00 - 1, x.toString() + "," + y)
            return sequenciaOtimizada(strListToSequence(aux, old))
        } else if (indice00 == 0) { //no começo..
            aux.removeAt(0)
            return sequenciaOtimizada(strListToSequence(aux, old.replaceFirst("/".toRegex(), "")))
        } else { //no fim...
            aux.removeAt(aux.size - 1)
            return sequenciaOtimizada(strListToSequence(aux, old))
        }
    } else {
        return strListToSequence(aux, old)
    }
}

private fun strListToSequence(lista: ArrayList<String>, original: String): String{
    var r = lista.toString().replace(", ", "/").replace("(", "").replace(")", "").replace(" ", "")

    if (original[0] == '/') r = "/" + r
    if (original.endsWith("/") || original.endsWith("/0,0")) r += "/"

    return r
}
