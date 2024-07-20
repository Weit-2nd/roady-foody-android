package com.weit2nd.data.util

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.KeyStore
import java.security.spec.RSAKeyGenParameterSpec
import javax.crypto.Cipher
import javax.inject.Inject

class SecurityProvider @Inject constructor() {
    private val keyEntry: KeyStore.PrivateKeyEntry

    init {
        val keyStore =
            KeyStore.getInstance(ANDROID_KEY_STORE).apply {
                load(null)
            }

        if (keyStore.containsAlias(KEY_STORE_ALIAS).not()) {
            generateKeyPair()
        }

        keyEntry = keyStore.getEntry(KEY_STORE_ALIAS, null) as KeyStore.PrivateKeyEntry
    }

    private fun generateKeyPair(): KeyPair {
        val keyPairGenerator: KeyPairGenerator =
            KeyPairGenerator.getInstance(
                KeyProperties.KEY_ALGORITHM_RSA,
                ANDROID_KEY_STORE,
            )

        val parameterSpec: KeyGenParameterSpec =
            KeyGenParameterSpec
                .Builder(
                    KEY_STORE_ALIAS,
                    KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT,
                ).setAlgorithmParameterSpec(RSAKeyGenParameterSpec(KEY_LENGTH, RSAKeyGenParameterSpec.F4))
                .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_RSA_PKCS1)
                .setDigests(KeyProperties.DIGEST_SHA256, KeyProperties.DIGEST_SHA512)
                .build()

        return keyPairGenerator.run {
            initialize(parameterSpec)
            generateKeyPair()
        }
    }

    fun encrypt(plainText: String): String {
        val cipher =
            Cipher.getInstance(CIPHER_ALGORITHM).apply {
                init(Cipher.ENCRYPT_MODE, keyEntry.certificate.publicKey)
            }
        val bytes = plainText.toByteArray()
        val encryptedBytes = cipher.doFinal(bytes)
        return Base64.encodeToString(encryptedBytes, Base64.DEFAULT)
    }

    fun decrypt(encryptedText: String): String {
        val cipher =
            Cipher.getInstance(CIPHER_ALGORITHM).apply {
                init(Cipher.DECRYPT_MODE, keyEntry.privateKey)
            }
        val encryptedBytes = Base64.decode(encryptedText, Base64.DEFAULT)
        return String(cipher.doFinal(encryptedBytes))
    }

    companion object {
        private const val ANDROID_KEY_STORE = "AndroidKeyStore"
        private const val KEY_STORE_ALIAS = "ROFO_STORE"
        private const val KEY_LENGTH = 2048
        private const val CIPHER_ALGORITHM =
            "${KeyProperties.KEY_ALGORITHM_RSA}/${KeyProperties.BLOCK_MODE_ECB}/${KeyProperties.ENCRYPTION_PADDING_RSA_PKCS1}"
    }
}
