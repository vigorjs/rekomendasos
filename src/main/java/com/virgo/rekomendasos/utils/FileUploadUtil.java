package com.virgo.rekomendasos.utils;

import lombok.experimental.UtilityClass;
import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@UtilityClass
public class FileUploadUtil {

    public static final long MAX_FILE_SIZE = 2 * 1024 * 1024;

    public static final String IMAGE_PATTERN = "([^\\s]+(\\.(?i)(jpe?g|png|gif|bmp))$)";

    public static final String DATE_FORMAT = "yyyMMddHHmmss";

    public static final String FILE_NAME_FORMAT = "%s_%s";

    public static boolean isAllowedExtension(String fileName, String pattern){
        final Matcher matcher = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE).matcher(fileName);
        return matcher.matches();
    }

    public static void assertAllowed(MultipartFile file, String pattern) {
        final long size = file.getSize();
        if (size > MAX_FILE_SIZE){
            throw new IllegalArgumentException("max file size is 2MB");
        }

        final String fileName = file.getOriginalFilename();
//        final String extension = FilenameUtils.getExtension(fileName);

        if (!isAllowedExtension(fileName, pattern)){
            throw new IllegalArgumentException("extensi image salah");
        }
    }

    public static String getFileName(String name) {
        final DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        final  String date = dateFormat.format(System.currentTimeMillis());
        return String.format(FILE_NAME_FORMAT, name, date);
    }
}
