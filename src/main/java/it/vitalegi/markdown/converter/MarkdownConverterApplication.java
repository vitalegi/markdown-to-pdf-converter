package it.vitalegi.markdown.converter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MarkdownConverterApplication {

	public static void main(String[] args) {
		SpringApplication.run(CommandlLineRunnerImpl.class, args);
	}
}
