package cn.slqwq.bing;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * @author Hajeekn
 * @date 2021/04/03
 * @link https://github.com/glahajeekn
 */
public class Wallpaper {

    private static String BING_API = "https://cn.bing.com/HPImageArchive.aspx?format=js&idx=0&n=10&nc=1612409408851&pid=hp&FORM=BEHPTB&uhd=1&uhdwidth=3840&uhdheight=2160";

    private static String BING_URL = "https://cn.bing.com";

    public static void main(String[] args) throws IOException {
        String httpContent = HttpUtls.getHttpContent(BING_API);
        JSONObject jsonObject = JSON.parseObject(httpContent);
        JSONArray jsonArray = jsonObject.getJSONArray("images");

        jsonObject = (JSONObject)jsonArray.get(0);
        String url = BING_URL + (String)jsonObject.get("url");
        url = url.substring(0, url.indexOf("&"));

        String enddate = (String)jsonObject.get("enddate");
        LocalDate localDate = LocalDate.parse(enddate, DateTimeFormatter.BASIC_ISO_DATE);
        enddate = localDate.format(DateTimeFormatter.ISO_LOCAL_DATE);

        String copyright = (String)jsonObject.get("copyright");

        List<Images> imagesList = FileUtils.readBing();
        imagesList.set(0,new Images(copyright, enddate, url));
        imagesList = imagesList.stream().distinct().collect(Collectors.toList());
        FileUtils.writeBing(imagesList);
        FileUtils.writeReadme(imagesList);

    }

}
