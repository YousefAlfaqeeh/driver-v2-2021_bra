package schooldriver.trackware.com.school_bus_driver_android.nfc

import android.content.Context
import android.content.Intent
import android.nfc.NfcAdapter
import android.provider.Settings

// https://github.com/KaulyLeite/nfc_app/tree/master/app/src/main/java/br/edu/ifsc/garopaba/nfcapp

object NfcUtils {


    fun verificarSuporteNfc(context: Context): Boolean {
        val adapter = NfcAdapter.getDefaultAdapter(context)
        return adapter != null
    }

    fun verificarAtivacaoNfc(context: Context): Boolean {
        val adapter = NfcAdapter.getDefaultAdapter(context)
        return adapter?.isEnabled == true
    }

    fun abrirConfiguracaoNfc(context: Context) {
        val intent = Intent(Settings.ACTION_NFC_SETTINGS)
        context.startActivity(intent)
    }
}

interface IModoLeitor {

    fun habilitarModoLeitor()

    fun desabilitarModoLeitor()
}