//This can become an interface later if need be. Cat and Dog as their own classes
//but the use case seems to be that they have the same attributes so I placed them 
//here.
public class Pet {

	private String imageUrl = "";
	private String species = "";
	private String gender = "";
	private String breed = "";
	private String colorOfCoat = "";
	private String eyeColor = "";
	private String locationStatus = "";
	private String currentLocation = "";
	private String identityMarks = "";
	private String petId = "";
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public String getSpecies() {
		return species;
	}
	public void setSpecies(String species) {
		this.species = species;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getBreed() {
		return breed;
	}
	public void setBreed(String breed) {
		this.breed = breed;
	}
	public String getColorOfCoat() {
		return colorOfCoat;
	}
	public void setColorOfCoat(String colorOfCoat) {
		this.colorOfCoat = colorOfCoat;
	}
	public String getEyeColor() {
		return eyeColor;
	}
	public void setEyeColor(String eyeColor) {
		this.eyeColor = eyeColor;
	}
	public String getLocationStatus() {
		return locationStatus;
	}
	public void setLocationStatus(String locationStatus) {
		this.locationStatus = locationStatus;
	}
	public String getCurrentLocation() {
		return currentLocation;
	}
	public void setCurrentLocation(String currentLocation) {
		this.currentLocation = currentLocation;
	}
	public String getIdentityMarks() {
		return identityMarks;
	}
	public void setIdentityMarks(String identityMarks) {
		this.identityMarks = identityMarks;
	}
	public String getPetId() {
		return petId;
	}
	public void setPetId(String petId) {
		this.petId = petId;
	}
	
	
	
	
}
