package moviebuddy;

import java.lang.reflect.Method;

import org.junit.jupiter.api.Test;

public class ReflectionTests {

	@Test
	void objectCreateAndMethodCall() throws Exception {
		// Without reflection
		Duck duck = new Duck();
		duck.quack();

		// With reflection
		Class<?> duckClass = Class.forName("moviebuddy.ReflectionTests$Duck");
		Object duckObject = duckClass.getDeclaredConstructor().newInstance();
		Method quack = duckObject.getClass().getDeclaredMethod("quack", new Class<?>[0]);
		quack.invoke(duckObject);
	}

	static class Duck {

		void quack() {
			System.out.println("꽥꽥!");
		}

	}

}
