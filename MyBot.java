/*
THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.

 */

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import org.json.*;
import java.util.Random;

public class MyBot{

    //Use SOLR to get the data needed in JSON format, add the PID data in an array list and use random elements 
    //of this arraylist to add at the end of  url for the tweets
    public static void main(String... args) throws TwitterException, Exception{
       String SOLR_DATA = "YOUR_SORL_URL/select?q=*&fl=PID,+dc.description&wt=json&indent=true&rows=50000\";";
       String jsonString =  readUrl(SOLR_DATA);
       JSONObject myjson = new JSONObject(jsonString);
       JSONObject myjsonDocs = myjson.getJSONObject("response");
       JSONArray ja = myjsonDocs.getJSONArray("docs");
       ArrayList<String> PID = new ArrayList<String>();
       int resultCount = ja.length();
        for (int i = 0; i < resultCount; i++)
        {
            JSONObject resultObject = ja.getJSONObject(i);
            String resultObjectPid = resultObject.getString("PID");
            PID.add(resultObjectPid);
        }
        Random randomGenerator = new Random();
        int index = randomGenerator.nextInt(PID.size());
        String item = (String) PID.get(index);
        //access the twitter API using your twitter4j.properties file
        Twitter twitter = TwitterFactory.getSingleton();

        String message = "YOUR_WEBSITE_URL/islandora/object/" + item;
        //tweet the url
        Status status = twitter.updateStatus(message);

        
    }

  //method to read the data from the SOLR URL
  private static String readUrl(String urlString) throws Exception {
    BufferedReader reader = null;
    try {
        URL url = new URL(urlString);
        reader = new BufferedReader(new InputStreamReader(url.openStream()));
        StringBuilder buffer = new StringBuilder();
        int read;
        char[] chars = new char[1024];
        while ((read = reader.read(chars)) != -1)
            buffer.append(chars, 0, read);

        return buffer.toString();
    } finally {
        if (reader != null)
            reader.close();
    }
}
}
