package bojankosta.xml_data.controller;

import bojankosta.xml_data.model.Continent;
import bojankosta.xml_data.model.User;
import bojankosta.xml_data.model.UserResponse;
import bojankosta.xml_data.service.ContinentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.List;



@RestController
public class BasicController {

       @Autowired
       private ContinentService continentService;

       @GetMapping("/")
       public ModelAndView home() {
              ModelAndView modelAndView = new ModelAndView();
              modelAndView.setViewName("index.html");
              return modelAndView;
       }

       @GetMapping("/data")
       public List<Continent> getAllData() throws ParserConfigurationException, SAXException, IOException {
              return continentService.getAllData();
       }

       @PostMapping("/add")
       public ResponseEntity<UserResponse> addUser(@RequestBody User user){
              System.out.println("New user " + user.getFirst_name() + " " + user.getLast_name());
              return new ResponseEntity<>(continentService.addUser(user), HttpStatus.OK);
       }

       @DeleteMapping("/delete")
       public ResponseEntity<UserResponse> deleteUser(@RequestParam("email") String email) throws JAXBException, IOException, ParserConfigurationException, SAXException {
              return new ResponseEntity<UserResponse>(continentService.deleteUser(email), HttpStatus.OK);
       }
}


