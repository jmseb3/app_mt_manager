package com.wonddak.mtmanger.ui.view.home.person

import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.wonddak.mtmanger.room.entity.SimplePerson
import platform.Contacts.CNContact
import platform.Contacts.CNContactFamilyNameKey
import platform.Contacts.CNContactGivenNameKey
import platform.Contacts.CNContactPhoneNumbersKey
import platform.Contacts.CNLabeledValue
import platform.Contacts.CNPhoneNumber
import platform.ContactsUI.CNContactPickerDelegateProtocol
import platform.ContactsUI.CNContactPickerViewController
import platform.Foundation.NSPredicate
import platform.UIKit.UIAlertAction
import platform.UIKit.UIAlertActionStyleDefault
import platform.UIKit.UIAlertController
import platform.UIKit.UIAlertControllerStyleAlert
import platform.UIKit.UIApplication
import platform.darwin.NSObject

@Composable
internal actual fun ContactButton(
    modifier: Modifier,
    updateValue: (SimplePerson) -> Unit
) {
    val contactPicker = remember { CNContactPickerViewController() }
    OutlinedButton(
        modifier = modifier,
        onClick = {
            contactPicker.delegate = ContactDelegate(updateValue)
            contactPicker.displayedPropertyKeys =
                listOf(CNContactGivenNameKey, CNContactFamilyNameKey, CNContactPhoneNumbersKey)
            contactPicker.predicateForEnablingContact =
                NSPredicate.predicateWithFormat("phoneNumbers.@count > 0")
            contactPicker.predicateForSelectionOfContact =
                NSPredicate.predicateWithFormat("phoneNumbers.@count >= 1")
//    contactPicker.predicateForSelectionOfProperty =
//        NSPredicate.predicateWithFormat("key == 'phoneNumbers'")
            UIApplication.sharedApplication.keyWindow?.rootViewController?.presentViewController(
                contactPicker,
                true,
                null,
            )
        }
    ) {
        ContactButtonContent()
    }
}


class ContactDelegate(
    private val onSelected: (SimplePerson) -> Unit
) : NSObject(),
    CNContactPickerDelegateProtocol {

    override fun contactPicker(picker: CNContactPickerViewController, didSelectContact: CNContact) {
        handleContact(picker, didSelectContact)
    }


    private fun handleContact(picker: CNContactPickerViewController, contact: CNContact) {
        println(">> $contact")
        val name = contact.givenName + " " + contact.familyName
        val numbers = contact.phoneNumbers as List<CNLabeledValue>
        println(">> $name : [${numbers.size == 1}] ${numbers.joinToString("/")}")
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