package etc;

public class Ferma {

	public static void main(String[] args) {
		boolean isSosu = true;
		for (int i=2; i*i<1234567891 && isSosu; i++)
			if (1234567891%i == 0) isSosu = false;
		System.out.println(isSosu);
	}

}
