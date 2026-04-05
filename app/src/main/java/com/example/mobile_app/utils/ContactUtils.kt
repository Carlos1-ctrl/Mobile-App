package com.example.mobile_app.utils

import android.content.ContentProviderOperation
import android.content.Context
import android.provider.ContactsContract
import android.widget.Toast
import com.example.mobile_app.data.model.User

/**
 * Guarda la informacion de contacto de un usuario en la agenda del dispositivo.
 * Inserta nombre, telefono y email usando ContentProviderOperation.
 * Muestra un Toast de confirmacion o error segun el resultado
 * @param context Contexto de la aplicaicon
 * @param user Usuario cuyos datos se guardaran como contacto
 */

fun saveContact(context: Context, user: User) {
    try {
        val ops = arrayListOf<ContentProviderOperation>()

        ops.add(
            ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                .build()
        )

        ops.add(
            ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.Data.MIMETYPE,
                    ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME, user.name.first)
                .withValue(ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME, user.name.last)
                .build()
        )

        ops.add(
            ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.Data.MIMETYPE,
                    ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, user.phone)
                .withValue(ContactsContract.CommonDataKinds.Phone.TYPE,
                    ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
                .build()
        )

        ops.add(
            ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.Data.MIMETYPE,
                    ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Email.ADDRESS, user.email)
                .build()
        )

        context.contentResolver.applyBatch(ContactsContract.AUTHORITY, ops)
        Toast.makeText(context, "Contacto guardado: ${user.name.first}", Toast.LENGTH_SHORT).show()

    } catch (e: Exception) {
        Toast.makeText(context, "Error al guardar contacto: ${e.message}", Toast.LENGTH_LONG).show()
        e.printStackTrace()
    }
}