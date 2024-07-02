package com.wonddak.mtmanger.ui.view.home.person

import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.wonddak.mtmanger.room.entity.SimplePerson
import platform.Contacts.CNContact
import platform.Contacts.CNContactFamilyNameKey
import platform.Contacts.CNContactGivenNameKey
import platform.Contacts.CNContactPhoneNumbersKey
import platform.Contacts.CNContactProperty
import platform.Contacts.CNContactStore
import platform.Contacts.CNLabeledValue
import platform.Contacts.CNPhoneNumber
import platform.Contacts.predicateForContactsWithIdentifiers
import platform.ContactsUI.CNContactPickerDelegateProtocol
import platform.ContactsUI.CNContactPickerViewController
import platform.Foundation.NSPredicate
import platform.UIKit.UIApplication
import platform.darwin.NSObject
import platform.posix.open

@Composable
internal actual fun ContactButton(
    modifier: Modifier,
    updateValue: (SimplePerson) -> Unit
) {
    OutlinedButton(
        modifier = modifier,
        onClick = {
            openPicker {
                updateValue(it)
            }
        }
    ) {
        ContactButtonContent()
    }
}

fun openPicker(onSelected: (SimplePerson) -> Unit) {
    val contactPicker = CNContactPickerViewController()
    contactPicker.delegate = ContactDelegate(onSelected)
    contactPicker.displayedPropertyKeys =
        listOf(CNContactGivenNameKey, CNContactFamilyNameKey, CNContactPhoneNumbersKey)
    contactPicker.predicateForEnablingContact =
        NSPredicate.predicateWithFormat("phoneNumbers.@count > 0")
//    contactPicker.predicateForSelectionOfContact =
//        NSPredicate.predicateWithFormat("phoneNumbers.@count == 1")
//    contactPicker.predicateForSelectionOfProperty =
//        NSPredicate.predicateWithFormat("key == 'phoneNumbers'")
    UIApplication.sharedApplication.keyWindow?.rootViewController?.presentViewController(
        contactPicker,
        true,
        null,
    )
}

class ContactDelegate(
    private val onSelected: (SimplePerson) -> Unit
) : NSObject(),
    CNContactPickerDelegateProtocol {

    override fun contactPicker(picker: CNContactPickerViewController, didSelectContact: CNContact) {
        handleContact(contact = didSelectContact)
    }

    private fun handleContact(contact: CNContact) {
        val name = contact.givenName + " " + contact.familyName
        val numbers = contact.phoneNumbers as List<CNLabeledValue>
        val number = (numbers.first().value) as CNPhoneNumber
        onSelected(SimplePerson(name, number.stringValue))
    }
}