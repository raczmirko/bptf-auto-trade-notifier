package hu.okrim.TextFileChangeEmailSender;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TradeLogNotifier implements CommandLineRunner {
	@Autowired
	private EmailSenderService senderService;
	@Autowired
	private TradeLogChangeService textFileChangeService;

	public static void main(String[] args) {
		SpringApplication.run(TradeLogNotifier.class, args);
	}
	@Override
	public void run(String... args) {
		if (args.length != 2) {
			System.out.println("ERROR: You must provide the two required arguments when running the application. Usage: java -jar your-jar-file.jar <toEmail> <textFileFilePath>");
		} else {
			String toEmail = args[0];
			String examinedFilePath = args[1];
			textFileChangeService.createHistoryFileIfNotExists();
			if(textFileChangeService.changesOccurredInExaminedFile(examinedFilePath)){
				String bodyText = textFileChangeService.getNewLinesFromExaminedFile(examinedFilePath);
				senderService.sendMail(toEmail, "New TF2 items sold!", bodyText);
				textFileChangeService.updateTextHistory(examinedFilePath);
			}
			else{
				System.out.println("No changes found. Finishing up...");
			}
		}
	}
}
