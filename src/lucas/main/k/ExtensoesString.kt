package lucas.main.k

import java.util.*

fun String.emBaixo(): String {
    val prefix = if (startsWith("/")) "/6,6/-1,1" else "/6,6/-1,1/0,0/"
    val sufix = if (!endsWith("/")) "/6,6/-1,1" else "1,-1/6,6/"
    return prefix + this + sufix
}

fun String.aoContrario(): String {
    this.replace(")", "")
    this.replace("(", "")
    this.replace(" ", "")

    val holder = arrayListOf<String>()

    for (s in this.split("/").reversed()){
        val ss = s.split(",")
        if (s != ""){
            holder.add(((ss[0].replace(" ", "").toInt() * -1).toString() + "," + (ss[1].replace(" ", "").toInt() * -1)))
        }
    }

    var r = holder.toString().replace(", ", "/").replace("[", "").replace("]", "").replace(" ", "")
    if (startsWith("/")) r += "/"
    if (endsWith("/")) r = "/" + r

    return r
}

fun String.otimizada(): String {
    val old = this
    val aux = arrayListOf<String>()
    aux.addAll(old.replace(" ", "").replace("[", "").replace("]", "").split("/"))
    aux.removeAll(Collections.singletonList(""))

    val indice00 = aux.indexOf("0,0")

    if (indice00 != -1){
        if (indice00 >= 1 && indice00 < aux.size - 1){
            val anterior = aux[indice00 - 1].split(",")
            val seguinte = aux[indice00 + 1].split(",")

            aux.removeAt(indice00 - 1)
            aux.removeAt(indice00)
            aux.removeAt(indice00 - 1)

            val a = anterior[0].toInt() + seguinte[0].toInt()
            val b = anterior[1].toInt() + seguinte[1].toInt()

            val x = if (a > 6) (12 - a) * -1 else if (a < 0) if (a < -6) 12 - a else a else a
            val y = if (b > 6) (12 - b) * -1 else if (b < 0) if (b < -6) 12 - b else b else b

            //a+c,b+d
            aux.add(indice00 - 1, "$x,$y")
            return listaToSequencia(aux, old).otimizada()
        } else if (indice00 == 0){
            aux.removeAt(0)
            return listaToSequencia(aux, old.replaceFirst("/", "")).otimizada()
        } else {
            aux.removeAt(aux.size - 1)
            return listaToSequencia(aux, old).otimizada()
        }
    } else {
        return listaToSequencia(aux, old)
    }
}

private fun listaToSequencia(strings: java.util.ArrayList<String>, original: String): String {
    val hold = java.util.ArrayList<String>()

    for (x in strings) {
        val aux2 = x.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        if (x != "") {
            hold.add(Integer.parseInt(aux2[0]).toString() + "," + Integer.parseInt(aux2[1]))
        }
    }

    //cleans toString list
    var r = hold.toString().replace(", ".toRegex(), "/").replace("\\[".toRegex(), "").replace("]".toRegex(), "").replace(" ".toRegex(), "")

    //re-adds twists
    if (original.startsWith("/")) r = "/$r"
    if (original.endsWith("/") || original.endsWith("/0,0")) r += "/"

    return r
}
