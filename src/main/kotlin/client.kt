package c

import com.google.gson.Gson
import java.util.*
import kotlin.concurrent.thread

var mapOfDevices = mutableMapOf<Int, Device>()

fun main() {
    val server = instantiateServer()

    if (server != null) {
        mapOfDevices = getDevicesObjects(server)
        if (server.isConnected) {
            do {
                var choice = choiceDeviceInTheMenu()
                if (choice == 0) {
                    break
                }
                val selectedDevice = mapOfDevices[choice]!!
                val command = deviceOptions(selectedDevice)

                if(command.isNullOrEmpty()) {
                    choice = 0
                } else {
                    thread {
                        val serverFeedback = server.sendCommand(command)
                        println("\n${selectedDevice.name} respond $serverFeedback\n")
                    }
                }


            } while (choice != 0)

        } else {
            if (server.connectedWithDns) {
                println("Erro de conexão com o servidor ${server.dnsName} na porta ${server.port}")
            } else {
                println("Erro de conexão com o servidor ${server.localIp} na porta ${server.port}")
            }
        }

    } else {
        println("Arquivo de configuração do servidor está corrompido ou inelegível!")
    }



}

fun choiceDeviceInTheMenu() : Int {
    while (true) {
        if (mapOfDevices.isNotEmpty()) {
            for ((k, v) in mapOfDevices) {
                println("$k - ${v.name}")
            }
            println("0 - Sair do Programa")

            val input = Scanner(System.`in`)
            val choice = input.nextLine()
            val numberSelected = parseToInt(choice)

            if (numberSelected <= mapOfDevices.size && numberSelected > 0) {
                return numberSelected
            }
            else if(numberSelected == 0) {
                break
            } else {
                println("opção inválida")
            }
            println()
        }
    }
    return 0
}

fun deviceOptions(device: Device): String? {
    val scanKeyboard = Scanner(System.`in`)
    val optionList = listOf(Device.COMMAND_TURN_ON, Device.COMMAND_TURN_OFF, Device.COMMAND_STATUS)

    do {
        if (device.isOnOffType()) {
            println("1 - LIGAR")
            println("2 - DESLIGAR")
            println("3 - STATUS")
            println("0 - VOLTAR")

            val selectedOption = parseToInt(scanKeyboard.nextLine())
            if (selectedOption > 0 && selectedOption <= optionList.size) {
                return "${device.ip}=${optionList[selectedOption-1]}"

            } else if (selectedOption == 0) {
                return null

            } else {
                println("Opção Inválida!")
                println()
            }
        } else if (device.isPulseType()) {
            return "${device.ip}=${Device.COMMAND_PULSE_THIS}"
        }
        println()
    } while (true)

}

fun instantiateServer() : ServerToConnect? {
    val configFile = ConfigFile()
    val configInJsonFormat = configFile.readConfigFile()

    return try {
        Gson().fromJson(configInJsonFormat, ServerToConnect::class.java)
    } catch (e: Exception) {
        null
    }
}

fun getDevicesObjects(server: ServerToConnect) : MutableMap<Int, Device>{
    val map = mutableMapOf<Int, Device>()
    val textInput = server.sendCommand("getDevices")
    val list = separateArguments(textInput) //Separates Json objects with '=' in the input sting

    var cont = 1
    for (deviceJson in list) {
        val device = Gson().fromJson(deviceJson, Device::class.java)
        if (device != null) {
            map[cont] = device
            cont++
        }
    }

    return map
}
