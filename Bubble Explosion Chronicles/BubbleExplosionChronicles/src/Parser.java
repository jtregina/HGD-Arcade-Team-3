import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.json.*;

public class Parser {
	
	public int [] getLevel(String s) {
	
		try {
			JSONObject obj = new JSONObject(readFile("src/levels/levels.json"));
			
			JSONArray levelObj = obj.getJSONArray(s);
			JSONObject levelLayout = levelObj.getJSONObject(0);
			
			JSONArray layout = levelLayout.getJSONArray("layout");
			int numBubbles = levelLayout.getInt("numBubbles");
			
			int [] level = new int[numBubbles];
			
			for(int i = 0; i < numBubbles; i++) {
				level[i] = layout.getInt(i);
			}
			
			return level;
		} catch (IOException e) {
			return new int[0];
		}	
	}
	
	private String readFile(String file) throws IOException {
	    BufferedReader reader = new BufferedReader(new FileReader (file));
	    String         line = null;
	    StringBuilder  stringBuilder = new StringBuilder();
	    String         ls = System.getProperty("line.separator");

	    try {
	        while((line = reader.readLine()) != null) {
	            stringBuilder.append(line);
	            stringBuilder.append(ls);
	        }

	        return stringBuilder.toString();
	    } finally {
	        reader.close();
	    }
	}
}