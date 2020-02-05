package helper;

public class StringUtils {
    public static boolean  isAllUpperCase(String s){
        for (int i = 0; i < s.length(); i++){
            char c = s.charAt(i);
            if(Character.isLowerCase(c)){
                return  false;
            }
        }
        return true;

    }

    public static boolean  isAllLowerCase(String s){
        for (int i = 0; i < s.length(); i++){
            char c = s.charAt(i);
            if(Character.isUpperCase(c)){
                return  false;
            }
        }
        return true;

    }
}
