import java.util.UUID;

public class Demo {

	public static void main(String[] args) {
		String token = UUID.randomUUID().toString();
		System.out.println(token);
		String token1 = UUID.randomUUID().toString();
		System.out.println(token1);
		String abc= "dhasda";
		System.out.println(abc.trim());
		System.out.println(abc.trim().isEmpty());

	}

}
