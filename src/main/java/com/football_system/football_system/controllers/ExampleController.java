package com.football_system.football_system.controllers;


import com.football_system.football_system.logicTest.SecurityObject;
import com.football_system.football_system.logicTest.UserTest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * ExampleController
 *
 * @author danielpadua
 *
 */
@RestController
public class ExampleController {

    private List<UserTest> Users = new LinkedList<>();
    public ExampleController(){
        Users.add(new UserTest("david" , "david@gmai.com"));
        Users.add(new UserTest("eitan" , "eitan@gmai.com"));
        Users.add(new UserTest("lior" , "lior@gmai.com"));
    }
    @RequestMapping("/hi")
    public ResponseEntity<String> get() {
        return ResponseEntity.ok("Hello World!");
    }

    @RequestMapping("/Users")
    public List<UserTest> getUsers() {
        return Users;
    }

    @RequestMapping("/Users/{name}")
    public UserTest getUsers(@PathVariable String name) {
        return Users.stream().filter(t-> t.getName().equals(name)).findFirst().get() ;
    }

    @PostMapping("/addUser")
    boolean newEmployee(@RequestBody UserTest newUser) {
        return Users.add(newUser);
    }

    @RequestMapping("/securityObjectCheck")
    SecurityObject securityObjectCheck() {
        List<Object> list = new LinkedList<>();
        list.add(new UserTest("avi","avi@walla.com"));
        SecurityObject so = new SecurityObject("id", "123456", "add User", list);
        return so;
    }


    /**
     * demo post req -> {"reqID":"123456","functionName":"add User","object":[{"name":"avi","mail":"avi@walla.com"}]}
     * @param securityObject
     * @return
     * @throws Exception
     */
    @RequestMapping(
            value = "/postSecurity",
            method = RequestMethod.POST)
    public boolean process(@RequestBody SecurityObject securityObject)
            throws Exception {
        if(securityObject.getReqID().equals("123456")){
            if(securityObject.getFunctionName().equals("add User")){
                return Users.add(new UserTest((LinkedHashMap<String,String>)securityObject.getObject().get(0)));
            }
        }
        return false;
    }


    /***
     * demo post req ->
     * {
     *     "ReqID":  "123456",
     *     "User":{
     *         "name": "ko",
     *         "mail": "kobigal@kal"
     *     }
     * }
     * @param payload
     * @return
     * @throws Exception
     */
    @RequestMapping(
            value = "/process",
            method = RequestMethod.POST)
    public boolean process(@RequestBody Map<String, Object> payload)
            throws Exception {
        if(payload.containsKey("ReqID") && payload.get("ReqID").equals("123456")){
            Object temp = payload.get("User") ;
//            ObjectMapper objectMapper = new ObjectMapper();
//            //String json = objectMapper.writeValueAsString(temp);
//            String json = "{ \"name\" : \"lior\", \"mail\" : \"BMW@gamai\" }";
//            User userT = objectMapper.readValue(json, User.class);
//          //  System.out.println(temp.toString());
//          //
            if(temp instanceof Map) {
                String name = ((Map) temp).get("name").toString();
                String mail = ((Map) temp).get("mail").toString();
                return Users.add(new UserTest(name,mail));
            }

        }
        return false;
    }


//
//    @RequestMapping("/htmlTest")
//    public String html()
//            throws Exception {
//       File file = new File(getClass().getResource("/public/TESThtmi.html").getFile());
//        String content = "";
//        try {
//            BufferedReader in = new BufferedReader(new FileReader(file.getPath().toString()));
//            String str;
//            while ((str = in.readLine()) != null) {
//                content +=str;
//            }
//            in.close();
//        } catch (IOException e) { }
//        return content ;
//    }
}
