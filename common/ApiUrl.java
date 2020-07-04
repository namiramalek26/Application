package com.hotapps.easyplant.common;

public class ApiUrl {

    public static final String baseUrl = "https://plantreco.000webhostapp.com/";
    public static final String login = baseUrl + "login.php";
    public static final String registerUser = baseUrl + "user_register.php";
    public static final String updateProfilePicture = baseUrl + "update_profile_pic.php";
    public static final String updateProfile = baseUrl + "update_profile.php";
    public static final String getUserDetailsFromMobile = baseUrl + "getUserDetails.php";
    public static final String plant_api_url = "https://api.plant.id";
    public static final String getIdUrl = plant_api_url + "/identify";
    public static final String chekIdentification = plant_api_url + "/check_identifications";
    public static String getAllFlower = baseUrl+"getAllFlower.php";
    public static String getSearchHistory =  baseUrl +"getSearchHistory.php";
    public static String searchPlant =  baseUrl + "searchPlant.php";
    public static String addSearchHistory = baseUrl+ "addSearchHistory.php";
}
