package moviebuddy;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import moviebuddy.domain.Movie;
import moviebuddy.domain.MovieFinder;

/**
 * @author springrunner.kr@gmail.com
 */
public class MovieBuddyApplication {

	public static void main(String[] args) throws Exception {
		new MovieBuddyApplication().run(args);
	}
	
	/* 애플리케이션 추가 요구사항:
	 * 
	 * (완료) 1. XML 문서로 작성된 영화 메타데이터도 다룰 수 있게 기능을 확장하라
	 * TODO 2. 영화 메타데이터 위치를 변경할 수 있도록 하라  
	 * TODO 3. 영화 메타데이터 읽기 속도를 빠르게 하라 
	 * TODO 4. 시스템 언어설정에 따라 애플리케이션 메시지가 영어 또는 한글로 출력되게 하라
	 */	
	
	void run(String[] args) throws Exception {
		final ApplicationContext applicationContext = new AnnotationConfigApplicationContext(MovieBuddyFactory.class);
		final MovieFinder movieFinder = applicationContext.getBean(MovieFinder.class);
		
		final AtomicBoolean running = new AtomicBoolean(true);
		final BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
		final PrintWriter output = new PrintWriter(System.out, false);
		
		/*--------------------------------------------------------------------------------------*/
		/* 명령어 별 실행 로직을 정의한다. */
		
		final Map<Command, Consumer<List<String>>> commandActions = new HashMap<>();
		// 애플리케이션 종료:: ❯ quit
		commandActions.put(Command.Quit, arguments -> {
			output.println("quit application.");
			running.set(false);
		});
		// 감독으로 영화 검색:: ❯ directedBy Michael Bay
		commandActions.put(Command.DirectedBy, arguments -> {
			String director = String.join(" ", arguments.subList(1, arguments.size()));
			if (director.isBlank()) {
				throw new ApplicationException.InvalidCommandArgumentsException();
			}
			List<Movie> moviesDirectedBy = movieFinder.directedBy(director);
			AtomicInteger counter = new AtomicInteger(1);
			
			output.println(String.format("find for movies by %s.", director));
			moviesDirectedBy.forEach(it -> {
				String data = String.format("%d. title: %-50s\treleaseYear: %d\tdirector: %-25s\twatchedDate: %s", counter.getAndIncrement(), it.getTitle(), it.getReleaseYear(), it.getDirector(), it.getWatchedDate().format(Movie.DEFAULT_WATCHED_DATE_FORMATTER));
				output.println(data);
			});
			output.println(String.format("%d movies found.", moviesDirectedBy.size()));
		});
		// 개봉년도로 영화 검색:: ❯ releasedYearBy 2015
		commandActions.put(Command.releasedYearBy, arguments -> {
			int releaseYear;
			try {
				releaseYear = Integer.parseInt(arguments.get(1));
			} catch (IndexOutOfBoundsException|NumberFormatException error) {
				throw new ApplicationException.InvalidCommandArgumentsException(error);
			}
			List<Movie> moviesReleasedYearBy = movieFinder.releasedYearBy(releaseYear);
			AtomicInteger counter = new AtomicInteger(1);
			
			output.println(String.format("find for movies from %s year.", releaseYear));
			moviesReleasedYearBy.forEach(it -> {
				String data = String.format("%d. title: %-50s\treleaseYear: %d\tdirector: %-25s\twatchedDate: %s", counter.getAndIncrement(), it.getTitle(), it.getReleaseYear(), it.getDirector(), it.getWatchedDate().format(Movie.DEFAULT_WATCHED_DATE_FORMATTER));
				output.println(data);
			});
			output.println(String.format("%d movies found.", moviesReleasedYearBy.size()));
		});

		
		/*--------------------------------------------------------------------------------------*/
		/* 사용자가 입력한 값을 해석 후 연결된 명령을 실행한다. */
		
		output.println();
		output.println("application is ready.");

		// quit(애플리케이션 종료) 명령어가 입력되기 전까지 무한히 반복하기(infinite loop)
		while(running.get()) {
			try {
				// 사용자가 입력한 값 읽기
				output.print("❯ ");
				output.flush();
				List<String> arguments = Stream.of(input.readLine().split(" "))
											   .map(String::trim)
											   .filter(argument -> !argument.isBlank())
											   .collect(Collectors.toList());

				// 명령어 해석 후 실행, 연결된 명령어가 없으면 입력 오류 메시지 출력하기
				Command command = Command.parse(arguments.isEmpty() ? null : arguments.get(0));
				Consumer<List<String>> commandAction = commandActions.getOrDefault(command, null);
				if (Objects.isNull(commandAction)) {
					throw new ApplicationException.UndefinedCommandActionException();
				}
				commandAction.accept(arguments);
			} catch (ApplicationException error) {
				output.println(error.getMessage());
			} finally {
				output.flush();
			}
		}
	}
		
	/**
	 * 사용자 명령어 정의
	 */
	enum Command {
		Quit, DirectedBy, releasedYearBy;

		static Command parse(String text) {
			if (Objects.isNull(text)) {
				return null;
			}
			return Stream.of(Command.values())
						 .filter(it -> Objects.equals(it.name().toLowerCase(), text.toLowerCase()))
			 			 .findAny()
						 .orElse(null);
		}
	}	
	
}
