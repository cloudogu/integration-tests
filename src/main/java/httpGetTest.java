import com.cloudogu.ces.JenkinsAPI;
import com.fasterxml.jackson.databind.JsonNode;

public class httpGetTest {

        
        public static void jerseyClient(){
            JenkinsAPI api = new JenkinsAPI("admin", "admin123");
            
            JsonNode information = api.getInformation();
            System.out.println(information.get("primaryView").get("url"));
        }
        
        public static void main(String[]args){
            /*String s = new String(); 
            try {
                s = scrape("https://192.168.115.169/jenkins/api","malte","malte123");
            } catch (IOException ex) {
                Logger.getLogger(httpGetTest.class.getName()).log(Level.SEVERE, null, ex);
            }*/
            
            jerseyClient();
        }
}