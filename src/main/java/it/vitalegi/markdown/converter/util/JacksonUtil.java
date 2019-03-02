package it.vitalegi.markdown.converter.util;

import java.io.IOException;
import java.io.InputStream;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

/**
 * @author Giorgio Vitale
 */
public class JacksonUtil {

	public static ObjectMapper getYamlMapper() {
		ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
		mapper.registerModule(new JavaTimeModule());
		mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
		return mapper;
	}

	public static <E> E getValue(ObjectMapper mapper, Class<E> clazz, InputStream is) {

		try {
			return mapper.<E>readValue(is, clazz);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			close(is);
		}
	}

	public static <E> E getValueYaml(Class<E> clazz, InputStream is) {
		try {
			return getValue(getYamlMapper(), clazz, is);
		} finally {
			close(is);
		}
	}

	private static void close(InputStream is) {
		try {
			is.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
