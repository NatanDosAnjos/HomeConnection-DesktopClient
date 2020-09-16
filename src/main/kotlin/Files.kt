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
        val mainDir = File(path, "data")
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
    private val configFileName = "clientConfigFile.txt"
    private val dataDir = dataFolder()
    private val configFile = File(dataDir, configFileName)

    init {
        // If the configuration file doesn't exist, it'll be created, and then, the default value of PORT will be write on file.
        // Se o arquivo de configuração não existir, ele será criado e, em seguida, o valor padrão da porta será escrito no arquivo.
        if(configFile.createNewFile()) {
            val writer = PrintStream(configFile)

            writer.println("SERVER_IP_LOCAL:")
            writer.println("SERVER_DNS:")
            writer.println("PORT:")

            writer.flush()
            writer.close()
        }
    }

    //Lê o arquivo de configuração e devolve um Mpa Mutável onde o primeiro argumento é a Key e o segundo argumento o Value
    fun readConfigFile() : MutableMap<String, String> {
        val input = Scanner(configFile)
        val fileContent = mutableMapOf<String, String>()

        while(input.hasNext()) {
            val line = separateArguments(input.nextLine())
            val argument = line[0]
            val value = line[1]
            fileContent[argument] = value
        }
        return fileContent
    }

    private fun separateArguments(lineText: String): List<String> = lineText.split(":")

    fun parseToInt(text: String?): Int {
        return try {
            if(text.isNullOrEmpty()) {
                0
            } else {
                text.toInt()
            }
        }
        catch(e: Exception) {
            0
        }
    }
}