package dev.robert.navigation.navtype

import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import androidx.navigation.NavType
import kotlin.reflect.KClass
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json

object CustomNavTypeArgs {
    fun <T : Parcelable> create(kClass: KClass<T>, kSerializer: KSerializer<T>): NavType<T> {
        return object : NavType<T>(true) {
            override fun get(bundle: Bundle, key: String): T? {
                return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    bundle.getParcelable(key, kClass.java)
                } else bundle.getParcelable(key)
            }

            override fun parseValue(value: String): T {
                return Json.decodeFromString(kSerializer, value)
            }

            override fun put(bundle: Bundle, key: String, value: T) {
                bundle.putParcelable(key, value)
            }

            override fun serializeAsValue(value: T): String {
                return Json.encodeToString(serializer = kSerializer, value = value)
            }
        }
    }
}
