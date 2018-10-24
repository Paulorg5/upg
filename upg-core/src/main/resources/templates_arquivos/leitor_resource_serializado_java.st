package <pacote>.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class LeitorResourceSerializado {

	private LeitorResourceSerializado() { }
	
	private static class LerResourceSerializadoSingleton {
		private static final LeitorResourceSerializado INSTANCE = new LeitorResourceSerializado();
	}
	
	public static LeitorResourceSerializado getInstance() {
		return LerResourceSerializadoSingleton.INSTANCE;
	}
	
	public Object ler(String caminho) {
		try {
			FileInputStream fin = new FileInputStream(
					getClass().getClassLoader().getResource(caminho).getFile());
			ObjectInputStream ois = new ObjectInputStream(fin);
			
			Object retorno = ois.readObject();
			
			ois.close();
			fin.close();
			
			return retorno;
		} catch (IOException | ClassNotFoundException e) {
			System.out.println(e.getMessage());
			return null;
		}
	}
	
}
