package com.wonddak.mtmanger.util

import platform.Foundation.NSError
import platform.MessageUI.MFMailComposeResult
import platform.MessageUI.MFMailComposeViewController
import platform.MessageUI.MFMailComposeViewControllerDelegateProtocol
import platform.UIKit.UIAlertAction
import platform.UIKit.UIAlertActionStyleDefault
import platform.UIKit.UIAlertController
import platform.UIKit.UIAlertControllerStyleAlert
import platform.UIKit.UIApplication
import platform.darwin.NSObject


actual object AppUtil {
    actual fun sendMail() {
        if (MFMailComposeViewController.canSendMail()) {
            val mailC =  MFMailComposeViewController()
            mailC.mailComposeDelegate = MailDelegate()
            mailC.setToRecipients(listOf(MAIL_ADDRESS))
            mailC.setSubject(MAIL_TITLE)
            UIApplication.sharedApplication.keyWindow?.rootViewController?.presentViewController(
                mailC,
                true,
                null,
            )
        } else {
            val sendMailErrorAlert = UIAlertController.alertControllerWithTitle(
                "메일 전송 실패",
                message = "아이폰 이메일 설정을 확인하고 다시 시도해주세요.",
                preferredStyle = UIAlertControllerStyleAlert
            )
            val action = UIAlertAction.actionWithTitle(
                title = "확인",
                style = UIAlertActionStyleDefault
                ,null
            )
            sendMailErrorAlert.addAction(action)
            UIApplication.sharedApplication.keyWindow?.rootViewController?.presentViewController(
                sendMailErrorAlert,
                true,
                null,
            )
        }
    }

    private class MailDelegate() :NSObject(),MFMailComposeViewControllerDelegateProtocol {
        override fun mailComposeController(
            controller: MFMailComposeViewController,
            didFinishWithResult: MFMailComposeResult,
            error: NSError?
        ) {
            controller.dismissViewControllerAnimated(true,null)
        }
    }
}