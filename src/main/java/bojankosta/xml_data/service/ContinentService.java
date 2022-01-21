package bojankosta.xml_data.service;

import bojankosta.xml_data.model.*;
import bojankosta.xml_data.utility.PrintAllHandler;
import bojankosta.xml_data.XmlDataApplication;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.awt.*;
import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.List;

@Service
public class ContinentService {

    public List<Continent> getAllData()  {

        String fileName = "C:\\Users\\BojanKosta\\Desktop\\java\\xml_data\\src\\main\\resources\\data.xml";
        List<Continent> result = new ArrayList<>();

      try{
          SAXParserFactory factory = SAXParserFactory.newInstance();
          InputStream is = new FileInputStream(fileName);
          SAXParser saxParser = factory.newSAXParser();

          // parse XML and map to object
          PrintAllHandler handler = new PrintAllHandler();

          saxParser.parse(is, handler);
          is.close();

          // print all
          result = handler.getContinentList();
          //result.forEach(System.out::println);

        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
       }
        return result;
    }

    public UserResponse addUser(User newUser){
        List<Continent> listData = getAllData();
        String email = newUser.getEmail();
        UserResponse userResponse = new UserResponse("success", email);
        //System.out.println(newUser.getFirst_name());

        // check if user exist if True return response
        boolean userExist = userExist(listData, email);
        if(userExist){
            userResponse.setMessage("fail");
            return userResponse;
        }


        Map<String, String[]> continentList = new HashMap<>();
        continentList.put("Europe", new String[]{ "United Kingdom", "Germany", "France" });
        continentList.put("North America", new String[]{ "United States", "Canada" });
        continentList.put("Asia", new String[]{ "China", "Japan" });

        // checks witch country belogs to the witch continent and then ckecks if it exist in the xml file
        String countryBelong = countryBelong(continentList, newUser.getCountry());
        System.out.println(countryBelong);

        boolean continentExist = continentExist(listData, countryBelong);
        System.out.println("Continent exist " + continentExist);

        if(!continentExist){
            Continent newContinent = new Continent();
            newContinent.setName(countryBelong);
            //System.out.println(newContinent.getName());

            Country newCountry = new Country();
            newCountry.setName(newUser.getCountry());
            //System.out.println(newCountry.getName());

            ArrayList<User> userList = new ArrayList<>();
            userList.add(newUser);
            newCountry.setUserList(userList);

            ArrayList<Country> countryList = new ArrayList<>();
            countryList.add(newCountry);
            newContinent.setCountryList(countryList);
            listData.add(newContinent);

            //add new user to xml file
            createXML(listData);
            return userResponse;
        }

        boolean countryExist =  countryExist(listData, newUser.getCountry());
        System.out.println("Country exist " +countryExist);
        if(!countryExist){
            for(Continent continent : listData){
                if(continent.getName().equalsIgnoreCase(countryBelong)){

                    Country country = new Country();
                    country.setName(newUser.getCountry());
                    ArrayList<User> userList = new ArrayList<>();
                    userList.add(newUser);
                    country.setUserList(userList);
                    continent.getCountryList().add(country);
                    //System.out.println(continent);
                    //add new user to xml file
                    createXML(listData);
                    return userResponse;
                }
            }
        }else {

            boolean isUserAdded = false;
            for (Continent continent : listData) {
                if (isUserAdded) {
                    break;
                }

                for (Country country : continent.getCountryList()) {
                    if (isUserAdded) {
                        break;
                    }

                    if (newUser.getCountry().equalsIgnoreCase(country.getName())) {
                        country.getUserList().add(newUser);
                        isUserAdded = true;

                        //add new user to xml file
                        createXML(listData);
                        break;
                    }

                }
            }
        }
        return userResponse;
    }

    private boolean continentExist(List<Continent> listData, String newContinent) {

        for(Continent continent : listData){
            if(continent.getName().equalsIgnoreCase(newContinent)){
                return true;
            }
        }
        return false;
    }

    private String countryBelong(Map<String, String[]> continentList, String newCountry) {
        for(Map.Entry<String, String[]> continent : continentList.entrySet()){
            for (String country : continent.getValue()) {
                if(country.equalsIgnoreCase(newCountry)){
                    return continent.getKey();
                }
            }
        }
        return "N";
    }

    private boolean countryExist(List<Continent> listData, String newCountry) {
        boolean countryExist = false;
        for(Continent continent : listData){
            if(countryExist){
                break;
            }

            for(Country country : continent.getCountryList()){
                if(countryExist){
                    break;
                }

                if(newCountry.equalsIgnoreCase(country.getName())){
                    countryExist = true;
                    break;
                }

            }
        }
        return countryExist;
    }

    private boolean userExist(List<Continent> listData, String email) {
        boolean exist = false;
        for(Continent continent : listData){
            if(exist){
                break;
            }
            for(Country country : continent.getCountryList()){
                if(exist){
                    break;
                }
                for (User user : country.getUserList()){
                    if(email.equalsIgnoreCase(user.getEmail())){
                        exist = true;
                        break;
                    }
                }

            }
        }
        return exist;
    }

    public UserResponse deleteUser(String email) {
        List<Continent> listData = getAllData();
        UserResponse userResponse = new UserResponse("fail", email);

        boolean isDeleted = false;
        for(Continent continent : listData){
            if(isDeleted){
                break;
            }
            for(Country country : continent.getCountryList()){
                if(isDeleted){
                    break;
                }
                int index = 0;
                for (User user : country.getUserList()){
                    if(email.equalsIgnoreCase(user.getEmail())){
                        country.getUserList().remove(index);
                        userResponse.setMessage("success");
                        isDeleted = true;
                        break;
                    }
                    index++;
                }

            }
        }

        createXML(listData);

        return userResponse;
    }

    private void createXML(List<Continent> listData) {

        StringBuilder sb = new StringBuilder();

        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n");
        sb.append("<data>\n");

        for(Continent continent : listData){
            //System.out.println(continent.getName());
            sb.append("    <continent name=\""+continent.getName() + "\">\n");

            for(Country country : continent.getCountryList()){
                sb.append("       <country name=\"" + country.getName() + "\">\n");
                //System.out.println(country.getName());
                for (User user : country.getUserList()){

                    sb.append(
                            "           <user>\n" +
                                    "               <first_name>" + user.getFirst_name() + "</first_name>\n" +
                                    "               <last_name>"+ user.getLast_name() + "</last_name>\n" +
                                    "               <address>" + user.getAddress() + "</address>\n" +
                                    "               <city>" + user.getCity() + "</city>\n" +
                                    "               <email>" + user.getEmail() + "</email>\n" +
                                    "               <password>" + user.getPassword() + "</password>\n" +
                                    "           </user>\n");
                }
                sb.append("       </country>\n");
            }
            sb.append("   </continent>\n");
        }
        sb.append("</data>");

        //System.out.println(sb.toString());
        writeIntoXml(sb.toString());

    }

    public void writeIntoXml(String data){

        String fileName = "C:\\Users\\BojanKosta\\Desktop\\java\\xml_data\\src\\main\\resources\\data.xml";

        try(PrintWriter pw = new PrintWriter(fileName)){
            pw.print("");
        }catch (IOException e){
            e.printStackTrace();
        }

        try (Writer writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write(data);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
