package c

import jdk.nashorn.internal.ir.debug.JSONWriter
import jdk.nashorn.internal.runtime.JSONFunctions
import netscape.javascript.JSObject
import java.net.Socket
import java.net.InetAddress
import java.net.URL
import java.io.InputStreamReader
import java.io.BufferedReader
import java.util.*
import kotlin.concurrent.thread


var PORT: Int = 0
var SERVER_IP_LOCAL = ""
var SERVER_DNS: String = ""

fun main(args: Array<String>) {

    doIt(args)

}

fun doIt(args: Array<String>) {
    val configFile = ConfigFile()
    val map = configFile.readConfigFile()

    SERVER_DNS = returnStringNonNullable(map, "SERVER_DNS")
    SERVER_IP_LOCAL = returnStringNonNullable(map, "SERVER_IP_LOCAL")

    val portTmp = map["PORT"]
    PORT = configFile.parseToInt(portTmp)

    val socket: Socket? = if(ipsAreSame()) {
        connectToHost(SERVER_IP_LOCAL, PORT)
    } else {
        connectToHost(SERVER_DNS, PORT)
    }

    if(socket != null) {

        for(i in 0 .. args.size.minus(1)) {
            sendCommand(args[i], socket)
            println("${espIp(args[i])} is ${receiveStatus(socket)}")
            socket.close()
        }
    }
}

fun returnStringNonNullable(map: MutableMap<String, String>,variableName: String): String {
    val variable = map[variableName]
    if (variable.isNullOrEmpty()) {
        return ""
    }
    return variable
}

fun espIp(lineText: String): String {
    val list = lineText.split("=")
    return list[0]
}

fun sendCommand(command: String, socket: Socket?) {
    val outputStreamSocket = socket!!.outputStream
    outputStreamSocket.write("$command\n".toByteArray())
    outputStreamSocket.flush()
}

fun receiveStatus(socket: Socket): String? {
    val scan = Scanner(socket.inputStream)

    if(scan.hasNextLine()) {
        return scan.nextLine()
    }

    return "cant read"
}


/*-------------------------------------Functions-------------------------------------------------- */

//Return my Global IP
fun myIp(): String? {
    try{
        val whatMyIp = URL("https://checkip.amazonaws.com")
        val input = BufferedReader(InputStreamReader(whatMyIp.openStream()))
        return input.readLine()
        
    } catch (e: Exception){
        e.printStackTrace()
    }

    return null
}

fun hostIp(hostName: String): String?{
    return try{
        val inetAddressObject = InetAddress.getByName(hostName)
        val ip: String? = inetAddressObject.hostAddress
        ip

    } catch (e: Exception) {
        null
    }
}

//Verify if my Internal IP is same of my Global IP
fun ipsAreSame(hostIp: String? = hostIp(SERVER_DNS), myIp: String? = myIp()): Boolean {
    if(hostIp != null || myIp != null) {
        return myIp == hostIp
    }
    return false
}

fun connectToHost(host: String, port: Int): Socket? {
    try {
        return Socket(host, port)
    }
    catch(e: Exception) {
        println("Erro de conexão com o servidor $host na porta $port")
    }
    return null
}
