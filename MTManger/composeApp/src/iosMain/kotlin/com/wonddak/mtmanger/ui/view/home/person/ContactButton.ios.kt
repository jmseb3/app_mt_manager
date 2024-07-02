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
import platform.UIKit.UIAlertAction
import platform.UIKit.UIAlertActionStyleDefault
import platform.UIKit.UIAlertController
import platform.UIKit.UIAlertControllerStyleAlert
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
        handleContact(picker, didSelectContact)
    }

    private fun handleContact(picker: CNContactPickerViewController, contact: CNContact) {
        val name = contact.givenName + " " + contact.familyName
        val numbers = contact.phoneNumbers as List<CNLabeledValue>
        if (numbers.size == 1) {
            val number = (numbers.first().value) as CNPhoneNumber
            picker.dismissViewControllerAnimated(false) {
                onSelected(SimplePerson(name, number.stringValue))
            }
        } else {
            picker.dismissViewControllerAnimated(true) {
                val alert = UIAlertController.alertControllerWithTitle(
                    "Select One",
                    message = null,
                    preferredStyle = UIAlertControllerStyleAlert
                )
                numbers.forEach { number ->
                    val getNumber = (number.value as CNPhoneNumber).stringValue
                    val action = UIAlertAction.actionWithTitle(
                        title = getNumber,
                        style = UIAlertActionStyleDefault
                    ) {
                        onSelected(SimplePerson(name, getNumber))
                    }
                    alert.addAction(action)
                }

                alert.addAction(UIAlertAction.actionWithTitle(
                    title = "닫기",
                    style = UIAlertActionStyleDefault
                ) {

                })
                UIApplication.sharedApplication.keyWindow?.rootViewController?.presentViewController(
                    alert,
                    true,
                    null,
                )
            }
        }
    }
}