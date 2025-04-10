package org.sopt.homework.repository.util.io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.stream.Collectors;

import org.sopt.homework.domain.Post;
import org.sopt.homework.exception.ExceptionMessage;

public class FileIO {
	private static final String FILE_PATH = "src/main/java/org/sopt/homework/assets/Post.txt";

	// 버퍼스트림을 이용하여 게시글 생성시 파일에 append 수행
	public static void saveToFile(Long id, Post post) {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
			writer.write(id.toString() + "," + post.getTitle());
			writer.newLine();
		} catch (IOException e) {
			System.out.println(ExceptionMessage.FILE_ACCESS_ERROR.getMessage());
		}
	}

	// 파일 전체를 읽어와서 map으로 변환 후 반환
	public static Map<Long, Post> loadFromFile() {
		try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
			return reader.lines()
				.map(line -> line.split(","))
				.collect(Collectors.toMap(
					splitLine -> Long.parseLong(splitLine[0]),
					splitLine -> new Post(splitLine[1])
				));
		} catch (IOException e) {
			System.out.println(ExceptionMessage.FILE_ACCESS_ERROR.getMessage());
			return Map.of();
		}
	}

	// 메모리의 게시글 데이터를 받아와서 파일내용 덮어쓰기
	public static void overwriteToFile(Map<Long, Post> posts) {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, false))) {
			for (Map.Entry<Long, Post> entry : posts.entrySet()) {
				writer.write(entry.getKey() + "," + entry.getValue().getTitle());
				writer.newLine();
			}
			// 이거 두개 gpt에 쳐보기
			writer.flush();
			writer.close();
		} catch (IOException e) {
			System.out.println(ExceptionMessage.FILE_ACCESS_ERROR.getMessage());
		}
	}
}