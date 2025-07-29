package com.foryourlife.shared.email

import com.sendgrid.Method
import com.sendgrid.Request
import com.sendgrid.SendGrid
import com.sendgrid.helpers.mail.Mail
import com.sendgrid.helpers.mail.objects.Attachments
import com.sendgrid.helpers.mail.objects.Content
import com.sendgrid.helpers.mail.objects.Email
import org.springframework.stereotype.Service
import org.thymeleaf.TemplateEngine
import org.thymeleaf.context.Context
import java.io.*
import java.nio.charset.StandardCharsets
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Base64
import java.util.Locale


@Service
class DispatchNotification(
    val email: Email,
    val sendGrid: SendGrid,
    private val templateEngine: TemplateEngine,
) {
    fun send(toEmail: String, subject: String, message: String) {
        try {


            val now = LocalDate.now()
            val day = now.dayOfMonth
            val month = now.month.getDisplayName(
                TextStyle.FULL, Locale("es")
            )
            val toEmail = Email(toEmail)
            val context = Context().apply {
                setVariables(
                    mapOf(
                        "message" to message,
                    )
                )
            }

            val htmlContent = templateEngine.process(
                "email-template.html", context
            )
            val content = Content(
                "text/html", htmlContent
            )
            val mail = Mail(
                email, subject, toEmail, content
            )

            val request = Request()
            request.method = Method.POST
            request.endpoint = "mail/send"
            request.body = mail.build()

            val response = sendGrid.api(request)

            println("Correo enviado. Código de estado: ${response.statusCode}")
            println("Respuesta de SendGrid: ${response.body}")

        } catch (e: Exception) {
            println("Error al enviar el correo: ${e.message}")
            e.printStackTrace()
        }
    }

    private fun createAttachment(file: File): Attachments {
        val encodedFileContent: ByteArray = Base64.getEncoder().encode(file.readBytes())
        val attachment = Attachments()
        attachment.disposition = "attachment"
        attachment.type = file.extension
        attachment.filename = file.name
        attachment.content = String(
            encodedFileContent,
            StandardCharsets.UTF_8
        )
        return attachment
    }
}
