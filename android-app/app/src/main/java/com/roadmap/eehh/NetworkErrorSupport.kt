package com.roadmap.eehh

import java.io.IOException
import java.net.URL
import java.security.cert.CertPathValidatorException

internal object NetworkErrorSupport {
    fun wrap(baseUrl: String, exc: Exception): IOException {
        if (isTlsTrustFailure(exc)) {
            return IOException(buildTlsMessage(baseUrl), exc)
        }
        return if (exc is IOException) {
            exc
        } else {
            IOException(exc.message ?: "Falha de rede.", exc)
        }
    }

    private fun buildTlsMessage(baseUrl: String): String {
        val parsedUrl = runCatching { URL(baseUrl) }.getOrNull()
        val host = parsedUrl?.host?.takeIf { it.isNotBlank() } ?: "o servidor configurado"
        val localHint = if (parsedUrl?.host?.let(::isLocalHost) == true) {
            " Para ambiente local no build debug, prefira http://10.0.2.2:8088/ ou instale a CA local no dispositivo/emulador."
        } else {
            " Verifique se o servidor envia a cadeia completa do certificado e usa uma CA confiavel no Android."
        }

        return "Nao foi possivel validar o certificado TLS de $host.$localHint"
    }

    private fun isTlsTrustFailure(exc: Throwable?): Boolean {
        var current = exc
        var depth = 0
        while (current != null && depth < 8) {
            if (current is CertPathValidatorException) {
                return true
            }

            val message = current.message.orEmpty()
            if (
                message.contains("Trust anchor for certification path not found", ignoreCase = true) ||
                message.contains("trust anchor", ignoreCase = true) && message.contains("certificate", ignoreCase = true) ||
                message.contains("CertPathValidatorException", ignoreCase = true)
            ) {
                return true
            }

            current = current.cause
            depth += 1
        }
        return false
    }

    private fun isLocalHost(host: String): Boolean {
        return host == "10.0.2.2" ||
            host == "127.0.0.1" ||
            host == "localhost" ||
            host.endsWith(".local") ||
            host.startsWith("192.168.") ||
            host.startsWith("10.") ||
            host.startsWith("172.16.") ||
            host.startsWith("172.17.") ||
            host.startsWith("172.18.") ||
            host.startsWith("172.19.") ||
            host.startsWith("172.2")
    }
}