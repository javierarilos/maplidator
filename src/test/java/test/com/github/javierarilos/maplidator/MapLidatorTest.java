package test.com.github.javierarilos.maplidator;

import static com.github.javierarilos.maplidator.MapLidator.Validation.*;

import java.io.InputStream;
import java.util.Map;

import org.codehaus.jackson.JsonParser.Feature;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.github.javierarilos.maplidator.MapLidator;

public class MapLidatorTest {
    
    Map<String, Object> data=null;

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() throws Exception {
	InputStream is = MapLidatorTest.class.getClassLoader().getResourceAsStream("json/invoice.json");
        ObjectMapper mapper = new ObjectMapper(); // can reuse, share globally
        mapper.configure(Feature.ALLOW_COMMENTS, true);
        data = mapper.readValue(is, Map.class);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testValidateObjectNotNullSimple() {
	MapLidator.validate(data, "customer", OBJECT, NOT_NULL);
    }
    
    @Test
    public void testValidateObjectNotNullPath() {
	MapLidator.validate(data, "organization.companyAddress", OBJECT, NOT_NULL);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testValidateObjectNotNullFailsSimple() {
	MapLidator.validate(data, "NOTEXIST", OBJECT, NOT_NULL);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testValidateObjectNotNullFailsPath1() {
	MapLidator.validate(data, "organization.NOTEXIST", OBJECT, NOT_NULL);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testValidateObjectNotNullFailsPath2() {
	MapLidator.validate(data, "NOTEXIST.NOTEXIST", OBJECT, NOT_NULL);
    }
    
    @Test
    public void testValidateString(){
	MapLidator.validate(data, "customer.firstName", STRING, NOT_NULL, STRING_NOT_EMPTY);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testValidateStringFails(){
	MapLidator.validate(data, "organization.companyAddress", STRING, NOT_NULL, STRING_NOT_EMPTY);
    }
    
    @Test
    public void testValidateNullableString(){
	MapLidator.validate(data, "NOTEXIST", STRING, STRING_NOT_EMPTY);
    }
    
    @Test
    public void testValidateNullableObject(){
	MapLidator.validate(data, "NOTEXIST", OBJECT);
    }
    
    @Test
    public void testValidateNullableArray(){
	MapLidator.validate(data, "NOTEXIST", ARRAY);
    }
    
    @Test
    public void testValidateNullableArrayExists(){
	MapLidator.validate(data, "order", ARRAY);
    }
    
    @Test
    public void testValidateNonNullableArrayExists(){
	MapLidator.validate(data, "order", ARRAY, NOT_NULL);
    }
    

}
