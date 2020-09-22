package c

import java.lang.Exception
import java.net.InetAddress
import java.net.Socket
import java.util.*

class ServerToConnect (val localIp: String, val dnsName: String = "", val port: Int = 80){

    var connectedWithDns = false
    var isConnected = false

   private fun connect(): Socket? {
        return try {
            if (globalIpsAreSame(serverGlobalIp())) {
                Socket(localIp, port)
            } else {
                connectedWithDns = true
                Socket(dnsName, port)
            }

        }catch (e: Exception) {
            null
        }
    }

    fun sendCommand(command: String) : String {
        val socket = connect()

        if (socket != null) {
            isConnected = true

            val outStream = socket.outputStream
            outStream.write("$command\n".toByteArray())
            val text = receiveFeedback(socket)
            outStream.flush()
            socket.close()

            return text
        }

        return ""
    }

    private fun receiveFeedback(socket: Socket): String {

        val input = Scanner(socket.inputStream)

        if(input.hasNextLine()) {
            return input.nextLine()
        }

        return ""
    }

    private fun serverGlobalIp(): String?{
        return try{
            val inetAddressObject = InetAddress.getByName(this.dnsName)
            val ip: String? = inetAddressObject.hostAddress
            ip

        } catch (e: Exception) {
            null
        }
    }
}