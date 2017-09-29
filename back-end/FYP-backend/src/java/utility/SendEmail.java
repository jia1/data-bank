package utility;


import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SendEmail {

    public static void sendVerificationEmail(String email, String code) {
        
        String verificationLink = "http://localhost:4200/activate-account/"+email+"/"+code;

        final String username = "legit.test.acc";
        final String password = "leGITpassword$1";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("legit.test.acc@gmail.com"));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(email));
            message.setSubject("Highlander Email Verification");
           
            message.setText("Please verify your account by clicking the link below \n\n"
                    + verificationLink);

            Transport.send(message);

            System.out.println("Verification Email Sent to " +email);

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
