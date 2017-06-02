
public class test {
	public static void main(String[] args) {
		String Msg = "APR+2/FFFFFF@";
		String COLOR = "";
		int first;

		first = Msg.indexOf("+");

		COLOR = Msg.substring(first+3,first+9);
		System.out.println(COLOR);
	}
}
