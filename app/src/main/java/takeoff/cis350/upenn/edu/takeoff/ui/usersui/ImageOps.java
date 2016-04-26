package takeoff.cis350.upenn.edu.takeoff.ui.usersui;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.net.Uri;
import android.provider.MediaStore;

import org.apache.commons.codec.binary.Base64;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created by dylan on 4/23/16.
 */
public class ImageOps {

    private static int profileDimension = 336;
    private static int iconDimension = 50;

    /**
     * Code sourced from the following:
     *  https://stackoverflow.com/questions/11202754/android-how-to-enlarge-a-bitmap
     *  https://stackoverflow.com/questions/2789276/android-get-real-path-by-uri-getpath
     *  https://stackoverflow.com/questions/10796660/convert-image-to-bitmap
     *  https://stackoverflow.com/questions/13562429/how-many-ways-to-convert-bitmap-to-string-and-vice-versa
     *  https://stackoverflow.com/questions/14050813/how-to-make-an-image-fit-into-a-circular-frame-in-android
     */

    /**
     * Scales the bitmap of a profile picture to fit the screen
     * @param bitmap the profile picture bitmap
     * @return the new scaled bitmap
     */
    public static Bitmap scaleProfilePictureBitmap(Bitmap bitmap) {
        //get the original width and height
        return Bitmap.createScaledBitmap(bitmap, profileDimension, profileDimension, true);
    }

    /**
     * Turns a profile bitmap image into a small, round icon
     * @param bitmap
     * @return
     */
    public static Bitmap createProfileIcon(Bitmap bitmap) {
        Bitmap scaled = Bitmap.createScaledBitmap(bitmap, 100, 100, true);

        Bitmap result = Bitmap.createBitmap(200, 200, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(result);

        int color = 0xff424242;
        Paint paint = new Paint();
        Rect rect = new Rect(0, 0, 200, 200);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawCircle(50, 50, 50, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(scaled, rect, rect, paint);

        return result;
    }

    /**
     * Get an image's filepath from the Uri object
     * @param contentURI the uri
     * @param c the context of the app
     * @return the filepath
     */
    public static String getRealPathFromURI(Uri contentURI, Context c) {
        String result;
        Cursor cursor = c.getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }


    //

    /**
     * Convert an image to a bitmap
     * @param path the string to the path of the file
     * @return the new bitmap
     */
    public static Bitmap pathToBitmap(String path) {
        try {
            FileInputStream in = new FileInputStream(path);
            BufferedInputStream buf = new BufferedInputStream(in);
            byte[] bMapArray= new byte[buf.available()];
            buf.read(bMapArray);
            Bitmap bMap = BitmapFactory.decodeByteArray(bMapArray, 0, bMapArray.length);
            return bMap;
        } catch (IOException e) {
            // TODO: Handle
        }
        return null;
    }

    /**
     * Convert a bitmap image to a base64 encoded string
     * @param bitmap the image to convert
     * @return the base64 encoded string
     */
    public static String bitmapToString(Bitmap bitmap){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte [] b = baos.toByteArray();
        Base64 b64 = new Base64();
        String temp = new String(Base64.encodeBase64(b));
        return temp;
    }

    /**
     * Convert a string (base64 binary encoded) to a bitmap image
     * @param encodedString the base64 string
     * @return the new bitmap created
     */
    public static Bitmap stringToBitmap(String encodedString){
        try {
            byte[] decodedString = (new Base64()).decode(encodedString.getBytes());
            Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            return bitmap;
        } catch(Exception e) {
            // TODO: Handle
            e.getMessage();
            return null;
        }
    }



}
