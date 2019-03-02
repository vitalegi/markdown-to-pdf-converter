package it.vitalegi.markdown.converter;

import java.io.ByteArrayInputStream;
import java.io.Closeable;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.commonmark.Extension;
import org.commonmark.ext.front.matter.YamlFrontMatterExtension;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.Renderer;
import org.commonmark.renderer.html.HtmlRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;

/**
 * @author Giorgio Vitale
 */
@Component
@Profile("!test")
public class CommandlLineRunnerImpl implements CommandLineRunner {

	private static final Logger log = LoggerFactory.getLogger(CommandlLineRunnerImpl.class);

	@Override
	public void run(String... args) throws Exception {

		log.info("Start");

		String input = "sample1.md";
		String output = "out.pdf";
		InputStream cssIs = new FileInputStream("style.css");

		List<Extension> extensions = Arrays.asList(YamlFrontMatterExtension.create());
		Parser parser = Parser.builder().extensions(extensions).build();

		Node doc = parseFile(parser, input);
		Renderer renderer = HtmlRenderer.builder().extensions(extensions).build();

		String body = renderer.render(doc);
		log.info("> "+body);
		String html = "<html><head></head><body>"+body+"</body></html>";

		InputStream htmlIs = new ByteArrayInputStream(html.getBytes());
		generatePDFFromHTML(htmlIs, cssIs, new FileOutputStream(output));

		cssIs = new FileInputStream("style.css");
		StringBuilder sb = new StringBuilder();
		sb.append("<html><head></head>");
		sb.append("<body>");
		sb.append("<div id=\"aaa\">AAAAAAAAAAAAAA</div>");
		sb.append("<div>BBBBBBBBBBBBBB</div>");
		sb.append("</body>");
		sb.append("</html>");
		htmlIs = new ByteArrayInputStream(sb.toString().getBytes());

		generatePDFFromHTML(htmlIs, cssIs, new FileOutputStream("out2.pdf"));

		log.info("End");
	}

	protected void generatePDFFromHTML(InputStream htmlIs, InputStream cssIs, OutputStream os) {
		try {
			Document document = new Document();
			PdfWriter writer = PdfWriter.getInstance(document, os);
			document.open();

			XMLWorkerHelper.getInstance().parseXHtml(writer, document, htmlIs, cssIs);
			document.close();
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			close(htmlIs);
			try {
				os.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}

	protected void close(Closeable c) {
		try {
			c.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	protected Node parseFile(Parser parser, String filename) {

		try (InputStream is = new FileInputStream(filename)) {

			List<String> lines = Files.readAllLines(Paths.get(filename));

			String content = lines.stream().collect(Collectors.joining("\n"));

			return parser.parse(content);

		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
