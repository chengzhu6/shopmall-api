package com.castellan.util;

import com.alipay.api.domain.Person;
import com.castellan.pojo.User;
import com.google.common.collect.Lists;
import com.qiniu.util.Json;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.JavaType;
import org.springframework.util.CollectionUtils;

import javax.jws.soap.SOAPBinding;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class JsonUtil {

    public static String serialize(Object object){
        ObjectMapper objectMapper = new ObjectMapper();
        StringWriter stringWriter = new StringWriter();

        JsonGenerator jsonGenerator = null;
        try {
            jsonGenerator = new JsonFactory().createJsonGenerator(stringWriter);
            objectMapper.writeValue(jsonGenerator,object);
            jsonGenerator.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String objectJson = stringWriter.toString();
        return objectJson;
    }






    public static <T> T deserialize(String jsonStr , Class<?> collectionClass, Class<?>... elementClasses) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(jsonStr, getCollectionType(mapper, collectionClass, elementClasses));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> T deserialize(String jsonStr , Class<T> javaType)   {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(jsonStr,javaType);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }



    private static JavaType getCollectionType(ObjectMapper mapper, Class<?> collectionClass, Class<?>... elementClasses) {
        return mapper.getTypeFactory().constructParametricType(collectionClass, elementClasses);
    }




    public static void main(String[] args) {
        User user = new User();
        user.setPassword("aaaa");
        User user2 = new User();
        user2.setPassword("bbbbb");
        List<User> userList = Lists.newArrayList(user,user2);
        String userJson = serialize(user);
        System.out.println(userJson);
        User user4 = deserialize(userJson, User.class);
        System.out.println(user4.getPassword());




    }

}
