package response;

import java.util.Date;

/**Implement a deserializer and serializer for every entry in Question.Response_Type*/
public class ResponseSerializer {
	
	public static String Serialize(String in){
		return in;
	}
	public static String DeSerialize_String(String in){
		return in;
	}
	
	public static String Serialize(Date in){
		return String.valueOf(in.getTime());
	}
	public static Date DeSerialize_Date(String in){
		return new Date(Long.valueOf(in));
	}
	
	public static String Serialize(int in){
		return String.valueOf(in);
	}
	public static int DeSerialize_Int(String in){
		return Integer.valueOf(in);
	}
	
	public static String Serialize(float in){
		return String.valueOf(in);
	}
	public static float DeSerialize_Float(String in){
		return Float.valueOf(in);
	}
	
	public static String Serialize(boolean in){
		return String.valueOf(in);
	}
	public static boolean DeSerialize_Bool(String in){
		return Boolean.valueOf(in);
	}
	
	public static String Serialize(char in){
		return String.valueOf(in);
	}
	public static char DeSerialize_Char(String in){
		return in.charAt(0);
	}
}
