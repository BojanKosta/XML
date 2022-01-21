package bojankosta.xml_data.utility;

import bojankosta.xml_data.model.Continent;
import bojankosta.xml_data.model.Country;
import bojankosta.xml_data.model.User;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

public class PrintAllHandler extends DefaultHandler {

    private StringBuilder currentValue = new StringBuilder();
    List<Continent> continentList;
    Continent courentContinent;
    List<Country> countryList;
    Country courentCountry;
    List<User> userList;
    User courentUser;

    public List<Continent> getContinentList(){
        return continentList;
    }

    @Override
    public void startDocument() throws SAXException {
        continentList = new ArrayList<>();
        countryList = new ArrayList<>();
        userList = new ArrayList<>();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        currentValue.setLength(0);

        if(qName.equalsIgnoreCase("continent")){
            courentContinent = new Continent();
            String name = attributes.getValue("name");
            courentContinent.setName(name);
        }

        if(qName.equalsIgnoreCase("country")){
            courentCountry = new Country();
            String name = attributes.getValue("name");
            courentCountry.setName(name);
        }

        if(qName.equalsIgnoreCase("user")){
            courentUser = new User();
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {

        if (qName.equalsIgnoreCase("first_name")) {
            courentUser.setFirst_name(currentValue.toString());
        }

        if (qName.equalsIgnoreCase("last_name")) {
            courentUser.setLast_name(currentValue.toString());
        }

        if (qName.equalsIgnoreCase("address")) {
            courentUser.setAddress(currentValue.toString());
        }

        if (qName.equalsIgnoreCase("city")) {
            courentUser.setCity(currentValue.toString());
        }

        if (qName.equalsIgnoreCase("email")) {
            courentUser.setEmail(currentValue.toString());
        }

        if (qName.equalsIgnoreCase("password")) {
            courentUser.setPassword(currentValue.toString());
        }

        if(qName.equalsIgnoreCase("continent")){
            courentContinent.setCountryList((ArrayList<Country>) countryList);
            continentList.add(courentContinent);
            countryList = new ArrayList<>();
        }

        if(qName.equalsIgnoreCase("country")){
            courentCountry.setUserList((ArrayList<User>) userList);
            countryList.add(courentCountry);
            userList = new ArrayList<>();
        }

        if(qName.equalsIgnoreCase("user")){
            userList.add(courentUser);
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        currentValue.append(ch, start, length);
    }
}
