package blog.rain.com.rainlog.util;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ImageUtil {

    public static String getImageType(String image) {
        String type = "png";

        String pattern = "^data:image/(.*);base64,";
        Pattern compile = Pattern.compile(pattern);
        Matcher matcher = compile.matcher(image);
        if (matcher.find()) {
            type = matcher.group(1);
        }

        return type;
    }

    public static String getBase64(String image) {
        String base64 = null;

        String pattern = "^data:image/(.*);base64,";
        Pattern compile = Pattern.compile(pattern);
        Matcher matcher = compile.matcher(image);
        if (matcher.find()) {
            base64 = image.substring(matcher.end());
        }

        return base64;
    }

    public static boolean saveImage(byte[] image, String imagePath) throws IOException {
        File imageFile = new File(imagePath);

        if (!imageFile.getParentFile().exists()) {
            imageFile.getParentFile().mkdirs();
        }

        if (!imageFile.exists() || !imageFile.isFile()) {
            imageFile.createNewFile();
        }

        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(imageFile));
        bufferedOutputStream.write(image);
        bufferedOutputStream.flush();
        bufferedOutputStream.close();

        return true;
    }
}
