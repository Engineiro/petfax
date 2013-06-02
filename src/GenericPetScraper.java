import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * This class scrapes several Atlanta Pet Adoption agencies for their pet data.
 * @author geo
 *
 */
public class GenericPetScraper {
	public static ArrayList<Pet> petCollector = new ArrayList<Pet>();
	public static Pet pet = new Pet();
	
	public static void main(String[] args){
		try {
			
			GenericPetScraper petScrape = new GenericPetScraper();
			petScrape.getAtlPetRescue();
			print("Atlanta Pet Rescue has loaded : " +petCollector.size());
			petScrape.getAtlHumaneSociety();
			print("Atlanta Humane Society has loaded : " +petCollector.size());
			petScrape.getFultonAnimalService();
			print("Fulton Animal has loaded : " +petCollector.size());
			BufferedWriter buff = new BufferedWriter(new FileWriter("petWoof.csv"));
			for(Pet p : petCollector){
				buff.write(p.getImageUrl()+","+p.getSpecies()+","+p.getGender()
						+","+p.getBreed()+","+p.getColorOfCoat()+","+p.getEyeColor()
						+","+p.getLocationStatus()+","+p.getIdentityMarks()+","+p.getPetId()
						+",,,,,,,,,,,,,,,,\n");
			}
			buff.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * This scrapes data from the Atlanta pet rescue
	 * @throws Exception
	 */
	public void getAtlPetRescue() throws Exception {

		List<String> catDog = new ArrayList<String>();
		catDog.add("cats");
		catDog.add("all-dogs");
		for(String cd : catDog){
			Document doc = getDoc("http://www.atlantapetrescue.org/our-pets/"+ cd +"/");
			
			int i =1;
			
			Elements petsEls = doc.getElementsByClass("pet");
			for(Element petEl : petsEls){
				
				if(!petEl.getAllElements().hasClass("adopted")){//this prevents us from grabbing pets that have already been adopted
					
					print(petEl.getElementsByTag("img").first().attr("src"));
					print(petEl.getElementsByClass("name").first().text());
					print(petEl.getElementsByClass("age").first().text());
					print(petEl.getElementsByClass("weight").first().text());
					print(petEl.getElementsByClass("breed").first().text());
					print(petEl.getElementsByClass("gender").first().text());
					print("Animal Type : " + (cd.equals("cats") ? "cat" : "dog"));
					
					pet.setImageUrl(petEl.getElementsByTag("img").first().attr("src"));
					pet.setSpecies((cd.equals("cats") ? "cat" : "dog"));
					pet.setGender(petEl.getElementsByClass("gender").first().text());
					pet.setBreed(petEl.getElementsByClass("breed").first().text());
					pet.setColorOfCoat("magenta");
					pet.setEyeColor("bradPittBlue");
					pet.setLocationStatus("46 Dog Road");
					pet.setCurrentLocation("Shelter");
					pet.setIdentityMarks("orions belt on navel");
					pet.setPetId("2.718");//ln()
					
					print("Total : "+ i++);
				}
				petCollector.add(pet);
				pet = new Pet();
				print("" + petCollector.size());
			}
		}
	}
	
	public void getAtlHumaneSociety() throws Exception {

		List<String> catDog = new ArrayList<String>();
		//Different campus websites for each
		catDog.add("adopt-a-cat/alpharetta-campus/");
		catDog.add("adopt-a-cat/howell-mill-campus/");
		catDog.add("adopt-a-dog/alpharetta-campus-dogs/");
		catDog.add("adopt-a-dog/howell-mill-campus-dogs/");

		int count = 1;
		
		for(String cd : catDog){
			Document doc = getDoc("http://atlantahumane.org/adopt/"+ cd);

			Elements pets = doc.select("iframe");
			
			Elements iFramePets = getDoc(pets.attr("src")).getElementsByClass("list-animal-name");
			
			String[] detail = new String[]{"imgAnimalPhoto", "trSpecies", "trBreed","trAge",
					"trSex","trColor","trAltered","trDeclawed","trSite",
					"trLocation","trIntakeDate","trAdoptionPrice"};
			
			for(Element petEl : iFramePets){
				print("http://www.petango.com/webservices/adoptablesearch/"+petEl.getElementsByTag("a").first().attr("href"));
				Document petDetail = getDoc("http://www.petango.com/webservices/adoptablesearch/"+petEl.getElementsByTag("a").first().attr("href"));
				
				//This populates the pet object by iterating over each attribute in the array above tr**(etc)
				for(String deet : detail){
					
					if(petDetail.getElementById(deet) == null){//skip null attributes
						continue;
					}
					
					if(deet.equals("imgAnimalPhoto")){
						pet.setImageUrl(petDetail.getElementById(deet).attr("src"));
						print(petDetail.getElementById(deet).attr("src"));
						
					} else if(deet.equals("trAltered")){//this is a hacky way to see if dogs have been nuttered. Oops Politically correct is "Altered".
						print(
								petDetail.getElementById("trAltered").getElementsByClass("detail-value").first().getElementsByTag("img").attr("src").contains("Green")
								? "YES": "NO" 
							);
					} else if(deet.equals("trAdoptionPrice")){
						print(petDetail.getElementById(deet).getElementsByClass("detail-label").first().text());
						print(petDetail.getElementById(deet).getElementsByClass("detail-value").first().text());
					} else {
						print(petDetail.getElementById(deet).getElementsByClass("detail-label").first().text());
						print(petDetail.getElementById(deet).getElementsByClass("detail-value").first().text());
						
						if(deet.equals("trSpecies"))
							pet.setSpecies(setDetail(petDetail, deet));
						else if(deet.equals("trBreed"))
							pet.setBreed(setDetail(petDetail, deet));
						else if(deet.equals("trSex"))
							pet.setGender(setDetail(petDetail, deet));
						else if(deet.equals("trColor"))
							pet.setColorOfCoat(setDetail(petDetail, deet));
						else if(deet.equals("trLocation"))
							pet.setCurrentLocation(setDetail(petDetail, deet));
						
					}
				}
				pet.setEyeColor("SophiaVergara Brown");
				pet.setLocationStatus("Shelter");
				pet.setIdentityMarks("other nose near belly button; don't stare");
				pet.setPetId("011235813");//fibonacci
				petCollector.add(pet);
				pet = new Pet();
				print("" + count++);
			}
		}
	}
	//convenience method for Atlanta Humane Society
	public String setDetail(Element e, String s){
		return e.getElementById(s).getElementsByClass("detail-value").first().text();
	}
	//Scrapes Fulton Animal Service
	public void getFultonAnimalService() throws Exception{
		
		List<String> catDog = new ArrayList<String>();
		catDog.add("Cat");
		catDog.add("Dog");
		
		String breed;
		
		int count = 1;
		
		for(String cd : catDog){
			
			Document doc = getDoc("http://www.fultonanimalservices.com/Fulton_County_Animal_Services/Pets_for_Adoption.php?keywordsearch=&species="+cd+"&age=all&size=all&sex=all&breed=all&submit=Search");
			doc.select(".pagination > a").last().remove();
			
			for(int pages=2; pages <= Integer.valueOf(doc.select(".pagination > a").last().ownText()); pages++){
				
				Elements pets = doc.getElementsByClass("searchResultsCell");
				
				for(Element petEl : pets){
					breed="";
					print(petEl.select("h4 > a").text());
					print(petEl.select("tr > td > a > img").attr("src"));
					print((petEl.select("tr > td").text().toLowerCase().contains("female") ? "Female" : "Male"));
					//this below captures the breed
					String[] breedArr = petEl.select("tr > td").text().split(" ");
					
					for(int x=6; x < breedArr.length; x++){
						String br = breedArr[x];
						breed = breed + br.trim() + " ";
					}
					print(breed);
					
					pet.setImageUrl(petEl.select("tr > td > a > img").attr("src"));
					pet.setSpecies(cd);
					pet.setGender((petEl.select("tr > td").text().toLowerCase().contains("female") ? "Female" : "male"));
					pet.setBreed(breed);
					pet.setColorOfCoat("egyptian cotton");
					pet.setEyeColor("Elizabeth Taylor Purple");
					pet.setLocationStatus("Shelter");
					pet.setCurrentLocation("civic hackathon, 6am, no sleep");
					pet.setIdentityMarks("lower lip above upper lip");
					pet.setPetId("3.14159");//guess!
					petCollector.add(pet);
					pet = new Pet();
					print("" + count++);
				}
				
				
				
				doc = getDoc(doc.select(".pagination > a").attr("href").replaceAll("page=.*", "page="+String.valueOf(pages)));
				doc.select(".pagination > a").last().remove();
			}
			
		}
	}
	//Connects to the various databases
	public Document getDoc(String link) throws Exception{
		return Jsoup.connect(link).timeout(20*1000).get();
	}
	//Convenience method
	static public void print(String printThis){
		System.out.println(printThis);
	}
}
