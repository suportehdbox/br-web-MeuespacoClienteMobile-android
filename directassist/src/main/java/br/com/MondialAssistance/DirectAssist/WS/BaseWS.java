package br.com.MondialAssistance.DirectAssist.WS;

public class BaseWS {

	private static String Token = "3d5900ae-111a-45be-96b3-d9e4606ca793";
	private static String TokenMapLink = "bw9hz0LHbWVH5nkgbCFKawZsf0BibMSpywjk";

	private static int TypeAcess = 1;
	private static String devURL = "http://desenv.webmondial.com.br/"; //TypeAcess=0
	private static String prodURL = "https://www.webmondial.com.br/"; //TypeAcess=1

	private static String AutomakerURL = "directassistws/v2/automaker.asmx";
	private static String AutomotiveURL = "directassistws/v2/automotive.asmx";
	private static String DirectAssistURL = "directassistws/v2/directassistws.asmx";
	private static String PropertyURL = "directassistws/v2/property.asmx";
	private static String MaplinkAddressFinderURL = "http://webservices.maplink3.com.br/webservices/v3/addressfinder/addressfinder.asmx";


	public static String getToken() {
		return Token;
	}
	
	public static String getTokenMapLink(){
		return TokenMapLink;
	}
	
	public static void setTypeAcess(int value){
		TypeAcess = value;
	}
	
	public static String getAutomakerURL() {
		return ((TypeAcess == 0) ? devURL : prodURL) + AutomakerURL;
	}
	
	public static String getAutomotiveURL() {
		return ((TypeAcess == 0) ? devURL : prodURL) + AutomotiveURL;
	}
	
	public static String getDirectAssistURL() {
		return ((TypeAcess == 0) ? devURL : prodURL) + DirectAssistURL;
	}
	
	public static String getPropertyURL() {
		return ((TypeAcess == 0) ? devURL : prodURL) + PropertyURL;
	}
	
	public static String getMaplinkAddressFinderURL() {
		return MaplinkAddressFinderURL;
	}
}
