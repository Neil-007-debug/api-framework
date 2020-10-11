package org.neil.tf.api.utils;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

public class JsonReaderUtil {

    private final static Logger logger= LoggerFactory.getLogger(JsonReaderUtil.class);

    public static String readJsonFile(String fileName) {
        String path= JsonReaderUtil.class.getClassLoader().getResource(fileName).getPath();
        String jsonStr = "";
        try {
            File jsonFile = new File(path);
            FileReader fileReader = new FileReader(jsonFile);
            Reader reader = new InputStreamReader(new FileInputStream(jsonFile),"utf-8");
            int ch = 0;
            StringBuffer sb = new StringBuffer();
            while ((ch = reader.read()) != -1) {
                sb.append((char) ch);
            }
            fileReader.close();
            reader.close();
            logger.debug("read json file succeed");
            jsonStr = sb.toString();
            return jsonStr;
        } catch (IOException e) {
            logger.error("read json file failed");
            e.printStackTrace();
            return null;
        }
    }

    @Test
    public void testReadJson(){
        String fileName="testcase/createJob.json";
        String json=readJsonFile(fileName);
        System.out.println(json);
    }
}
