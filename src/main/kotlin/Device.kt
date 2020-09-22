package c

class Device (val ip: String, private val port: Int = 80, var name: String = "", private val deviceType: String) {

    private var statOfDevice: String? = null

    //Static Property in Kotlin
    companion object {
        //@JvmStatic val REGISTER_CODE: String = "2510"
        @JvmStatic val COMMAND_STATUS = "status"
        @JvmStatic val COMMAND_PULSE_THIS = "pulseThis"
        @JvmStatic val COMMAND_TURN_ON = "turnOn"
        @JvmStatic val COMMAND_TURN_OFF = "turnOff"
        //@JvmStatic val INVALID_COMMAND = "INVALID_COMMAND"
        //@JvmStatic val COMMAND_RETURN_MAC_ADDRESS = "returnMacAddress"
        //@JvmStatic val COMMAND_REBOOT = "rebootThis"
        @JvmStatic val TYPE_ONOFF = "onOff"
        @JvmStatic val TYPE_PULSE = "pulse"
    }

    fun isPulseType() : Boolean {
        return deviceType == TYPE_PULSE
    }

    fun isOnOffType() : Boolean {
        return deviceType == TYPE_ONOFF
    }

    override fun toString(): String {
       super.toString()

        val text = StringBuilder()
        text.append("IP: ${this.ip}\n")
        text.append("NAME: ${this.name}\n")
        text.append("PORT: ${this.port}\n")
        text.append("STATUS: ${this.statOfDevice}\n")

        return text.toString()
    }
}