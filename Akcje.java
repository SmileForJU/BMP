package EasyAsFAQ;

import java.io.IOException;
import java.util.List;

public abstract class Akcja {

	
	public static final Akcja zatwierdz = new Akcja(){
		@Override	public Dokument wykonaj(Dokument doc) {
			doc.setStan(StanDokumentu.ZATWIERDZONY);
		
			return doc;
		}

		@Override	public String getNazwa() {
			return "Zatwierdz";
		}
	};
	
	
	public static final Akcja archiwizuj = new Akcja(){
		@Override	public Dokument wykonaj(Dokument doc) {
			doc.setStan(StanDokumentu.ACHRIWALNY);
			return doc;
		}

		@Override	public String getNazwa() {
			return "Archiwizuj";
		}
	};
	
	public static final Akcja usun = new Akcja(){
		@Override	public Dokument wykonaj(Dokument doc) {
			doc.setStan(StanDokumentu.USUNIETY);
			return doc;
		}

		@Override	public String getNazwa() {
			return "Usun";
		}
	};
	
	public static final Akcja przywroc = new Akcja(){
		@Override	public Dokument wykonaj(Dokument doc) {
			if(doc.getStan()!=StanDokumentu.USUNIETY){
			System.out.println("Stan dokumentu nie jest prawidłowy dla akcji przywróc"+this+", ponieważ jest to"+doc.getStan());
			return doc;
			}
			doc.setStan(StanDokumentu.NOWY);
			System.out.print("Nazwa dokumentu");
			try {
				String nazwa = Utils.readLine();
				doc.setNazwa(nazwa);
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(1);
			}
			doc.setTresc("");
			
			DokumentyDAO.getInstance().getDane().add(doc);
	
			return doc;
		}

		@Override	public String getNazwa() {
			return "przywroc";
		}
	};
	
	public static final Akcja wyrzuc = new Akcja(){
		@Override	public Dokument wykonaj(Dokument doc) {
			if(doc.getStan()!=StanDokumentu.USUNIETY){
			System.out.println("Stan dokumentu nie jest prawidłowy dla akcji przywróc"+this+", ponieważ jest to"+doc.getStan());
			return doc;
			}
		
			DokumentyDAO.getInstance().getDane().remove(doc);
			
			return null;
		}

		@Override	public String getNazwa() {
			return "Wyrzuc";
		}
	};
	
	public static final Akcja koniec = new Akcja(){
		@Override	public Dokument wykonaj(Dokument doc) {
			System.out.println("Zapis danych...");		
			DokumentyDAO.getInstance().zapiszDane();
			System.out.println("Dane zapisano");
			return null;
		}
		//TODO: finalizacja
		@Override	public String  getNazwa(){
				return "Koniec";
			}
};


public static final Akcja nowy = new Akcja(){
	@Override	public Dokument wykonaj(Dokument doc) {
		doc = new Dokument();
		doc.setStan(StanDokumentu.NOWY);
		System.out.println("Akcja nowy");
		try {
			String nazwa = Utils.readLine();
			doc.setNazwa(nazwa);
		}catch (IOException e){
			e.printStackTrace();
			System.exit(1);
		}
		
		
		
		//TODO: Tworzenie dokumentu
		return doc;
	}
	
	@Override	public String  getNazwa(){
			return "Nowy";
		}
};

public static final Akcja edytuj = new Akcja(){
	@Override	public Dokument wykonaj(Dokument doc) {
		
		System.out.println("Edycja dokumentu");
		System.out.println("Akcja nowy "+doc.getNazwa());
		System.out.println(doc.getTresc());
		System.out.println("---------------------");
		System.out.print("Nowa tresc: ");
		
		try {
			String tresc = Utils.readLine();
			doc.setTresc(tresc);
		}catch (IOException e){
			e.printStackTrace();
			System.exit(1);
		}
		

		return doc;
	}
	
	@Override	public String  getNazwa(){
			return "Edytuj";
		}
};

public static final Akcja wybierzZListy = new Akcja(){

	@Override public Dokument wykonaj(Dokument doc) {
		System.out.println("Wybierz dokument");
		System.out.println("0. Powrot do menu");
		List<Dokument> dane = DokumentyDAO.getInstance().getDane();
		int i=1;
		for(Dokument d : dane){
			System.out.printf(i+"%d: %25s, %13s\n",i,d.getNazwa(),d.getStan());
		i++;
		}
		
		
		try {
			System.out.print("nr dokumentu >");
			String nr = Utils.readLine();
			int nrDoc = Integer.parseInt(nr);
			if(nrDoc>0 && nrDoc<=dane.size()){
				doc = dane.get(nrDoc-1);
				System.out.println("Zmieniono aktywny  dokument na: "+doc.getNazwa());
				
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
			return doc;
	}

	@Override	public String getNazwa() {	
		return "Wybierz dokument z listy";
	}
};

public static final Akcja podglad = new Akcja(){
	@Override	public Dokument wykonaj(Dokument doc) {
		
		System.out.println("Podglad dokumentu");
		System.out.println("Akcja nowy "+doc.getNazwa());
		System.out.println(doc.getTresc());
		System.out.println("---------------------");
		
		System.out.print("Nowa tresc: ");
		
		for(Zdarzenie z : doc.getHistoria()){
			System.out.println(z);
		}
	
		return doc;
	}
	
	@Override	public String  getNazwa(){
			return "Podglad";
		}
};
	
	
	
	
	public abstract Dokument wykonaj(Dokument doc);
	public abstract String getNazwa();
	
	@Override
	public String toString() {
		return  getNazwa();
	}
}
