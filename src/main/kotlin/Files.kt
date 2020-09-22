package c

import java.util.Scanner
import java.io.PrintStream
import java.io.File
import java.lang.System

private val mainPath: String = System.getProperty("user.home") + File.separator + "Server"

interface MainFolder {

    // Creates the program's main directory if it doesn't already exist.
    // Cria o diretório principal do programa caso não exista.
    fun dataFolder(path: String = mainPath): File? {
        val mainDir = File(path, "clientDesktop")
        if(!(mainDir.exists())) {
            try {
                mainDir.mkdirs()
            }
            catch(e: Exception) {
                return null
            }  
        }
        return mainDir
    }
}

/*-----------------------------------------------------Config-File------------------------------------------------------------------------ */
class ConfigFile : MainFolder {
    private val configFileName = "clientConfigFile.json"
    private val dataDir = dataFolder()
    private val configFile = File(dataDir, configFileName)

    companion object {
        const val LOCAL_IP_VARIABLE_NAME = "localIp"
        const val DNS_VARIABLE_NAME = "dnsName"
        const val PORT_VARIABLE_NAME = "port"
    }

    init {
        // If the configuration file doesn't exist, it'll be created, and then, the default value of PORT will be write on file.
        // Se o arquivo de configuração não existir, ele será criado e, em seguida, o valor padrão da porta será escrito no arquivo.
        if(configFile.createNewFile()) {
            val content = StringBuilder()
            val writer = PrintStream(configFile)

            content.append("{\n")
            content.append("\t\"$LOCAL_IP_VARIABLE_NAME\": \"\",\n")
            content.append("\t\"$DNS_VARIABLE_NAME\": \"\",\n")
            content.append("\t\"$PORT_VARIABLE_NAME\": \"\"\n")
            content.append("}")

            writer.println(content.toString())
            writer.flush()
            writer.close()
        }
    }

    //Lê o arquivo de configuração e devolve um Mpa Mutável onde o primeiro argumento é a Key e o segundo argumento o Value
    fun readConfigFile() : String {
        val input = Scanner(configFile)
        val fileContent = StringBuilder()

        while(input.hasNextLine()) {
            fileContent.append(input.nextLine())
        }
        return fileContent.toString()
    }
}