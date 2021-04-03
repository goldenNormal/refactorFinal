package visualize;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtils {
    static ObjectMapper mapper;

    static {
        mapper=new ObjectMapper();
    }

    public static ObjectMapper getMapper(){
        return mapper;
    }

}
